package at.ac.htlhl.nucleij.presenter.tasks;

import com.ezware.dialog.task.TaskDialog;

import javax.swing.*;
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

    public AnalyzerTask(JProgressBar progressBar, TaskDialog taskDialog)
    {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
    }

    protected String doInBackground() throws Exception
    {
        for(int i=1; i<=100; i++)
        {
            // Task
            Thread.sleep(10);
            publish(i);
        }
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
