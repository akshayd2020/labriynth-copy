import com.google.common.util.concurrent.SimpleTimeLimiter;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import javafx.util.Pair;

import java.util.*;

import com.google.common.util.concurrent.TimeLimiter;

import javax.swing.text.html.Option;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A referee runs a game from start to finish.
 *
 * Failure modes:
 * - if a player makes an invalid move(moves an immoveable row/column, moves to a non-reachable position, doesn't move
 *      to a new position but tries to make a non-pass move)
 * - if a player undoes the last player's move
 * - if calling takeTurn on a player results in an exception being thrown
 * - if calling takeTurn on a player results in an infinite loop
 */
public class Referee {
    private GameState gameState;
    private final int NUM_ROWS = 7;
    private final int NUM_COLUMNS = 7;
    private final int MAX_ROUNDS = 1000;
    // number of seconds for a player to respond
    private final int TIME_TO_RESPOND = 4;

    private int numPassesInRound = 0;

    private int numRounds = 0;
    private final Rules rules = Rules.getInstance();

    private Set<IPlayer> winners = new HashSet<>();
    private Set<IPlayer> misbehaved = new HashSet<>();

    private List<IObserver> observers;

    public Referee() {
        this.observers = new ArrayList<>();
    }

    public Referee(IObserver observer) {
        this.observers = new ArrayList<>(Arrays.asList(observer));
    }
    public Referee(List<IObserver> observers) {
        this.observers = observers;
    }
    /**
     * Setup the game with a generated game state and run the game from start to finished to get the output
     * of the game as an array of winners and players that misbehave.
     * @param playersSorted the list of sorted players for this game.
     * @return              the outcome of winners and players that misbehaved
     */
    public GameResult setupAndRunGame(List<IPlayer> playersSorted) {
        makeRandomGSAndSetUpPlayers(playersSorted);
        return runGame();
    }

    /**
     * Setup the game with a given game state and run the game from start to finished to get the output
     * of the game as an array of winners and players that misbehave
     * @param gameState     the initial game state to run the game
     * @param numRounds     the number of rounds this game will start running from
     * @return              the outcome of winners and players that misbehaved
     */
    public GameResult setupAndRunGame(GameState gameState, int numRounds) {
        this.gameState = gameState;
        this.numRounds = numRounds;
        setupPlayers(gameState);
        return runGame();
    }

    /**
     * Run the game from start to finish.
     * If an observer is present, the observer will be informed of the initial game state
     * before the referee starts running player turns.
     * @return the outcome of winners and players that misbehaved
     */
    private GameResult runGame() {
        notifyObserversOfStateChange();
        Optional<IPlayer> gameTerminatingPlayer = takingTurns();
        winners = getWinners(gameTerminatingPlayer);
        relayScores(winners);
        return new GameResult(this.winners, this.misbehaved);
    }

    /**
     * Initializes the gameState field with a starting game state which includes a board that has the
     * specified dimensions and sets up all players with their corresponding information.
     * @param playersSorted the list of sorted players in this game state.
     */
    private void makeRandomGSAndSetUpPlayers(List<IPlayer> playersSorted) {
        this.gameState = GameState.generateGameState(NUM_ROWS, NUM_COLUMNS, playersSorted);
        this.setupPlayers(gameState);
    }

    /**
     * Set up all the players with their corresponding goals
     * @param gameState     initial game state with the mapped player states
     */
    private void setupPlayers(GameState gameState) {
        int initialNumPlayers = gameState.getNumPlayers();
        for (int i = 0; i < initialNumPlayers; i += 1) {
            PlayerState currentPlayerState = gameState.getActivePlayer();
            IPlayer player = currentPlayerState.getPlayer();
            Position goalPos = currentPlayerState.getGoalPosition();
            if (safePlayerSetUp(player, goalPos)) {
                gameState.changeActivePlayer();
            }
        }
    }

    /**
     * Runs all the rounds in a game until completion and updates this referee's set of those who misbehaved if
     * any player misbehaves. If the observer is present, the observer is notified when the game is over.
     * @return   the game-terminating player if it exists.
     */
    private Optional<IPlayer> takingTurns() {
        Optional<IPlayer> gameTerminatingPlayer = Optional.empty();
        while (!isGameOver()) {
            gameTerminatingPlayer = playOneRound();
            if (gameTerminatingPlayer.isPresent()) {
                break;
            }
            if (allPlayersPassed()) {
                break;
            }
            this.numPassesInRound = 0;
            this.numRounds += 1;
        }
        notifyObserversOfGameOver();
        return gameTerminatingPlayer;
    }

