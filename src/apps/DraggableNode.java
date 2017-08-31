/*
 * Creation : 29-Aug-2017
 */
package apps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class DraggableNode extends Pane {

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

    private Map<Line, Boolean> connectedLines = new HashMap<>();

    public DraggableNode() {
        init();
    }

    public DraggableNode(Node view) {
        this.view = view;

        getChildren().add(view);
        init();
    }

    public Map<Line, Boolean> getConnectedLines() {
        return connectedLines;
    }

    public void setConnectedLines(Map<Line, Boolean> connectedLines) {
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

        // Event Listener for MouseDragged
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
        Iterator<Entry<Line, Boolean>> it = connectedLines.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Line, Boolean> curr = it.next();
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

    public void setLine(Line line) {
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
}