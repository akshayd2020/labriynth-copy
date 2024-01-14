import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import javafx.util.Pair;

/**
 * A referee game state where the "plmt" field contains an array of MappedRefPlayers
 */
public class MappedRefGS extends AbstractMappedGS {

    private MappedRefPlayer[] plmt;

    public MappedRefGS(MappedBoard board, MappedTile spare, MappedRefPlayer[] plmt, JsonElement last) {
        super(board, spare, last);
        this.plmt = plmt;
    }

    public MappedRefPlayer[] getPlmt() {
        return plmt;
    }

    public Queue<PlayerState> getPlayerOrder(List<IPlayer> sortedPlayers) throws IllegalArgumentException {
        Queue<PlayerState> playerOrder = new LinkedList<>();
        for (int index = 0; index < plmt.length; index++) {
            IPlayer player = sortedPlayers.get(index);
            MappedRefPlayer p = plmt[index];
            playerOrder.add(p.getPlayerState(player));
        }
        return playerOrder;
    }

    public Queue<PlayerStateWithoutIPlayer> getPlayerOrderWithoutIPlayers() throws IllegalArgumentException {
        Queue<PlayerStateWithoutIPlayer> playerOrder = new LinkedList<>();
        for (int index = 0; index < plmt.length; index++) {
            MappedRefPlayer p = plmt[index];
            playerOrder.add(p.getPlayerStateWithoutIPlayer());
        }
        return playerOrder;
    }

    /**
     * Convert the given game state to a referee state
     * @param gs    given game state
     * @return      referee state
     */
    public static MappedRefGS gameStateToMappedRefGS(GameState gs) {
        return new MappedRefGS(MappedBoard.boardToMappedBoard(gs.getBoard()),
                MappedTile.tileToMappedTile(gs.getSpare()),
                MappedRefPlayer.playerStatesToRefPlayers(gs.getPlayerOrder()),
                lastActionToJsonArray(gs.getLastAction()));
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof MappedRefGS)) {
            return false;
        }

        MappedRefGS toCompare = (MappedRefGS) o;
        return this.board.equals(MappedBoard.boardToMappedBoard(toCompare.getBoard()))
                && this.spare.equals(MappedTile.tileToMappedTile(toCompare.getSpare()))
                && Arrays.deepEquals(this.plmt, toCompare.getPlmt())
                && this.last.equals(toCompare.getLast());
    }

    @Override
    public int hashCode() {
        return (this.board.toString() + "," +  this.spare.toString() + "," + this.plmt.toString() + this.last.toString()).hashCode();
    }

    public GameState getGameState(List<IPlayer> players) {
        return new GameState(this.getSpare(), this.getBoard(), this.getPlayerOrder(players), this.getLastAction());
    }

    public GameStateWithoutIPlayers getGameStateWithoutIPlayers() {
        return new GameStateWithoutIPlayers(this.getSpare(), this.getBoard(),
                this.getPlayerOrderWithoutIPlayers(), this.getLastAction());
    }
}

