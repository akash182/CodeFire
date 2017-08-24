/*
 * Creation : 21-Aug-2017
 */
package apps;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainPageCtrl {
    Logger log;
    @FXML
    private HBox myFlow;

    @FXML
    private Button btnDrag;

    @FXML
    private Button btnDragJs;

    @FXML
    private Button dbCheck;

    private boolean isBackend;

    @FXML
    void nodeDragOver(DragEvent de) {
        /* data is dragged over the target */

        Dragboard db = de.getDragboard();
        if (db.hasString()) {
            de.acceptTransferModes(TransferMode.COPY);
            System.out.println("onDragOver");
        }

        de.consume();

    }

    @FXML
    void nodeDropped(DragEvent de) {

        Dragboard db = de.getDragboard();

        if (db.hasString()) {
            if (db.getString().equals("Login page")) {
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                Label userNameLabel = new Label("User Name : ");
                TextField userName = new TextField();

                Label passwordLabel = new Label("Password : ");
                PasswordField passwordField1 = new PasswordField();
                grid.getStyleClass().add("grid");
                grid.add(userNameLabel, 1, 0);
                grid.add(userName, 2, 0);
                grid.add(passwordLabel, 1, 1);
                grid.add(passwordField1, 2, 1);
                grid.setMaxSize(300, 110);
                grid.setId("LoginPage");
                myFlow.getChildren().add(grid);
            } else if (db.getString().equals("Empty Check")) {
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                Label emptyCheck = new Label("Empty Check ");
                grid.getStyleClass().add("grid");
                grid.add(emptyCheck, 1, 1);
                grid.setMaxSize(300, 110);
                grid.setId("EmptyCheck");
                myFlow.getChildren().add(grid);
            } else if (db.getString().equals("Alert")) {
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                Label emptyCheck = new Label("Alert Box");
                grid.getStyleClass().add("grid");
                grid.add(emptyCheck, 1, 1);
                grid.setMaxSize(300, 110);
                grid.setId("AlertBox");
                myFlow.getChildren().add(grid);
            } else if (db.getString().equals("Check in DB")) {
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                Label emptyCheck = new Label("User DB Check");
                grid.getStyleClass().add("grid");
                grid.add(emptyCheck, 1, 1);
                grid.setMaxSize(300, 110);
                grid.setId("dbCheck");
                isBackend = true;
                myFlow.getChildren().add(grid);
            }

            System.out.println("onDragDropped");
            de.setDropCompleted(true);
        } else {
            de.setDropCompleted(false);
        }

        de.consume();

    }

    @FXML
    void startDrag(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard db = btnDrag.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(btnDrag.getText());
        db.setContent(content);

        event.consume();

    }

    @FXML
    void startDragJs(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard db = btnDragJs.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(btnDragJs.getText());
        db.setContent(content);

        event.consume();

    }

    @FXML
    void startDragDbcheck(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard db = dbCheck.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(dbCheck.getText());
        db.setContent(content);

        event.consume();

    }

    @FXML
    void handleCreateButtonAction(ActionEvent e) {
        ObservableList<Node> childrens = myFlow.getChildren();
        // Convert the string to a
        // byte array.
        StringBuilder body = new StringBuilder();
        for (Node node : childrens) {
            if (node.getId().equals("LoginPage")) {
                body.append("<form id=\"loginForm\"");
                if (isBackend) {
                    body.append("method=\"post\" action=\"Login\"> ");
                } else {
                    body.append("> ");
                }
                body.append("<div class=\"container\">" + "<label><b>Username</b></label>"
                        + " <input type=\"text\" placeholder=\"Enter Username\" id=\"uname\" name=\"username\" >" + " <label><b>Password</b></label>"
                        + "  <input type=\"password\" placeholder=\"Enter Password\" id=\"psw\"  name=\"password\" >" + " </div>"
                        + " <div class=\"container\" style=\"background-color:#f1f1f1\">"
                        + " <button id=\"btnSubmit\" type=\"button\" >Login</button>"
                        + "   <button type=\"button\" class=\"cancelbtn\">Cancel</button>" + " </div>" + "</form>");
            } else if (node.getId().equals("EmptyCheck")) {
                body.append("<script>" + "(function(){" + "var subBtn=document.getElementById(\"btnSubmit\");subBtn.onclick=function(){"
                        + "var uname=document.getElementById(\"uname\").value;"
                        + "var psw=document.getElementById(\"psw\").value;if(uname == '' || psw== ''){alert(\"Please Enter all mandatory values\");"
                        + "}else{alert(\"Success\");");
                if (isBackend) {
                    body.append(" document.getElementById(\"loginForm\").submit(); ");
                }
                body.append("};" + "};})()" + "</script>");
            }
        }

        String headerPart = "<html>" + "<head>" + "<title>CodeFire</title>" + "</head><body>";
        String bodypart = body.toString();
        String endPart = "</body>" + "</html>";
        byte datah[] = headerPart.getBytes();
        byte datab[] = bodypart.getBytes();
        byte datae[] = endPart.getBytes();
        Path htmlPath = Paths.get("D:\\WorkSpace\\CodeFire0.0.5\\htmls\\index.html");

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(htmlPath))) {
            out.write(datah, 0, datah.length);
            out.write(datab, 0, datab.length);
            out.write(datae, 0, datae.length);
        } catch (IOException x) {
            System.err.println(x);
        }

    }

    @FXML
    void initialize() {
        // you could initialize
        // JavaFX Controls here
    }

}
