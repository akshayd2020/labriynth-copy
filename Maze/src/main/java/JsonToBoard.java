import org.jgrapht.alg.util.UnorderedPair;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.InputStreamReader;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Reads in JSON objects from STDIN input representing the Labyrinth board and a starting tile
 * and prints a JSON array of reachable tiles to STDOUT
 */
public class JsonToBoard {
    InputStreamReader inputStreamReader;
    PrintStream printStream;

    public JsonToBoard(InputStream inputStream, OutputStream outputStream) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
    }

    public void parseAndPrint() {
        try {
            List<Position> positionsReachable = parseInput();
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
        JsonObject boardObj = jsonReader.next().getAsJsonObject();
        JsonObject startPosObj = jsonReader.next().getAsJsonObject();

        Tile[][] grid = convertToGrid(boardObj);
        Position startPosition = convertToPosition(startPosObj);
        Set<Position> reachable = new Board(grid).getReachableTiles(startPosition.getRow(),
            startPosition.getColumn());
        return sortReachable(reachable);
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

    private JsonArray convertPositionsToJsonArray(List<Position> positions) {
        JsonArray result = new JsonArray();
        for(Position p : positions) {
            result.add(convertPosToJson(p));
        }
        return result;
    }

    private JsonObject convertPosToJson(Position p) {
        JsonObject result = new JsonObject();
        result.addProperty("row#", p.getRow());
        result.addProperty("column#", p.getColumn());
        return result;
    }

    /**
     * Get the grid of the game from the JSON object representing the board
     * @param obj   the json object to convert
     * @return      a grid of tiles
     */
    private Tile[][] convertToGrid(JsonObject obj) {
        JsonArray connectors = obj.get("connectors").getAsJsonArray();
        JsonArray treasures = obj.get("treasures").getAsJsonArray();

        return createGrid(connectors, treasures);
    }

    private Tile[][] createGrid(JsonArray connectors, JsonArray treasures) {
        int numRows = connectors.size();
        int numCols = (connectors.size() > 0) ? connectors.get(0).getAsJsonArray().size() : 0;

        Tile[][] result = new Tile[numRows][numCols];
        for (int row = 0; row < numRows; row += 1) {
            for (int col = 0; col < numCols; col += 1) {
                JsonArray currTreasure =
                    treasures.get(row).getAsJsonArray().get(col).getAsJsonArray();
                String currConnector = connectors.get(row).getAsJsonArray().get(col).getAsString();
                result[row][col] = new Tile(getTreasure(currTreasure), getConnector(currConnector));
            }
        }
        return result;
    }

    private Position convertToPosition(JsonObject obj) {
        return new Position(obj.get("row#").getAsInt(), obj.get("column#").getAsInt());
    }

    private UnorderedPair<Gem, Gem> getTreasure(JsonArray arr) {
        Gem gem1 = Gem.getGem(arr.get(0).getAsString());
        Gem gem2 = Gem.getGem(arr.get(1).getAsString());
        return new UnorderedPair(gem1, gem2);
    }

    private Connector getConnector(String str) {
        char unicode = str.charAt(0);
        return Connector.getCharToConnector().get(unicode);
    }


}