import java.awt.Color;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashSet;
import java.util.Random;
import java.util.Queue;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * The referee's game state
 */
public class GameState {
    private final Tile spare;
    private final Board board;
    private final Queue<PlayerState> playerOrder;
    private final Optional<SlidingAction> lastAction;
    private final Queue<Position> goalsToDistribute;


    // constructor that sets lastAction to empty
    public GameState(Tile spare, Board board, Queue<PlayerState> playerOrder) {
        this(spare, board, playerOrder, Optional.empty(), new LinkedList<>());
    }

    // constructor with last action
    public GameState(Tile spare, Board board, Queue<PlayerState> playerOrder, Optional<SlidingAction> lastAction) {
        this(spare, board, playerOrder, lastAction, new LinkedList<>());
    }

    public GameState(Tile spare, Board board, Queue<PlayerState> playerOrder,
                     Optional<SlidingAction> lastAction, Queue<Position> goalsToDistribute) {
        if (board.getAllGemPairs().contains(spare.getGems())) {
            throw new IllegalArgumentException("Board contains the spare's pair of gems");
        }
        checkValidPlayers(playerOrder);
        checkValidPlayerSizeForBoardSize(board, playerOrder);
        this.spare = spare;
        this.board = board;
        this.playerOrder = playerOrder;
        this.lastAction = lastAction;
        this.goalsToDistribute = goalsToDistribute;
    }

    // Copy constructor
    public GameState(GameState other) {
        checkValidPlayers(other.getPlayerOrder());
        checkValidPlayerSizeForBoardSize(other.getBoard(), other.getPlayerOrder());
        this.board = new Board(other.getBoard().getGridDeepCopy());
        this.spare = other.getSpare().getDeepCopy();
        this.playerOrder = other.getPlayerOrderDeepCopy();
        this.lastAction = other.getLastAction();
        this.goalsToDistribute = other.getGoalsToDistributeDeepCopy();
    }

    private Queue<PlayerState> getPlayerOrderDeepCopy() {
        return getQueueDeepCopy(this.playerOrder, PlayerState::getDeepCopy);
    }

    private Queue<Position> getGoalsToDistributeDeepCopy() {
        return getQueueDeepCopy(this.goalsToDistribute, Position::getDeepCopy);
    }

    private <T> Queue<T> getQueueDeepCopy(Queue<T> queue, Function<T, T> deepCopyFunction) {
        Queue<T> deepCopyQueue = new LinkedList<>();
        for (T item : queue) {
            deepCopyQueue.add(deepCopyFunction.apply(item));
        }
        return deepCopyQueue;
    }

    public Queue<Position> getGoalsToDistribute() {
        return goalsToDistribute;
    }

    /**
     * Pops and returns the next distributed goal in the ordered sequence if it exists.
     * If there are no more goals to distribute, it will return Optional.empty().
     * @return  the next distributed goal if it exists, otherwise return empty.
     */
    public Optional<Position> getNextDistributedGoal() {
        return goalsToDistribute.isEmpty()
                ? Optional.empty()
                : Optional.of(goalsToDistribute.remove());
    }

    /**
     * Throws an IllegalArgumentException if the number of players exceeds the number of fixed tiles on the board
     * since each home must be on a distinct tile.
     * @param board         the board of this game state.
     * @param playerOrder   the players in this game state.
     */
    private void checkValidPlayerSizeForBoardSize(Board board, Queue<PlayerState> playerOrder) {
        if (playerOrder.size() > board.getNumFixedTiles()) {
            throw new IllegalArgumentException("The number of players cannot exceed the number of fixed tiles on the board.");
        }
    }

    /**
     * Throws an IllegalArgumentException if the current player states violate any of the following constraints:
     * - any the home positions of the players are not distinct
     * - any of the avatar colors are not distinct
     * @param playerOrder the order of the players with their game state information
     */
    private void checkValidPlayers(Queue<PlayerState> playerOrder) {
        checkDistinctHomes(playerOrder);
        checkDistinctAvatars(playerOrder);
    }

    /**
     * Throws an IllegalArgumentException if the given field of the players is not distinct
     * @param playerOrder the order of the players with their game state information
     */
    private <T> void checkDistinctField(Queue<PlayerState> playerOrder, Function<PlayerState, T> getField) {
        Set<T> seenSoFar = new HashSet<>();
        for (PlayerState playerState : playerOrder) {
            if (seenSoFar.contains(getField.apply(playerState))) {
                throw new IllegalArgumentException("One of the player state's fields that should " +
                        "be distinct is not distinct");
            }
            seenSoFar.add(getField.apply(playerState));
        }
    }

