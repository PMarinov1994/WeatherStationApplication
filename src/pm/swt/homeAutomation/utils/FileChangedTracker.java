package pm.swt.homeAutomation.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;


public class FileChangedTracker
{
    private Worker worker;
    private Thread thread;



    public FileChangedTracker(String path)
    {
        this.worker = new Worker(path);
        this.thread = new Thread(this.worker);
    }



    public void startTracker()
    {
        if (!this.worker.isRunning)
        {
            this.worker.isRunning = true;
            this.thread.start();
        }
        else
            System.err.println("File tracker can only be started once!");
    }



    public void stopTracker()
    {
        if (this.worker.isRunning)
        {
            try
            {
                this.worker.isRunning = false;
                this.thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }



    private class Worker implements Runnable
    {
        private Path path;
        private WatchService watcher;
        private boolean isRunning;



        public Worker(String path)
        {
            this.path = Paths.get(path);

            Path fileFolder = this.path.getParent();

            try
            {
                this.watcher = fileFolder.getFileSystem().newWatchService();
                fileFolder.register(this.watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }



        @Override
        public void run()
        {
            while (this.isRunning)
            {
                WatchKey watchKey = this.watcher.poll();

                if (watchKey == null)
                    continue;

                try
                {
                    // Ignore multiple occurrences of the same event
                    // https://stackoverflow.com/questions/16777869/java-7-watchservice-ignoring-multiple-occurrences-of-the-same-event
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                List<WatchEvent<?>> events = watchKey.pollEvents();
                for (WatchEvent<?> watchEvent : events)
                {
                    if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
                    {
                        final Path changed = (Path) watchEvent.context();
                        if (changed.equals(this.path.getFileName()))
                        {
                            // TODO Fire notify.
                            System.err.println("Our File changed");
                        }
                    }
                }

                watchKey.reset();
                Thread.yield();
            }
        }
    }
}
