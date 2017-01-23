package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import com.ezware.dialog.task.TaskDialog;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Stefan on 29.12.2016.
 */
public class RoiTask
{
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());

    private GLScanAnalyzer glScanAnalyzer;

    public RoiTask(GLScanAnalyzer glScanAnalyzer)
    {
        super();

        this.glScanAnalyzer = glScanAnalyzer;
    }

    public String setROI()
    {
        JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                "This feature is currently not working!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);

        System.out.println(IJ.getFullVersion());
        System.out.println("set gestartet");
        ImagePlus bild = IJ.openImage(glScanAnalyzer.getInputpath());
        bild.show();
        //imp.setRoi(1404,15,690,966);
        //IJ.run(imp, "Crop", "");

        boolean roigesetzt = false;

        do
        {

            //bild.updateAndRepaintWindow();
            new WaitForUserDialog("Information", "Bitte rechteckige ROI\nsetzen und OK druecken").show();

            Roi roi = bild.getRoi();
            if (roi instanceof Roi)
            {
                PolygonRoi polygon = (PolygonRoi)roi;
                //Rectangle bounds = roi.getBounds();
                int n = polygon.getNCoordinates();
                int[] x = polygon.getXCoordinates();
                int[] y = polygon.getYCoordinates();
                System.out.println(x+"."+y);

                /*IJ.run(bild, "Crop", "");
                bild.updateAndDraw();
                */
                roigesetzt = true;
            }
            roigesetzt = true;
        }while (roigesetzt==false);


        return "test";
    }


}

