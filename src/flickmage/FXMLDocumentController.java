package flickmage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ui.UIManager;
import utils.CustomLogger;

/**
 * Main class controller for UI items
 *
 * @author Seitan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    public Pane ImageContainerPane;
    @FXML
    public Button SearchByTagButton;
    @FXML
    public TextField TagTextField;
    @FXML
    public Circle LoadingCircle;
    @FXML
    public TextArea DetailsTextArea;
    @FXML
    public ImageView LogoImage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIManager.getInstance().initializeController(this);
        if (LogoImage != null) {
            //wizzia
            LogoImage.setImage(new Image("file:resources\\logo\\logo1_1.png"));
        }
        
    }
    
    @FXML
    private void SearchByTag(ActionEvent event) {
        DetailsTextArea.setText("");
        if (TagTextField != null) {
            CustomLogger.logger.log(Level.INFO, "SearchByTag tag = " + TagTextField.getText());
            UIManager.getInstance().startDownloadByTag(TagTextField.getText());
        } else {
            CustomLogger.logger.log(Level.INFO, "SearchByTag no tag");
            UIManager.getInstance().startDownloadByTag("");
        }
    }
    
    @FXML
    private void SortByDateTaked(ActionEvent event) {
        CustomLogger.logger.log(Level.INFO, "SortByDateTaked");
        UIManager.getInstance().sortByDateTaken(true);
    }
    
    @FXML
    private void SortByDatePublished(ActionEvent event) {
        CustomLogger.logger.log(Level.INFO, "SortByDatePublished");
        UIManager.getInstance().sortByDateTaken(false);
    }
    
    public void SetDownloadStatusDone(int done) {
        CustomLogger.logger.log(Level.INFO, "SortByDatePublished done = " + done);
        if (LoadingCircle != null) {
            if (done == 0) {
                LoadingCircle.setFill(Color.YELLOW);
            } else {
                LoadingCircle.setFill(Color.GREEN);
            }
        }
    }
    
    @FXML
    private void SaveFileToSystemGallery(ActionEvent event) {
        CustomLogger.logger.log(Level.INFO, "SaveFileToSystemGallery");
        UIManager.getInstance().SaveFileToSystemGallery();
    }
    
    @FXML
    private void OpenFileInBrowser(ActionEvent event) {
        CustomLogger.logger.log(Level.INFO, "OpenFileInBrowser");
        UIManager.getInstance().OpenFileInBrowser();
    }
    
    @FXML
    private void ShareFileViaMail(ActionEvent event) {
        CustomLogger.logger.log(Level.INFO, "ShareFileViaMail");
        UIManager.getInstance().ShareFileViaMail();
    }
    
    @FXML
    private void SearchButtonEnter(ActionEvent ae) {
        SearchByTag(null);
    }
}
