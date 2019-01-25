package pm.swt.homeAutomation.model;

public class ConfigurationModel extends BaseModel
{
    public static final String MISSED_REPORTS_COUNT = "missedReportsCount";
    public static final String LOW_BATTERY_LEVEL = "lowBatteryLevel";
    public static final String MEDIUM_BATTERY_LEVEL = "mediumBatteryLevel";
    public static final String FULL_BATTERY_LEVEL = "fullBatteryLevel";
    public static final String MQTT_PORT = "mqttPort";
    public static final String MQTT_ADDRESS = "mqttAddress";
    public static final String MQTT_RECONNECT_INTERVAL_SECONDS = "mqttReconnectIntervalSeconds";

    private double fullBatteryLevel = 4;
    private double mediumBatteryLevel = 3.7;
    private double lowBatteryLevel = 3.3;
    
    private String mqttAddress = "localhost";
    private String mqttPort = "1883";

    private int mqttReconnectIntervalSeconds = 5;

    /**
     * The amount of failed station check-ins before an error flag is raised.
     */
    private int missedReportsCount;



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
}
