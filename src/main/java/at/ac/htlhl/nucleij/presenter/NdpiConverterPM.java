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

public class NdpiConverterPM extends PresentationModel<NdpiConverter> {
    private static final Logger LOGGER = Logger.getLogger(NdpiConverterPM.class.getName());

    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    private FileNameExtensionFilter ndpiFilter = new FileNameExtensionFilter("Nano Zoomer Digital Pathology Image (.ndpi)", "ndpi");
    private FileNameExtensionFilter tifFilter  = new FileNameExtensionFilter("Tagged Image File (.tif)", "tif");

    private Action inputPathAction;
    private Action outputPathAction;

    private NdpiConverter  ndpiConverter;
    private GLScanAnalyzer glScanAnalyzer;

    public NdpiConverterPM(NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        super(ndpiConverter);

        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;

        inputPathAction = new InputPathAction();
        outputPathAction = new OutputPathAction();

        ndpiConverter.addPropertyChangeListener(evt -> {
            // Ausgabe jeder Aenderung
            LOGGER.info("Property name=" + evt.getPropertyName() + ", oldValue=" + evt.getOldValue() + ", newValue=" + evt.getNewValue());

            if (NdpiConverter.PROPERTY_MAGNIFICATION.equals(evt.getPropertyName())) {
                if (evt.getNewValue().toString().toLowerCase().equals(MAG_X5.toLowerCase())) {
                    JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                            bundle.getString("JOptionPaneMessage.text"),
                            bundle.getString("Error.text"),
                            JOptionPane.ERROR_MESSAGE);
                    ndpiConverter.setMagnification(MAG_X10);
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
    //endregion


    private JFileChooser createFileChooser() {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(tifFilter);
        chooser.addChoosableFileFilter(ndpiFilter);
        chooser.setControlButtonsAreShown(true);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(bundle.getString("FilechooserTitle.text"));

        return chooser;
    }

    private class InputPathAction extends AbstractAction {
        public InputPathAction() {
            super();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            JFileChooser chooser = createFileChooser();

            List<String> ndpiFileList = new ArrayList<>();
            List<String> tifFileList = new ArrayList<>();
            File[] filesInDirectory;

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                ndpiConverter.setInputpath(chooser.getSelectedFile().getAbsolutePath());

                // Prüfen ob Datei oder Ordner & Erstellen der File-Listen
                loadFilesAndFolders(chooser);

                // Auf Dateityp prüfen und sortieren in ndpiFileList & tifFileList
                filesInDirectory = ndpiConverter.getFilesInDirectory();
                checkAndSetFiles(filesInDirectory, ndpiFileList, tifFileList, chooser);

                // Enable/Disable ROI
                if (ndpiConverter.getNumberTifFiles() == 1 && ndpiConverter.getNumberNdpiFiles() == 0) {
                    glScanAnalyzer.setSetroi(true);
                } else {
                    glScanAnalyzer.setSetroi(false);
                }

                // Warnung wenn mehr als 1 Ordner ausgewählt
                if (moreThanOneFolder(chooser)) {
                    TaskDialogs.inform(parent,
                            bundle.getString("OnlyOneFolder.text"),
                            bundle.getString("SelectedFirstFolder.text") + "'" + chooser.getSelectedFile().getName() + "'");
                }
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

            // Set OutputPath
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                ndpiConverter.setOutputpath(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }


    private void loadFilesAndFolders(JFileChooser chooser) {
        if (chooser.getSelectedFile().isFile()) {
            if (ndpiConverter.getOutputpath().isEmpty()) {
                ndpiConverter.setOutputpath(ndpiConverter.getInputpath().substring(0, ndpiConverter.getInputpath().lastIndexOf(File.separator)).concat(File.separator + "Output"));
            }
            ndpiConverter.setFilesInDirectory(chooser.getSelectedFiles());
        } else if (chooser.getSelectedFile().isDirectory()) {
            ndpiConverter.setOutputpath(ndpiConverter.getInputpath().concat(File.separator + "Output"));
            ndpiConverter.setFilesInDirectory(chooser.getSelectedFile().listFiles());
        }
    }

    private void checkAndSetFiles(File[] filesInDirectory, List<String> ndpiFileList, List<String> tifFileList, JFileChooser chooser) {
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

        int numberTifFiles = 0;
        int numberNdpiFiles = 0;

        assert filesInDirectory != null;
        for (File file : filesInDirectory) {
            if (file.isFile()) {
                // Check file type
                if (file.getName().endsWith(".ndpi")) {
                    ndpiFileList.add(file.getAbsolutePath());
                    numberNdpiFiles++;
                } else if (file.getName().endsWith(".tif")) {
                    tifFileList.add(file.getAbsolutePath());
                    numberTifFiles++;
                } else {
                    // Input und Output Textfeld leeren und warnung ausgeben
                    ndpiConverter.setOutputpath(null);
                    ndpiConverter.setInputpath(null);

                    LOGGER.warning(bundle.getString("InvalidFileExtension.text") + " '" + file.getName().substring(file.getName().indexOf(".")) + bundle.getString("InvalidFileExtension.forFile") + file.getName() + "'");
                    TaskDialog errorDialog = new TaskDialog(parent, "Application Error");
                    errorDialog.setInstruction(bundle.getString("InvalidFileExtension.text2"));
                    errorDialog.setIcon(TaskDialog.StandardIcon.ERROR);
                    errorDialog.setText(bundle.getString("InvalidFileExtension.text") + file.getName().substring(file.getName().indexOf(".")) + bundle.getString("InvalidFileExtension.forFile") + "'" + file.getName() + "'");
                    errorDialog.show();
                }
            }
        }
        glScanAnalyzer.setNdpiList(ndpiFileList);
        glScanAnalyzer.setTifList(tifFileList);

        ndpiConverter.setNumberNdpiFiles(numberNdpiFiles);
        ndpiConverter.setNumberTifFiles(numberTifFiles);

        LOGGER.info(numberNdpiFiles + " NDPI-Files & " + numberTifFiles + " TIF-Files in Folder '" + chooser.getCurrentDirectory().getName() + "' found");
    }

    private boolean moreThanOneFolder(JFileChooser chooser) {
        boolean moreThanOneFolder = false;
        int numberOfFolders = 0;

        File[] moreFolders = chooser.getSelectedFiles();
        for (File file : moreFolders) {
            if (file.isDirectory()) {
                numberOfFolders++;
            }
        }
        if (numberOfFolders > 1) {
            moreThanOneFolder = true;
        }
        return moreThanOneFolder;
    }

}
