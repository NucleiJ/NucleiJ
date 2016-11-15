package at.ac.htlhl.nucleij.model;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.forms.layout.ColumnSpec;

/**
 * Created by Stefan on 11.11.2016.
 */
public class NdpiConverter extends Model
{
    //region Constants
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_INPUTPATH = "inputpath";
    public static final String PROPERTY_OUTPUTPATH = "outputpath";
    public static final String PROPERTY_MAGNIFICATION = "magnification";


    public static final Object SINGLE = ColumnSpec.LEFT;
    public static final Object MULTI = ColumnSpec.RIGHT;

    //endregion Constants

    //*******************************************************************
    enum Magnification {x40,x10}

    private Object type;
    private Magnification magnification;
    private String inputpath;
    private String outputpath;

    public NdpiConverter()
    {
        super();

        this.type = MULTI;
        this.inputpath = "";
        this.outputpath = "";
        this.magnification = Magnification.x10;
    }

    //region Getter&Setter

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        Object oldValue = this.type;
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