    private boolean allPlayersPassed() {
        return this.numPassesInRound == this.gameState.getPlayerOrder().size();
    }

    /**
     * Play one round and adds misbehavers to misbehaved players.
     * @return the game-terminating player if it exists.
     */
    private Optional<IPlayer> playOneRound() {
        List<PlayerState> playerStates = new ArrayList<>(this.gameState.getPlayerOrder());
        for (PlayerState playerState: playerStates) {
            IPlayer currPlayer = playerState.getPlayer();
            Optional<IPlayer> gameTerminatingPlayer = playSingleMoveAndCheckWin(currPlayer);
            notifyObserversOfStateChange();
            if (gameTerminatingPlayer.isPresent()) {
                return gameTerminatingPlayer;
            }
        }
        return Optional.empty();
    }

    /**
     * Play a single move specified by the currently active player and return whether this player has won.
     * Also adds the given player to the given set of those who misbehaved if they misbehave
     * A player misbehaves by:
     * - moving an immoveable row/column
     * - moving to a position not reachable
     * - not moving to a new position while taking a turn (not a pass)
     * - if they take too long / cause an infinite loop
     * - if they raise an exception or error
     * @return the game-terminating player if it exists.
     */
    private Optional<IPlayer> playSingleMoveAndCheckWin(IPlayer player) {
        Pair<Optional<PlayInfo>, Boolean> playerMovePair = safePlayerTakeTurn(player); // does not exit
        Optional<PlayInfo> currentPlay = playerMovePair.getKey();
        if (playerMovePair.getValue()) {
            if (this.isPass(currentPlay)) {
                this.numPassesInRound += 1;
                this.gameState.changeActivePlayer();
            } else if (rules.isValidMove(currentPlay.get(), this.gameState)) {
                return this.moveAndCheckWin(player, currentPlay.get());
            } else {
                handleMisbehaved(player);
            }
        }
        return Optional.empty();
    }

    /**
     * Add to the list of those who misbehaved and kickout the player from the game
     * @param currentPlayer    the current player
     */
    private void handleMisbehaved(IPlayer currentPlayer) {
        this.misbehaved.add(currentPlayer);
        this.gameState = this.gameState.kickOutPlayer();
    }

    /**
     * Complete the given move and determine if it led to a win. Add to list of misbehaved if current player misbehaved
     * @param currentPlayer     the current player
     * @param currentPlay       the current play information
     * @return                  the game-terminating player if it exists.
     */
    private Optional<IPlayer> moveAndCheckWin(IPlayer currentPlayer, PlayInfo currentPlay) {
        this.gameState = applyMove(currentPlay);
        boolean won = this.gameState.didActivePlayerWin();
        redirectPlayerToNextGoal(currentPlayer);
        return won
                ? Optional.of(currentPlayer)
                : Optional.empty();
    }

    /**
     * Gives the active player a new goal if they need one.
     * @param   currentPlayer the player to set up with a new goal.
     */
    private void redirectPlayerToNextGoal(IPlayer currentPlayer) {
        if (doesActivePlayerNeedNewGoal()) {
            this.gameState.setNewGoalForActivePlayer();
            Position nextGoal = this.gameState.getGoalPosition();
            if (!safePlayerSetUp(currentPlayer, nextGoal)) {
                return;
            }
        }
        this.gameState.changeActivePlayer();
    }

    /**
     * Determines if the active player needs to update their goal because they have
     * reached their current intermediary goal.
     * @return  true if the player should get a new goal, false otherwise.
     */
    private boolean doesActivePlayerNeedNewGoal() {
        return this.gameState.isActivePlayerOnGoalTile()
                && !this.gameState.didActivePlayerWin();
    }

    /**
     * Is this game over? A game is over when:
     * - the number of rounds reaches the MAX_ROUNDS
     * - the number of players left in a game is 0
     * @return whether the game is over
     */
    private boolean isGameOver() {
        return this.numRounds == MAX_ROUNDS || this.gameState.getPlayerOrder().size() == 0;
    }

