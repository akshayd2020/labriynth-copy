import javax.swing.*;
import java.awt.*;

/**
 * Represents a player's home on the GUI.
 */
public class HomeComponent extends JComponent {

    private final Color color;

    public HomeComponent(Color color) {
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        Polygon triangle = new Polygon();
        triangle.addPoint(0, this.getHeight());
        triangle.addPoint(this.getWidth(), this.getHeight());
        triangle.addPoint(this.getWidth() / 2, 0);
        g.fillPolygon(triangle);
    }
}
