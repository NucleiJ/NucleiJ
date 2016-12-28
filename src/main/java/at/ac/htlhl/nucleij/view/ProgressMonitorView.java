/*
 * Created by JFormDesigner on Wed Dec 28 21:07:25 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class ProgressMonitorView extends JDialog {
    public ProgressMonitorView(Frame owner) {
        super(owner);
        initComponents();
    }

    public ProgressMonitorView(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void btnProgressCancelActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panel1 = new JPanel();
        btnProgressCancel = new JButton();
        progressbar = new JProgressBar();
        listProgress = new JList();
        separator1 = compFactory.createSeparator("Progress");

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default:grow",
            "default"));

        //======== panel1 ========
        {
            panel1.setBorder(Borders.DIALOG);
            panel1.setLayout(new FormLayout(
                "default:grow",
                "3*(default, $lgap), default"));

            //---- btnProgressCancel ----
            btnProgressCancel.setText("Stop");
            btnProgressCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnProgressCancelActionPerformed(e);
                }
            });
            panel1.add(btnProgressCancel, CC.xy(1, 7, CC.RIGHT, CC.DEFAULT));

            //---- progressbar ----
            progressbar.setMinimumSize(new Dimension(10, 15));
            progressbar.setPreferredSize(new Dimension(146, 18));
            progressbar.setStringPainted(true);
            panel1.add(progressbar, CC.xy(1, 3, CC.FILL, CC.FILL));

            //---- listProgress ----
            listProgress.setVisibleRowCount(5);
            listProgress.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            listProgress.setModel(new AbstractListModel() {
                String[] values = {
                    "Bild 1 verarbeitet...                                                         OK",
                    "Bild 2 verarbeitet...                                                         OK",
                    "Bild 3 verarbeitet...                                                         OK"
                };
                @Override
                public int getSize() { return values.length; }
                @Override
                public Object getElementAt(int i) { return values[i]; }
            });
            panel1.add(listProgress, CC.xy(1, 5));
            panel1.add(separator1, CC.xy(1, 1));
        }
        contentPane.add(panel1, CC.xy(1, 1));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JButton btnProgressCancel;
    private JProgressBar progressbar;
    private JList listProgress;
    private JComponent separator1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
