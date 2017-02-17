/*
 * Created by JFormDesigner on Fri Nov 11 10:34:53 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import at.ac.htlhl.nucleij.presenter.MainPM;

/**
 * @author Schülerlizenz 2016/17
 */
public class MainFrameView extends JFrame {


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu settingsMenu;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JToolBar toolBar1;
    private JButton button2;
    private JButton button3;
    private JPanel hSpacer2;
    private JButton button4;
    private AbstractAction loadAction;
    private AbstractAction saveAction;
    private AbstractAction aboutAction;
    private AbstractAction exitAction;
    private AbstractAction infoAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private MainPM mainPM;

    public MainFrameView(MainPM mainPM)
    {
        this.mainPM = mainPM;

        initComponents();
    }

    private void createUIComponents() {
        loadAction = (AbstractAction) mainPM.getLoadAction();
        saveAction = (AbstractAction) mainPM.getSaveAction();
        aboutAction = (AbstractAction) mainPM.getAboutAction();
        exitAction = (AbstractAction) mainPM.getExitAction();
        infoAction = (AbstractAction) mainPM.getInfoAction();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.mainview");
        menuBar1 = new JMenuBar();
        settingsMenu = new JMenu();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        toolBar1 = new JToolBar();
        button2 = new JButton();
        button3 = new JButton();
        hSpacer2 = new JPanel(null);
        button4 = new JButton();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/icon_png_v1.png")).getImage());
        setMinimumSize(new Dimension(600, 600));
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== settingsMenu ========
            {
                settingsMenu.setText(bundle.getString("MainFrame.settingsMenu.text"));

                //---- openMenuItem ----
                openMenuItem.setText("text");
                openMenuItem.setAction(loadAction);
                settingsMenu.add(openMenuItem);

                //---- saveMenuItem ----
                saveMenuItem.setText("text");
                saveMenuItem.setAction(saveAction);
                settingsMenu.add(saveMenuItem);
                settingsMenu.addSeparator();

                //---- exitMenuItem ----
                exitMenuItem.setText("text");
                exitMenuItem.setAction(exitAction);
                settingsMenu.add(exitMenuItem);
            }
            menuBar1.add(settingsMenu);

            //======== helpMenu ========
            {
                helpMenu.setText(bundle.getString("MainFrame.helpMenu.text"));

                //---- aboutMenuItem ----
                aboutMenuItem.setText("text");
                aboutMenuItem.setAction(aboutAction);
                helpMenu.add(aboutMenuItem);
            }
            menuBar1.add(helpMenu);
        }
        setJMenuBar(menuBar1);

        //======== toolBar1 ========
        {
            toolBar1.setFloatable(false);

            //---- button2 ----
            button2.setText("Open...");
            button2.setAction(loadAction);
            toolBar1.add(button2);

            //---- button3 ----
            button3.setText("Save");
            button3.setAction(saveAction);
            toolBar1.add(button3);

            //---- hSpacer2 ----
            hSpacer2.setOpaque(false);
            toolBar1.add(hSpacer2);

            //---- button4 ----
            button4.setText("text");
            button4.setAction(infoAction);
            toolBar1.add(button4);
        }
        contentPane.add(toolBar1, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());

        //---- loadAction ----
        loadAction.putValue(Action.NAME, bundle.getString("MainFrame.loadAction.Name"));
        loadAction.putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/document-open.png")));
        loadAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/document-open.png")));

        //---- saveAction ----
        saveAction.putValue(Action.NAME, bundle.getString("MainFrame.saveAction.Name"));
        saveAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/document-save-as.png")));
        saveAction.putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/document-save-as.png")));

        //---- aboutAction ----
        aboutAction.putValue(Action.NAME, bundle.getString("MainFrame.aboutAction.Name"));
        aboutAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/contact-new.png")));
        aboutAction.putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/contact-new.png")));

        //---- exitAction ----
        exitAction.putValue(Action.NAME, bundle.getString("MainFrame.exitAction.Name"));
        exitAction.putValue(Action.LARGE_ICON_KEY, null);
        exitAction.putValue(Action.SMALL_ICON, null);

        //---- infoAction ----
        infoAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/help-browser.png")));
        infoAction.putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/help-browser.png")));
        infoAction.putValue(Action.NAME, "Info");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
