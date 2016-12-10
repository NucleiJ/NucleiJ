package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.ColumnSpec;

/**
 *
 * @author Andreas Mattes
 * @version 1.0
 */
public class NdpiConverter extends Model
{
    //region Constants
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_INPUTPATH = "inputpath";
    public static final String PROPERTY_OUTPUTPATH = "outputpath";
    public static final String PROPERTY_MAGNIFICATION = "magnification";


    public static final String SINGLE_FILE = "Single_File";
    public static final String SINGLE_DIR = "Single_Dir";
    public static final String MULTI_FILE = "Multi_File";
    public static final String MULTI_DIR = "Multi_Dir";
    public static final String[] TYPE_CHOICES = {SINGLE_FILE, MULTI_FILE};

    //endregion Constants

    //*******************************************************************
    enum Magnification {x40,x10}

    private String type;
    private Magnification magnification;
    private String inputpath;
    private String outputpath;

    public NdpiConverter()
    {
        super();

        this.type = MULTI_FILE;
        this.inputpath = "";
        this.outputpath = "";
        this.magnification = Magnification.x10;
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

    public Magnification getMagnification() {
        return magnification;
    }

    public void setMagnification(Magnification magnification) {
        Magnification oldValue = this.magnification;
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
    }


    //endregion Gerrer&Setter

}
