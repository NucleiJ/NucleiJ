package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.converter.MainConverter;
import com.ezware.dialog.task.TaskDialog;
import ij.IJ;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConverterTask extends SwingWorker<String, Integer> {
    private static final Logger LOGGER = Logger.getLogger(AnalyzerTask.class.getName());
    private JProgressBar progressBar;
    private TaskDialog taskDialog;
    private NdpiConverter ndpiConverter;
    private List<String> ndpiFileList;
    private List<String> tifFileList;
    private ControllerTask controllerTask;


    public ConverterTask(JProgressBar progressBar, TaskDialog taskDialog, NdpiConverter ndpiConverter, ControllerTask controllerTask) {
        super();

        this.progressBar = progressBar;
        this.taskDialog = taskDialog;
        this.ndpiConverter = ndpiConverter;
        this.controllerTask = controllerTask;
    }

    protected String doInBackground() throws Exception {
        int i = 0;
        int add;
        int counter = 0;
        LOGGER.log(Level.INFO, "Converting Process started!");

        ControllerTask controllerTask = new ControllerTask();
        ndpiFileList = controllerTask.getNdpiList();

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

        controllerTask.addToList(filePath.replace(".ndpi", "_").concat(magnification).concat("_z0.tif"));
    }
}

/*

public class ConvertWorker extends SwingWorker<Integer, String> {

    private JProgressBar progressBar;
    private Converter standard;
    private JList listProgress;
    private Filechooser general;
    private DefaultListModel listModel;
    private Boolean singleMode;
    private JDialog ProgressDialog;
    private JDialog SummaryDialog;

    public ConvertWorker(JProgressBar progressBar, JList listProgress, Converter standard, Filechooser general, DefaultListModel listModel, Boolean singleMode, JDialog ProgressDialog, JDialog SummaryDialog)
    {
        super();
        this.standard = standard;
        this.general = general;
        this.listProgress = listProgress;
        this.progressBar = progressBar;
        this.listModel = listModel;
        this.singleMode = singleMode;
        this.ProgressDialog = ProgressDialog;
        this.SummaryDialog = SummaryDialog;
    }

    @Override
    protected Integer doInBackground() throws Exception
    {
        if(singleMode) {
            standard.setDir(general.chooser.getSelectedFile().getAbsolutePath());           //Set path of selected file
            System.out.println("Current filepath: " + general.chooser.getSelectedFile().getAbsolutePath());

            String filename = general.chooser.getSelectedFile().getName();
            publish(filename);

            standard.startConverter();
            setProgress(100);
        }

        else {
            File[] filesInDirectory = general.chooser.getCurrentDirectory().listFiles();
            int length = 0;

            for ( File file : filesInDirectory ) {
                if (file.isFile()) {
                    length ++;
                }
            }

            for ( File file : filesInDirectory ) {
                if (file.isFile()) {
                    System.out.println(file);

                    standard.setDir(file.getAbsolutePath());
                    System.out.println("Current filepath: " + file.getAbsolutePath());

                    standard.startConverter();
                    String filename = file.getName();
                    //listModel.addElement(filename);
                    publish(filename);
                    //progressBar.setValue(progressBar.getValue() + 100/length);
                    setProgress(progressBar.getValue() + 100/length);
                }

            }
        }

        return 1;
    }

    @Override
    protected void process(List<String> v) {
        listModel.addElement(""+v.get(v.size()-1));
        listProgress.ensureIndexIsVisible(listModel.size()-1);
    }

    @Override
    protected void done()
    {
        listModel.addElement("Finished");
        System.out.println("Convert process finished!");
        ProgressDialog.dispose();
        SummaryDialog.setVisible(true);
    }
}

 */