import javax.swing.*;
import java.awt.*;

/**
 * Represents a player avatar on the GUI.
 */
public class AvatarComponent extends JComponent {

    private final Color color;

    public AvatarComponent(Color color) {
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(0, 0, this.getWidth(), this.getHeight());
    }
}
