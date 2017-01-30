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
    public static final String PROPERTY_INPUTPATH       = "inputpath";
    public static final String PROPERTY_OUTPUTPATH      = "outputpath";
    public static final String PROPERTY_MAGNIFICATION   = "magnification";

    public static final String MAG_X5   = "x5";
    public static final String MAG_X10  = "x10";
    public static final String MAG_X40  = "x40";
    public static final String[] MAG_CHOICES = {MAG_X5, MAG_X10, MAG_X40};
    //endregion Constants

    //*******************************************************************
    private String magnification;
    private String inputpath;
    private String outputpath;

    private int choice;
    private int numberNdpiFiles;
    private int numberTifFiles;

    public NdpiConverter()
    {
        super();

        this.inputpath = "";
        this.outputpath = "";
        this.magnification = "x10";
    }

    public int getChoice () {
        return choice;
    }

    public void setChoice (int choice) {
        this.choice = choice;
    }

    public int getNumberNdpiFiles() {
        return numberNdpiFiles;
    }

    public void setNumberNdpiFiles(int numberNdpiFiles) {
        this.numberNdpiFiles = numberNdpiFiles;
    }

    public int getNumberTifFiles() {
        return numberTifFiles;
    }

    public void setNumberTifFiles(int numberTifFiles) {
        this.numberTifFiles = numberTifFiles;
    }


    //region Getter&Setter
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
    }
    //endregion Getter&Setter
}
