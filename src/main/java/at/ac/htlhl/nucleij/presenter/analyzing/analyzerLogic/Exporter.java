package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Exporter class for the NucleiJ Analyzer
 *
 * @author Stefan Erben
 * @version 1.0
 */

public class Exporter {
    private String newDirectoryname = "";

    //setValue
    public void setnewDirectoryname(String newDirectoryname) {
        this.newDirectoryname = newDirectoryname;
    }

    //getValue
    public String getnewDirectoryname() {
        return newDirectoryname;
    }

    // exportiert die CSV Summary Datei
    public void csvSummary(String resultzeile, String path) {
        String exportReport = path.concat(File.separator + "Results.csv");

        File reportfile = new File(exportReport);
        try {
            reportfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter writeSummary = new BufferedWriter(fw);
        try {
            writeSummary.write(resultzeile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writeSummary.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeSummary.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The results method creates and exports a textfile (.txt), which contains the analyzed results for the
     * current Scan.
     *
     * @param resultzeile String, which contains the return value of the method getValue() from the object
     *                    resultStack. The String represents the hole Results of the analyzed Scan.
     * @param filename    String, which contains the return value of the getValue Method from the Object file
     * @param path        String, which String, which contains the return value of the getValue Method from
     *                    the Object path
     * @see StringAdder
     * @see StringTransfer
     */
    public void results(String resultzeile, String filename, String path) {
        // In Textdatei exportieren
        String ReportFilename = filename.replaceFirst("[.][^.]+$", "").concat("_Results.txt");        //Neuen Filenamen festlegen

        // Pfad festlegen
        String exportReport = path.concat(File.separator + ReportFilename);

        File reportfile = new File(exportReport);
        try {
            reportfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(reportfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter writeSummary = new BufferedWriter(fw);
        try {
            writeSummary.write("Results fuer den Scan: " + filename.replaceFirst("[.][^.]+$", "") + "\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeSummary.write(resultzeile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            writeSummary.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writeSummary.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The summary method creates and exports a textfile (.txt), which contains and exports the summary of the
     * analyzed results for all Scans.
     *
     * @param summaryStack   String, which contains the return value of the method getString() from the object
     *                       summaryStack.
     * @param path           String, which contains the return value of the method getValue() from the object path
     * @param todayTimeStamp String, which contains the return value of the method getCurrentTimeStamp() from the
     *                       object today.
     * @see StringAdder
     * @see StringTransfer
     * @see Timestamp
     */
    public void summary(String summaryStack, String path, String todayTimeStamp) {
        if (summaryStack != null)            //Error Handling
        {
            String logInhalt = summaryStack;

            // In Textdatei exportieren
            String ReportFilename = "Summary-Report.txt";        //Neuen Filenamen festlegen

            String exportReport = path + File.separator + ReportFilename;

            File file = new File(exportReport);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter writeSummary = new BufferedWriter(fw);
            try {
                // Kopfzeile einfuegen
                writeSummary.write("Programm von Stefan Erben und Andreas Mattes. (c) 2016\r\nSummary erstellt am: " + todayTimeStamp + "\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writeSummary.write(logInhalt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writeSummary.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writeSummary.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }
}
