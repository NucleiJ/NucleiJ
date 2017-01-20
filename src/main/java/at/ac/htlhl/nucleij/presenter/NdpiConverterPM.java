package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.ControllerTask;
import at.ac.htlhl.nucleij.presenter.tasks.ConverterTask;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import fr.in2p3.imnc.ndpitools.NDPIToolsOpenTIFFPlugin;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static at.ac.htlhl.nucleij.model.NdpiConverter.SINGLE_FILE;


/**
 * Created by Andreas on 11.11.2016.
 */
//TODO Wenn mode geändert wird, werden alle Eingaben zurückgesetzt (Construktor aufrufen??)
public class NdpiConverterPM extends PresentationModel<NdpiConverter>
{
    private static final Logger LOGGER = Logger.getLogger(NdpiConverterPM.class.getName());
    public static final String FILE_EXTENSION = "nucleij";

    FileNameExtensionFilter ndpiFilter = new FileNameExtensionFilter("Nano Zoomer Digital Pathology Image (.ndpi)","ndpi");
    FileNameExtensionFilter tifFilter = new FileNameExtensionFilter("Tagged Image File (.tif)","tif");

    private Action typeAction;
    private Action inputPathAction;
    private Action outputPathAction;
    private Action customPathAction;
    private Action magnificationAction;
    private Action convertAction;

    private NdpiConverter ndpiConverter;
    private GLScanAnalyzerPM glScanAnalyzerPM;


