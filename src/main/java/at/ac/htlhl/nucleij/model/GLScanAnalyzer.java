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

    public static final String PROPERTY_CALCULATEANDSHOWHEATMAP = "calculateandshowheatmap";
    public static final String PROPERTY_HEATMAPQUALITY = "heatmapquality";
    public static final String PROPERTY_ROIAREA = "roiarea";
    public static final String PROPERTY_SETROI = "setroi";

    // endregion

    // region Fields
    // ************************************************************************


    //eigene Werte
    private NdpiConverter ndpiConverter;
    private int heatmapquality;
    private boolean calculateandshowheatmap;
    private String roiarea;
    private boolean setroi;

    private String absolutePathofNdpiJar;

    //Listen fuer Pfade der zu verarbeitenden arbeiten
    private List<String> ndpiList;
    private List<String> tifList;

    //ROI Informationen:
    private int roiX;
    private int roiY;
    private int roiWidth;
    private int roiHeight;

    // endregion

    public GLScanAnalyzer(NdpiConverter ndpiConverter)
    {
        super();

        this.ndpiConverter = ndpiConverter;

        //this.selectroi = false;

        this.heatmapquality = 60;
        this.calculateandshowheatmap = false;
        this.setroi = false;

        this.roiX = 0;
        this.roiY = 0;
        this.roiWidth = 0;
        this.roiHeight = 0;
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

    public boolean isSetroi() {
        if(roiX + roiY + roiHeight +roiWidth < 4)
        {
            return false;
        }
        else
        {
            return setroi;
        }
    }

    public void setSetroi(boolean setroi) {
        boolean oldValue = this.setroi;
        this.setroi = setroi;
        firePropertyChange(PROPERTY_SETROI, oldValue, setroi);
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

    public int getRoiX() {
        return roiX;
    }

    public void setRoiX(int roiX) {
        this.roiX = roiX;
    }

    public int getRoiY() {
        return roiY;
    }

    public void setRoiY(int roiY) {
        this.roiY = roiY;
    }

    public int getRoiWidth() {
        return roiWidth;
    }

    public void setRoiWidth(int roiWidth) {
        this.roiWidth = roiWidth;
    }

    public int getRoiHeight() {
        return roiHeight;
    }

    public void setRoiHeight(int roiHeight) {
        this.roiHeight = roiHeight;
    }

    public String getAbsolutePathofNdpiJar() {
        return absolutePathofNdpiJar;
    }

    public void setAbsolutePathofNdpiJar(String absolutePathofNdpiJar) {
        this.absolutePathofNdpiJar = absolutePathofNdpiJar;
    }

    // endregion
}
