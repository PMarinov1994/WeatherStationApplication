package pm.swt.homeAutomation.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import pm.swt.homeAutomation.configurator.ConfigurationFileManager;
import pm.swt.homeAutomation.configurator.IConfigurationChanged;
import pm.swt.homeAutomation.model.ConfigurationModel;
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
        private long bufferTime = 10 /* seconds */ * 1000 /* milliseconds */;
        private volatile int errorsBeforeFail;

        private ConfigurationFileManager configManager;



        public TrackerWorker()
        {
            this.configManager = (ConfigurationFileManager) DependencyIndector.getInstance()
                    .resolveInstance(GlobalResources.CONFIGURATION_FILE_MANAGER_NAME);

            this.errorsBeforeFail = this.configManager.getConfig().getMissedReportsCount();
            this.configManager.addConfigurationChangeSubscriber(new IConfigurationChanged()
            {

                @Override
                public void configurationChanged(ConfigurationModel newConfig)
                {
                    errorsBeforeFail = newConfig.getMissedReportsCount();
                }
            });
        }



        @Override
        public void run()
        {
            this.changeStatus(StationStatus.STANDBY_STATUS);

            // Wait for the checkInTime to be initialized.
            while (tracking)
            {
                if (checkInTime >= 0)
                    break;

                Thread.yield();
            }

            // After we have a checkInTime set we can start tracking
            // the station status.
            int failedCheckins = 0;
            boolean ignoreNoCheckin = false;
            while (tracking)
            {
                if (checkedIn || ignoreNoCheckin)
                {
                    nextCheckIn = new Date((new Date()).getTime() + checkInTime);
                    ignoreNoCheckin = false;
                    checkedIn = false;
                }

                // Give some buffer time for the message to come
                // before sounding the alarm.
                //
                // Sometimes stations fail to report every single time.
                // Its OK to skip some check-ins and not mark them as errors.
                if (nextCheckIn.getTime() - (new Date()).getTime() + bufferTime < 0)
                {
                    if (++failedCheckins < this.errorsBeforeFail)
                    {
                        ignoreNoCheckin = true;
                    }
                    else
                    {
                        this.changeStatus(StationStatus.ERROR_STATUS);
                        failedCheckins = 0;
                        ignoreNoCheckin = false;
                    }
                }
                else
                    this.changeStatus(StationStatus.OK_STATUS);

                Thread.yield();
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
