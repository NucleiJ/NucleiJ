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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Andreas Mattes and Stefan Erben on 23.01.17.
 */

public class AnalyzerConverterTask extends SwingWorker<String, String> {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());
    public volatile  boolean finishedCancel;
    private JProgressBar   progressBar;
    private TaskDialog     taskDialog;
    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter  ndpiConverter;
    private volatile boolean requestCancel;

    public AnalyzerConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;

        this.requestCancel = false;
        this.finishedCancel = false;
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

        switch (choice) {
            case 0:
                add = 100 / (numberNdpiFiles * 2 + numberTifFiles);
                taskDialog.setInstruction("Converting & analyzing your Scans...");
                break;
            case 1:
                add = 100 / numberNdpiFiles;
                taskDialog.setInstruction("Converting your Scans...");
                break;
            case 2:
                add = 100 / numberTifFiles;
                taskDialog.setInstruction("Analyzing your Scans...");
                break;
            default:
                add = 0;
        }

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

        if (choice != 1) {
            tifFileList = glScanAnalyzer.getTifList();
            for (String tifListElement : tifFileList) {
                if (!requestCancel) {
                    numberTifFiles--;

                    publish(tifListElement.substring(tifListElement.lastIndexOf(File.separator) + 1));

                    mainAnalyzer.setDateiname(tifListElement);
                    System.out.println("\n****************\n" + tifListElement + "\n********************");
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

        taskDialog.setText(element.toString());
    }

    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        progressBar.setValue(100);
        taskDialog.setVisible(false);
    }

    public void stopProcess(boolean requestCancel) {
        this.requestCancel = requestCancel;
        taskDialog.setInstruction("Prozess stoppen...");
    }

    private void startConverter(String filePath) {
        String OS = System.getProperty("os.name").toLowerCase();
        File outputpath = new File(ndpiConverter.getOutputpath());

        //region ################ NDPI-Converter JAR ################
        File jarPath = new File("lib/ndpi-converter/ndpi-converter.jar");
        String absolutePathofNdpiJar = jarPath.getAbsolutePath();
        if (jarPath.exists()) {
            absolutePathofNdpiJar = jarPath.getAbsolutePath();
        } else {
            try {
                File newjarPath = new File(NucleiJ.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());

                //concat(File.separator).concat("ndpi-converter").concat(File.separator).concat("ndpi-converter.jar"));
                System.out.println("Pfad der JAR: " + newjarPath.getParent().concat(File.separator).concat("ndpi-converter").concat(File.separator).concat("ndpi-converter.jar"));
                if (newjarPath.exists()) {
                    absolutePathofNdpiJar = newjarPath.getParent().concat(File.separator).concat("ndpi-converter").concat(File.separator).concat("ndpi-converter.jar");
                    System.out.println("Pfad der JAR: " + absolutePathofNdpiJar);
                } else {
                    newjarPath = null;
                    if (OS.contains("linux")) {
                        System.out.println("Your OS is Linux");
                    } else if (OS.contains("windows")) {
                        System.out.println("Your OS is Windows");
                        newjarPath = new File("C:\\Program Files\\NucleiJ\\ndpi-converter.jar");
                    } else if (OS.contains("mac")) {
                        System.out.println("Your OS is Mac OS");
                    } else {
                        System.out.println("Your OS is not supported!");
                    }

                    if (newjarPath.exists()) {
                        absolutePathofNdpiJar = newjarPath.getAbsolutePath();
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
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        // endregion
        System.out.println("\nAbsolutePathOfNdpi" + absolutePathofNdpiJar);
        System.out.println("FilePath" + filePath);
        System.out.println("OutputPath.getParent" + outputpath.getParent());

        Process p;

        try {
            if (OS.contains("linux")) {
                System.out.println("Your OS is Linux");

                p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "2", "-c", "lzw", "-s", filePath, outputpath.getParent()});
            } else if (OS.contains("windows")) {
                System.out.println("Your OS is Windows");
                //p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath + "\" \"" + outputpath.getParent() + "\"");
                p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "2", "-c", "lzw", "-s", filePath, outputpath.getParent()});
            } else if (OS.contains("mac")) {
                p = Runtime.getRuntime().exec(new String[]{"java", "-jar", absolutePathofNdpiJar, "-i", "2", "-c", "lzw", "-s", filePath, outputpath.getParent()});
                System.out.println("Your OS is Mac OS");
            } else {
                System.out.println("Your OS is not supported!");
                p = null;
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
        String newTifListElement = filePath.replace(".ndpi", renameFileName);
        System.out.println("Der Filename nach dem Konvertieren ist:" + newTifListElement);
        glScanAnalyzer.addTifToList(newTifListElement);

        // TODO Ausgabe beim dialog verbessern, wenn zB eine datei konv & analysiert werden soll dann muss tif Counter erhoeht werden
        // gehoert dort hin wo andis abfrage ist um nur k oder a werden soll
        //ndpiConverter.setNumberTifFiles(ndpiConverter.getNumberTifFiles()+1);
    }

    public boolean isFinishedCancel() {
        return finishedCancel;
    }
}
