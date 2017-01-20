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
                System.out.println(filename);

                //ndpiList[numberOfFiles] = filename;
                numberOfFiles++;
                // file.getName().endet mit ndpi:
                //if ()
                {
                    //start konverter
                }
            }
        }
    }
}
