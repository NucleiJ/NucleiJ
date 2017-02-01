package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;//SummaryCreator Class v1, Stringspeicher fuer SummaryReport

public class StringAdder {
    String zwischenspeicherString = "";

    public void appendString(String uebergebenerString) {
        if (uebergebenerString == "reset") {
            zwischenspeicherString = "";
        } else if (uebergebenerString == "csvHeader") {
            zwischenspeicherString = "Dateiname;gesamte Gewebeflaeche [um2];gesamte Zellkernflaeche [um2];Zellkernflaeche in %;Zellkerne / mm2;Arith. Umfang [um];groesste Zellkernflaeche [um2];Arith. Zellkernflaeche [um2];Med. Zellkernflaeche [um2];\n";
            // TODO texte auslagern
        } else {
            zwischenspeicherString = zwischenspeicherString + uebergebenerString;
        }
    }

    public String getString() {
        return zwischenspeicherString;
    }
}
