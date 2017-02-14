package flickmage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TilePane imageContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if (0==0)return;
        String path = "cache/";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if(imageContainer == null )
        {
            for (final File file : listOfFiles) {
                
                Image image = null;
                try {
                    image = new Image(new FileInputStream(file), 150, 0, true, true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }

                ImageView imageView = new ImageView(image);
                imageContainer.getChildren().add(imageView);
            }
        }
    }    
    
}
