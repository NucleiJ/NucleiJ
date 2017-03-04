/*
 * Created by JFormDesigner on Fri Nov 11 10:33:34 CET 2016
 */
package at.ac.htlhl.nucleij.view;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.GLScanAnalyzerPM;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzerView extends JPanel {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComponent heatmapSeparator;
    private JLabel calculateandshowheatmapLabel;
    private JCheckBox calculateandshowheatmapCheckBox;
    private JLabel heatmapqualityLabel;
    private JSlider heatmapqualitySlider;
    private JFormattedTextField heatmapqualityTextField;
    private JComponent roiSeperator;
    private JLabel selectroiLabel;
    private JButton selctroiButton;
    private JButton deleteRoiButton;
    private JLabel roiDataLabel;
    private JButton startAnalyzerButton;
    private AbstractAction analyzeAction;
    private AbstractAction calculateandshowheatmapAction;
    private AbstractAction setRoiAction;
    private AbstractAction changeheatmapqualityAction;
    private AbstractAction deleteroiAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private GLScanAnalyzerPM glScanAnalyzerPM;

    public GLScanAnalyzerView(GLScanAnalyzerPM glScanAnalyzerPM) {

        this.glScanAnalyzerPM = glScanAnalyzerPM;

        initComponents();   //vom GUI Designer erstelte Komponenten
        initBinding();
    }

    // manuelle, selbst Erstellte Komponenten
    private void createUIComponents() {
        analyzeAction = (AbstractAction) glScanAnalyzerPM.getAnalyzeAction();
        calculateandshowheatmapAction = (AbstractAction) glScanAnalyzerPM.getCalculateandshowheatmapAction();
        setRoiAction = (AbstractAction) glScanAnalyzerPM.getSelectroiAction();
        deleteroiAction = (AbstractAction) glScanAnalyzerPM.getDeleteroiAction();
    }

    private void initBinding() {
        // Binding fuer die HeatmapCheckbox und die ROI Auswahl (Label)
        PresentationModelBinder binder = Binders.binderFor(glScanAnalyzerPM);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_CALCULATEHEATMAP).to(calculateandshowheatmapCheckBox);
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_ROIAREA).to(roiDataLabel);

        // Property: HeatMapQuality
        ValueModel heatmapQualityValueModel = glScanAnalyzerPM.getModel(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY);
        heatmapqualitySlider.setModel(new BoundedRangeAdapter(heatmapQualityValueModel, 1, 1, 101));
        binder.bindBeanProperty(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY).to(heatmapqualityTextField);
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
        selctroiButton = new JButton();
        deleteRoiButton = new JButton();
        roiDataLabel = new JLabel();
        startAnalyzerButton = new JButton();

        //======== this ========
        setBorder(Borders.DIALOG);
        setLayout(new FormLayout(
            "right:68dlu, $lcgap, 30dlu, $lcgap, default, $lcgap, pref:grow, $lcgap, default, $lcgap, 24dlu, $lcgap, pref",
            "pref, $lgap, 16dlu, $lgap, 18dlu, 3*($lgap, pref)"));
        add(heatmapSeparator, CC.xywh(1, 1, 13, 1));

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
        add(heatmapqualitySlider, CC.xywh(3, 5, 7, 1));

        //---- heatmapqualityTextField ----
        heatmapqualityTextField.setBackground(UIManager.getColor("ArrowButton.background"));
        heatmapqualityTextField.setColumns(3);
        add(heatmapqualityTextField, CC.xy(11, 5, CC.LEFT, CC.DEFAULT));
        add(roiSeperator, CC.xywh(1, 7, 13, 1));

        //---- selectroiLabel ----
        selectroiLabel.setText(bundle.getString("GLScanAnalyzerView.selectroiLabel"));
        add(selectroiLabel, CC.xy(1, 9));

        //---- selctroiButton ----
        selctroiButton.setAction(setRoiAction);
        add(selctroiButton, CC.xy(3, 9, CC.FILL, CC.DEFAULT));

        //---- deleteRoiButton ----
        deleteRoiButton.setBorderPainted(false);
        deleteRoiButton.setAction(deleteroiAction);
        deleteRoiButton.setIcon(new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/process-stop.png")));
        add(deleteRoiButton, CC.xy(5, 9, CC.RIGHT, CC.DEFAULT));
        add(roiDataLabel, CC.xy(7, 9));

        //---- startAnalyzerButton ----
        startAnalyzerButton.setAction(analyzeAction);
        add(startAnalyzerButton, CC.xywh(7, 11, 5, 1, CC.RIGHT, CC.DEFAULT));

        //---- analyzeAction ----
        analyzeAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.analyzeAction.Name"));
        analyzeAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/analyzer.png")));
        analyzeAction.putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/analyzer.png")));

        //---- setRoiAction ----
        setRoiAction.putValue(Action.NAME, bundle.getString("GLScanAnalyzerView.Dialog.Name"));

        //---- deleteroiAction ----
        deleteroiAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/process-stop.png")));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}