import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import javafx.util.Pair;

import java.awt.Color;

public abstract class AbstractMappedGS implements IMappedGS {
    MappedBoard board;
    MappedTile spare;
    JsonElement last;

    public AbstractMappedGS(MappedBoard board, MappedTile spare, JsonElement last) {
        this.board = board;
        this.spare = spare;
        this.last = Objects.requireNonNullElse(last, JsonNull.INSTANCE);
    }

    public JsonElement getLast() {
        return last;
    }

    public Tile getSpare() {
        return spare.getTile();
    }

    public Board getBoard() {
        return board.getBoard();
    }

    public Optional<SlidingAction> getLastAction() {
        if (last.isJsonNull()) {
            return Optional.empty();
        }
        JsonArray lastJsonArray = last.getAsJsonArray();
        int index = lastJsonArray.get(0).getAsInt();
        Direction direction = Direction.getDirectionFromString(lastJsonArray.get(1).getAsString());
        return Optional.of(new SlidingAction(index, direction));
    }

    protected static JsonElement lastActionToJsonArray(Optional<SlidingAction> lastAction) {
        if (lastAction.isEmpty()) {
            return JsonNull.INSTANCE;
        }
        JsonArray lastActionArr = new JsonArray();
        lastActionArr.add(lastAction.get().getIndex());
        lastActionArr.add(lastAction.get().getDirection().toString());
        return lastActionArr;
    }
}