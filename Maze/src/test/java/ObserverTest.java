import javafx.util.Pair;
import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {
    UnorderedPair<Gem, Gem> berylZircon;
    Tile bZTile;
    Board testBoard7x7;
    Color avatar0;
    Color avatar1;
    Color avatar2;
    Position homePos0;
    Position goalPos0;
    Position currenPos0;
    Position homePos1;
    Position goalPos1;
    Position currenPos1;
    Position homePos2;
    Position goalPos2;
    Position currenPos2;
    IPlayer testPlayer0;
    IPlayer testPlayer1;
    IPlayer testPlayer2;
    PlayerState playerStateZero;
    PlayerState playerStateOne;
    PlayerState playerStateTwo;
    Queue<PlayerState> playerOrder;
    Map<Color, Position> homePositions;
    Map<Color, Position> currentPositions;
    Optional<Pair<Integer, Direction>> lastAction;
    GameState gameStateOne;
    IObserver observer;

    @BeforeEach
    public void setup() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        testBoard7x7 = BoardTest.generateStaticBoard();
        avatar0 = Color.GREEN;
        avatar1 = Color.MAGENTA;
        avatar2 = Color.BLACK;
        homePos0 = new Position(1, 1);
        goalPos0 = new Position(5, 5);
        currenPos0 = new Position(1, 1);

        homePos1 = new Position(1, 3);
        goalPos1 = new Position(5, 3);
        currenPos1 = new Position(1, 3);

        homePos2 = new Position(1, 5);
        goalPos2 = new Position(5, 1);
        currenPos2 = new Position(1, 5);
        testPlayer0 = new Player("Test Player 0", new RiemannStrategy());
        testPlayer1 = new Player("Test Player 1", new RiemannStrategy());
        testPlayer2 = new Player("Test Player 2", new RiemannStrategy());
        playerStateZero = new PlayerState(avatar0, homePos0, goalPos0, currenPos0, 0, testPlayer0);
        playerStateOne = new PlayerState(avatar1, homePos1, goalPos1, currenPos1, 0, testPlayer1);
        playerStateTwo = new PlayerState(avatar2, homePos2, goalPos2, currenPos2, 0, testPlayer2);
        playerOrder = new LinkedList<>();
        playerOrder.add(playerStateZero);
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);
        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);

    }

    @Test
    public void getGameStateWorks() {
        assertEquals(new GameState(bZTile, testBoard7x7, playerOrder),
                new Observer(gameStateOne).getCurrentGameState());
    }

    @Test
    public void informNewStateWorks() {
        GameState newGS = new GameState(new Tile(berylZircon, new Connector(Unicode.BAR, Orientation.ZERO)), testBoard7x7, playerOrder);
        observer = new Observer(gameStateOne);
        assertEquals(new GameState(bZTile, testBoard7x7, playerOrder),
                new Observer(gameStateOne).getCurrentGameState());
        observer.informNewState(newGS);
        observer.safeIncrementNextState();
        assertEquals(new GameState(new Tile(berylZircon, new Connector(Unicode.BAR, Orientation.ZERO)), testBoard7x7, playerOrder),
                observer.getCurrentGameState());
    }

    //region saveCurrentState

    @Test
    public void saveCurrentStateWorksMock() throws IOException {
        Appendable appendable = new StringBuilder();
        IObserver mockObserver = new MockObserver(gameStateOne, appendable);
        mockObserver.saveCurrentState("hi.txt");

        assertEquals("handleSaveCurrentStatehi.txt", appendable.toString());

    }

    //endregion
    @Test
    public void isGameOverWorks() {
        assertFalse(new Observer(gameStateOne).isGameOver());
    }

    @Test
    public void gameOverWorks() {
        observer = new Observer(gameStateOne);
        assertFalse(observer.isGameOver());
        observer.gameOver();
        assertTrue(observer.isGameOver());
    }
}
