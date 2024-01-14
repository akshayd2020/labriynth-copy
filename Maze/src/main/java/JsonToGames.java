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
 * - list of players and their strategies
 * - the state of the referee
 * and prints the computed winners in alphabetical order to the output stream
 */
public class JsonToGames implements IJsonToX {
    InputStreamReader inputStreamReader;
    PrintStream printStream;
    boolean isObserverOn = false;

    public JsonToGames(InputStream inputStream, OutputStream outputStream) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(System.in, "UTF-8");
        this.printStream = new PrintStream(outputStream, true, "UTF-8");
    }

    /**
     * Reads in JSON objects from the input stream,
     * computes the output, and prints the output to the output stream
     * @param isObserverOn  whether the observer is on
     */
    public void parseAndPrint(boolean isObserverOn) {
        try {
            this.isObserverOn = isObserverOn;
            List<String> sortedWinners = parseInput();
            this.printStream.println(convertNamesToJsonArray(sortedWinners));
        } catch (UnsupportedEncodingException e) {
            this.printStream.println("Input cannot be read");
        }
    }

    /**
     * Parse the input and return the sorted list of winners
     * @return  sorted list of winners
     */
    public List<String> parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        JsonArray playerSpec = jsonReader.next().getAsJsonArray();
        JsonElement refStateObj = jsonReader.next();

        return convertInputAndComputeWinners(playerSpec, refStateObj);
    }

    /**
     * Convert JSON objects into a list of players and a referee state and compute the sorted list of winners
     * @param playerSpec    players in a JSON array
     * @param refStateObj   referee state as a JSON object
     * @return  sorted list of players
     */
    private List<String> convertInputAndComputeWinners(JsonArray playerSpec, JsonElement refStateObj) {
        List<IPlayer> players = convertJsonArrayToPlayers(playerSpec);
        GameState gameState = GsonSingleton.getInstance().fromJson(refStateObj, MappedRefGS.class).getGameState(players);
        return runGameAndGetWinners(gameState);
    }

    /**
     * Runs the game given a list of players and the referee state
     * @param gameState input referee state
     * @return          sorted list of players
     */
    private List<String> runGameAndGetWinners(GameState gameState) {
        Referee referee;
        if (this.isObserverOn) {
            referee = new Referee(new Observer(gameState));
        } else {
            referee = new Referee();
        }
        GameResult winnersAndMisbehaved = referee.setupAndRunGame(gameState, 0);
        Set<IPlayer> winners = winnersAndMisbehaved.getWinners();
        return getSortedWinners(winners, gameState);
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
    private IPlayer convertToPlayer(JsonArray ps) {
        return new Player(ps.get(0).getAsString(), convertToStrategy(ps.get(1).getAsString()));
    }

    /**
     * Sort the set of winners into a sorted list
     * @param winners   set of winners represented by colors
     * @param gameState game state
     * @return          sorted list of winners
     */
    private List<String> getSortedWinners(Set<IPlayer> winners, GameState gameState) {
        List<String> winnerNames = getWinnerNames(winners, gameState);
        Collections.sort(winnerNames);
        return winnerNames;
    }

    /**
     * Get the list of player names that won
     * @param winners   winners of this game represented by colors
     * @param gameState the game state
     * @return          list of player names
     */
    private List<String> getWinnerNames(Set<IPlayer> winners, GameState gameState) {
        List<String> ans = new ArrayList<>();
        for (IPlayer player : winners) {
            ans.add(player.getName());
        }
        return ans;
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
    private IStrategy convertToStrategy(String strategy) {
        if (strategy.equals("Riemann")) {
            return new RiemannStrategy();
        } else {
            return new EuclidStrategy();
        }
    }
}