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

    private StatusBar model;
    private TimeKeeper timeKeeper;

    private String time;

    private PropertyChangeListener listener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            switch (evt.getPropertyName())
            {
            case StatusBar.DATE_PROP_NAME:
                Date newTime = (Date) evt.getNewValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                setTime(dateFormat.format(newTime));
                break;
            default:
                break;
            }
        }
    };



    public StatusBarViewModel(StatusBar model)
    {
        this.model = model;
        this.model.addPropertyChangeListener(this.listener);

        this.timeKeeper = new TimeKeeper();
        this.timeKeeper.startRunning(this.model);
    }



    public void dispose()
    {
        this.model.removePropertyChangeListener(this.listener);
        this.timeKeeper.stopRunning();
    }



    public String getTime()
    {
        return time;
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
}
