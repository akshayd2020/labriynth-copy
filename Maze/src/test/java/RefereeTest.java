import java.awt.Color;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;

import static org.junit.jupiter.api.Assertions.*;

/*

    1. a player reaches home after visiting its goal tile
        - one player, riemann
        - place the current position to be right next to goal + reachable to goal
    2. all players pass
        multiple players are on the way back home & have the same minimal euclidean distance
        - 2 players, make an instance of a Player obj where playTurn function always returns pass
            - playTurn function always returns pass
            - all of their goalReached flags are all true in the GameState
            - all players are one tile away from goal
            - all current positions are one unit away from their goals, but they can't move there
    3. the referee runs 1000 rounds
        - 1 players that are on bars in a board of minuses

        - no players can move at all on the board
        - board is all minuses, they are all 1 tile away from their goals

   5. no playeres are on way back home, winners share minimal euclidean distance to goal
        - all goalReached are false
        - all pass
        - return

    4. all players get kicked out => [[][all players misbehaved]]
        player gets kicked out when they shift an immoveable row
        player gets kicked out when they try to move to a immoveable position
        player gets kicked out if they do not move to a new position during a Move (not a pass)

        - make a player that playTurn returns a 3 as index for row
        - try to move to -1,-1
        - a player that moves to the same position as their current position
     */
public class RefereeTest {
    UnorderedPair<Gem, Gem> berylZircon;
    Tile bZTile;
    Tile minusTile;
    Color magenta;
    IPlayer testPlayer;
    List<IPlayer> playersSorted1;
    PlayerState playerState1;
    Board board7x7;
    GameState gameState1;
    Queue<PlayerState> playerOrderWithMultiplePlayers;
    Queue<PlayerState> playerOrderWithMultiplePlayers2;

    Referee referee;

    IPlayer player1Pass;
    IStrategy riemann;

    IPlayer player2Pass;
    IPlayer playerShiftImmoveable;
    IPlayer playerMoveToInvalid;
    IPlayer playerNeverMoves;
    IPlayer playerInfiniteLoopSetUp;
    IPlayer playerInfiniteLoopTakeTurn;
    IPlayer playerInfiniteLoopWon;

    Board boardAllMinusBar = BoardTest.createBoard(7, 7, BoardTest.generateTiles(BoardTest.generateGemPairs(49),
            BoardTest.get49Bar90Connectors(), 49));

    @BeforeEach
    public void setup() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        minusTile = new Tile(berylZircon, new Connector(Unicode.BAR, Orientation.NINETY));
        magenta = Color.MAGENTA;
        riemann = new RiemannStrategy();
        testPlayer = new Player("Jess", riemann);

        board7x7 = BoardTest.generateStaticBoard();
        playersSorted1 = new ArrayList<>();
        playersSorted1.add(testPlayer);

        playerState1 = new PlayerState(magenta, new Position(1, 1),
                new Position(1, 1), new Position(1, 1), 1, testPlayer);
        Map<Color, PlayerState> playerColorToState1 = new HashMap<>();

        Queue<PlayerState> playerOrder1 = new LinkedList<>();
        playerOrder1.add(playerState1);

        PlayerState ps1 = new PlayerState(Color.RED, new Position(1, 1), new Position(3, 3),
                new Position(1, 1), 0, new Player("Test Player 1", new RiemannStrategy()));
        PlayerState ps2 = new PlayerState(Color.GREEN, new Position(5, 5), new Position(3, 5),
                new Position(5, 5), 0, new Player("Test Player 2", new EuclidStrategy()));
        PlayerState ps3 = new PlayerState(Color.BLUE, new Position(1, 3), new Position(1, 1),
                new Position(1, 3), 0, new Player("Test Player 3", new RiemannStrategy()));
        playerOrderWithMultiplePlayers = new LinkedList<>(Arrays.asList(ps1, ps2, ps3));

