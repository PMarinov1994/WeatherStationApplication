package pm.swt.homeAutomation.model;

import pm.swt.homeAutomation.utils.StationStatus;


public class BaseWeatherStationModel extends BaseModel implements ITrackableStation
{
    public static final String TEMPRETURE_PROP_NAME = "tempreture";
    public static final String BATTERY_LEVEL_PROP_NAME = "batteryLevel";

    private double tempreture = 0;
    private BatteryLevel batteryLevel = BatteryLevel.UNKNOWN;

    private StationStatus status = StationStatus.STANDBY_STATUS;
    private int refreshInterval = 0;



    @Override
    public int getRefreshInterval()
    {
        return this.refreshInterval;
    }



    @Override
    public void setRefreshInterval(int refreshInterval)
    {
        this.firePropertyChange(REFRESH_INTERVAL_PROP_NAME, this.refreshInterval, this.refreshInterval = refreshInterval);
    }



    @Override
    public void setStatus(StationStatus status)
    {
        this.firePropertyChange(STATUS_PROP_NAME, this.status, this.status = status);
    }



    @Override
    public StationStatus getStatus()
    {
        return this.status;
    }



    public double getTempreture()
    {
        return tempreture;
    }



    public void setTempreture(double tempreture)
    {
        this.firePropertyChange(TEMPRETURE_PROP_NAME, this.tempreture, this.tempreture = tempreture);
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
