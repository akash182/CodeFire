/*
 * Creation : 21-Aug-2017
 */
package apps;

import java.net.URL;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    Logger log;

    @Override
    public void start(Stage stage) {
        try {
            URL url = getClass().getResource("/fxmls/MainPage.fxml");
            BorderPane root = FXMLLoader.load(url);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("JavaFX Drop Image");
            stage.show();
        } catch (Exception e) {
            log.warning("Exception Occured in start method : " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
