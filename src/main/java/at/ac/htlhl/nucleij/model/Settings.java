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

    public Settings()
    {
        super();


    }

}
