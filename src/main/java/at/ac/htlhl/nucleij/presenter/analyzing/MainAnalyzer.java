package at.ac.htlhl.nucleij.presenter.analyzing;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic.*;
import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.io.File;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;


public class MainAnalyzer implements PlugInFilter {

    // Constants **************************************************************
    public String  pixelSize;                   //fuer min Partikelgroesse
    public int     aufloesungSlider;            //fuer die aufloesung der heatmap
    public boolean cropCheckbox;                //var ob das bild zugeschnitten werden soll
    public boolean heatmapCheckbox;             //var ob heatmap erstellt werden soll
    public boolean exportResultsCheckbox;       //var ob Results exportiert werden sollen

    StringAdder            summaryStack        = new StringAdder();
    StringAdder            csvSummaryStack     = new StringAdder();
    StringTransfer         resultStack         = new StringTransfer();
    Timestamp              today               = new Timestamp();
    MeasureSettings        settings            = new MeasureSettings();
    PictureAnalyzer        simplePixelAnalysis = new PictureAnalyzer();
    StringTransfer         path                = new StringTransfer();
    StringTransfer         file                = new StringTransfer();
    Heatmap                heatmap             = new Heatmap();
    Exporter               startExporter       = new Exporter();
    PictureCharacteristics properties          = new PictureCharacteristics();

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter  ndpiConverter;
    private String radiobox = "Nothing";
    private String dateiname;
    private String csvSummaryString = "";

    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    public MainAnalyzer(GLScanAnalyzer glScanAnalyzer, NdpiConverter ndpiConverter) {
        this.glScanAnalyzer = glScanAnalyzer;
        this.ndpiConverter = ndpiConverter;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

    public void createSummary() {   //Erzeugt die beiden Summary-Files (.txt, .csv)
        startExporter.summary(summaryStack.getString(), path.getValue(), today.getCurrentTimeStamp());
        startExporter.csvSummary(csvSummaryStack.getString(), path.getValue());
    }

    public int setup(String arg, ImagePlus imp) {
        return NO_IMAGE_REQUIRED;       //Beim Start wird kein geladenes Bild benoetigt
    }

    public void run(ImageProcessor original_alt) {
        path.setValue(glScanAnalyzer.getOutputpath());                                  // Outputpfad holen
        startExporter.setnewDirectoryname(File.separator + "Output");   // Name des Ordners: Output
        File outputPathFile = new File(glScanAnalyzer.getOutputpath());
        outputPathFile.mkdirs();                                                        // Verzeichnis erstellen

        csvSummaryStack.appendString("csvHeader");                     // Header fur csvDatei erstellen
        Path absoulterPfad = Paths.get(dateiname);
        file.setValue(absoulterPfad.getFileName().toString());
        String pfad = dateiname;

        //ist Scan in x10 oder x40 Aufloesung? -> Umrechnungsfaktor px=um
        boolean x10 = file.getValue().toLowerCase().contains("x10");
        getUserInput(x10);                                          // x10 pixelsize oder x40?
        double distance = settings.selectMagnificationAutomatically(pfad, x10);
        properties.setMagnification(distance);

        //gewaehltes Bild automatisch laden
        LOGGER.info(pfad);
        ImagePlus imp = IJ.openImage(pfad);
        imp.unlock();
        imp.show();

        //set Measurements Properties
        settings.setMeasurementProporties(imp, distance);

        //set ROI? - UP zum zuschneiden oeffnen
        setROI(imp);

        //Variablen usw erstellen
        ImageProcessor original = imp.getProcessor();
        int w = original.getWidth();
        int h = original.getHeight();
        ImageProcessor copy = original.duplicate();
        ImageProcessor sicherung = original.duplicate();

        //Neues 8-Bit Bild erstellen fuer Heatmap
        ImagePlus heatmapTmp = IJ.createImage("Heatmap", "8-bit", w / 10, h / 10, 1);
        ImageProcessor heatmap_ip = heatmapTmp.getProcessor();


        //Hauptprozess zum Erkennen der Zellkerne
        startImageProcessingActivity(original, copy, sicherung, heatmapTmp, imp, heatmap_ip, x10, radiobox, w, h);

        //Alle Fenster schlieen
        if (IJ.isResultsWindow() == true)        //Error Handling
        {
            IJ.selectWindow("Results");
            IJ.run("Close");

        }
    }

    //Particle Analyzer mit geaenderten Parametern starten, weitere Werte berechnen und Ausgabe starten
    public void startParticleAnalyzer(ImagePlus markiert, String radiobox) {
        //Particle Analyzer parametrisieren und ausfuehren:
        IJ.run("Set Measurements...", "area standard centroid perimeter bounding shape redirect=None decimal=3");
        String befehl = "size=$-50 pixel circularity=0.00-1.00 show=% ";    //display

        String endbefehl = befehl.replace("$", pixelSize);
        IJ.run("Analyze Particles...", endbefehl.replace("%", radiobox));

        //Resultate auslesen:
        ResultsTable alt = Analyzer.getResultsTable();
        ResultsTable rt = (ResultsTable) alt.clone();

        int counter = 0;
        counter = rt.getCounter();

        //Erstelle Arrays fuer alle Messwerte
        double[] area = new double[counter];
        double[] roundness = new double[counter];
        double[] xCoordinate = new double[counter];
        double[] yCoordinate = new double[counter];
        double[] perim = new double[counter];
        double[] width = new double[counter];
        double[] height = new double[counter];
        double[] circ = new double[counter];
        double[] ar = new double[counter];
        double[] solidity = new double[counter];
        double area_max = 0;
        double area_min = 999999999;
        double area_all = 0;
        double perim_all = 0;
        double area_arith = 0;
        double perim_arith = 0;
        int found_particles = 0;

        DecimalFormat d3 = new DecimalFormat("#.###");
        d3.setRoundingMode(RoundingMode.HALF_UP);

        DecimalFormat d1 = new DecimalFormat("#.##");
        d1.setRoundingMode(RoundingMode.HALF_UP);

        //Zahlenformate und Kommastellendefiniton
        DecimalFormat nr = new DecimalFormat("00000");
        DecimalFormat n13 = new DecimalFormat("0.000");
        DecimalFormat n32 = new DecimalFormat("000.00");

        //StringBuffer erstellen, in diesen werden nun alle Results gespeichert
        StringBuffer resultzeile = new StringBuffer();
        resultzeile.append("Nummer\tArea\tPerim.\tRound\tWidth\tHeight\tX\t\tY\tCirc.\tSolidity\r\n");

        if (exportResultsCheckbox == true) {
            for (int x = 0; x <= counter - 1; x++) {
                area[x] = rt.getValue("Area", x);
                roundness[x] = rt.getValue("Round", x);
                xCoordinate[x] = rt.getValue("X", x);
                yCoordinate[x] = rt.getValue("Y", x);
                perim[x] = rt.getValue("Perim.", x);
                width[x] = rt.getValue("Width", x);
                height[x] = rt.getValue("Height", x);
                circ[x] = rt.getValue("Circ.", x);
                ar[x] = rt.getValue("AR", x);
                solidity[x] = rt.getValue("Solidity", x);

                //Diese Variablen in String speichern
                resultzeile.append(nr.format(x + 1) + "\t");
                resultzeile.append(d3.format(area[x]) + "\t");
                resultzeile.append(d3.format(perim[x]) + "\t");
                resultzeile.append(n13.format(roundness[x]) + "\t");
                resultzeile.append(d3.format(width[x]) + "\t");
                resultzeile.append(d3.format(height[x]) + "\t");
                resultzeile.append(n32.format(xCoordinate[x]) + "\t\t");
                resultzeile.append(n32.format(yCoordinate[x]) + "\t");
                resultzeile.append(n13.format(circ[x]) + "\t");
                resultzeile.append(n13.format(solidity[x]) + "\r\n");

                //arithmetischen Umfang berechnen
                perim_all += perim[x];
            }
            resultStack.setValue(resultzeile.toString());

        } else {
            for (int x = 0; x <= counter - 1; x++) {
                //Werte in Variablen laden
                area[x] = rt.getValue("Area", x);
                roundness[x] = rt.getValue("Round", x);
                perim[x] = rt.getValue("Perim.", x);

                //arithmetischen Umfang berechnen
                perim_all += perim[x];
            }
        }

        for (int x = 0; x <= counter - 1; x++) {
            //Berechnungen fuer Area:
            if (area[x] < area_min) {
                area_min = area[x];
            } else if (area[x] > area_max) {
                area_max = area[x];
            }
            area_all += area[x];

            //ovale Partikel zaehlen
            if (roundness[x] > 0.05 && roundness[x] < 0.4) {
                //Ausgabe der Anzahl der ovalen Partikel:
                found_particles++;
            }

        }

        //Arithmetisches Mittel berechnen
        area_arith = area_all / counter;
        perim_arith = perim_all / counter;

        //Median berechnen:
        Arrays.sort(area);
        double median;
        if (area.length % 2 == 0) {
            median = (area[area.length / 2] + area[area.length / 2 - 1]) / 2;
        } else {
            median = area[area.length / 2];
        }

        outputCellnucleiInfo(counter, perim_arith, area_all, area_min, area_max, area_arith, median, found_particles);
        IJ.run("Clear Results", "");

    }


    public void startImageProcessingActivity(ImageProcessor original, ImageProcessor copy, ImageProcessor sicherung, ImagePlus heatmapTmp,
                                             ImagePlus imp, ImageProcessor heatmap_ip, boolean x10, String radiobox, int w, int h) {
        //Zellkerne erkennen und Fehlerpixel ignorieren sowie Tumarflaeche berechnen
        int gewebepixel = simplePixelAnalysis.detectCellPixels(original, w, h);
        properties.setGewebepixel(gewebepixel);
        simplePixelAnalysis.ignoreSinglePixels(original, copy, x10, w, h);

        //Image fuer Kommandos vorbereiten
        ImagePlus markiert = IJ.getImage();
        markiert.unlock();

        //Maske erstellen, Loecher ausfuellen, Filter anwenden
        createMask(markiert, x10, radiobox, w, h);
        ImageProcessor maske = markiert.getProcessor();
        ImageProcessor heatmapMaske = maske.duplicate();

        //Maske ueber Originalbild legen, Zellkerne Rot markieren
        simplePixelAnalysis.addMasktoOriginal(maske, sicherung, w, h);

        //Ergebnis anzeigen & als neue Datei speichern,
        imp.updateAndRepaintWindow();

        //Heatmap erstellen
        if (heatmapCheckbox == true) {
            heatmap.create(file.getValue(), path.getValue(), startExporter.getnewDirectoryname(), aufloesungSlider, heatmapTmp, heatmapMaske, heatmap_ip, w, h);
        }

        imp.changes = false;        //es wurden keine Aenderungen vorgenommen, -> "wollen Sie speichern" umgehen

        //Bild schliessen
        imp.close();
        //Ende des Erkennungs Prozesses

    }

    public void setROI(ImagePlus imp) {
        if (cropCheckbox == true)        //Bild wird zugeschnitten
        {
            cropCheckbox = false;

            do {
                //Bild oeffnen, var setzen dass bild bereits offen ist
                imp.updateAndRepaintWindow();

                imp.setRoi(glScanAnalyzer.getRoiX(), glScanAnalyzer.getRoiY(), glScanAnalyzer.getRoiWidth(), glScanAnalyzer.getRoiHeight());

                Roi roi = imp.getRoi();
                if (roi instanceof Roi) {
                    IJ.run(imp, "Crop", "");
                    imp.updateAndDraw();
                    cropCheckbox = true;
                }
            } while (cropCheckbox == false);
        }
    }

    public void getUserInput(boolean x10) {
        if (!x10) {
            pixelSize = "8"; //egal, da in um angegeben
        } else {
            pixelSize = "8";
        }

        if (glScanAnalyzer.getRoiarea() == null) {
            cropCheckbox = false;
        } else {
            cropCheckbox = true;
        }

        exportResultsCheckbox = true;
        cropCheckbox = glScanAnalyzer.isSelectroi();
        heatmapCheckbox = glScanAnalyzer.isCalculateHeatmap();
        aufloesungSlider = glScanAnalyzer.getHeatmapquality();
    }

    //Maske erstellen
    public void createMask(ImagePlus markiert, boolean x10, String radiobox, int w, int h) {
        IJ.run(markiert, "Multiply...", "value=5");

        Prefs.blackBackground = false;
        IJ.run(markiert, "Make Binary", "");
        IJ.run(markiert, "Fill Holes", "");

        //Erben Analyzer starten
        startParticleAnalyzer(markiert, radiobox);

        //Results exportieren
        if (exportResultsCheckbox == true) {
            startExporter.results(resultStack.getValue(), file.getValue(), path.getValue());
        }

        //Bild wieder entsperren und zurueck invertieren
        markiert.unlock();

        //Invertieren oder nicht?
        ImageProcessor invertieren = markiert.getProcessor();
        int bwZaehler = 0;
        for (int x = 0; x <= w; x++) {
            for (int y = 0; y <= h; y++) {
                int p = invertieren.getPixel(x, y);
                if (p == 0) {
                    //Zellkerne zaehlen
                    bwZaehler++;
                }
            }
        }

        //Fehler verhindern, da Bild vom externen Plugin invertiert werden kann
        int gesamtzaehler = w * h;
        if (bwZaehler < (gesamtzaehler / 2)) {
            IJ.run(markiert, "Invert", "");
        }

        //Filter anwenden und wieder zu RGB Bild zurueckwandeln
        if (x10) {
            IJ.run(markiert, "Median...", "radius=1");
        } else {
            IJ.run(markiert, "Median...", "radius=3");
        }

        IJ.run(markiert, "RGB Color", "");

        return;
    }

    //Ausgabe der Zellkern Informationen
    public void outputCellnucleiInfo(double counter, double perim_arith, double area_all, double area_min, double area_max, double area_arith, double median, double found_particles) {

        String ueberschrift = "";
        int lenghtFilename = file.getValue().length();
        if ((50 - lenghtFilename) % 2 == 0) {
            for (int k = 0; k < ((70 - lenghtFilename) / 2); k++) {
                ueberschrift = ueberschrift + "_";
            }
            ueberschrift = ueberschrift + file.getValue();
            for (int k = 0; k < ((70 - lenghtFilename) / 2); k++) {
                ueberschrift = ueberschrift + "_";
            }
        } else {
            for (int k = 0; k < (((70 - lenghtFilename - 1) / 2) + 1); k++) {
                ueberschrift = ueberschrift + "_";
            }
            ueberschrift = ueberschrift + file.getValue();
            for (int k = 0; k < ((70 - lenghtFilename - 1) / 2); k++) {
                ueberschrift = ueberschrift + "_";
            }
        }
        int intcounter = (int) counter;

        //Ausgabe in String -> Summary-File
        String summaryString = "";
        //summaryString = summaryString + "\r\n\r\n" + ueberschrift + "\r\nFound nuclei:\t\t\t" + intcounter + "\r\nAdditional measured values:\r\n";
        summaryString = summaryString + "\r\n\r\n" + ueberschrift + "\r\n" + bundle.getString("Txt.FoundNuclei.text") + intcounter + "\r\n" + bundle.getString("Txt.AddValues.text") + "\r\n";

        //Werte auf 3 Kommastellen runden und anzeigen
        DecimalFormat df = new DecimalFormat("#.000");
        //df.setRoundingMode(RoundingMode.HALF_UP);
        //df.setRoundingMode(RoundingMode.);

        //Ausgaben in einem ImageJ Log Fenster / speichern in String:
        csvSummaryString = csvSummaryString + file.getValue().replaceFirst("[.][^.]+$", "") + ";";
        csvSummaryString = csvSummaryString + intcounter + ";";

        summaryString = summaryString + bundle.getString("Txt.GesGewebeA.text") + df.format(properties.getTumorArea()) + " um2\r\n";
        csvSummaryString = csvSummaryString + df.format(properties.getTumorArea()) + ";";

        summaryString = summaryString + bundle.getString("Txt.TotalArea.text") + df.format(area_all) + " um2\r\n";
        csvSummaryString = csvSummaryString + df.format(area_all) + ";";

        summaryString = summaryString  + bundle.getString("Txt.NucleiArea.text") + df.format(100 / (properties.getTumorArea() / area_all)) + "%\r\n";
        csvSummaryString = csvSummaryString + df.format(100 / (properties.getTumorArea() / area_all)) + ";";

        summaryString = summaryString  + bundle.getString("Txt.NucleipMm.text") + df.format((intcounter / properties.getTumorArea()) * 1000000) + "\r\n";        //Zellkerne 1 / Tumorfläche in um2 / 1000000
        csvSummaryString = csvSummaryString + df.format((intcounter / properties.getTumorArea()) * 1000000) + ";";        //Zellkerne 1 / Tumorfläche in um2 / 1000000

        summaryString = summaryString  + bundle.getString("Txt.ArithPeri.text") + df.format(perim_arith) + " um\r\n";
        csvSummaryString = csvSummaryString + df.format(perim_arith) + ";";

        summaryString = summaryString + bundle.getString("Txt.ArithArea.text") + df.format(area_arith) + " um2\r\n";    //Ausgabe des arithmetishen Mittels aller Zellkernflaechen (auf 3 Kommastellen genau)
        csvSummaryString = csvSummaryString + df.format(area_arith) + ";";    //Ausgabe des arithmetishen Mittels aller Zellkernflaechen (auf 3 Kommastellen genau)

        summaryString = summaryString + bundle.getString("Txt.MedianArea.text") + df.format(median) + " um2\r\n";                //Ausgabe des Medianwerts aller Zellkernfleachen (auf 3 Kommastellen genau)
        csvSummaryString = csvSummaryString + df.format(median) + ";\n";                //Ausgabe des Medianwerts aller Zellkernfleachen (auf 3 Kommastellen genau)

        //An StringStack uebergeben
        summaryStack.appendString(summaryString);
        csvSummaryStack.appendString(csvSummaryString);

        return;
    }
}

