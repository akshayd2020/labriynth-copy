import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Represents a game state that has not been initialized with IPlayers yet.
 * Contains the same information as a GameState, except the stored PlayerStates
 * do not contain their actual IPlayer.
 * Used exclusively for testing Server by passing in a preexisting game state.
 */
public class GameStateWithoutIPlayers {
    private Tile spare;
    private Board board;
    private Queue<PlayerStateWithoutIPlayer> playerOrder;
    private Optional<SlidingAction> lastAction;
    private Queue<Position> goalsToDistribute;

    public GameStateWithoutIPlayers(Tile spare, Board board, Queue<PlayerStateWithoutIPlayer> playerOrder,
                                    Optional<SlidingAction> lastAction) {

        this(spare, board, playerOrder, lastAction, new LinkedList<>());
    }

    public GameStateWithoutIPlayers(Tile spare, Board board, Queue<PlayerStateWithoutIPlayer> playerOrder,
                                    Optional<SlidingAction> lastAction, Queue<Position> goalsToDistribute) {

        this.spare = spare;
        this.board = board;
        this.playerOrder = playerOrder;
        this.lastAction = lastAction;
        this.goalsToDistribute = goalsToDistribute;
    }

    /**
     * Returns a GameState where each IPlayer in the given list of players is put
     * into their respective PlayerState.
     * CONSTRAINT: The size of the given list of players must be the same as the size of
     * the stored queue of PlayerStates.
     * @param players   the list of IPlayers to add to their respective PlayerStates.
     * @return          the GameState containing the state information stored in this class
     *                  as well as the list of IPlayers added to their PlayerStates.
     */
    public GameState createGameStateWithIPlayers(List<IPlayer> players) {
        return new GameState(spare, board, createPlayerStateQueueWithIPlayers(players), lastAction, goalsToDistribute);
    }

    private Queue<PlayerState> createPlayerStateQueueWithIPlayers(List<IPlayer> players) {
        Queue<PlayerState> completePlayerStates = new LinkedList<>();
        for (PlayerStateWithoutIPlayer playerState : playerOrder) {
            IPlayer player = players.remove(0);
            completePlayerStates.add(playerState.createPlayerStateWithIPlayer(player));
        }
        return completePlayerStates;
    }
}
