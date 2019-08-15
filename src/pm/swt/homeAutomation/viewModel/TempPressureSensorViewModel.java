package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;

import pm.swt.homeAutomation.model.TempPressureSensor;
import pm.swt.homeAutomation.utils.StationLocation;


public class TempPressureSensorViewModel extends WeatherStationViewModel
{
    private static final String PRESSURE_STRING_FORMAT = "%dp";

    public static final String PRESSURE_PROP_NAME = "pressure";
    public static final String HUMIDITY_PROP_NAME = "humidity";

    private String pressure;
    private String humidity;



    public TempPressureSensorViewModel(TempPressureSensor model, StationLocation homeSector)
    {
        super(model, homeSector);

        setPressure("N/Ap");
        setHumidity("N/A%");
    }



    public String getPressure()
    {
        return pressure;
    }



    public void setPressure(String pressure)
    {
        this.firePropertyChange(PRESSURE_PROP_NAME, this.pressure, this.pressure = pressure);
    }



    @Override
    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        super.onPropertyChanged(evt);

        switch (evt.getPropertyName())
        {
        case TempPressureSensor.PRESSURE_PROP_NAME:
            this.setPressure(this.formatPressure((double) evt.getNewValue()));
            break;

        case TempPressureSensor.HUMIDITY_PROP_NAME:
            this.setHumidity(this.formatHum((double) evt.getNewValue()));
            break;
        default:
            break;
        }
    }



    private String formatPressure(double pressure)
    {
        return String.format(PRESSURE_STRING_FORMAT, (int) pressure);
    }



    private String formatHum(double hum)
    {
        return String.format("%02.1f%%", hum);
    }



    public String getHumidity()
    {
        return humidity;
    }



    public void setHumidity(String humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }
}
