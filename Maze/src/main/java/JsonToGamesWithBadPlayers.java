import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.awt.Color;
/**
 * Reads in JSON objects from input stream representing the given:
 * - list of players, their strategies, and which phase they may misbehave
 * - the state of the game
 * and prints the results of the game in an array with the first index being
 * - the computed winners in alphabetical order
 * - the computed misbehaved players in alphabetical order
 * to the output stream
 */
public class JsonToGamesWithBadPlayers implements IJsonToX {
    InputStreamReader inputStreamReader;
    PrintStream printStream;

    public JsonToGamesWithBadPlayers(InputStream inputStream, OutputStream outputStream) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
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
     * @return  sorted list of winners
     */
    public List<String>[] parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        JsonArray playerSpec = jsonReader.next().getAsJsonArray();
        JsonElement refStateObj = jsonReader.next();

        return convertInputAndComputeWinnersAndMisbehaved(playerSpec, refStateObj);
    }

    /**
     * Convert JSON objects into a list of players and a referee state and compute the sorted list of winners
     * @param playerSpec    players in a JSON array
     * @param refStateObj   referee state as a JSON object
     * @return  sorted list of players
     */
    private List<String>[] convertInputAndComputeWinnersAndMisbehaved(JsonArray playerSpec, JsonElement refStateObj) {
        List<IPlayer> players = convertJsonArrayToPlayers(playerSpec);
        GameState gameState = GsonSingleton.getInstance().fromJson(refStateObj, MappedRefGS.class).getGameState(players);
        return runGameAndGetWinnersAndMisbehaved(players, gameState);
    }

    /**
     * Runs the game given a list of players and the referee state
     * @param players   input list of players
     * @param gameState input referee state
     * @return          sorted list of players
     */
    private List<String>[] runGameAndGetWinnersAndMisbehaved(List<IPlayer> players, GameState gameState) {
        Referee referee = new Referee();
        GameResult winnersAndMisbehaved = referee.setupAndRunGame(gameState, 0);
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
     * Convert JSON array into a list of players
     * @param playerSpec    players in a JSON array
     * @return              list of players
     */
    private List<IPlayer> convertJsonArrayToPlayers(JsonArray playerSpec) {
        List<IPlayer> res = new ArrayList<>();
        for (JsonElement p : playerSpec) {
            IPlayer player = convertToPlayer(p.getAsJsonArray());
            res.add(player);
        }
        return res;
    }

    /**
     * Convert the JSON array representation of a referee player into a player
     * @param ps    single player representation
     * @return      player
     */
    protected IPlayer convertToPlayer(JsonArray ps) {
        String name = ps.get(0).getAsString();
        String strategy = ps.get(1).getAsString();
        if (ps.size() > 2) {
            return getNewPlayerFailsBadFM(ps);
        }
        return new Player(name, convertToStrategy(strategy));
    }

    protected IPlayer getNewPlayerFailsBadFM(JsonArray ps) {
        String name = ps.get(0).getAsString();
        IStrategy strategy = convertToStrategy(ps.get(1).getAsString());
        String badFM = ps.get(2).getAsString();
        switch(badFM) {
            case "setUp":
                return new PlayerFailsSetUp(name, strategy);
            case "takeTurn":
                return new PlayerFailsTakeTurn(name, strategy);
            case "win":
                return new PlayerFailsWon(name, strategy);
            default:
                throw new IllegalArgumentException("Should never reach this case because BadFM can only be above cases");
        }
    }

    /**
     * Sort the set of players into a sorted list
     * @param players   set of players represented by colors
     * @return          sorted list of players
     */
    private List<String> getSortedPlayers(Set<IPlayer> players) {
        List<String> playerNames = getPlayerNames(players);
        Collections.sort(playerNames);
        return playerNames;
    }

    /**
     * Get the list of player names that won
     * @param players   players of this game represented by colors
     * @return          list of player names
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
     * @param winnersAndMisbehaved  the lists of winners and misbehaved.
     * @return                      JsonArray of winners and misbehaved.
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

    /**
     * Get the strategy from the given string
     * @param strategy  the strategy represented as a string
     * @return  the strategy object
     */
    protected IStrategy convertToStrategy(String strategy) {
        if (strategy.equals("Riemann")) {
            return new RiemannStrategy();
        } else {
            return new EuclidStrategy();
        }
    }

    /**
     * Determines if the given JsonArray is a BadPS. A BadPS will have
     * exactly 3 elements in the array.
     * @param ps    the PS to check.
     * @return      true if the ps is a BadPS, false otherwise.
     */
    protected boolean isBadPSFormat(JsonArray ps) {
        return ps.size() == 3;
    }

    /**
     * Determines if the given JsonArray is a BadPS2. A BadPS will have
     * exactly 4 elements in the array.
     * @param ps    the PS to check.
     * @return      true if the ps is a BadPS2, false otherwise.
     */
    protected boolean isBadPS2Format(JsonArray ps) {
        return ps.size() == 4;
    }
}
