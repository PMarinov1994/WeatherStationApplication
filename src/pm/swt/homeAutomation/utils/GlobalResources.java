package pm.swt.homeAutomation.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public abstract class GlobalResources
{
    public static final String STATUS_BAR_INSTANCE_MODEL_NAME = "statusBarModel";

    public static final String CONFIGURATION_FILE_MANAGER_NAME = "configurationFileManager";
    public static final String UI_LAYOUT_MANAGER_NAME = "uiLayoutManager";

    public static String getExecJarPath()
    {
        String jarExePath = "";

        try
        {
            File jarFile = new File(GlobalResources.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            jarExePath = jarFile.getAbsolutePath();
            jarExePath = Paths.get(jarExePath).getParent().toAbsolutePath().toString();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        return jarExePath;
    }
}
