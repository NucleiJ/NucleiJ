package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.tasks.AnalyzerTask;
import at.ac.htlhl.nucleij.util.SuffixFileFilter;
import at.ac.htlhl.nucleij.view.GLScanAnalyzerView;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import java.awt.event.ActionEvent;
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
    public static final String FILE_EXTENSION			= "nucleij";

    private Action analyzeAction;
    private Action selectpathAction;
    private Action typeAction;
    private Action outputpathAction;
    private Action calculateandshowheatmapAction;
    private Action selectroiAction;

    private GLScanAnalyzer glScanAnalyzer;

    private String inputpathString;
    private String outputpathString;

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer) {
        super(glScanAnalyzer);

        this.glScanAnalyzer = glScanAnalyzer;



        analyzeAction = new AnalyzeAction();
        selectpathAction = new SelectpathAction();
        typeAction = new TypeAction();
        outputpathAction = new OutputpathAction();
        calculateandshowheatmapAction = new CalculateandshowheatmapAction();
        selectroiAction = new SelectroiAction();


    }

    public Action getAnalyzeAction() {
        return analyzeAction;
    }

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

            System.out.println("Mach etwas!!\n");
            // TODO Action ausprogrammieren
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
            //noinspection Since15
            analyzerTask.execute();

            taskDialog.show();
        }
    }

    private void selectPath()
    {
        LOGGER.info("Select Path Action clicked");

        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        JFileChooser chooser = new JFileChooser();
        if(glScanAnalyzer.getInputpath() != null)
        {
            File inputpathFile = new File(glScanAnalyzer.getInputpath());
            chooser.setCurrentDirectory(inputpathFile);
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            inputpathString = chooser.getSelectedFile().getAbsolutePath();
            outputpathString = inputpathString.concat("\\Output");
            glScanAnalyzer.setInputpath(inputpathString);

            boolean createOutputDirectory = new File(outputpathString).mkdirs();
            if (!createOutputDirectory) {
                //TODO Warum Fehler? Gibt es dieses Verzeichnis schon? Dann kein Fehler
                System.out.println("Error beim Erstellen des Ordners");
            }
            glScanAnalyzer.setOutputpath(outputpathString);
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

    private class CalculateandshowheatmapAction extends AbstractAction
    {
        public CalculateandshowheatmapAction()
        {

        }

        public void actionPerformed(ActionEvent e) {

            // Radiobox toggled von selber, Domainobject aendert sich von selber
            glScanAnalyzer.setCalculateandshowheatmap(glScanAnalyzer.isCalculateandshowheatmap());
            System.out.println(glScanAnalyzer.isCalculateandshowheatmap());

            // TODO Slider sichtbar machen

        }
    }

    private class SelectroiAction extends AbstractAction
    {
        public SelectroiAction() {

        }

        public void actionPerformed(ActionEvent e) {

            // Radiobox toggled von selber, Domainobject aendert sich von selber
            glScanAnalyzer.setSelectroi(glScanAnalyzer.isSelectroi());
            System.out.println(glScanAnalyzer.isSelectroi());
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

    //endregion:
}
