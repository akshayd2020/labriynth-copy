import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.jgrapht.alg.util.UnorderedPair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import java.awt.Color;

public class PlayerTest {
     Player player1;
     Position position1;
     Color avatar1;
     IStrategy riemann;
     UnorderedPair<Gem, Gem> berylZircon;
     Tile bZTile;
     Board board7x7;
     Board boardAllMinusBar;
     LimitedGS limitedGs1;
     Queue<LimitedPlayerState> playerOrder = new LinkedList<>();
     Optional<SlidingAction> lastAction;

     IPlayer player2;
     Position position2;
     Color avatar2;
     IStrategy euclid;
     @BeforeEach
     public void setUp() {
          riemann = new RiemannStrategy();
          avatar1 = Color.GREEN;
          playerOrder.add(new LimitedPlayerState(avatar1, position1, position1));
          position1 = new Position(3, 3);
          lastAction = Optional.empty();
          player1 = new Player("Jamie", riemann);
          berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
          bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
          board7x7 = BoardTest.generateStaticBoard();
          boardAllMinusBar = BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
                  BoardTest.get49Bar90Connectors(), 49));
          limitedGs1 = new LimitedGS(bZTile, board7x7, playerOrder, lastAction);

          euclid = new EuclidStrategy();
          avatar2 = Color.MAGENTA;
          position2 = new Position(5, 5);
          player2 = new Player("Jess", euclid);

     }

     // region setUp()
     public void setUpEmptyState() {
         Position goal = new Position(3, 3);
         player1.setup(Optional.empty(), goal);
         assertEquals(goal, player1.getTarget().get());
     }

     //endregion

     //region playTurn()

     @Test
     public void playTurnRiemann() {
          Position goal = new Position(2,4);
          Position currPos = new Position(1, 1);
          playerOrder = new LinkedList<>();
          playerOrder.add(new LimitedPlayerState(avatar1, position1, currPos));
          LimitedGS limitedGs2 = new LimitedGS(bZTile, board7x7, playerOrder, lastAction);
          player1.setup(Optional.of(limitedGs2), goal);
          assertEquals(Optional.of(new PlayInfo(0, Direction.LEFT, 0, new Position(2, 4))), player1.takeTurn(limitedGs2));
     }

     @Test
     public void playTurnEuclid() {
          Position goal = new Position(2,4);
          Position currPos = new Position(1, 1);
          playerOrder = new LinkedList<>();
          playerOrder.add(new LimitedPlayerState(avatar1, position1, currPos));
          LimitedGS limitedGs2 = new LimitedGS(bZTile, board7x7, playerOrder, lastAction);
          player1.setup(Optional.of(limitedGs2), goal);

          assertEquals(Optional.of(new PlayInfo(0, Direction.LEFT, 0, new Position(2, 4))), player1.takeTurn(limitedGs2));
     }

     @Test
     public void passTakeTurnWithGoalPosition() {
          Tile[][] mustPassGrid = boardAllMinusBar.getGridDeepCopy();
          mustPassGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.BAR,
                  Orientation.ZERO));
          mustPassGrid[5][5] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.JASPER), new Connector(Unicode.BAR,
                  Orientation.ZERO));
          playerOrder = new LinkedList<>();
          playerOrder.add(new LimitedPlayerState(avatar1, position1, new Position(5, 5)));
          limitedGs1 = new LimitedGS(bZTile, new Board(mustPassGrid), playerOrder, lastAction);
          player1.setup(Optional.of(limitedGs1), new Position(1, 1));
          assertEquals(Optional.empty(), player1.takeTurn(limitedGs1));
     }

     //endregion

     @Test
     public void playerNotEqual() {
          assertFalse(player1.equals(player2));
     }

     @Test
     public void playerEquals() {
          assertTrue(player1.equals(new Player("Jamie", riemann)));
     }

     @Test
     public void player2Equals() {
          IPlayer expected = new Player("Jamie",  new RiemannStrategy());
          assertEquals(expected, new Player("Jamie", new RiemannStrategy()));
     }

     @Test
     public void HashSetEquals() {
          IPlayer expected = new Player("Jamie", new RiemannStrategy());
          Set<IPlayer> expectedSet = new HashSet<>(Arrays.asList(new IPlayer[]{expected}));
          assertEquals(expectedSet, new HashSet<>(Arrays.asList(new IPlayer[]{expected})));
     }

     @Test
     public void hashCodesAreEqual() {
          assertEquals(player1.hashCode(), new Player("Jamie", riemann).hashCode());
     }

     @Test
     public void player2HashCode() {
          IPlayer expected = new Player("Jamie", new RiemannStrategy());
          assertEquals(expected.hashCode(), new Player("Jamie", new RiemannStrategy()).hashCode());
     }

     @Test
     public void hashCodesAreNotEqual() {
          assertFalse(player1.hashCode() == player2.hashCode());
     }

     //region proposeBoard()
     @Test
     public void proposeBoardCorrectDimensions() {
          assertEquals(7, player1.proposeBoard(7,9).getNumRows());
          assertEquals(9, player1.proposeBoard(7,9).getNumColumns());
     }

     @Test
     public void proposeBoardValidGems() {
          assertEquals(7*9, player1.proposeBoard(7,9).getAllGemPairs().size());
     }
     //end region
}

