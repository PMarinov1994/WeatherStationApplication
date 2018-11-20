package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;

import pm.swt.homeAutomation.model.TempPressureSensor;
import pm.swt.homeAutomation.utils.StationLocation;


public class TempPressureSensorViewModel extends WeatherStationViewModel
{
    private static final String PRESSURE_STRING_FORMAT = "%.2f\nhPa";

    public static final String PRESSURE_PROP_NAME = "pressure";

    private String pressure;



    public TempPressureSensorViewModel(TempPressureSensor model, StationLocation homeSector)
    {
        super(model, homeSector);

        setPressure("N/A hPa");
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
        default:
            break;
        }
    }



    private String formatPressure(double pressure)
    {
        return String.format(PRESSURE_STRING_FORMAT, pressure);
    }
}
