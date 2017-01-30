package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.NucleiJ;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import com.ezware.dialog.task.TaskDialog;
import de.javasoft.util.OS;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andreas on 23.01.17.
 */

public class AnalyzerConverterTask extends SwingWorker<String, String> {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());

    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;

    public AnalyzerConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
    }

    @Override
    protected String doInBackground() throws Exception {
        MainAnalyzer mainAnalyzer = new MainAnalyzer(glScanAnalyzer);
        List<String> tifFileList; //Wird erst vor Konvertieren aktualisiert geholt
        List<String> ndpiFileList = glScanAnalyzer.getNdpiList();
        String element;
        float currentStatus = 0;
        float add;
        int choice = ndpiConverter.getChoice();
        float numberNdpiFiles = ndpiConverter.getNumberNdpiFiles();
        float numberTifFiles = ndpiConverter.getNumberTifFiles();

        switch (choice) {
            case 0:
                add = 100 / (numberNdpiFiles * 2 + numberTifFiles);
                taskDialog.setInstruction("Converting & analyzing your Scans...");
                break;
            case 1:
                add = 100 / numberNdpiFiles;
                taskDialog.setInstruction("Converting your Scans...");
                break;
            case 2:
                add = 100 / numberTifFiles;
                taskDialog.setInstruction("Analyzing your Scans...");
                break;
            default:
                add = 0;
        }

        if (choice != 2) { // Sicher Konvertieren
            // Konvertieren & an TifListe anhängen
            for (String ndpiListElement : ndpiFileList) {
                numberNdpiFiles--;

                //element = ndpiListElement.substring(ndpiListElement.lastIndexOf(File.separator)+1, ndpiListElement.lastIndexOf("]"));// + "\n" + "Remaining: " + numberNdpiFiles + "NDPIs & " + numberTifFiles + "TIFs";
                publish(ndpiListElement.substring(ndpiListElement.lastIndexOf(File.separator) + 1));
                //taskDialog.setText("File: " + String.valueOf(ndpiListElement).substring(String.valueOf(ndpiListElement).lastIndexOf(File.separator)+1, String.valueOf(ndpiListElement).lastIndexOf("]")));
                //+ "\n" + "Remaining: " + numberNdpiFiles + "NDPIs & " + numberTifFiles + "TIFs");

                startConverter(ndpiListElement);
                currentStatus = currentStatus + add;

                progressBar.setValue(Math.round(currentStatus));
                //publish(Math.round(currentStatus));
            }
        }

        if (choice != 1) { // Sicher Analysieren
            tifFileList = glScanAnalyzer.getTifList();      //Aktualisierte Liste holen!!!
            for (String tifListElement : tifFileList) {
                numberTifFiles--;

                //element = tifListElement.substring(tifListElement.lastIndexOf(File.separator)+1, tifListElement.lastIndexOf("]"));// + "\n" + "Remaining: " + numberNdpiFiles + "NDPIs & " + numberTifFiles + "TIFs";
                publish(tifListElement.substring(tifListElement.lastIndexOf(File.separator) + 1));
                //taskDialog.setText("File: " + String.valueOf(tifListElement).substring(String.valueOf(tifListElement).lastIndexOf(File.separator)+1, String.valueOf(tifListElement).lastIndexOf("]")));
                // + "\n" + "Remaining: " + numberNdpiFiles + "NDPIs & " + numberTifFiles + "TIFs");

                mainAnalyzer.setDateiname(tifListElement);
                System.out.println("\n****************\n" + tifListElement + "\n********************");
                mainAnalyzer.run(null);
                currentStatus = currentStatus + add;

                progressBar.setValue(Math.round(currentStatus));
                //publish(Math.round(currentStatus));
            }
            mainAnalyzer.createSummary();


        }
        return "Finished";
    }

    @Override
    protected void process(List<String> element) {
        super.process(element);

        taskDialog.setText(element.toString());
    }

    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        progressBar.setValue(100);
        taskDialog.setVisible(false);
    }

    private void startConverter(String filePath) {

        String testwhereJARisLocated = NucleiJ.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedTestwhereJARisLocated = testwhereJARisLocated;
        try {
            decodedTestwhereJARisLocated = URLDecoder.decode(testwhereJARisLocated, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("JAR wird unter dem Pfad: " + decodedTestwhereJARisLocated.concat("ndpi-converter.jar") + " gesucht!!");

        File converterFile = new File(decodedTestwhereJARisLocated.concat("ndpi-converter.jar"));
        String absolutePathofNdpiJar = converterFile.getAbsolutePath();

        if (converterFile.exists()) {
            //Pfad ausaehlen
            System.out.println("Konverter JAR selber auswaehlen");

            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            JFileChooser chooser = new JFileChooser();

            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setControlButtonsAreShown(true);
            chooser.setDialogTitle("Konverter JAR Auswählen");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("jar"));

            if (chooser.showDialog(parent, "Auswählen") == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile().isFile()) {
                    if (ndpiConverter.getOutputpath().isEmpty()) {
                        ndpiConverter.setOutputpath(ndpiConverter.getInputpath().substring(0, ndpiConverter.getInputpath().lastIndexOf(File.separator)).concat(File.separator + "Output"));
                    }
                    absolutePathofNdpiJar = chooser.getSelectedFile().getAbsolutePath();
                }
            }
            else
            {
                System.out.println("keine JAR ausgewaehlt!");
            }
        }

            File outputpath = new File(ndpiConverter.getOutputpath());

            System.out.println(absolutePathofNdpiJar);
            System.out.println(filePath);
            System.out.println(outputpath.getParent());

            //absolutePathofNdpiJar = "/home/stefan/Desktop/ndpi-to-ome-tiff-converter-v1.5/ndpi-converter.jar";
            Process p;

            //absolutePathofNdpiJar = "/home/andreas/IdeaProjects/nucleij/lib/ndpi-converter/ndpi-converter.jar";

            String OS = System.getProperty("os.name").toLowerCase();
            try {
                if (OS.contains("linux")) {
                    System.out.println("Your OS is Linux");
                    p = Runtime.getRuntime().exec("sudo java -jar " + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath + "\" \"" + outputpath.getParent() + "\"");
                    //p = Runtime.getRuntime().exec("sudo java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \""+ filePath + "\" \"" + outputpath.getParent() + "\"");
                    //p = Runtime.getRuntime().exec("sudo java -jar /home/andreas/IdeaProjects/nucleij/lib/ndpi-converter/ndpi-converter.jar -i 2 -c lzw -s /home/andreas/Schreibtisch/Scans/N2700-14\\ 5\\ HE\\ -\\ 2016-06-06\\ 14.57.00.ndpi");
                    //p = Runtime.getRuntime().exec("sudo java -jar /home/andreas/IdeaProjects/nucleij/lib/ndpi-converter/ndpi-converter.jar -i 2 -c lzw -s /home/andreas/Schreibtisch/Scans/N2700-14 5 HE - 2016-06-06 14.57.00.ndpi");

                    // Befehl auf Linux:
                    // FUNKTIONIERT Erben: java -jar "/home/stefan/Desktop/ndpi-to-ome-tiff-converter-v1.5/ndpi-converter.jar"  -i 2 -c lzw -s  "/home/stefan/Desktop/test Scan.ndpi"
                    // sudo java -jar /home/andreas/IdeaProjects/nucleij/lib/ndpi-converter/ndpi-converter.jar -i 2 -c lzw -s /home/andreas/Schreibtisch/Scans/N2700-14\ 5\ HE\ -\ 2016-06-06\ 14.57.00.ndpi
                } else if (OS.contains("windows")) {
                    System.out.println("Your OS is Windows");
                    p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath + "\" \"" + outputpath.getParent() + "\"");
                } else if (OS.contains("mac")) {
                    p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath + "\" \"" + outputpath.getParent() + "\"");
                    System.out.println("Your OS is Mac OS");
                } else {
                    System.out.println("Your OS is not supported!");
                    //TODO Möglicherweise ConvertProzess beenden und not supported Dialog anzeigen
                    p = null;
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        /*try {
            Process p;
            if (ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X10)) {
                System.out.println("X10 Konvertieren beginnt:");
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
                //p = Runtime.getRuntime().exec( "java -jar \"C:\\Users\\Stefan\\Downloads\\ndpi-to-ome-tiff-converter-v1.5\\ndpi-converter.jar\" -i 2 -c lzw -s \"C:\\Users\\Stefan\\Documents\\stapel\\test.ndpi\"");
                System.out.println("X10 Konvertieren sollte enden:");
            }
            else if(ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X40)) {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 1 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            }
            else {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
                LOGGER.warning("Magnification error: set to Std. 10x Magnification");
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

            String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
            String newTifListElement = filePath.replace(".ndpi", renameFileName);
            System.out.println("Der Filename nach dem Konvertieren ist:" + newTifListElement);
            glScanAnalyzer.addTifToList(newTifListElement);
        }

}
