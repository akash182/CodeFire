/*
 * Creation : 14-Sep-2017
 */
package apps;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class Context {
    BorderPane mainPage;
    private static Context instance = new Context();;

    public Context() {
        try {
            mainPage = FXMLLoader.load(getClass().getResource("/fxmls/MainPage.fxml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Context getInstance() {
        return instance;
    }

    public BorderPane getMainPage() {
        return mainPage;
    }

    public void setMainPage(BorderPane mainPage) {
        this.mainPage = mainPage;
    }
}