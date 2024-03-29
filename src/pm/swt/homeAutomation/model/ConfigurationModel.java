package pm.swt.homeAutomation.model;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationModel extends BaseModel
{
    public static final String MISSED_REPORTS_COUNT = "missedReportsCount";
    public static final String LOW_BATTERY_LEVEL = "lowBatteryLevel";
    public static final String MEDIUM_BATTERY_LEVEL = "mediumBatteryLevel";
    public static final String FULL_BATTERY_LEVEL = "fullBatteryLevel";
    public static final String MQTT_PORT = "mqttPort";
    public static final String MQTT_ADDRESS = "mqttAddress";
    public static final String MQTT_RECONNECT_INTERVAL_SECONDS = "mqttReconnectIntervalSeconds";
    public static final String APPLICATION_HOT_PARAMETERS_CHANGE_ENABLE = "applicationHotParametersChangeEnable";

    private double fullBatteryLevel = 4;
    private double mediumBatteryLevel = 3.7;
    private double lowBatteryLevel = 3.3;

    private String mqttAddress = "localhost";
    private String mqttPort = "1883";

    private int mqttReconnectIntervalSeconds = 5;

    private boolean applicationHotParametersChangeEnable = false;

    /**
     * The amount of failed station check-ins before an error flag is raised.
     */
    private int missedReportsCount;

    private Map<String, String> additionalParameters = new HashMap<>();



    public void putAdditionalParamer(String key, String value)
    {
        this.additionalParameters.put(key, value);
    }



    public String getAdditionalParamer(String key)
    {
        if (this.additionalParameters.get(key) == null)
            return "";

        return this.additionalParameters.get(key);
    }



    public double getFullBatteryLevel()
    {
        return fullBatteryLevel;
    }



    public void setFullBatteryLevel(double fullBatteryLevel)
    {
        this.firePropertyChange(FULL_BATTERY_LEVEL, this.fullBatteryLevel, this.fullBatteryLevel = fullBatteryLevel);
    }



    public double getMediumBatteryLevel()
    {
        return mediumBatteryLevel;
    }



    public void setMediumBatteryLevel(double mediumBatteryLevel)
    {
        this.firePropertyChange(MEDIUM_BATTERY_LEVEL, this.mediumBatteryLevel, this.mediumBatteryLevel = mediumBatteryLevel);
    }



    public double getLowBatteryLevel()
    {
        return lowBatteryLevel;
    }



    public void setLowBatteryLevel(double lowBatteryLevel)
    {
        this.firePropertyChange(LOW_BATTERY_LEVEL, this.lowBatteryLevel, this.lowBatteryLevel = lowBatteryLevel);
    }



    public int getMissedReportsCount()
    {
        return missedReportsCount;
    }



    public void setMissedReportsCount(int missedReportsCount)
    {
        this.firePropertyChange(MISSED_REPORTS_COUNT, this.missedReportsCount, this.missedReportsCount = missedReportsCount);
    }



    public String getMqttAddress()
    {
        return mqttAddress;
    }



    public void setMqttAddress(String mqttAddress)
    {
        this.firePropertyChange(MQTT_ADDRESS, this.mqttAddress, this.mqttAddress = mqttAddress);
    }



    public String getMqttPort()
    {
        return mqttPort;
    }



    public void setMqttPort(String mqttPort)
    {
        this.firePropertyChange(MQTT_PORT, this.mqttPort, this.mqttPort = mqttPort);
    }



    public int getMqttReconnectIntervalSeconds()
    {
        return mqttReconnectIntervalSeconds;
    }



    public void setMqttReconnectIntervalSeconds(int mqttReconnectIntervalSeconds)
    {
        this.firePropertyChange(MQTT_RECONNECT_INTERVAL_SECONDS, this.mqttReconnectIntervalSeconds, this.mqttReconnectIntervalSeconds = mqttReconnectIntervalSeconds);
    }



    public boolean isApplicationHotParametersChangeEnable()
    {
        return applicationHotParametersChangeEnable;
    }



    public void setApplicationHotParametersChangeEnable(boolean applicationHotParametersChangeEnable)
    {
        this.firePropertyChange(
                APPLICATION_HOT_PARAMETERS_CHANGE_ENABLE,
                this.applicationHotParametersChangeEnable,
                this.applicationHotParametersChangeEnable = applicationHotParametersChangeEnable);
    }
}
