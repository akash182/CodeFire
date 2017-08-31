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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class MainPageCtrl {
    Logger log;
    @FXML
    private Pane myFlow;

    @FXML
    private Button btnDrag;

    @FXML
    private Button btnDragJs;

    @FXML
    private Button dbCheck;

    private boolean isBackend;
    private boolean startLine = true;

    public boolean isStartLine() {
        return startLine;
    }

    public void setStartLine(boolean startLine) {
        this.startLine = startLine;
    }

    final double spacing = 30;
    // Timeline timeline;
    int i = 1;

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
                GridPane grid = createGridPane("LoginPage");

                Label userNameLabel = new Label("User Name : ");
                TextField userName = new TextField();

                Label passwordLabel = new Label("Password : ");
                PasswordField passwordField1 = new PasswordField();

                grid.add(userNameLabel, 1, 0);
                grid.add(userName, 2, 0);
                grid.add(passwordLabel, 1, 1);
                grid.add(passwordField1, 2, 1);
                DraggableNode node = createDragableNode(grid);

                myFlow.getChildren().add(node);

            } else if (db.getString().equals("Empty Check")) {
                GridPane grid = createGridPane("EmptyCheck");
                Label emptyCheck = new Label("Empty Check ");
                grid.add(emptyCheck, 1, 1);
                DraggableNode node = createDragableNode(grid);
                myFlow.getChildren().add(node);
            } else if (db.getString().equals("Alert")) {
                GridPane grid = createGridPane("AlertBox");
                Label emptyCheck = new Label("Alert Box");
                grid.add(emptyCheck, 1, 1);
                DraggableNode node = createDragableNode(grid);
                myFlow.getChildren().add(node);
            } else if (db.getString().equals("Check in DB")) {
                GridPane grid = createGridPane("dbCheck");
                Label emptyCheck = new Label("User DB Check");
                grid.add(emptyCheck, 1, 1);
                DraggableNode node = createDragableNode(grid);
                isBackend = true;
                myFlow.getChildren().add(node);
            }

            System.out.println("onDragDropped");
            de.setDropCompleted(true);
        } else {
            de.setDropCompleted(false);
        }

        de.consume();

    }

    private DraggableNode createDragableNode(GridPane grid) {
        DraggableNode node = new DraggableNode();
        node.setPrefSize(300, 110);
        // define the style via css
        node.getStyleClass().add("grid");
        // position the node
        // node.setLayoutX(spacing * (i + 1) + node.getPrefWidth() * i);
        // node.setLayoutY(spacing);
        node.getChildren().add(grid);

        // KeyValue initKeyValue = new KeyValue(node.translateXProperty(), node.getLayoutX());
        // KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
        //
        // KeyValue endKeyValue = new KeyValue(node.translateXProperty(), myFlow.getScene().getWidth() + 700);
        // KeyFrame endFrame = new KeyFrame(Duration.seconds(0.2), endKeyValue);
        //
        // timeline = new Timeline(initFrame, endFrame);
        //
        // timeline.setCycleCount(0);
        return node;
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
        for (Node parent : childrens) {
            if (!(parent instanceof Line)) {
                DraggableNode dg1 = (DraggableNode) parent;
                if (checkForInitNode(dg1)) {
                    System.out.println("Root found" + parent.getId());

                }
                ObservableList<Node> grandChildren = ((Parent) parent).getChildrenUnmodifiable();
                for (Node node : grandChildren) {
                    if (null != node.getId() && node.getId().equals("LoginPage")) {
                        body.append("<form id=\"loginForm\"");
                        if (isBackend) {
                            body.append("method=\"post\" action=\"Login\"> ");
                        } else {
                            body.append("> ");
                        }
                        body.append("<div class=\"container\">" + "<label><b>Username</b></label>"
                                + " <input type=\"text\" placeholder=\"Enter Username\" id=\"uname\" name=\"username\" >"
                                + " <label><b>Password</b></label>"
                                + "  <input type=\"password\" placeholder=\"Enter Password\" id=\"psw\"  name=\"password\" >" + " </div>"
                                + " <div class=\"container\" style=\"background-color:#f1f1f1\">"
                                + " <button id=\"btnSubmit\" type=\"button\" >Login</button>"
                                + "   <button type=\"button\" class=\"cancelbtn\">Cancel</button>" + " </div>" + "</form>");
                    } else if (null != node.getId() && node.getId().equals("EmptyCheck")) {
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
        ;

    }

    private boolean checkForInitNode(DraggableNode parent) {
        boolean isRoot = true;
        Map<Line, Boolean> map = parent.getConnectedLines();
        Iterator<Entry<Line, Boolean>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Line, Boolean> curr = it.next();
            if (!curr.getValue()) {
                isRoot = false;
            }
        }
        return isRoot;
    }

    private GridPane createGridPane(String id) {
        GridPane newGrid = null;
        try {

            newGrid = FXMLLoader.load(getClass().getResource("/fxmls/GridPaneForBox.fxml"));
            newGrid.setId(id);
            newGrid.setMaxSize(300, 110);
            Button btn = new Button("o/p");
            newGrid.add(btn, 3, 3);
            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Bounds n1InCommonAncestor = getRelativeBounds(btn, myFlow);
                    Point2D n1Center = getCenter(n1InCommonAncestor);
                    DraggableNode dg = (DraggableNode) btn.getParent().getParent();
                    if (isStartLine()) {
                        createLine(n1Center, null, dg);
                        setStartLine(false);
                    } else {
                        createLine(null, n1Center, dg);
                        setStartLine(true);
                    }
                }
            });
        } catch (

        IOException e) {
            log.info("Error " + e);
        }
        return newGrid;
    }

    private Bounds getRelativeBounds(Node node, Node relativeTo) {
        Bounds nodeBoundsInScene = node.localToScene(node.getBoundsInLocal());
        return relativeTo.sceneToLocal(nodeBoundsInScene);
    }

    private Point2D getCenter(Bounds b) {
        return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
    }

    double a = 0, b = 0, c = 0, d = 0;
    DraggableNode source = null;
    DraggableNode target = null;

    private void createLine(Point2D n1Center, Point2D n2Center, DraggableNode dg) {

        if (n1Center != null) {
            a = n1Center.getX();
            b = n1Center.getY();
            dg.setSource(true);
            source = dg;
        } else if (n2Center != null) {
            c = n2Center.getX();
            d = n2Center.getY();
            dg.setSource(false);
            target = dg;
        }
        double eps = 0.001;
        if (!(Math.abs(a - 0) < eps) && !(Math.abs(b - 0) < eps) && !(Math.abs(c - 0) < eps) && !(Math.abs(d - 0) < eps)) {
            Line line = new Line(a, b, c, d);
            line.setId(Integer.toString(i));
            i++;
            source.setLine(line);
            target.setLine(line);
            myFlow.getChildren().add(line);
            a = 0;
            b = 0;
            c = 0;
            d = 0;
        }
    }

    @FXML
    void initialize() {
        // you could initialize
        // JavaFX Controls here
    }

}