    public NdpiConverterPM(NdpiConverter ndpiConverter, final GLScanAnalyzerPM glScanAnalyzerPM)
    {
        super(ndpiConverter);

        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzerPM = glScanAnalyzerPM;

        typeAction = new TypeAction();
        inputPathAction = new InputPathAction();
        outputPathAction = new ExportPathAction();
        customPathAction = new CustomPathAction();
        magnificationAction = new MagnificationAction();
        convertAction = new ConvertAction();


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
    public Action getTypeAction() {
        return typeAction;
    }

    public Action getInputPathAction() {
        return inputPathAction;
    }

    public Action getOutputPathAction() {
        return outputPathAction;
    }

    public Action getCustomPathAction() {
        return customPathAction;
    }

    public Action getMagnificationAction() {
        return magnificationAction;
    }

    public Action getConvertAction() {
        return convertAction;
    }
    //endregion



    private JFileChooser createFileChooser()
    {
        LOGGER.info("Select Path Action clicked");
        JFileChooser chooser = new JFileChooser();

        //TODO IDEE: Nur einen Filechooser mit setFileSelectionMode FILES_AND_DIRECTORIES und dann erkennen was gewählt wurde
        //TODO Bei mehreren gewählten Datein/Verzeichnissen den übergeordneten Ordner im InputPath Feld anzeigen (Oder 1.Datei mit "und X weitere Dateien" dran)

        if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_FILE)) {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.addChoosableFileFilter(tifFilter);
            chooser.addChoosableFileFilter(ndpiFilter);
            chooser.setControlButtonsAreShown(true);
            chooser.setDialogTitle("Datei auswählen");
        }
        else if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_DIR) ) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setControlButtonsAreShown(true);
            chooser.setDialogTitle("Verzeichnis auswählen");
        }
        else if (ndpiConverter.getType().equals(NdpiConverter.MULTI_FILE)) {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.addChoosableFileFilter(tifFilter);
            chooser.addChoosableFileFilter(ndpiFilter);
            chooser.setControlButtonsAreShown(true);
            chooser.setMultiSelectionEnabled(true);
            chooser.setDialogTitle("Mehrere Dateien auswählen");
        }
        else if (ndpiConverter.getType().equals(NdpiConverter.AUTO_MODE)) {
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.addChoosableFileFilter(tifFilter);
            chooser.addChoosableFileFilter(ndpiFilter);
            chooser.setControlButtonsAreShown(true);
            chooser.setMultiSelectionEnabled(true);
            chooser.setDialogTitle("Dateien Auswählen");
        }
        else {
            System.out.println("\nERROR");
        }
        return chooser;
    }


    class TypeAction extends AbstractAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            ndpiConverter.setType(e.getPropertyName());
            LOGGER.info("Type= "+ e.getPropertyName());
        }

        public void actionPerformed(ActionEvent e) {

        }
    }

    private class InputPathAction extends AbstractAction {
        public InputPathAction () {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            JFileChooser chooser = createFileChooser();

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                ndpiConverter.setCustompath(chooser.getSelectedFile().getName());
                ndpiConverter.setInputpath(chooser.getSelectedFile().getAbsolutePath());
                ndpiConverter.setOutputpath(chooser.getSelectedFile().getAbsolutePath());

                //TODO Extension: String extension = chooser.getTypeDescription(File);
                //Andere Methode: String extension = file.getName().substring(file.getName().lastIndexOf(".")+1,file.getName().length());
                if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_FILE)) {
                    //File file = chooser.getSelectedFile();
                    LOGGER.info("1 File in Folder '" + chooser.getCurrentDirectory().getName() +"' found");
                }

                else if(ndpiConverter.getType().equals(NdpiConverter.SINGLE_DIR)) {
                    int numberOfFiles = 0;
                    int numberOfFolder = 0;
                    File[] filesInDirectory = chooser.getSelectedFile().listFiles();

                    for (File file : filesInDirectory ) {
                        System.out.println(file.getName());
                        if (file.isFile()) {
                            numberOfFiles ++;
                        }
                        else if (file.isDirectory()) {
                            numberOfFolder ++;
                        }
                    }
                    LOGGER.info(numberOfFiles + " Files & " + numberOfFolder + " Dirs in Folder '" + chooser.getSelectedFile().getName() +"' found");

                }
                else if (ndpiConverter.getType().equals(NdpiConverter.MULTI_FILE)) {
                    int numberOfFiles = 0;
                    File[] filesInDirectory = chooser.getSelectedFiles();

                    for (File file : filesInDirectory ) {
                        System.out.println(file.getName());
                    }
                    LOGGER.info(numberOfFiles + " Files in Folder '" + chooser.getSelectedFile().getName() +"' found");
                }

                else if (ndpiConverter.getType().equals(NdpiConverter.AUTO_MODE)) {
                    int numberOfFiles = 0;
                    String extension = "";

                    File[] filesInDirectory = chooser.getSelectedFiles();
                    String[] ndpiFileList = new String[1000];
                    String[] tiffFileList = new String[1000];

                    for (File file : filesInDirectory ) {
                        if (file.isFile()) {
                            extension = chooser.getTypeDescription(file);
                            if (extension.equals("ndpi")) {
                                ndpiFileList[numberOfFiles] = file.getAbsolutePath();
                            }
                            else if (extension.equals("tif")) {
                                tiffFileList[numberOfFiles] = file.getAbsolutePath();
                            }
                            else {
                                LOGGER.warning("Not supported file extension for file " + file.getName());
                            }

                            numberOfFiles++;
                        }
                    }
                    System.out.println("\nNeue Liste NUR FILES:");

                    for (String string : ndpiFileList) {
                        if (string != null) {
                            System.out.println(string);
                        }
                    }
                    //TODO NDPI und TIF filtern
                    LOGGER.info(numberOfFiles + " Files in Folder '" + chooser.getCurrentDirectory().getName() +"' found");
                    ControllerTask controllerTask = new ControllerTask();
                    controllerTask.main(tiffFileList, ndpiFileList);
                }
            }
        }
    }

    class CustomPathAction extends AbstractAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            ndpiConverter.setCustompath(e.getPropertyName());
            int position = ndpiConverter.getOutputpath().lastIndexOf( '/' );
            String path = ndpiConverter.getOutputpath().substring(0,position+1);
            ndpiConverter.setOutputpath(path.concat(e.getPropertyName()));
            //TODO Wenn CustomPathTextField geändert wird soll outputPath aktualisiert werden
        }

        public void actionPerformed(ActionEvent e) {

        }
    }

    private class ExportPathAction extends AbstractAction {
        public ExportPathAction() {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            JFileChooser chooser = createFileChooser();

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                ndpiConverter.setCustompath(chooser.getSelectedFile().getName());
                ndpiConverter.setOutputpath(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    class MagnificationAction extends AbstractAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            ndpiConverter.setMagnification(e.getPropertyName());
        }

        public void actionPerformed(ActionEvent e) {

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

            ConverterTask converterTask = new ConverterTask(progressBar, taskDialog, ndpiConverter);
            converterTask.execute();

            //taskDialog.show();
        }

        public ConvertAction() {

        }
    }
}
