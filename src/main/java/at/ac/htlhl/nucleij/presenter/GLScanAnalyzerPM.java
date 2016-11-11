package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import com.jgoodies.binding.PresentationModel;

/**
 * Created by Stefan on 11.11.2016.
 */
public class GLScanAnalyzerPM extends PresentationModel<GLScanAnalyzer>
{
    private GLScanAnalyzerPM glScanAnalyzerPM;

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer)
    {
        super(glScanAnalyzer);
    }

    public GLScanAnalyzerPM getGLScanAnalyzerPM()
    {
        return glScanAnalyzerPM;
    }
}
