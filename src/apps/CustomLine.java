/*
 * Creation : 04-Sep-2017
 */
package apps;

import javafx.scene.shape.Line;

public class CustomLine extends Line {
    private DraggableNode source;
    private DraggableNode target;

    public CustomLine(double a, double b, double c, double d) {
        super(a, b, c, d);
    }

    public DraggableNode getSource() {
        return source;
    }

    public void setSource(DraggableNode source) {
        this.source = source;
    }

    public DraggableNode getTarget() {
        return target;
    }

    public void setTarget(DraggableNode target) {
        this.target = target;
    }

}
