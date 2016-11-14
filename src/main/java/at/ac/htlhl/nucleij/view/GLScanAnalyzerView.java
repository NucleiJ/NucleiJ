/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.util.*;
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
    // region Fields
    // ************************************************************************
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComponent separator1;
    private JLabel label1;
    private JTextField inputpathTextField;
    private JButton button1;
    private JLabel label2;
    private JTextField outputpathTextField;
    private JButton button2;
    private JButton startAnalyzerButton;
    private AbstractAction analyzeAction;
    private AbstractAction selectpathAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private GLScanAnalyzerPM glScanAnalyzerPM;
    // endregion

    // Instance creation
    // ************************************************************************
    public GLScanAnalyzerView(GLScanAnalyzerPM glScanAnalyzerPM) {

        this.glScanAnalyzerPM = glScanAnalyzerPM;

        initComponents();

        initBinding();
    }

    private void createUIComponents()
    {
        analyzeAction = (AbstractAction) glScanAnalyzerPM.getAnalyzeAction();
        selectpathAction= (AbstractAction) glScanAnalyzerPM.getSelectpathAction();
    }

    private void initBinding()
    {
        PresentationModelBinder binder = Binders.binderFor(glScanAnalyzerPM);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_INPUTPATH).to(inputpathTextField);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.glscananalyzerview");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        separator1 = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.generalSeperator.text"));
        label1 = new JLabel();
        inputpathTextField = new JTextField();
        button1 = new JButton();
        label2 = new JLabel();
        outputpathTextField = new JTextField();
        button2 = new JButton();
        startAnalyzerButton = new JButton();

        //======== this ========
        setLayout(new FormLayout(
            "25dlu, $lcgap, 47dlu, $lcgap, 63dlu, $lcgap, default:grow, $lcgap, default",
            "18dlu, 9*($lgap, default)"));
        add(separator1, CC.xywh(1, 1, 9, 1));

        //---- label1 ----
        label1.setText(bundle.getString("GLScanAnalyzerView.inputpathLabel.text"));
        add(label1, CC.xy(3, 3));
        add(inputpathTextField, CC.xy(5, 3));

        //---- button1 ----
        button1.setText("text");
        button1.setAction(selectpathAction);
        add(button1, CC.xy(7, 3));

        //---- label2 ----
        label2.setText(bundle.getString("GLScanAnalyzerView.outputpathLabel.text"));
        add(label2, CC.xy(3, 5));
        add(outputpathTextField, CC.xy(5, 5));

        //---- button2 ----
        button2.setText("text");
        add(button2, CC.xy(7, 5));

        //---- startAnalyzerButton ----
        startAnalyzerButton.setText("text");
        startAnalyzerButton.setAction(analyzeAction);
        add(startAnalyzerButton, CC.xy(7, 19));

        //---- analyzeAction ----
        analyzeAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.analyzeAction.Name"));

        //---- selectpathAction ----
        selectpathAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.selectpathAction.Name"));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

}
