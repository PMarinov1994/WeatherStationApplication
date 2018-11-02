package pm.swt.homeAutomation.model;

import pm.swt.homeAutomation.utils.StationStatus;


public class TempHumSensor extends BaseModel implements ITrackableStation
{
    public static final String TEMPRETURE_PROP_NAME = "tempreture";
    public static final String HUMIDITY_PROP_NAME = "humidity";

    private double tempreture = 0;
    private double humidity = 0;
    private StationStatus status = StationStatus.STANDBY_STATUS;
    private int refreshInterval = 0;



    public double getTempreture()
    {
        return tempreture;
    }



    public void setTempreture(double tempreture)
    {
        this.firePropertyChange(TEMPRETURE_PROP_NAME, this.tempreture, this.tempreture = tempreture);
    }



    public double getHumidity()
    {
        return humidity;
    }



    public void setHumidity(double humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }



    public StationStatus getStatus()
    {
        return status;
    }



    public void setStatus(StationStatus status)
    {
        this.firePropertyChange(STATUS_PROP_NAME, this.humidity, this.status = status);
    }



    public int getRefreshInterval()
    {
        return refreshInterval;
    }



    public void setRefreshInterval(int refreshInterval)
    {
        this.firePropertyChange(REFRESH_INTERVAL_PROP_NAME, this.humidity, this.refreshInterval = refreshInterval);
    }
}
