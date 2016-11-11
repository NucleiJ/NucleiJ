/*
 * Created by JFormDesigner on Fri Nov 11 10:34:53 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import javax.swing.*;

import at.ac.htlhl.nucleij.presenter.MainPM;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class MainFrame extends JFrame {

    private MainPM mainPM;

    public MainFrame(MainPM mainPM)
    {
        this.mainPM = mainPM;

        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- label1 ----
        label1.setText("NucleiJ");
        contentPane.add(label1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
