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
        separator4 = compFactory.createSeparator("Seperator");
        exportFolder = new JLabel();
        outputDirTextField = new JTextField();
        separator2 = compFactory.createSeparator("Seperator");
        setMagnificationLabel = new JLabel();
        magnification40Button = new JRadioButton();
        magnification10Button = new JRadioButton();
        magnification5Button = new JRadioButton();
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
                    "5*(default, $lgap), default"));
                ((FormLayout)contentPanel.getLayout()).setColumnGroups(new int[][] {{3, 5, 7}});
                contentPanel.add(separator3, CC.xywh(1, 1, 7, 1));

                //---- folderDirectoryLabel ----
                folderDirectoryLabel.setText("Output Folder Name");
                contentPanel.add(folderDirectoryLabel, CC.xy(1, 3));
                contentPanel.add(inputDirTextField, CC.xywh(3, 3, 5, 1));
                contentPanel.add(separator4, CC.xywh(1, 5, 7, 1));

                //---- exportFolder ----
                exportFolder.setText("Setting:");
                contentPanel.add(exportFolder, CC.xy(1, 7));
                contentPanel.add(outputDirTextField, CC.xywh(3, 7, 5, 1));
                contentPanel.add(separator2, CC.xywh(1, 9, 7, 1));

                //---- setMagnificationLabel ----
                setMagnificationLabel.setText("Setting:");
                contentPanel.add(setMagnificationLabel, CC.xy(1, 11));

                //---- magnification40Button ----
                magnification40Button.setMinimumSize(new Dimension(40, 28));
                magnification40Button.setSelected(true);
                magnification40Button.setActionCommand("x40");
                magnification40Button.setText("Opt1");
                contentPanel.add(magnification40Button, CC.xy(3, 11));

                //---- magnification10Button ----
                magnification10Button.setMinimumSize(new Dimension(40, 28));
                magnification10Button.setText("Opt2");
                contentPanel.add(magnification10Button, CC.xy(5, 11));

                //---- magnification5Button ----
                magnification5Button.setMinimumSize(new Dimension(40, 28));
                magnification5Button.setText("Opt3");
                contentPanel.add(magnification5Button, CC.xy(7, 11));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.createEmptyBorder("5dlu, 0dlu, 0dlu, 0dlu"));
                buttonBar.setLayout(new FormLayout(
                    "$lcgap, $button, $glue, 2*($button, $rgap), $button",
                    "pref"));

                //---- okButton2 ----
                okButton2.setText("Reset");
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
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroupMagnification ----
        ButtonGroup buttonGroupMagnification = new ButtonGroup();
        buttonGroupMagnification.add(magnification40Button);
        buttonGroupMagnification.add(magnification10Button);
        buttonGroupMagnification.add(magnification5Button);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JComponent separator3;
    private JLabel folderDirectoryLabel;
    private JTextField inputDirTextField;
    private JComponent separator4;
    private JLabel exportFolder;
    private JTextField outputDirTextField;
    private JComponent separator2;
    private JLabel setMagnificationLabel;
    private JRadioButton magnification40Button;
    private JRadioButton magnification10Button;
    private JRadioButton magnification5Button;
    private JPanel buttonBar;
    private JButton okButton2;
    private JButton okButton;
    private JButton cancelButton;
    private JButton helpButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
