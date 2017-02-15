package ui;

import download.FileManager;
import download.ThreadManager;
import flickmage.FXMLDocumentController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import utils.CustomLogger;
import utils.OSSpecific;

public class UIManager {

    private static UIManager managerSingle = null;
    FXMLDocumentController controller = null;
    ArrayList<ImageViewWrapper> dataView;
    ImageViewWrapper selectedItem = null;

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
                //safety check
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
        selectedItem = wr;
        if (controller != null && controller.DetailsTextArea != null) {
            controller.DetailsTextArea.setText("Title: " + wr.getTitle() + " \n\nPublished: " + wr.getPublishDateAsString() + "\n\nTaken: " + wr.getTakenDateAsString());
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

    public void SaveFileToSystemGallery() {
        if (selectedItem == null) {
            return;
        }

        //i know it is an ugly dialog, but its actually the only way. 
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        chooser.setDialogTitle("Browse to location ");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String sourcePath = "cache\\" + selectedItem.getID() + ".jpg";
            File sourceFile = new File(sourcePath);
            String destinationPath = chooser.getCurrentDirectory() + "\\" + selectedItem.getID() + ".jpg";
            CustomLogger.logger.log(Level.INFO, "source = " + sourcePath + " destinationPath = " + destinationPath);
            File destinationFile = new File(destinationPath);
            //OSSpecific.copyFile(file2copy, copied);
            OSSpecific.copyFile(sourceFile, destinationFile);
        } else {
            CustomLogger.logger.log(Level.INFO, "No Selection");
        }

    }

    public void OpenFileInBrowser() {
        if (selectedItem == null) {
            return;
        }
        try {
            OSSpecific.openWebpage(new URL(selectedItem.getURL()));

        } catch (MalformedURLException ex) {
            Logger.getLogger(UIManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ShareFileViaMail() {
        if (selectedItem == null) {
            return;
        }
    }
}
