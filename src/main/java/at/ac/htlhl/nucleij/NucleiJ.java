package at.ac.htlhl.nucleij;

import at.ac.htlhl.nucleij.model.Main;
import at.ac.htlhl.nucleij.presenter.MainPM;
import at.ac.htlhl.nucleij.util.logging.ConsoleHandler;
import at.ac.htlhl.nucleij.util.logging.ConsoleOutFormatter;
import at.ac.htlhl.nucleij.view.CombinedView;
import at.ac.htlhl.nucleij.view.GLScanAnalyzerView;
import at.ac.htlhl.nucleij.view.MainFrameView;
import at.ac.htlhl.nucleij.view.NdpiConverterView;
import com.apple.eawt.Application;
import com.ezware.dialog.task.TaskDialogs;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceManager;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Stefan on 11.11.2016.
 */
public class NucleiJ extends SingleFrameApplication {
    // Constants
    // ************************************************************************
    public static final  NucleiJ nucleiJ = new NucleiJ();
    private static final Logger  LOGGER  = Logger.getLogger(NucleiJ.class.getName());

    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");


    // Fields
    private ResourceMap resource;

    public static void main(String... args) {
        org.jdesktop.application.Application.launch(NucleiJ.class, args);
    }

    @Override
    protected void initialize(String[] args) {
        // Force application to use given language setting instead of using OS default
        //Locale.setDefault(Locale.ENGLISH);

        // Load NucleiJ.properties file
        ApplicationContext ctxt = getContext();
        ResourceManager mgr = ctxt.getResourceManager();
        resource = mgr.getResourceMap(NucleiJ.class);

        configureLoggingSystem();
        initLookAndFeel();
    }

    @Override
    protected void startup() {
        LOGGER.log(Level.INFO, "Startup application '" + resource.getString("Application.id") + "'");

        Main main = new Main();
        MainPM mainPM = new MainPM(main);

        GLScanAnalyzerView glScanAnalyzerView = new GLScanAnalyzerView(mainPM.getGLScanAnalyzerPM());
        NdpiConverterView ndpiConverterView = new NdpiConverterView(mainPM.getNdpiConverterPM());

        MainFrameView mainFrame = new MainFrameView(mainPM);

        setMainFrame(mainFrame);
        getMainFrame().setTitle(resource.getString("Application.id"));

        CombinedView combinedView = new CombinedView(ndpiConverterView, glScanAnalyzerView);
        show(combinedView);

        addExitListener(new NucleiJExitListener());
    }


    // endregion
    // Private helper methods
    // ************************************************************************

    @Override
    protected void shutdown() {
        super.shutdown();
    }

    private void configureLoggingSystem() {
        Logger logger = Logger.getLogger("");

        // Remove default logging handlers
        for (int i = (logger.getHandlers().length - 1); i >= 0; i--) {
            logger.removeHandler(logger.getHandlers()[i]);
        }

        // Configure Logging for console output:
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ConsoleOutFormatter());
        logger.addHandler(consoleHandler);
    }

    private void initLookAndFeel() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // MAC ICON
                Application application = Application.getApplication();
                Image image = Toolkit.getDefaultToolkit().getImage(
                        "at/ac/htlhl/nucleij/resources/images/icon_png_v1.png");
                application.setDockIconImage(image);
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Look and Feel konnte nicht initialisiert werden " + ex.getMessage(), ex);
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        //setUIFont (new javax.swing.plaf.FontUIResource("Roboto", Font.PLAIN,12));

    }

    // region Nested classes
    // ************************************************************************

    private class NucleiJExitListener implements ExitListener {
        public boolean canExit(EventObject e) {
            JFrame parent = ((SingleFrameApplication) getContext().getApplication()).getMainFrame();

            // Confirmation Dialog
            if (TaskDialogs.ask(parent, bundle.getString("QuitNucleiJ.text"), bundle.getString("QuitNucleiJ.text2"))) {
                return true;
            } else {
                return false;
            }
        }

        public void willExit(EventObject event) {

        }
    }

    // endregion
}
