package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.ColumnSpec;

import java.io.Serializable;

/**
 *
 * @author Andreas Mattes
 * @version 1.0
 */
public class NdpiConverter extends Model
{
    //region Constants
    public static final String PROPERTY_TYPE            = "type";
    public static final String PROPERTY_INPUTPATH       = "inputpath";
    public static final String PROPERTY_OUTPUTPATH      = "outputpath";
    public static final String PROPERTY_MAGNIFICATION   = "magnification";

    public static final String SINGLE_FILE  = "Single File";
    public static final String SINGLE_DIR   = "Single Directory";
    public static final String MULTI_FILE   = "Multi File";
    public static final String AUTO_MODE  = "Auto File";
    public static final String[] TYPE_CHOICES = {SINGLE_FILE, SINGLE_DIR, MULTI_FILE, AUTO_MODE};

    public static final String MAG_X5   = "x5";
    public static final String MAG_X10  = "x10";
    public static final String MAG_X40  = "x40";
    public static final String[] MAG_CHOICES = {MAG_X5, MAG_X10, MAG_X40};

    //endregion Constants

    //*******************************************************************
    private String type;
    private String magnification;
    private String inputpath;
    private String outputpath;
    private String custompath;

    public NdpiConverter()
    {
        super();

        this.type = AUTO_MODE;
        this.inputpath = "";
        this.outputpath = "";
        this.custompath = "";
        this.magnification = "x10";
    }

    //region Getter&Setter

    public String getType() {
        return type;
    }

    public void setType(String type) {
        String oldValue = this.type;
        this.type = type;
        firePropertyChange(PROPERTY_TYPE,oldValue,type);
    }

    public String getMagnification() {
        return magnification;
    }

    public void setMagnification(String magnification) {
        String oldValue = this.magnification;
        this.magnification = magnification;
        firePropertyChange(PROPERTY_MAGNIFICATION,oldValue,magnification);
    }

    public String getInputpath() {
        return inputpath;
    }

    public void setInputpath(String inputpath) {
        String oldValue = this.inputpath;
        this.inputpath = inputpath;
        firePropertyChange(PROPERTY_INPUTPATH,oldValue,inputpath);
    }

    public String getOutputpath() {
        return outputpath;
    }

    public void setOutputpath(String outputpath) {
        String oldValue = this.outputpath;
        this.outputpath = outputpath;
        firePropertyChange(PROPERTY_OUTPUTPATH,oldValue,outputpath);

        /*int position = outputpath.lastIndexOf( '/' );
        outputpath = outputpath.substring(position+1);
        //firePropertyChange(PROPERTY_OUTPUTPATH, oldValue,outputpath);     //Wenn nicht auskommentiert, wird in beiden Textfeldern der entgültige Output-Name angezeigt
        */

        //TODO dass im directoryNameTextField nur benutzerdefinierter Name angezeigt wird und im outputFolderTextField gleich der Pfad sich mitaktualisiert
    }

    //endregion Getter&Setter

}