    /**
     * Throws an IllegalArgumentException if any the home positions of the players are not distinct
     * @param playerOrder the order of the players with their game state information
     */
    private void checkDistinctHomes(Queue<PlayerState> playerOrder) {
        checkDistinctField(playerOrder, PlayerState::getHomePosition);
    }

    /**
     * Throws an IllegalArgumentException if any the avatars of the players are not distinct
     * @param playerOrder the order of the players with their game state information
     */
    private void checkDistinctAvatars(Queue<PlayerState> playerOrder) {
        checkDistinctField(playerOrder, PlayerState::getAvatar);
    }

    /**
     * Return the last action a player made. If no action was made yet, this function will return null
     * @return          last action
     */
    public Optional<SlidingAction> getLastAction() {
        return this.lastAction;
    }


    public Queue<PlayerState> getPlayerOrder() {
        return copyPlayerOrder();
    }

    private Queue<PlayerState> copyPlayerOrder() {
        Queue<PlayerState> copiedQueue = new LinkedList<>();
        for (PlayerState playerState : this.playerOrder) {
            copiedQueue.add(new PlayerState(playerState));
        }
        return copiedQueue;
    }

    public Tile getSpare() {
        return new Tile(this.spare.getGems(), this.spare.getConnector());
    }

    public Board getBoard() {
        return this.board;
    }

    /**
     * The currently active player performs an action
     * @param action    an action to perform
     */
    public GameState performAction(IAction action) {
        return action.perform();
    }

    /**
     * Can the currently active player reach the given position
     * @param targetPosition    the position to reach
     * @return                  whether or not the given position is reachable for the active player
     */
    public boolean canReach(Position targetPosition) {
        if (!this.board.isWithinRange(targetPosition)) {
            throw new IllegalArgumentException("Cannot reach a position that is out of bounds");
        }
        Position activePos = this.getActivePlayer().getCurrentPosition();
        return this.board.getReachableTiles(activePos.getRow(), activePos.getColumn()).contains(targetPosition);
    }

    /**
     * Did the currently active player reach their goal tile in this round?
     * @return              whether the currently active player reach their goal tile
     */
    public boolean isActivePlayerOnGoalTile() {
        return this.getActivePlayer().getCurrentPosition().equals(this.getActivePlayer().getGoalPosition());
    }

    /**
     * Get the goal position of the currently active player
     * @return  the goal position of the currently active player
     */
    public Position getGoalPosition() {
        return this.getActivePlayer().getGoalPosition();
    }

    /**
     * Get the home position of the currently active player
     * @return  the home position of the currently active player
     */
    public Position getHomePosition() {
        return this.getActivePlayer().getHomePosition();
    }

    /**
     * Did the currently active player reach their home tile?
     * @return              whether the currently active player reach their home tile
     */
    public boolean homeReached() {
        Position homePos = this.getActivePlayer().getHomePosition();
        Position currPos = this.getActivePlayer().getCurrentPosition();
        return currPos.equals(homePos);
    }

    /**
     * Did the currently active player win and should terminate this game?
     * This condition is true specifically if there are no more goals to distribute,the player
     * is currently being directed home, and they have landed on their home tile.
     * @return  whether the currently active player won.
     */
    public boolean didActivePlayerWin() {
        return homeReached() && this.isActivePlayerGoingHome() && this.goalsToDistribute.isEmpty();
    }

    /**
     * Did the currently active player reach their last intermediary goal already and is on their way home?
     * @return  whether the currently active player is on their way home.
     */
    public boolean isActivePlayerGoingHome() {
        return this.getActivePlayer().isPlayerGoingHome();
    }


    /**
     * Get the current active player
     * @return              the current active player
     */
    public PlayerState getActivePlayer() {
        if (this.playerOrder.size() <= 0) {
            throw new IllegalStateException("There are no more players in this game");
        }
        return this.playerOrder.peek();
    }

    public int getNumPlayers() {
        return this.playerOrder.size();
    }

