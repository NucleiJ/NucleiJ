/*
 * Created by JFormDesigner on Tue Jan 24 23:38:46 CET 2017
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import javax.swing.*;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Settings;
import at.ac.htlhl.nucleij.presenter.SettingsPM;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class SettingsView extends JDialog {

    private SettingsPM settingsPM;
    private Settings settings;

    public SettingsView(Frame owner, SettingsPM settingsPM, Settings settings) {
        super(owner);

        this.settingsPM = settingsPM;
        this.settings = settings;

        initComponents();
        initBinding();
    }

    public SettingsView(Dialog owner, SettingsPM settingsPM, Settings settings) {
        super(owner);

        this.settingsPM = settingsPM;
        this.settings = settings;

        initComponents();
        initBinding();
    }

    private void initBinding()
    {
        PresentationModelBinder binder = Binders.binderFor(settingsPM);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_NUMBER).to(numberCheckBox);

    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
        changeNumberAction = (AbstractAction) settingsPM.getNumberAction();
        changeAreaAction = (AbstractAction) settingsPM.getAreaAction();
        changeCircularityAction = (AbstractAction) settingsPM.getCircularityAction();
        changePerimeterAction = (AbstractAction) settingsPM.getPerimeterAction();
        changeRoundnessAction = (AbstractAction) settingsPM.getRoundnessAction();
        changeSolidityAction = (AbstractAction) settingsPM.getSolidityAction();
        changeXYCoordinatesAction = (AbstractAction) settingsPM.getXyCoordinatesAction();
        changeWidthHeightAction = (AbstractAction) settingsPM.getWidthHeightAction();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        separator3 = compFactory.createSeparator("General");
        folderDirectoryLabel = new JLabel();
        inputDirTextField = new JTextField();
        button1 = new JButton();
        folderDirectoryLabel2 = new JLabel();
        textField1 = new JTextField();
        button2 = new JButton();
        label1 = new JLabel();
        textField2 = new JTextField();
        separator4 = compFactory.createSeparator("Language");
        exportFolder = new JLabel();
        comboBox1 = new JComboBox<>();
        separator2 = compFactory.createSeparator("Advanced");
        setMagnificationLabel = new JLabel();
        numberCheckBox = new JCheckBox();
        areaCheckBox = new JCheckBox();
        perimeterCheckBox = new JCheckBox();
        roundnessCheckBox = new JCheckBox();
        widthheightCheckBox = new JCheckBox();
        xycoordinatesCheckBox = new JCheckBox();
        circularityCheckBox = new JCheckBox();
        solidityCheckBox = new JCheckBox();
        label2 = new JLabel();
        comboBox3 = new JComboBox<>();
        buttonBar = new JPanel();
        okButton2 = new JButton();
        okButton = new JButton();
        cancelButton = new JButton();
        helpButton = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.createEmptyBorder("7dlu, 7dlu, 7dlu, 7dlu"));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "68dlu, $lcgap, 31dlu:grow, 2*($lcgap, default:grow)",
                    "10*(default, $lgap), default"));
                ((FormLayout)contentPanel.getLayout()).setColumnGroups(new int[][] {{3, 5, 7}});
                contentPanel.add(separator3, CC.xywh(1, 1, 7, 1));

                //---- folderDirectoryLabel ----
                folderDirectoryLabel.setText("Output Name:");
                contentPanel.add(folderDirectoryLabel, CC.xy(1, 3, CC.RIGHT, CC.DEFAULT));

                //---- inputDirTextField ----
                inputDirTextField.setText("Output");
                contentPanel.add(inputDirTextField, CC.xywh(3, 3, 3, 1));

                //---- button1 ----
                button1.setText("Reset");
                contentPanel.add(button1, CC.xy(7, 3, CC.LEFT, CC.DEFAULT));

                //---- folderDirectoryLabel2 ----
                folderDirectoryLabel2.setText("Standard Folder:");
                contentPanel.add(folderDirectoryLabel2, CC.xy(1, 5, CC.RIGHT, CC.DEFAULT));

                //---- textField1 ----
                textField1.setText("C:/User/Andreas/");
                contentPanel.add(textField1, CC.xywh(3, 5, 3, 1));

                //---- button2 ----
                button2.setText("Reset");
                contentPanel.add(button2, CC.xy(7, 5, CC.LEFT, CC.DEFAULT));

                //---- label1 ----
                label1.setText("Convert Name:");
                contentPanel.add(label1, CC.xy(1, 7, CC.RIGHT, CC.DEFAULT));

                //---- textField2 ----
                textField2.setText("_x10.OME");
                contentPanel.add(textField2, CC.xy(3, 7));
                contentPanel.add(separator4, CC.xywh(1, 9, 7, 1));

                //---- exportFolder ----
                exportFolder.setText("Source:");
                contentPanel.add(exportFolder, CC.xy(1, 11, CC.RIGHT, CC.DEFAULT));

                //---- comboBox1 ----
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "EN",
                    "DE",
                    "FR",
                    "CZ",
                    "ES"
                }));
                contentPanel.add(comboBox1, CC.xy(3, 11));
                contentPanel.add(separator2, CC.xywh(1, 13, 7, 1));

                //---- setMagnificationLabel ----
                setMagnificationLabel.setText("Summary Exports:");
                contentPanel.add(setMagnificationLabel, CC.xy(1, 15, CC.RIGHT, CC.DEFAULT));

                //---- numberCheckBox ----
                numberCheckBox.setText("# Number");
                contentPanel.add(numberCheckBox, CC.xy(3, 15));

                //---- areaCheckBox ----
                areaCheckBox.setText("Area");
                contentPanel.add(areaCheckBox, CC.xy(5, 15));

                //---- perimeterCheckBox ----
                perimeterCheckBox.setText("Perimeter");
                contentPanel.add(perimeterCheckBox, CC.xy(7, 15));

                //---- roundnessCheckBox ----
                roundnessCheckBox.setText("Roundness");
                contentPanel.add(roundnessCheckBox, CC.xy(3, 17));

                //---- widthheightCheckBox ----
                widthheightCheckBox.setText("Width/Height");
                contentPanel.add(widthheightCheckBox, CC.xy(5, 17));

                //---- xycoordinatesCheckBox ----
                xycoordinatesCheckBox.setText("X/Y-Coordinates");
                contentPanel.add(xycoordinatesCheckBox, CC.xy(7, 17));

                //---- circularityCheckBox ----
                circularityCheckBox.setText("Circularity");
                contentPanel.add(circularityCheckBox, CC.xy(3, 19));

                //---- solidityCheckBox ----
                solidityCheckBox.setText("Solidity");
                contentPanel.add(solidityCheckBox, CC.xy(5, 19));

                //---- label2 ----
                label2.setText("Summary Type:");
                contentPanel.add(label2, CC.xy(1, 21, CC.RIGHT, CC.DEFAULT));

                //---- comboBox3 ----
                comboBox3.setModel(new DefaultComboBoxModel<>(new String[] {
                    "CSV",
                    "TXT ",
                    "CSV & TXT"
                }));
                contentPanel.add(comboBox3, CC.xy(3, 21));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("5dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$lcgap, $button, $glue, 2*($button, $rgap), $button",
                    "pref"));

                //---- okButton2 ----
                okButton2.setText("Reset all");
                buttonBar.add(okButton2, CC.xy(2, 1));

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, CC.xy(4, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, CC.xy(6, 1));

                //---- helpButton ----
                helpButton.setText("Help");
                buttonBar.add(helpButton, CC.xy(8, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JComponent separator3;
    private JLabel folderDirectoryLabel;
    private JTextField inputDirTextField;
    private JButton button1;
    private JLabel folderDirectoryLabel2;
    private JTextField textField1;
    private JButton button2;
    private JLabel label1;
    private JTextField textField2;
    private JComponent separator4;
    private JLabel exportFolder;
    private JComboBox<String> comboBox1;
    private JComponent separator2;
    private JLabel setMagnificationLabel;
    private JCheckBox numberCheckBox;
    private JCheckBox areaCheckBox;
    private JCheckBox perimeterCheckBox;
    private JCheckBox roundnessCheckBox;
    private JCheckBox widthheightCheckBox;
    private JCheckBox xycoordinatesCheckBox;
    private JCheckBox circularityCheckBox;
    private JCheckBox solidityCheckBox;
    private JLabel label2;
    private JComboBox<String> comboBox3;
    private JPanel buttonBar;
    private JButton okButton2;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    private AbstractAction changeNumberAction;
    private AbstractAction changeRoundnessAction;
    private AbstractAction changeCircularityAction;
    private AbstractAction changeAreaAction;
    private AbstractAction changeWidthHeightAction;
    private AbstractAction changeSolidityAction;
    private AbstractAction changePerimeterAction;
    private AbstractAction changeXYCoordinatesAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
