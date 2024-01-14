import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.google.gson.*;

import java.util.Optional;
import java.util.Queue;
import java.util.Map;

import java.awt.Color;


/**
 * Reads in JSON objects from input stream representing the given:
 * - strategy designation
 * - state object
 * - goal coordinate of the active player
 * and prints the computed choice to the output stream
 */
public class JsonToChoice implements IJsonToX {
    InputStreamReader inputStreamReader;
    PrintStream printStream;

    public JsonToChoice(InputStream inputStream, OutputStream outputStream) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
    }

    /**
     * Reads in JSON objects from the input stream,
     * computes the output, and prints the output to the output stream
     */
    public void parseAndPrint() {
        try {
            Optional<PlayInfo> playInfo = parseInput();
            if (playInfo.isEmpty()) {
                this.printStream.println(new JsonPrimitive("PASS"));
            } else {
                this.printStream.println((convertPlayInfoToJsonArray(playInfo.get())));
            }
        } catch (UnsupportedEncodingException e) {
            this.printStream.println("Input cannot be read");
        }
    }

    /**
     * Parse the input and return play information if it exists
     * @return  play information if it exists
     */
    public Optional<PlayInfo> parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        String strategyStr = jsonReader.next().getAsString();
        JsonElement stateObj = jsonReader.next();
        JsonElement goalPosObj = jsonReader.next();

        return getPlayInfoFromInput(strategyStr, stateObj, goalPosObj);
    }

    /**
     * Convert JSON objects into a strategy and a goal position and compute the play info if it exists
     * @param strategyStr   strategy string
     * @param stateObj      state JSON object
     * @param goalPosObj   position JSON object
     * @return  play info if it exists
     */
    private Optional<PlayInfo> getPlayInfoFromInput(String strategyStr, JsonElement stateObj, JsonElement goalPosObj) {
        GameState gameState = convertJsonToGameState(stateObj);
        Position goalPos = GsonSingleton.getInstance().fromJson(goalPosObj, MappedCoordinate.class).getPosition();
        return strategyComputePlayInfo(strategyStr, gameState, goalPos);
    }

    /**
     * Convert the play info into a JSON array
     * @param playInfo  play information
     * @return  a JSON array representing play information
     */
    private JsonArray convertPlayInfoToJsonArray(PlayInfo playInfo) {
        JsonArray result = new JsonArray();
        result.add(playInfo.getIndex());
        result.add(playInfo.getDirection().toString());
        result.add(playInfo.getDegreeToRotate());
        result.add(convertPosToJson(playInfo.getNewPosition()));
        return result;
    }

    /**
     * Convert the given JSON object to a game state object
     * @param json      json object to parse
     * @return  game state represented by the JSON object
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

    private Position convertToPosition(JsonObject obj) {
        return new Position(obj.get("row#").getAsInt(), obj.get("column#").getAsInt());
    }

    /**
     * Convert a position to JSON object
     * @param p position
     * @return  JSON object representing position
     */
    private JsonObject convertPosToJson(Position p) {
        JsonObject result = new JsonObject();
        result.addProperty("row#", p.getRow());
        result.addProperty("column#", p.getColumn());
        return result;
    }

    /**
     * Returns the play information computed from the strategy specified by the given input string
     * @param strategy  the strategy represented as a string
     * @param gameState the state of this game
     * @param goal      the goal position
     * @return  the strategy object
     */
    private Optional<PlayInfo> strategyComputePlayInfo(String strategy, GameState gameState, Position goal) {
        Position currentPos = gameState.getActivePlayer().getCurrentPosition();
        if (strategy.equals("Riemann")) {
            return new RiemannStrategy().computePlayInfo(currentPos, goal, gameState.getBoard(), gameState.getSpare(),
                    gameState.getLastAction());
        } else {
            return new EuclidStrategy().computePlayInfo(currentPos, goal, gameState.getBoard(), gameState.getSpare(),
                    gameState.getLastAction());
        }
    }
}
