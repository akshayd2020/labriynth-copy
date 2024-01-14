import com.google.gson.JsonElement;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Handle server-client sign-ups from remote clients, creating remote proxy players and sending them to the referee.
 * Once the referee finishes the game, this server will receive the game results and print them.
 */
public class Server {
    private final Referee referee;
    // The length of the sign-up period in seconds.
    // The number of sign up periods before starting the game
    public static final int NUMBER_OF_SIGN_UP_PERIODS = 2;
    public static final int SIGN_UP_PERIOD_DURATION = 20;
    // The duration allocated for a client to provide their player's name in seconds.
    private final int SUPPLY_PLAYER_NAME_DURATION = 2;
    // The minimum number of players that can be signed up for a game.
    private final int MIN_PLAYER_SIGN_UPS = 2;
    // The maximum number of players that can be signed up for a game.
    private final int MAX_PLAYER_SIGN_UPS = 6;
    // The players that are signed up
    private final List<ProxyPlayer> proxyPlayers;
    private final ServerSocket serverSocket;
    private boolean signUpPeriodIsOver;
    private Optional<GameStateWithoutIPlayers> optionalGameState;

    public Server(int port) {
        this(port, Optional.empty());
    }

    public Server(int port, GameStateWithoutIPlayers gameState) {
        this(port, Optional.of(gameState));
    }

    public Server(int port, Optional<GameStateWithoutIPlayers> optionalGameState) {
        this.referee = new Referee();
        this.proxyPlayers = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("IO exception occurred while trying to create a socket: " + e.getMessage());
        }
        this.signUpPeriodIsOver = true;
        this.optionalGameState = optionalGameState;
    }

    /**
     * FOR TESTING PURPOSES ONLY:
     * Returns the number of players that were signed up by this server.
     * The number of signed up players is the number of players in the proxyPlayers list.
     * @return  the number of signed up players.
     */
    public int getNumberOfPlayersSignedUp() {
        return this.proxyPlayers.size();
    }

    /**
     * Runs the server by:
     * - handling server-client sign-ups
     * - creates the remote proxy players and sends them to this server's Referee to delegate running a complete game
     * - receive game results from the Referee and print them out
     */
    public GameResult runServer() {
        signUpPlayers();
        return hasMinPlayersSignedUp()
                ? runReferee()
                : new GameResult();
    }

    private GameResult runReferee() {
        Collections.reverse(proxyPlayers);
        if (optionalGameState.isEmpty()) {
            return referee.setupAndRunGame(getIPlayerList(proxyPlayers));
        }
        GameState gameState = optionalGameState.get().createGameStateWithIPlayers(getIPlayerList(proxyPlayers));
        return referee.setupAndRunGame(gameState, 0);
    }

    private List<IPlayer> getIPlayerList(List<ProxyPlayer> proxyPlayers) {
        return new ArrayList<>(proxyPlayers);
    }

    /**
     * Handle the server-client sign-up phase and add to the list of signed up proxy players in the order of sign-up
     */
    private void signUpPlayers() {
        signUpPeriodIsOver = false;
        runSignUpPeriods();
        signUpPeriodIsOver = true;
    }

    /**
     * Runs a specified number of sign up periods until the minimum number of players have signed up.
     */
    private void runSignUpPeriods() {
        int signUpPeriodCounter = 0;
        while (signUpPeriodCounter < NUMBER_OF_SIGN_UP_PERIODS && !hasMinPlayersSignedUp()) {
            runSignUpPeriodWithTimeout();
            signUpPeriodCounter++;
        }
    }

    /**
     * Has the minimum number of players signed up for a game?
     * @return  whether the minimum number of players have signed up
     */
    private boolean hasMinPlayersSignedUp() {
        return this.proxyPlayers.size() >= MIN_PLAYER_SIGN_UPS;
    }

    /**
     * Accept connections made by clients over this server's SIGN_UP_PERIOD_DURATION and adds to the list of
     * signed up players. Throws an exception if there is an interruption in the connections.
     */
    private void runSignUpPeriodWithTimeout() {
        runMethodWithTimeout(this::acceptConnections, SIGN_UP_PERIOD_DURATION);
    }

    /**
     * Runs the given method, timing out if the method runs longer than the given timeout in seconds.
     * @param method    the method to run with a timeout.
     * @param timeout   the timeout in seconds.
     */
    private void runMethodWithTimeout(Runnable method, int timeout) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future future = executor.submit(method);
            future.get(timeout, TimeUnit.SECONDS);
        }
        catch (TimeoutException e) {
            // if timing out, does nothing but end the acceptConnections method
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Server failed to accept connections using Future");
        }
    }

    /**
     * Adds to the current list of signed up players as clients connect to this server via TCP and those clients supply
     * a player's name within this server's specified duration to do so.
     */
    private void acceptConnections() {
        while (proxyPlayers.size() < MAX_PLAYER_SIGN_UPS) {
            SocketWrapper clientSocket = acceptClientConnection();
            waitForNameFromClient(clientSocket);
        }
    }

    /**
     * Add a proxy player to this server if the given client socket provides a name within the time allocated
     * @param clientSocketWrapper      the SocketWrapper of the client
     */
    private void waitForNameFromClient(SocketWrapper clientSocketWrapper) {
        runMethodWithTimeout(() -> addNameFromClient(clientSocketWrapper), SUPPLY_PLAYER_NAME_DURATION);
    }

    /**
     * Add the proxy player to this list of signed-up players if they provide a name within the given time allocation
     * @param clientSocketWrapper      SocketWrapper for the client
     */
    private void addNameFromClient(SocketWrapper clientSocketWrapper) {
        parseAndAddIfValidName(clientSocketWrapper);
    }

    private void parseAndAddIfValidName(SocketWrapper clientSocketWrapper) {
        JsonElement message = clientSocketWrapper.receiveMessage();
        addPlayerIfValidName(clientSocketWrapper, message);
    }

    /**
     * Add player to the list of proxy players in this server if they provide a valid name and the
     * sign up period allows them to connect.
     * @param clientSocketWrapper   the client socket to provide the proxy player
     * @param element               the element that is written by the client
     */
    private void addPlayerIfValidName(SocketWrapper clientSocketWrapper, JsonElement element) {
        if (JsonValidator.isValidJsonName(element) && !signUpPeriodIsOver) {
            String name = element.getAsString();
            ProxyPlayer newPlayer = new ProxyPlayer(name, clientSocketWrapper);
            proxyPlayers.add(newPlayer);
        }
    }

    /**
     * Waits to accept a client connection, returning the client's Socket if a client successfully connects.
     * @return  the socket for the connected client.
     */
    private SocketWrapper acceptClientConnection() {
        try {
            return new SocketWrapper(serverSocket.accept());
        } catch (IOException e) {
            throw new RuntimeException("IO exception occurred while trying to accept a client socket");
        }
    }
}