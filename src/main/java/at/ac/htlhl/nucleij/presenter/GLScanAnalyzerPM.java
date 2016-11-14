package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    private Action analyzeAction;
    private Action selectpathAction;
    private Action typeAction;
    private Action outputpathAction;

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer) {
        super(glScanAnalyzer);

        analyzeAction = new AnalyzeAction();
        selectpathAction = new SelectpathAction();
        typeAction = new TypeAction();
        outputpathAction = new OutputpathAction();
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


    private class AnalyzeAction extends AbstractAction {
        public AnalyzeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Analyze Action clicked");

            System.out.println("Mach etwas!!\n");
            // TODO Action ausprogrammieren
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }
    }

    private class SelectpathAction extends AbstractAction
    {
        public SelectpathAction()
        {
        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Select Path Action clicked");

            System.out.println("Pfad auswählen!!\n");
            // TODO Action ausprogrammieren
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
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
            LOGGER.info("Analyze Action clicked");

            System.out.println("Ändere den Outputpath!!\n");
            // TODO Action ausprogrammieren
            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }
    }
}
