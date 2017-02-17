package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Main;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.util.SuffixFileFilter;
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

/**
 * Created by Stefan & Andreas on 11.11.2016.
 */
public class MainPM extends PresentationModel<Main> {
    public static final String FILE_EXTENSION = "nucleij";
    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");
    private Action loadAction;
    private Action saveAction;
    private Action aboutAction;
    private Action exitAction;
    private Action newAction;
    private Action infoAction;

    private GLScanAnalyzerPM glScanAnalyzerPM;
    private NdpiConverterPM  ndpiConverterPM;

    public MainPM(Main main) {
        super(main);

        glScanAnalyzerPM = new GLScanAnalyzerPM(main.getGLScanAnalyzer(), main.getNdpiConverter());
        ndpiConverterPM = new NdpiConverterPM(main.getNdpiConverter(), main.getGLScanAnalyzer());

        loadAction = new LoadAction();
        saveAction = new SaveAction();
        aboutAction = new AboutAction();
        exitAction = new ExitAction();
        newAction = new NewAction();
        infoAction = new InfoAction();
    }

    public GLScanAnalyzerPM getGLScanAnalyzerPM() {
        return glScanAnalyzerPM;
    }

    public NdpiConverterPM getNdpiConverterPM() {
        return ndpiConverterPM;
    }

    public Action getLoadAction() {
        return loadAction;
    }

    public Action getSaveAction() {
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

    public Action getInfoAction() {
        return infoAction;
    }

    private void showAbout() {
        JFrame parentAbout = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

        JLabel text = new JLabel("http://www.htl-hl.ac.at", JLabel.RIGHT);

        TaskDialog taskDialogAbout = new TaskDialog(parentAbout, bundle.getString("AboutDialog.title"));
        taskDialogAbout.setInstruction(bundle.getString("AboutDialog.instructionMessage"));
        taskDialogAbout.setText(bundle.getString("AboutDialog.text"));
        taskDialogAbout.setFixedComponent(text);

        taskDialogAbout.setCommands(TaskDialog.StandardCommand.CANCEL);
        taskDialogAbout.setIcon(TaskDialog.StandardIcon.INFO);

        taskDialogAbout.show();
    }

    private void saveAs() {
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

    private void load() {
        JFileChooser chooser = createPreparedFileChooser();
        JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                Main loadedMain = AppContext.getInstance().getJsonMapper().readValue(chooser.getSelectedFile(), Main.class);
                glScanAnalyzerPM.setBean(loadedMain.getGLScanAnalyzer());
                ndpiConverterPM.setBean(loadedMain.getNdpiConverter());
            } catch (IOException ex) {
                TaskDialogs.showException(ex);
            }
        }
    }

    private void newClass() {
        ndpiConverterPM.setBean(new NdpiConverter());
        glScanAnalyzerPM.setBean(new GLScanAnalyzer(ndpiConverterPM.getBean()));
    }

    private JFileChooser createPreparedFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new SuffixFileFilter(FILE_EXTENSION, "*." + FILE_EXTENSION, true));
        return chooser;
    }

    private class LoadAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            load();
        }
    }

    private class SaveAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            saveAs();
        }
    }

    private class AboutAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            showAbout();
        }
    }

    private class ExitAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            Application.getInstance().exit(e);
        }
    }

    private class NewAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            newClass();
        }
    }

    private class InfoAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {

            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            TaskDialogs.inform(parent, bundle.getString("HelpDialog.titel"),
                    bundle.getString("HelpDialog.text"));
        }
    }
}
