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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;

public class MainPageCtrl {
    private static final String ERROR = "Error :";
    private static final String FILE_CREATED_SUCCESFULLY = "File Created Succesfully";

    Logger log;
    int countOfElements = 1;
    boolean ifCond = true;
    @FXML
    private Pane myFlow;

    @FXML
    private VBox menuBox;
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

    @FXML
    private Button db;

    private boolean startLine = true;
    double a = 0;
    double b = 0;
    double c = 0;
    double d = 0;
    DraggableNode source = null;
    DraggableNode target = null;
    private static final String FEXTENSION = ".properties";

    static final double SPACING = 30;
    int i = 1;
    int dgNodeCount = 1;

    public boolean isStartLine() {
        return startLine;
    }

    public void setStartLine(boolean startLine) {
        this.startLine = startLine;
    }

    @FXML
    void nodeDragOver(DragEvent de) {
        /* data is dragged over the target */

        Dragboard dragboard = de.getDragboard();
        if (dragboard.hasString()) {
            de.acceptTransferModes(TransferMode.COPY);
        }

        de.consume();

    }

    @FXML
    void nodeDropped(DragEvent de) {
        Dragboard dragString = de.getDragboard();
        if (dragString.hasString()) {
            if ("Html page".equals(dragString.getString())) {
                createHtmlPage();

            } else if ("Check".equals(dragString.getString())) {
                createjsCheck();
            } else if ("Alert".equals(dragString.getString())) {
                createjsAlert();
            } else if ("dbConnection".equals(dragString.getString())) {
                createDbCon();
            }

            de.setDropCompleted(true);
        } else {
            de.setDropCompleted(false);
        }

        de.consume();

    }

    private void createDbCon() {
        GridPane grid = createGridPane("dbConnection");
        Label emptyCheck = new Label("User DB Check");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.PINK);
        redMaterial.setSpecularColor(Color.PINK);
        grid.add(emptyCheck, 1, 1);
        Cylinder cylinder = new Cylinder(20, 40);
        cylinder.setMaterial(redMaterial);
        grid.add(cylinder, 0, 2, 2, 2);
        DraggableNode node = createDragableNode(grid);

        node.setId("fJavadb" + countOfElements);
        node.setType("dbConection");
        File fjs = new File("./src/properties/fJavadb" + countOfElements + "" + FEXTENSION);

