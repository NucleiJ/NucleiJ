package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import com.jgoodies.binding.PresentationModel;

/**
 *
 * @author Stefan Erben
 * @version 1.0
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
