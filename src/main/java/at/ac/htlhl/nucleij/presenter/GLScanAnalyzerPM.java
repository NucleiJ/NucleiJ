package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.AnalyzerConverterTask;
import com.ezware.dialog.task.TaskDialog;
import com.jgoodies.binding.PresentationModel;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.plugin.frame.RoiManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static at.ac.htlhl.nucleij.model.NdpiConverter.MAG_X10;
import static at.ac.htlhl.nucleij.model.NdpiConverter.MAG_X5;


/**
 *
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzerPM extends PresentationModel<GLScanAnalyzer> {
    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(GLScanAnalyzerPM.class.getName());
    //public static final String FILE_EXTENSION			= "nucleij";


    /*private Action selectpathAction;
    private Action typeAction;
    private Action outputpathAction;*/

    private Action analyzeAction;
    private Action calculateandshowheatmapAction;
    private Action setroiAction;
    private Action deleteroiAction;

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter ndpiConverter;

    /*private String inputpathString;
    private String outputpathString;*/

    public GLScanAnalyzerPM(GLScanAnalyzer glScanAnalyzer, NdpiConverter ndpiConverter) {
        super(glScanAnalyzer);

        this.glScanAnalyzer = glScanAnalyzer;
        this.ndpiConverter = ndpiConverter;

        analyzeAction = new AnalyzeAction();
        calculateandshowheatmapAction = new CalculateandshowheatmapAction();
        setroiAction = new SetroiAction();
        deleteroiAction = new DeleteroiAction();

        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, getBean().isCalculateandshowheatmap());

        setComponentEnabled(GLScanAnalyzer.PROPERTY_SETROI, glScanAnalyzer.isSetroi());
        setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, false);

        // Ausgabe jeder Aenderung
        glScanAnalyzer.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());

                if(GLScanAnalyzer.PROPERTY_SETROI.equals(evt.getPropertyName()))
                {
                    if(evt.getNewValue().equals(false))
                    {
                        System.out.println("Property ist false");
                    }
                }
            }


        });



    }

    public Action getAnalyzeAction() {
        return analyzeAction;
    }

    public Action getDeleteroiAction() {
        return deleteroiAction;
    }

    public Action getCalculateandshowheatmapAction()
    {
        return calculateandshowheatmapAction;
    }

    public Action getSetroiAction()
    {
        return setroiAction;
    }

    private class AnalyzeAction extends AbstractAction {
        public AnalyzeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            JFrame parentAnalyzerConverter = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle resourceBundleAnalyzer = AppContext.getInstance().getResourceBundle();


            //TODO: WICHTIG: Liste in glscanner model erstellen, nach analyze

            TaskDialog taskDialogAnalyzerConverter = new TaskDialog(parentAnalyzerConverter, resourceBundleAnalyzer.getString("AnalyzerConverterDialog.title"));

            JProgressBar progressBarAnalyzerConverter = new JProgressBar(0,100);
            progressBarAnalyzerConverter.setStringPainted(true);
            progressBarAnalyzerConverter.setValue(0);
            taskDialogAnalyzerConverter.setInstruction(resourceBundleAnalyzer.getString("AnalyzerConverterDialog.instructionMessage"));
            taskDialogAnalyzerConverter.setText(resourceBundleAnalyzer.getString("AnalyzerConverterDialog.text"));
            taskDialogAnalyzerConverter.setResizable(true);

            taskDialogAnalyzerConverter.setFixedComponent(progressBarAnalyzerConverter);
            taskDialogAnalyzerConverter.setCommands(TaskDialog.StandardCommand.CANCEL);

            AnalyzerConverterTask analyzerConverterTask = new AnalyzerConverterTask(progressBarAnalyzerConverter, taskDialogAnalyzerConverter, ndpiConverter, glScanAnalyzer);
            analyzerConverterTask.execute();
            taskDialogAnalyzerConverter.show();
        }
    }

    private class CalculateandshowheatmapAction extends AbstractAction
    {
        public CalculateandshowheatmapAction()
        {
            super();
        }

        public void actionPerformed(ActionEvent e) {

            // Radiobox toggled von selber, Domainobject aendert sich von selber
            glScanAnalyzer.setCalculateandshowheatmap(glScanAnalyzer.isCalculateandshowheatmap());
        }
    }

    private class DeleteroiAction extends AbstractAction
    {
        public DeleteroiAction()
        {
            super();
        }
        public void actionPerformed(ActionEvent e){
            glScanAnalyzer.setRoiarea("");
        }
    }

    private class SetroiAction extends AbstractAction
    {
        public SetroiAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {

            if(ndpiConverter.getInputpath() != null && ndpiConverter.getInputpath() != "" &&
                    ndpiConverter.getInputpath().contains(".tif") && ndpiConverter.getNumberTifFiles() == 1 &&
                    ndpiConverter.getNumberNdpiFiles() == 0)
            {
                Runnable myRunnable = new Runnable()
                {
                    public void run() {
                        System.out.println("Runnable running");

                        glScanAnalyzer.setRoiX(0);
                        glScanAnalyzer.setRoiY(0);
                        glScanAnalyzer.setRoiHeight(0);
                        glScanAnalyzer.setRoiWidth(0);
                        System.out.println("ROI Infos zurueckgesetzt..");

                        ImagePlus imp = IJ.openImage(ndpiConverter.getInputpath());
                        imp.unlock();
                        imp.show();

                        RoiManager roiMng = RoiManager.getInstance();
                        try {
                            roiMng.setEditMode(imp, true);
                        } catch (Exception e1) {
                            System.out.println("Probleme bei dem editMode true setzten..\n");
                        }

                        boolean roigesetzt = false;
                        //System.out.println("In Schleife ");
                        imp.updateAndRepaintWindow();

                        Roi roi;

                        do {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            //System.out.println("suche ROI...");
                            roi = imp.getRoi();
                            if (roi instanceof Roi) {
                                roigesetzt = true;
                                System.out.println("User setzt ROI");
                            }
                        }while (roigesetzt == false);

                        new WaitForUserDialog("Information", "OK drücken, wenn die\nROI ausgewählt wurde").show();

                        Rectangle roiRec = roi.getBounds();

                        if ( roiRec.getWidth() > 20 && roiRec.getHeight() > 20)
                        {
                            System.out.println("Folgende ROI wurde erkannt:\n" + roiRec.getX()  + "\n" + roiRec.getY() + "\n"  + roiRec.getWidth() + "\n" + roiRec.getHeight() );

                            glScanAnalyzer.setRoiX((int) roiRec.getX());
                            glScanAnalyzer.setRoiY((int) roiRec.getY());
                            glScanAnalyzer.setRoiHeight((int) roiRec.getHeight());
                            glScanAnalyzer.setRoiWidth((int) roiRec.getWidth());
                            glScanAnalyzer.setSetroi(true);
                        }
                        else
                        {
                            TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), "Application Error" );
                            dlg.setIcon( TaskDialog.StandardIcon.ERROR );
                            dlg.setText( "Diese Auswahl der Region of Interest ist nicht erlaubt.\nKlicken Sie für weitere Informationen auf \"weitere Details\". " );
                            dlg.getDetails().setExpandableComponent(
                                    new JLabel(" ROI zu klein gewählt. Muss mindestens 20 pixel Seitenlänge haben.\n " +
                                            "Ihre Auswahl: " + String.valueOf(roiRec.getHeight()) + "px * " + String.valueOf(roiRec.getWidth()) +"px."));
                            dlg.show();
                        }

                        imp.close();
                    }
                };

                Thread thread = new Thread(myRunnable);
                thread.start();

            }else
            {
                TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(),"Warnung");
                dlg.setInstruction("ROI Funktion nicht verfügbar");
                dlg.setText( "Die ROI Funktion ist nur verfügbar, " +
                        "wenn sich im\nEingabepfad nur <b>eine</b> Datei befindet!" );
                dlg.setIcon( TaskDialog.StandardIcon.WARNING );
                dlg.show();
            }
            //glScanAnalyzer.setSelectroi(glScanAnalyzer.isSelectroi());
            //System.out.println(glScanAnalyzer.isSelectroi());
        }
    }

    //region additional Methods for simple Funtions:
    private static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(null!=files){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }

    //endregion:
}
