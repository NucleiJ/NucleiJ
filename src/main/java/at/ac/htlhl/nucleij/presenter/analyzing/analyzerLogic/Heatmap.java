package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;

import at.ac.htlhl.nucleij.NucleiJ;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


public class Heatmap {
    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    public void create(String filename, String path, String newDirectoryname, int aufloesungSlider, ImagePlus heatmapTmp, ImageProcessor heatmapMaske, ImageProcessor heatmap_ip, int w, int h)
    {
        //Dichte berechnen:
        float radiusFloat = (float) (w * 0.0002 * aufloesungSlider);        //w/(w/aufloesung);
        int radius = Math.round(radiusFloat);
        int max = 0;
        double max_double = 0;
        int xsuchsetzRadius = w / (w / 10);
        int ysuchsetzRadius = 10;

        for (int x = 0; x <= w; x = x + xsuchsetzRadius) {
            //region ProgressBar !!
            if (x == 0) {
                IJ.showProgress(0.01);
            } else if (x < (w / 100 * 10)) {
                IJ.showProgress(0.1);
            } else if (x < (w / 100 * 20)) {
                IJ.showProgress(0.2);
            } else if (x < (w / 100 * 30)) {
                IJ.showProgress(0.3);
            } else if (x < (w / 100 * 40)) {
                IJ.showProgress(0.4);
            } else if (x < (w / 100 * 50)) {
                IJ.showProgress(0.5);
            } else if (x < (w / 100 * 60)) {
                IJ.showProgress(0.6);
            } else if (x < (w / 100 * 70)) {
                IJ.showProgress(0.7);
            } else if (x < (w / 100 * 80)) {
                IJ.showProgress(0.8);
            } else if (x < (w / 100 * 90)) {
                IJ.showProgress(0.9);
            } else if (x < (w / 100 * 95)) {
                IJ.showProgress(0.95);
            }
            //endregion ProgressBar

            for (int y = 0; y <= h; y = y + ysuchsetzRadius) {
                // in Radius Umgebung nach roten Pixeln suchen
                int i, j, sum = 0, abgesuchtepixel = 0;
                float dichte;
                for (j = -radius; j <= radius; j++) {
                    for (i = -radius; i <= radius; i++) {
                        if (((x + j) >= 0) && ((y + i) >= 0))        //regel dass wir nicht im negativen bereich suchen
                        {
                            abgesuchtepixel++;
                            int c = heatmapMaske.getPixel(x + j, y + i);
                            if (c == -16777216) {
                                sum++;                            //gefundene Pixel zaehlen
                            }
                        }
                    }
                }
                //alles abgesucht, sum ist gesetzt
                if (sum == 0) {
                    dichte = 0;
                    if (((x / 10) >= 0) && ((y / 10) >= 0)) {
                        heatmap_ip.putPixel(x / 10, y / 10, 0);
                    }

                } else {
                    dichte = 255 / (abgesuchtepixel / sum);
                    if (dichte > max) {
                        max = (int) dichte;
                        max_double = dichte;
                    }
                    //float dichte zu int tmp
                    int tmp = Math.round(dichte);
                    for (j = -xsuchsetzRadius; j <= xsuchsetzRadius; j++) {
                        for (i = -ysuchsetzRadius; i <= ysuchsetzRadius; i++) {
                            if (((x / 10) >= 0) && ((y / 10) >= 0)) {
                                heatmap_ip.putPixel(x / 10, y / 10, tmp);
                            }
                        }
                    }
                }
            }
        }
        heatmapTmp.show();

        double heatmapMaxDichteWert = 100 / (radius * radius / (max_double / 255 * (radius * radius)));

        float faktorfloat = (255 / max);
        String befehl = "value=%";
        String value = String.valueOf(faktorfloat);

        File newLutPath = null;
        try {
            newLutPath = new File(NucleiJ.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            TaskDialogs.inform(((SingleFrameApplication) Application.getInstance()).getMainFrame(), bundle.getString("LUT.Dialog.instruction"), bundle.getString("LUT.Dialog.text"));
        }

        assert newLutPath != null;
        String absolutePathofLUT = newLutPath.getParent().concat(File.separator).concat("data").
                concat(File.separator).concat("lut").concat(File.separator).concat("RedGreenErben.lut");

        if (new File (absolutePathofLUT).exists())
        {
            try {
                absolutePathofLUT = URLDecoder.decode(absolutePathofLUT, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            LOGGER.info("LUT-Path: " + absolutePathofLUT);
            IJ.run("LUT... ", "open=".concat(absolutePathofLUT));
        }
        else
        {
            LOGGER.warning("LUT not found!");
            if(absolutePathofLUT.contains(" "))
            {
                LOGGER.warning("Space in absolute Path of LUT!");
            }
        }


        int heatmapWidth = heatmap_ip.getWidth();
        int heatmapHeight = heatmap_ip.getHeight();
        int maxPixel = 0;

        // Heatmap verbessern, falls es nur sehr kleine Hitzefelder gibt, wird die Heatmap aufgehellt
        while (maxPixel < (heatmapWidth * heatmapHeight / 500)) {
            if (maxPixel == 0) {
                IJ.run(heatmapTmp, "Multiply...", befehl.replace("%", value));
            } else {
                IJ.run(heatmapTmp, "Multiply...", "value=1.1");
            }
            maxPixel = 0;
            for (int x = 0; x <= heatmapWidth; x++) {
                for (int y = 0; y <= heatmapHeight; y++) {
                    int p = heatmap_ip.getPixel(x, y);
                    if (p > 240) {
                        maxPixel++;
                    }
                }
            }

        }

        // Einfugen der Legende
        // Berechnen der Werte
        int hLegend = heatmapHeight / 900;
        if (hLegend < 5) {
            hLegend = 5;
        }

        int wLegend = heatmapWidth / 900;
        if (wLegend < 5) {
            wLegend = 5;
        }
        int hightLegend = heatmapHeight / 70;

        int yStartLegend = heatmapHeight - hLegend - hightLegend;
        int yEndLegend = heatmapHeight - hLegend;
        int xStartLegend = wLegend;
        int xEndLegend = heatmapWidth - wLegend;

        int farbe = 0;
        double lenghtLegend = heatmapWidth - (2 * wLegend);
        double farbWechsel = (lenghtLegend / 255);
        double colorSwitchCounter = 0;
        double exactLegendwidth = xEndLegend;
        int pixelweite = xEndLegend / 255;
        double uebertragswert = exactLegendwidth / 255;
        double nextV = uebertragswert - pixelweite;
        int nextC = (int) Math.round(1 / nextV);
        int changeColorP = 0;

        for (int x = xStartLegend; x <= xEndLegend; x++) {
            colorSwitchCounter++;
            changeColorP++;

            if (changeColorP == nextC) {
                colorSwitchCounter--;
            }

            for (int y = yStartLegend; y <= yEndLegend; y++) {
                heatmap_ip.putPixel(x, y, farbe);
            }

            if (colorSwitchCounter == Math.round(farbWechsel)) {
                farbe++;
                colorSwitchCounter = 0;
            }
        }

        //Speichern der Heatmap
        String resultsFilename = filename.replaceFirst("[.][^.]+$", "") + "_Heatmap.png";        //Neuen Filenamen festlegen
        String exportHeatmap = path + File.separator + resultsFilename;

        IJ.run(heatmapTmp, "Median...", "radius=2");
        IJ.saveAs(heatmapTmp, "Png", exportHeatmap);

        DecimalFormat d3 = new DecimalFormat("#.##");
        d3.setRoundingMode(RoundingMode.HALF_UP);

        String key = String.valueOf(d3.format(heatmapMaxDichteWert)) + "%";

        BufferedImage bufferedImage = heatmap_ip.getBufferedImage();

        Graphics graphics = bufferedImage.getGraphics();

        graphics.setColor(Color.GREEN);
        graphics.setFont(new Font("Arial Black", Font.BOLD, heatmapWidth / 50));

        int schriftgroesse = Math.toIntExact((long) (lenghtLegend - 35));
        if (schriftgroesse < 5) {
            schriftgroesse = 5;
        }
        graphics.drawString(key, schriftgroesse, yStartLegend - 5);
        try {
            ImageIO.write(bufferedImage, "png", new File(exportHeatmap));        //kein tif verfuegbar
        } catch (IOException e) {
            e.printStackTrace();
        }

        heatmapTmp.changes = false;

        if(absolutePathofLUT.contains(" "))
        {
            LOGGER.warning("Space in absolute Path of LUT!");
            TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), bundle.getString("Error.text"));
            dlg.setInstruction(bundle.getString("LUT.Dialog.instruction"));
            dlg.setText(bundle.getString("LUT.Dialog.text"));
            dlg.setIcon(TaskDialog.StandardIcon.ERROR);
            dlg.show();
        }

        heatmapTmp.close();

        return;
    }
}
