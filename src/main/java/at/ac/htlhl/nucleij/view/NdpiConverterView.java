package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.NdpiConverterPM;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class NdpiConverterView extends JPanel {

    private NdpiConverterPM ndpiConverterPM;

    public NdpiConverterView(NdpiConverterPM ndpiConverterPM)
    {
        this.ndpiConverterPM = ndpiConverterPM;

        initComponents();
        initBindings();
    }

    private void createUIComponents() {
        convertAction = (AbstractAction) ndpiConverterPM.getConvertAction();
        outputPathAction = (AbstractAction) ndpiConverterPM.getOutputPathAction();
        typeAction = (AbstractAction) ndpiConverterPM.getTypeAction();
        inputPathAction = (AbstractAction) ndpiConverterPM.getInputPathAction();
        magnificationAction = (AbstractAction) ndpiConverterPM.getMagnificationAction();
    }

    public void initBindings() {
        PresentationModelBinder binder = Binders.binderFor(ndpiConverterPM);
        binder.bindBeanProperty(NdpiConverter.PROPERTY_INPUTPATH).to(btnImportPath);
        binder.bindBeanProperty(NdpiConverter.PROPERTY_OUTPUTPATH).to(btnOutputPath);

        //binder.bindBeanProperty(NdpiConverter.PROPERTY_TYPE).to(modeComboBox, SingleMode);
        //binder.bindBeanProperty(NdpiConverter.PROPERTY_TYPE).to(buttonGroupMagnification);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        label23 = new JLabel();
        separator9 = new JPopupMenu.Separator();
        label17 = new JLabel();
        modeComboBox = new JComboBox();
        label24 = new JLabel();
        textfieldImportDir = new JTextField();
        btnImportPath = new JButton();
        labelImportStatus = new JLabel();
        label31 = new JLabel();
        textfieldExportDir = new JTextField();
        btnOutputPath = new JButton();
        labelExportStatus = new JLabel();
        label25 = new JLabel();
        separator10 = new JSeparator();
        labelSetMagnification = new JLabel();
        btnMagnification40 = new JRadioButton();
        btnMagnification10 = new JRadioButton();
        btnConvert = new JButton();
        btnRoiSelect = new JButton();
        separator5 = new JPopupMenu.Separator();
        labelRoiStatus = new JLabel();
        checkBoxRoi = new JCheckBox();
        label19 = new JLabel();
        label28 = new JLabel();
        separator12 = new JPopupMenu.Separator();
        checkBoxHeatmap = new JCheckBox();
        checkBox1 = new JCheckBox();
        comboBox3 = new JComboBox();
        labelResolution = new JLabel();
        spinnerSliderValue = new JSpinner();
        sliderHeatmap = new JSlider();
        menu2 = new JMenu();
        checkBox2 = new JCheckBox();
        checkBox3 = new JCheckBox();
        checkBox4 = new JCheckBox();
        checkBox5 = new JCheckBox();

        //======== this ========
        setLayout(new FormLayout(
            "$lcgap, 6dlu, $lcgap, left:22dlu, $lcgap, left:18dlu, $lcgap, left:26dlu, $lcgap, 30dlu, $lcgap, 26dlu, $lcgap, right:24dlu:grow, $lcgap, right:53dlu:grow, 2*($lcgap)",
            "$lgap, 2*(default), 17dlu, 3*(default), $ugap, default, min, $ugap, $lcgap"));
        ((FormLayout)getLayout()).setRowGroups(new int[][] {{2, 9}, {5, 7}});

        //---- label23 ----
        label23.setText("General");
        label23.setFont(label23.getFont().deriveFont(label23.getFont().getStyle() | Font.BOLD));
        add(label23, CC.xywh(2, 2, 5, 1));
        add(separator9, CC.xywh(6, 2, 11, 1));

        //---- label17 ----
        label17.setText("Select mode:");
        add(label17, CC.xywh(4, 3, 5, 1));

        //---- modeComboBox ----
        modeComboBox.setModel(new DefaultComboBoxModel(new String[] {
            "SingleMode",
            "MultiMode"
        }));
        modeComboBox.setToolTipText("Select mode");
        add(modeComboBox, CC.xywh(10, 3, 3, 1));

        //---- label24 ----
        label24.setText("Folder Directory:");
        add(label24, CC.xywh(4, 4, 5, 1, CC.LEFT, CC.CENTER));

        //---- textfieldImportDir ----
        textfieldImportDir.setEditable(false);
        add(textfieldImportDir, CC.xywh(4, 5, 9, 1));

        //---- btnImportPath ----
        btnImportPath.setText("...");
        add(btnImportPath, CC.xy(14, 5, CC.LEFT, CC.DEFAULT));

        //---- labelImportStatus ----
        labelImportStatus.setText("Wrong Format");
        labelImportStatus.setEnabled(false);
        add(labelImportStatus, CC.xy(16, 5, CC.LEFT, CC.DEFAULT));

        //---- label31 ----
        label31.setText("Export folder:");
        add(label31, CC.xywh(4, 6, 5, 1, CC.LEFT, CC.CENTER));

        //---- textfieldExportDir ----
        textfieldExportDir.setEditable(false);
        add(textfieldExportDir, CC.xywh(4, 7, 9, 1));

        //---- btnOutputPath ----
        btnOutputPath.setText("...");
        add(btnOutputPath, CC.xy(14, 7, CC.LEFT, CC.DEFAULT));

        //---- labelExportStatus ----
        labelExportStatus.setText("Wrong location");
        labelExportStatus.setEnabled(false);
        add(labelExportStatus, CC.xy(16, 7, CC.LEFT, CC.DEFAULT));

        //---- label25 ----
        label25.setText("Magnification");
        label25.setFont(label25.getFont().deriveFont(label25.getFont().getStyle() | Font.BOLD));
        add(label25, CC.xywh(2, 9, 5, 1));
        add(separator10, CC.xywh(8, 9, 9, 1));

        //---- labelSetMagnification ----
        labelSetMagnification.setText("Set Magnification:");
        add(labelSetMagnification, CC.xywh(4, 10, 3, 1, CC.LEFT, CC.CENTER));

        //---- btnMagnification40 ----
        btnMagnification40.setText("x40");
        btnMagnification40.setMinimumSize(new Dimension(40, 28));
        btnMagnification40.setSelected(true);
        add(btnMagnification40, CC.xy(10, 10));

        //---- btnMagnification10 ----
        btnMagnification10.setText("x10");
        btnMagnification10.setMinimumSize(new Dimension(40, 28));
        add(btnMagnification10, CC.xy(12, 10));

        //---- btnConvert ----
        btnConvert.setText("Convert");
        add(btnConvert, CC.xy(16, 10));

        //---- btnRoiSelect ----
        btnRoiSelect.setText("Select ROI");
        btnRoiSelect.setEnabled(false);

        //---- labelRoiStatus ----
        labelRoiStatus.setText("Roi succesfull selected!");
        labelRoiStatus.setEnabled(false);

        //---- checkBoxRoi ----
        checkBoxRoi.setText("Select Roi");

        //---- label19 ----
        label19.setText("Set ROI");
        label19.setFont(label19.getFont().deriveFont(label19.getFont().getStyle() | Font.BOLD));

        //---- label28 ----
        label28.setText("Output");
        label28.setFont(label28.getFont().deriveFont(label28.getFont().getStyle() | Font.BOLD));

        //---- checkBoxHeatmap ----
        checkBoxHeatmap.setText("Heatmap");

        //---- checkBox1 ----
        checkBox1.setText("Summary:");

        //---- comboBox3 ----
        comboBox3.setModel(new DefaultComboBoxModel(new String[] {
            "TXT",
            "CSV"
        }));

        //---- labelResolution ----
        labelResolution.setText("Quality:");
        labelResolution.setEnabled(false);

        //---- spinnerSliderValue ----
        spinnerSliderValue.setModel(new SpinnerNumberModel(0, 0, 100, 1));

        //---- sliderHeatmap ----
        sliderHeatmap.setMinimumSize(new Dimension(20, 16));
        sliderHeatmap.setPreferredSize(new Dimension(170, 16));
        sliderHeatmap.setMinorTickSpacing(1);
        sliderHeatmap.setValue(0);
        sliderHeatmap.setEnabled(false);

        //======== menu2 ========
        {
            menu2.setText("Ausgabe");

            //---- checkBox2 ----
            checkBox2.setText("#");
            menu2.add(checkBox2);

            //---- checkBox3 ----
            checkBox3.setText("X-Coordinates");
            menu2.add(checkBox3);

            //---- checkBox4 ----
            checkBox4.setText("Y-Coordinates");
            menu2.add(checkBox4);

            //---- checkBox5 ----
            checkBox5.setText("Roundness");
            menu2.add(checkBox5);
        }

        //---- buttonGroupMagnification ----
        ButtonGroup buttonGroupMagnification = new ButtonGroup();
        buttonGroupMagnification.add(btnMagnification40);
        buttonGroupMagnification.add(btnMagnification10);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label23;
    private JPopupMenu.Separator separator9;
    private JLabel label17;
    private JComboBox modeComboBox;
    private JLabel label24;
    private JTextField textfieldImportDir;
    private JButton btnImportPath;
    private JLabel labelImportStatus;
    private JLabel label31;
    private JTextField textfieldExportDir;
    private JButton btnOutputPath;
    private JLabel labelExportStatus;
    private JLabel label25;
    private JSeparator separator10;
    private JLabel labelSetMagnification;
    private JRadioButton btnMagnification40;
    private JRadioButton btnMagnification10;
    private JButton btnConvert;
    private JButton btnRoiSelect;
    private JPopupMenu.Separator separator5;
    private JLabel labelRoiStatus;
    private JCheckBox checkBoxRoi;
    private JLabel label19;
    private JLabel label28;
    private JPopupMenu.Separator separator12;
    private JCheckBox checkBoxHeatmap;
    private JCheckBox checkBox1;
    private JComboBox comboBox3;
    private JLabel labelResolution;
    private JSpinner spinnerSliderValue;
    private JSlider sliderHeatmap;
    private JMenu menu2;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private AbstractAction convertAction;
    private AbstractAction outputPathAction;
    private AbstractAction typeAction;
    private AbstractAction inputPathAction;
    private AbstractAction magnificationAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
