package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Main;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.ConverterTask;
import at.ac.htlhl.nucleij.util.SuffixFileFilter;
import at.ac.htlhl.nucleij.view.GLScanAnalyzerView;
import at.ac.htlhl.nucleij.view.NdpiConverterView;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static at.ac.htlhl.nucleij.NucleiJ.nucleiJ;

/**
 * Created by Stefan & Andreas on 11.11.2016.
 */
public class MainPM extends PresentationModel<Main>
{
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(MainPM.class.getName());

    public static final String FILE_EXTENSION			= "nucleij";

    public static final String PROPERTY_MAIN			= "main";

    // Fields
    // ************************************************************************
    private Action loadAction;
    private Action saveAction;
    private Action aboutAction;
    private Action exitAction;
    private Action newAction;
    private Action startAction;
    private Action enableConverterViewAction;

    // References to sub presentation models
    private NdpiConverter ndpiConverter;

    private GLScanAnalyzerPM glScanAnalyzerPM;
    private NdpiConverterPM ndpiConverterPM;

    private GLScanAnalyzerView glScanAnalyzerView;
    private JComponent ndpiConverterView;

    public MainPM(Main main)
    {
        super(main);

        glScanAnalyzerPM = new GLScanAnalyzerPM(main.getGLScanAnalyzer(), main.getNdpiConverter());
        ndpiConverterPM = new NdpiConverterPM(main.getNdpiConverter(), glScanAnalyzerPM);


        loadAction = new LoadAction();
        saveAction = new SaveAction();
        aboutAction = new AboutAction();
        exitAction = new ExitAction();
        newAction = new newAction();
        startAction = new startAction();
        enableConverterViewAction = new enableConverterViewAction();
    }

    public GLScanAnalyzerPM getGLScanAnalyzerPM()
    {
        return glScanAnalyzerPM;
    }

    public NdpiConverterPM getNdpiConverterPM()
    {
        return ndpiConverterPM;
    }


    public Action getLoadAction()
    {
        return loadAction;
    }

    public Action getSaveAction()
    {
        return saveAction;
    }

    public Action getAboutAction() {
        return aboutAction;
    }

    public Action getExitAction() {
        return exitAction;
    }

    public Action getNewAction() {
        return newAction;
    }

    public Action getStartAction() {
        return startAction;
    }

    public Action getEnableConverterViewAction() {
        return enableConverterViewAction;
    }

    public void setGlScanAnalyzerView(GLScanAnalyzerView glScanAnalyzerView) {
        this.glScanAnalyzerView = glScanAnalyzerView;
    }

    public void setNdpiConverterView(NdpiConverterView ndpiConverterView) {
        this.ndpiConverterView = ndpiConverterView;
    }

    private void saveAs()
    {
        System.out.println("Save\n");

        JFileChooser chooser = createPreparedFileChooser();
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().toString();
            String fne = ((SuffixFileFilter) chooser.getFileFilter()).getAcceptingSuffix();
            if (!filePath.endsWith(fne)) {
                filePath = filePath + '.' + fne;
            }
            try {
                AppContext.getInstance().getJsonMapper().writerWithDefaultPrettyPrinter().writeValue(new File(filePath), getBean());
            } catch (IOException ex) {
                TaskDialogs.showException(ex);
            }
        }

    }

    private void load()
    {
        System.out.println("Load\n");

        JFileChooser chooser = createPreparedFileChooser();
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            try {
                Main loadedMain = AppContext.getInstance().getJsonMapper().readValue(chooser.getSelectedFile(), Main.class);
                glScanAnalyzerPM.setBean(loadedMain.getGLScanAnalyzer());
            } catch (IOException ex) {
                TaskDialogs.showException(ex);
            }
        }

    }

    private void newClass()
    {
        ndpiConverterPM.setBean(new NdpiConverter());
        glScanAnalyzerPM.setBean(new GLScanAnalyzer(ndpiConverterPM.getBean()));
        System.out.println("Neu gedrueckt\n");
    }

    private JFileChooser createPreparedFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new SuffixFileFilter(FILE_EXTENSION, "*." + FILE_EXTENSION, true));
        return chooser;
    }


    // region Nested classes
    // ************************************************************************
    private class LoadAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            load();
        }
    }

    private class SaveAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            saveAs();
        }
    }

    private class AboutAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            LOGGER.info("About Action clicked");
        }
    }

    private class ExitAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            Application.getInstance().exit(e);
        }
    }

    private class newAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            newClass();
        }
    }

    private class startAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Starten");
            //Macht noch nix!
            // TODO view enablen!

            ndpiConverter = new NdpiConverter();

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

            //ConverterTask converterTask = new ConverterTask(progressBar, taskDialog, ndpiConverter);
            //converterTask.execute();

        }
    }

    private class enableConverterViewAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Konvertieren");
            //Macht noch nix!
            //NucleiJ.getInstance().changeView(NdpiConverterView ndpiConverterView);
            nucleiJ.changeView(ndpiConverterView);

            // TODO view enablen!
        }
    }



    // endregion


}
