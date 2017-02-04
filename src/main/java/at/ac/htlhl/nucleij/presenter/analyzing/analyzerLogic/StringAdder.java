package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;//SummaryCreator Class v1, Stringspeicher fuer SummaryReport

import java.util.ResourceBundle;

public class StringAdder {
    private ResourceBundle bundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.i18n.dialogs");

    private String zwischenspeicherString = "";

    public void appendString(String uebergebenerString) {
        if (uebergebenerString == "reset") {
            zwischenspeicherString = "";
        } else if (uebergebenerString == "csvHeader") {
            zwischenspeicherString = bundle.getString("CSV.StringAdder.text");
        } else {
            zwischenspeicherString = zwischenspeicherString + uebergebenerString;
        }
    }

    public String getString() {
        return zwischenspeicherString;
    }
}
