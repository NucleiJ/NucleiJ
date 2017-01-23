package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
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
    private Action convertAction;

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
        outputPathAction = new ExportPathAction();
        magnificationAction = new MagnificationAction();
        convertAction = new ConvertAction();


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

    public Action getInputPathAction() {
        return inputPathAction;
    }

    public Action getOutputPathAction() {
        return outputPathAction;
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
            JFileChooser chooser = createFileChooser();

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                ndpiConverter.setInputpath(chooser.getSelectedFile().getAbsolutePath());
                //ndpiConverter.setOutputpath(chooser.getSelectedFile().getAbsolutePath().concat("\\Output"));

                List<String> ndpiFileList = new ArrayList<>();
                List<String> tifFileList = new ArrayList<>();
                File[] filesInDirectory = new File[0];
                boolean moreThanOneFolder = false;
                int numberTifFiles = 0;
                int numberNdpiFiles = 0;

                if (chooser.getSelectedFile().isFile()) {
                    ndpiConverter.setOutputpath(ndpiConverter.getInputpath().substring(0,ndpiConverter.getInputpath().lastIndexOf(File.separator)).concat("\\Output"));

                    filesInDirectory = chooser.getSelectedFiles();
                }
                else if (chooser.getSelectedFile().isDirectory()) {
                    ndpiConverter.setOutputpath(ndpiConverter.getInputpath().concat("\\Output"));
                    filesInDirectory = chooser.getSelectedFile().listFiles();


                    //Checken ob mehr als ein Ordner ausgewählt wurde (Beta)
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
                            LOGGER.warning("Invalid file extension '" + file.getName().substring(file.getName().indexOf(".")) + "' for file '" + file.getName() + "'");
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

                System.out.println("\nTIF-Files:");
                for (String string : tifFileList) {
                    if (string != null) {
                        numberTifFiles++;
                        System.out.println(string);
                    }
                }
                System.out.println();
                LOGGER.info(numberNdpiFiles + " NDPI-Files & " + numberTifFiles + " TIF-Files in Folder '"+ chooser.getCurrentDirectory().getName() +"' found" + "\n");

                glScanAnalyzer.setNdpiList(ndpiFileList);
                glScanAnalyzer.setTifList(tifFileList);


                if (moreThanOneFolder) {
                    JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                            "Only one folder allowed!" +
                                    "\n" + "Selected first folder '" + chooser.getSelectedFile().getAbsolutePath() + "':" +
                                    "\n\n" + "NDPI-Files: " + numberNdpiFiles +
                                    "\n" + "TIF-Files: " + numberTifFiles,
                            "File-Selection successful!",
                            JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                            "Successfully selected:" +
                                    "\n\n" + "NDPI-Files: " + numberNdpiFiles +
                                    "\n" + "TIF-Files: " + numberTifFiles,
                            "File-Selection successful!",
                            JOptionPane.PLAIN_MESSAGE);
                }

                //Würde den Controller starten (Startet automatisch)
                //controllerTask.main();
            }
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

    private class ConvertAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {


        }

        public ConvertAction() {

        }
    }
}
