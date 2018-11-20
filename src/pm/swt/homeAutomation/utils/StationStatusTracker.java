package pm.swt.homeAutomation.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import pm.swt.homeAutomation.model.ITrackableStation;


public class StationStatusTracker
{
    private ITrackableStation station;
    private Thread thread;

    private volatile boolean tracking = false;
    private volatile int checkInTime = -1; // in milliseconds.

    private volatile boolean checkedIn = false;

    private PropertyChangeListener changeListener = new PropertyChangeListener()
    {

        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            switch (evt.getPropertyName())
            {
            case ITrackableStation.STATUS_PROP_NAME:
                // Ignore the status change. Only we are setting it.
                break;
            case ITrackableStation.REFRESH_INTERVAL_PROP_NAME:
                // The station will give us the checkIn time in minutes.
                // Convert it to milliseconds.
                checkInTime = (int) evt.getNewValue() /* minutes */ * 60 /* seconds */ * 1000 /* milliseconds */;
            default:
                checkedIn = true;
                break;
            }
        }
    };



    public StationStatusTracker(ITrackableStation station)
    {
        this.station = station;
    }



    public void startTracking()
    {
        this.station.addPropertyChangeListener(this.changeListener);

        this.thread = new Thread(new TrackerWorker());

        this.tracking = true;
        this.thread.start();
    }



    public void stopTracking()
    {
        this.station.removePropertyChangeListener(this.changeListener);
        this.tracking = false;

        try
        {
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            this.thread.interrupt();
        }
    }



    private class TrackerWorker implements Runnable
    {
        private Date nextCheckIn;
        private long bufferTime = 30 /* seconds */ * 1000 /* milliseconds */;



        @Override
        public void run()
        {
            this.changeStatus(StationStatus.STANDBY_STATUS);

            // Wait for the checkInTime to be initialized.
            while (tracking)
            {
                if (checkInTime >= 0)
                    break;

                try
                {
                    Thread.sleep(20);
                }
                catch (InterruptedException e)
                {
                    return;
                }
            }

            // After we have a checkInTime set we can start tracking
            // the station status.
            while (tracking)
            {
                if (checkedIn)
                {
                    nextCheckIn = new Date((new Date()).getTime() + checkInTime);
                    checkedIn = false;
                }

                // Give some buffer time for the message to come
                // before sounding the alarm.
                if (nextCheckIn.getTime() - (new Date()).getTime() + bufferTime < 0)
                    this.changeStatus(StationStatus.ERROR_STATUS);
                else
                    this.changeStatus(StationStatus.OK_STATUS);

                try
                {
                    Thread.sleep(20);
                }
                catch (InterruptedException e)
                {
                    return;
                }
            }
        }



        private void changeStatus(StationStatus status)
        {
            StationStatus oldStatus = station.getStatus();
            if (oldStatus != status)
                station.setStatus(status);
        }
    }
}
