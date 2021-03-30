package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Display;

import pm.swt.homeAutomation.model.BaseModel;
import pm.swt.homeAutomation.model.BaseWeatherStationModel;
import pm.swt.homeAutomation.model.BatteryLevel;
import pm.swt.homeAutomation.utils.StationStatus;
import pm.swt.homeAutomation.utils.StationStatusTracker;


public abstract class WeatherStationViewModel extends BaseModel
{
    public static final String TEMPERATURE_PROP_NAME = "temperature";
    public static final String HOME_SECTOR_PROP_NAME = "homeSector";
    public static final String STATION_STATUS_PROP_NAME = "stationStatus";
    public static final String BATTERY_LEVEL_PROP_NAME = "batteryLevel";

    public static final String TEMP_CEL_SIM = "\u2103";

    private String temperature;
    private String homeSectorIcon;
    private StationStatus stationStatus;
    private BatteryLevel batteryLevel;

    private StationStatusTracker ssTracker;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            WeatherStationViewModel.this.handlePropertyChanged(evt);
        }
    };


    protected BaseWeatherStationModel model;



    public WeatherStationViewModel(BaseWeatherStationModel model, String homeSectorIcon)
    {
        this.model = model;
        this.setBatteryLevel(this.model.getBatteryLevel());
        this.setStationStatus(this.model.getStatus());

        this.homeSectorIcon = homeSectorIcon;
        this.model.addPropertyChangeListener(this.listener);

        this.temperature = "N/A" + TEMP_CEL_SIM;
        this.batteryLevel = this.model.getBatteryLevel();
        this.stationStatus = this.model.getStatus();
        
        this.ssTracker = new StationStatusTracker(model);
        this.ssTracker.startTracking();
    }



    public void dispose()
    {
        this.ssTracker.stopTracking();
    }



    public String getTemperature()
    {
        return temperature;
    }



    public void setTemperature(String temperature)
    {
        this.firePropertyChange(TEMPERATURE_PROP_NAME, this.temperature, this.temperature = temperature);
    }



    public String getHomeSectorIcon()
    {
        return homeSectorIcon;
    }



    public StationStatus getStationStatus()
    {
        return stationStatus;
    }



    public void setStationStatus(StationStatus stationStatus)
    {
        this.firePropertyChange(STATION_STATUS_PROP_NAME, this.stationStatus, this.stationStatus = stationStatus);
    }



    protected void formatTemp(double temp)
    {
        this.setTemperature(String.format("%02.1f\u2103", temp));
    }



    protected void onPropertyChanged(PropertyChangeEvent evt)
    {
        switch (evt.getPropertyName())
        {
        case BaseWeatherStationModel.TEMPRETURE_PROP_NAME:
            formatTemp((double) evt.getNewValue());
            break;
        case BaseWeatherStationModel.STATUS_PROP_NAME:
            this.setStationStatus((StationStatus) evt.getNewValue());
            break;
        case BaseWeatherStationModel.BATTERY_LEVEL_PROP_NAME:
            this.setBatteryLevel((BatteryLevel) evt.getNewValue());
            break;
        default:
            break;
        }
    }



    private void handlePropertyChanged(PropertyChangeEvent evt)
    {
        Display display = null;
        if (Display.getCurrent() != null)
            display = Display.getCurrent();
        else
            display = Display.getDefault();

        display.asyncExec(new Runnable()
        {
            @Override
            public void run()
            {
                WeatherStationViewModel.this.onPropertyChanged(evt);
            }
        });
    }



    public BatteryLevel getBatteryLevel()
    {
        return batteryLevel;
    }



    public void setBatteryLevel(BatteryLevel batteryLevel)
    {
        this.firePropertyChange(BATTERY_LEVEL_PROP_NAME, this.batteryLevel, this.batteryLevel = batteryLevel);
    }
}
