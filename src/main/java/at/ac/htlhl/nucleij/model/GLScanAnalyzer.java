package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;

/**
 *
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzer extends Model
{
    // region Constants
    // ************************************************************************
    // Properties
    public static final String PROPERTY_INPUTPATH = "inputpath";
    public static final String PROPERTY_OUTPUTPATH = "outputpath";
    public static final String PROPERTY_MAGNIFICATION = "magnification";
    public static final String PROPERTY_GENERATEHEATMAP = "generateheatmap";

    // XXX Ist magnification nicht eigentlich Object? Jetzt mit String geloest
    // endregion


    /*
    // TODO Erweiterung von JGoodies Beispiel fuer ObjectChoice
    // An int based enumeration, Zeile 38..
    public static final Integer X10   = 10;
    public static final Integer X40 = 40;
    static final Integer[] INTEGER_CHOICES =
            { X10, X40 };
    */

    // region Fields
    // ************************************************************************
    private String inputpath;
    private String outputpath;
    private String magnification;
    private boolean generateheatmap;
    // endregion


    public GLScanAnalyzer()
    {
        super();

        this.inputpath = "";
        this.outputpath = "";
        this.magnification = "x10";
        this.generateheatmap = false;
    }

    // region Getter and Setter
    // ************************************************************************
    public String getInputpath()
    {
        return inputpath;
    }

    public void setInputpath(String inputpath)
    {
        String oldValue = this.inputpath;
        this.inputpath = inputpath;
        firePropertyChange(PROPERTY_INPUTPATH, oldValue, inputpath);
    }

    public String getOutputpath()
    {
        return outputpath;
    }

    public void setOutputpath(String outputpath)
    {
        String oldValue = this.outputpath;
        this.outputpath = outputpath;
        firePropertyChange(PROPERTY_OUTPUTPATH, oldValue, outputpath);
    }

    public Object getMagnification()
    {
        return magnification;
    }

    public void setMagnification(String magnification)
    {
        String oldValue = this.magnification;
        this.magnification = magnification;
        firePropertyChange(PROPERTY_MAGNIFICATION, oldValue, magnification);
    }

    public boolean isGenerateheatmap()
    {
        return generateheatmap;
    }

    public void setGenerateheatmap(boolean generateheatmap) {
        boolean oldValue = this.generateheatmap;
        this.generateheatmap = generateheatmap;
        firePropertyChange(PROPERTY_GENERATEHEATMAP, oldValue, generateheatmap);
    }

    // endregion
}
