package pm.swt.homeAutomation.utils;

import java.util.Date;

import pm.swt.homeAutomation.model.StatusBar;


public class TimeKeeper
{
    private volatile boolean isRunning = false;
    private Thread thread;



    public void startRunning(StatusBar statusBar)
    {
        this.thread = new Thread(new Worker(statusBar));
        this.isRunning = true;
        this.thread.start();
    }



    public void stopRunning()
    {
        this.isRunning = false;
        try
        {
            this.thread.join(1000 * 3);
        }
        catch (InterruptedException e)
        {
            this.thread.interrupt();
        }
    }



    private class Worker implements Runnable
    {
        private StatusBar statusBarModel;



        public Worker(StatusBar statusBarModel)
        {
            this.statusBarModel = statusBarModel;
        }



        @Override
        public void run()
        {
            while (isRunning)
            {
                this.statusBarModel.setDate(new Date());

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

    }
}
