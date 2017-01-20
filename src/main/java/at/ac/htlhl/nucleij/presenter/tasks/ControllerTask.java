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
                    //NDPI KONVERTER Starten, absoluten Pfad übergeben

                    //absoluten Pfad an die Tif liste anhaengen
                    for(int i=0; i<tifList.length-1; i++)
                    {
                        boolean newFileinserted = false;
                        do {
                            if(tifList[i].equals(null))
                            {
                                tifList[i] = "INSERTED"+filename;
                                newFileinserted = true;
                            }
                        }while(!newFileinserted);

                    }
                }
                else if (filename.endsWith(".tif"))
                {
                    System.out.println("TIF gefunden!");
                }
                else
                {
                    System.out.println("Anderes File gefunden!");
                }
                //ndpiList[numberOfFiles] = filename;
                numberOfFiles++;

            }
        }


        for (String tifName : tifList ) {

            if (tifName != null) {
                System.out.println("erhaltene Liste:" + tifName);
            }
        }
    }
}
