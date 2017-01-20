package at.ac.htlhl.nucleij.presenter.tasks;


import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.presenter.converter.MainConverter;
import com.ezware.dialog.task.TaskDialog;
import com.sun.javafx.scene.layout.region.Margins;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.io.File;
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
