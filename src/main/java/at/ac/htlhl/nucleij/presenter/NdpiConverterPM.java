package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.AnalyzerTask;
import at.ac.htlhl.nucleij.util.SuffixFileFilter;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class NdpiConverterPM extends PresentationModel<NdpiConverter>
{
    private static final Logger LOGGER = Logger.getLogger(NdpiConverterPM.class.getName());
    public static final String FILE_EXTENSION = "nucleij";

    FileNameExtensionFilter ndpiFilter = new FileNameExtensionFilter("NanoZoomerDigitalPathologyImage (.ndpi)","ndpi");
    FileNameExtensionFilter tifFilter = new FileNameExtensionFilter("TaggedImageFile (.tif)","tif");

    private Action convertAction;
    private Action exportPathAction;
    private Action inputPathAction;
    private Action typeAction;
    private Action magnificationAction;

    private NdpiConverter ndpiConverter;

    private String path;


    public NdpiConverterPM(NdpiConverter ndpiConverter)
    {
        super(ndpiConverter);

        convertAction = new ConvertAction();
        exportPathAction = new ExportPathAction();
        inputPathAction = new InputPathAction();
        typeAction = new TypeAction();
        magnificationAction = new MagnificationAction();

        // Ausgabe jeder Aenderung
        ndpiConverter.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());
            }
        });
    }

    //region Actions
    public Action getConvertAction() {
        return convertAction;
    }

    public Action getMagnificationAction() {
        return magnificationAction;
    }

    public Action getOutputPathAction() {
        return exportPathAction;
    }

    public Action getInputPathAction() {
        return inputPathAction;
    }

    public Action getTypeAction() {
        return typeAction;
    }
    //endregion



    private JFileChooser createDirChooser()
    {
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //Action details = chooser.getActionMap().get("viewTypeDetails");
        //details.actionPerformed(null);
        chooser.setControlButtonsAreShown(true);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            path = chooser.getSelectedFile().getAbsolutePath();
            ndpiConverter.setInputpath(path);
            ndpiConverter.setOutputpath(path);
        }

        return chooser;
    }

    private JFileChooser createFileChooser()
    {
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.addChoosableFileFilter(tifFilter);
        chooser.addChoosableFileFilter(ndpiFilter);
        //Action details = chooser.getActionMap().get("viewTypeDetails");
        //details.actionPerformed(null);
        chooser.setControlButtonsAreShown(true);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            path = chooser.getSelectedFile().getAbsolutePath();
            ndpiConverter.setInputpath(path);
            ndpiConverter.setOutputpath(path);
            File inputPathFile = new File(path);
        }

        return chooser;
    }

    private void selectPath()
    {
        LOGGER.info("Select Path Action clicked");

        if(ndpiConverter.getType().equals("Single Dir") ) {
            JFileChooser chooser = createDirChooser();
            chooser.setDialogTitle("Verzeichnis auswählen");
        }
        else if(ndpiConverter.getType().equals("Single File")) {
            JFileChooser chooser = createFileChooser();
            chooser.setDialogTitle("File auswählen");
        }
        else if (ndpiConverter.getType().equals("Multi File")) {
            JFileChooser chooser = createFileChooser();
            chooser.setMultiSelectionEnabled(true);
        }
        else if (ndpiConverter.getType().equals("Multi Dir")) {
            JFileChooser chooser = createDirChooser();
            chooser.setMultiSelectionEnabled(true);
        }
        else {
            System.out.println("\nERROR");
        }

    }

    private class ConvertAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {
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

            AnalyzerTask analyzerTask = new AnalyzerTask(progressBar, taskDialog);

            analyzerTask.execute();

            taskDialog.show();
        }

        public ConvertAction() {

        }
    }

    private class InputPathAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public InputPathAction () {
            //selectPath();
        }
    }

    private class ExportPathAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public ExportPathAction() {

        }
    }

    private class TypeAction extends AbstractAction {
        public TypeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            //ndpiConverter.setType(e.getPropertyName());
            //TODO Auslesen von ComboBox;
            LOGGER.info("Analyze Action clicked");
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }

    }


    private class MagnificationAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public MagnificationAction () {

        }
    }

}
