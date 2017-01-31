package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by Stefan on 29.12.2016.
 */
public class RoiTask {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());
    ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    private GLScanAnalyzer glScanAnalyzer;

    public RoiTask(GLScanAnalyzer glScanAnalyzer) {
        super();

        this.glScanAnalyzer = glScanAnalyzer;
    }

    public String setROI() {
        JOptionPane.showMessageDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),
                bundle.getString("FeatureNotAvailable.text"),
                bundle.getString("Warning.text"),
                JOptionPane.WARNING_MESSAGE);

        System.out.println(IJ.getFullVersion());
        System.out.println("set gestartet");
        ImagePlus bild = IJ.openImage(glScanAnalyzer.getInputpath());
        bild.show();

        boolean roigesetzt;

        do {
            new WaitForUserDialog("Information",
                    bundle.getString("RoiSetzen.text")).show();

            Roi roi = bild.getRoi();
            if (roi instanceof Roi) {
                PolygonRoi polygon = (PolygonRoi) roi;
                //Rectangle bounds = roi.getBounds();
                int n = polygon.getNCoordinates();
                int[] x = polygon.getXCoordinates();
                int[] y = polygon.getYCoordinates();
                System.out.println(x + "." + y);

                /*IJ.run(bild, "Crop", "");
                bild.updateAndDraw();
                */
                roigesetzt = true;
            }
            roigesetzt = true;
        } while (roigesetzt == false);


        return "test";
    }


}

