package at.ac.htlhl.nucleij.presenter.tasks;


import java.io.File;

/**
 * Created by Stefan on 20.01.2017.
 */
public class ControllerTask
{
    public ControllerTask()
    {
        super();
    }

    public void main(String[] tifList, String[] ndpiList)
    {
        int numberOfFiles = 0;

        for (String filename : ndpiList ) {

            if (!filename.equals(null))
            {
                System.out.println("erhaltene Liste:" + filename);

                if(filename.endsWith(".ndpi"))
                {
                    System.out.println("NDPI gefunden!");
                    //NDPI KONVERTER Starten
                }
                //ndpiList[numberOfFiles] = filename;
                numberOfFiles++;

            }
        }
    }
}
