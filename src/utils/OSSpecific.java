/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seitan
 */
public class OSSpecific {

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) {
        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(OSSpecific.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OSSpecific.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OSSpecific.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException ex) {
                    Logger.getLogger(OSSpecific.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException ex) {
                    Logger.getLogger(OSSpecific.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
