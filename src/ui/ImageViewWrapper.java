package ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImageViewWrapper {

    //add the metadata here
    Date takenDate;
    Date publishDate;
    String id;
    String title;
    ImageView imageView;
    EventHandler handler;
    String url;
    Boolean sortByDateTaken;

    final String dateFormat = "yyyy-MM-dd hh:mm:ss";

    ImageViewWrapper(String imgID) {
        id = imgID;
        takenDate = new Date();
        publishDate = new Date();
        sortByDateTaken = true;

        handler = (EventHandler<MouseEvent>) (final MouseEvent keyEvent) -> {
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
        url = u;
    }

    public String getURL() {
        if (url == null) {
            return "";
        }
        return url;
    }

    public void setTitle(String t) {
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

    public Date getPublishDate() {
        return publishDate;
    }

    public String getTakenDateAsString() {
        if (takenDate == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(takenDate);
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

    public String getPublishDateAsString() {
        if (publishDate == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(publishDate);
    }

    public void setSortByDateTaken(Boolean b) {
        sortByDateTaken = b;
    }

}

class TakenDateComparator implements Comparator<ImageViewWrapper> {

    @Override
    public int compare(ImageViewWrapper o1, ImageViewWrapper o2) {

        Date takenDate1 = ((ImageViewWrapper) o1).getTakenDate();
        Date takenDate2 = ((ImageViewWrapper) o2).getTakenDate();
        if (takenDate1 == null || takenDate2 == null) {
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
            return 0;
        }
        return publishDate1.compareTo(publishDate2);
    }
}
