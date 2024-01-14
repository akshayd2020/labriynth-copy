import javax.swing.*;
import java.awt.*;

/**
 * Represents a player's goal on the GUI.
 */
public class GoalComponent extends JComponent {

    private final Color color;

    public GoalComponent(Color color) {
        this.color = color;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
