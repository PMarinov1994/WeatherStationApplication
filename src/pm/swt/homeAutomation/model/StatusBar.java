package pm.swt.homeAutomation.model;

import java.util.Date;


public class StatusBar extends BaseModel
{
    public static final String MESSAGE_PROP_NAME = "message";
    public static final String DATE_PROP_NAME = "date";
    public static final String CAM_1_ONLINE_NAME = "cam1Online";

    public static final String RELAY_ONE_STATE = "relayOneState";
    public static final String RELAY_TWO_STATE = "relayTwoState";

    private Date date = new Date();
    private String message = "Status";
    private boolean cam1Online = true;

    private boolean relayOneState = false;
    private boolean relayTwoState = false;


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



    public boolean getCam1Online()
    {
        return this.cam1Online;
    }

    

    public void setCam1Online(boolean cam1Online)
    {
        this.firePropertyChange(CAM_1_ONLINE_NAME, this.cam1Online, this.cam1Online = cam1Online);
    }


    public boolean getRelayOneState()
    {
        return this.relayOneState;
    }



    public void setRelayOneState(boolean relayOneState)
    {
        this.firePropertyChange(RELAY_ONE_STATE, this.relayOneState, this.relayOneState = relayOneState);
    }



    public boolean getRelayTwoState()
    {
        return this.relayTwoState;
    }



    public void setRelayTwoState(boolean relayTwoState)
    {
        this.firePropertyChange(RELAY_TWO_STATE, this.relayTwoState, this.relayTwoState = relayTwoState);
    }
}
