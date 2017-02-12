package at.ac.htlhl.nucleij.presenter.analyzing.analyzerLogic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {
    public String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
