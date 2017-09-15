/*
 * Creation : 29-Aug-2017
 */
package apps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class DraggableNode extends Pane {
    private int id;
    // node position
    private double x = 0;
    private double y = 0;
    // mouse position
    private double mousex = 0;
    private double mousey = 0;
    private Node view;
    private boolean dragging = false;
    private boolean moveToFront = true;
    private boolean isSource;
    private Map<CustomLine, Boolean> connectedLines = new HashMap<>();
    private String type;

    public DraggableNode() {
        init();
    }

    public DraggableNode(Node view) {
        this.view = view;

        getChildren().add(view);
        init();
    }

    public Map<CustomLine, Boolean> getConnectedLines() {
        return connectedLines;
    }

    public void setConnectedLines(Map<CustomLine, Boolean> connectedLines) {
        this.connectedLines = connectedLines;
    }

    private void init() {

        onMousePressedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // record the current mouse X and Y position on Node
                mousex = event.getSceneX();
                mousey = event.getSceneY();

                x = getLayoutX();
                y = getLayoutY();

                if (isMoveToFront()) {
                    toFront();
                }
            }
        });

        // Event Listener for MouseDraggedLogin page
        onMouseDraggedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Get the exact moved X and Y
                double offsetX = event.getSceneX() - mousex;
                double offsetY = event.getSceneY() - mousey;
                Node trgt = (Node) event.getTarget();
                double tHeight = trgt.getLayoutBounds().getHeight();
                double tWidth = trgt.getLayoutBounds().getWidth();
                x += offsetX;
                y += offsetY;

                double scaledX = x;
                double scaledY = y;
                if (scaledX < 20 || scaledX > 400 || scaledY < 40 || scaledY > 700) {
                    return;
                }
                setLayoutX(scaledX);
                setLayoutY(scaledY);
                updateLine(scaledX, scaledY, tHeight, tWidth);
                dragging = true;

                // again set current Mouse x AND y position
                mousex = event.getSceneX();
                mousey = event.getSceneY();

                event.consume();
            }
        });

        onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BorderPane bp = (BorderPane) getParent().getParent();
                VBox rightPanel = (VBox) bp.getRight();

                rightPanel.getChildren().clear();
                Label l = new Label();
                if ("fJs".equals(getId().substring(0, getId().length() - 1))) {

                    ObservableList<String> options = FXCollections.observableArrayList("username", "pasword", "both");
                    ObservableList<String> options1 = FXCollections.observableArrayList("not empty", "empty");
                    final ComboBox<String> comboBox = new ComboBox<>(options);
                    final ComboBox<String> comparator = new ComboBox<>(options1);
                    Properties props = new Properties();
                    FileInputStream fs = null;
                    String frts = null;
                    String scnd = null;
                    try {
                        fs = new FileInputStream("./src/properties/" + getId() + ".properties");
                        props.load(fs);
                        if (props.getProperty("firstBox") != null) {
                            frts = props.getProperty("firstBox");
                            scnd = props.getProperty("secondBox");
                        } else {
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    l.setText(" " + getId() + "\n check if \n");
                    Label l1 = new Label("is/are");
                    Button btnSave = new Button("Save Properties");
                    btnSave.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            props.setProperty("firstBox", comboBox.getSelectionModel().getSelectedItem());
                            props.setProperty("secondBox", comparator.getSelectionModel().getSelectedItem());
                            try {
                                props.store(new FileOutputStream("./src/properties/" + getId() + ".properties"), "Values for parameters");
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    });
                    if (frts != null) {

                        comboBox.getSelectionModel().select(frts);
                    }
                    if (scnd != null) {

                        comparator.getSelectionModel().select(scnd);
                    }
                    rightPanel.getChildren().addAll(l, comboBox, l1, comparator, btnSave);

                } else if ("fAlert".equals(getId().substring(0, getId().length() - 1))) {
                    l.setText("Alert Box \n Your Message : ");
                    final TextField text = new TextField();
                    Properties props = new Properties();
                    FileInputStream fs = null;
                    String message = null;
                    try {
                        fs = new FileInputStream("./src/properties/" + getId() + ".properties");
                        props.load(fs);
                        if (props.getProperty("message") != null) {
                            message = props.getProperty("message");
                            text.setText(message);
                        } else {
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    Button btnSave = new Button("Save Properties");
                    btnSave.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            props.setProperty("message", text.getText());
                            try {
                                props.store(new FileOutputStream("./src/properties/" + getId() + ".properties"), "Values for parameters");
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    });

                    rightPanel.getChildren().addAll(l, text, btnSave);
                } else if ("fJavadb".equals(getId().substring(0, getId().length() - 1))) {
                    l.setText("Host Url : ");
                    final TextField hostUrl = new TextField();
                    final TextField username = new TextField();
                    final TextField password = new TextField();
                    Label user = new Label("User Name : ");
                    Label pwd = new Label("Password : ");
                    Properties props = new Properties();
                    FileInputStream fs = null;
                    String host = null;
                    String uname = null;
                    String pswd = null;
                    try {
                        fs = new FileInputStream("./src/properties/" + getId() + ".properties");
                        props.load(fs);
                        if (props.getProperty("host") != null) {
                            host = props.getProperty("host");
                            uname = props.getProperty("user");
                            pswd = props.getProperty("password");
                            hostUrl.setText(host);
                            username.setText(uname);
                            password.setText(pswd);
                        } else {
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    Button btnSave = new Button("Save Properties");
                    btnSave.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            props.setProperty("host", hostUrl.getText());
                            props.setProperty("user", username.getText());
                            props.setProperty("password", password.getText());
                            try {
                                props.store(new FileOutputStream("./src/properties/" + getId() + ".properties"), "Values for parameters");
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    });
                    rightPanel.getChildren().addAll(l, hostUrl, user, username, pwd, password, btnSave);
                }

                dragging = false;
            }
        });

    }

    /**
     * @return the dragging
     */
    protected boolean isDragging() {
        return dragging;
    }

    private void updateLine(double x, double y, double height, double width) {
        Iterator<Entry<CustomLine, Boolean>> it = connectedLines.entrySet().iterator();
        while (it.hasNext()) {
            Entry<CustomLine, Boolean> curr = it.next();
            if (curr.getValue()) {
                changeStart(x, y, height, width, curr.getKey());
            } else {
                changeEnd(x, y, height, width, curr.getKey());
            }

        }
    }

    /**
     * @return the view
     */
    public Node getView() {
        return view;
    }

    /**
     * @param moveToFront the moveToFront to set
     */
    public void setMoveToFront(boolean moveToFront) {
        this.moveToFront = moveToFront;
    }

    /**
     * @return the moveToFront
     */
    public boolean isMoveToFront() {
        return moveToFront;
    }

    public void removeNode(Node n) {
        getChildren().remove(n);
    }

    public void setLine(CustomLine line) {
        this.connectedLines.put(line, this.isSource());
    }

    public boolean isSource() {
        return isSource;
    }

    public void setSource(boolean isSource) {
        this.isSource = isSource;
    }

    public void changeStart(double x, double y, double height, double width, Line l) {
        l.setStartX(x + (width / 2));
        l.setStartY(y + height);
    }

    public void changeEnd(double x, double y, double height, double width, Line l) {
        l.setEndX(x + (width / 2));
        l.setEndY(y);

    }

    public int getDgId() {
        return id;
    }

    public void setDgId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}