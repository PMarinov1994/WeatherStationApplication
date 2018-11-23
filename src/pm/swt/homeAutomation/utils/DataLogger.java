package pm.swt.homeAutomation.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;


/**
 * Work in progress.
 * @author pmarinov
 *
 */
public class DataLogger
{
    private static final String DATA_DIR_NAME = "dataLogger";

//    private final String workingDirPath;
    @SuppressWarnings("unused")
    private final File workingDir;



    public DataLogger()
    {
        String tempPath = "";

        try
        {
            File jarFile = new File(DataLogger.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            tempPath = jarFile.getAbsolutePath();
            tempPath = Paths.get(tempPath).getParent().toAbsolutePath().toString();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        File dir = new File(tempPath + File.separatorChar + DATA_DIR_NAME);
        if (!dir.exists())
        {
            System.out.println("Trying to create data logger dir at: " + dir.getAbsolutePath());
            boolean result = dir.mkdirs();
            System.out.println("Data logger dir created: " + result);
        }

        workingDir = dir;
    }
}
