/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.border.*;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.GLScanAnalyzerPM;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import javax.swing.*;

import static at.ac.htlhl.nucleij.model.GLScanAnalyzer.PROPERTY_FLOAT_VALUE;

/**
 * @author Schülerlizenz 2016/17
 */
public class GLScanAnalyzerView extends JPanel
{

    // region Fields
    // ************************************************************************
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComponent inputSeparator;
    private JLabel analyzeType;
    private JComboBox typeComboBox;
    private JLabel inputpathLabel;
    private JTextField inputpathTextField;
    private JButton inputpathButton;
    private JComponent outputSeperator;
    private JLabel outputpathLabel;
    private JTextField outputpathTextField;
    private JButton outputpathButton;
    private JComponent heatmapSeparator;
    private JLabel calculateandshowheatmapLabel;
    private JCheckBox calculateandshowheatmapCheckBox;
    private JLabel heatmapqualityLabel;
    private JSlider heatmapqualitySlider;
    private JLabel heatmapqualityvalueLabel;
    private JComponent roiSeperator;
    private JLabel selectroiLabel;
    private JCheckBox selectroiCheckBox;
    private JButton startAnalyzerButton;
    private AbstractAction analyzeAction;
    private AbstractAction selectpathAction;
    private AbstractAction typeAction;
    private AbstractAction outputpathAction;
    private AbstractAction calculateandshowheatmapAction;
    private AbstractAction selectroiAction;
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
        typeAction = (AbstractAction) glScanAnalyzerPM.getTypeAction();
        outputpathAction  = (AbstractAction) glScanAnalyzerPM.getOutputpathAction();
        calculateandshowheatmapAction = (AbstractAction) glScanAnalyzerPM.getCalculateandshowheatmapAction();
        selectroiAction = (AbstractAction) glScanAnalyzerPM.getSelectroiAction();
    }

    private void initBinding()
    {
        PresentationModelBinder binder = Binders.binderFor(glScanAnalyzerPM);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_INPUTPATH).to(inputpathTextField);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_OUTPUTPATH).to(outputpathTextField);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_CALCULATEANDSHOWHEATMAP).to(calculateandshowheatmapCheckBox);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_SELECTROI).to(selectroiCheckBox);


        //binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_TYPE).to(typeComboBox, SingleMode);
        //binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY).to(heatmapqualityvalueLabel);

        // TODO KomboBox und Slider binden

        /*
        ValueModel floatModel = model.getModel(PROPERTY_FLOAT_VALUE);
        heatmapqualitySlider.setModel(new BoundedRangeAdapter(
                ConverterFactory.createFloatToIntegerConverter(floatModel, 100), 0, 0, 100));
        binder.bind(ConverterFactory.createStringConverter(
                floatModel,
                NumberFormat.getPercentInstance())).to(heatmapqualityvalueLabel);
       */
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.glscananalyzerview");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        inputSeparator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.inputSeperator.text"));
        analyzeType = new JLabel();
        typeComboBox = new JComboBox();
        inputpathLabel = new JLabel();
        inputpathTextField = new JTextField();
        inputpathButton = new JButton();
        outputSeperator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.outputSeperator.text"));
        outputpathLabel = new JLabel();
        outputpathTextField = new JTextField();
        outputpathButton = new JButton();
        heatmapSeparator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.heatmapSeperator.text"));
        calculateandshowheatmapLabel = new JLabel();
        calculateandshowheatmapCheckBox = new JCheckBox();
        heatmapqualityLabel = new JLabel();
        heatmapqualitySlider = new JSlider();
        heatmapqualityvalueLabel = new JLabel();
        roiSeperator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.roiSeperator.text"));
        selectroiLabel = new JLabel();
        selectroiCheckBox = new JCheckBox();
        startAnalyzerButton = new JButton();

        //======== this ========
        setLayout(new FormLayout(
            "right:59dlu, $lcgap, 63dlu, $lcgap, default:grow, 2*($lcgap, default)",
            "18dlu, 5*($lgap, default), $lgap, 16dlu, $lgap, 18dlu, 3*($lgap, default), $lgap, default:grow"));
        add(inputSeparator, CC.xywh(1, 1, 9, 1));

        //---- analyzeType ----
        analyzeType.setText(bundle.getString("GLScanAnalyzerView.analyzetypeLabel.text"));
        add(analyzeType, CC.xy(1, 3));

        //---- typeComboBox ----
        typeComboBox.setModel(new DefaultComboBoxModel(new String[] {
            "SingleMode",
            "MultiMode"
        }));
        typeComboBox.setSelectedIndex(1);
        typeComboBox.setAction(typeAction);
        add(typeComboBox, CC.xy(3, 3));

        //---- inputpathLabel ----
        inputpathLabel.setText(bundle.getString("GLScanAnalyzerView.inputpathLabel.text"));
        add(inputpathLabel, CC.xy(1, 5));
        add(inputpathTextField, CC.xywh(3, 5, 3, 1));

        //---- inputpathButton ----
        inputpathButton.setText("text");
        inputpathButton.setAction(selectpathAction);
        add(inputpathButton, CC.xy(7, 5));

        //---- outputSeperator ----
        outputSeperator.setFont(new Font("sansserif", Font.PLAIN, 12));
        add(outputSeperator, CC.xywh(1, 7, 9, 1));

        //---- outputpathLabel ----
        outputpathLabel.setText(bundle.getString("GLScanAnalyzerView.outputpathLabel.text"));
        add(outputpathLabel, CC.xy(1, 9));
        add(outputpathTextField, CC.xywh(3, 9, 3, 1));

        //---- outputpathButton ----
        outputpathButton.setAction(outputpathAction);
        add(outputpathButton, CC.xy(7, 9));
        add(heatmapSeparator, CC.xywh(1, 11, 9, 1));

        //---- calculateandshowheatmapLabel ----
        calculateandshowheatmapLabel.setText(bundle.getString("GLScanAnalyzerView.calculateandshowheatmapLabel"));
        add(calculateandshowheatmapLabel, CC.xy(1, 13));

        //---- calculateandshowheatmapCheckBox ----
        calculateandshowheatmapCheckBox.setAction(calculateandshowheatmapAction);
        add(calculateandshowheatmapCheckBox, CC.xy(3, 13));

        //---- heatmapqualityLabel ----
        heatmapqualityLabel.setText(bundle.getString("GLScanAnalyzerView.heatmapqualityLabel.text"));
        add(heatmapqualityLabel, CC.xy(1, 15));

        //---- heatmapqualitySlider ----
        heatmapqualitySlider.setMinimum(1);
        add(heatmapqualitySlider, CC.xywh(3, 15, 5, 1));
        add(heatmapqualityvalueLabel, CC.xy(9, 15));
        add(roiSeperator, CC.xywh(1, 17, 9, 1));

        //---- selectroiLabel ----
        selectroiLabel.setText(bundle.getString("GLScanAnalyzerView.selectroiLabel"));
        add(selectroiLabel, CC.xy(1, 19));

        //---- selectroiCheckBox ----
        selectroiCheckBox.setAction(selectroiAction);
        add(selectroiCheckBox, CC.xy(3, 19));

        //---- startAnalyzerButton ----
        startAnalyzerButton.setText("text");
        startAnalyzerButton.setAction(analyzeAction);
        add(startAnalyzerButton, CC.xy(7, 21));

        //---- analyzeAction ----
        analyzeAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.analyzeAction.Name"));

        //---- selectpathAction ----
        selectpathAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.selectpathAction.Name"));

        //---- typeAction ----
        typeAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.analyzetypeLabel.text"));

        //---- outputpathAction ----
        outputpathAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.outputpathButton.text"));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

}
