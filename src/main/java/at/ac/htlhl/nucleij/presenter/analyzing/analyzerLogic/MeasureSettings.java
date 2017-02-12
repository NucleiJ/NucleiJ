package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;

/**
 * MeasureSettings Klasse fuer NucleiJ
 *
 * @author Stefan Erben
 * @version 1.0
 */

public class MeasureSettings {
    public void setMeasurementProporties(ImagePlus imp, double distance) {
        String distanceString = String.valueOf(distance);
        String befehlMeassurement = "distance=% known=1 pixel=1 unit=um";
        IJ.run(imp, "Set Scale...", befehlMeassurement.replace("%", distanceString));

        return;
    }

    //Vergroesserung in Dateinamen erkennen
    public double selectMagnificationAutomatically(String str, boolean x10) {
        boolean x40 = str.contains("x40");

        double distanceTmp = 4.392;

        if (x40 == true && x10 == false) {
            distanceTmp = 4.392;
        } else if (x40 == false && x10 == true) {
            distanceTmp = 1.098;
        } else {
            //Generic Dialog zum selber Auswaehlen, da nicht automatisch gefunden
            distanceTmp = selectMagnificationManually();
        }
        return distanceTmp;
    }


    public double selectMagnificationManually() {
        GenericDialog x1040 = new GenericDialog("Welche Vergroesserung?");
        x1040.addMessage("Keine Parameter gefunden.\nWelche Vergroesserung hat der Scan? (x10/x40)?");

        String[] items = {"x10", "x40"};
        x1040.addRadioButtonGroup("Welche Aufloesung hat dieser Scan?", items, 2, 1, "x40");

        x1040.showDialog();
        if (x1040.wasCanceled())
            return 4.392;

        String vergroesserung = x1040.getNextRadioButton();
        double vergroesserungDouble;
        if (vergroesserung == "x10") {
            vergroesserungDouble = 1.098;   //Falls x10 Vergroesserung
        } else {
            vergroesserungDouble = 4.392;   //Falls x40 Vergroesserung
        }

        return vergroesserungDouble;
    }
}
