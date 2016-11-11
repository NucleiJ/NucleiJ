package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.Main;
import com.jgoodies.binding.PresentationModel;

import java.util.logging.Logger;

/**
 * Created by Stefan on 11.11.2016.
 */
public class MainPM extends PresentationModel<Main>
{
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(MainPM.class.getName());

    public static final String FILE_EXTENSION			= "nucleij";

    public static final String PROPERTY_MAIN			= "main";

    // Fields
    // ************************************************************************


    // References to sub presentation models
    private GLScanAnalyzerPM glScanAnalyzerPM;
    private NdpiConverterPM ndpiConverterPM;



    public MainPM(Main main)
    {
        super(main);

        glScanAnalyzerPM = new GLScanAnalyzerPM(main.getGLScanAnalyzer());
        ndpiConverterPM = new NdpiConverterPM(main.getNdpiConverter());

    }

    public GLScanAnalyzerPM getGLScanAnalyzerPM()
    {
        return glScanAnalyzerPM;
    }

    public NdpiConverterPM getNdpiConverterPM()
    {
        return ndpiConverterPM;
    }

    // region Nested classes
    // ************************************************************************


}
