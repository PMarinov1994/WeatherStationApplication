package pm.swt.homeAutomation.model;

import java.util.Date;


public class StatusBar extends BaseModel
{
    public static final String DATE_PROP_NAME = "date";

    private Date date;



    public Date getDate()
    {
        return date;
    }



    public void setDate(Date date)
    {
        this.firePropertyChange(DATE_PROP_NAME, this.date, this.date = date);
    }
}
