/*
 * Created by JFormDesigner on Tue Jan 24 23:38:46 CET 2017
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.util.Set;
import javax.swing.*;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Settings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import at.ac.htlhl.nucleij.presenter.SettingsPM;
import com.jgoodies.binding.binder.Binders;
import com.jgoodies.binding.binder.PresentationModelBinder;
import com.jgoodies.binding.value.ValueModel;
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

        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_OUTPUTNAME).to(outputNameTextField);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_STANDARD_FOLDER).to(standardDirTextField);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_NUMBER).to(numberCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_AREA).to(areaCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_PERIMETER).to(perimeterCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_ROUNDNESS).to(roundnessCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_WIDTHHEIGHT).to(widthheightCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_XYCOORDINATES).to(xycoordinatesCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_CIRCULARITY).to(circularityCheckBox);
        binder.bindBeanProperty(Settings.PROPERTY_SETTINGS_SOLIDITY).to(solidityCheckBox);

        ValueModel summaryTypeModel = settingsPM.getModel(Settings.PROPERTY_SETTINGS_SUMMARYTYPE);
        ComboBoxAdapter summaryTypeComboBoxAdapter = new ComboBoxAdapter(Settings.TYPE_SETTINGS_CHOICES, summaryTypeModel);
        summaryTypeComboBox.setModel(summaryTypeComboBoxAdapter);

        ValueModel languageModel = settingsPM.getModel(Settings.PROPERTY_SETTINGS_LANGUAGE);
        ComboBoxAdapter languageComboBoxAdapter = new ComboBoxAdapter(Settings.LANGUAGE_SETTINGS_CHOICES, languageModel);
        languageComboBox.setModel(languageComboBoxAdapter);
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
        outputNameTextField = new JTextField();
        folderDirectoryLabel2 = new JLabel();
        standardDirTextField = new JTextField();
        button2 = new JButton();
        separator4 = compFactory.createSeparator("Language");
        exportFolder = new JLabel();
        languageComboBox = new JComboBox<>();
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
        summaryTypeComboBox = new JComboBox<>();
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
                    "9*(default, $lgap), default"));
                ((FormLayout)contentPanel.getLayout()).setColumnGroups(new int[][] {{3, 5, 7}});
                contentPanel.add(separator3, CC.xywh(1, 1, 7, 1));

                //---- folderDirectoryLabel ----
                folderDirectoryLabel.setText("Output Name:");
                contentPanel.add(folderDirectoryLabel, CC.xy(1, 3, CC.RIGHT, CC.DEFAULT));

                //---- outputNameTextField ----
                outputNameTextField.setText("Output");
                contentPanel.add(outputNameTextField, CC.xywh(3, 3, 3, 1));

                //---- folderDirectoryLabel2 ----
                folderDirectoryLabel2.setText("Standard Folder:");
                contentPanel.add(folderDirectoryLabel2, CC.xy(1, 5, CC.RIGHT, CC.DEFAULT));

                //---- standardDirTextField ----
                standardDirTextField.setText("C:/User/Andreas/");
                contentPanel.add(standardDirTextField, CC.xywh(3, 5, 3, 1));

                //---- button2 ----
                button2.setText("...");
                contentPanel.add(button2, CC.xy(7, 5, CC.LEFT, CC.DEFAULT));
                contentPanel.add(separator4, CC.xywh(1, 7, 7, 1));

                //---- exportFolder ----
                exportFolder.setText("Source:");
                contentPanel.add(exportFolder, CC.xy(1, 9, CC.RIGHT, CC.DEFAULT));

                //---- languageComboBox ----
                languageComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                    "EN",
                    "DE",
                    "FR",
                    "CZ",
                    "ES"
                }));
                contentPanel.add(languageComboBox, CC.xy(3, 9));
                contentPanel.add(separator2, CC.xywh(1, 11, 7, 1));

                //---- setMagnificationLabel ----
                setMagnificationLabel.setText("Summary Exports:");
                contentPanel.add(setMagnificationLabel, CC.xy(1, 13, CC.RIGHT, CC.DEFAULT));

                //---- numberCheckBox ----
                numberCheckBox.setText("# Number");
                contentPanel.add(numberCheckBox, CC.xy(3, 13));

                //---- areaCheckBox ----
                areaCheckBox.setText("Area");
                contentPanel.add(areaCheckBox, CC.xy(5, 13));

                //---- perimeterCheckBox ----
                perimeterCheckBox.setText("Perimeter");
                contentPanel.add(perimeterCheckBox, CC.xy(7, 13));

                //---- roundnessCheckBox ----
                roundnessCheckBox.setText("Roundness");
                contentPanel.add(roundnessCheckBox, CC.xy(3, 15));

                //---- widthheightCheckBox ----
                widthheightCheckBox.setText("Width/Height");
                contentPanel.add(widthheightCheckBox, CC.xy(5, 15));

                //---- xycoordinatesCheckBox ----
                xycoordinatesCheckBox.setText("X/Y-Coordinates");
                contentPanel.add(xycoordinatesCheckBox, CC.xy(7, 15));

                //---- circularityCheckBox ----
                circularityCheckBox.setText("Circularity");
                contentPanel.add(circularityCheckBox, CC.xy(3, 17));

                //---- solidityCheckBox ----
                solidityCheckBox.setText("Solidity");
                contentPanel.add(solidityCheckBox, CC.xy(5, 17));

                //---- label2 ----
                label2.setText("Summary Type:");
                contentPanel.add(label2, CC.xy(1, 19, CC.RIGHT, CC.DEFAULT));

                //---- summaryTypeComboBox ----
                summaryTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                    "CSV",
                    "TXT ",
                    "CSV & TXT"
                }));
                contentPanel.add(summaryTypeComboBox, CC.xy(3, 19));
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
                cancelButton.setText("Apply");
                buttonBar.add(cancelButton, CC.xy(6, 1));

                //---- helpButton ----
                helpButton.setText("Cancel");
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
    private JTextField outputNameTextField;
    private JLabel folderDirectoryLabel2;
    private JTextField standardDirTextField;
    private JButton button2;
    private JComponent separator4;
    private JLabel exportFolder;
    private JComboBox<String> languageComboBox;
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
    private JComboBox<String> summaryTypeComboBox;
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
