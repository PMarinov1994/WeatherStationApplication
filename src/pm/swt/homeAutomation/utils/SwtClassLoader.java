package pm.swt.homeAutomation.utils;


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
        String osNameProperty = System.getProperty("os.name");

        if (osNameProperty == null)
            throw new RuntimeException("os.name property is not set");
        else
            osNameProperty = osNameProperty.toLowerCase();

        if (osNameProperty.contains("win"))
            return "win";
        else if (osNameProperty.contains("mac"))
            return "osx";
        else if (osNameProperty.contains("linux") || osNameProperty.contains("nix"))
            return "linux";
        else
            throw new RuntimeException("Unknown OS name: " + osNameProperty);
    }



    private String getArchName()
    {
        String osArch = System.getProperty("os.arch");

        if (osArch == null)
            return null;

        if (osArch.contains("64"))
            return "x64";
        else if (osArch.contains("32"))
            return "x86";
        else if (osArch.contains("arm"))
            return "arm";

        return null;
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
