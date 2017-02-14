package download;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import utils.Logger;

public class ImageDownloaderThread extends Thread {

    String url = "https://api.flickr.com/services/feeds/photos_public.gne";
    String urlWithTag = "https://api.flickr.com/services/feeds/photos_public.gne?tags=";

    String tag = "";

    @Override
    public void run() {
        Logger.log("Started image downloading");

        String usedURL = "";
        if (tag.length() != 0) {
            usedURL = urlWithTag + tag;
        } else {
            usedURL = url;
        }
        try {
            FileManager.parseXML(download.Network.sendHTTPGet(usedURL));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ImageDownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initializeTag(String t) {
        tag = t;
    }
}
