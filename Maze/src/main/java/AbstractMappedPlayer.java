import com.google.gson.annotations.SerializedName;

import java.awt.Color;

/**
 * Common implementation of JSON mapped players
 */
public abstract class AbstractMappedPlayer {
    MappedCoordinate current;
    MappedCoordinate home;
    String color;

    public AbstractMappedPlayer(MappedCoordinate current, MappedCoordinate home, String color) {
        this.current = current;
        this.home = home;
        this.color = color;
    }

    protected static String getHexString(Color c) {
        return String.format("%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()).toUpperCase();
    }

    /**
     * Return the given color if it is valid
     * @return         valid color
     */
    public Color getColor() throws IllegalArgumentException {
        try {
            return Color.decode("#" + this.color);
        } catch (NumberFormatException nfe) {
            switch(this.color) {
                case "purple":
                    return Color.MAGENTA;
                case "orange":
                    return Color.ORANGE;
                case "pink":
                    return Color.PINK;
                case "red":
                    return Color.RED;
                case "blue":
                    return Color.BLUE;
                case "green":
                    return Color.GREEN;
                case "yellow":
                    return Color.YELLOW;
                case "white":
                    return Color.WHITE;
                case "black":
                    return Color.BLACK;
                default:
                    throw new IllegalArgumentException("Color is invalid " + this.color);
            }
        }
    }
}