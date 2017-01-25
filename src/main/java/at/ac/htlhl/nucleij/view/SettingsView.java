/*
 * Created by JFormDesigner on Tue Jan 24 23:38:46 CET 2017
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class SettingsView extends JDialog {
    public SettingsView(Frame owner) {
        super(owner);
        initComponents();
    }

    public SettingsView(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
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
        checkBox13 = new JCheckBox();
        checkBox9 = new JCheckBox();
        checkBox14 = new JCheckBox();
        checkBox2 = new JCheckBox();
        checkBox11 = new JCheckBox();
        checkBox4 = new JCheckBox();
        checkBox8 = new JCheckBox();
        checkBox10 = new JCheckBox();
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

                //---- checkBox13 ----
                checkBox13.setText("# Nummer");
                contentPanel.add(checkBox13, CC.xy(3, 15));

                //---- checkBox9 ----
                checkBox9.setText("Area");
                contentPanel.add(checkBox9, CC.xy(5, 15));

                //---- checkBox14 ----
                checkBox14.setText("Perim.");
                contentPanel.add(checkBox14, CC.xy(7, 15));

                //---- checkBox2 ----
                checkBox2.setText("Roundnes");
                contentPanel.add(checkBox2, CC.xy(3, 17));

                //---- checkBox11 ----
                checkBox11.setText("Width/Height");
                contentPanel.add(checkBox11, CC.xy(5, 17));

                //---- checkBox4 ----
                checkBox4.setText("X/Y-Coordinates");
                contentPanel.add(checkBox4, CC.xy(7, 17));

                //---- checkBox8 ----
                checkBox8.setText("Circularity");
                contentPanel.add(checkBox8, CC.xy(3, 19));

                //---- checkBox10 ----
                checkBox10.setText("Solidity");
                contentPanel.add(checkBox10, CC.xy(5, 19));

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
    private JCheckBox checkBox13;
    private JCheckBox checkBox9;
    private JCheckBox checkBox14;
    private JCheckBox checkBox2;
    private JCheckBox checkBox11;
    private JCheckBox checkBox4;
    private JCheckBox checkBox8;
    private JCheckBox checkBox10;
    private JLabel label2;
    private JComboBox<String> comboBox3;
    private JPanel buttonBar;
    private JButton okButton2;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
