/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package download;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import utils.Logger;

public class Network {

    public static String sendHTTPGet(String usedURL) throws IOException {
        final String USER_AGENT = "Mozilla/5.0";

        URL obj = null;
        try {
            obj = new URL(usedURL);
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(ImageDownloaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        Logger.log("\nSending 'GET' request to URL : " + usedURL);
        Logger.log("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Logger.log(response.toString());

        //print result
        return response.toString();
    }

    public static void downloadImage(String url, String fileName) {
        File f = new File(fileName);
        if(f.exists()) return;

        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(fileName));
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
