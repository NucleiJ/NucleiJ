package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
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

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.ndpiconverter");
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
        btnConvert.setAction(convertAction);
        add(btnConvert, CC.xy(16, 10));

        //---- convertAction ----
        convertAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.convertAction.Name"));

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
    private AbstractAction convertAction;
    private AbstractAction outputPathAction;
    private AbstractAction typeAction;
    private AbstractAction inputPathAction;
    private AbstractAction magnificationAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
