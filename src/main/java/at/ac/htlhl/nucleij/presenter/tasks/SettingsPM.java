package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.GLScanAnalyzer;
import at.ac.htlhl.nucleij.model.Settings;
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
    private Action convertNameAction;
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

    private Settings settings;

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


        this.settings = settings;

        //outputNameAction = new OutputNameAction();

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

    public Action getConvertNameAction() {
        return convertNameAction;
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

    private class NumberAction extends AbstractAction
    {
        public NumberAction() {
        }

        public void actionPerformed(ActionEvent e)
        {
            settings.setNumber(settings.isNumber());
        }
    }

    private class AreaAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setArea(settings.isArea());
        }
    }

    private class PerimeterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setPerimeter(settings.isPerimeter());
        }
    }

    private class RoundnessAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setRoundness(settings.isRoundness());
        }
    }

    private class WidthHeightAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setWidthheight(settings.isWidthheight());
        }
    }

    private class XyCoordinatesAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setXycoordinates(settings.isXycoordinates());
        }
    }

    private class CircularityAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setCircularity(settings.isCircularity());
        }
    }

    private class SolidityAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setSolidity(settings.isSolidity());
        }
    }

    private class SummaryTypeAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setSummarytype("Test");
        }
    }

    //public class Action OutputNameAction extends

}
