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

    private StatusBar model;
    private TimeKeeper timeKeeper;

    private String time;
    private String message;

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



    public StatusBarViewModel(StatusBar model)
    {
        this.model = model;
        this.model.addPropertyChangeListener(StatusBar.DATE_PROP_NAME, this.dateChangeListener);
        this.model.addPropertyChangeListener(StatusBar.MESSAGE_PROP_NAME, this.messageChangeListener);

        this.setTime(this.model.getDate());
        
        this.timeKeeper = new TimeKeeper();
        this.timeKeeper.startRunning(this.model);
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
                    setTime(time);
                }
            });
        }
        else
        {
            this.firePropertyChange(TIME_PROP_NAME, this.time, this.time = time);
        }
    }



    public String getMessage()
    {
        return message;
    }



    public void setMessage(String message)
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
                    setMessage(message);
                }
            });
        }
        else
            this.firePropertyChange(MESSAGE_PROP_NAME, this.message, this.message = message);
    }
}
