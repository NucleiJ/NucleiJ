package at.ac.htlhl.nucleij.presenter.tasks;

/**
 * Created by andreas on 23.01.17.
 */
public class RemainingTime {
    private RemainingTime () {

    }

    // When you start downloading save timestamp :
    Long startTime = System.nanoTime();

    // to calculate average remaining speed :
    Long elapsedTime = System.nanoTime() - startTime;
    //Long allTimeForDownloading = (elapsedTime * allBytes / downloadedBytes);
    //Long remainingTime = allTimeForDownloading - elapsedTime;



}
