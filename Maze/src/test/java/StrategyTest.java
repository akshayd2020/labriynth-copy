import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.jgrapht.alg.util.UnorderedPair;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyTest {
     Board board7x7;
     Board boardVertSlide;
     Board boardAllMinusBar;
     UnorderedPair<Gem, Gem> zirconZoisite;
     UnorderedPair<Gem, Gem> zirconRuby;
     UnorderedPair<Gem, Gem> rubyJasper;
     UnorderedPair<Gem, Gem> jasperZoisite;
     Tile spare;
     PlayInfo playInfo0;

     @BeforeEach
     public void setUp() {
        board7x7 = BoardTest.generateStaticBoard();
        boardAllMinusBar = BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
            BoardTest.get49Bar90Connectors(), 49));
        boardVertSlide = createVertSlideBoard(boardAllMinusBar);
        zirconZoisite = new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE);
        zirconRuby = new UnorderedPair(Gem.ZIRCON, Gem.RUBY);
        rubyJasper = new UnorderedPair(Gem.RUBY, Gem.JASPER);
        jasperZoisite = new UnorderedPair(Gem.JASPER, Gem.ZOISITE);
        spare = new Tile(zirconZoisite, new Connector(Unicode.BAR,
            Orientation.ZERO));
        playInfo0 = new PlayInfo(0, Direction.LEFT, 0, new Position(0, 0));
     }

     /**
      * TODO: STOPPED HERE
      * Return a new board for the test to reach goal by moving left-most column and rotating
      * the spare 90 degrees
      * @return     new board
      */
     private Board createVertSlideBoard(Board b) {
          Tile[][] newGrid = createGridDeepCopy(b);

          newGrid[0][0] = new Tile(b.getTile(new Position(0, 0)).getGems(),
              new Connector(Unicode.PLUS, Orientation.ZERO));
          newGrid[1][0] = new Tile(b.getTile(new Position(1, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[2][0] = new Tile(b.getTile(new Position(2, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[3][0] = new Tile(b.getTile(new Position(3, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[4][0] = new Tile(b.getTile(new Position(4, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[5][0] = new Tile(b.getTile(new Position(5, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[6][0] = new Tile(b.getTile(new Position(6, 0)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[0][3] = new Tile(b.getTile(new Position(0, 3)).getGems(),
              new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
          newGrid[1][3] = new Tile(b.getTile(new Position(1, 3)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[2][3] = new Tile(b.getTile(new Position(2, 3)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[3][3] = new Tile(b.getTile(new Position(3,3)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[4][3] = new Tile(b.getTile(new Position(4,3)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[5][3] = new Tile(b.getTile(new Position(5,3)).getGems(),
              new Connector(Unicode.BAR, Orientation.ZERO));
          newGrid[6][3] = new Tile(b.getTile(new Position(6,3)).getGems(),
              new Connector(Unicode.PLUS, Orientation.ZERO));

          return new Board(newGrid);
     }

     private Tile[][] createGridDeepCopy(Board b) {
          Tile[][] newGrid = new Tile[b.getNumRows()][b.getNumColumns()];
          for (int i = 0; i < b.getNumRows(); i += 1) {
             for (int j = 0; j < b.getNumColumns(); j += 1) {
                 newGrid[i][j] = b.getTile(new Position(i, j));
             }
          }
          return newGrid;
     }

     //region Reimann strategy computePlayInfo()
     @Test
     public void canReachWithoutSlidingRiemann() {
          assertEquals(new PlayInfo(0, Direction.LEFT, 0, new Position(2, 4)),
              new RiemannStrategy().computePlayInfo(new Position(1, 1), new Position(2,4),
                      board7x7, spare, Optional.empty()).get());
    }

     @Test
     public void canReachSlideColRotate90Riemann() {
          assertEquals(new PlayInfo(0, Direction.UP, 90, new Position(6, 3)),
                  new RiemannStrategy().computePlayInfo(new Position(0,0), new Position(6, 3),
                          boardVertSlide, spare, Optional.empty()).get());
     }

     @Test
     public void canReachByMovingCurrColumnRiemann() {
         Tile[][] grid = boardAllMinusBar.getGridDeepCopy();
         grid[2][4] = new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
         grid[3][4] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
         grid[3][5] = new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
         assertEquals(new PlayInfo(4, Direction.DOWN, 0, new Position(3, 5)),
                 new RiemannStrategy().computePlayInfo(new Position(3,4), new Position(3, 5),
                         new Board(grid), spare, Optional.empty()).get());
     }

     @Test
     public void findAlternateGoalRiemann() {
          spare = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.PLUS,
              Orientation.ZERO));
          assertEquals(new PlayInfo(0, Direction.RIGHT, 0, new Position(0, 0)),
              new RiemannStrategy().computePlayInfo(new Position(1,2), new Position(1,3),
                      board7x7, spare, Optional.empty()).get());
     }

     @Test
    public void passPlayTurnComputePlayInfoRiemann() {
         Tile[][] onePlusGrid = createGridDeepCopy(boardAllMinusBar);
         onePlusGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.BAR,
                 Orientation.ZERO));
         onePlusGrid[5][5] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.JASPER), new Connector(Unicode.BAR,
                 Orientation.ZERO));

         assertEquals(Optional.empty(), new RiemannStrategy().computePlayInfo(new Position(5, 5), new Position(1,1),
                 new Board(onePlusGrid), new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                         new Connector(Unicode.BAR, Orientation.NINETY)), Optional.empty()));
     }

     @Test
     public void playTurnCurrentPosIsGoalPos() {
         Tile[][] oneMinusGrid = createGridDeepCopy(boardAllMinusBar);
         oneMinusGrid[0][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.PLUS,
                 Orientation.ZERO));
         oneMinusGrid[1][0] = new Tile(new UnorderedPair(Gem.JASPER, Gem.ZOISITE), new Connector(Unicode.PLUS,
                 Orientation.ZERO));
         assertEquals(new PlayInfo(0, Direction.LEFT, 0, new Position(0, 0)), new RiemannStrategy().computePlayInfo(new Position(1,1), new Position(1,1),
                 new Board(oneMinusGrid), new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                         new Connector(Unicode.BAR, Orientation.NINETY)), Optional.empty()).get());
     }

    @Test
    public void avoidsLastAction() {
        IStrategy riemann = new RiemannStrategy();
        // Check that if no last action exists, the player slides 0, RIGHT.
        Optional<PlayInfo> playInfo = riemann.computePlayInfo(
                new Position(1,2),
                new Position(1,3),
                board7x7,
                spare,
                Optional.empty());
        assertEquals(0, playInfo.get().getIndex());
        assertEquals(Direction.RIGHT, playInfo.get().getDirection());

        // Check that if 0, RIGHT will undo the last action, the player does not make that move.
        SlidingAction lastAction = new SlidingAction(0, Direction.LEFT);
        SlidingAction undoingAction = new SlidingAction(0, Direction.RIGHT);
        playInfo = riemann.computePlayInfo(
                new Position(1,2),
                new Position(1,3),
                board7x7,
                spare,
                Optional.of(lastAction));
        SlidingAction playInfoAction = new SlidingAction(playInfo.get().getIndex(), playInfo.get().getDirection());
        assertNotEquals(undoingAction, playInfoAction);
    }
     // Euclid
     @Test
     public void canReachWithoutSlidingEuclid() {
         assertEquals(new PlayInfo(0, Direction.LEFT, 0, new Position(2, 4)),
                 new EuclidStrategy().computePlayInfo(new Position(1, 1), new Position(2,4),
                         board7x7, spare, Optional.empty()).get());
     }

    @Test
    public void canReachSlideColRotate90Euclid() {
        assertEquals(new PlayInfo(0, Direction.UP, 90, new Position(6, 3)),
                new EuclidStrategy().computePlayInfo(new Position(0,0), new Position(6, 3),
                        boardVertSlide, spare, Optional.empty()).get());
    }

    @Test
    public void canReachByMovingCurrColumnEuclid() {
        Tile[][] grid = boardAllMinusBar.getGridDeepCopy();
        grid[2][4] = new Tile(rubyJasper, new Connector(Unicode.CORNER, Orientation.TWO_SEVENTY));
        grid[3][4] = new Tile(jasperZoisite, new Connector(Unicode.BAR, Orientation.ZERO));
        grid[3][5] = new Tile(zirconRuby, new Connector(Unicode.CORNER, Orientation.ONE_EIGHTY));
        assertEquals(Optional.of(new PlayInfo(4, Direction.DOWN, 0, new Position(3, 5))),
                new EuclidStrategy().computePlayInfo(new Position(3, 4), new Position(3, 5),
                        new Board(grid), spare, Optional.empty()));
    }

    @Test
    public void findAlternateGoalEuclid() {
        spare = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.PLUS,
                Orientation.ZERO));
        assertEquals(new PlayInfo(2, Direction.UP, 0, new Position(0, 3)),
                new EuclidStrategy().computePlayInfo(new Position(1,2), new Position(1,3),
                        board7x7, spare, Optional.empty()).get());
    }

    @Test
    public void passPlayTurnComputePlayInfoEuclid() {
        Tile[][] onePlusGrid = createGridDeepCopy(boardAllMinusBar);
        onePlusGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.BAR,
                Orientation.ZERO));
        onePlusGrid[5][5] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.JASPER), new Connector(Unicode.BAR,
                Orientation.ZERO));
        assertEquals(Optional.empty(), new EuclidStrategy().computePlayInfo(new Position(5,5), new Position(1,1),
                new Board(onePlusGrid), new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)), Optional.empty()));
    }

    @Test
    public void computePlayInfoCurrentIsGoalPos() {
        Tile[][] onePlusGrid = createGridDeepCopy(boardAllMinusBar);
        onePlusGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE), new Connector(Unicode.BAR,
                Orientation.ZERO));
        onePlusGrid[5][5] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.JASPER), new Connector(Unicode.BAR,
                Orientation.ZERO));
        assertEquals(Optional.empty(), new EuclidStrategy().computePlayInfo(new Position(5,5), new Position(1,1),
                new Board(onePlusGrid), new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)), Optional.empty()));
    }

    //endregion

    //region PlayInfo
    @Test
    public void passGetIndex() {
         assertEquals(0, playInfo0.getIndex());
    }

    @Test
    public void passGetDirection() {
        assertEquals(Direction.LEFT, playInfo0.getDirection());
    }

    @Test
    public void passGetDegree() {
        assertEquals(0, playInfo0.getDegreeToRotate());
    }

    @Test
    public void passGetNewPosition() {
        assertEquals(new Position(0, 0), playInfo0.getNewPosition());
    }

    @Test
    public void passPlayInfoEquals() {
         assertEquals(new PlayInfo(0, Direction.LEFT, 0, new Position(0, 0)), playInfo0);
    }

    @Test
    public void passPlayInfoHashCode() {
        assertEquals(new PlayInfo(0, Direction.LEFT, 0, new Position(0, 0)).hashCode(), playInfo0.hashCode());
    }
    //endregion

    //region getter methods
    @Test
    public void getCurrPosWorks() {
         IStrategy euclid = new EuclidStrategy();
         euclid.computePlayInfo(new Position(1, 1),
                new Position(1, 2), board7x7, new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)),
                Optional.of(new SlidingAction(0, Direction.LEFT)));
         assertEquals(new Position(1, 1), euclid.getCurrPos());
    }

    @Test
    public void getGoalPos() {
        IStrategy euclid = new EuclidStrategy();
        euclid.computePlayInfo(new Position(1, 1),
                new Position(1, 2), board7x7, new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)),
                Optional.of(new SlidingAction(0, Direction.LEFT)));
        assertEquals(new Position(1, 2), euclid.getGoalPos());
    }

    @Test
    public void getBoard() {
        IStrategy euclid = new EuclidStrategy();
        euclid.computePlayInfo(new Position(1, 1),
                new Position(1, 2), board7x7, new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)),
                Optional.of(new SlidingAction(0, Direction.LEFT)));
        assertEquals(BoardTest.generateStaticBoard(), euclid.getBoard());
    }

    @Test
    public void getSpare() {
        IStrategy euclid = new EuclidStrategy();
        euclid.computePlayInfo(new Position(1, 1),
                new Position(1, 2), board7x7, new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)),
                Optional.of(new SlidingAction(0, Direction.LEFT)));
        assertEquals(new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE), new Connector(Unicode.BAR, Orientation.NINETY)),
                euclid.getSpare());
    }

    @Test
    public void getLastAction() {
        IStrategy euclid = new EuclidStrategy();
        euclid.computePlayInfo(new Position(1, 1),
                new Position(1, 2), board7x7, new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                        new Connector(Unicode.BAR, Orientation.NINETY)),
                Optional.of(new SlidingAction(0, Direction.LEFT)));
        assertEquals(Optional.of(new SlidingAction(0, Direction.LEFT)),
                euclid.getLastAction());
    }
    //endregion

    //region strategyEquals()
    @Test
    public void riemannEquals() {
         assertEquals(new RiemannStrategy(), new RiemannStrategy());
    }

    @Test
    public void euclidEquals() {
        assertEquals(new EuclidStrategy(), new EuclidStrategy());
    }

    @Test
    public void strategyNotEqual() {
        assertFalse(new RiemannStrategy().equals(new EuclidStrategy()));
    }

    //endregion

}