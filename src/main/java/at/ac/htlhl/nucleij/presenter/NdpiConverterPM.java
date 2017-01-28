package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static at.ac.htlhl.nucleij.model.NdpiConverter.MAG_X10;
import static at.ac.htlhl.nucleij.model.NdpiConverter.MAG_X5;


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

    private Action inputPathAction;
    private Action outputPathAction;
    private Action magnificationAction;

    private NdpiConverter ndpiConverter;
    private GLScanAnalyzer glScanAnalyzer;
    private GLScanAnalyzerPM glScanAnalyzerPM;


    public NdpiConverterPM(NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer, final GLScanAnalyzerPM glScanAnalyzerPM)
    {
        super(ndpiConverter);

        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
        this.glScanAnalyzerPM = glScanAnalyzerPM;

        inputPathAction = new InputPathAction();
        outputPathAction = new OutputPathAction();
        magnificationAction = new MagnificationAction();

        // Ausgabe jeder Aenderung
        ndpiConverter.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());

                if(NdpiConverter.PROPERTY_MAGNIFICATION.equals(evt.getPropertyName()))
                {
                    if(evt.getNewValue().toString().toLowerCase().equals(MAG_X5.toLowerCase()))
                    {
                        JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                                "This magnification is currently not aviable!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        ndpiConverter.setMagnification(MAG_X10);
                    }
                }
            }
        });
    }

    //region Actions
    public Action getInputPathAction() {
        return inputPathAction;
    }

    public Action getOutputPathAction() {
        return outputPathAction;
    }

    public Action getMagnificationAction() {
        return magnificationAction;
    }
    //endregion


    private JFileChooser createFileChooser()
    {
        LOGGER.info("Select Path Action clicked");
        JFileChooser chooser = new JFileChooser();

        //TODO IDEE: Nur einen Filechooser mit setFileSelectionMode FILES_AND_DIRECTORIES und dann erkennen was gewählt wurde
        //TODO Bei mehreren gewählten Datein/Verzeichnissen den übergeordneten Ordner im InputPath Feld anzeigen (Oder 1.Datei mit "und X weitere Dateien" dran)

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(tifFilter);
        chooser.addChoosableFileFilter(ndpiFilter);
        chooser.setControlButtonsAreShown(true);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle("Dateien Auswählen");

        return chooser;
    }

    private class InputPathAction extends AbstractAction {
        public InputPathAction () {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");
            JFileChooser chooser = createFileChooser();

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                ndpiConverter.setInputpath(chooser.getSelectedFile().getAbsolutePath());

                List<String> ndpiFileList = new ArrayList<>();
                List<String> tifFileList = new ArrayList<>();
                File[] filesInDirectory = new File[0];
                boolean moreThanOneFolder = false;
                int numberTifFiles = 0;
                int numberNdpiFiles = 0;

                if (chooser.getSelectedFile().isFile()) {
                    if (ndpiConverter.getOutputpath().isEmpty())
                    {
                        ndpiConverter.setOutputpath(ndpiConverter.getInputpath().substring(0,ndpiConverter.getInputpath().lastIndexOf(File.separator)).concat(File.separator + "Output"));
                    }
                    filesInDirectory = chooser.getSelectedFiles();
                }
                else if (chooser.getSelectedFile().isDirectory()) {
                    ndpiConverter.setOutputpath(ndpiConverter.getInputpath().concat(File.separator + "Output"));
                    filesInDirectory = chooser.getSelectedFile().listFiles();

                    int numberOfFolders = 0;
                    File[] moreFolders = chooser.getSelectedFiles();
                    for (File file : moreFolders ) {
                        if (file.isDirectory()) {
                            numberOfFolders++;
                        }
                    }
                    if (numberOfFolders > 1) {
                        moreThanOneFolder = true;
                    }
                }



                assert filesInDirectory != null;
                for (File file : filesInDirectory ) {
                    if (file.isFile()) {
                        if (file.getName().endsWith(".ndpi")) {
                            ndpiFileList.add(file.getAbsolutePath());
                        }
                        else if (file.getName().endsWith(".tif")) {
                            tifFileList.add(file.getAbsolutePath());
                        }
                        else {
                            ndpiConverter.setOutputpath(null);
                            ndpiConverter.setInputpath(null);

                            LOGGER.warning("Invalid file extension '" + file.getName().substring(file.getName().indexOf(".")) + bundle.getString("InvalidFileExtension.forFile") + file.getName() + "'");
                            TaskDialog errorDialog = new TaskDialog(parent,"Application Error");
                            errorDialog.setInstruction(bundle.getString("InvalidFileExtension.text2"));
                            errorDialog.setIcon(TaskDialog.StandardIcon.ERROR );
                            errorDialog.setText(bundle.getString("InvalidFileExtension.text") + file.getName().substring(file.getName().indexOf(".")) + bundle.getString("InvalidFileExtension.forFile") + file.getName() + "'" );
                            errorDialog.show();
                        }
                    }
                }

                System.out.println("\nNDPI-Files:");
                for (String string : ndpiFileList) {
                    if (string != null) {
                        numberNdpiFiles++;
                        System.out.println(string);
                    }
                }
                ndpiConverter.setNumberNdpiFiles(numberNdpiFiles);

                System.out.println("\nTIF-Files:");
                for (String string : tifFileList) {
                    if (string != null) {
                        numberTifFiles++;
                        System.out.println(string);
                    }
                }
                ndpiConverter.setNumberTifFiles(numberTifFiles);


                System.out.println();
                LOGGER.info(numberNdpiFiles + " NDPI-Files & " + numberTifFiles + " TIF-Files in Folder '"+ chooser.getCurrentDirectory().getName() +"' found" + "\n");

                glScanAnalyzer.setNdpiList(ndpiFileList);
                glScanAnalyzer.setTifList(tifFileList);

                if( numberTifFiles == 1 && numberNdpiFiles == 0 )
                {
                    System.out.println("ROI Modus enablen");
                    glScanAnalyzer.setSetroi(true);
                }
                else
                {
                    System.out.println("ROI Modus disablen");
                    glScanAnalyzer.setSetroi(false);

                }

                JFrame parentDialog = ((SingleFrameApplication) Application.getInstance()).getMainFrame();


                if (moreThanOneFolder) {
                    TaskDialogs.inform(parentDialog,
                            "Only one folder allowed!",
                            "Selected first folder '" + chooser.getSelectedFile().getName() + "'");
                    //Automatisch wird 1. Ordner gewählt
                }

                /*int choice;
                if (numberNdpiFiles > 0 && numberTifFiles > 0) {
                    choice = TaskDialogs.radioChoice(parentDialog,
                                "It seems that you selected both NDPI & TIF Files" ,
                                "NDPI-Files: \t" + numberNdpiFiles + "\nTIF-Files: \t" + numberTifFiles + "\n\nWhat do you want do?" ,
                                0,
                                "Convert & Analyze", "Convert Only", "Analyze Only" );
                    ndpiConverter.setChoice(choice);
                }
                else if (numberNdpiFiles > 0 && numberTifFiles == 0) { //Only NDPI Selected
                    choice = TaskDialogs.radioChoice(parentDialog,
                            "It seems that your selection is a Ndpi File",
                            "What will you do?",
                            0,
                            "Convert & Analyze", "Convert Only" );
                    ndpiConverter.setChoice(choice);
                }
                else if (numberNdpiFiles == 0 && numberTifFiles > 0) {
                    // Wird nur Konvertiert
                    ndpiConverter.setChoice(2);
                }*/


            }
        }
    }

    private class OutputPathAction extends AbstractAction {
        public OutputPathAction() {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            JFileChooser chooser = createFileChooser();

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                ndpiConverter.setOutputpath(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    class MagnificationAction extends AbstractAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            ndpiConverter.setMagnification(e.getPropertyName());
            LOGGER.info("Magnification= "+ e.getPropertyName());
        }

        public void actionPerformed(ActionEvent e) {

        }
    }


}
