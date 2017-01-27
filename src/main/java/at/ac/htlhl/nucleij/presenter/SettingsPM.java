package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Settings;
import at.ac.htlhl.nucleij.view.SettingsView;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.jgoodies.binding.PresentationModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * Created by andreas on 25.01.2017.
 */
public class SettingsPM extends PresentationModel<Settings>
{
    private Action outputNameAction;
    private Action standardFolderAction;
    private Action languageAction;

    private Action numberAction;
    private Action areaAction;
    private Action perimeterAction;
    private Action roundnessAction;
    private Action widthHeightAction;
    private Action xyCoordinatesAction;
    private Action circularityAction;
    private Action solidityAction;
    private Action summaryTypeAction;

    private Action typeChoicesAction;

    private Action okAction;
    private Action resetAllAction;
    private Action applyAction;
    private Action cancelAction;

    private Settings settings;

    // RealWerte
    private String realOutputName;
    private String realStandardFodler;
    private String realLanguage;
    private String realSummaryType;

    private boolean realNumber;
    private boolean realArea;
    private boolean realPerimeter;
    private boolean realRoundness;
    private boolean realWidthHeight;
    private boolean realXyCoordinates;
    private boolean realCircularity;
    private boolean realSolitity;


    public SettingsPM (Settings settings)
    {
        super(settings);

        numberAction = new NumberAction();
        areaAction = new AreaAction();
        perimeterAction = new PerimeterAction();
        roundnessAction = new RoundnessAction();
        widthHeightAction = new WidthHeightAction();
        xyCoordinatesAction = new XyCoordinatesAction();
        circularityAction = new CircularityAction();
        solidityAction = new SolidityAction();
        summaryTypeAction = new SummaryTypeAction();
        languageAction = new LanguageAction();
        outputNameAction = new OutputNameAction();
        standardFolderAction = new StandardFolderAction();
        okAction = new OkAction();
        resetAllAction = new ResetAllAction();
        applyAction = new ApplyAction();
        cancelAction = new CancelAction();


        this.settings = settings;


        settings.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                LOGGER.info("Property name="+evt.getPropertyName()+", oldValue="+evt.getOldValue()+", newValue="+evt.getNewValue());

            }


        });

    }

    public Action getOutputNameAction() {
        return outputNameAction;
    }

    public Action getStandardFolderAction() {
        return standardFolderAction;
    }

    public Action getLanguageAction() {
        return languageAction;
    }

    public Action getNumberAction() {
        return numberAction;
    }

    public Action getAreaAction() {
        return areaAction;
    }

    public Action getPerimeterAction() {
        return perimeterAction;
    }

    public Action getRoundnessAction() {
        return roundnessAction;
    }

    public Action getWidthHeightAction() {
        return widthHeightAction;
    }

    public Action getXyCoordinatesAction() {
        return xyCoordinatesAction;
    }

    public Action getCircularityAction() {
        return circularityAction;
    }

    public Action getSolidityAction() {
        return solidityAction;
    }

    public Action getSummaryTypeAction() {
        return summaryTypeAction;
    }

    public Action getTypeChoicesAction() {
        return typeChoicesAction;
    }

    public Action getOkAction() {
        return okAction;
    }

    public Action getApplyAction() {
        return applyAction;
    }

    public Action getCancelActionAction() {
        return cancelAction;
    }

    public Action getResetAllAction() {
        return resetAllAction;
    }


    private class NumberAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            realNumber = settings.isNumber();
        }
    }

    private class AreaAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realArea = settings.isArea();
        }
    }

    private class PerimeterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realPerimeter = settings.isPerimeter();
        }
    }

    private class RoundnessAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realRoundness = settings.isRoundness();
        }
    }

    private class WidthHeightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realWidthHeight = settings.isWidthheight();
        }
    }

    private class XyCoordinatesAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realXyCoordinates = settings.isXycoordinates();
        }
    }

    private class CircularityAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realCircularity = settings.isCircularity();
        }
    }

    private class SolidityAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            realSolitity = settings.isSolidity();
        }
    }

    private class SummaryTypeAction extends AbstractAction implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            realSummaryType = evt.getPropertyName();
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class LanguageAction extends AbstractAction implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            realLanguage = evt.getPropertyName();
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class OutputNameAction extends AbstractAction implements PropertyChangeListener{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            realOutputName = evt.getPropertyName();
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class StandardFolderAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            //TODO in einstellungsdialog den Pfad setzen
            settings.setStandardFolder(chooser.getCurrentDirectory().toString());
        }
    }

    private class OkAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO Einstellungen übernehmen und Settings schließen
        }
    }

    private class ResetAllAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Settings();
        }
    }

    private class ApplyAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO Einstellungen übernehmen (speichern)
        }
    }

    private class CancelAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO abort
        }
    }

    private void apply() {
        settings.setNumber(realNumber);
        settings.setArea(realArea);
        settings.setPerimeter(realPerimeter);
        settings.setRoundness(realRoundness);
        settings.setWidthheight(realWidthHeight);
        settings.setXycoordinates(realXyCoordinates);
        settings.setCircularity(realCircularity);
        settings.setSolidity(realSolitity);
        settings.setSummarytype(realSummaryType);
        settings.setLanguage(realLanguage);
        settings.setOutputName(realOutputName);
    }
}
