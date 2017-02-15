package ui;

import download.FileManager;
import download.ThreadManager;
import flickmage.FXMLDocumentController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UIManager {

    private static UIManager managerSingle = null;
    FXMLDocumentController controller = null;
    ArrayList<ImageViewWrapper> dataView;

    protected UIManager() {
        dataView = new ArrayList();
    }

    public static UIManager getInstance() {
        if (managerSingle == null) {
            managerSingle = new UIManager();
        }
        return managerSingle;
    }

    public void initializeController(FXMLDocumentController c) {
        controller = c;
    }

    private void loadImageFromWorkerThread(Vector<String> ids, Boolean fromWorkerThread) {

        if (fromWorkerThread) {
            dataView.clear();
        }

        for (int i = 0; i < ids.size(); i++) {
            String id = ids.elementAt(i);
            String path = "cache/" + id + ".jpg";
            File file = new File(path);
            if (controller.ImageContainerPane != null) {

                if (file.isDirectory()) {
                    continue;
                }
                Image image = null;
                try {
                    image = new Image(new FileInputStream(file), 100, 0, true, true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

                ImageViewWrapper imageViewWrapper = new ImageViewWrapper(id);
                FileManager.fillData(id, imageViewWrapper);
                imageViewWrapper.imageView = new ImageView(image);

                if (fromWorkerThread) {
                    dataView.add(imageViewWrapper);
                }

                imageViewWrapper.imageView.setOnMouseClicked(imageViewWrapper.handler);

                //avoid changing UI elements from worker thread. javafx does not allow that
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //i'm using fixed sizes because flickr call returns only 20 items. this is not scalable. YET
                        //imageView.setFitHeight(170);
                        imageViewWrapper.imageView.setFitWidth(100);

                        //imageView.maxHeight(10);
                        //imageView.maxWidth(10);
                        imageViewWrapper.imageView.preserveRatioProperty();
                        if (controller != null && controller.ImageContainerPane != null) {
                            if (!controller.ImageContainerPane.getChildren().contains(imageViewWrapper.imageView)) {
                                controller.ImageContainerPane.getChildren().add(imageViewWrapper.imageView);
                            }
                        }
                    }
                });

            }
        }
    }

    public void downloadDone(Vector<String> ids) {
        if (controller != null) {
            controller.SetDownloadStatusDone(1);
        }

        loadImageFromWorkerThread(ids, true);
    }

    public void startDownloadByTag(String tag) {
        if (controller != null && controller.ImageContainerPane != null) {
            controller.ImageContainerPane.getChildren().clear();
        }
        if (controller != null) {
            controller.SetDownloadStatusDone(0);
        }
        ThreadManager.executeWorkWithTag(tag);
    }

    public void startDownload() {
        if (controller != null) {
            controller.SetDownloadStatusDone(1);
        }
        ThreadManager.executeWork();
    }

    public void imageSelected(ImageViewWrapper wr) {
        if (controller != null && controller.DetailsTextArea != null) {
            controller.DetailsTextArea.setText("Title: " + wr.getTitle() + " \nPublished: " + wr.getPublishDateAsString() + "\nTaken: " + wr.getTakenDateAsString());
        }
    }

    public void sortByDateTaken(Boolean b) {

        if (b) {
            Collections.sort(dataView, new TakenDateComparator());
        } else {
            Collections.sort(dataView, new PublishDateComparator());
        }

        Vector<String> orderedIds = new Vector<String>();
        for (ImageViewWrapper vr : dataView) {
            orderedIds.add(vr.getID());
        }

        if (controller != null && controller.ImageContainerPane != null) {
            controller.ImageContainerPane.getChildren().clear();
        }

        loadImageFromWorkerThread(orderedIds, false);
    }

}
