package at.ac.htlhl.nucleij.presenter;

import at.ac.htlhl.nucleij.model.NdpiConverter;
import com.jgoodies.binding.PresentationModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;


/**
 * Created by Stefan on 11.11.2016.
 */
public class NdpiConverterPM extends PresentationModel<NdpiConverter>
{
    private static final Logger LOGGER = Logger.getLogger(NdpiConverterPM.class.getName());
    public static final String FILE_EXTENSION = "nucleij";

    private Action convertAction;
    private Action exportPathAction;
    private Action inputPathAction;
    private Action typeAction;
    private Action magnificationAction;



    public NdpiConverterPM(NdpiConverter ndpiConverter)
    {
        super(ndpiConverter);

        convertAction = new ConvertAction();
        exportPathAction = new ExportPathAction();
        inputPathAction = new InputPathAction();
        typeAction = new TypeAction();
        magnificationAction = new MagnificationAction();
    }

    public Action getConvertAction() {
        return convertAction;
    }

    public Action getMagnificationAction() {
        return magnificationAction;
    }

    public Action getOutputPathAction() {
        return exportPathAction;
    }

    public Action getInputPathAction() {
        return inputPathAction;
    }

    public Action getTypeAction() {
        return typeAction;
    }

    private class ConvertAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public ConvertAction() {

        }
    }

    private class ExportPathAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public ExportPathAction() {

        }
    }

    private class InputPathAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public InputPathAction () {

        }
    }

    private class TypeAction extends AbstractAction {
        public TypeAction() {
        }

        public void actionPerformed(ActionEvent e) {
            LOGGER.info("Analyze Action clicked");

            JFrame parent = ((SingleFrameApplication) Application.getInstance()).getMainFrame();
        }
    }

    private class MagnificationAction extends AbstractAction {
        public void actionPerformed(ActionEvent actionEvent) {

        }

        public MagnificationAction () {

        }
    }


}
