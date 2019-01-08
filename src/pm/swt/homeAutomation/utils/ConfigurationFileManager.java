package pm.swt.homeAutomation.utils;

import java.io.File;
import java.io.IOException;

import javax.naming.OperationNotSupportedException;


public class ConfigurationFileManager
{
    private static volatile boolean isConfigured = false;

    private final String CONFIG_FILE_NAME = "config.txt";
    private final String CONFIG_FILE_PATH;

    private FileChangedTracker fileTracker;



    public ConfigurationFileManager() throws OperationNotSupportedException
    {
        if (isConfigured)
            throw new OperationNotSupportedException("This class can be created only once!");

        isConfigured = true;

        String jarExecPath = GlobalResources.getExecJarPath();
        CONFIG_FILE_PATH = String.format("%s%s%s", jarExecPath, File.separator, CONFIG_FILE_NAME);

        File configFile = new File(CONFIG_FILE_PATH);

        if (!configFile.exists())
        {
            try
            {
                if (configFile.createNewFile())
                    System.err.println("File already exists!");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        this.fileTracker = new FileChangedTracker(CONFIG_FILE_PATH);
        this.fileTracker.startTracker();
    }



    public void dispose()
    {
        this.fileTracker.stopTracker();
    }
}
