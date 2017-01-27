package at.ac.htlhl.nucleij.presenter.tasks;

import at.ac.htlhl.nucleij.model.Settings;
import com.jgoodies.binding.PresentationModel;

import javax.swing.*;

/**
 * Created by andreas on 25.01.2017.
 */
public class SettingsPM extends PresentationModel<Settings>
{
    private Action outputName;
    private Action standardFolder;
    private Action convertName;
    private Action language;

    private Action number;
    private Action area;
    private Action perimeter;
    private Action roundness;
    private Action widthHeight;
    private Action xyCoordinates;
    private Action circulaarity;
    private Action solidity;
    private Action summaryType;

    private Action typeChoices;

    private Settings settings;

    public SettingsPM (Settings settings)
    {
        //super(Settings);

        this.settings = settings;

        //outputNameAction = new OutputNameAction();

    }

    //public class Action OutputNameAction extends

}
