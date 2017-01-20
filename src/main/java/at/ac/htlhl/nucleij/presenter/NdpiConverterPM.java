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
    private GLScanAnalyzer glScanAnalyzer;
    private GLScanAnalyzerPM glScanAnalyzerPM;


    public NdpiConverterPM(NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer, final GLScanAnalyzerPM glScanAnalyzerPM)
    {
        super(ndpiConverter);

        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
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
                    //System.out.println(enabled);
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
                    int numberTifFiles = 0;
                    int numberNdpiFiles = 0;
                    boolean isFile = true;

                    File testPath = chooser.getSelectedFile();


                    if (testPath.isFile()) {
                        System.out.println("File gefunden");
                        isFile = true;
                    }
                    testPath = chooser.getCurrentDirectory();
                    if (testPath.isDirectory()) {
                        isFile = false;
                    }

                    System.out.println("Es ist " + isFile);



                    File[] filesInDirectory = chooser.getSelectedFiles();
                    //File[] filesInDirectory = chooser.getCurrentDirectory().listFiles();

                    List<String> ndpiFileList = new ArrayList<String>();
                    List<String> tifFileList = new ArrayList<String>();

                    for (File file : filesInDirectory ) {
                        if (file.isFile()) {
                            if (file.getName().endsWith(".ndpi")) {
                                ndpiFileList.add(file.getAbsolutePath());
                            }
                            else if (file.getName().endsWith(".tif")) {
                                tifFileList.add(file.getAbsolutePath());
                            }
                            else {
                                LOGGER.warning("Not supported file extension for file " + file.getName());
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

                    // List interation
                    /*for (int i = 0; i < ndpiFileList.size(); i++) {
                        System.out.println(ndpiFileList.get(i));
                    }
                    ndpiFileList.forEach((temp) -> {
                        System.out.println(temp);
                    });*/

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



                    //Würde den Controller starten (Startet automatisch)
                    //controllerTask.main();
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


        }

        public ConvertAction() {

        }
    }
}
