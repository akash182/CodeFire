/*
 * Creation : 21-Aug-2017
 */
package apps;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    Logger log;

    @Override
    public void start(Stage stage) {

        try {
            Context cont = Context.getInstance();
            // URL url = getClass().getResource("/fxmls/MainPage.fxml");
            BorderPane root = cont.getMainPage(); // FXMLLoader.load(url);
            VBox menuBar = FXMLLoader.load(getClass().getResource("/fxmls/Menubar.fxml"));
            root.setTop(menuBar);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("CodeFire");
            stage.getIcons().add(new Image("/images/icon.jpg"));
            stage.show();
        } catch (Exception e) {
            log.warning("Exception Occured in start method : " + e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
