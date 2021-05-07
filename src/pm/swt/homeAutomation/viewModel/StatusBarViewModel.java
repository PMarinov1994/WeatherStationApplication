package pm.swt.homeAutomation.viewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Display;

import pm.swt.homeAutomation.model.BaseModel;
import pm.swt.homeAutomation.model.StatusBar;
import pm.swt.homeAutomation.utils.TimeKeeper;


public class StatusBarViewModel extends BaseModel
{
    public static final String TIME_PROP_NAME = "time";
    public static final String MESSAGE_PROP_NAME = "message";
    public static final String CAM_1_PROP_NAME = "cam1Status";
    public static final String RELAY_ONE_STATE = "relayOneState";
    public static final String RELAY_TWO_STATE = "relayTwoState";

    private StatusBar model;
    private TimeKeeper timeKeeper;

    private String time;
    private String message;

    private boolean cam1Status;
    private boolean relayOneState;
    private boolean relayTwoState;

    private PropertyChangeListener dateChangeListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            Date newTime = (Date) evt.getNewValue();
            setTime(newTime);
        }
    };

    private PropertyChangeListener messageChangeListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            setMessage((String) evt.getNewValue());
        }
    };


    private PropertyChangeListener cam1StatusChangedListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            setCam1Status((boolean) evt.getNewValue());
        }
    };

    private PropertyChangeListener relayOneStatusChangedListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            setRelayOneState((boolean) evt.getNewValue());
        }
    };

    private PropertyChangeListener relayTwoStatusChangedListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            setRelayTwoState((boolean) evt.getNewValue());
        }
    };

    public StatusBarViewModel(StatusBar model)
    {
        this.model = model;
        this.model.addPropertyChangeListener(StatusBar.DATE_PROP_NAME, this.dateChangeListener);
        this.model.addPropertyChangeListener(StatusBar.MESSAGE_PROP_NAME, this.messageChangeListener);
        this.model.addPropertyChangeListener(StatusBar.CAM_1_ONLINE_NAME, this.cam1StatusChangedListener);
        this.model.addPropertyChangeListener(StatusBar.RELAY_ONE_STATE, this.relayOneStatusChangedListener);
        this.model.addPropertyChangeListener(StatusBar.RELAY_TWO_STATE, this.relayTwoStatusChangedListener);

        this.setTime(this.model.getDate());
        
        this.timeKeeper = new TimeKeeper();
        this.timeKeeper.startRunning(this.model);

        this.setCam1Status(this.model.getCam1Online());
        this.setRelayOneState(this.model.getRelayOneState());
        this.setRelayTwoState(this.model.getRelayTwoState());
    }



    public void dispose()
    {
        this.model.removePropertyChangeListener(StatusBar.DATE_PROP_NAME, this.dateChangeListener);
        this.model.removePropertyChangeListener(StatusBar.MESSAGE_PROP_NAME, this.messageChangeListener);
        this.timeKeeper.stopRunning();
    }



    public String getTime()
    {
        return time;
    }



    public void setTime(Date time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        this.setTime(dateFormat.format(time));
    }



    public void setTime(String time)
    {
        this.dispatchEvent(() -> this.firePropertyChange(TIME_PROP_NAME, this.time, this.time = time));
    }



    public String getMessage()
    {
        return message;
    }



    public void setMessage(String message)
    {
        this.dispatchEvent(() -> this.firePropertyChange(MESSAGE_PROP_NAME, this.message, this.message = message));
    }

    public boolean getCam1Status()
    {
        return this.cam1Status;
    }



    public void setCam1Status(boolean cam1Status)
    {
        this.dispatchEvent(() -> this.firePropertyChange(CAM_1_PROP_NAME, this.cam1Status, this.cam1Status = cam1Status));
    }



    public boolean getRelayOneState()
    {
        return this.relayOneState;
    }



    public void setRelayOneState(boolean relayOneState)
    {
        this.dispatchEvent(() -> this.firePropertyChange(RELAY_ONE_STATE, this.relayOneState, this.relayOneState = relayOneState));
    }



    public boolean getRelayTwoState()
    {
        return this.relayTwoState;
    }



    public void setRelayTwoState(boolean relayTwoState)
    {
        this.dispatchEvent(() -> this.firePropertyChange(RELAY_TWO_STATE, this.relayTwoState, this.relayTwoState = relayTwoState));
    }



    protected void dispatchEvent(Runnable event)
    {
        Display display = Display.getCurrent();
        if (display == null)
            display = Display.getDefault();

        if (Thread.currentThread() != display.getThread())
        {
            display.asyncExec(new Runnable()
            {

                @Override
                public void run()
                {
                    event.run();
                }
            });
        }
        else
            event.run();
    }
}
