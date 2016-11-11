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
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menu2 = new JMenu();
        toolBar1 = new JToolBar();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        panel2 = new JPanel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("File");
            }
            menuBar1.add(menu1);

            //======== menu2 ========
            {
                menu2.setText("Help");
            }
            menuBar1.add(menu2);
        }
        setJMenuBar(menuBar1);

        //======== toolBar1 ========
        {
            toolBar1.setFloatable(false);

            //---- button1 ----
            button1.setText("Convert & Analyze");
            toolBar1.add(button1);

            //---- button2 ----
            button2.setText("Analyse");
            toolBar1.add(button2);

            //---- button3 ----
            button3.setText("Convert");
            toolBar1.add(button3);
        }
        contentPane.add(toolBar1, BorderLayout.NORTH);

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {
                panel1.setLayout(new BorderLayout());
            }
            tabbedPane1.addTab("Converter", panel1);

            //======== panel2 ========
            {
                panel2.setLayout(new BorderLayout());
            }
            tabbedPane1.addTab("Analyzer", panel2);
        }
        contentPane.add(tabbedPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenu menu2;
    private JToolBar toolBar1;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
