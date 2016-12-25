/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */
package at.ac.htlhl.nucleij.view;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.GLScanAnalyzerPM;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import net.miginfocom.swing.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class GLScanAnalyzerView extends JPanel
{

    // region Fields
    // ************************************************************************
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComponent heatmapSeparator;
    private JLabel calculateandshowheatmapLabel;
    private JCheckBox calculateandshowheatmapCheckBox;
    private JLabel heatmapqualityLabel;
    private JSlider heatmapqualitySlider;
    private JFormattedTextField heatmapqualityTextField;
    private JComponent roiSeperator;
    private JLabel selectroiLabel;
    private JFormattedTextField selectRoiTextField;
    private JButton deleteRoiButton;
    private JButton selectRoiButton;
    private JButton startAnalyzerButton;
    private AbstractAction analyzeAction;
    private AbstractAction calculateandshowheatmapAction;
    private AbstractAction selectroiAction;
    private AbstractAction changeheatmapqualityAction;
    private AbstractAction deleteroiAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private GLScanAnalyzerPM glScanAnalyzerPM;
    private GLScanAnalyzer glScanAnalyzer;

    // endregion

    // Instance creation
    // ************************************************************************
    public GLScanAnalyzerView(GLScanAnalyzerPM glScanAnalyzerPM, GLScanAnalyzer glScanAnalyzer) {

        this.glScanAnalyzerPM = glScanAnalyzerPM;
        this.glScanAnalyzer = glScanAnalyzer;

        initComponents();

        initBinding();

        //outputpathTextField.setEditable(false);
    }

    private void createUIComponents()
    {
        analyzeAction = (AbstractAction) glScanAnalyzerPM.getAnalyzeAction();
        calculateandshowheatmapAction = (AbstractAction) glScanAnalyzerPM.getCalculateandshowheatmapAction();
        selectroiAction = (AbstractAction) glScanAnalyzerPM.getSelectroiAction();
        deleteroiAction = (AbstractAction) glScanAnalyzerPM.getDeleteroiAction();

        /*selectpathAction= (AbstractAction) glScanAnalyzerPM.getSelectpathAction();
        typeAction = (AbstractAction) glScanAnalyzerPM.getTypeAction();
        outputpathAction  = (AbstractAction) glScanAnalyzerPM.getOutputpathAction();*/
    }

    private void initBinding()
    {
        /*//ALTES BINDING
        PresentationModelBinder binder = Binders.binderFor(glScanAnalyzerPM);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_INPUTPATH).to(inputpathTextField);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_OUTPUTPATH).to(outputpathTextField);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_SELECTROI).to(selectroiCheckBox);
        ValueModel typeValueModel =  glScanAnalyzerPM.getComponentModel(GLScanAnalyzer.PROPERTY_TYPE);
        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(GLScanAnalyzer.STRING_CHOICES, typeValueModel);
        typeComboBox.setModel(comboBoxAdapter);*/

        PresentationModelBinder binder = Binders.binderFor(glScanAnalyzerPM);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_CALCULATEANDSHOWHEATMAP).to(calculateandshowheatmapCheckBox);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_ROIAREA).to(selectRoiTextField);

        // Property: HeatMapQuality
        ValueModel heatmapQualityValueModel = glScanAnalyzerPM.getModel(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY);
        heatmapqualitySlider.setModel(new BoundedRangeAdapter(heatmapQualityValueModel, 1,  1,  101) );
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY).to(heatmapqualityTextField);
        //TODO Wieso max Value = 101?, in View aber 100, wenn 100 eingestellt, dann 99 in View??



    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.glscananalyzerview");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        heatmapSeparator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.heatmapSeperator.text"));
        calculateandshowheatmapLabel = new JLabel();
        calculateandshowheatmapCheckBox = new JCheckBox();
        heatmapqualityLabel = new JLabel();
        heatmapqualitySlider = new JSlider();
        heatmapqualityTextField = new JFormattedTextField();
        roiSeperator = compFactory.createSeparator(bundle.getString("GLScanAnalyzerView.roiSeperator.text"));
        selectroiLabel = new JLabel();
        selectRoiTextField = new JFormattedTextField();
        deleteRoiButton = new JButton();
        selectRoiButton = new JButton();
        startAnalyzerButton = new JButton();

        //======== this ========
        setBorder(Borders.DIALOG);
        setLayout(new FormLayout(
            "right:68dlu, $lcgap, 63dlu, $lcgap, pref:grow, $lcgap, default, $lcgap, 24dlu, $lcgap, pref",
            "pref, $lgap, 16dlu, $lgap, 18dlu, 3*($lgap, pref)"));
        add(heatmapSeparator, CC.xywh(1, 1, 11, 1));

        //---- calculateandshowheatmapLabel ----
        calculateandshowheatmapLabel.setText(bundle.getString("GLScanAnalyzerView.calculateandshowheatmapLabel"));
        add(calculateandshowheatmapLabel, CC.xy(1, 3));

        //---- calculateandshowheatmapCheckBox ----
        calculateandshowheatmapCheckBox.setAction(calculateandshowheatmapAction);
        add(calculateandshowheatmapCheckBox, CC.xy(3, 3));

        //---- heatmapqualityLabel ----
        heatmapqualityLabel.setText(bundle.getString("GLScanAnalyzerView.heatmapqualityLabel.text"));
        add(heatmapqualityLabel, CC.xy(1, 5));

        //---- heatmapqualitySlider ----
        heatmapqualitySlider.setMinimum(1);
        heatmapqualitySlider.setValue(60);
        add(heatmapqualitySlider, CC.xywh(3, 5, 5, 1));

        //---- heatmapqualityTextField ----
        heatmapqualityTextField.setBackground(UIManager.getColor("ArrowButton.background"));
        heatmapqualityTextField.setColumns(3);
        add(heatmapqualityTextField, CC.xy(9, 5, CC.LEFT, CC.DEFAULT));
        add(roiSeperator, CC.xywh(1, 7, 11, 1));

        //---- selectroiLabel ----
        selectroiLabel.setText(bundle.getString("GLScanAnalyzerView.selectroiLabel"));
        add(selectroiLabel, CC.xy(1, 9));

        //---- selectRoiTextField ----
        selectRoiTextField.setEditable(false);
        add(selectRoiTextField, CC.xywh(3, 9, 3, 1));

        //---- deleteRoiButton ----
        deleteRoiButton.setBorderPainted(false);
        deleteRoiButton.setAction(deleteroiAction);
        deleteRoiButton.setIcon(new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/process-stop.png")));
        add(deleteRoiButton, CC.xy(7, 9));

        //---- selectRoiButton ----
        selectRoiButton.setAction(selectroiAction);
        add(selectRoiButton, CC.xy(9, 9));

        //---- startAnalyzerButton ----
        startAnalyzerButton.setAction(analyzeAction);
        add(startAnalyzerButton, CC.xywh(3, 11, 3, 1, CC.LEFT, CC.DEFAULT));

        //---- analyzeAction ----
        analyzeAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.analyzeAction.Name"));

        //---- selectroiAction ----
        selectroiAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.Dialog.Name"));

        //---- deleteroiAction ----
        deleteroiAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/process-stop.png")));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public void setSliderInvisble()
    {
        heatmapqualitySlider.setVisible(false);
    }
}
