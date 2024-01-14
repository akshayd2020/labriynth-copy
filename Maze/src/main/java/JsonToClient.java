import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Reads in JSON objects from input stream representing the given:
 * - list of players, their strategies, and which phase they may misbehave
 * and connect these players to a server that is open on the specified port.
 */
public class JsonToClient implements IJsonToX {
    InputStreamReader inputStreamReader;
    PrintStream printStream;
    int portNum;
    String ip;

    public JsonToClient(InputStream inputStream, int portNum, Optional<String> optionalIp) throws UnsupportedEncodingException {
        this.inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        this.portNum = portNum;
        if (optionalIp.isPresent()) {
            this.ip = optionalIp.get();
        }
        else {
            this.ip = "127.0.0.1";
        }
    }

    /**
     * Reads in JSON objects from the input stream and connects the created clients to the server.
     */
    public void parseAndConnect() {
        try {
            parseInput();
        } catch (UnsupportedEncodingException e) {
            this.printStream.println("Input cannot be read");
        }
    }

    /**
     * Parse the input and connect the created clients to the server.
     */
    public void parseInput() throws UnsupportedEncodingException {
        JsonStreamParser jsonReader = new JsonStreamParser(this.inputStreamReader);
        JsonArray playerSpec = jsonReader.next().getAsJsonArray();
        convertInputAndConnect(playerSpec);
    }

    /**
     * Convert JSON objects into a list of players and connects them to a server.
     * @param playerSpec    players in a JSON array
     */
    private void convertInputAndConnect(JsonArray playerSpec) {
        List<IPlayer> players = convertJsonArrayToPlayers(playerSpec);
        runClientsWithPlayers(players);
    }

    /**
     * Starts up client threads for each player in the given list and connects them to a server.
     * @param players   input list of players
     */
    private void runClientsWithPlayers(List<IPlayer> players) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(players.size());
        for (IPlayer player : players) {
            Client playerClient = new Client();
            executor.submit(() -> playerClient.connectToServerAndCreatePlayer(ip, portNum, player));
            while (!playerClient.isSuccessfullyConnected()) {
                waitSpecifiedAmountOfMS(3000);
            }
        }
    }

    private void waitSpecifiedAmountOfMS(int ms) {
        synchronized (this) {
            try {
                this.wait(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
        if (isBadPSFormat(ps)) {
            return getNewPlayerFailsBadFM(ps);
        }
        else if (isBadPS2Format(ps)) {
            return getNewPlayerLoops(ps);
        }
        return new Player(name, convertToStrategy(strategy));
    }

    private IPlayer getNewPlayerLoops(JsonArray ps) {
        String name = ps.get(0).getAsString();
        IStrategy strategy = convertToStrategy(ps.get(1).getAsString());
        String badFM = ps.get(2).getAsString();
        int count = ps.get(3).getAsInt();
        switch(badFM) {
            case "setUp":
                return new PlayerLoopsSetUp(name, strategy, count);
            case "takeTurn":
                return new PlayerLoopsTakeTurn(name, strategy, count);
            case "win":
                return new PlayerLoopsWon(name, strategy, count);
            default:
                throw new IllegalArgumentException("Should never reach this case because PlayerLoop can only be above cases");
        }
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
