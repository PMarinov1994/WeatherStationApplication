package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;

import pm.swt.homeAutomation.model.TempHumSensor;


public class TempHumSensorViewModel extends WeatherStationViewModel
{
    public static final String HUMIDITY_PROP_NAME = "humidity";


    @SuppressWarnings("unused")
    private TempHumSensor model;

    private String humidity = "";



    public TempHumSensorViewModel(TempHumSensor model, String homeSectorIcon)
    {
        super(model, homeSectorIcon);

        this.model = model;
        this.humidity = "N/A%";
    }



    public String getHumidity()
    {
        return humidity;
    }



    public void setHumidity(String humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }



    public void formatHum(double hum)
    {
        this.setHumidity(String.format("%02.0f%%", hum));
    }



    @Override
    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        super.onPropertyChanged(evt);

        switch (evt.getPropertyName())
        {
        case TempHumSensor.HUMIDITY_PROP_NAME:
            formatHum((double) evt.getNewValue());
            break;
        default:
            break;
        }
    }
}
