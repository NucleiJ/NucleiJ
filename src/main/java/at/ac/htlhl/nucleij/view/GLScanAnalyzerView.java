/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import javax.swing.*;

import at.ac.htlhl.nucleij.presenter.GLScanAnalyzerPM;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class GLScanAnalyzerView extends JPanel
{
    private GLScanAnalyzerPM glScanAnalyzerPM;

    public GLScanAnalyzerView(GLScanAnalyzerPM glScanAnalyzerPM) {

        this.glScanAnalyzerPM = glScanAnalyzerPM;
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        setLayout(new FormLayout(
            "default, $lcgap, default",
            "2*(default, $lgap), default"));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
