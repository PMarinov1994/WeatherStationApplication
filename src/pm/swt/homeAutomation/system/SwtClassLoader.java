package pm.swt.homeAutomation.system;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class SwtClassLoader
{
    private static final String MQTT_PAHO_CLIENT_JAR = "lib/org.eclipse.paho.client.mqtt.jar";
    private URLClassLoader urlClassLoader;



    public SwtClassLoader()
    {
        this.urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    }



    public void addJarToClasspath()
    {
        try
        {
            String jarFilePath = getArchFilename("lib/swt");

            File jarFile = new File(jarFilePath);
            if (!jarFile.exists())
                System.err.println(String.format("File '%s' is missing!", jarFile.getAbsolutePath()));

            URL url = jarFile.toURI().toURL();

            // Load SWT Library
            this.loadLibrary(url);

            // Load MQTT PAHO Library
            jarFile = new File(MQTT_PAHO_CLIENT_JAR);
            if (!jarFile.exists())
                System.err.println(String.format("File '%s' is missing!", jarFile.getAbsolutePath()));

            url = jarFile.toURI().toURL();
            this.loadLibrary(url);
        }
        catch (Exception t)
        {
            System.err.println(t.getMessage());
        }

        URL[] urLs = this.urlClassLoader.getURLs();
        System.out.println("Printing available libs:");
        for (URL classLoaderUlts : urLs)
            System.out.println(String.format("Class loader url: %s", classLoaderUlts.toString()));
    }



    public void loadLibrary(URL url)
            throws NoSuchMethodException,
            SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        if (!loaded(url))
        {
            Class<?> urlClass = URLClassLoader.class;

            Method method = urlClass.getDeclaredMethod("addURL", new Class<?>[] { URL.class });
            method.setAccessible(true);
            method.invoke(this.urlClassLoader, new Object[] { url });
        }
        else
            System.err.println("Library already loaded!");
    }



    private boolean loaded(URL url)
    {
        String libToLoadName = this.getLibraryName(url).toLowerCase();

        URL[] urLs = this.urlClassLoader.getURLs();
        for (URL tmp : urLs)
        {
            String libName = this.getLibraryName(tmp).toLowerCase();
            if (libToLoadName.equals(libName))
                return true;
        }

        return false;
    }



    private String getArchFilename(String prefix)
    {
        return prefix + "_" + getOSName() + "_" + getArchName() + ".jar";
    }



    private String getOSName()
    {
        SystemType systemType = SystemInfo.getSystemType();

        switch (systemType)
        {
        case LINUX:
            return "linux";
        case MAC_OS:
            return "osx";
        case WINDOWS:
            return "win";
        default:
            return null;
        }
    }



    private String getArchName()
    {
        SystemArch systemArch = SystemInfo.getSystemArch();

        switch (systemArch)
        {
        case ARM:
            return "arm";
        case x86:
            return "x86";
        case x86_64:
            return "x64";
        default:
            return null;

        }
    }



    private String getLibraryName(URL url)
    {
        String result = url.getFile();

        int lastPathSepIndex = result.lastIndexOf(File.separatorChar);

        if (lastPathSepIndex >= 0)
            result = result.substring(lastPathSepIndex);

        return result;
    }
}
