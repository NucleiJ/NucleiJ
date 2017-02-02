package at.ac.htlhl.nucleij.presenter;

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
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @author Stefan Erben
 * @version 1.0
 */
public class GLScanAnalyzerPM extends PresentationModel<GLScanAnalyzer> {

    // Constants
    // ************************************************************************
    private static final Logger LOGGER = Logger.getLogger(GLScanAnalyzerPM.class.getName());

    private Action analyzeAction;
    private Action calculateandshowheatmapAction;
    private Action setroiAction;
    private Action deleteroiAction;

    private GLScanAnalyzer glScanAnalyzer;
    private NdpiConverter  ndpiConverter;

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
                LOGGER.info("Property name=" + evt.getPropertyName() + ", oldValue=" + evt.getOldValue() + ", newValue=" + evt.getNewValue());

                if (GLScanAnalyzer.PROPERTY_CALCULATEANDSHOWHEATMAP.equals(evt.getPropertyName())) {
                    if (evt.getNewValue().equals(false)) {
                        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, false);
                    } else {
                        setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, true);
                    }
                }

                if (GLScanAnalyzer.PROPERTY_HEATMAPQUALITY.equals(evt.getPropertyName())) {
                    setComponentEnabled(GLScanAnalyzer.PROPERTY_HEATMAPQUALITY, true);
                    glScanAnalyzer.setCalculateandshowheatmap(true);
                }
            }
        });
    }

    public void analyzeConvertCheck(int numberNdpiFiles, int numberTifFiles) {
        JFrame parentDialog = ((SingleFrameApplication) Application.getInstance()).getMainFrame();

        int choice;
        if (numberNdpiFiles > 0 && numberTifFiles > 0) {
            choice = TaskDialogs.radioChoice(parentDialog,
                    "It seems that you selected both NDPI & TIF Files", //TODO Texte auslagern
                    "NDPI-Files: \t" + numberNdpiFiles + "\nTIF-Files: \t" + numberTifFiles + "\n\nWhat do you want do?",
                    0,
                    "Convert & Analyze", "Convert Only", "Analyze Only");
            ndpiConverter.setChoice(choice);
        } else if (numberNdpiFiles > 0 && numberTifFiles == 0) { //Only NDPI Selected
            choice = TaskDialogs.radioChoice(parentDialog,
                    "It seems that your selection is a Ndpi File",
                    "What will you do?",
                    0,
                    "Convert & Analyze", "Convert Only");
            ndpiConverter.setChoice(choice);
        } else if (numberNdpiFiles == 0 && numberTifFiles > 0) {
            ndpiConverter.setChoice(2);
        }
    }

    public Action getAnalyzeAction() {
        return analyzeAction;
    }

    public Action getDeleteroiAction() {
        return deleteroiAction;
    }

    public Action getCalculateandshowheatmapAction() {
        return calculateandshowheatmapAction;
    }

    public Action getSetroiAction() {
        return setroiAction;
    }


    //region Getter

    private class AnalyzeAction extends AbstractAction {
        public AnalyzeAction() {
        }

        public void actionPerformed(ActionEvent e) {

            long startTime = System.nanoTime();

            JFrame parentAnalyzerConverter = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
            ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

            int numberNdpiFiles = ndpiConverter.getNumberNdpiFiles();
            int numberTifFiles = ndpiConverter.getNumberTifFiles();
            analyzeConvertCheck(numberNdpiFiles, numberTifFiles);

            if (ndpiConverter.getChoice() == -1) {
                return;
            }

            TaskDialog taskDialogAnalyzerConverter = new TaskDialog(parentAnalyzerConverter, bundle.getString("AnalyzerConverterDialog.title"));

            JProgressBar progressBarAnalyzerConverter = new JProgressBar(0, 100);
            progressBarAnalyzerConverter.setStringPainted(true);
            progressBarAnalyzerConverter.setValue(0);

            taskDialogAnalyzerConverter.setInstruction(bundle.getString("AnalyzerConverterDialog.instructionMessage"));
            taskDialogAnalyzerConverter.setText(bundle.getString("AnalyzerConverterDialog.text"));
            taskDialogAnalyzerConverter.setResizable(true);

            taskDialogAnalyzerConverter.setFixedComponent(progressBarAnalyzerConverter);
            taskDialogAnalyzerConverter.setCommands(TaskDialog.StandardCommand.CANCEL);

            AnalyzerConverterTask analyzerConverterTask = new AnalyzerConverterTask(progressBarAnalyzerConverter, taskDialogAnalyzerConverter, ndpiConverter, glScanAnalyzer);
            analyzerConverterTask.execute();

            TaskDialog.Command erg = taskDialogAnalyzerConverter.show();

            if (erg.toString() == "CANCEL") {
                analyzerConverterTask.stopProcess(true);

                if (!analyzerConverterTask.isDone()) {
                    TaskDialog dlg = new TaskDialog(parentAnalyzerConverter, "Beenden..."); // TODO texte auslagern
                    dlg.setInstruction("Vorgang wird abgebrochen...");
                    dlg.setText("NucleiJ beendet den Vorgang. Dieser wird in den naechsten\n" +
                            "Momenten selbststaendig im Hintergrund abgebrochen. Bitte OK druecken");
                    dlg.setIcon(TaskDialog.StandardIcon.WARNING);
                    dlg.show();
                    ndpiConverter.setNumberNdpiFiles(0);
                    ndpiConverter.setNumberTifFiles(0);
                }

            }

            long elapsedTime = System.nanoTime() - startTime;
            double seconds = (double) elapsedTime / 1000000000.0;
            double minutes = 0;
            if (seconds > 59) {
                minutes = seconds / 60;
                seconds = seconds % 60;
            }

            String processDuration;
            if (minutes != 0) {
                processDuration = (int) minutes + " " + bundle.getString("Words.minutes.text") + " " +
                        (int) seconds + " " + bundle.getString("Words.sekunds.text");
            } else {
                processDuration = (int) seconds + " " + bundle.getString("Words.sekunds.text");
            }

            String listString = "";
            for (String s : glScanAnalyzer.getTifList()) {
                listString += s.concat("\n");
            }

            String InfosOfProcessedScans = "Prozess wurde waerend der Programmlaufzeit abgebrochen!";       //TODO Texte auslagern
            if ((ndpiConverter.getNumberTifFiles() + ndpiConverter.getNumberNdpiFiles()) > 0) {
                if (ndpiConverter.getNumberNdpiFiles() != 0 && ndpiConverter.getNumberTifFiles() == 0) {
                    InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.KonvertierteDateien.text") + ": </b>" + ndpiConverter.getNumberNdpiFiles();
                } else if (ndpiConverter.getNumberNdpiFiles() == 0 && ndpiConverter.getNumberTifFiles() != 0) {
                    InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.AnalysierteDateien.text") + ": </b>" + ndpiConverter.getNumberTifFiles();
                } else {
                    InfosOfProcessedScans = "\n<b>" + bundle.getString("Words.KonvertierteDateien.text") + ": </b>" + ndpiConverter.getNumberNdpiFiles() +
                            "\n<b>" + bundle.getString("Words.AnalysierteDateien.text") + ": </b>" + (ndpiConverter.getNumberTifFiles() + ndpiConverter.getNumberNdpiFiles());
                }
            }

            String[] columnNamesSpezific = {"Information"};
            Object[][] dataSpezific = {
            };

            DefaultTableModel modelSpezific = new DefaultTableModel(dataSpezific, columnNamesSpezific);
            JTable tableSpezific = new JTable(modelSpezific);

            int i = 0;
            for (String ndpiElement : glScanAnalyzer.getNdpiList()) {
                if (i == 0) {
                    modelSpezific.insertRow(modelSpezific.getRowCount(), new Object[]{"<html><b>" + bundle.getString("Words.KonvertierteDateien.text") + ":</b></html>"});
                }
                modelSpezific.insertRow(modelSpezific.getRowCount(), new Object[]{ndpiElement});
                i++;
            }

            i = 0;
            for (String tifElement : glScanAnalyzer.getTifList()) {
                if (i == 0) {
                    modelSpezific.insertRow(modelSpezific.getRowCount(), new Object[]{""});
                    modelSpezific.insertRow(modelSpezific.getRowCount(), new Object[]{"<html><b>" + bundle.getString("Words.AnalysierteDateien.text") + ":</b></html>"});
                }
                modelSpezific.insertRow(modelSpezific.getRowCount(), new Object[]{tifElement});
                i++;
            }

            TableColumn column = tableSpezific.getColumnModel().getColumn(0);
            column.setPreferredWidth(700);

            // Summary Dialog:
            TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), bundle.getString("Words.Zusammenfassung.text"));
            dlg.setIcon(TaskDialog.StandardIcon.INFO);

            if ((ndpiConverter.getNumberTifFiles() + ndpiConverter.getNumberNdpiFiles()) > 0) {
                dlg.setText("<b>" + bundle.getString("Words.Ausgabepfad.text") + ":</b> " + ndpiConverter.getOutputpath() +
                        "\n<b>" + bundle.getString("Words.Dauer.text") + ":</b> " + processDuration + "\n" + InfosOfProcessedScans);
                dlg.getDetails().setExpandableComponent(tableSpezific);
                dlg.getFooter().setText("\u00A9 NucleiJ 2017");
                dlg.getFooter().setIcon(TaskDialog.StandardIcon.INFO);
            } else {
                dlg.setText("<b>" + bundle.getString("Words.Dauer.text") + ":</b> " + processDuration + "\n" + InfosOfProcessedScans);

            }
            dlg.show();
        }
    }

    private class SetroiAction extends AbstractAction {
        public SetroiAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {

            ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

            if (ndpiConverter.getInputpath() != null && ndpiConverter.getInputpath() != "" &&
                    ndpiConverter.getInputpath().contains(".tif") && ndpiConverter.getNumberTifFiles() == 1 &&
                    ndpiConverter.getNumberNdpiFiles() == 0) {
                Runnable myRunnable = () -> {
                    glScanAnalyzer.setRoiX(0);
                    glScanAnalyzer.setRoiY(0);
                    glScanAnalyzer.setRoiHeight(0);
                    glScanAnalyzer.setRoiWidth(0);

                    ImagePlus imp = IJ.openImage(ndpiConverter.getInputpath());
                    imp.unlock();
                    imp.show();

                    RoiManager roiMng = RoiManager.getInstance();
                    try {
                        roiMng.setEditMode(imp, true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    boolean roigesetzt = false;
                    imp.updateAndRepaintWindow();

                    Roi roi;

                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        roi = imp.getRoi();
                        if (roi instanceof Roi) {
                            roigesetzt = true;
                        }
                    } while (roigesetzt == false);

                    new WaitForUserDialog("Information", bundle.getString("ROIinfo.info.text")).show();

                    Rectangle roiRec = roi.getBounds();

                    if (roiRec.getWidth() > 20 && roiRec.getHeight() > 20) {
                        glScanAnalyzer.setRoiX((int) roiRec.getX());
                        glScanAnalyzer.setRoiY((int) roiRec.getY());
                        glScanAnalyzer.setRoiHeight((int) roiRec.getHeight());
                        glScanAnalyzer.setRoiWidth((int) roiRec.getWidth());
                        glScanAnalyzer.setSetroi(true);
                    } else {
                        TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), "Error");
                        dlg.setIcon(TaskDialog.StandardIcon.ERROR);
                        dlg.setText(bundle.getString("ROIerror.info.text"));
                        dlg.getDetails().setExpandableComponent(
                                new JLabel(bundle.getString("ROIerror.details.text") + String.valueOf(roiRec.getHeight())
                                        + "px * " + String.valueOf(roiRec.getWidth()) + "px."));
                        dlg.show();
                    }
                    imp.close();
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            } else {
                TaskDialog dlg = new TaskDialog(((SingleFrameApplication) Application.getInstance()).getMainFrame(), "Warnung");
                dlg.setInstruction(bundle.getString("ROInotavialbe.instruction.text"));
                dlg.setText(bundle.getString("ROInotavialbe.info.text"));
                dlg.setIcon(TaskDialog.StandardIcon.WARNING);
                dlg.show();
            }
        }
    }

    private class CalculateandshowheatmapAction extends AbstractAction {
        public CalculateandshowheatmapAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            glScanAnalyzer.setCalculateandshowheatmap(glScanAnalyzer.isCalculateandshowheatmap());
        }
    }

    private class DeleteroiAction extends AbstractAction {
        public DeleteroiAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            glScanAnalyzer.setRoiarea("");
        }
    }
    //endregion:

}
