package flickmage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import ui.UIManager;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIManager.getInstance().initializeController(this);
    }

    @FXML
    private void SearchByTag(ActionEvent event) {
        UIManager.getInstance().startDownloadByTag(TagTextField.getText());
    }
    
    @FXML
    private void SortByDateTaked(ActionEvent event) {
        UIManager.getInstance().sortByDateTaken(true);
    }
    
    @FXML
    private void SortByDatePublished(ActionEvent event) {
        UIManager.getInstance().sortByDateTaken(false);
    }

    public void SetDownloadStatusDone(int done) {
        if (LoadingCircle != null) {
            if (done == 0) {
                LoadingCircle.setFill(Color.YELLOW);
            } else {
                LoadingCircle.setFill(Color.GREEN);
            }
        }
    }
}
