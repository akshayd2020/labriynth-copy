import org.jgrapht.alg.util.UnorderedPair;

import java.util.*;

import java.awt.Color;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameStateTest {

    UnorderedPair<Gem, Gem> berylZircon;
    UnorderedPair<Gem, Gem> rubyZoisite;
    Tile bZTile;
    Tile[][] testGrid7x7Expected;
    Tile[][] testGrid7x7;
    Board testBoard7x7Expected;
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
    LimitedPlayerState limitedPSZero;
    LimitedPlayerState limitedPSOne;
    LimitedPlayerState limitedPSTwo;
    GameState gameStateOne;
    Queue<PlayerState> playerOrder;
    Queue<LimitedPlayerState> limitedPlayerOrder = new LinkedList<>();
    Tile spare;

    IPlayer player1;
    Position position1;
    IStrategy riemann;
    LimitedGS limitedGs1;
    Optional<SlidingAction> lastAction;

    @BeforeEach
    public void setUp() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        rubyZoisite = new UnorderedPair(Gem.RUBY, Gem.ZOISITE);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        spare = bZTile;
        testBoard7x7Expected = BoardTest.generateStaticBoard();
        testGrid7x7Expected = testBoard7x7Expected.getGrid();
        testBoard7x7 = BoardTest.generateStaticBoard();
        testGrid7x7 = testBoard7x7.getGrid();
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

        playerStateZero = new PlayerState(
            avatar0,
            homePos0, goalPos0, currenPos0, 0, testPlayer0);
        playerStateOne = new PlayerState(
                avatar1,
                homePos1, goalPos1, currenPos1, 0, testPlayer1);
        playerStateTwo = new PlayerState(
                avatar2,
                homePos2, goalPos2, currenPos2, 0, testPlayer2);
        playerOrder = new LinkedList<>();
        playerOrder.add(playerStateZero);
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);
        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);

        limitedPSZero = new LimitedPlayerState(avatar0, homePos0, currenPos0);
        limitedPSOne = new LimitedPlayerState(avatar1, homePos1, currenPos1);
        limitedPSTwo = new LimitedPlayerState(avatar2, homePos2, currenPos2);

        riemann = new RiemannStrategy();
        position1 = new Position(3, 3);
        limitedPlayerOrder.add(limitedPSZero);
        limitedPlayerOrder.add(limitedPSOne);
        limitedPlayerOrder.add(limitedPSTwo);
        lastAction = Optional.empty();
        limitedGs1 = new LimitedGS(bZTile, testBoard7x7, limitedPlayerOrder, lastAction);
    }

    //region GameState constructor tests
    @Test
    public void gameStateConstructorFail() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new GameState(new Tile(new UnorderedPair(Gem.PINK_SC,
                Gem.PURPLE_ST), new Connector(Unicode.BAR, Orientation.ZERO)), testBoard7x7,
                playerOrder));

        assertEquals("Board contains the spare's pair of gems", exception.getMessage(),
            "GameState constructor did not throw error");
    }

    @Test
    public void gameStateConstructorNonDistinctHomePositions() {
        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.MAGENTA, new Position(1, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GameState(spare, testBoard7x7, playerOrder));

        assertEquals("One of the player state's fields that should be distinct is not distinct",
                exception.getMessage());
    }

    @Test
    public void gameStateConstructorNonDistinctAvatars() {
        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GameState(spare, testBoard7x7, playerOrder));

        assertEquals("One of the player state's fields that should be distinct is not distinct",
                exception.getMessage());
    }

    @Test
    public void gameStateConstructorMoreHomesThanFixedTiles() {
        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.RED, new Position(0, 0),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.BLUE, new Position(1, 3),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.MAGENTA, new Position(1, 5),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.WHITE, new Position(3, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.ORANGE, new Position(3, 3),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.BLACK, new Position(3, 5),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.PINK, new Position(5, 1),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.YELLOW, new Position(5, 3),
                new Position(1, 3), new Position(1, 1), 0, player1));
        playerOrder.add(new PlayerState(Color.CYAN, new Position(5, 5),
                new Position(1, 3), new Position(1, 1), 0, player1));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new GameState(spare, testBoard7x7, playerOrder));

        assertEquals("The number of players cannot exceed the number of fixed tiles on the board.",
                exception.getMessage());
    }

    //endregion

    //region goalReached()
    @Test
    public void goalReachedFalse() {
        assertFalse(gameStateOne.isActivePlayerOnGoalTile(), "The goalReached returned true, should be false");
    }

    @Test
    public void goalReachedTrue() {
        playerStateZero.setCurrentPosition(goalPos0);
        assertTrue(gameStateOne.isActivePlayerOnGoalTile(), "The goalReached returned false, should be true");
    }
    //endregion

    //region getPlayerOrder()
    @Test
    public void passGetPlayerOrder() {
        playerOrder = new LinkedList<>();
        playerOrder.add(playerStateZero);
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);
        assertEquals(playerOrder, gameStateOne.getPlayerOrder());
    }
    //endregion

    //region getActivePlayer()
    @Test
    public void passGetActivePlayer() {
        assertEquals(playerStateZero, gameStateOne.getActivePlayer());
    }
    //endregion

    //region movePlayersOnShift()
    @Test
    public void passMovePlayersOnShiftNoPlayersMove() {
        PlayerState playerStateZeroTest = new PlayerState(
                Color.GREEN,
                new Position(1, 1),
                new Position(5, 5), new Position(1, 1), 0, testPlayer0);
        PlayerState playerStateOneTest = new PlayerState(
                Color.MAGENTA,
                new Position(1, 3), new Position(5, 3),
                new Position(1, 3), 0, testPlayer1);
        PlayerState playerStateTwoTest = new PlayerState(
                Color.BLACK,
                new Position(1, 5), new Position(5, 1),
                new Position(1, 5), 0, testPlayer2);
        Queue<PlayerState> expectedPlayerOrder = new LinkedList<>();
        expectedPlayerOrder.add(playerStateZeroTest);
        expectedPlayerOrder.add(playerStateOneTest);
        expectedPlayerOrder.add(playerStateTwoTest);
        assertEquals(expectedPlayerOrder, this.gameStateOne.getPlayerOrder());

        assertEquals(expectedPlayerOrder, this.gameStateOne.movePlayersOnShift(0, Direction.LEFT));
    }

    @Test
    public void passMovePlayersOnShiftSomePlayersMove() {
        PlayerState playerStateZeroTest = new PlayerState(
                Color.GREEN,
                new Position(1, 1), new Position(5, 5),
                new Position(0, 1), 0, testPlayer0);
        PlayerState playerStateOneTest = new PlayerState(
                Color.MAGENTA,
                new Position(1, 3), new Position(5, 3),
                new Position(0, 3), 0, testPlayer1);
        PlayerState playerStateTwoTest = new PlayerState(
                Color.BLACK,
                new Position(1, 5), new Position(5, 1),
                new Position(1, 5), 0, testPlayer2);
        Queue<PlayerState> expectedPlayerOrder = new LinkedList<>();
        expectedPlayerOrder.add(playerStateZeroTest);
        expectedPlayerOrder.add(playerStateOneTest);
        expectedPlayerOrder.add(playerStateTwoTest);

        playerStateZero = new PlayerState(
                avatar0,
                homePos0, goalPos0,
                new Position(0, 1), 0, testPlayer0);
        playerStateOne = new PlayerState(
                avatar1,
                homePos1, goalPos1, new Position(0, 3), 0, testPlayer1);
        playerOrder = new LinkedList<>();
        playerOrder.add(playerStateZero);
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);
        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);
        assertEquals(expectedPlayerOrder, gameStateOne.getPlayerOrder());

        Queue<PlayerState> expectedAfterPlayersOnShift = new LinkedList<>();
        expectedAfterPlayersOnShift.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(5, 5), new Position(0, 0), 0, testPlayer0));
        expectedAfterPlayersOnShift.add(new PlayerState(Color.MAGENTA, new Position(1, 3),
                new Position(5, 3), new Position(0, 2), 0, testPlayer1));
        expectedAfterPlayersOnShift.add(playerStateTwoTest);
        assertEquals(expectedAfterPlayersOnShift, gameStateOne.movePlayersOnShift(0, Direction.LEFT));
    }
    //endregion

    //region getSpare()
    @Test
    public void passGetSpare() {
        assertEquals(bZTile, gameStateOne.getSpare(), "The getSpare returned incorrect tile");
    }

    //endregion

    //region performAction

    //region slideAndInsert()
    @Test
    public void invalidSlideAndInsertRow() {
        int row = 1;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> gameStateOne.performAction(new SlideAndInsert(row, Direction.LEFT,
                gameStateOne)));

        assertEquals("Attempted to slide invalid row or column", exception.getMessage(),
            "Failed to throw an error if slideAndInsert got an invalid row");
    }

    @Test
    public void invalidSlideAndInsertColumn() {
        int col = 3;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameStateOne.performAction(new SlideAndInsert(col, Direction.UP, gameStateOne)));

        assertEquals("Attempted to slide invalid row or column", exception.getMessage(),
                "Failed to throw an error if slideAndInsert got an invalid column");
    }

    @Test
    public void passSlideAndInsert() {
        int row = 2;
        Tile expectedSpare = testGrid7x7Expected[row][0];

        testGrid7x7Expected[row][0] = testGrid7x7Expected[row][1];
        testGrid7x7Expected[row][1] = testGrid7x7Expected[row][2];
        testGrid7x7Expected[row][2] = testGrid7x7Expected[row][3];
        testGrid7x7Expected[row][3] = testGrid7x7Expected[row][4];
        testGrid7x7Expected[row][4] = testGrid7x7Expected[row][5];
        testGrid7x7Expected[row][5] = testGrid7x7Expected[row][6];
        testGrid7x7Expected[row][6] = bZTile;

        GameState expected =
            new GameState(expectedSpare, new Board(testGrid7x7Expected),
                playerOrder, Optional.of(new SlidingAction(row, Direction.LEFT)));

        GameState actualGs = gameStateOne.performAction(new SlideAndInsert(row,
            Direction.LEFT,
            gameStateOne));
        assertEquals(expected, actualGs, "Failed to slide and insert our spare");
    }
    @Test
    public void passSlideAndInsertPlayerFallOff() {
        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(avatar0, homePos0, goalPos0,
                new Position(0, 0), 0, testPlayer0));
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);

        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);
        assertEquals(new PlayerState(avatar0, homePos0, goalPos0, new Position(0, 0),
                        0, testPlayer0),
            gameStateOne.getActivePlayer());

        PlayerState expected = new PlayerState(avatar0, homePos0, goalPos0, new Position(0, 6),
                0, testPlayer0);

        GameState actualGs = gameStateOne.performAction(new SlideAndInsert(0, Direction.LEFT, gameStateOne));
        assertEquals(expected, actualGs.getActivePlayer());
    }

    @Test
    public void passSlideAndInsertUpMiddlePlayerMoved() {
        int col = 0;
        Position expectedBefore = new Position(3, col);

        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(avatar0, homePos0, goalPos0, new Position(3, col),
                0, testPlayer0));
        playerOrder.add(playerStateOne);
        playerOrder.add(playerStateTwo);

        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);

        assertEquals(new PlayerState(avatar0, homePos0, goalPos0,
                new Position(3, col), 0, testPlayer0), gameStateOne.getActivePlayer());
        assertEquals(expectedBefore, gameStateOne.getActivePlayer().getCurrentPosition());


        testGrid7x7Expected[0][col] = testGrid7x7Expected[1][col];
        testGrid7x7Expected[1][col] = testGrid7x7Expected[2][col];
        testGrid7x7Expected[2][col] = testGrid7x7Expected[3][col];
        testGrid7x7Expected[3][col] = testGrid7x7Expected[4][col];
        testGrid7x7Expected[4][col] = testGrid7x7Expected[5][col];
        testGrid7x7Expected[5][col] = testGrid7x7Expected[6][col];
        testGrid7x7Expected[6][col] = bZTile;


        GameState actualGs = gameStateOne.performAction(new SlideAndInsert(col,
            Direction.UP, gameStateOne));
        assertEquals(new Position(2, col),
            actualGs.getActivePlayer().getCurrentPosition());
    }
    //endregion

    //region

    //region GameState equals()
    @Test
    public void passGameStateEquals() {
        Queue<PlayerState> expectedPlayerOrder = new LinkedList<>();
        PlayerState playerStateZeroTest = new PlayerState(
                Color.GREEN,
                new Position(1, 1), new Position(5, 5),
                new Position(1, 1), 0, testPlayer0);
        PlayerState playerStateOneTest = new PlayerState(
                Color.MAGENTA,
                new Position(1, 3), new Position(5, 3),
                new Position(1, 3), 0, testPlayer1);
        PlayerState playerStateTwoTest = new PlayerState(
                Color.BLACK,
                new Position(1, 5), new Position(5, 1),
                new Position(1, 5), 0, testPlayer2);
        expectedPlayerOrder.add(playerStateZeroTest);
        expectedPlayerOrder.add(playerStateOneTest);
        expectedPlayerOrder.add(playerStateTwoTest);

        assertEquals(new GameState(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO)),
                testBoard7x7Expected, expectedPlayerOrder), this.gameStateOne);
    }
    //endregion

    //region getPlayerColorToState()

    @Test
    public void passGetPlayerColorToState() {
        PlayerState playerStateZeroTest = new PlayerState(
                Color.GREEN,
                new Position(1, 1), new Position(5, 5),
                new Position(1, 1), 0, testPlayer0);
        PlayerState playerStateOneTest = new PlayerState(
                Color.MAGENTA,
                new Position(1, 3), new Position(5, 3),
                new Position(1, 3), 0, testPlayer1);
        PlayerState playerStateTwoTest = new PlayerState(
                Color.BLACK,
                new Position(1, 5), new Position(5, 1),
                new Position(1, 5), 0, testPlayer2);
        Queue<PlayerState> playerColorToStateTest = new LinkedList<>();
        playerColorToStateTest.add(playerStateZeroTest);
        playerColorToStateTest.add(playerStateOneTest);
        playerColorToStateTest.add(playerStateTwoTest);
        assertEquals(playerColorToStateTest, gameStateOne.getPlayerOrder());
    }
    //endregion

    //region RotateTile
    @Test
    public void passRotateTileBar0() {
        Tile actualBefore = gameStateOne.getSpare();
        Tile expectedSpare = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        assertEquals(expectedSpare, actualBefore);

        GameState expectedGs = new GameState(expectedSpare, testBoard7x7Expected, playerOrder);
        GameState actualGs = gameStateOne.performAction(new RotateTile(0, gameStateOne));
        assertEquals(expectedGs, actualGs);
    }

    @Test
    public void passRotateTileCorner90() {
        Tile corner = new Tile(rubyZoisite, new Connector(Unicode.CORNER, Orientation.ZERO));
        GameState gs =
                new GameState(corner, testBoard7x7, playerOrder);
        Tile actualBefore = gs.getSpare();
        Tile expectedBefore = new Tile(rubyZoisite, new Connector(Unicode.CORNER, Orientation.ZERO));
        assertEquals(expectedBefore, actualBefore);

        Tile expectedSpare = new Tile(rubyZoisite, new Connector(Unicode.CORNER, Orientation.NINETY));
        GameState expectedGs = new GameState(expectedSpare, testBoard7x7Expected, playerOrder);
        GameState actualGs = gs.performAction(new RotateTile(90, gs));
        assertEquals(expectedGs, actualGs);
    }

    @Test
    public void passRotateTileTShape180() {
        Tile tshape = new Tile(rubyZoisite, new Connector(Unicode.T_SHAPE, Orientation.NINETY));
        GameState gs =
                new GameState(tshape, testBoard7x7, playerOrder);
        Tile actualBefore = gs.getSpare();
        Tile expectedBefore = new Tile(rubyZoisite, new Connector(Unicode.T_SHAPE, Orientation.NINETY));
        assertEquals(expectedBefore, actualBefore);

        Tile expectedSpare = new Tile(rubyZoisite, new Connector(Unicode.T_SHAPE, Orientation.TWO_SEVENTY));
        GameState expectedGs = new GameState(expectedSpare, testBoard7x7Expected, playerOrder);
        GameState actualgs = gs.performAction(new RotateTile(180, gs));
        assertEquals(expectedGs, actualgs);
    }

    @Test
    public void passRotateTilePlus270() {
        Tile plus = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY));
        GameState gs =
                new GameState(plus, testBoard7x7, playerOrder);
        Tile actualBefore = gs.getSpare();
        Tile expectedBefore = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ONE_EIGHTY));
        assertEquals(expectedBefore, actualBefore);

        Tile expectedSpare = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.NINETY));
        GameState expectedGs = new GameState(expectedSpare, testBoard7x7Expected, playerOrder);
        GameState actualGs = gs.performAction(new RotateTile(270, gs));
        assertEquals(expectedGs, actualGs);
    }

    @Test
    public void passRotateTile450() {
        Tile plus = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ZERO));
        GameState gs =
                new GameState(plus, testBoard7x7, playerOrder);
        Tile actualBefore = gs.getSpare();
        Tile expectedBefore = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ZERO));
        assertEquals(expectedBefore, actualBefore);

        Tile expectedSpare = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.NINETY));
        GameState expectedGs = new GameState(expectedSpare, testBoard7x7Expected, playerOrder);
        GameState actualGs = gs.performAction(new RotateTile(450, gs));
        assertEquals(expectedGs, actualGs);
    }


    @Test
    public void passRotateTileNegative450() {
        Tile plus = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ZERO));
        GameState gs =
                new GameState(plus, testBoard7x7, playerOrder);
        Tile actualBefore = gs.getSpare();
        Tile expectedBefore = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.ZERO));
        assertEquals(expectedBefore, actualBefore);

        Tile expectedAfter = new Tile(rubyZoisite, new Connector(Unicode.PLUS, Orientation.TWO_SEVENTY));
        GameState expectedGs = new GameState(expectedAfter, testBoard7x7Expected, playerOrder);
        GameState actualGs = gs.performAction(new RotateTile(-450, gs));
        assertEquals(expectedGs, actualGs);
    }

    @Test
    public void invalidRotateTile5() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> gameStateOne.performAction(new RotateTile(5, gameStateOne)));
        assertEquals("Attempted to rotate an invalid degree", e.getMessage());
    }

    //endregion

    //region canReach()

    @Test
    public void canReachOutOfBounds() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> gameStateOne.canReach(new Position(-1, 6)));
        assertEquals("Cannot reach a position that is out of bounds", e.getMessage());
    }

    @Test
    public void canReachNotReachable() {
        assertFalse(gameStateOne.canReach(new Position(1, 4)));
    }

    @Test
    public void canReachReachable() {
        assertTrue(gameStateOne.canReach(new Position(2, 4)));
    }

    //endregion

    //region goalReached()

    @Test
    public void goalNotReached() {
        assertFalse(gameStateOne.isActivePlayerOnGoalTile());
    }

    @Test
    public void goalReached() {
        playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(avatar0, homePos0, goalPos0, goalPos0, 1, testPlayer0));
        playerOrder.add(playerStateOne);
        gameStateOne = new GameState(bZTile, testBoard7x7, playerOrder);
        assertTrue(gameStateOne.isActivePlayerOnGoalTile());
    }

    //endregion


    //region Move
    @Test
    public void failMove() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> gameStateOne.performAction(new MovePlayer(new Position(8, 8), gameStateOne)));

        assertEquals("Cannot move to a position out of bounds", exception.getMessage());
    }

    @Test
    public void moveWorks() {
        // position is updated, the goalReached field is NOT set when goal is reached (that is handled by referee)
        Queue<PlayerState> playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1), new Position(1, 3),
                new Position(1, 1), 0, player1));
        GameState gs = new GameState(bZTile, BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
                BoardTest.get49Bar90Connectors(), 49)), playerOrder);
        assertEquals(new Position(1, 1), gs.getActivePlayer().getCurrentPosition());
        assertEquals(false, gs.getActivePlayer().isPlayerGoingHome());

        gs.performAction(new MovePlayer(new Position(1, 3), gs));
        assertEquals(new Position(1, 3), gs.getActivePlayer().getCurrentPosition());
        assertEquals(false, gs.getActivePlayer().isPlayerGoingHome());

    }
    //endregion

    //region BoardAndSpare
    @Test
    public void passBoardAndSpareEquals() {
        assertEquals(new BoardAndSpare(testBoard7x7Expected, new Tile(berylZircon,
            new Connector(Unicode.CORNER, Orientation.ZERO))), new BoardAndSpare(testBoard7x7,
            spare));
    }
    //endregion

    //region getLastMove
    @Test
    public void getLastActionIfNoActionMade() {
        assertEquals(Optional.empty(), gameStateOne.getLastAction());
    }

    @Test
    public void getLastMove() {
        GameState newGs = gameStateOne.performAction(new SlideAndInsert(0,
                Direction.UP,
                gameStateOne));

        assertEquals(Optional.of(new SlidingAction(0, Direction.UP)), newGs.getLastAction());
    }
    //endregion

    //region kickOutPlayer()

    @Test
    public void passKickOutPlayer() {
        gameStateOne = gameStateOne.kickOutPlayer();
        Queue<PlayerState> afterExpected = new LinkedList<>();
        afterExpected.add(playerStateOne);
        afterExpected.add(playerStateTwo);
        assertEquals(afterExpected, gameStateOne.getPlayerOrder());
    }
    //endregion

    //region homeReached()
    /**
     * Gets the connectors for the horizontal bar only connector test
     * -
     * -
     * -
     * @return     list of connectors for horizontal bar only test
     */
    private List<Connector> getHorizBarOnlyConnectors() {
        List<Connector> barOnly = new ArrayList<>();
        for (int i=0; i < 49; i += 1) {
            barOnly.add(new Connector(Unicode.BAR, Orientation.NINETY));
        }
        return barOnly;

    }
    @Test
    public void homeGoalAreSameHomeReachWorks() {
        List<Tile> horizBarOnlyTiles = BoardTest.generateTiles(BoardTest.generateGemPairs(49), getHorizBarOnlyConnectors(), 49);
        Board testBoardHorizBarOnly = BoardTest.createBoard(7, 7, horizBarOnlyTiles);

        playerStateZero = new PlayerState(
                avatar0,
                new Position(1, 1), new Position(1, 3),
                new Position(1, 1), 1, testPlayer0);

        playerOrder = new LinkedList<>();
        playerOrder.add(playerStateZero);

        gameStateOne = new GameState(bZTile, testBoardHorizBarOnly, playerOrder);

        gameStateOne = gameStateOne.performAction(new RotateTile(0, gameStateOne));
        gameStateOne = gameStateOne.performAction(new SlideAndInsert(0, Direction.LEFT,
                gameStateOne));
        gameStateOne = gameStateOne.performAction(new MovePlayer(new Position(1, 3),
                gameStateOne));
        assertTrue(gameStateOne.isActivePlayerOnGoalTile());

        gameStateOne = gameStateOne.changeActivePlayer();

        gameStateOne = gameStateOne.performAction(new RotateTile(0, gameStateOne));
        gameStateOne = gameStateOne.performAction(new SlideAndInsert(0, Direction.LEFT,
                gameStateOne));
        gameStateOne = gameStateOne.performAction(new MovePlayer(new Position(1, 1),
                gameStateOne));

        assertTrue(gameStateOne.homeReached());
    }
    //endregion
    //region LimitedGS getters
    @Test
    public void passLimitedGSGetBoard() {
        assertEquals(new Board(testBoard7x7.getGridDeepCopy()), limitedGs1.getBoard());
    }

    @Test
    public void passLimitedGSGetSpare() {
        assertEquals(new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO)), limitedGs1.getSpare());
    }
    //endregion

    //region getNumPasses()

    //endregion

    //region changeActivePlayer()
    @Test
    public void changeActivePlayer() {
        assertEquals(playerStateOne, gameStateOne.changeActivePlayer().getActivePlayer());
        assertEquals(playerStateTwo, gameStateOne.changeActivePlayer().getActivePlayer());
        assertEquals(playerStateZero, gameStateOne.changeActivePlayer().getActivePlayer());
    }

    //endregion

    //region getLimitedGS()

    @Test
    public void getLimitedGSWorks() {

        // new GameState(bZTile, testBoard7x7, playerOrder, playerColorToState);
        LimitedGS actual = gameStateOne.getLimitedGS();

        Queue<LimitedPlayerState> expectedLPS = new LinkedList<>();
        expectedLPS.add(new LimitedPlayerState(avatar0, homePos0, currenPos0));
        expectedLPS.add(new LimitedPlayerState(avatar1, homePos1, currenPos1));
        expectedLPS.add(new LimitedPlayerState(avatar2, homePos2, currenPos2));

        LimitedGS expected = new LimitedGS(bZTile, testBoard7x7, expectedLPS, gameStateOne.getLastAction());

        assertEquals(expected, actual);
    }
    //endregion

    //region undoesLastAction()
    @Test
    public void undoesLastAction() {
        gameStateOne = gameStateOne.performAction(new SlideAndInsert(2,
                Direction.LEFT,
                gameStateOne));
        assertTrue(Rules.getInstance().doesPlayUndoLastAction(
                new PlayInfo(2, Direction.RIGHT, 0, new Position(1, 1)),
                gameStateOne.getLastAction()));
    }

    @Test
    public void doesNotUndo() {
        gameStateOne = gameStateOne.performAction(new SlideAndInsert(2,
                Direction.LEFT,
                gameStateOne));
        assertFalse(Rules.getInstance().doesPlayUndoLastAction(
                new PlayInfo(0, Direction.RIGHT, 0, new Position(1, 1)),
                gameStateOne.getLastAction()));
    }

    //endregion

    //start region getNumPlayers()
    @Test
    public void getNumPlayersWorksPlayerKicked() {
        gameStateOne = gameStateOne.kickOutPlayer();
        gameStateOne = gameStateOne.changeActivePlayer();
        gameStateOne = gameStateOne.changeActivePlayer();
        gameStateOne = gameStateOne.changeActivePlayer();

        assertEquals(2, gameStateOne.getNumPlayers());
    }

    @Test
    public void getNumPlayersWorks() {
        gameStateOne = gameStateOne.changeActivePlayer();
        gameStateOne = gameStateOne.changeActivePlayer();
        gameStateOne = gameStateOne.changeActivePlayer();

        assertEquals(3, gameStateOne.getNumPlayers());
    }
    //end region

    // Tests the functionality of getGoalsToDistribute() on a GameState with an empty
    // queue of goals to distribute.
    @Test
    public void testGetNextDistributedGoalInEmptyQueue() {
        assertTrue(gameStateOne.getGoalsToDistribute().isEmpty());
        Optional<Position> nextGoal = gameStateOne.getNextDistributedGoal();
        assertTrue(nextGoal.isEmpty());
        assertTrue(gameStateOne.getGoalsToDistribute().isEmpty());
    }

    // Tests the functionality of getGoalsToDistribute() on a GameState with a non-empty
    // queue of goals to distribute.
    @Test
    public void testGetNextDistributedGoalInNonEmptyQueue() {
        Queue<Position> goalsToDistribute = new LinkedList<>(Arrays.asList(
                new Position(1,1),
                new Position(1,3),
                new Position(1,5)));
        GameState gameStateWithGoals = new GameState(bZTile, testBoard7x7, playerOrder, Optional.empty(), goalsToDistribute);
        assertEquals(3, gameStateWithGoals.getGoalsToDistribute().size());

        Optional<Position> nextGoal = gameStateWithGoals.getNextDistributedGoal();
        assertTrue(nextGoal.isPresent());
        assertEquals(new Position(1,1), nextGoal.get());
        assertEquals(2, gameStateWithGoals.getGoalsToDistribute().size());

        nextGoal = gameStateWithGoals.getNextDistributedGoal();
        assertTrue(nextGoal.isPresent());
        assertEquals(new Position(1,3), nextGoal.get());
        assertEquals(1, gameStateWithGoals.getGoalsToDistribute().size());

        nextGoal = gameStateWithGoals.getNextDistributedGoal();
        assertTrue(nextGoal.isPresent());
        assertEquals(new Position(1,5), nextGoal.get());
        assertTrue(gameStateWithGoals.getGoalsToDistribute().isEmpty());

        nextGoal = gameStateWithGoals.getNextDistributedGoal();
        assertTrue(nextGoal.isEmpty());
        assertTrue(gameStateWithGoals.getGoalsToDistribute().isEmpty());
    }

    // Test the functionality of generateGameState with a board of size 7x7 and 3 players.
    @Test
    public void testGenerateGameStateMedium() {
        List<IPlayer> testIPlayers = new ArrayList<>(Arrays.asList(testPlayer0, testPlayer1, testPlayer2));
        testGenerateGameStateWithParameters(7, 7, testIPlayers);
    }

    // Test the functionality of generateGameState with a board of size 5x5 and 2 players.
    @Test
    public void testGenerateGameStateSmall() {
        List<IPlayer> testIPlayers = new ArrayList<>(Arrays.asList(testPlayer0, testPlayer1));
        testGenerateGameStateWithParameters(5, 5, testIPlayers);
    }

    // Test the functionality of generateGameState with a board of size 9x9 and 5 players.
    @Test
    public void testGenerateGameStateLarge() {
        List<IPlayer> testIPlayers = new ArrayList<>(Arrays.asList(testPlayer0, testPlayer1, testPlayer2,
                new Player("Test Player 3", new EuclidStrategy()),
                new Player("Test Player 4", new EuclidStrategy())));
        testGenerateGameStateWithParameters(9, 9, testIPlayers);
    }

    private void testGenerateGameStateWithParameters(int numRows, int numColumns, List<IPlayer> players) {
        GameState generatedGameState = GameState.generateGameState(numRows, numColumns, players);
        assertEquals(numRows, generatedGameState.getBoard().getNumRows());
        assertEquals(numColumns, generatedGameState.getBoard().getNumColumns());
        testAllPlayerStatesHaveUniqueHomes(generatedGameState.getPlayerOrder());
        testAllPlayerStatesHaveUniqueGoals(generatedGameState.getPlayerOrder());
        assertEquals(0, generatedGameState.getGoalsToDistribute().size());
    }

    // Tests that all players in the given queue of PlayerStates have all unique home positions.
    private void testAllPlayerStatesHaveUniqueHomes(Queue<PlayerState> playerStates) {
        testAllPlayerStatePositionsAreUnique(playerStates, PlayerState::getHomePosition);
    }

    // Tests that all players in the given queue of PlayerStates have all unique goal positions.
    private void testAllPlayerStatesHaveUniqueGoals(Queue<PlayerState> playerStates) {
        testAllPlayerStatePositionsAreUnique(playerStates, PlayerState::getGoalPosition);
    }

    // Tests that all players in the given queue of PlayerStates have unique positions returned by the given function.
    private void testAllPlayerStatePositionsAreUnique(Queue<PlayerState> playerStates,
                                                      Function<PlayerState, Position> getPositionFromPlayerState) {
        Set<Position> uniquePositions = new HashSet<>();
        for (PlayerState playerState : playerStates) {
            Position playerPositionField = getPositionFromPlayerState.apply(playerState);
            assertFalse(uniquePositions.contains(playerPositionField));
            uniquePositions.add(playerPositionField);
        }
    }

}
