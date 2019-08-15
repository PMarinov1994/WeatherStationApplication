package pm.swt.homeAutomation.model;

public class TempPressureSensor extends BaseWeatherStationModel
{
    public static final String PRESSURE_PROP_NAME = "pressure";
    public static final String ALTITUDE_PROP_NAME = "altitude";
    public static final String HUMIDITY_PROP_NAME = "humidity";

    private double pressure = 0;
    private double altitude = 0;
    private double humidity = 0;



    public double getPressure()
    {
        return pressure;
    }



    public void setPressure(double pressure)
    {
        this.firePropertyChange(PRESSURE_PROP_NAME, this.pressure, this.pressure = pressure);
    }



    public double getAltitude()
    {
        return altitude;
    }



    public void setAltitude(double altitude)
    {
        this.firePropertyChange(ALTITUDE_PROP_NAME, this.altitude, this.altitude = altitude);
    }



    public double getHumidity()
    {
        return humidity;
    }



    public void setHumidity(double humidity)
    {
        this.firePropertyChange(HUMIDITY_PROP_NAME, this.humidity, this.humidity = humidity);
    }
}
