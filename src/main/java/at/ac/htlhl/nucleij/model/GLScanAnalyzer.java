package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;

import static at.ac.htlhl.nucleij.model.GLScanAnalyzer.Type.MULTI;

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

    // XXX Ist heatmapquality nicht eigentlich Object? Jetzt mit String geloest
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
    enum Type { MULTI, SINGLE }

    private Type type;
    private String inputpath;
    private String outputpath;
    private int heatmapquality;
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

    public int getHeatmapquality()
    {
        return heatmapquality;
    }

    public void setHeatmapquality(int heatmapquality)
    {
        int oldValue = this.heatmapquality;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Type oldValue = this.type;
        this.type = type;
        firePropertyChange(PROPERTY_TYPE, oldValue, type);
    }

    // endregion
}
