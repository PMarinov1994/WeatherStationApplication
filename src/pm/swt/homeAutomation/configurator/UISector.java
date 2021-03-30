package pm.swt.homeAutomation.configurator;

import pm.swt.homeAutomation.model.BaseModel;
import pm.swt.homeAutomation.model.TempHumSensor;
import pm.swt.homeAutomation.model.TempPressureSensor;

public class UISector
{
    private String mqttTopic;
    private String name;
    private String iconName;
    private UISensorType sensorType;

    private BaseModel model;

    public UISector(String name, String mqttTopic, String iconName, UISensorType sensorType)
    {
        this.name = name;
        this.mqttTopic = mqttTopic;
        this.iconName = iconName;
        this.sensorType = sensorType;

        switch (this.sensorType)
        {
            case AM2320:
                this.model = new TempHumSensor();
                break;            
            case BME280:
                this.model = new TempPressureSensor();
                break;
        }
    }

    public<T extends BaseModel> T getModel(Class<T> clazz)
    {
        try
        {
            return clazz.cast(this.model);
        }
        catch(ClassCastException e)
        {
            return null;
        }
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public UISensorType getSensorType()
    {
        return this.sensorType;
    }

    public String getName()
    {
        return this.name;
    }

    public String getMqttTopic()
    {
        return this.mqttTopic;
    }
}