package pm.swt.homeAutomation.model;

import java.util.Date;


public class StatusBar extends BaseModel
{
    public static final String MESSAGE_PROP_NAME = "message";
    public static final String DATE_PROP_NAME = "date";

    private Date date;
    private String message;



    public Date getDate()
    {
        return date;
    }



    public void setDate(Date date)
    {
        this.firePropertyChange(DATE_PROP_NAME, this.date, this.date = date);
    }



    public String getMessage()
    {
        return message;
    }



    public void setMessage(String message)
    {
        this.firePropertyChange(MESSAGE_PROP_NAME, this.message, this.message = message);
    }
}
