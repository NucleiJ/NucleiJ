/*
 * Created by JFormDesigner on Fri Nov 11 10:34:53 CET 2016
 */

package at.ac.htlhl.nucleij.view;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.View;

import at.ac.htlhl.nucleij.presenter.MainPM;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Schülerlizenz 2016/17
 */
public class MainFrameView extends JFrame {


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu settingsMenu;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem newMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JToolBar toolBar1;
    private JButton analyzeButton;
    private JButton convertButton;
    private AbstractAction loadAction;
    private AbstractAction saveAction;
    private AbstractAction aboutAction;
    private AbstractAction exitAction;
    private AbstractAction newAction;
    private AbstractAction enableAnalyzerViewAction;
    private AbstractAction enableConverterViewAction;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private MainPM mainPM;

    //public MainFrameView(MainPM mainPM, JPanel ndpiConverterView, JPanel glScanAnalyzerView)
    public MainFrameView(MainPM mainPM)
    {
        this.mainPM = mainPM;
        //this.converterPanel = ndpiConverterView;
        //this.analyzerPanel  = glScanAnalyzerView;

        initComponents();

        //mainTabbedPain.setComponentAt(0, new JButton("Hallo"));

    }
/*

    public JPanel getConverterPanel() {
        return converterPanel;
    }

    public void setConverterPanel(JPanel converterPanel) {
        this.converterPanel = converterPanel;
    }

    public JPanel getAnalyzerPanel() {
        return analyzerPanel;
    }

    public void setAnalyzerPanel(JPanel analyzerPanel) {
        this.analyzerPanel = analyzerPanel;
    }
*/

    private void createUIComponents() {
        loadAction = (AbstractAction) mainPM.getLoadAction();
        saveAction = (AbstractAction) mainPM.getSaveAction();
        aboutAction = (AbstractAction) mainPM.getAboutAction();
        exitAction = (AbstractAction) mainPM.getExitAction();
        newAction = (AbstractAction) mainPM.getNewAction();
        enableAnalyzerViewAction = (AbstractAction) mainPM.getEnableAnalyzerViewAction();
        enableConverterViewAction = (AbstractAction) mainPM.getEnableConverterViewAction();

    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.mainview");
        menuBar1 = new JMenuBar();
        settingsMenu = new JMenu();
        openMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        newMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        toolBar1 = new JToolBar();
        analyzeButton = new JButton();
        convertButton = new JButton();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/icon_png_v1.png")).getImage());
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

                //---- newMenuItem ----
                newMenuItem.setText("text");
                newMenuItem.setAction(newAction);
                settingsMenu.add(newMenuItem);
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

            //---- analyzeButton ----
            analyzeButton.setAction(enableAnalyzerViewAction);
            toolBar1.add(analyzeButton);

            //---- convertButton ----
            convertButton.setText("Convert");
            convertButton.setAction(enableConverterViewAction);
            toolBar1.add(convertButton);
        }
        contentPane.add(toolBar1, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());

        //---- loadAction ----
        loadAction.putValue(Action.NAME, bundle.getString("MainFrame.loadAction.Name"));
        loadAction.putValue("SwingLargeIconKey", new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/document-open.png")));
        loadAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/document-open.png")));

        //---- saveAction ----
        saveAction.putValue(Action.NAME, bundle.getString("MainFrame.saveAction.Name"));
        saveAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/document-save-as.png")));
        saveAction.putValue("SwingLargeIconKey", new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/document-save-as.png")));

        //---- aboutAction ----
        aboutAction.putValue(Action.NAME, bundle.getString("MainFrame.aboutAction.Name"));
        aboutAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/contact-new.png")));
        aboutAction.putValue("SwingLargeIconKey", new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/contact-new.png")));

        //---- exitAction ----
        exitAction.putValue(Action.NAME, bundle.getString("MainFrame.exitAction.Name"));
        exitAction.putValue("SwingLargeIconKey", new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/process-stop.png")));
        exitAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/process-stop.png")));

        //---- newAction ----
        newAction.putValue(Action.NAME, bundle.getString("MainFrame.newAction.Name"));
        newAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i16x16/document-new.png")));
        newAction.putValue("SwingLargeIconKey", new ImageIcon(getClass().getResource("/at/ac/htlhl/nucleij/resources/images/i32x32/document-new.png")));

        //---- enableAnalyzerViewAction ----
        enableAnalyzerViewAction.putValue(Action.NAME, bundle.getString("MainFrame.enableAnalyzerViewAction.Name"));
        enableAnalyzerViewAction.putValue(Action.SMALL_ICON, new ImageIcon("C:\\DA-Workspace\\NucleiJ\\src\\main\\resources\\at\\ac\\htlhl\\nucleij\\resources\\images\\i16x16\\analyzer.png"));
        enableAnalyzerViewAction.putValue("SwingLargeIconKey", new ImageIcon("C:\\DA-Workspace\\NucleiJ\\src\\main\\resources\\at\\ac\\htlhl\\nucleij\\resources\\images\\i32x32\\analyzer.png"));

        //---- enableConverterViewAction ----
        enableConverterViewAction.putValue(Action.NAME, bundle.getString("MainFrame.enableConverterViewAction.Name"));
        enableConverterViewAction.putValue(Action.SMALL_ICON, new ImageIcon("C:\\DA-Workspace\\NucleiJ\\src\\main\\resources\\at\\ac\\htlhl\\nucleij\\resources\\images\\i16x16\\converter.png"));
        enableConverterViewAction.putValue("SwingLargeIconKey", new ImageIcon("C:\\DA-Workspace\\NucleiJ\\src\\main\\resources\\at\\ac\\htlhl\\nucleij\\resources\\images\\i32x32\\converter.png"));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
