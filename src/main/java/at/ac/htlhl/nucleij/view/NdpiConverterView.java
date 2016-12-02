package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.NdpiConverterPM;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import net.miginfocom.swing.*;

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

        ValueModel typeValueModel =  ndpiConverterPM.getComponentModel(NdpiConverter.PROPERTY_TYPE);
        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(NdpiConverter.TYPE_CHOICES, typeValueModel);
        typeComboBox.setModel(comboBoxAdapter);

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.ndpiconverter");
        general = new JLabel();
        separator1 = new JSeparator();
        separator9 = new JPopupMenu.Separator();
        analyzeType = new JLabel();
        typeComboBox = new JComboBox();
        filderDirectory = new JLabel();
        textfieldImportDir = new JTextField();
        btnImportPath = new JButton();
        importError = new JLabel();
        exportFolder = new JLabel();
        textfieldExportDir = new JTextField();
        btnOutputPath = new JButton();
        exportError = new JLabel();
        magnification = new JLabel();
        separator10 = new JSeparator();
        setMagnification = new JLabel();
        btnMagnification40 = new JRadioButton();
        btnMagnification10 = new JRadioButton();
        btnConvert = new JButton();

        //======== this ========
        setLayout(new MigLayout(
            "insets 0,hidemode 3",
            "rel[13,fill][83,left][39,left][57,left][65,fill][57,fill][52,grow,right][116,grow,right]rel",
            "rel[sizegroup 1,center]0[center]0[30,center]0[sizegroup 2,center]0[center]0[sizegroup 2,center]unrel[sizegroup 1,center]0[min,sizegroup 3,center]unrel"));

        //---- general ----
        general.setText(bundle.getString("NdpiConverterView.generalLabel.text"));
        general.setFont(general.getFont().deriveFont(general.getFont().getStyle() | Font.BOLD));
        add(general, "cell 0 0 2 1,alignx left,growx 0");
        add(separator1, "cell 2 0 6 1,growx");
        add(separator9, "cell 2 0 6 1,growx");

        //---- analyzeType ----
        analyzeType.setText(bundle.getString("NdpiConverterView.analyzeTypeLabel.text"));
        add(analyzeType, "cell 1 1 3 1,alignx left,growx 0");

        //---- typeComboBox ----
        typeComboBox.setModel(new DefaultComboBoxModel(new String[] {
            "Single File",
            "Single Dir",
            "Multi File",
            "Multi Dir"
        }));
        typeComboBox.setToolTipText("Select mode");
        typeComboBox.setAction(typeAction);
        add(typeComboBox, "cell 4 1 2 1");

        //---- filderDirectory ----
        filderDirectory.setText(bundle.getString("NdpiConverterView.inputFolderDirectory.text"));
        add(filderDirectory, "cell 1 2 3 1,align left center,grow 0 0");

        //---- textfieldImportDir ----
        textfieldImportDir.setEditable(false);
        add(textfieldImportDir, "cell 1 3 5 1,growx");

        //---- btnImportPath ----
        btnImportPath.setAction(inputPathAction);
        add(btnImportPath, "cell 6 3,alignx left,growx 0");

        //---- importError ----
        importError.setText(bundle.getString("NdpiConverterView.inputOutputError"));
        importError.setEnabled(false);
        add(importError, "cell 7 3,alignx left,growx 0");

        //---- exportFolder ----
        exportFolder.setText(bundle.getString("NdpiConverterView.outputFolderDirectory"));
        add(exportFolder, "cell 1 4 3 1,align left center,grow 0 0");

        //---- textfieldExportDir ----
        textfieldExportDir.setEditable(false);
        add(textfieldExportDir, "cell 1 5 5 1,growx");

        //---- btnOutputPath ----
        btnOutputPath.setAction(outputPathAction);
        add(btnOutputPath, "cell 6 5,alignx left,growx 0");

        //---- exportError ----
        exportError.setText(bundle.getString("NdpiConverterView.inputOutputError"));
        exportError.setEnabled(false);
        add(exportError, "cell 7 5,alignx left,growx 0");

        //---- magnification ----
        magnification.setText(bundle.getString("NdpiConverterView.magnificationLabel.text"));
        magnification.setFont(magnification.getFont().deriveFont(magnification.getFont().getStyle() | Font.BOLD));
        add(magnification, "cell 0 6 2 1,alignx left,growx 0");
        add(separator10, "cell 2 6 6 1,growx");

        //---- setMagnification ----
        setMagnification.setText(bundle.getString("NdpiConverterView.setMagnificationLabel.text"));
        add(setMagnification, "cell 1 7 2 1,align left center,grow 0 0");

        //---- btnMagnification40 ----
        btnMagnification40.setText("x40");
        btnMagnification40.setMinimumSize(new Dimension(40, 28));
        btnMagnification40.setSelected(true);
        add(btnMagnification40, "cell 4 7");

        //---- btnMagnification10 ----
        btnMagnification10.setText("x10");
        btnMagnification10.setMinimumSize(new Dimension(40, 28));
        add(btnMagnification10, "cell 5 7");

        //---- btnConvert ----
        btnConvert.setAction(convertAction);
        add(btnConvert, "cell 7 7");

        //---- convertAction ----
        convertAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.convertAction.name"));

        //---- outputPathAction ----
        outputPathAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.inputOutputPathAction.name"));

        //---- inputPathAction ----
        inputPathAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.inputOutputPathAction.name"));

        //---- buttonGroupMagnification ----
        ButtonGroup buttonGroupMagnification = new ButtonGroup();
        buttonGroupMagnification.add(btnMagnification40);
        buttonGroupMagnification.add(btnMagnification10);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel general;
    private JSeparator separator1;
    private JPopupMenu.Separator separator9;
    private JLabel analyzeType;
    private JComboBox typeComboBox;
    private JLabel filderDirectory;
    private JTextField textfieldImportDir;
    private JButton btnImportPath;
    private JLabel importError;
    private JLabel exportFolder;
    private JTextField textfieldExportDir;
    private JButton btnOutputPath;
    private JLabel exportError;
    private JLabel magnification;
    private JSeparator separator10;
    private JLabel setMagnification;
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
