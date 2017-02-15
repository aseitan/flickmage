package ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.logging.Level;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import utils.CustomLogger;

/**
 * ImageViewWrapper - Data structure used to store info about Flickr posts: URL, metadata, etc 
 * 
 * @author Seitan
 */
public class ImageViewWrapper {

    //add the metadata here
    Date takenDate;
    Date publishDate;
    String id;
    String title;
    ImageView imageView;
    HBox pictureRegion;
    EventHandler handler;
    String url;

    final String dateFormat = "yyyy-MM-dd hh:mm:ss";

    ImageViewWrapper(String imgID) {
        CustomLogger.logger.log(Level.INFO, "ImageViewWrapper id = " + imgID);
        id = imgID;
        takenDate = new Date();
        publishDate = new Date();

        handler = (EventHandler<MouseEvent>) (final MouseEvent keyEvent) -> {
            CustomLogger.logger.log(Level.INFO, "EventHandler click");
            UIManager.getInstance().imageSelected(this);
        };
    }

    public String getID() {
        if (id == null) {
            return "";
        }
        return id;
    }

    public void setURL(String u) {
        CustomLogger.logger.log(Level.INFO, "ImageViewWrapper url = " + u);
        url = u;
    }

    public String getURL() {
        if (url == null) {
            return "";
        }
        return url;
    }

    public void setTitle(String t) {
        CustomLogger.logger.log(Level.INFO, "ImageViewWrapper title = " + t);
        title = t;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        }
        return title;
    }

    public void setTakenDateFromString(String d) {

        String dpartOne = d.substring(0, 9);
        String partTwo = d.substring(11, 18);
        d = dpartOne + " " + partTwo;
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            takenDate = df.parse(d);
        } catch (ParseException e) {
        }
    }

    public Date getTakenDate() {
        return takenDate;
    }

    public String getTakenDateAsString() {
        if (takenDate == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(takenDate);
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public String getPublishDateAsString() {
        if (publishDate == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(publishDate);
    }

    public void setPublishDateFromString(String d) {

        String dpartOne = d.substring(0, 9);
        String partTwo = d.substring(11, 18);
        d = dpartOne + " " + partTwo;

        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            publishDate = df.parse(d);
        } catch (ParseException e) {
        }
    }

    public void setSelected() {
        pictureRegion.setStyle("-fx-padding: 5;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;"
                + "-fx-border-insets: 3;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: blue;");
    }

    public void setUnselected() {
        pictureRegion.setStyle("-fx-padding: 5;"
                + "-fx-border-style: solid inside;"
                + "-fx-border-width: 1;"
                + "-fx-border-insets: 3;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: cyan;");
    }
}

class TakenDateComparator implements Comparator<ImageViewWrapper> {

    @Override
    public int compare(ImageViewWrapper o1, ImageViewWrapper o2) {

        Date takenDate1 = ((ImageViewWrapper) o1).getTakenDate();
        Date takenDate2 = ((ImageViewWrapper) o2).getTakenDate();
        if (takenDate1 == null || takenDate2 == null) {
            CustomLogger.logger.log(Level.SEVERE, "takenDate1 = " + takenDate1 + " takenDate2=" + takenDate2);
            return 0;
        }
        return takenDate1.compareTo(takenDate2);
    }
}

class PublishDateComparator implements Comparator<ImageViewWrapper> {

    @Override
    public int compare(ImageViewWrapper o1, ImageViewWrapper o2) {
        Date publishDate1 = ((ImageViewWrapper) o1).getPublishDate();
        Date publishDate2 = ((ImageViewWrapper) o2).getPublishDate();

        if (publishDate1 == null || publishDate2 == null) {
            CustomLogger.logger.log(Level.SEVERE, "publishDate1 = " + publishDate1 + " publishDate2=" + publishDate2);
            return 0;
        }
        return publishDate1.compareTo(publishDate2);
    }
}