    /**
     * Is this play information a pass?
     * @param playInfo  play information
     * @return          whether or not this play information is a pass
     */
    private boolean isPass(Optional<PlayInfo> playInfo) {
        return playInfo.isEmpty();
    }

    private GameState applyMove(PlayInfo turn) {
        GameState updatedGS = this.gameState.performAction(new RotateTile(turn.getDegreeToRotate(), this.gameState));
        updatedGS = this.gameState.performAction(new SlideAndInsert(turn.getIndex(), turn.getDirection(), updatedGS));
        updatedGS = this.gameState.performAction(new MovePlayer(turn.getNewPosition(), updatedGS));
        return updatedGS;
    }

    /**
     * Get the winners of a single game. If at least 1 player has reached their goal, get the players with the minimum
     * distance to their homes. If there are no players who have reached their goal, get the players with the minimum
     * distance to their goals.
     * @return  the winners of a single game
     */
    private Set<IPlayer> getWinners(Optional<IPlayer> gameTerminatingPlayer) {
        Set<PlayerState> maxGoalReachers = getPlayersWithHighestGoalsVisited();
        if (doesGameTerminatingPlayerHaveHighestGoals(maxGoalReachers, gameTerminatingPlayer)) {
            return new HashSet<>(List.of(gameTerminatingPlayer.get()));
        }
        return getPlayersWithMinDistanceToNextGoal(maxGoalReachers);
    }

    private boolean doesGameTerminatingPlayerHaveHighestGoals(
            Set<PlayerState> maxGoalReachers,
            Optional<IPlayer> gameTerminatingPlayer) {

        return gameTerminatingPlayer.isPresent() &&
                maxGoalReachers.stream().anyMatch((s) -> s.getPlayer().equals(gameTerminatingPlayer.get()));
    }

    /**
     * Get the players that visited the highest number of goal tiles
     * @return  all the players that visited the highest number of goal tiles
     */
    private Set<PlayerState> getPlayersWithHighestGoalsVisited() {
        ToIntFunction<PlayerState> getGoalsReached = PlayerState::getNumberOfGoalsReached;
        BiFunction<Integer, Integer, Boolean> greaterThanComparison = ((extreme, current) -> extreme < current);
        int minimumPossibleNumberOfGoals = 0;
        return getPlayersComparisonHelper(this.gameState.getPlayerOrder(),
                getGoalsReached, minimumPossibleNumberOfGoals, greaterThanComparison);
    }

    /**
     * Get the players that have the minimal distance to their goals
     * @return  the players that have the minimal distance to their goals
     */
    private Set<IPlayer> getPlayersWithMinDistanceToNextGoal(Set<PlayerState> potentialWinners) {
        ToIntFunction<PlayerState> getSquaredDistFromGoal =
                (x -> Position.calculatedSquareDistance(x.getCurrentPosition(), x.getGoalPosition()));
        BiFunction<Integer, Integer, Boolean> lessThanComparison = ((extreme, current) -> extreme > current);
        int maximumPossibleDistanceToGoal = this.gameState.maximumSquareDistance();
        Set<PlayerState> playersWithMinDistanceToNextGoal = getPlayersComparisonHelper(potentialWinners,
                getSquaredDistFromGoal, maximumPossibleDistanceToGoal, lessThanComparison);
        return convertPlayerStatesToIPlayers(playersWithMinDistanceToNextGoal);
    }

    /**
     * Converts a set of PlayerStates to a set of IPlayers.
     * @param playerStates  the set of PlayerStates to convert.
     * @return              the corresponding set of IPlayers.
     */
    private Set<IPlayer> convertPlayerStatesToIPlayers(Set<PlayerState> playerStates) {
        return playerStates.stream().map(PlayerState::getPlayer).collect(Collectors.toSet());
    }

    /**
     * Abstracts out the functionality to get all players that have the minimum or maximum attribute
     * as specified by the given parameters.
     * @param candidates List of candidate players
     * @param getPlayerAttribute function to get an integer attribute of a player
     * @param extremeValue worst possible value for an attribute
     * @param intCompare comparison function to compare integer attributes
     * @return the set of candidates that has the best attribute
     */
    private Set<PlayerState> getPlayersComparisonHelper(Collection<PlayerState> candidates,
        ToIntFunction<PlayerState> getPlayerAttribute,
        int extremeValue,
        BiFunction<Integer, Integer, Boolean> intCompare) {

        Set<PlayerState> ans = new HashSet<>();
        for (PlayerState currentPlayerState : candidates) {
            int currValue = getPlayerAttribute.applyAsInt(currentPlayerState);
            if (intCompare.apply(extremeValue, currValue)) {
                ans = new HashSet<>(Arrays.asList(currentPlayerState));
                extremeValue = currValue;
            } else if (currValue == extremeValue) {
                ans.add(currentPlayerState);
            }
        }
        return ans;
    }

