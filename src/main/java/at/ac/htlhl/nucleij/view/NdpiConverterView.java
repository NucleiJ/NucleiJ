package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.NdpiConverterPM;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import net.miginfocom.swing.*;

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
        binder.bindBeanProperty(NdpiConverter.PROPERTY_INPUTPATH).to(inputDirTextField);
        binder.bindBeanProperty(NdpiConverter.PROPERTY_OUTPUTPATH).to(outputDirTextField);

        ValueModel typeValueModel =  ndpiConverterPM.getComponentModel(NdpiConverter.PROPERTY_TYPE);
        ComboBoxAdapter comboBoxAdapter = new ComboBoxAdapter(NdpiConverter.TYPE_CHOICES, typeValueModel);
        typeComboBox.setModel(comboBoxAdapter);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.ndpiconverter");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        separator3 = compFactory.createSeparator(bundle.getString("NdpiConverterView.InputSeperator.text"));
        analyzeTypeLabel = new JLabel();
        typeComboBox = new JComboBox();
        folderDirectoryLabel = new JLabel();
        inputDirTextField = new JTextField();
        inputPathButton = new JButton();
        separator4 = compFactory.createSeparator(bundle.getString("NdpiConverterView.OutputSeperator.Text"));
        label1 = new JLabel();
        checkBox1 = new JCheckBox();
        textField1 = new JTextField();
        exportFolder = new JLabel();
        outputDirTextField = new JTextField();
        outputPathButton = new JButton();
        separator2 = compFactory.createSeparator(bundle.getString("NdpiConverterView.MagnificationSeperator.text"));
        setMagnificationLabel = new JLabel();
        panel1 = new JPanel();
        magnification40Button = new JRadioButton();
        magnification10Button = new JRadioButton();
        magnification5Button = new JRadioButton();
        convertButton = new JButton();

        //======== this ========
        setBorder(Borders.DIALOG);
        setLayout(new FormLayout(
            "right:59dlu, $lcgap, right:15dlu, $lcgap, 63dlu, $lcgap, 31dlu:grow, $lcgap, 24dlu, $lcgap, default",
            "18dlu, $lgap, pref, $lgap, 18dlu, 2*($lgap, default), 3*($lgap, pref)"));
        ((FormLayout)getLayout()).setRowGroups(new int[][] {{1, 7, 11, 13}, {4, 8}});
        add(separator3, CC.xywh(1, 1, 11, 1));

        //---- analyzeTypeLabel ----
        analyzeTypeLabel.setText(bundle.getString("NdpiConverterView.analyzeTypeLabel.text"));
        add(analyzeTypeLabel, CC.xy(1, 3, CC.RIGHT, CC.DEFAULT));

        //---- typeComboBox ----
        typeComboBox.setModel(new DefaultComboBoxModel(new String[] {
            "Single File",
            "Single Dir",
            "Multi File",
            "Multi Dir"
        }));
        typeComboBox.setToolTipText("Select mode");
        typeComboBox.setAction(typeAction);
        add(typeComboBox, CC.xy(5, 3));

        //---- folderDirectoryLabel ----
        folderDirectoryLabel.setText(bundle.getString("NdpiConverterView.inputFolderDirectory.text"));
        add(folderDirectoryLabel, CC.xy(1, 5, CC.RIGHT, CC.CENTER));

        //---- inputDirTextField ----
        inputDirTextField.setEditable(false);
        add(inputDirTextField, CC.xywh(5, 5, 3, 1));

        //---- inputPathButton ----
        inputPathButton.setAction(inputPathAction);
        inputPathButton.setActionCommand(bundle.getString("NdpiConverterView.inputOutputPathAction.name"));
        add(inputPathButton, CC.xy(9, 5, CC.LEFT, CC.DEFAULT));
        add(separator4, CC.xywh(1, 7, 11, 1));

        //---- label1 ----
        label1.setText(bundle.getString("NdpiConverterView.customNameLabel.text"));
        add(label1, CC.xy(1, 9));
        add(checkBox1, CC.xy(3, 9));
        add(textField1, CC.xywh(5, 9, 3, 1));

        //---- exportFolder ----
        exportFolder.setText(bundle.getString("NdpiConverterView.OutputFolder.text"));
        add(exportFolder, CC.xy(1, 11, CC.RIGHT, CC.CENTER));

        //---- outputDirTextField ----
        outputDirTextField.setEditable(false);
        add(outputDirTextField, CC.xywh(5, 11, 3, 1));

        //---- outputPathButton ----
        outputPathButton.setAction(outputPathAction);
        outputPathButton.setActionCommand(bundle.getString("NdpiConverterView.inputOutputPathAction.name"));
        add(outputPathButton, CC.xy(9, 11, CC.LEFT, CC.DEFAULT));
        add(separator2, CC.xywh(1, 13, 11, 1));

        //---- setMagnificationLabel ----
        setMagnificationLabel.setText(bundle.getString("NdpiConverterView.setMagnificationLabel.text"));
        add(setMagnificationLabel, CC.xywh(1, 15, 3, 1, CC.RIGHT, CC.CENTER));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                "2*(default:grow, $lcgap), default:grow",
                "default"));

            //---- magnification40Button ----
            magnification40Button.setText("x40");
            magnification40Button.setMinimumSize(new Dimension(40, 28));
            magnification40Button.setSelected(true);
            panel1.add(magnification40Button, CC.xy(1, 1));

            //---- magnification10Button ----
            magnification10Button.setText("x10");
            magnification10Button.setMinimumSize(new Dimension(40, 28));
            panel1.add(magnification10Button, CC.xy(3, 1));

            //---- magnification5Button ----
            magnification5Button.setText("x5");
            magnification5Button.setMinimumSize(new Dimension(40, 28));
            panel1.add(magnification5Button, CC.xy(5, 1));
        }
        add(panel1, CC.xywh(5, 15, 3, 1));

        //---- convertButton ----
        convertButton.setAction(convertAction);
        add(convertButton, CC.xywh(9, 15, 3, 1));

        //---- convertAction ----
        convertAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.convertAction.name"));

        //---- outputPathAction ----
        outputPathAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.inputOutputPathAction.name"));

        //---- inputPathAction ----
        inputPathAction.putValue(Action.NAME, bundle.getString("NdpiConverterView.inputOutputPathAction.name"));

        //---- buttonGroupMagnification ----
        ButtonGroup buttonGroupMagnification = new ButtonGroup();
        buttonGroupMagnification.add(magnification40Button);
        buttonGroupMagnification.add(magnification10Button);
        buttonGroupMagnification.add(magnification5Button);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComponent separator3;
    private JLabel analyzeTypeLabel;
    private JComboBox typeComboBox;
    private JLabel folderDirectoryLabel;
    private JTextField inputDirTextField;
    private JButton inputPathButton;
    private JComponent separator4;
    private JLabel label1;
    private JCheckBox checkBox1;
    private JTextField textField1;
    private JLabel exportFolder;
    private JTextField outputDirTextField;
    private JButton outputPathButton;
    private JComponent separator2;
    private JLabel setMagnificationLabel;
    private JPanel panel1;
    private JRadioButton magnification40Button;
    private JRadioButton magnification10Button;
    private JRadioButton magnification5Button;
    private JButton convertButton;
    private AbstractAction convertAction;
    private AbstractAction outputPathAction;
    private AbstractAction typeAction;
    private AbstractAction inputPathAction;
    private AbstractAction magnificationAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
