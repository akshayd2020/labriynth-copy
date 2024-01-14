import java.awt.Point;

/**
 * Represents information to render a rectangle
 */
public class RectangleInfo {
    private Point coordinate;
    private int width;
    private int height;

    public Point getCoordinate() {
        return coordinate;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public RectangleInfo(Point coordinate, int width, int height) {
        this.coordinate = coordinate;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "coordinate: " + this.coordinate.toString() + " width: " + Integer.toString(this.width) + " height: "
                + Integer.toString(this.height);
    }
}
