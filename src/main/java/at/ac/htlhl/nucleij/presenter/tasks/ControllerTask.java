package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.AppContext;
import com.ezware.dialog.task.TaskDialog;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Stefan on 20.01.2017.
 */
public class ControllerTask
{
    private List<String> tifList;
    private List<String> ndpiList;

    public ControllerTask()
    {
        super();
    }

    public void main()
    {
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        ResourceBundle resourceBundle = AppContext.getInstance().getResourceBundle();

        TaskDialog taskDialog = new TaskDialog(parent, resourceBundle.getString("TuningDialog.title"));

        JProgressBar progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        taskDialog.setInstruction(resourceBundle.getString("TuningDialog.instructionMessage"));
        taskDialog.setText(resourceBundle.getString("TuningDialog.text"));
        taskDialog.setFixedComponent(progressBar);
        taskDialog.setCommands(TaskDialog.StandardCommand.CANCEL);

        ConverterTask converterTask = new ConverterTask(progressBar, taskDialog, ndpiConverter);
        converterTask.execute();

        //TODO Start des Converters!!!
        // Converter liefert namen des convertierte Files zurück
        // ConverterTask converterTask = new ConverterTask(controllertask);

        // Anhängen an TIF-Liste!
    }

    public void addToList(String filename) {
        tifList.add(filename);
    }

    public List<String> getTifList() {
        return tifList;
    }

    public void setTifList(List<String> tifList) {
        this.tifList = tifList;
    }

    public List<String> getNdpiList() {
        return ndpiList;
    }

    public void setNdpiList(List<String> ndpiList) {
        this.ndpiList = ndpiList;
    }
}
