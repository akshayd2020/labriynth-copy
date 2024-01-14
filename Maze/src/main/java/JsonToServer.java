/**
 * Used for the Milestone 9 runnable task, encapsulating the functionality for xserver.
 */
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Reads in JSON objects from input stream representing the given:
 * - the state of the game
 * and prints the results of the game in an array with the first index being
 * - the computed winners in alphabetical order
 * - the computed misbehaved players in alphabetical order
 * to the output stream
 */
public class JsonToServer implements IJsonToX {
    InputStreamReader inputStreamReader;
    PrintStream printStream;
    int portNum;

    public JsonToServer(InputStream inputStream, OutputStream outputStream, int portNum) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
        this.portNum = portNum;
    }

    /**
     * Reads in JSON objects from the input stream,
     * computes the output, and prints the output to the output stream
     */
    public void parseAndPrint() {
        try {
            List<String>[] sortedWinnersAndMisbehaved = parseInput();
            this.printStream.println(convertNamesToJson2DArray(sortedWinnersAndMisbehaved));
        } catch (UnsupportedEncodingException e) {
            this.printStream.println("Input cannot be read");
        }
    }

    /**
     * Parse the input and return the sorted list of winners
     *
     * @return sorted list of winners
     */
    public List<String>[] parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        JsonElement refStateObj = jsonReader.next();

        return convertInputAndComputeWinnersAndMisbehaved(refStateObj);
    }

    /**
     * Convert JSON objects into a game state without IPlayers and compute the sorted list of winners
     *
     * @param refStateObj referee state as a JSON object
     * @return sorted list of players
     */
    private List<String>[] convertInputAndComputeWinnersAndMisbehaved(JsonElement refStateObj) {
        GameStateWithoutIPlayers gameState;
        if (refStateObj.getAsJsonObject().has("goals")) {
            gameState =  GsonSingleton.getInstance().fromJson(refStateObj, MappedRefGS2.class).getGameStateWithoutIPlayers();
        } else {
            gameState =  GsonSingleton.getInstance().fromJson(refStateObj, MappedRefGS.class).getGameStateWithoutIPlayers();
        }
        return runServerAndGetWinnersAndMisbehaved(gameState);
    }

    /**
     * Runs the game given a game state to pass to the Server.
     *
     * @param gameState input referee state
     * @return sorted list of players
     */
    private List<String>[] runServerAndGetWinnersAndMisbehaved(GameStateWithoutIPlayers gameState) {
        Server server = new Server(portNum, gameState);
        GameResult winnersAndMisbehaved = server.runServer();
        return getSortedWinnersAndMisbehaved(winnersAndMisbehaved);
    }

    private List<String>[] getSortedWinnersAndMisbehaved(GameResult winnersAndMisbehaved) {
        Set<IPlayer> winners = winnersAndMisbehaved.getWinners();
        List<String> sortedWinners = getSortedPlayers(winners);
        Set<IPlayer> misbehaved = winnersAndMisbehaved.getMisbehaved();
        List<String> sortedMisbehaved = getSortedPlayers(misbehaved);
        List<String>[] sortedWinnersAndMisbehaved = (ArrayList<String>[]) new ArrayList[2];
        sortedWinnersAndMisbehaved[0] = sortedWinners;
        sortedWinnersAndMisbehaved[1] = sortedMisbehaved;
        return sortedWinnersAndMisbehaved;
    }

    /**
     * Sort the set of players into a sorted list
     *
     * @param players set of players represented by colors
     * @return sorted list of players
     */
    private List<String> getSortedPlayers(Set<IPlayer> players) {
        List<String> playerNames = getPlayerNames(players);
        Collections.sort(playerNames);
        return playerNames;
    }

    /**
     * Get the list of player names that won
     *
     * @param players players of this game represented by colors
     * @return list of player names
     */
    private List<String> getPlayerNames(Set<IPlayer> players) {
        List<String> ans = new ArrayList<>();
        for (IPlayer player : players) {
            ans.add(player.getName());
        }
        return ans;
    }

    /**
     * Given the names of the winners and misbehaved players, returns a JsonArray of
     * JsonArrays of the winner names and misbehaved names respectively.
     *
     * @param winnersAndMisbehaved the lists of winners and misbehaved.
     * @return JsonArray of winners and misbehaved.
     */
    private JsonArray convertNamesToJson2DArray(List<String>[] winnersAndMisbehaved) {
        JsonArray winnerJsonArray = convertNamesToJsonArray(winnersAndMisbehaved[0]);
        JsonArray misbehavedJsonArray = convertNamesToJsonArray(winnersAndMisbehaved[1]);
        JsonArray winnersAndMisbehavedArray = new JsonArray();
        winnersAndMisbehavedArray.add(winnerJsonArray);
        winnersAndMisbehavedArray.add(misbehavedJsonArray);
        return winnersAndMisbehavedArray;
    }

    private JsonArray convertNamesToJsonArray(List<String> names) {
        JsonArray res = new JsonArray();
        for (String name : names) {
            res.add(name);
        }
        return res;
    }
}

