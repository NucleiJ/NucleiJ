package at.ac.htlhl.nucleij.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jgoodies.binding.beans.Model;

import java.util.List;

/**
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzer extends Model {

    // region Constants
    // ************************************************************************

    // Properties
    public static final String PROPERTY_CALCULATEANDSHOWHEATMAP = "calculateandshowheatmap";
    public static final String PROPERTY_HEATMAPQUALITY          = "heatmapquality";
    public static final String PROPERTY_ROIAREA                 = "roiarea";
    public static final String PROPERTY_SELECTROI               = "selectroi";

    // endregion

    // region Klasseninterne Objekte
    // ************************************************************************

    // Klasseninterne Objekte:
    private NdpiConverter ndpiConverter;
    private int           heatmapquality;
    private boolean       calculateandshowheatmap;
    private String        roiarea;
    private boolean       selectroi;

    // Listen fuer Pfade der zu verarbeitenden Scans
    private List<String> ndpiList;
    private List<String> tifList;
    private String       inputpath;
    private String       outputpath;

    // ROI Informationen:
    private int roiX;
    private int roiY;
    private int roiWidth;
    private int roiHeight;
    // endregion

    public GLScanAnalyzer() {
        this(null);
    }

    public GLScanAnalyzer(NdpiConverter ndpiConverter) {
        super();

        this.ndpiConverter = ndpiConverter;

        // Initialisierungswerte setzen
        this.heatmapquality = 60;
        this.calculateandshowheatmap = false;
        this.selectroi = false;

        this.roiX = 0;
        this.roiY = 0;
        this.roiWidth = 0;
        this.roiHeight = 0;
    }

    // region Getter and Setter
    // ************************************************************************
    public int getHeatmapquality() {
        return heatmapquality;
    }

    public void setHeatmapquality(int heatmapquality) {
        if (heatmapquality > 100) {         // Falls User mehr als 100 eingibt
            heatmapquality = 100;           // wird Wert auf 100 gesetzt
        } else if (heatmapquality < 1) {    // bei einem zu kleinen Wert
            heatmapquality = 1;             // wird Slider auf 1 gesetzt
        }
        int oldValue = this.heatmapquality;
        this.heatmapquality = heatmapquality;
        firePropertyChange(PROPERTY_HEATMAPQUALITY, oldValue, heatmapquality);
    }

    public boolean isSelectroi() {
        if (roiX + roiY + roiHeight + roiWidth < 4) {   //falsche Werte vorhanden?
            return false;
        } else {
            return selectroi;
        }
    }

    public void setSelectroi(boolean selectroi) {
        boolean oldValue = this.selectroi;
        this.selectroi = selectroi;
        firePropertyChange(PROPERTY_SELECTROI, oldValue, selectroi);
    }

    public boolean isCalculateandshowheatmap() {
        return calculateandshowheatmap;
    }

    public void setCalculateandshowheatmap(boolean calculateandshowheatmap) {
        boolean oldValue = this.calculateandshowheatmap;
        this.calculateandshowheatmap = calculateandshowheatmap;
        firePropertyChange(PROPERTY_CALCULATEANDSHOWHEATMAP, oldValue, calculateandshowheatmap);
    }

    @JsonIgnore
    public String getRoiarea() {
        return roiarea;
    }

    public void setRoiarea(String roiarea) {
        String oldValue = this.roiarea;
        this.roiarea = roiarea;
        firePropertyChange(PROPERTY_ROIAREA, oldValue, roiarea);
    }

    @JsonIgnore
    public List<String> getNdpiList() {
        return ndpiList;
    }

    public void setNdpiList(List<String> ndpiList) {
        this.ndpiList = ndpiList;
    }

    @JsonIgnore
    public List<String> getTifList() {
        return tifList;
    }

    public void setTifList(List<String> tifList) {
        this.tifList = tifList;
    }

    public void addTifToList(String path) {
        tifList.add(path);
    }

    @JsonIgnore
    public String getInputpath() {
        return ndpiConverter.getInputpath();
    }

    public void setInputpath(String inputpath) {
        this.inputpath = inputpath;
    }

    @JsonIgnore
    public String getOutputpath() {
        return ndpiConverter.getOutputpath();
    }

    public void setOutputpath(String outputpath) {
        this.outputpath = outputpath;
    }

    @JsonIgnore
    public int getRoiX() {
        return roiX;
    }

    @JsonIgnore
    public void setRoiX(int roiX) {
        this.roiX = roiX;
    }

    @JsonIgnore
    public int getRoiY() {
        return roiY;
    }

    public void setRoiY(int roiY) {
        this.roiY = roiY;
    }

    @JsonIgnore
    public int getRoiWidth() {
        return roiWidth;
    }

    public void setRoiWidth(int roiWidth) {
        this.roiWidth = roiWidth;
    }

    @JsonIgnore
    public int getRoiHeight() {
        return roiHeight;
    }

    public void setRoiHeight(int roiHeight) {
        this.roiHeight = roiHeight;

    }

    // endregion
}
