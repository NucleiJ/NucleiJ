package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic.StringAdder;
import com.ezware.dialog.task.TaskDialog;
import ij.plugin.filter.PlugInFilter;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Stefan on 14.11.2016.
 */
@SuppressWarnings("Since15")
public class AnalyzerTask extends SwingWorker<String, Integer>
{
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(AnalyzerTask.class.getName());

    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private GLScanAnalyzer glScanAnalyzer;

    public AnalyzerTask(JProgressBar progressBar, TaskDialog taskDialog, GLScanAnalyzer glScanAnalyzer)
    {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.glScanAnalyzer = glScanAnalyzer;
    }

    protected String doInBackground() throws Exception
    {
        MainAnalyzer mainAnalyzer = new MainAnalyzer(glScanAnalyzer);

        for(int i=1; i<=100; i++)
        {
            // Task
            if (i == 1)
            {



                //Stapelfunktion!!
                int gefundeneneElemente = 0;

                String choosenDirectory = glScanAnalyzer.getInputpath();

                File folder = new File(choosenDirectory.concat("\\"));
                File[] listOfFiles = folder.listFiles();

                for (int k = 0; k < listOfFiles.length; k++) {
                    if (listOfFiles[k].isFile()) {
                        if (listOfFiles[k].getName().endsWith("tif") == true) {
                            gefundeneneElemente++;
                        }
                    }
                }
                int plus = 100/gefundeneneElemente;


                StringAdder summaryStack = new StringAdder();
                StringAdder csvSummaryStack = new StringAdder();
                mainAnalyzer.setSummaryStacks(summaryStack, csvSummaryStack);

                for (int k = 0; k < listOfFiles.length; k++) {
                    if (listOfFiles[k].isFile()) {

                        String dateiname = listOfFiles[k].getName();
                        if (dateiname.endsWith("tif") == true) {
                            gefundeneneElemente++;
                            mainAnalyzer.setDateiname(dateiname);
                            mainAnalyzer.run(null);
                            i = i + plus-1;
                            publish(i);
                        }
                    }
                }
            }
            mainAnalyzer.createSummary();

            publish(i);

        }

        // am Ende Summarys ausgeben
        mainAnalyzer.createSummary();

        return "Finished";
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks);
        for(int value : chunks)
        {
            progressBar.setValue(value);
            //LOGGER.log(Level.INFO, "Value: " + value);
        }
    }



    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        progressBar.setValue(100);
        taskDialog.setVisible(false);

    }
}
