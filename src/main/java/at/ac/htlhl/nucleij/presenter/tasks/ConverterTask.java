package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.ezware.dialog.task.TaskDialog;
import ij.IJ;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConverterTask extends SwingWorker<String, Integer> {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private NdpiConverter ndpiConverter;
    private GLScanAnalyzer glScanAnalyzer;
    private List<String> ndpiFileList;
    private List<String> tifFileList;


    public ConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer) {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
    }

    protected String doInBackground() throws Exception {
        int i = 0;
        int add;
        int counter = 0;
        LOGGER.log(Level.INFO, "Converting Process started!");

        ndpiFileList = glScanAnalyzer.getNdpiList();

        for (String ndpiListElement : ndpiFileList) {
            counter++;
        }
        add = 100/counter;

        for (String ndpiListElement : ndpiFileList) {
            startConverter(ndpiListElement);
            publish(i+add);
        }

        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks);
        for (int value : chunks) {
            progressBar.setValue(value);
        }
    }

    @Override
    protected void done() {
        super.done();
        LOGGER.log(Level.INFO, "Done");
        taskDialog.setVisible(false);

    }

    private void startConverter(String filePath) {
        //Test fuer Consoleneingabe:
        //Dateinamen:
        Path fileName = Paths.get(filePath);

        File outputpath = new File(ndpiConverter.getOutputpath());

        File file = new File("lib/ndpi-converter/ndpi-converter.jar");
        String absolutePathofNdpiJar = file.getAbsolutePath();
        System.out.println(absolutePathofNdpiJar);

        try {
            //Process p = Runtime.getRuntime().exec("java -jar " + absolutePathofNdpiJar + " -i 2 -c lzw -s \"" + filePath);
            Process p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            //Process p = Runtime.getRuntime().exec("java -jar \" "+ absolutePathofNdpiJar +"\" -i 2 -c lzw -s \"C:\\Users\\Stefan\\Desktop\\Medizin Projekt\\Bilder\\stapel\\test.ndpi\" \"C:\\Users\\Stefan\\Desktop\\Medizin Projekt\\Bilder\\stapel\" ");
            //java -jar "C:\Users\Stefan\Downloads\ndpi-to-ome-tiff-converter-v1.5\ndpi-converter.jar" -i 2 -c lzw -s "C:\Users\Stefan\Desktop\Medizin Projekt\Bilder\stapel\test.ndpi" "C:\Users\Stefan\Desktop\Medizin Projekt\Bilder\stapel"

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        String magnification = ndpiConverter.getMagnification();
        //String directory = ndpiConverter.getInputpath();
        String command = ("no=[%]").replace("%", filePath);
        command += " format_of_split_images=[TIFF with LZW compression] make_mosaic=never mosaic_pieces_format=[TIFF with JPEG compression] requested_jpeg_compression=75 mosaic_pieces_overlap=0.000000 mosaic_pieces_overlap_unit=pixels size_limit_on_each_mosaic_piece=1024 width_of_each_mosaic_piece_in_pixels=0 height_of_each_mosaic_piece_in_pixels=0 ";
        magnification = "extract_images_at_magnification_%".replace("%", magnification);
        command = command + magnification + " extract_images_with_z-offset_0";

        IJ.run("Custom extract to TIFF / Mosaic...", command);
        */

        // TODO Die files liegen im ordner converted. von da in den parent ordner verschieben. danach leeren ordner loeschen. neue files in tif liste


        System.out.println("Der Filename nach dem Konvertieren ist:" + fileName.getFileName().toString());

    }
}
