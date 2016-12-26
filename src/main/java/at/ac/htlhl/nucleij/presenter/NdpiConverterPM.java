package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static at.ac.htlhl.nucleij.model.NdpiConverter.SINGLE_FILE;


/**
 * Created by Stefan on 11.11.2016.
 */
public class NdpiConverterPM extends PresentationModel<NdpiConverter>
{
    private static final Logger LOGGER = Logger.getLogger(NdpiConverterPM.class.getName());
    public static final String FILE_EXTENSION = "nucleij";

    FileNameExtensionFilter ndpiFilter = new FileNameExtensionFilter("Nano Zoomer Digital Pathology Image (.ndpi)","ndpi");
    FileNameExtensionFilter tifFilter = new FileNameExtensionFilter("Tagged Image File (.tif)","tif");

    private Action convertAction;
    private Action exportPathAction;
    private Action inputPathAction;
    private Action typeAction;
    private Action magnificationAction;

    private NdpiConverter ndpiConverter;
    private GLScanAnalyzerPM glScanAnalyzerPM;

    private String path;


    public NdpiConverterPM(NdpiConverter ndpiConverter, final GLScanAnalyzerPM glScanAnalyzerPM)
    {
        super(ndpiConverter);

        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzerPM = glScanAnalyzerPM;

        convertAction = new ConvertAction();
        exportPathAction = new ExportPathAction();
        inputPathAction = new InputPathAction();
        typeAction = new TypeAction();
        magnificationAction = new MagnificationAction();

        // Ausgabe jeder Aenderung
        ndpiConverter.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());

                // TODO ROI ein und ausblenden
                if(NdpiConverter.PROPERTY_TYPE.equals(evt.getPropertyName())) {
                    boolean enabled = evt.getNewValue().toString().toLowerCase().equals(SINGLE_FILE.toLowerCase());
                    System.out.println(enabled);
                    //setComponentEnabled(GLScanAnalyzer.PROPERTY_ROIAREA, enabled);
                    //setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, enabled);
                    glScanAnalyzerPM.setROIvisible(enabled);
                }
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



    private void createFileChooser()
    {
        LOGGER.info("Select Path Action clicked");
        JFileChooser chooser = new JFileChooser();

        if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_DIR) ) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setControlButtonsAreShown(true);
            chooser.setDialogTitle("Verzeichnis auswählen");
        }
        else if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_FILE)) {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.addChoosableFileFilter(tifFilter);
            chooser.addChoosableFileFilter(ndpiFilter);
            chooser.setControlButtonsAreShown(true);
            chooser.setDialogTitle("Datei auswählen");
        }
        else if (ndpiConverter.getType().equals(NdpiConverter.MULTI_FILE)) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setControlButtonsAreShown(true);
            chooser.setMultiSelectionEnabled(true);
            chooser.setDialogTitle("Mehrere Dateien auswählen");
        }
        else {
            System.out.println("\nERROR");
        }

        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            path = chooser.getSelectedFile().getAbsolutePath();
            ndpiConverter.setInputpath(path);
            ndpiConverter.setOutputpath(path);
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

            //AnalyzerTask analyzerTask = new AnalyzerTask(progressBar, taskDialog, calculateandshowheatmap);

            //analyzerTask.execute();

            taskDialog.show();
        }

        public ConvertAction() {

        }
    }

    private class InputPathAction extends AbstractAction {

        public InputPathAction () {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            createFileChooser();
        }


    }

    private class ExportPathAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public ExportPathAction() {

        }
    }

    /*private class TypeAction extends AbstractAction {
        public TypeAction() {
        }

        public void propertyChangeListener(PropertyChangeEvent e)

        public void actionPerformed(PropertyChangeEvent e) {
            ndpiConverter.setType(e.getPropertyName());
            //TODO Auslesen von ComboBox;
            LOGGER.info("Analyze Action clicked");
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }

        public void actionPerformed(ActionEvent e) {

        }
    }*/

    class TypeAction extends AbstractAction implements PropertyChangeListener  {
        public void propertyChange(PropertyChangeEvent e) {
            ndpiConverter.setType(e.getPropertyName());
            LOGGER.info("Type= "+ e.getPropertyName());
        }

        public void actionPerformed(ActionEvent e) {

        }
    }


    private class MagnificationAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public MagnificationAction () {

        }
    }

}
