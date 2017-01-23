package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import com.ezware.dialog.task.TaskDialog;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andreas on 23.01.17.
 */

public class AnalyzerConverterTask extends SwingWorker<String, Integer>
{
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;

    public AnalyzerConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer)
    {
        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
    }


    @Override
    protected String doInBackground() throws Exception {
        MainAnalyzer mainAnalyzer = new MainAnalyzer(glScanAnalyzer);
        List<String> tifFileList = glScanAnalyzer.getTifList();
        List<String> ndpiFileList = glScanAnalyzer.getNdpiList();
        int i = 0;
        float currentStatus = 0;
        float add;
        float counter = 0;

        for (String ndpiListElement : ndpiFileList) {
            counter++;
        }
        counter=counter*2;

        for (String tifListElement : tifFileList) {
            counter++;
        }

        add = 100/counter;

        // Konvertieren & an TifListe anhängen
        for (String ndpiListElement : ndpiFileList) {
            startConverter(ndpiListElement);
            currentStatus = currentStatus + add;
            publish(Math.round(currentStatus));
        }

        // Analysieren
        for (String tifListElement : tifFileList)
        {
            mainAnalyzer.setDateiname(tifListElement);
            System.out.println("\n****************\n"+tifListElement+"\n********************");
            mainAnalyzer.run(null);
            currentStatus = currentStatus + add;
            publish(Math.round(currentStatus));
        }
        mainAnalyzer.createSummary();
        return "Finished";
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
        progressBar.setValue(100);
        taskDialog.setVisible(false);
    }

    private void startConverter(String filePath) {
        File outputpath = new File(ndpiConverter.getOutputpath());

        File file = new File("lib/ndpi-converter/ndpi-converter.jar");
        String absolutePathofNdpiJar = file.getAbsolutePath();
        System.out.println(absolutePathofNdpiJar);

        try {
            Process p;
            if (ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X10)) {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            }
            else if(ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X40)) {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 1 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            }
            else {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
                LOGGER.info("Magnification error: set to Std. 10x Magnification");
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO neue files in tif liste
        String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
        String newTifListElement = filePath.replace(".ndpi", renameFileName);
        System.out.println("Der Filename nach dem Konvertieren ist:" + newTifListElement);
        glScanAnalyzer.addTifToList(newTifListElement);
    }
}
