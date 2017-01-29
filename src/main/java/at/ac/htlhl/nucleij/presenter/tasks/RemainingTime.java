package at.ac.htlhl.nucleij.presenter.tasks;

import java.io.File;

/**
 * Created by andreas on 23.01.17.
 */
public class RemainingTime {
    private RemainingTime () {

    }
    //TODO Wenigstens durhcschnittliche Restzeit aus Anzahl der Dateien berechnen
    // When you start downloading save timestamp :
    Long startTime = System.nanoTime();

    // to calculate average remaining speed :
    Long elapsedTime = System.nanoTime() - startTime;
    //Long allTimeForDownloading = (elapsedTime * allBytes / downloadedBytes);
    //Long remainingTime = allTimeForDownloading - elapsedTime;


    public void getFileSize(String filePath) {
        File file =new File(filePath);

        double bytes = file.length();

    }


}
