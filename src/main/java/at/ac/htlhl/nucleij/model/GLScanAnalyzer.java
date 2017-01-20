package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.ColumnSpec;

import java.util.List;

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

    /*public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_INPUTPATH = "inputpath";
    public static final String PROPERTY_OUTPUTPATH = "outputpath";
    public static final String PROPERTY_SELECTROI = "selectroi";*/

    public static final String PROPERTY_CALCULATEANDSHOWHEATMAP = "calculateandshowheatmap";
    public static final String PROPERTY_HEATMAPQUALITY = "heatmapquality";
    public static final String PROPERTY_ROIAREA = "roiarea";

    // endregion

    /*public static final String SINGLE   = "Single";
    public static final String MULTI    = "Multi";
    public static final String[] STRING_CHOICES = { SINGLE, MULTI };*/

    // region Fields
    // ************************************************************************

    /*
    private boolean selectroi;*/

    //eigene Werte
    private NdpiConverter ndpiConverter;
    private int heatmapquality;
    private boolean calculateandshowheatmap;
    private String roiarea;

    //von Andi uebernommen
    private String inputpath;
    private String outputpath;
    private String type;

    //Listen fuer Pfade der zu verarbeitenden arbeiten
    private List<String> ndpiList;
    private List<String> tifList;
    // endregion


    public GLScanAnalyzer(NdpiConverter ndpiConverter)
    {
        super();

        this.ndpiConverter = ndpiConverter;

        //this.selectroi = false;

        this.heatmapquality = 60;
        this.calculateandshowheatmap = false;

        this.inputpath = "";
        this.outputpath = "";
        this.type = ndpiConverter.getType();
    }

    public List<String> getNdpiList() {
        return ndpiList;
    }

    public void setNdpiList(List<String> ndpiList) {
        this.ndpiList = ndpiList;
    }

    public void addNdpiToList(String path)
    {
        ndpiList.add(path);
    }

    public List<String> getTifList() {
        return tifList;
    }

    public void setTifList(List<String> tifList) {
        this.tifList = tifList;
    }

    public void addTifToList(String path)
    {
        tifList.add(path);
    }

    // region Getter and Setter
    // ************************************************************************
    public int getHeatmapquality()
    {
        return heatmapquality;
    }

    public void setHeatmapquality(int heatmapquality)
    {
        if (heatmapquality>100) {
            heatmapquality=100;
        } else if (heatmapquality <1) {
            heatmapquality=1;
        }
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

    public String getRoiarea() {
        return roiarea;
    }

    public void setRoiarea(String roiarea) {
        String oldValue = this.roiarea;
        this.roiarea = roiarea;
        firePropertyChange(PROPERTY_ROIAREA, oldValue, roiarea);
    }


    public String getInputpath()
    {
        return ndpiConverter.getInputpath();
    }

    public String getOutputpath()
    {
        return ndpiConverter.getOutputpath();
    }


    public String getType() {
        return ndpiConverter.getType();
    }


    /*
    public boolean isSelectroi()
    {
        return selectroi;
    }

    public void setSelectroi(boolean selectroi) {
        boolean oldValue = this.selectroi;
        this.selectroi = selectroi;
        firePropertyChange(PROPERTY_SELECTROI, oldValue, selectroi);
    }



    */

    // endregion
}
