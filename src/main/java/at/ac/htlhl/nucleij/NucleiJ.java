package at.ac.htlhl.nucleij;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.model.Main;
import at.ac.htlhl.nucleij.presenter.MainPM;
import at.ac.htlhl.nucleij.util.logging.ConsoleHandler;
import at.ac.htlhl.nucleij.util.logging.ConsoleOutFormatter;
import at.ac.htlhl.nucleij.view.GLScanAnalyzerView;
import at.ac.htlhl.nucleij.view.MainFrame;
import at.ac.htlhl.nucleij.view.NdpiConverterView;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.SkyBluer;
import org.jdesktop.application.*;

import javax.swing.*;
import java.util.EventObject;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Stefan on 11.11.2016.
 */
public class NucleiJ extends SingleFrameApplication
{
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(NucleiJ.class.getName());


    // Fields
    private ResourceMap resource;

    // region Public API
    // ************************************************************************



    @Override
    protected void initialize(String[] args)
    {
        // Force application to use given language setting instead of using OS default
        Locale.setDefault(Locale.ENGLISH);

        // Load CarConfiguration.properties file
        ApplicationContext ctxt = getContext();
        ResourceManager mgr = ctxt.getResourceManager();
        resource = mgr.getResourceMap(NucleiJ.class);

        configureLoggingSystem();

        initLookAndFeel();
    }

    @Override
    protected void startup()
    {
        LOGGER.log(Level.INFO, "Startup application '" + resource.getString("Application.id") + "'");

        Main main = new Main();

        MainPM mainPM = new MainPM(main);
        MainFrame mainFrame = new MainFrame(mainPM);

        GLScanAnalyzerView glScanAnalyzerView = new GLScanAnalyzerView(mainPM.getGLScanAnalyzerPM());
        NdpiConverterView ndpiConverter = new NdpiConverterView(mainPM.getNdpiConverterPM());

        setMainFrame(mainFrame);
        getMainFrame().setTitle(resource.getString("Application.id"));

        show(glScanAnalyzerView);

        // getMainFrame().setResizable(false);
        addExitListener(new CarConfiguratorExitListener());

    }


    @Override
    protected void shutdown()
    {
        super.shutdown();
    }


    // endregion

    // Private helper methods
    // ************************************************************************

    private void configureLoggingSystem()
    {
        Logger logger = Logger.getLogger("");

        // Remove default logging handlers
        for (int i = (logger.getHandlers().length - 1) ; i >= 0; i--) {
            logger.removeHandler(logger.getHandlers()[i]);
        }

        // Configure Logging for console output:
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ConsoleOutFormatter());
        logger.addHandler(consoleHandler);
    }

    private void initLookAndFeel()
    {
        try {
            PlasticLookAndFeel.setPlasticTheme(new SkyBluer());
            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        }
        catch(UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Look and Feel could not be initialized " + ex.getMessage(), ex);
        }
    }


    public static void main(String ... args)
    {
        Application.launch(NucleiJ.class, args);
    }


    // region Nested classes
    // ************************************************************************

    private class CarConfiguratorExitListener implements ExitListener
    {
        public boolean canExit(EventObject e)
        {
            JFrame parent = ((SingleFrameApplication) getContext().getApplication()).getMainFrame();

            // Confirmation Dialog
            if(TaskDialogs.isConfirmed( parent, "Are you sure you want to quit?", "There are some changed data sets ..."))
            {
                return true;
            }
            else
            {
                return false;
            }

            // LOGGER.info("Result: " + taskDialog.getResult());

        }

        public void willExit(EventObject event)
        {

        }
    }

    // endregion
}
