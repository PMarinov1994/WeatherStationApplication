package pm.swt.homeAutomation.model;

public class TempPressureSensor extends BaseWeatherStationModel
{
    public static final String PRESSURE_PROP_NAME = "pressure";
    public static final String ALTITUDE_PROP_NAME = "altitude";

    private double pressure = 0;
    private double altitude = 0;



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
}