        ps1 = new PlayerState(Color.RED, new Position(1, 1), new Position(3, 3),
                new Position(1, 1), 0, new Player("Test Player 1", new RiemannStrategy()));
        ps2 = new PlayerState(Color.GREEN, new Position(5, 5), new Position(5, 5),
                new Position(3, 3), 0, new Player("Test Player 2", new EuclidStrategy()));
        ps3 = new PlayerState(Color.BLUE, new Position(1, 3), new Position(1, 1),
                new Position(1, 3), 0, new Player("Test Player 3", new RiemannStrategy()));
        playerOrderWithMultiplePlayers2 = new LinkedList<>(Arrays.asList(ps1, ps2, ps3));

        playerColorToState1.put(magenta, playerState1);
        gameState1 = new GameState(bZTile, board7x7, playerOrder1);
        referee = new Referee();

        player1Pass = new Player("Jamie", riemann);

        player2Pass = new Player("Jess", riemann);

        playerShiftImmoveable = new PlayerShiftImmoveable("Jamie", riemann);
        playerMoveToInvalid = new PlayerMoveToInvalid("Jamie", riemann);
        playerNeverMoves = new PlayerNeverMoves("Jamie", riemann);

        playerInfiniteLoopSetUp = new PlayerLoopsSetUp("Loopy", riemann);
        playerInfiniteLoopTakeTurn = new PlayerLoopsTakeTurn("Looper", riemann);
        playerInfiniteLoopWon = new PlayerLoopsWon("Loopifer", riemann);
    }

    @Test
    public void playerWinsByReachingHomeTile() {
        Set<IPlayer> expectedWinners = new HashSet<>(Arrays.asList(new Player("Jess", new Position(1, 1),
                riemann, true)));
        Set<IPlayer> expectedMisbehaved = new HashSet<>();
        GameResult expected = new GameResult(expectedWinners, expectedMisbehaved);
        GameResult actual = referee.setupAndRunGame(gameState1, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void gameCompleteAfter1000Rounds() {
        Tile[][] boardAllMinusBarGrid = boardAllMinusBar.getGridDeepCopy();
        boardAllMinusBarGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE),
                new Connector(Unicode.BAR, Orientation.ZERO));
        boardAllMinusBar = new Board(boardAllMinusBarGrid);

        Queue<PlayerState> playerOrder = new LinkedList<>();

        playerOrder.add(new PlayerState(Color.GREEN, new Position(0, 0),
                new Position(1,1), new Position(6, 6), 0, testPlayer));
        GameState gs = new GameState(new Tile(new UnorderedPair(Gem.BERYL, Gem.ZOISITE),
                new Connector(Unicode.BAR, Orientation.NINETY)), boardAllMinusBar,
                playerOrder, Optional.empty());

        Set<IPlayer> expectedWinners = new HashSet<>(Arrays.asList(new IPlayer[]
                {new Player("Jess", new Position(1,1), riemann, true)}));
        Set<IPlayer> expectedMisbehaved = new HashSet<>();
        GameResult expected = new GameResult(expectedWinners, expectedMisbehaved);

        Referee ref = new Referee();
        assertEquals(expected, ref.setupAndRunGame(gs, 999));
    }

    @Test
    public void gameCompleteWinnersAreEquidistantToGoal() {
        Tile[][] boardAllMinusBarGrid = boardAllMinusBar.getGridDeepCopy();
        boardAllMinusBarGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE),
                new Connector(Unicode.BAR, Orientation.ZERO));
        boardAllMinusBarGrid[1][3] = new Tile(new UnorderedPair(Gem.YELLOW_HEART, Gem.YELLOW_JASPER),
                new Connector(Unicode.BAR, Orientation.ZERO));

        boardAllMinusBar = new Board(boardAllMinusBarGrid);
        Queue<PlayerState> playerOrder = new LinkedList<>();
        IPlayer jamieRiemann = new Player("Jamie", new RiemannStrategy());

        playerOrder.add(new PlayerState(Color.GREEN, new Position(5, 5),
                new Position(3,1), new Position(1,1), 0, testPlayer));
        playerOrder.add(new PlayerState(Color.RED, new Position(5, 3),
                new Position(1,5), new Position(1,3), 0, jamieRiemann));

        GameState gs = new GameState(minusTile, boardAllMinusBar, playerOrder);


        Set<IPlayer> expectedWinners = new HashSet<>(Arrays.asList(new IPlayer[] {testPlayer, jamieRiemann}));
        Set<IPlayer> expectedMisbehaved = new HashSet<>();

        Referee ref = new Referee();

        GameResult actual = ref.setupAndRunGame(gs, 0);

        testPlayerSetEquality(expectedWinners, actual.getWinners());
        testPlayerSetEquality(expectedMisbehaved, actual.getMisbehaved());
    }

    @Test
    public void allPlayersPassAndWinnersAreClosestToHome() {
        Tile[][] boardAllMinusBarGrid = boardAllMinusBar.getGridDeepCopy();
        boardAllMinusBarGrid[1][1] = new Tile(new UnorderedPair(Gem.ZIRCON, Gem.ZOISITE),
                new Connector(Unicode.BAR, Orientation.ZERO));
        boardAllMinusBarGrid[1][3] = new Tile(new UnorderedPair(Gem.YELLOW_HEART, Gem.YELLOW_JASPER),
                new Connector(Unicode.BAR, Orientation.ZERO));

        boardAllMinusBar = new Board(boardAllMinusBarGrid);
        Queue<PlayerState> playerOrder = new LinkedList<>();
        playerOrder.add(new PlayerState(Color.GREEN, new Position(3,1),
                new Position(3, 1), new Position(1,1), 1, player1Pass));
        playerOrder.add(new PlayerState(Color.RED, new Position(1,5),
            new Position(1, 5), new Position(1,3), 1, player2Pass));
        GameState gs = new GameState(bZTile, boardAllMinusBar, playerOrder);

        Set<IPlayer> expectedWinners = new HashSet<>(Arrays.asList(new IPlayer[] {player1Pass, player2Pass}));
        Set<IPlayer> expectedMisbehaved = new HashSet<>();

        Referee ref = new Referee();

        GameResult actual = ref.setupAndRunGame(gs, 0);

        testPlayerSetEquality(expectedWinners, actual.getWinners());
        testPlayerSetEquality(expectedMisbehaved, actual.getMisbehaved());
    }

    @Test
    public void allPlayersMisbehave() {
        Queue<PlayerState> playerOrder = new LinkedList<>();

        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1), new Position(3,3),
                new Position(0, 0), 1, playerShiftImmoveable));
        playerOrder.add(new PlayerState(Color.RED, new Position(1, 3), new Position(3,3),
                new Position(0, 0), 1, playerMoveToInvalid));
        playerOrder.add(new PlayerState(Color.BLUE, new Position(1, 5), new Position(3,3),
                new Position(0, 0), 1, playerNeverMoves));
        GameState gs = new GameState(bZTile, boardAllMinusBar, playerOrder);

        Referee ref = new Referee();

        Set<IPlayer> expectedWinners = new HashSet<>();
        Set<IPlayer> expectedMisbehaved = new HashSet<>(Arrays.asList(new IPlayer[] {playerShiftImmoveable,
                playerMoveToInvalid, playerNeverMoves}));


        GameResult actual = ref.setupAndRunGame(gs, 7);

        testPlayerSetEquality(expectedWinners, actual.getWinners());
        testPlayerSetEquality(expectedMisbehaved, actual.getMisbehaved());
    }

    @Test
    public void playerInfiniteLoopHandling() {
        Queue<PlayerState> playerOrder = new LinkedList<>();

        playerOrder.add(new PlayerState(Color.GREEN, new Position(1, 1), new Position(3,3),
                new Position(0, 0), 1, playerInfiniteLoopSetUp));
        playerOrder.add(new PlayerState(Color.RED, new Position(1, 3), new Position(3,3),
                new Position(0, 0), 1, playerInfiniteLoopTakeTurn));
        playerOrder.add(new PlayerState(Color.BLUE, new Position(1, 5), new Position(3,3),
                new Position(0, 0), 1, playerInfiniteLoopWon));
        GameState gs = new GameState(bZTile, boardAllMinusBar, playerOrder);

        Referee ref = new Referee();
        GameResult actual = ref.setupAndRunGame(gs, 998);

        Set<IPlayer> expectedWinners = new HashSet<>();
        Set<IPlayer> expectedMisbehaved = new HashSet<>(Arrays.asList(new IPlayer[] {playerInfiniteLoopSetUp,
                playerInfiniteLoopTakeTurn, playerInfiniteLoopWon}));

        testPlayerSetEquality(expectedWinners, actual.getWinners());
        testPlayerSetEquality(expectedMisbehaved, actual.getMisbehaved());
    }

    @Test
    public void testWithMultipleGoalsToDistribute() {
        Queue<Position> goalsToDistribute = new LinkedList<>(Arrays.asList(
                new Position(3, 1),
                new Position(3, 5),
                new Position(5, 1)));
        GameState gs = new GameState(bZTile, board7x7, playerOrderWithMultiplePlayers, Optional.empty(), goalsToDistribute);
        Referee ref = new Referee();
        GameResult result = ref.setupAndRunGame(gs, 0);
        assertEquals(1, result.getWinners().size());
        testExpectedPlayerName("Test Player 1", result.getWinners());
        assertTrue(result.getMisbehaved().isEmpty());
    }

    // Tests that if two players have the same number of goals and the same distance from their next goal,
    // the game terminating player will win.
    @Test
    public void testGameTerminatingPlayerWins() {
        GameState gs = new GameState(bZTile, BoardTest.generateStaticPlusBoard(), playerOrderWithMultiplePlayers2, Optional.empty());
        Referee ref = new Referee();
        GameResult result = ref.setupAndRunGame(gs, 0);
        assertEquals(1, result.getWinners().size());
        testExpectedPlayerName("Test Player 1", result.getWinners());
        assertTrue(result.getMisbehaved().isEmpty());
    }

    // Tests that if the game terminating player has less goals reached than another player,
    // the game terminating player will lose.
    @Test
    public void testGameTerminatingPlayerLoses() {
        Queue<Position> goalsToDistribute = new LinkedList<>(List.of(new Position(3, 1)));
        PlayerState ps1 = new PlayerState(Color.RED, new Position(1, 1), new Position(3, 3),
                new Position(1, 1), 0, new Player("Test Player 1", new RiemannStrategy()));
        PlayerState ps2 = new PlayerState(Color.GREEN, new Position(5, 5), new Position(3, 3),
                new Position(1, 1), 0, new Player("Test Player 2", new EuclidStrategy()));
        Queue<PlayerState> playerOrder = new LinkedList<>(Arrays.asList(ps1, ps2));

        GameState gs = new GameState(bZTile, BoardTest.generateStaticPlusBoard(), playerOrder, Optional.empty(), goalsToDistribute);
        Referee ref = new Referee();
        GameResult result = ref.setupAndRunGame(gs, 0);
        assertEquals(1, result.getWinners().size());
        testExpectedPlayerName("Test Player 1", result.getWinners());
        assertTrue(result.getMisbehaved().isEmpty());
    }

    @Test
    public void testGameRunsWithNoPlayers() {
        Queue<PlayerState> playerOrder = new LinkedList<>();

        GameState gs = new GameState(bZTile, BoardTest.generateStaticPlusBoard(), playerOrder, Optional.empty());
        Referee ref = new Referee();
        GameResult result = ref.setupAndRunGame(gs, 0);
        assertTrue(result.getWinners().isEmpty());
        assertTrue(result.getMisbehaved().isEmpty());
    }

    // Tests that the given player name exists in the given set of players.
    private void testExpectedPlayerName(String playerName, Set<IPlayer> players) {
        for (IPlayer player : players) {
            assertEquals(playerName, player.getName());
        }
    }

    private void testPlayerSetEquality(Set<IPlayer> set1, Set<IPlayer> set2) {
        assertEquals(set1.size(), set2.size());
        List<IPlayer> set1List = new ArrayList<>(set1);
        List<IPlayer> set2List = new ArrayList<>(set2);
        for (IPlayer player: set1List) {
            assertTrue(set2List.contains(player));
        }
        for (IPlayer player: set2List) {
            assertTrue(set1List.contains(player));
        }
    }

}