    /**
     * Relay scores to all players who did not misbehave. If a player misbehaves while we send their
     * winning status, they are considered misbehaved and are moved to the misbehaved list.
     * @param winners       a list of players that are the winners
     */
    private void relayScores(Set<IPlayer> winners) {
        int numPlayers = this.gameState.getNumPlayers();
        for (int i = 0; i < numPlayers; i += 1) {
            IPlayer player = this.gameState.getActivePlayer().getPlayer();
            boolean safelyUpdatedWonStatus;
            if (winners.contains(player)) {
                safelyUpdatedWonStatus = safePlayerWon(player, true);
            } else {
                safelyUpdatedWonStatus = safePlayerWon(player, false);
            }
            if (safelyUpdatedWonStatus) {
                this.gameState.changeActivePlayer();
            }
        }
    }

    /**
     * Safely performs set up on a specified player, kicking them if the method throws an exception or error.
     * @param player    the player to run the method on.
     * @param target    the target position to give the player.
     * @return          true if the given player was set up with the given target without exceptions, false otherwise
     */
    private boolean safePlayerSetUp(IPlayer player, Position target) {
        try {
            IPlayer playerWithTimeout = makePlayerWithTimeout(player);
            playerWithTimeout.setup(Optional.of(this.gameState.getLimitedGS()), target);
            return true;
        } catch (Exception | Error e) {
            handleMisbehaved(player);
            return false;
        }
    }

    /**
     * Safely performs take turn on a specified player, kicking them if the method throws an exception or error.
     * @param player    the player to run the method on.
     * @return          a pair of the Optional PlayInfo and a boolean representing if the turn didn't throw an error
     *                  or exception.
     */
    private Pair<Optional<PlayInfo>, Boolean> safePlayerTakeTurn(IPlayer player) {
        try {
            IPlayer playerWithTimeout = makePlayerWithTimeout(player);
            Optional<PlayInfo> playerMove = playerWithTimeout .takeTurn(this.gameState.getLimitedGS());
            return new Pair<>(playerMove, true);
        } catch (Exception | Error e) {
            handleMisbehaved(player);
        }
        return new Pair<>(Optional.empty(), false);
    }

    /**
     * Safely performs won on a specified player, kicking them if the method throws an exception or error.
     * If the player has won, but misbehaves in the won method, they will no longer be considered a winner.
     * @param player    the player to run the method on.
     * @param won       the status if a player has won.
     * @return          true if the given player was set up with the given target without exceptions, false otherwise
     */
    private boolean safePlayerWon(IPlayer player, boolean won) {
        try {
            IPlayer playerWithTimeout = makePlayerWithTimeout(player);
            playerWithTimeout.won(won);
            return true;
        } catch (Exception | Error e) {
            handleMisbehaved(player);
            if (won) {
                this.winners.remove(player);
            }
            return false;
        }
    }

    /**
     * Returns a proxy of the player that will time out any method called if it takes longer than
     * TIME_TO_RESPOND.
     * @param player    the player to create a proxy of.
     * @return          an IPlayer representing the given player with the ability to throw an UncheckedTimeoutException.
     */
    private IPlayer makePlayerWithTimeout(IPlayer player) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        TimeLimiter timeLimiter = SimpleTimeLimiter.create(es);
        return timeLimiter.newProxy(player, IPlayer.class, TIME_TO_RESPOND, TimeUnit.SECONDS);
    }

    private void notifyObserversOfStateChange() {
        notifyObservers((observer -> observer.informNewState(new GameState(this.gameState))));
    }

    private void notifyObserversOfGameOver() {
        notifyObservers((IObserver::gameOver));
    }

    private void notifyObservers(Consumer<IObserver> observerFunction) {
        for (IObserver observer : observers) {
            observerFunction.accept(observer);
        }
    }
}
