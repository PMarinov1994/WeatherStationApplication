package pm.swt.homeAutomation.model;

public class ConfigurationModel extends BaseModel
{
    public static final String MISSED_REPORTS_COUNT = "missedReportsCount";
    public static final String LOW_BATTERY_LEVEL = "lowBatteryLevel";
    public static final String MEDIUM_BATTERY_LEVEL = "mediumBatteryLevel";
    public static final String FULL_BATTERY_LEVEL = "fullBatteryLevel";

    private double fullBatteryLevel;
    private double mediumBatteryLevel;
    private double lowBatteryLevel;


    /**
     * The amount of failed station check-ins before an error flag is raised.
     */
    private int missedReportsCount;



    public double getFullBatteryLevel()
    {
        return fullBatteryLevel;
    }



    public void setFullBatteryLevel(double fullBatteryLevel)
    {
        this.firePropertyChange(FULL_BATTERY_LEVEL, this.fullBatteryLevel, this.fullBatteryLevel = fullBatteryLevel);
    }



    public double getMediumBatteryLevel()
    {
        return mediumBatteryLevel;
    }



    public void setMediumBatteryLevel(double mediumBatteryLevel)
    {
        this.firePropertyChange(MEDIUM_BATTERY_LEVEL, this.mediumBatteryLevel, this.mediumBatteryLevel = mediumBatteryLevel);
    }



    public double getLowBatteryLevel()
    {
        return lowBatteryLevel;
    }



    public void setLowBatteryLevel(double lowBatteryLevel)
    {
        this.firePropertyChange(LOW_BATTERY_LEVEL, this.lowBatteryLevel, this.lowBatteryLevel = lowBatteryLevel);
    }



    public int getMissedReportsCount()
    {
        return missedReportsCount;
    }



    public void setMissedReportsCount(int missedReportsCount)
    {
        this.firePropertyChange(MISSED_REPORTS_COUNT, this.missedReportsCount, this.missedReportsCount = missedReportsCount);
    }
}
