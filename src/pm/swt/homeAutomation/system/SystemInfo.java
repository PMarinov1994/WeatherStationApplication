package pm.swt.homeAutomation.system;

public final class SystemInfo
{
    private SystemInfo()
    {
    }



    public static SystemArch getSystemArch()
    {
        String osArch = System.getProperty("os.arch");

        if (osArch == null)
            return null;

        if (osArch.contains("64"))
            return SystemArch.x86_64;
        else if (osArch.contains("32"))
            return SystemArch.x86;
        else if (osArch.contains("arm"))
            return SystemArch.ARM;

        return null;
    }



    public static SystemType getSystemType()
    {
        String osNameProperty = System.getProperty("os.name");

        if (osNameProperty == null)
            throw new RuntimeException("os.name property is not set");
        else
            osNameProperty = osNameProperty.toLowerCase();

        if (osNameProperty.contains("win"))
            return SystemType.WINDOWS;
        else if (osNameProperty.contains("mac"))
            return SystemType.MAC_OS;
        else if (osNameProperty.contains("linux") || osNameProperty.contains("nix"))
            return SystemType.LINUX;
        else
            throw new RuntimeException("Unknown OS name: " + osNameProperty);
    }
}
