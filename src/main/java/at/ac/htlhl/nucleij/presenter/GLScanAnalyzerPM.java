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

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer) {
        super(glScanAnalyzer);

        analyzeAction = new AnalyzeAction();
    }

    public Action getAnalyzeAction() {
        return analyzeAction;
    }

    private class AnalyzeAction extends AbstractAction {
        public AnalyzeAction() {

        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Analyze Action clicked");

            System.out.println("Mach etwas!!\n");

            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();


        }
    }
}
