import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.util.Pair;

import java.awt.Color;

/**
 * The mapped game state as a data transfer object representing the JSON object representing a game state
 */
public class MappedGS extends AbstractMappedGS {
    private MappedPlayer[] plmt;

    public MappedGS(MappedBoard board, MappedTile spare, MappedPlayer[] plmt, JsonElement last) {
        super(board, spare, last);
        this.plmt = plmt;
    }

    public MappedPlayer[] getPlmt() {
        return plmt;
    }

    public Queue<PlayerState> getPlayerOrder() throws IllegalArgumentException {
        Queue<PlayerState> playerOrder = new LinkedList<>();
        for (MappedPlayer p: plmt) {
            playerOrder.add(p.getPlayerState());
        }
        return playerOrder;
    }

    /**
     * Convert the given game state to a State as a data transfer object
     * @param gs    given game state
     * @return      state of the game as a data transfer object
     */
    public static MappedGS gameStateToMappedGS(LimitedGS gs) {
        return new MappedGS(MappedBoard.boardToMappedBoard(gs.getBoard()),
                MappedTile.tileToMappedTile(gs.getSpare()),
                MappedPlayer.playerStatesToPlayers(gs.getPlayerOrder()),
                lastActionToJsonArray(gs.getLastAction()));
    }

    public static JsonElement convertLimitedGSToJson(LimitedGS limitedGS) {
        return GsonSingleton.getInstance().toJsonTree(MappedGS.gameStateToMappedGS(limitedGS), MappedGS.class);
    }

    public static LimitedGS convertJsonElementToLimitedGS(JsonElement element) {
        return GsonSingleton.getInstance().fromJson(element, MappedGS.class).getGameState().getLimitedGS();
    }

    public GameState getGameState() {
        if (this.getLastAction().isPresent()) {
            return new GameState(this.getSpare(), this.getBoard(), this.getPlayerOrder(), this.getLastAction());
        } else {
            return new GameState(this.getSpare(), this.getBoard(), this.getPlayerOrder());
        }
    }
}

