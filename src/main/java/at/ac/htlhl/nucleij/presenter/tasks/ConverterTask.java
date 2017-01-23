package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.ezware.dialog.task.TaskDialog;
import ij.IJ;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConverterTask extends SwingWorker<String, Integer> {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private NdpiConverter ndpiConverter;
    private GLScanAnalyzer glScanAnalyzer;
    private List<String> ndpiFileList;
    private List<String> tifFileList;


    public ConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
    }

    protected String doInBackground() throws Exception {
        int i = 0;
        int add;
        int counter = 0;
        LOGGER.log(Level.INFO, "Converting Process started!");

        ndpiFileList = glScanAnalyzer.getNdpiList();

        for (String temp : ndpiFileList) {
            counter++;
        }
        add = 100/counter;

        for (String ndpiListElement : ndpiFileList) {
            startConverter(ndpiListElement);
            publish(i+add);
        }

        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks);
        for (int value : chunks) {
            progressBar.setValue(value);
        }
    }

    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        taskDialog.setVisible(false);

    }

    private void startConverter(String filePath) {
        File outputpath = new File(ndpiConverter.getOutputpath());

        File file = new File("lib/ndpi-converter/ndpi-converter.jar");
        String absolutePathofNdpiJar = file.getAbsolutePath();
        System.out.println(absolutePathofNdpiJar);

        try {
            Process p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                    "Fehler beim Konvertieren: ","NucleiJ-Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
        String newTifListElement = filePath.replace(".ndpi", renameFileName);
        System.out.println("Der Filename nach dem Konvertieren ist:" + newTifListElement);
        glScanAnalyzer.addTifToList(newTifListElement);
    }
}