    /**
     * Find and move all players that are shifted by a move
     * @param index     index of row/column to shift
     * @param dir       direction to move the row/column
     * @return          Map of player id to playerstate
     */
    public Queue<PlayerState> movePlayersOnShift(int index, Direction dir) {
        List<PlayerState> playersToShift = new ArrayList<>(this.playerOrder);
        Queue<PlayerState> playersAfterShift = new LinkedList<>();
        for (PlayerState playerState : playersToShift) {
            PlayerState playerAfterShift = movePlayer(playerState, dir, index);
            playersAfterShift.add(playerAfterShift);
        }
        return playersAfterShift;
    }

    /**
     * Move the given player in the given direction if at the given row/column
     * @param playerState      player to be shifted
     * @param dir              direction to move
     * @param index            index to shift
     * @return                 updated player's state
     */
    private PlayerState movePlayer(PlayerState playerState, Direction dir, int index) {
        if (!shouldPlayerMove(playerState, dir, index)) {
            return playerState;
        }
        safelyUpdateCurrentPosition(playerState, dir);
        return playerState;
    }

    /**
     * Safely updates the current position of this player's state. Ensures that the given player's new position
     * will be off the given board, their new position will be on the opposite side of the row/colum they are on
     * @param playerState   the player state to update their current position
     * @param direction     the direction to shift the given row or column index
     */
    private void safelyUpdateCurrentPosition(PlayerState playerState, Direction direction) {
        Position currentPosition = playerState.getCurrentPosition();
        Position newPosition = this.board.safelyUpdatesPosition(currentPosition, direction);
        playerState.setCurrentPosition(newPosition);
    }

    /**
     * Should the given player move?
     * @param playerState   the current player state
     * @param dir           the direction to move the given row/column index
     * @param index         the index of the row/column to move
     * @return              whether the player's state should be updated by a move
     */
    private boolean shouldPlayerMove(PlayerState playerState, Direction dir, int index) {
        boolean isRow = Direction.isHorizontalDirection(dir);
        return (isRow && playerState.getCurrentPosition().getRow() == index)
                || (!isRow && playerState.getCurrentPosition().getColumn() == index);
    }

    /**
     * Gives the active player a new intermediary goal from this game state's queue of
     * distributed goals.
     */
    public void setNewGoalForActivePlayer() {
        Optional<Position> nextGoal = getNextDistributedGoal();
        Position goalToGive = nextGoal.isEmpty() ? getHomePosition() : nextGoal.get();
        this.getActivePlayer().giveNewGoal(goalToGive);
    }

    /**
     * Kicks out the currently active player from this game
     */
    public GameState kickOutPlayer() {
        if (playerOrder.isEmpty()) {
            throw new IllegalStateException("Cannot kick out a player if there are no players");
        }
        playerOrder.remove();
        return new GameState(spare, board, playerOrder, lastAction, goalsToDistribute);
    }

    /**
     * Moves the currently active player to the end of the player queue. Reset the number of passes in round if this is a new round.
     * @return      the updated game state with the new active player
     */
    public GameState changeActivePlayer() {
        if (playerOrder.isEmpty()) {
            throw new IllegalStateException("Cannot change the active player if there are no players in this game");
        }

        PlayerState pastActive = playerOrder.remove();
        playerOrder.add(pastActive);

        return new GameState(spare, board, playerOrder, this.lastAction, goalsToDistribute);
    }

    /**
     * Gets the maximum possible square distance between two positions on the board.
     * @return  the maximum square distance on this board.
     */
    public int maximumSquareDistance() {
        Position topLeftPositionOnBoard = new Position(0, 0);
        Position bottomRightPositionOnBoard = new Position(this.board.getNumRows() - 1, this.board.getNumColumns() - 1);
        return Position.calculatedSquareDistance(topLeftPositionOnBoard, bottomRightPositionOnBoard);
    }

    /**
     * Generates a game state given the dimensions of the board and the number of players in a game.
     * CONSTRAINT: this method should only be called with dimensions that will result in a board
     * where the number of fixed tiles <= the number of players.
     * @param numRows       number of rows for the board
     * @param numColumns    number of columns for the board
     * @param playersSorted list of sorted players for this game
     * @return              the game state to play a game
     */
    public static GameState generateGameState(int numRows, int numColumns, List<IPlayer> playersSorted) {
        BoardAndSpare bs = BoardAndSpare.generateBoardAndSpare(numRows, numColumns);
        Board b = bs.getBoard();
        Tile s = bs.getSpare();
        List<Position> allPossibleGoals = b.getFixedPositions();
        List<Position> allPossibleHomes = b.getFixedPositions();
        Queue<PlayerState> playerOrder = generatePlayerOrder(playersSorted, allPossibleGoals, allPossibleHomes);
        return new GameState(s, b, playerOrder, Optional.empty(), new LinkedList<>());
    }

