package at.ac.htlhl.nucleij;

import at.ac.htlhl.nucleij.model.Main;
import at.ac.htlhl.nucleij.presenter.MainPM;
import at.ac.htlhl.nucleij.util.logging.ConsoleHandler;
import at.ac.htlhl.nucleij.util.logging.ConsoleOutFormatter;
import at.ac.htlhl.nucleij.view.CombinedView;
import at.ac.htlhl.nucleij.view.GLScanAnalyzerView;
import at.ac.htlhl.nucleij.view.MainFrameView;
import at.ac.htlhl.nucleij.view.NdpiConverterView;
import com.ezware.dialog.task.TaskDialogs;
import org.jdesktop.application.*;

import javax.swing.*;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Stefan on 11.11.2016.
 */
public class NucleiJ extends SingleFrameApplication
{
    // Constants
    // ************************************************************************
    public static final NucleiJ nucleiJ = new NucleiJ();
    private static final Logger LOGGER = Logger.getLogger(NucleiJ.class.getName());

    // Fields
    private ResourceMap resource;

    // region Public API
    // ************************************************************************



    @Override
    protected void initialize(String[] args)
    {
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
    protected void startup()
    {
        LOGGER.log(Level.INFO, "Startup application '" + resource.getString("Application.id") + "'");

        Main main = new Main();
        MainPM mainPM = new MainPM(main);


        GLScanAnalyzerView glScanAnalyzerView = new GLScanAnalyzerView(mainPM.getGLScanAnalyzerPM(), main.getGLScanAnalyzer());
        NdpiConverterView ndpiConverterView = new NdpiConverterView(mainPM.getNdpiConverterPM());

        MainFrameView mainFrame = new MainFrameView(mainPM);

        // die Views ins PM Model "senden"
        mainPM.setGlScanAnalyzerView(glScanAnalyzerView);
        mainPM.setNdpiConverterView(ndpiConverterView);

        setMainFrame(mainFrame);
        getMainFrame().setTitle(resource.getString("Application.id"));

        CombinedView combinedView = new CombinedView(ndpiConverterView, glScanAnalyzerView);
        show(combinedView);

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
        //NIMBUS
        try {
            //PlasticLookAndFeel.setPlasticTheme(new SkyBluer());
            //UIManager.setLookAndFeel(new PlasticXPLookAndFeel());

            //Nimbus Design
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch(UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.SEVERE, "Look and Feel could not be initialized " + ex.getMessage(), ex);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //UIManager.put("Synthetica.font.scaleFactor", 110);
        //UIManager.put("Synthetica.font.scaleFactor", 110);
        //UIManager.put("Synthetica.slider.hoverAndPressed.enabled", true);
        //UIManager.put("Synthetica.slider.track.animation.delay", 500);
        /*
        try
        {
            UIManager.setLookAndFeel(new SyntheticaBlueIceLookAndFeel());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */

    }


    public static void main(String ... args)
    {
        Application.launch(NucleiJ.class, args);
    }

    public void changeView(JComponent ndpiConverterView) {

        show(ndpiConverterView);
    }


    // region Nested classes
    // ************************************************************************

    private class CarConfiguratorExitListener implements ExitListener
    {
        public boolean canExit(EventObject e)
        {
            JFrame parent = ((SingleFrameApplication) getContext().getApplication()).getMainFrame();

            // Confirmation Dialog
            if(TaskDialogs.isConfirmed( parent, "Wollen Sie wirklich beenden?", "Falls Vorgänge noch nicht abgeschlossen sind,\nkönnen diese fehlerhaft beendet werden! ..."))
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
