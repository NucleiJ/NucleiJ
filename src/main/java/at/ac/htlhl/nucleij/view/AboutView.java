/*
 * Created by JFormDesigner on Fri Jan 20 21:33:38 CET 2017
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class AboutView extends JDialog {
    public AboutView(Frame owner) {
        super(owner);
        initComponents();
    }

    public AboutView(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        textField1 = new JTextField();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default, $lcgap, default",
            "2*(default, $lgap), default"));

        //---- textField1 ----
        textField1.setText("Test! Ha");
        contentPane.add(textField1, CC.xy(1, 3));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField textField1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
