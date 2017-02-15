package download;


import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.SwingWorker;
import ui.UIManager;
import utils.CustomLogger;

public class DownloaderWorkerThread extends SwingWorker<Boolean, Void> {

    final String url = "https://api.flickr.com/services/feeds/photos_public.gne";
    final String urlWithTag = "https://api.flickr.com/services/feeds/photos_public.gne?tags=";
    String tag = "";
    Vector<String> entryIDs;

    public Boolean isWorking = false;

    @Override
    protected Boolean doInBackground() throws Exception {
        CustomLogger.logger.log(Level.INFO, "Started data downloading");
        isWorking = true;
        String usedURL = "";
        if (tag.length() != 0) {
            usedURL = urlWithTag + tag;
        } else {
            usedURL = url;
        }
        try {
            entryIDs = FileManager.parseXML(download.Network.sendHTTPGet(usedURL));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DownloaderWorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        CustomLogger.logger.log(Level.INFO, "Done data downloading tag = " + tag);
        tag = "";
        isWorking = false;
        return null;
    }

    @Override
    protected void done() {
        UIManager.getInstance().downloadDone(entryIDs);
    }

    public void setTag(String t) {
        tag = t;
    }
}
