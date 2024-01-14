import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Queue;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Optional;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.Color;

public class LimitedGSTest {

    UnorderedPair<Gem, Gem> berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
    Tile bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
    Board testBoard7x7 = BoardTest.generateStaticBoard();

    Position homePos0 = new Position(1, 1);
    Position currenPos0 = new Position(1, 1);
    Queue<LimitedPlayerState> playerOrder = new LinkedList<>(Arrays.asList(new LimitedPlayerState(Color.GREEN, homePos0, currenPos0)));

    Map<Color, Position> colorToPosition = new HashMap<>();
    LimitedGS limitedGS1;

    @BeforeEach
    public void setUp() {
        colorToPosition.put(Color.GREEN, new Position(1, 1));
        limitedGS1 = new LimitedGS(bZTile, testBoard7x7, playerOrder,Optional.empty());

    }

    @Test
    public void getCurrentPositionWorks() {
        assertEquals(new Position(1, 1), limitedGS1.getCurrentPosition());
    }

    @Test
    public void getBoardWorks() {
        Board expected = BoardTest.generateStaticBoard();
        assertEquals(expected, limitedGS1.getBoard());
    }

    @Test
    public void getSpareWorks() {
        Tile expected = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        assertEquals(expected, limitedGS1.getSpare());
    }
}