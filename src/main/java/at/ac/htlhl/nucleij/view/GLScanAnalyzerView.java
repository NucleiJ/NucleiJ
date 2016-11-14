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
    private JComponent generalSeparator;
    private JLabel analyzeType;
    private JComboBox modeComboBox;
    private JLabel inputpathLabel;
    private JTextField inputpathTextField;
    private JButton inputpathButton;
    private JComponent detailsSeperator;
    private JLabel outputpathLabel;
    private JTextField outputpathTextField;
    private JButton outputpathButton;
    private JLabel selectroiButton;
    private JRadioButton selectroiRadioButton;
    private JSlider slider1;
    private JRadioButton radioButton2;
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
        generalSeparator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.generalSeperator.text"));
        analyzeType = new JLabel();
        modeComboBox = new JComboBox();
        inputpathLabel = new JLabel();
        inputpathTextField = new JTextField();
        inputpathButton = new JButton();
        detailsSeperator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.detailsSeperator.text"));
        outputpathLabel = new JLabel();
        outputpathTextField = new JTextField();
        outputpathButton = new JButton();
        selectroiButton = new JLabel();
        selectroiRadioButton = new JRadioButton();
        slider1 = new JSlider();
        radioButton2 = new JRadioButton();
        startAnalyzerButton = new JButton();

        //======== this ========
        setLayout(new FormLayout(
            "25dlu, $lcgap, right:47dlu, $lcgap, 63dlu, 2*($lcgap, default:grow)",
            "18dlu, 9*($lgap, default), $lgap, default:grow"));
        add(generalSeparator, CC.xywh(1, 1, 9, 1));

        //---- analyzeType ----
        analyzeType.setText(bundle.getString("GLScanAnalyzerView.analyzetypeLabel.text"));
        add(analyzeType, CC.xy(3, 3));
        add(modeComboBox, CC.xy(5, 3));

        //---- inputpathLabel ----
        inputpathLabel.setText(bundle.getString("GLScanAnalyzerView.inputpathLabel.text"));
        add(inputpathLabel, CC.xy(3, 5));
        add(inputpathTextField, CC.xy(5, 5));

        //---- inputpathButton ----
        inputpathButton.setText("text");
        inputpathButton.setAction(selectpathAction);
        add(inputpathButton, CC.xy(7, 5));
        add(detailsSeperator, CC.xywh(1, 7, 9, 1));

        //---- outputpathLabel ----
        outputpathLabel.setText(bundle.getString("GLScanAnalyzerView.outputpathLabel.text"));
        add(outputpathLabel, CC.xy(3, 9));
        add(outputpathTextField, CC.xy(5, 9));

        //---- outputpathButton ----
        outputpathButton.setText("text");
        add(outputpathButton, CC.xy(7, 9));

        //---- selectroiButton ----
        selectroiButton.setText(bundle.getString("GLScanAnalyzerView.selectroiButton.text"));
        add(selectroiButton, CC.xy(3, 11));

        //---- selectroiRadioButton ----
        selectroiRadioButton.setText(bundle.getString("GLScanAnalyzerView.selectroiRadioButton.text"));
        add(selectroiRadioButton, CC.xy(5, 11));

        //---- slider1 ----
        slider1.setMinimum(1);
        add(slider1, CC.xywh(5, 13, 3, 1));

        //---- radioButton2 ----
        radioButton2.setText(bundle.getString("GLScanAnalyzerView.radioButton2.text"));
        add(radioButton2, CC.xy(5, 15));

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
