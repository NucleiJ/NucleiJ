package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.ezware.dialog.task.TaskDialog;
import ij.IJ;

import javax.swing.*;
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
        String magnification = ndpiConverter.getMagnification();
        //String directory = ndpiConverter.getInputpath();
        String command = ("no=[%]").replace("%", filePath);
        command += " format_of_split_images=[TIFF with LZW compression] make_mosaic=never mosaic_pieces_format=[TIFF with JPEG compression] requested_jpeg_compression=75 mosaic_pieces_overlap=0.000000 mosaic_pieces_overlap_unit=pixels size_limit_on_each_mosaic_piece=1024 width_of_each_mosaic_piece_in_pixels=0 height_of_each_mosaic_piece_in_pixels=0 ";
        magnification = "extract_images_at_magnification_%".replace("%", magnification);
        command = command + magnification + " extract_images_with_z-offset_0";

        IJ.run("Custom extract to TIFF / Mosaic...", command);

        // WICHTIG, wenn hier .tif angehaengt wird dann findet der analyzer die datei, kann sie aber nicht abarbeiten
        // es funktioniert zwar alles, aber die progress bar passt dann nicht mehr. Deswegen absichtlich hier falsche: .tiff
        // ebenfalls ist das wait nur da, um die dauer fuer den konvertiervorgang zu simulieren
        try {
            Thread.sleep(1000);                 //1000 milliseconds
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        glScanAnalyzer.addTifToList(filePath.replace(".ndpi", "_").concat(magnification).concat("_z0.tiff"));
    }
}
