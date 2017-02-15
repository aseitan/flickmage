package download;

public class ThreadManager {
    static DownloaderWorkerThread workerThread = null;    
        
    static public void executeWorkWithTag(String tag)
    {
        if(workerThread != null && workerThread.isWorking == true) 
        {
            return;
        }
        workerThread = new DownloaderWorkerThread();
        workerThread.setTag(tag);
        workerThread.execute();
    }
    
    static public void executeWork()
    {
        if(workerThread != null && workerThread.isWorking == true) 
        {
            return;
        }
        workerThread = new DownloaderWorkerThread();
        workerThread.execute();
    }
    
}
