package pm.swt.homeAutomation.model;


public class TempHumSensor extends BaseWeatherStationModel
{
    public static final String HUMIDITY_PROP_NAME = "humidity";

    private double humidity = 0;



    public double getHumidity()
    {
        return humidity;
    }



    public void setHumidity(double humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }
}
