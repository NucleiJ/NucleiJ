package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.AppContext;
import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.NdpiConverter;
import at.ac.htlhl.nucleij.presenter.tasks.AnalyzerConverterTask;
import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialogs;
import com.jgoodies.binding.PresentationModel;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.plugin.frame.RoiManager;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
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

                if(GLScanAnalyzer.PROPERTY_CALCULATEANDSHOWHEATMAP.equals(evt.getPropertyName()))
                {
                    if(evt.getNewValue().equals(false))
                    {
                        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, false);
                    }
                }

                if(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY.equals(evt.getPropertyName()))
                {
                    setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, true);
                    glScanAnalyzer.setCalculateandshowheatmap(true);
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

            long startTime = System.nanoTime();

            JFrame parentAnalyzerConverter = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

            // ausführen der analyzeConvertMethode
            int numberNdpiFiles = ndpiConverter.getNumberNdpiFiles();
            int numberTifFiles = ndpiConverter.getNumberTifFiles();
            analyzeConvertCheck(numberNdpiFiles, numberTifFiles);

            //TODO: WICHTIG: Liste in glscanner model erstellen, nach analyze

            TaskDialog taskDialogAnalyzerConverter = new TaskDialog(parentAnalyzerConverter, bundle.getString("AnalyzerConverterDialog.title"));

            JProgressBar progressBarAnalyzerConverter = new JProgressBar(0,100);
            progressBarAnalyzerConverter.setStringPainted(true);
            progressBarAnalyzerConverter.setValue(0);
            taskDialogAnalyzerConverter.setInstruction(bundle.getString("AnalyzerConverterDialog.instructionMessage"));
            taskDialogAnalyzerConverter.setText(bundle.getString("AnalyzerConverterDialog.text"));
            taskDialogAnalyzerConverter.setResizable(true);

            taskDialogAnalyzerConverter.setFixedComponent(progressBarAnalyzerConverter);
            taskDialogAnalyzerConverter.setCommands(TaskDialog.StandardCommand.CANCEL);

            AnalyzerConverterTask analyzerConverterTask = new AnalyzerConverterTask(progressBarAnalyzerConverter, taskDialogAnalyzerConverter, ndpiConverter, glScanAnalyzer);
            analyzerConverterTask.execute();

            //taskDialogAnalyzerConverter.show();
            TaskDialog.Command erg = taskDialogAnalyzerConverter.show();
            System.out.println("ERG:::" + erg);

            if (erg.toString() == "CANCEL")
            {
                //taskDialogAnalyzerConverter.setVisible(false);
                analyzerConverterTask.cancel(true);
            }

            long elapsedTime = System.nanoTime() - startTime;
            double seconds = (double) elapsedTime / 1000000000.0;
            double minutes = 0;
            if (seconds > 59)
            {
                minutes = seconds / 60;
                seconds = seconds % 60;
            }

            String processDuration;
            if (minutes != 0)
            {
                processDuration = (int) minutes + " " + bundle.getString("Words.minutes.text") + " " +
                        (int) seconds + " " + bundle.getString("Words.sekunds.text");
            }
            else
            {
                processDuration = (int) seconds + " " + bundle.getString("Words.sekunds.text");
            }

            String listString = "";
            for (String s : glScanAnalyzer.getTifList())
            {
                listString += s.concat("\n");
            }

            System.out.println(listString);

            String InfosOfProcessedScans;
            if (ndpiConverter.getNumberNdpiFiles() != 0  && ndpiConverter.getNumberTifFiles() == 0)
            {
                InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.KonvertierteDateien.text") + ": </b>" + ndpiConverter.getNumberNdpiFiles();
            }
            else if (ndpiConverter.getNumberNdpiFiles() == 0  && ndpiConverter.getNumberTifFiles() != 0)
            {
                InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.AnalysierteDateien.text") + ": </b>" + ndpiConverter.getNumberTifFiles();
            }
            else
            {
                InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.KonvertierteDateien.text") + ": </b>" + ndpiConverter.getNumberNdpiFiles() +
                        "\n<b>" + bundle.getString("Words.AnalysierteDateien.text") + ": </b>" + (ndpiConverter.getNumberTifFiles() + ndpiConverter.getNumberNdpiFiles() );
            }




            String[] columnNamesGeneral = {"Titel",
                    "Information"};
            Object[][] dataGeneral = {
                    {bundle.getString("Words.Ausgabepfad.text") + ":", ndpiConverter.getOutputpath()},
                    //{"Ausgabepfad:", ndpiConverter.getOutputpath(), new Integer(5), new Boolean(false)},
                    {bundle.getString("Words.Dauer.text") + ":", processDuration}, { bundle.getString("Words.KonvertierteDateien.text") +": ", ndpiConverter.getNumberNdpiFiles() },
                    {bundle.getString("Words.AnalysierteDateien.text") + ":", (ndpiConverter.getNumberTifFiles() + ndpiConverter.getNumberNdpiFiles() )},
            };

            String[] columnNamesSpezific = { "Information"};
            Object[][] dataSpezific = {

            };

            DefaultTableModel modelSpezific = new DefaultTableModel(dataSpezific, columnNamesSpezific);
            JTable tableSpezific = new JTable(modelSpezific);

            //DefaultTableModel modelGeneral = new DefaultTableModel(dataGeneral, columnNamesGeneral);
            //JTable tableGeneral = new JTable(modelGeneral);

            int i = 0;
            for (String ndpiElement : glScanAnalyzer.getNdpiList())
            {
                if(i == 0)
                {
                    modelSpezific.insertRow(modelSpezific.getRowCount() ,new Object[]{"<html><b>" + bundle.getString("Words.KonvertierteDateien.text") + ":</b></html>"});
                }
                modelSpezific.insertRow(modelSpezific.getRowCount() ,new Object[]{ndpiElement});
                i++;
            }
            i = 0;
            for (String tifElement : glScanAnalyzer.getTifList())
            {
                if(i == 0)
                {
                    modelSpezific.insertRow(modelSpezific.getRowCount() ,new Object[]{""});
                    modelSpezific.insertRow(modelSpezific.getRowCount() ,new Object[]{"<html><b>" + bundle.getString("Words.AnalysierteDateien.text") + ":</b></html>"});
                }
                modelSpezific.insertRow(modelSpezific.getRowCount() ,new Object[]{tifElement});
                i++;
            }

            TableColumn column = tableSpezific.getColumnModel().getColumn(0);
            column.setPreferredWidth(700);

            // Summary Dialog:
            TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), bundle.getString("Words.Zusammenfassung.text") );
            dlg.setIcon( TaskDialog.StandardIcon.INFO );
            dlg.setText( "<b>" + bundle.getString("Words.Ausgabepfad.text") + ":</b> " + ndpiConverter.getOutputpath() +
                    "\n<b>" + bundle.getString("Words.Dauer.text") + ":</b> " + processDuration + "\n" + InfosOfProcessedScans);

            dlg.getDetails().setExpandableComponent(
                    tableSpezific
                    //https://www.google.at/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=java+jtable+string+array
                    //new JLabel("Liste:\n" + listString )
            );

            dlg.getFooter().setText( "\u00A9 NucleiJ 2017");
            dlg.getFooter().setIcon( TaskDialog.StandardIcon.INFO );

            dlg.show();
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

            ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

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

                        new WaitForUserDialog("Information", bundle.getString("ROIinfo.info.text") ).show();

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
                            TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), "Error" );
                            dlg.setIcon( TaskDialog.StandardIcon.ERROR );
                            dlg.setText( bundle.getString("ROIerror.info.text") );
                            dlg.getDetails().setExpandableComponent(
                                    new JLabel(bundle.getString("ROIerror.details.text") + String.valueOf(roiRec.getHeight()) + "px * " + String.valueOf(roiRec.getWidth()) +"px."));
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
                dlg.setInstruction(bundle.getString("ROInotavialbe.instruction.text"));
                dlg.setText( bundle.getString("ROInotavialbe.info.text") );
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

    public void analyzeConvertCheck (int numberNdpiFiles, int numberTifFiles) {
        JFrame parentDialog = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        int choice;
        if (numberNdpiFiles > 0 && numberTifFiles > 0) {
            choice = TaskDialogs.radioChoice(parentDialog,
                    "It seems that you selected both NDPI & TIF Files" ,
                    "NDPI-Files: \t" + numberNdpiFiles + "\nTIF-Files: \t" + numberTifFiles + "\n\nWhat do you want do?" ,
                    0,
                    "Convert & Analyze", "Convert Only", "Analyze Only" );
            ndpiConverter.setChoice(choice);
        }
        else if (numberNdpiFiles > 0 && numberTifFiles == 0) { //Only NDPI Selected
            choice = TaskDialogs.radioChoice(parentDialog,
                    "It seems that your selection is a Ndpi File",
                    "What will you do?",
                    0,
                    "Convert & Analyze", "Convert Only" );
            ndpiConverter.setChoice(choice);
        }
        else if (numberNdpiFiles == 0 && numberTifFiles > 0) {
            // Wird nur Konvertiert
            ndpiConverter.setChoice(2);
        }
    }

}
