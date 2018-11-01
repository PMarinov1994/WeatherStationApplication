package pm.swt.homeAutomation.model;

import pm.swt.homeAutomation.utils.StationStatus;


public class TempHumSensor extends BaseModel
{
    public static final String TEMPRETURE_PROP_NAME = "tempreture";
    public static final String HUMIDITY_PROP_NAME = "humidity";
    public static final String STATUS_PROP_NAME = "status";
    public static final String REFRESH_INTERVAL_PROP_NAME = "refreshInterval";

    private double tempreture;
    private double humidity;
    private StationStatus status;
    private int refreshInterval;



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