        countOfElements++;
        try {
            boolean created = fjs.createNewFile();
            if (created) {
                log.info(FILE_CREATED_SUCCESFULLY);
            }
        } catch (IOException e) {

            log.info(ERROR + e);
        }
        myFlow.getChildren().add(node);
    }

    private void createjsAlert() {
        GridPane grid = createGridPane("AlertBox");
        Label emptyCheck = new Label("Alert Box");
        grid.add(emptyCheck, 1, 1);
        DraggableNode node = createDragableNode(grid);

        node.setId("fAlert" + countOfElements);
        node.setType("jsAlert");
        File fjs = new File("./src/properties/fAlert" + countOfElements + "" + FEXTENSION);

        countOfElements++;
        try {
            fjs.createNewFile();
        } catch (IOException e) {

            log.info(ERROR + e);
        }
        myFlow.getChildren().add(node);
    }

    private void createjsCheck() {
        GridPane grid = createGridPane("Check");
        grid.setPrefHeight(240);
        grid.setPrefWidth(120);

        Label emptyCheck = new Label("Check ");
        emptyCheck.setStyle("-fx-rotate : -45");
        grid.add(emptyCheck, 1, 1);

        DraggableNode node = createDragableNode(grid);

        node.setId("fJs" + countOfElements);
        node.setType("jsCheck");
        File fjs = new File("./src/properties/fJs" + countOfElements + "" + FEXTENSION);

        countOfElements++;
        try {
            fjs.createNewFile();
        } catch (IOException e) {

            log.info(ERROR + e);
        }
        node.setStyle("-fx-rotate : 45;");
        myFlow.getChildren().add(node);
    }

    private void createHtmlPage() {
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
        node.setId("fHtml" + countOfElements);
        node.setType("html");
        countOfElements++;
        myFlow.getChildren().add(node);
    }

    private DraggableNode createDragableNode(GridPane grid) {
        DraggableNode node = new DraggableNode();
        node.setDgId(dgNodeCount);
        node.getStyleClass().add("grid");
        node.getChildren().add(grid);

        dgNodeCount++;
        return node;

    }

    @FXML
    void startDrag(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard btnDragboard = btnDrag.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(btnDrag.getText());
        btnDragboard.setContent(content);

        event.consume();

    }

    @FXML
    void startDragJs(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard jsBtnDrag = btnDragJs.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(btnDragJs.getText());
        jsBtnDrag.setContent(content);

        event.consume();

    }

    @FXML
    void startDragDbcheck(MouseEvent event) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard dbBtnDrag = dbCheck.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(dbCheck.getText());
        dbBtnDrag.setContent(content);

        event.consume();

    }

    @FXML
    void startAlertDrag(MouseEvent e) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard alertBtnDrag = alertBox.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(alertBox.getText());
        alertBtnDrag.setContent(content);

        e.consume();
    }

    @FXML
    void startDBDrag(MouseEvent e) {
        /* drag was detected, start a drag-and-drop gesture */
        /* allow any transfer mode */
        Dragboard dgb = db.startDragAndDrop(TransferMode.ANY);

        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putString(db.getText());
        dgb.setContent(content);

        e.consume();
    }

    @FXML
    void saveFxml() {
        BorderPane root = Context.getInstance().getMainPage();

        Pane pane = (Pane) root.getChildren().get(0);
        ObservableList<Node> childrens = pane.getChildren();
        System.out.println("Yeee Haaw");
        for (Node node : childrens) {
            if (node instanceof DraggableNode && checkForInitNode(node)) {
                crawlAndCreateFxml(node);
                break;
            }
        }

    }

    private void crawlAndCreateFxml(Node node) {
        try {
            // root elements

            DraggableNode root = (DraggableNode) node;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement(root.getId());

            Attr attr = doc.createAttribute("id");
            attr.setValue("html");
            rootElement.setAttributeNode(attr);

            doc.appendChild(rootElement);
            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode("yong"));
            rootElement.appendChild(firstname);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/apps/Project.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");
        } catch (ParserConfigurationException | TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param e
     */
    @FXML
    void handleCreateButtonAction() {
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
        Path htmlPath = Paths.get(".\\plibrary\\index.html");
        Path jspath = Paths.get(".\\plibrary\\app.js");
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(htmlPath))) {
            out.write(datah, 0, datah.length);
            out.write(datab, 0, datab.length);
            out.write(datae, 0, datae.length);
        } catch (IOException x) {
            log.info(ERROR + x);
        }
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(jspath))) {
            out.write(datajs, 0, datajs.length);
        } catch (IOException x) {
            log.info(ERROR + x);
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
        if ("fHtml".equals(parent.getId().substring(0, parent.getId().length() - 1))) {
            appendHtmltext(body);
        } else if ("fJs".equals(parent.getId().substring(0, parent.getId().length() - 1))) {
            appendJsText(jstext, parent);
        } else if ("fAlert".equals(parent.getId().substring(0, parent.getId().length() - 1))) {
            Properties propAlert = returnProps(parent.getId());
            String message = propAlert.getProperty("message");
            if (message != null && ifCond) {
                jstext.append("if(!myCheck){alert(\" " + message + "\");}");
                ifCond = false;
            } else if (message != null) {
                jstext.append("else{alert(\" " + message + "\");}");
                ifCond = true;
            }
        }
    }

    private void appendJsText(StringBuilder jstext, DraggableNode parent) {
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
    }

    private void appendHtmltext(StringBuilder body) {
        body.append("<form id=\"loginForm\" >");
        body.append("<div class=\"container \">" + "<label><b>Username</b></label>"
                + " <input type=\"text\" placeholder=\"Enter Username\" id=\"uname\" name=\"username\" class=\"form-control\">"
                + " <label><b>Password</b></label>"
                + "  <input type=\"password\" placeholder=\"Enter Password\" id=\"psw\"  name=\"password\" class=\"form-control\" >"
                + " <div class=\"col-lg-4\" style=\"padding : 10px 0px;\">"
                + " <button id=\"btnSubmit\" type=\"button\" class=\"btn btn-success\">Login</button>"
                + "   <button type=\"button\" class=\"btn btn-danger cancelbtn\">Cancel</button>" + " </div>" + " </div>" + "</form>");
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
            btn.setOnAction((ActionEvent event) -> {
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
                sameNodes = innerLoop(sameNodes, sLine, it1);
            }
        }

        return sameNodes;
    }

    private boolean innerLoop(boolean sameNodes, Line sLine, Iterator<Entry<CustomLine, Boolean>> it1) {
        boolean nodeCheck = sameNodes;
        while (it1.hasNext()) {
            Line tLine = it1.next().getKey();
            if (tLine != null && sLine.getId().equals(tLine.getId())) {
                nodeCheck = true;
                Alert al = new Alert(AlertType.ERROR, "Arey Mand Gadhwaa agodarach Line aahe na ", ButtonType.CANCEL);
                al.show();
                break;
            }

        }
        return nodeCheck;
    }

    private Properties returnProps(String property) {
        Properties props = new Properties();

        String path = "./src/properties/" + property + ".properties";
        try (FileInputStream fs = new FileInputStream(path)) {
            props.load(fs);
        } catch (IOException e) {
            log.info(ERROR + e);
        }
        return props;

    }

    @FXML
    void initialize() {
        // you could initialize
        // JavaFX Controls here
    }

}
