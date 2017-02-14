package flickmage;

import download.ImageDownloaderThread;
import static java.lang.Boolean.TRUE;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlickMage extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ImageDownloaderThread th = new ImageDownloaderThread();
        th.initializeTag("car");
        th.run();
        
        launch(args);
        
    }
}
