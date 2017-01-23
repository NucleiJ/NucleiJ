package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.AnalyzerConverterTask;
import at.ac.htlhl.nucleij.presenter.tasks.RoiTask;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;


/**
 *
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzerPM extends PresentationModel<GLScanAnalyzer> {
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(GLScanAnalyzerPM.class.getName());
    //public static final String FILE_EXTENSION			= "nucleij";


    /*private Action selectpathAction;
    private Action typeAction;
    private Action outputpathAction;*/

    private Action analyzeAction;
    private Action calculateandshowheatmapAction;
    private Action selectroiAction;
    private Action deleteroiAction;

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;

    /*private String inputpathString;
    private String outputpathString;*/

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer, NdpiConverter ndpiConverter) {
        super(glScanAnalyzer);

        this.glScanAnalyzer = glScanAnalyzer;
        this.ndpiConverter = ndpiConverter;

        analyzeAction = new AnalyzeAction();
        calculateandshowheatmapAction = new CalculateandshowheatmapAction();
        selectroiAction = new SelectroiAction();
        deleteroiAction = new DeleteroiAction();

        /*selectpathAction = new SelectpathAction();
        typeAction = new TypeAction();
        outputpathAction = new OutputpathAction();*/

        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, getBean().isCalculateandshowheatmap());
        //setComponentEnabled(GLScanAnalyzer.PROPERTY, getBean().isCalculateandshowheatmap());

        setComponentEnabled(GLScanAnalyzer.PROPERTY_ROIAREA, false);
        setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, false);

        // Ausgabe jeder Aenderung
        glScanAnalyzer.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());
                if(GLScanAnalyzer.PROPERTY_CALCULATEANDSHOWHEATMAP.equals(evt.getPropertyName())) {
                    boolean enabled = evt.getNewValue().toString().toLowerCase().equals("true");
                    setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, enabled);
                }
            }
        });


    }

    public void setROIvisible(boolean enabled)
    {
        setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, enabled);
    }

    public Action getAnalyzeAction() {
        return analyzeAction;
    }

    public Action getDeleteroiAction() {
        return deleteroiAction;
    }

    public Action getCalculateandshowheatmapAction()
    {
        return calculateandshowheatmapAction;
    }

    public Action getSelectroiAction()
    {
        return selectroiAction;
    }

    private class AnalyzeAction extends AbstractAction {
        public AnalyzeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Analyze Action clicked");

            //TODO: WICHTIG: Liste in glscanner model erstellen, nach analyze
            //converter starten, scheuen ob was in der liste ist, danch analyzer starten, liste abarbeiten

            // ********************************************************************************
            // Converter:
            /*
            JFrame parentConverter = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle resourceBundleConverter = AppContext.getInstance().getResourceBundle();

            TaskDialog taskDialogConverter = new TaskDialog(parentConverter, resourceBundleConverter.getString("ConverterDialog.title"));

            JProgressBar progressBarConverter = new JProgressBar(0,100);
            progressBarConverter.setStringPainted(true);
            progressBarConverter.setValue(0);
            taskDialogConverter.setInstruction(resourceBundleConverter.getString("ConverterDialog.instructionMessage"));
            taskDialogConverter.setText(resourceBundleConverter.getString("ConverterDialog.text"));
            taskDialogConverter.setFixedComponent(progressBarConverter);
            taskDialogConverter.setCommands(TaskDialog.StandardCommand.CANCEL);

            ConverterTask converterTask = new ConverterTask(progressBarConverter, taskDialogConverter, ndpiConverter, glScanAnalyzer);
            //noinspection Since15
            converterTask.execute();

            taskDialogConverter.show();
            */

            // ********************************************************************************
            //Analyzer Task

            JFrame parentAnalyzer = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle resourceBundleAnalyzer = AppContext.getInstance().getResourceBundle();

            //TaskDialog taskDialogAnalyzer = new TaskDialog(parentAnalyzer, resourceBundleAnalyzer.getString("AnalyzerConverterDialog.title"));
            TaskDialog taskDialogAnalyzerConverter = new TaskDialog(parentAnalyzer, resourceBundleAnalyzer.getString("AnalyzerConverterDialog.title"));

            JProgressBar progressBarAnalyzer = new JProgressBar(0,100);
            progressBarAnalyzer.setStringPainted(true);
            progressBarAnalyzer.setValue(0);
            taskDialogAnalyzerConverter.setInstruction(resourceBundleAnalyzer.getString("AnalyzerConverterDialog.instructionMessage"));
            taskDialogAnalyzerConverter.setText(resourceBundleAnalyzer.getString("AnalyzerConverterDialog.text"));
            taskDialogAnalyzerConverter.setFixedComponent(progressBarAnalyzer);
            taskDialogAnalyzerConverter.setCommands(TaskDialog.StandardCommand.CANCEL);

            //AnalyzerTask analyzerTask = new AnalyzerTask(progressBarAnalyzer, taskDialogAnalyzerConverter, glScanAnalyzer);

            AnalyzerConverterTask analyzerConverterTask = new AnalyzerConverterTask(progressBarAnalyzer, taskDialogAnalyzerConverter, ndpiConverter, glScanAnalyzer);
            analyzerConverterTask.execute();
            taskDialogAnalyzerConverter.show();

            //analyzerTask.execute();

            //taskDialogAnalyzer.show();

        }
    }

    private class CalculateandshowheatmapAction extends AbstractAction
    {
        public CalculateandshowheatmapAction()
        {
            super();
        }

        public void actionPerformed(ActionEvent e) {

            // Radiobox toggled von selber, Domainobject aendert sich von selber
            glScanAnalyzer.setCalculateandshowheatmap(glScanAnalyzer.isCalculateandshowheatmap());
            System.out.println(glScanAnalyzer.isCalculateandshowheatmap());

            // TODO Slider sichtbar machen

        }
    }

    private class DeleteroiAction extends AbstractAction
    {
        public DeleteroiAction()
        {
            super();
        }
        public void actionPerformed(ActionEvent e){
            glScanAnalyzer.setRoiarea("");
        }
    }

    private class SelectroiAction extends AbstractAction
    {
        public SelectroiAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {

            // Radiobox toggled von selber, Domainobject aendert sich von selber
            System.out.println("\nSelect the ROI");
            glScanAnalyzer.setRoiarea("10|30|100|200");

            RoiTask test = new RoiTask(glScanAnalyzer);
            test.setROI();

            //glScanAnalyzer.setSelectroi(glScanAnalyzer.isSelectroi());
            //System.out.println(glScanAnalyzer.isSelectroi());
        }
    }

    //region additional Methods for simple Funtions:
    private static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }



