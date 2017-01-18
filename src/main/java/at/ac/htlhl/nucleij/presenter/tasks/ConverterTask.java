package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.converter.MainConverter;
import com.ezware.dialog.task.TaskDialog;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConverterTask extends SwingWorker<String, Integer>
{
    private static final Logger LOGGER = Logger.getLogger(AnalyzerTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private MainConverter mainConverter;
    private NdpiConverter ndpiConverter;

    public ConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter)
    {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;

        mainConverter = new MainConverter(ndpiConverter);
    }

    protected String doInBackground() throws Exception {
        LOGGER.log(Level.INFO, "Converting Process started!");
        mainConverter.startConverter();

        /*for(int i=1; i<=100; i++) {
            // Task
            Thread.sleep(10);
            publish(i);
        }*/
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks);
        for(int value : chunks)
        {
            progressBar.setValue(value);
        }
    }

    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        taskDialog.setVisible(false);

    }
}



/*

public class ConvertWorker extends SwingWorker<Integer, String> {

    private JProgressBar progressBar;
    private Converter standard;
    private JList listProgress;
    private Filechooser general;
    private DefaultListModel listModel;
    private Boolean singleMode;
    private JDialog ProgressDialog;
    private JDialog SummaryDialog;

    public ConvertWorker(JProgressBar progressBar, JList listProgress, Converter standard, Filechooser general, DefaultListModel listModel, Boolean singleMode, JDialog ProgressDialog, JDialog SummaryDialog)
    {
        super();
        this.standard = standard;
        this.general = general;
        this.listProgress = listProgress;
        this.progressBar = progressBar;
        this.listModel = listModel;
        this.singleMode = singleMode;
        this.ProgressDialog = ProgressDialog;
        this.SummaryDialog = SummaryDialog;
    }

    @Override
    protected Integer doInBackground() throws Exception
    {
        if(singleMode) {
            standard.setDir(general.chooser.getSelectedFile().getAbsolutePath());           //Set path of selected file
            System.out.println("Current filepath: " + general.chooser.getSelectedFile().getAbsolutePath());

            String filename = general.chooser.getSelectedFile().getName();
            publish(filename);

            standard.startConverter();
            setProgress(100);
        }

        else {
            File[] filesInDirectory = general.chooser.getCurrentDirectory().listFiles();
            int length = 0;

            for ( File file : filesInDirectory ) {
                if (file.isFile()) {
                    length ++;
                }
            }

            for ( File file : filesInDirectory ) {
                if (file.isFile()) {
                    System.out.println(file);

                    standard.setDir(file.getAbsolutePath());
                    System.out.println("Current filepath: " + file.getAbsolutePath());

                    standard.startConverter();
                    String filename = file.getName();
                    //listModel.addElement(filename);
                    publish(filename);
                    //progressBar.setValue(progressBar.getValue() + 100/length);
                    setProgress(progressBar.getValue() + 100/length);
                }

            }
        }

        return 1;
    }

    @Override
    protected void process(List<String> v) {
        listModel.addElement(""+v.get(v.size()-1));
        listProgress.ensureIndexIsVisible(listModel.size()-1);
    }

    @Override
    protected void done()
    {
        listModel.addElement("Finished");
        System.out.println("Convert process finished!");
        ProgressDialog.dispose();
        SummaryDialog.setVisible(true);
    }
}

 */