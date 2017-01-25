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

        /*selectpathAction = new SelectpathAction();
        typeAction = new TypeAction();
        outputpathAction = new OutputpathAction();*/

        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, getBean().isCalculateandshowheatmap());
        //setComponentEnabled(GLScanAnalyzer.PROPERTY, getBean().isCalculateandshowheatmap());

        setComponentEnabled(GLScanAnalyzer.PROPERTY_ROIAREA, false);
        setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, false);

        // Ausgabe jeder Aenderung
        glScanAnalyzer.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());

                if(GLScanAnalyzer.PROPERTY_SETROI.equals(evt.getPropertyName())) {
                    boolean enabled = evt.getNewValue().toString().toLowerCase().equals("true");
                    setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, enabled);
                }
            }
        });


    }

    public void setROIvisible(boolean enabled)
    {
        setComponentVisible(GLScanAnalyzer.PROPERTY_ROIAREA, enabled);
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

            if(ndpiConverter.getInputpath() != null && ndpiConverter.getInputpath() != "")
            {
                Runnable myRunnable = new Runnable()
                {
                    public void run() {
                        System.out.println("Runnable running");

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
                            System.out.println("suche ROI...");
                            roi = imp.getRoi();
                            if (roi instanceof Roi) {
                                roigesetzt = true;
                                System.out.println("User setzt ROI");
                            }
                        }while (roigesetzt == false);

                        new WaitForUserDialog("Information", "ROI selected").show();

                        Rectangle roiRec = roi.getBounds();
                        System.out.println("Folgende ROI wurde erkannt:\n" + roiRec.getX()  + "\n" + roiRec.getY() + "\n"  + roiRec.getWidth() + "\n" + roiRec.getHeight() );

                        imp.close();
                    }
                };

                Thread thread = new Thread(myRunnable);
                thread.start();

            }else
            {
                //TODO ERROR DIALOG
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
