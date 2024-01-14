import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.util.Pair;

public class Connector {
    private Unicode unicode;
    private Orientation orientation;
    private Map<Pair<Unicode, Orientation>, Set<Direction>> connectorToOpenDir = new HashMap<>();

    public Connector(Unicode unicode, Orientation orientation) {
        this.unicode = unicode;
        this.orientation = orientation;
        setConnectorToOpenDir();
    }

    public static Map<Character, Connector> getCharToConnector() {
        Map<Character, Connector> result = new HashMap<>();
        result.put('\u2502', new Connector(Unicode.BAR, Orientation.ZERO));
        result.put('\u2500', new Connector(Unicode.BAR, Orientation.NINETY));
        result.put('\u2514', new Connector(Unicode.CORNER, Orientation.ZERO));
        result.put('\u2518', new Connector(Unicode.CORNER, Orientation.NINETY));
        result.put('\u2510', new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
        result.put('\u250c', new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
        result.put('\u252c', new Connector(Unicode.T_SHAPE, Orientation.ZERO));
        result.put('\u251c', new Connector(Unicode.T_SHAPE, Orientation.NINETY));
        result.put('\u2534', new Connector(Unicode.T_SHAPE, Orientation.ONE_EIGHTY));
        result.put('\u2524', new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY));
        result.put('\u253c', new Connector(Unicode.PLUS, Orientation.ZERO));
        result.put('\u253c', new Connector(Unicode.PLUS, Orientation.NINETY));
        result.put('\u253c', new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY));
        result.put('\u253c', new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY));
        return result;
    }

    public static Connector convertCharToConnector(Character character) {
        return getCharToConnector().get(character);
    }

    public Unicode getUnicode() {
        return this.unicode;
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    public Connector rotateConnector() {
        return this;
    }

    public static Connector getRandom(Random r) {
        return new Connector(Unicode.getRandom(r), Orientation.getRandom(r));
    }

    public Set<Direction> getOpenDir() {
         return this.connectorToOpenDir.get(new Pair(this.unicode, this.orientation));
    }

    /**
     * Sets a mapping of connectors to a list of the directions which it can connect
     */
    public void setConnectorToOpenDir() {
        setBarOpenDirections();
        setCornerOpenDirections();
        setTShapeOpenDirections();
        setPlusOpenDirections();
    }

    /**
     * Sets the mapping of the Bar connector (at each orientation) to its open directions
     */
    private void setBarOpenDirections() {
        connectorToOpenDir.put(new Pair(Unicode.BAR,  Orientation.ZERO),
            new HashSet<>(Arrays.asList(new Direction[]{ Direction.UP, Direction.DOWN })));
        connectorToOpenDir.put(new Pair(Unicode.BAR,  Orientation.NINETY),
            new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT })));
        connectorToOpenDir.put(new Pair(Unicode.BAR,  Orientation.ONE_EIGHTY),
            new HashSet<>(Arrays.asList(new Direction[]{ Direction.UP, Direction.DOWN })));
        connectorToOpenDir.put(new Pair(Unicode.BAR,  Orientation.TWO_SEVENTY),
            new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT })));
    }

    /**
     * Sets the mapping of the Bar connector (at each orientation) to its open directions
     */
    private void setCornerOpenDirections() {
         connectorToOpenDir.put(new Pair(Unicode.CORNER,  Orientation.ZERO),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.RIGHT, Direction.UP })));
         connectorToOpenDir.put(new Pair(Unicode.CORNER,  Orientation.NINETY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.UP, Direction.LEFT })));
         connectorToOpenDir.put(new Pair(Unicode.CORNER,  Orientation.ONE_EIGHTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.DOWN, Direction.LEFT })));
         connectorToOpenDir.put(new Pair(Unicode.CORNER,  Orientation.TWO_SEVENTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.DOWN, Direction.RIGHT })));
    }

    /**
     * Sets the mapping of the T-shaped connector (at each orientation) to its open directions
     */
    private void setTShapeOpenDirections() {
         connectorToOpenDir.put(new Pair(Unicode.T_SHAPE,  Orientation.ZERO),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.DOWN })));
         connectorToOpenDir.put(new Pair(Unicode.T_SHAPE,  Orientation.NINETY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.RIGHT, Direction.UP, Direction.DOWN })));
         connectorToOpenDir.put(new Pair(Unicode.T_SHAPE,  Orientation.ONE_EIGHTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.UP })));
         connectorToOpenDir.put(new Pair(Unicode.T_SHAPE,  Orientation.TWO_SEVENTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.UP, Direction.DOWN })));
    }

    /**
     * Sets the mapping of the plus connector (at each orientation) to its open directions
     */
    private void setPlusOpenDirections() {
         connectorToOpenDir.put(new Pair(Unicode.PLUS,  Orientation.ZERO),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN })));
         connectorToOpenDir.put(new Pair(Unicode.PLUS,  Orientation.NINETY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN })));
         connectorToOpenDir.put(new Pair(Unicode.PLUS,  Orientation.ONE_EIGHTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN })));
         connectorToOpenDir.put(new Pair(Unicode.PLUS,  Orientation.TWO_SEVENTY),
                new HashSet<>(Arrays.asList(new Direction[]{ Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN })));
    }

    /**
     * Check if the given neighbor is connected to this connector
     * @param   neighbor  neighbor of this connector
     * @param   direction the direction that this connector is open at
     * @return  whether or not the given neighbor is connected to this connector
     */
    public boolean isConnected(Connector neighbor, Direction direction) {
        Direction opposite = Direction.getOpposite(direction);
        return this.connectorToOpenDir.get(new Pair(neighbor.getUnicode(),
            neighbor.getOrientation())).contains(opposite);
    }

    public Connector getDeepCopy() {
        return new Connector(this.unicode, this.orientation);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Connector)) {
            return false;
        }
        Connector connectorToCompare = (Connector) o;
        if (!(connectorToCompare.getUnicode() == this.unicode)
                || !(connectorToCompare.getOrientation() == this.orientation)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.unicode.hashCode() + this.orientation.hashCode();
    }

    @Override
    public String toString() {
        Map<Pair<Unicode, Orientation>, Character> characterMap = getCharacterMap();
        return Character.toString(characterMap.get(new Pair(this.unicode, this.orientation)));
    }

    public static Map<Pair<Unicode, Orientation>, Character> getCharacterMap() {
        Map<Pair<Unicode, Orientation>, Character> characterMap = new HashMap<>();
        characterMap.put(new Pair(Unicode.BAR, Orientation.ZERO), '\u2502');
        characterMap.put(new Pair(Unicode.BAR, Orientation.NINETY), '\u2500');
        characterMap.put(new Pair(Unicode.BAR, Orientation.ONE_EIGHTY), '\u2502');
        characterMap.put(new Pair(Unicode.BAR, Orientation.TWO_SEVENTY), '\u2500');
        characterMap.put(new Pair(Unicode.CORNER, Orientation.ZERO), '\u2514');
        characterMap.put(new Pair(Unicode.CORNER, Orientation.NINETY), '\u2518');
        characterMap.put(new Pair(Unicode.CORNER, Orientation.ONE_EIGHTY), '\u2510');
        characterMap.put(new Pair(Unicode.CORNER, Orientation.TWO_SEVENTY), '\u250C');
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.ZERO), '\u252C');
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.NINETY), '\u251C');
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.ONE_EIGHTY), '\u2534');
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.TWO_SEVENTY), '\u2524');
        characterMap.put(new Pair(Unicode.PLUS, Orientation.ZERO), '\u253C');
        characterMap.put(new Pair(Unicode.PLUS, Orientation.NINETY), '\u253C');
        characterMap.put(new Pair(Unicode.PLUS, Orientation.ONE_EIGHTY), '\u253C');
        characterMap.put(new Pair(Unicode.PLUS, Orientation.TWO_SEVENTY), '\u253C');
        return characterMap;
    }


    public String toStringForTesting() {
        Map<Pair<Unicode, Orientation>, String> characterMap = new HashMap<>();
        characterMap.put(new Pair(Unicode.BAR, Orientation.ZERO), '\u2502'+ "|");
        characterMap.put(new Pair(Unicode.BAR, Orientation.NINETY), '\u2500' + "-");
        characterMap.put(new Pair(Unicode.BAR, Orientation.ONE_EIGHTY), '\u2502' + "|");
        characterMap.put(new Pair(Unicode.BAR, Orientation.TWO_SEVENTY), '\u2500' + "-");
        characterMap.put(new Pair(Unicode.CORNER, Orientation.ZERO), '\u2514' + "L");
        characterMap.put(new Pair(Unicode.CORNER, Orientation.NINETY), '\u2518' + "_|");
        characterMap.put(new Pair(Unicode.CORNER, Orientation.ONE_EIGHTY), '\u2510'+ "~|");
        characterMap.put(new Pair(Unicode.CORNER, Orientation.TWO_SEVENTY), '\u250C'+ "r");
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.ZERO), '\u252C' + "T");
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.NINETY), '\u251C' + "|-");
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.ONE_EIGHTY), '\u2534' + "_|_");
        characterMap.put(new Pair(Unicode.T_SHAPE, Orientation.TWO_SEVENTY), '\u2524'+ "-|");
        characterMap.put(new Pair(Unicode.PLUS, Orientation.ZERO), '\u253C'+ "+");
        characterMap.put(new Pair(Unicode.PLUS, Orientation.NINETY), '\u253C'+ "+");
        characterMap.put(new Pair(Unicode.PLUS, Orientation.ONE_EIGHTY), '\u253C'+ "+");
        characterMap.put(new Pair(Unicode.PLUS, Orientation.TWO_SEVENTY), '\u253C'+ "+");
        return characterMap.get(new Pair(this.unicode, this.orientation));
    }
}