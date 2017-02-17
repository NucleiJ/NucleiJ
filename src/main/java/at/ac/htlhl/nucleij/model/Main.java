package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;


/**
 * Created by Stefan on 11.11.2016.
 *
 * @author Stefan Erben & Andreas Mattes
 * @version 1.0
 */
public class Main extends Model {
    // region Constants
    // ************************************************************************
    // Properties

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter  ndpiConverter;

    public Main() {
        super();

        ndpiConverter = new NdpiConverter();
        glScanAnalyzer = new GLScanAnalyzer(ndpiConverter);
    }

    // Getters
    public GLScanAnalyzer getGLScanAnalyzer() {
        return glScanAnalyzer;
    }

    public NdpiConverter getNdpiConverter() {
        return ndpiConverter;
    }

    public void setGlScanAnalyzer(GLScanAnalyzer glScanAnalyzer) {
        this.glScanAnalyzer = glScanAnalyzer;
    }

    public void setNdpiConverter(NdpiConverter ndpiConverter) {
        this.ndpiConverter = ndpiConverter;
    }
}
