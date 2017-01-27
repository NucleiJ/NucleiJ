package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;

/**
 * Created by andreas on 25.01.2017.
 */
public class Settings extends Model
{
    public static final String PROPERTY_SETTINGS_OUTPUTNAME        = "outputname";
    public static final String PROPERTY_SETTINGS_STANDARD_FOLDER   = "standardfolder";
    public static final String PROPERTY_SETTINGS_CONVERTNAME       = "convertname";
    public static final String PROPERTY_SETTINGS_LANGUAGE          = "language";

    public static final String PROPERTY_SETTINGS_NUMBER            = "number";
    public static final String PROPERTY_SETTINGS_AREA              = "area";
    // Mattes
    //**********************************
    // Erben
    public static final String PROPERTY_SETTINGS_PERIMETER         = "perimeter";
    public static final String PROPERTY_SETTINGS_ROUNDNESS         = "roundness";
    public static final String PROPERTY_SETTINGS_WIDTHHEIGHT       = "widthheight";
    public static final String PROPERTY_SETTINGS_XYCOORDINATES     = "xycoordinates";
    public static final String PROPERTY_SETTINGS_CIRCULARITY       = "circularity";
    public static final String PROPERTY_SETTINGS_SOLIDITY          = "solidity";
    public static final String PROPERTY_SETTINGS_SUMMARYTYPE       = "summarytype";

    public static final String TYPE_SETTINGS_CSV  = "CSV";
    public static final String TYPE_SETTINGS_CSVTXT   = "CSV & TXT";
    public static final String TYPE_SETTINGS_TXT   = "TXT";
    public static final String[] TYPE_SETTINGS_CHOICES = {TYPE_SETTINGS_CSV, TYPE_SETTINGS_CSVTXT, TYPE_SETTINGS_TXT};


    // private Vars #####################################################################################
    private String outputName;
    private String standardFolder;
    private String convertName;
    private String language;

    private Boolean number;
    private Boolean area;


    // Mattes
    // ****************************************************************************************
    //Erben


    private boolean perimeter;
    private boolean roundness;
    private boolean widthheight;
    private boolean xycoordinates;
    private boolean circularity;
    private boolean solidity;
    private String summarytype;

    public Settings()
    {
        super();

        this.outputName     = "Output";
        this.standardFolder = null;
        this.convertName    = "_x10.OME";
        this.language       = "EN";

        this.number         = true;
        this.area           = true;
        this.circularity    = false;
        this.perimeter      = true;
        this.roundness      = true;
        this.solidity       = false;
        this.widthheight    = true;
        this.xycoordinates  = false;
        this.summarytype    = "CSV & TXT";
    }



    // getter setter ###################################################################################
    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        String oldValue = this.outputName;
        this.outputName = outputName;
        firePropertyChange(PROPERTY_SETTINGS_OUTPUTNAME,oldValue,outputName);
    }

    public String getStandardFolder() {
        return standardFolder;
    }

    public void setStandardFolder(String standardFolder) {
        String oldValue = this.standardFolder;
        this.standardFolder = standardFolder;
        firePropertyChange(PROPERTY_SETTINGS_STANDARD_FOLDER,oldValue,standardFolder);
    }

    public String getConvertName() {
        return convertName;
    }

    public void setConvertName(String convertName) {
        String oldValue = this.convertName;
        this.convertName = convertName;
        firePropertyChange(PROPERTY_SETTINGS_CONVERTNAME,oldValue,convertName);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        String oldValue = this.language;
        this.language = language;
        firePropertyChange(PROPERTY_SETTINGS_LANGUAGE,oldValue,language);
    }

    public Boolean getNumber() {
        return number;
    }

    public void setNumber(Boolean number) {
        Boolean oldValue = this.number;
        this.number = number;
        firePropertyChange(PROPERTY_SETTINGS_NUMBER,oldValue,number);
    }

    public Boolean getArea() {
        return area;
    }

    public void setArea(Boolean area) {
        Boolean oldValue = this.area;
        this.area = area;
        firePropertyChange(PROPERTY_SETTINGS_AREA,oldValue,area);
    }

    // Mattes
    // ****************************************************************************************
    //Erben


    public boolean isPerimeter() {
        return perimeter;
    }

    public void setPerimeter(boolean perimeter) {
        boolean oldValue = this.perimeter;
        this.perimeter = perimeter;
        firePropertyChange(PROPERTY_SETTINGS_PERIMETER,oldValue,perimeter);
    }

    public boolean isRoundness() {
        return roundness;
    }

    public void setRoundness(boolean roundness) {
        boolean oldValue = this.roundness;
        this.roundness = roundness;
        firePropertyChange(PROPERTY_SETTINGS_ROUNDNESS,oldValue,roundness);
    }

    public boolean isWidthheight() {
        return widthheight;
    }

    public void setWidthheight(boolean widthheight) {
        boolean oldValue = this.widthheight;
        this.widthheight = widthheight;
        firePropertyChange(PROPERTY_SETTINGS_WIDTHHEIGHT,oldValue,widthheight);
    }

    public boolean isXycoordinates() {
        return xycoordinates;
    }

    public void setXycoordinates(boolean xycoordinates) {
        boolean oldValue = this.xycoordinates;
        this.xycoordinates = xycoordinates;
        firePropertyChange(PROPERTY_SETTINGS_XYCOORDINATES,oldValue,xycoordinates);

    }

    public boolean isCircularity() {
        return circularity;
    }

    public void setCircularity(boolean circularity) {
        boolean oldValue = this.circularity;
        this.circularity = circularity;
        firePropertyChange(PROPERTY_SETTINGS_CIRCULARITY,oldValue,circularity);

    }

    public boolean isSolidity() {
        return solidity;
    }

    public void setSolidity(boolean solidity) {
        this.solidity = solidity;
        boolean oldValue = this.widthheight;
        this.widthheight = widthheight;
        firePropertyChange(PROPERTY_SETTINGS_WIDTHHEIGHT,oldValue,widthheight);

    }

    public String getSummarytype() {
        return summarytype;
    }

    public void setSummarytype(String summarytype) {
        String oldValue = this.summarytype;
        this.summarytype = summarytype;
        firePropertyChange(PROPERTY_SETTINGS_SUMMARYTYPE,oldValue,summarytype);

    }
}