    /**
     * Generate a player order given a specified number of players
     * @param   playersSorted   the list of sorted players
     * @return                  the player order with random colors
     */
    private static Queue<PlayerState> generatePlayerOrder(List<IPlayer> playersSorted,
                                                          List<Position> allPossibleGoals,
                                                          List<Position> allPossibleHomes) {
        Queue<PlayerState> ans = new LinkedList<>();
        Set<Color> currentColors = new HashSet<>();

        for (int i = 0; i < playersSorted.size(); i += 1) {
            PlayerState playerState = generatePlayerState(currentColors, allPossibleGoals, allPossibleHomes, playersSorted.get(i));
            ans.add(playerState);
        }
        return ans;
    }

    /**
     * Generate a map of player avatars to their states
     * @param currentColors         the set of current colors that will be updated when this player's color gets chosen
     * @param allPossibleGoals      the set of available goal positions are available to be assigned to a player
     * @param allPossibleHomes      the set of available home positions are available to be assigned to a player
     * @param player                the player that corresponds to this player state
     * @return                      a map representing players to their states
     */
    private static PlayerState generatePlayerState(Set<Color> currentColors, List<Position> allPossibleGoals,
            List<Position> allPossibleHomes, IPlayer player) {
        Color randomColor = generateDistinctRandomColor(currentColors);
        Position home = removeRandomPosition(allPossibleHomes);
        Position goal = removeRandomPosition(allPossibleGoals);
        PlayerState playerState = new PlayerState(randomColor, home, goal, home, 0, player);
        return playerState;
    }

    /**
     * Removes a random position from the given list and returns it.
     * @param positions the list of positions to remove a single position randomly from
     * @return the random position removed from the given list
     */
    private static Position removeRandomPosition(List<Position> positions) {
        return positions.remove(new Random().nextInt(positions.size()));
    }

    /**
     * Generates a random color that is distinct from the given set of colors, updating that set once
     * a color is chosen.
     * @param currentColors     the set of current colors that the generated color should be distinct from.
     * @return                  the generated color.
     */
    private static Color generateDistinctRandomColor(Set<Color> currentColors) {
        Color randomColor = generateRandomColor();
        while (currentColors.contains(randomColor)) {
            randomColor = generateRandomColor();
        }
        currentColors.add(randomColor);
        return randomColor;
    }


    /**
     * Generates a random color
     * @return  a random color
     */
    private static Color generateRandomColor() {
        Random random = new Random();
        int colorLimit = 256;
        return new Color(random.nextInt(colorLimit), random.nextInt(colorLimit), random.nextInt(colorLimit));
    }

    /**
     * Get the limited game state that the current active player should see
     * @return      limited game state
     */
    public LimitedGS getLimitedGS() {
        return new LimitedGS(this.spare, this.board, this.getLimitedPS(), this.getLastAction());
    }

    private Queue<LimitedPlayerState> getLimitedPS() {
        List<PlayerState> playerStates = new ArrayList<>(this.playerOrder);
        Queue<LimitedPlayerState> limitedPS = new LinkedList<>();
        for (PlayerState ps: playerStates) {
            limitedPS.add(new LimitedPlayerState(ps.getAvatar(), ps.getHomePosition(), ps.getCurrentPosition()));
        }

        return limitedPS;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameState)) {
            return false;
        }
        GameState toCompare = (GameState) o;
        return this.spare.equals(toCompare.getSpare())
                && this.board.equals(toCompare.getBoard())
                && this.playerOrder.equals(toCompare.getPlayerOrder())
                && this.lastAction.equals(toCompare.getLastAction());

    }

    @Override
    public int hashCode() {
        return (this.spare.toString() + "," + this.board.toString()
            + "," + this.playerOrder.toString()).hashCode();
    }

    @Override
    public String toString() {
        return ("SPARE\n" + this.spare.toString() + "\nBOARD\n" + this.board.toString() + "playerOrder\n" +
                this.playerOrder.toString());
    }
}
