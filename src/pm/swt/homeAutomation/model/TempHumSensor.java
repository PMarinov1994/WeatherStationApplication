package pm.swt.homeAutomation.model;

public class TempHumSensor extends BaseModel
{
    public static final String TEMPRETURE_PROP_NAME = "tempreture";
    public static final String HUMIDITY_PROP_NAME = "humidity";

    private double tempreture;
    private double humidity;



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
}
