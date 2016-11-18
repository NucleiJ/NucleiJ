package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.ColumnSpec;

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
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_INPUTPATH = "inputpath";
    public static final String PROPERTY_OUTPUTPATH = "outputpath";
    public static final String PROPERTY_CALCULATEANDSHOWHEATMAP = "calculateandshowheatmap";
    public static final String PROPERTY_HEATMAPQUALITY = "heatmapquality";
    public static final String PROPERTY_SELECTROI = "selectroi";
    public static final String PROPERTY_FLOAT_VALUE    = "floatValue";

    // endregion

    // TODO Erweiterung von JGoodies Beispiel fuer ObjectChoice
    public static final String SINGLE   = "Single";
    public static final String MULTI    = "Multi";
    public static final String[] STRING_CHOICES = { SINGLE, MULTI };

    // region Fields
    // ************************************************************************

    private String type;
    private String inputpath;
    private String outputpath;
    private float heatmapquality;
    private boolean calculateandshowheatmap;
    private boolean selectroi;
    // endregion


    public GLScanAnalyzer()
    {
        super();

        this.type = MULTI;
        this.inputpath = "";
        this.outputpath = "";
        this.heatmapquality = 60;
        this.calculateandshowheatmap = false;
        this.selectroi = false;
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

    public float getHeatmapquality()
    {
        return heatmapquality;
    }

    public void setHeatmapquality(float heatmapquality)
    {
        float oldValue = this.heatmapquality;
        this.heatmapquality = heatmapquality;
        firePropertyChange(PROPERTY_HEATMAPQUALITY, oldValue, heatmapquality);
    }

    public boolean isCalculateandshowheatmap()
    {
        return calculateandshowheatmap;
    }

    public void setCalculateandshowheatmap(boolean calculateandshowheatmap) {
        boolean oldValue = this.calculateandshowheatmap;
        this.calculateandshowheatmap = calculateandshowheatmap;
        firePropertyChange(PROPERTY_CALCULATEANDSHOWHEATMAP, oldValue, calculateandshowheatmap);
    }

    public boolean isSelectroi()
    {
        return selectroi;
    }

    public void setSelectroi(boolean selectroi) {
        boolean oldValue = this.selectroi;
        this.selectroi = selectroi;
        firePropertyChange(PROPERTY_SELECTROI, oldValue, selectroi);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String oldValue = this.type;
        this.type = type;
        firePropertyChange(PROPERTY_TYPE, oldValue, type);
    }

    // endregion
}
