package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.analyzing.MainAnalyzer;
import com.ezware.dialog.task.TaskDialog;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andreas on 23.01.17.
 */
public class AnalyzerConverterTask extends SwingWorker<String, Integer>
{
    private static final Logger LOGGER = Logger.getLogger(AnalyzerConverterTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;
    private List<String> ndpiFileList;
    private List<String> tifFileList;

    public AnalyzerConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, GLScanAnalyzer glScanAnalyzer)
    {
        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.glScanAnalyzer = glScanAnalyzer;
    }


    @Override
    protected String doInBackground() throws Exception {
        MainAnalyzer mainAnalyzer = new MainAnalyzer(glScanAnalyzer);
        tifFileList = glScanAnalyzer.getTifList();
        ndpiFileList = glScanAnalyzer.getNdpiList();
        int i = 0;
        int add;
        int counter = 0;

        for (String tifListElement : tifFileList) {
            counter++;
        }

        for (String ndpiListElement : ndpiFileList) {
            counter++;
        }

        add = counter/100;

        for (String ndpiListElement : ndpiFileList) {
            startConverter(ndpiListElement);
            publish(i+add);
        }

        for (String tifListElement : tifFileList)
        {
            mainAnalyzer.setDateiname(tifListElement);
            System.out.println("\n****************\n"+tifListElement+"\n********************");
            mainAnalyzer.run(null);
            publish(i+add);
        }
        mainAnalyzer.createSummary();
        return "Finished";
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
        progressBar.setValue(100);
        taskDialog.setVisible(false);
    }




    private void startConverter(String filePath) {
        //Test fuer Consoleneingabe:

        File outputpath = new File(ndpiConverter.getOutputpath());

        File file = new File("lib/ndpi-converter/ndpi-converter.jar");
        String absolutePathofNdpiJar = file.getAbsolutePath();
        System.out.println(absolutePathofNdpiJar);

        try {
            Process p;
            if (ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X10)) {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            }
            else if(ndpiConverter.getMagnification().equals(NdpiConverter.MAG_X40)) {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 1 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
            }
            else {
                p = Runtime.getRuntime().exec("java -jar \"" + absolutePathofNdpiJar + "\" -i 2 -c lzw -s \"" + filePath +"\" \"" + outputpath.getParent().toString() + "\"");
                LOGGER.info("Magnification error: set to Std. 10x Magnification");
            }

            //Process p = Runtime.getRuntime().exec("java -jar " + absolutePathofNdpiJar + " -i 2 -c lzw -s \"" + filePath);
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

        // TODO neue files in tif liste

        String renameFileName = "_".concat(ndpiConverter.getMagnification().toLowerCase().concat(".ome.tif"));
        String newTifListElement = filePath.replace(".ndpi", renameFileName);
        System.out.println("Der Filename nach dem Konvertieren ist:" + newTifListElement);
        glScanAnalyzer.addTifToList(newTifListElement);
    }
}