/*

    public Action getSelectpathAction()
    {
        return selectpathAction;
    }

    public Action getTypeAction()
    {
        return typeAction;
    }

    public Action getOutputpathAction()
    {
        return outputpathAction;
    }

    private JFileChooser createPreparedDirChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Verzeichnis auswählen"); // TODO Text auslagern
        return chooser;
    }

    private JFileChooser createPreparedFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new SuffixFileFilter(FILE_EXTENSION, "*." + FILE_EXTENSION, true));
        chooser.setDialogTitle("File auswählen"); // TODO Text auslagern
        return chooser;
    }

    private void selectPath()
    {
        LOGGER.info("Select Path Action clicked");


        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        if(glScanAnalyzer.getType()== "Multi") {
            JFileChooser chooser = createPreparedDirChooser();

            if(glScanAnalyzer.getInputpath() != null)   //falls file im path dann wegfiltern
            {
                File inputpathFile = new File(glScanAnalyzer.getInputpath());
                chooser.setCurrentDirectory(inputpathFile);
            }

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                inputpathString = chooser.getSelectedFile().getAbsolutePath();
                glScanAnalyzer.setInputpath(inputpathString);

                outputpathString = inputpathString.concat("\\Output");
                glScanAnalyzer.setOutputpath(outputpathString);
            }
        }
        else if(glScanAnalyzer.getType()== "Single") {              // TODO equals to satt ==
            JFileChooser chooser = createPreparedFileChooser();

            if(glScanAnalyzer.getInputpath() != null)   //falls file im path dann wegfiltern
            {
                File inputpathFile = new File(glScanAnalyzer.getInputpath());
                chooser.setCurrentDirectory(inputpathFile);
            }

            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            {
                inputpathString = chooser.getSelectedFile().getAbsolutePath();
                glScanAnalyzer.setInputpath(inputpathString);

                File inputPathFile = new File( inputpathString);
                outputpathString = inputPathFile.getParent().concat("\\Output");
                glScanAnalyzer.setOutputpath(outputpathString);
            }
        } else {
            System.out.println("\nERROR");
        }

        boolean createOutputDirectory = new File(outputpathString).mkdirs();
        if (!createOutputDirectory) {
            //TODO Warum Fehler? Gibt es dieses Verzeichnis schon? Dann kein Fehler
            System.out.println("Error beim Erstellen des Ordners");
        }

    }

    private void changeOutputPath()
    {
        LOGGER.info("Change Outputpath Action clicked");

        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        JFileChooser chooser = new JFileChooser();
        if(glScanAnalyzer.getInputpath() != null)
        {
            File outputpathFile = new File(glScanAnalyzer.getOutputpath());
            chooser.setCurrentDirectory(outputpathFile);
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            String newoutputpath = chooser.getSelectedFile().getAbsolutePath().concat("\\Output");
            File outputpathFile = new File(outputpathString);

            deleteDirectory(outputpathFile);        //altes Dir löschen
            outputpathString = newoutputpath;       // Outpupath neu setzen

            // priv outputpathString neu setzen
            boolean createOutputDirectory = new File(outputpathString).mkdirs();    //neuen Ordner erstellen
            if (!createOutputDirectory) {
                //TODO Warum Fehler? Gibt es dieses Verzeichnis schon? Dann kein Fehler
                System.out.println("Error beim Erstellen des Ordners");
            }

            // Domainobjekt aktualisieren, in Textfeld anzeigen
            glScanAnalyzer.setOutputpath(outputpathString);
        }


    }

    private class SelectpathAction extends AbstractAction
    {
        public SelectpathAction()
        {
        }

        public void actionPerformed(ActionEvent e)
        {
            selectPath();
        }
    }

    private class TypeAction extends AbstractAction {
        public TypeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Analyze Action clicked");

            System.out.println("Type! Ich bin ein Test!!\n");
            // TODO Action ausprogrammieren
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }
    }

    private class OutputpathAction extends AbstractAction {
        public OutputpathAction() {
        }

        public void actionPerformed(ActionEvent e) {
            // TODO Pfad erst aenderbar, wenn Inputpfad gesetzt ist, vorher grau
            changeOutputPath();

        }
    }
*/

    //endregion:
}
