/*
 * Created by JFormDesigner on Fri Nov 11 12:58:55 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class GLScanAnalzyerView extends JPanel {
    public GLScanAnalzyerView() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();

        //======== this ========
        setLayout(new FormLayout(
            "default, $lcgap, 101dlu",
            "2*(default, $lgap), default"));

        //---- label1 ----
        label1.setText("GLScanView");
        add(label1, CC.xy(3, 5));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
