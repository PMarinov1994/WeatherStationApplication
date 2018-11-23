package pm.swt.homeAutomation.model;

public interface ILoggableData
{
    public void logData(String key, String value, Range range);



    public enum Range
    {
        MIN, MAX
    }
}
