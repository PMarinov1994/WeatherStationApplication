package pm.swt.homeAutomation.configurator;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import pm.swt.homeAutomation.model.ConfigurationModel;
import pm.swt.homeAutomation.serializer.ConfigurationSerializer;
import pm.swt.homeAutomation.utils.GlobalResources;


public class ConfigurationFileManager
{
    private static final int MISSED_REPORTS_COUNT_DEFAULT = 3;
    private static final double LOW_BATTERY_LEVEL_DEFAULT = 3.3;
    private static final double MEDIUM_BATTERY_LEVEL_DEFAULT = 3.7;
    private static final double FULL_BATTERY_LEVEL_DEFAULT = 4;
    private static final String MQTT_PORT_DEFAULT = "1883";
    private static final String MQTT_ADDRESS_DEFAULT = "localhost";
    private static final int MQTT_RECONNECT_INTERVAL_SECONDS_DEFAULT = 2;

    private static volatile boolean isConfigured = false;

    private final String CONFIG_FILE_NAME = "config.txt";
    private final String CONFIG_FILE_PATH;

    private FileChangedTracker fileTracker;
    private ConfigurationModel config;

    private List<IConfigurationChanged> subscribers;



    public ConfigurationFileManager() throws OperationNotSupportedException
    {
        if (isConfigured)
            throw new OperationNotSupportedException("This class can be created only once!");

        isConfigured = true;

        String jarExecPath = GlobalResources.getExecJarPath();
        CONFIG_FILE_PATH = String.format("%s%s%s", jarExecPath, File.separator, CONFIG_FILE_NAME);

        File configFile = new File(CONFIG_FILE_PATH);
        ConfigurationSerializer serializer = new ConfigurationSerializer(configFile);

        if (!configFile.exists())
            this.config = this.createConfigurationFile(serializer);
        else
        {
            try
            {
                this.config = serializer.deserialize();
            }
            catch (InvalidParameterException e)
            {
                System.err.println("Config file content error! Creating new default file...");
                this.config = this.createConfigurationFile(serializer);
            }
        }

        this.fileTracker = new FileChangedTracker(CONFIG_FILE_PATH);
        this.fileTracker.startTracker(new INotifyFileChanged()
        {

            @Override
            public void fileChanged()
            {
                config = serializer.deserialize();
                fireConfigurationChange(config);
            }
        });

        this.subscribers = new ArrayList<>();
    }



    public void addConfigurationChangeSubscriber(IConfigurationChanged callBack)
    {
        this.subscribers.add(callBack);
    }



    public boolean removeConfigurationChangeSubscriber(IConfigurationChanged callBack)
    {
        if (!this.subscribers.contains(callBack))
            throw new InvalidParameterException("Callback must be first registered before removing.");

        return this.subscribers.remove(callBack);
    }



    public void fireConfigurationChange(ConfigurationModel newConfig)
    {
        for (IConfigurationChanged sub : this.subscribers)
            sub.configurationChanged(newConfig);
    }



    public void dispose()
    {
        this.fileTracker.stopTracker();
        this.subscribers.clear();
    }



    private ConfigurationModel createConfigurationFile(ConfigurationSerializer serializer)
    {
        File file = serializer.getFile();

        try
        {
            file.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ConfigurationModel model = new ConfigurationModel();

        model.setFullBatteryLevel(FULL_BATTERY_LEVEL_DEFAULT);
        model.setMediumBatteryLevel(MEDIUM_BATTERY_LEVEL_DEFAULT);
        model.setLowBatteryLevel(LOW_BATTERY_LEVEL_DEFAULT);
        model.setMissedReportsCount(MISSED_REPORTS_COUNT_DEFAULT);
        model.setMqttAddress(MQTT_ADDRESS_DEFAULT);
        model.setMqttPort(MQTT_PORT_DEFAULT);
        model.setMqttReconnectIntervalSeconds(MQTT_RECONNECT_INTERVAL_SECONDS_DEFAULT);

        serializer.serialize(model);
        return model;
    }



    public ConfigurationModel getConfig()
    {
        return config;
    }
}
