package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;


/**
 * Created by Stefan on 11.11.2016.
 *
 * @author Stefan Erben & Andreas Mattes
 * @version 1.0
 */
public class Main extends Model
{
    // region Constants
    // ************************************************************************
    // Properties
    public static final String PROPERTY_GLSCANANALYZER = "GLScanAnalyzer";
    public static final String PROPERTY_NDPICONVERTER = "NdpiConverter";

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;

    public Main()
    {
        super();

        glScanAnalyzer = new GLScanAnalyzer();
        ndpiConverter = new NdpiConverter();
    }

    // Getters
    public GLScanAnalyzer getGLScanAnalyzer()
    {
        return glScanAnalyzer;
    }

    public NdpiConverter getNdpiConverter()
    {
        return ndpiConverter;
    }

}
