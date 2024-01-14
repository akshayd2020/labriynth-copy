import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStreamReader;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.Set;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.Color;

import javafx.util.Pair;

/**
 * Reads in JSON objects from STDIN input representing the Labyrinth board and a starting tile
 * and prints a JSON array of reachable tiles to STDOUT
 */
public class JsonToGameState {
    InputStreamReader inputStreamReader;
    PrintStream printStream;

    public JsonToGameState(InputStream inputStream, OutputStream outputStream) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
    }

    public void parseAndPrint() {
        try {
            List<Position> positionsReachable =
                            parseInput();
            this.printStream.println((convertPositionsToJsonArray(positionsReachable)));
        } catch (UnsupportedEncodingException e) {
            this.printStream.println("Input cannot be read");
        }
    }

    /**
     * Returns a list of reachable positions from parsed input
     * @return list of reachable positions
     * @throws UnsupportedEncodingException
     */
    public List<Position> parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        JsonElement stateObj = jsonReader.next();
        int index = jsonReader.next().getAsInt();
        Direction direction = Direction.getDirectionFromString(jsonReader.next().getAsString());
        int degree = jsonReader.next().getAsInt();

        GameState gameState = convertJsonToGameState(stateObj);
        return getReachablePositionsAfterSlideAndInsert(index, direction, degree, gameState);
    }

    private List<Position> getReachablePositionsAfterSlideAndInsert(int index, Direction direction, int degree, GameState gameState) {
        GameState afterGS = gameState.performAction(new RotateTile(degree, gameState));
        afterGS = afterGS.performAction(new SlideAndInsert(index, direction, gameState));
        Position startPosition = afterGS.getActivePlayer().getCurrentPosition();
        Set<Position> reachable = afterGS.getBoard().getReachableTiles(startPosition.getRow(),
                startPosition.getColumn());
        return sortReachable(reachable);
    }

    /**
     * Convert the given json object to a game state object
     * @param json      json object to parse
     * @return
     */
    private GameState convertJsonToGameState(JsonElement json) {
        try {
            MappedGS mappedGS = GsonSingleton.getInstance().fromJson(json, MappedGS.class);
            Tile spare = mappedGS.getSpare();
            Board board = mappedGS.getBoard();
            Queue<PlayerState> playerOrder = mappedGS.getPlayerOrder();
            if (mappedGS.getLastAction().isPresent()) {
                return new GameState(spare, board, playerOrder, mappedGS.getLastAction());
            } else {
                return new GameState(spare, board, playerOrder);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Input was invalid: " + e.getMessage());
        }
    }

    private List<Position> sortReachable(Set<Position> reachable) {
        List<Position> reachableList = new ArrayList<>(reachable);
        Collections.sort(reachableList, new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                if (p1.getRow() != p2.getRow()) {
                    return p1.getRow()-p2.getRow();
                }

                return p1.getColumn()-p2.getColumn();
            }
        });
        return reachableList;
    }

    private JsonObject convertPosToJson(Position p) {
        JsonObject result = new JsonObject();
        result.addProperty("row#", p.getRow());
        result.addProperty("column#", p.getColumn());
        return result;
    }

    private JsonArray convertPositionsToJsonArray(List<Position> positions) {
        JsonArray result = new JsonArray();
        for(Position p : positions) {
            result.add(convertPosToJson(p));
        }
        return result;
    }
}