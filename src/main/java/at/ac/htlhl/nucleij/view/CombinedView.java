/*
 * Created by JFormDesigner on Fri Dec 16 08:24:10 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.layout.*;
import net.miginfocom.swing.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class CombinedView extends JPanel
{
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public CombinedView(JPanel converterPanel, JPanel analyzerPanel)
    {
        initComponents();

        add(converterPanel, "cell 0 0");
        add(analyzerPanel, "cell 0 1");
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]",
            // rows
            "[]0" +
            "[]"));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
