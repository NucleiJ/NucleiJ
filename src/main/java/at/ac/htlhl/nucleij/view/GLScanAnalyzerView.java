/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.GLScanAnalyzerPM;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import javax.swing.*;

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
        label1 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        label2 = new JLabel();
        textField2 = new JTextField();
        button2 = new JButton();

        //======== this ========
        setLayout(new FormLayout(
            "23dlu, $lcgap, 47dlu, $lcgap, 63dlu, $lcgap, default:grow",
            "18dlu, 5*($lgap, default)"));

        //---- label1 ----
        label1.setText("text");
        add(label1, CC.xy(3, 3));
        add(textField1, CC.xy(5, 3));

        //---- button1 ----
        button1.setText("text");
        add(button1, CC.xy(7, 3));

        //---- label2 ----
        label2.setText("text");
        add(label2, CC.xy(3, 5));
        add(textField2, CC.xy(5, 5));

        //---- button2 ----
        button2.setText("text");
        add(button2, CC.xy(7, 5));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField textField1;
    private JButton button1;
    private JLabel label2;
    private JTextField textField2;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
