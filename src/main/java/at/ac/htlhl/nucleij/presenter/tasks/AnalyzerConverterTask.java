package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.NucleiJ;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import com.ezware.dialog.task.TaskDialog;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Andreas Mattes and Stefan Erben on 23.01.17.
 */

public class AnalyzerConverterTask extends SwingWorker<String, String> {
    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    private          JProgressBar   progressBar;
    private          TaskDialog     taskDialog;
    private          GLScanAnalyzer glScanAnalyzer;
    private          NdpiConverter  ndpiConverter;
    private volatile boolean        requestCancel;

    public AnalyzerConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;

        this.requestCancel = false;
    }

    @Override
    protected String doInBackground() throws Exception {
        MainAnalyzer mainAnalyzer = new MainAnalyzer(glScanAnalyzer, ndpiConverter);
        List<String> tifFileList;
        List<String> ndpiFileList = glScanAnalyzer.getNdpiList();
        float currentStatus = 0;
        float add;
        int choice = ndpiConverter.getChoice();
        float numberNdpiFiles = ndpiConverter.getNumberNdpiFiles();
        float numberTifFiles = ndpiConverter.getNumberTifFiles();

        // Schrittweite für ProgressBar bestimmen
        switch (choice) {
            case 0:
                add = 100 / (numberNdpiFiles * 2 + numberTifFiles);
                // Converting & analyzing your Scans...
                taskDialog.setInstruction(bundle.getString("SwingworkerDialog.instruction.ca"));
                break;
            case 1:
                add = 100 / numberNdpiFiles;
                // Converting your Scans...
                taskDialog.setInstruction(bundle.getString("SwingworkerDialog.instruction.c"));
                break;
            case 2:
                add = 100 / numberTifFiles;
                // Analyzing your Scans
                taskDialog.setInstruction(bundle.getString("SwingworkerDialog.instruction.a"));
                break;
            default:
                add = 0;
        }

        // Konvertieren
        if (choice != 2) {
            for (String ndpiListElement : ndpiFileList) {
                if (!requestCancel) {
                    numberNdpiFiles--;

                    publish(ndpiListElement.substring(ndpiListElement.lastIndexOf(File.separator) + 1));

                    startConverter(ndpiListElement);
                    currentStatus = currentStatus + add;

                    progressBar.setValue(Math.round(currentStatus));
                }
            }
        }

        // Analysieren
        if (choice != 1) {
            tifFileList = glScanAnalyzer.getTifList();
            for (String tifListElement : tifFileList) {
                if (!requestCancel) {
                    numberTifFiles--;

                    publish(tifListElement.substring(tifListElement.lastIndexOf(File.separator) + 1));

                    mainAnalyzer.setDateiname(tifListElement);
                    mainAnalyzer.run(null);
                    currentStatus = currentStatus + add;

                    progressBar.setValue(Math.round(currentStatus));
                }
            }
            if (!requestCancel) {
                mainAnalyzer.createSummary();
            }
        }
        return "Finished";
    }

    @Override
    protected void process(List<String> element) {
        super.process(element);

        taskDialog.setText(element.get(0));
    }

    @Override
    protected void done() {
        super.done();
        progressBar.setValue(100);
        taskDialog.setVisible(false);
    }

    public void stopProcess(boolean requestCancel) {
        this.requestCancel = requestCancel;
        taskDialog.setInstruction("Stop...");
    }

    private void startConverter(String filePath) {
        File outputpath = new File(ndpiConverter.getOutputpath());

        //region ################ NDPI-Converter JAR ################
        File jarPath = new File("lib/NucleiJ-Data/ndpi-converter.jar");
        String absolutePathofNdpiJar = jarPath.getAbsolutePath();
        if (jarPath.exists()) {
            absolutePathofNdpiJar = jarPath.getAbsolutePath();
        } else {
            try {
                // Get path of the ndpi-converter.jar
                File newjarPath = new File(NucleiJ.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

                // When found, set path
                if (newjarPath.exists()) {
                    absolutePathofNdpiJar = newjarPath.getParent().concat(File.separator).concat("NucleiJ-Data").concat(File.separator).concat("ndpi-converter.jar");
                } else {
                    JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty(newjarPath.getAbsolutePath())));
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.jar", "jar"));

                    int result = fileChooser.showOpenDialog(parent);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        absolutePathofNdpiJar = selectedFile.getAbsolutePath();
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        // endregion

        Process p;
        try {
            switch (ndpiConverter.getMagnification()) {
                case NdpiConverter.MAG_X10:
                    p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "2", "-c", "lzw", "-s", filePath, outputpath.getParent()});
                    break;
                case NdpiConverter.MAG_X40:
                    p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "1", "-c", "lzw", "-s", filePath, outputpath.getParent()});
                    break;
                case NdpiConverter.MAG_X5:
                    p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "0", "-c", "lzw", "-s", filePath, outputpath.getParent()});
                    break;
                default:    // Standard: X10
                    p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "2", "-c", "lzw", "-s", filePath, outputpath.getParent()});
                    break;
            }

            // Print inputStream
            assert p != null;
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Konvertierte Datei umbenennen und an Tif-Liste zum Analysieren anhängen
        String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
        String newTifListElement = filePath.replace(".ndpi", renameFileName);
        glScanAnalyzer.addTifToList(newTifListElement);
    }
}
