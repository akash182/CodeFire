/*
 * Creation : 21-Aug-2017
 */
package apps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    @FXML
    private Button check;

    @FXML
    private VBox rightPanel;

    @FXML
    private Button alertBox;

    private boolean isBackend;
    private boolean startLine = true;
    double a = 0;
    double b = 0;
    double c = 0;
    double d = 0;
    DraggableNode source = null;
    DraggableNode target = null;

    public boolean isStartLine() {
        return startLine;
    }

    public void setStartLine(boolean startLine) {
        this.startLine = startLine;
    }

    final double spacing = 30;
    // Timeline timeline;
    int i = 1;
    int dgNodeCount = 1;

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
            if (db.getString().equals("Html page")) {
                GridPane grid = createGridPane("htmlPage");

                Label userNameLabel = new Label("User Name : ");
                TextField userName = new TextField();

                Label passwordLabel = new Label("Password : ");
                PasswordField passwordField1 = new PasswordField();

                grid.add(userNameLabel, 1, 0);
                grid.add(userName, 2, 0);
                grid.add(passwordLabel, 1, 1);
                grid.add(passwordField1, 2, 1);
                DraggableNode node = createDragableNode(grid);
                node.setId("fHtml");
                Properties prop = new Properties();
                myFlow.getChildren().add(node);

            } else if (db.getString().equals("Check")) {
                GridPane grid = createGridPane("Check");
                grid.setPrefHeight(240);
                grid.setPrefWidth(120);

                Label emptyCheck = new Label("Check ");
                emptyCheck.setStyle("-fx-rotate : -45");
                grid.add(emptyCheck, 1, 1);

                DraggableNode node = createDragableNode(grid);

                node.setId("fJs");
                File fjs = new File("./src/properties/fJs.properties");
                try {
                    boolean created = fjs.createNewFile();
                    if (created) {
                        System.out.println("File Created Succesfully");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                node.setStyle("-fx-rotate : 45;");
                myFlow.getChildren().add(node);
            } else if (db.getString().equals("Alert")) {
                GridPane grid = createGridPane("AlertBox");
                Label emptyCheck = new Label("Alert Box");
                grid.add(emptyCheck, 1, 1);
                DraggableNode node = createDragableNode(grid);

                node.setId("fAlert");
                File fjs = new File("./src/properties/fAlert.properties");
                try {
                    boolean created = fjs.createNewFile();
                    if (created) {
                        System.out.println("File Created Succesfully");
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                myFlow.getChildren().add(node);
            } else if (db.getString().equals("Check in DB")) {
                GridPane grid = createGridPane("dbCheck");
                Label emptyCheck = new Label("User DB Check");
                grid.add(emptyCheck, 1, 1);
                DraggableNode node = createDragableNode(grid);

                node.setId("fJava");
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
        node.setDgId(dgNodeCount);
        // node.setPrefSize(300, 110);
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
        dgNodeCount++;
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
    void startAlertDrag(MouseEvent e) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard db = alertBox.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(alertBox.getText());
        db.setContent(content);

        e.consume();
    }

    @FXML
    void handleCreateButtonAction(ActionEvent e) {
        ObservableList<Node> childrens = myFlow.getChildren();
        // Convert the string to a
        // byte array.

        StringBuilder body = new StringBuilder();
        StringBuilder jstext = new StringBuilder();
        jstext.append("(function(){var subBtn=document.getElementById(\"btnSubmit\");subBtn.onclick=function(){");
        for (Node parent : childrens) {
            if (!(parent instanceof CustomLine) && checkForInitNode(parent)) {
                crawlToFlow(body, jstext, parent);
                break;
            }

        }
        ;

        jstext.append("};})()");
        String headerPart = "<html>" + "<head>" + "<title>CodeFire</title>" + "<script  src=\"https://code.jquery.com/jquery-3.2.1.min.js\""
                + "integrity=\"sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=\""
                + "crossorigin=\"anonymous\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">"
                + "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script></head><body>";
        String bodypart = body.toString();
        String endPart = "<script src=\"app.js\"></script></body>" + "</html>";
        String jsCode = jstext.toString();
        byte[] datajs = jsCode.getBytes();
        byte[] datah = headerPart.getBytes();
        byte[] datab = bodypart.getBytes();
        byte[] datae = endPart.getBytes();
        Path htmlPath = Paths.get("D:\\WorkSpace\\CodeFire0.0.5\\htmls\\index.html");
        Path jspath = Paths.get("D:\\WorkSpace\\CodeFire0.0.5\\htmls\\app.js");
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(htmlPath))) {
            out.write(datah, 0, datah.length);
            out.write(datab, 0, datab.length);
            out.write(datae, 0, datae.length);
        } catch (IOException x) {
            System.err.println(x);
        }
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(jspath))) {
            out.write(datajs, 0, datajs.length);
        } catch (IOException x) {
            System.err.println(x);
        }

    }

    private void crawlToFlow(StringBuilder body, StringBuilder jstext, Node parent) {
        DraggableNode dg1 = (DraggableNode) parent;
        createCodeBasedOnNode(body, jstext, dg1);
        Map<CustomLine, Boolean> map = dg1.getConnectedLines();
        Iterator<Entry<CustomLine, Boolean>> it = map.entrySet().iterator();
        if (it != null) {
            while (it.hasNext()) {
                Entry<CustomLine, Boolean> entry = it.next();
                CustomLine l = entry.getKey();

                Parent dg = l.getTarget();
                if (!dg1.getId().equals(dg.getId())) {
                    crawlToFlow(body, jstext, dg);
                }
            }
        }
    }

    private void createCodeBasedOnNode(StringBuilder body, StringBuilder jstext, DraggableNode parent) {
        if (parent.getId().equals("fHtml")) {
            body.append("<form id=\"loginForm\" >");
            body.append("<div class=\"container\">" + "<label><b>Username</b></label>"
                    + " <input type=\"text\" placeholder=\"Enter Username\" id=\"uname\" name=\"username\" >" + " <label><b>Password</b></label>"
                    + "  <input type=\"password\" placeholder=\"Enter Password\" id=\"psw\"  name=\"password\" >" + " </div>"
                    + " <div class=\"container\" style=\"background-color:#f1f1f1\">" + " <button id=\"btnSubmit\" type=\"button\" >Login</button>"
                    + "   <button type=\"button\" class=\"cancelbtn\">Cancel</button>" + " </div>" + "</form>");
        } else if (parent.getId().equals("fJs")) {
            jstext.append("var myCheck=true;");
            Properties prop = returnProps(parent.getId());
            String frst = prop.getProperty("firstBox");
            String scnd = prop.getProperty("secondBox");
            if (frst != null && "both".equals(frst)) {
                jstext.append("var uname=document.getElementById(\"uname\").value;");
                jstext.append("var psw=document.getElementById(\"psw\").value;");
            }
            if (scnd != null && "not empty".equals(scnd)) {
                jstext.append("if(uname !=='' && psw !==''){myCheck=true;}else{myCheck=false}");
            }
        } else if (parent.getId().equals("fAlert")) {
            Properties propAlert = returnProps(parent.getId());
            String message = propAlert.getProperty("message");
            if (message != null) {
                jstext.append("if(!myCheck){alert(\" " + message + "\");}");
            }
        }
    }

    private boolean checkForInitNode(Node parent) {

        DraggableNode dg1 = (DraggableNode) parent;
        boolean isRoot = true;
        Map<CustomLine, Boolean> map = dg1.getConnectedLines();
        Iterator<Entry<CustomLine, Boolean>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<CustomLine, Boolean> curr = it.next();
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
            Image editImg = new Image(getClass().getResourceAsStream("/images/pencil.png"));
            Button editBtn = new Button();
            editBtn.setGraphic(new ImageView(editImg));
            Button btn = new Button("o/p");
            newGrid.add(btn, 3, 3);
            newGrid.add(editBtn, 3, 0);
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

    private void createLine(Point2D n1Center, Point2D n2Center, DraggableNode dg) {

        if (n1Center != null) {
            a = n1Center.getX();
            b = n1Center.getY();
            dg.setSource(true);
            source = dg;
            source.setDgId(dg.getDgId());
        } else if (n2Center != null) {
            c = n2Center.getX();
            d = n2Center.getY();
            dg.setSource(false);
            target = dg;
            target.setDgId(dg.getDgId());
        }
        double eps = 0.001;
        if (!(Math.abs(a - 0) < eps) && !(Math.abs(b - 0) < eps) && !(Math.abs(c - 0) < eps) && !(Math.abs(d - 0) < eps)) {
            boolean alreadyConnected = checkIfSame(source, target);
            if (!alreadyConnected) {
                CustomLine line = new CustomLine(a, b, c, d);
                line.setId(Integer.toString(i));
                line.setSource(source);
                line.setTarget(target);
                i++;
                source.setLine(line);
                target.setLine(line);
                myFlow.getChildren().add(line);
            }
            a = 0;
            b = 0;
            c = 0;
            d = 0;

        }
    }

    private boolean checkIfSame(DraggableNode source, DraggableNode target) {
        boolean sameNodes = false;
        Map<CustomLine, Boolean> slink = source.getConnectedLines();
        Map<CustomLine, Boolean> tlink = target.getConnectedLines();
        Iterator<Entry<CustomLine, Boolean>> it = slink.entrySet().iterator();
        while (it.hasNext()) {
            Line sLine = it.next().getKey();
            if (sLine != null) {
                Iterator<Entry<CustomLine, Boolean>> it1 = tlink.entrySet().iterator();
                while (it1.hasNext()) {
                    Line tLine = it1.next().getKey();
                    if (tLine != null && sLine.getId().equals(tLine.getId())) {
                        sameNodes = true;
                        Alert al = new Alert(AlertType.ERROR, "Arey Mand Gadhwaa agodarach Line aahe na ", ButtonType.CANCEL);
                        al.show();
                        break;
                    }

                }
            }
        }

        return sameNodes;
    }

    private Properties returnProps(String property) {
        Properties props = new Properties();
        FileInputStream fs = null;
        try {
            String path = "./src/properties/" + property + ".properties";
            fs = new FileInputStream(path);
            props.load(fs);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return props;

    }

    @FXML
    void initialize() {
        // you could initialize
        // JavaFX Controls here
    }

}
