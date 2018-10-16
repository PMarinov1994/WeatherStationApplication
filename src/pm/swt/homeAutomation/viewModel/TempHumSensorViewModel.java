package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import pm.swt.homeAutomation.model.BaseModel;
import pm.swt.homeAutomation.model.TempHumSensor;


public class TempHumSensorViewModel extends BaseModel
{
    public static final String TEMPERATURE_PROP_NAME = "temperature";
    public static final String HUMIDITY_PROP_NAME = "humidity";
    public static final String HOME_SECTOR_PROP_NAME = "homeSector";

    private TempHumSensor model;

    private String temperature;
    private String humidity;
    private String homeSector;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            switch (evt.getPropertyName())
            {
            case TempHumSensor.TEMPRETURE_PROP_NAME:
                formatTemp((double) evt.getNewValue());
                break;
            case TempHumSensor.HUMIDITY_PROP_NAME:
                formatHum((double) evt.getNewValue());
                break;
            default:
                break;
            }
        }
    };



    public TempHumSensorViewModel(TempHumSensor model, String homeSector)
    {
        this.model = model;
        this.homeSector = homeSector;
        this.model.addPropertyChangeListener(this.listener);

        this.formatHum(this.model.getHumidity());
        this.formatTemp(this.model.getTempreture());
    }



    public String getTemperature()
    {
        return temperature;
    }



    public void setTemperature(String temperature)
    {
        this.firePropertyChange(TEMPERATURE_PROP_NAME, this.temperature, this.temperature = temperature);
    }



    public String getHumidity()
    {
        return humidity;
    }



    public void setHumidity(String humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }



    public void dispose()
    {
        this.model.removePropertyChangeListener(this.listener);
    }



    public String getHomeSector()
    {
        return homeSector;
    }



    public void setHomeSector(String homeSector)
    {
        this.firePropertyChange(HOME_SECTOR_PROP_NAME, this.homeSector, this.homeSector = homeSector);
    }



    public void formatTemp(double temp)
    {
        this.setTemperature(String.format("%02.2f\u2103", temp));
    }



    public void formatHum(double hum)
    {
        this.setHumidity(String.format("%02.2f%%", hum));
    }
}
