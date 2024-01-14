import com.google.gson.*;

import java.net.Socket;
import java.util.Optional;

/**
 * Handles calls from the Referee by converting those calls to JSON which will be sent over the TCP
 * connection between the Server and the corresponding client. After carrying out the functionality,
 * this ProxyPlayer will delegate sending back its corresponding responses via this player's SocketWrapper
 * to the Referee.
 */
public class ProxyPlayer implements IPlayer {
    private final String name;
    private final SocketWrapper connection;

    public ProxyPlayer(String name, SocketWrapper connection) {
        this.name = name;
        this.connection = connection;
    }

    /**
     * Constructor that takes a name and socket and generates a SocketWrapper for that socket.
     * @param name      the name of the player.
     * @param socket    the socket representing the TCP connection of this player.
     */
    public ProxyPlayer(String name, Socket socket) {
        this(name, new SocketWrapper(socket));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Board proposeBoard(int rows, int columns) {
        throw new UnsupportedOperationException("Board proposal not part of specifications yet");
    }

    @Override
    public void setup(Optional<LimitedGS> limitedGameState, Position target) {
        JsonElement setupJsonInfo = getJsonForSetup(limitedGameState, target);
        connection.sendMessage(setupJsonInfo);
        checkForVoidReturn(connection.receiveMessage());
    }

    /**
     * Get the final message to send back to the ProxyReferee in the client to carry out the setup function
     * for this proxy player.
     * @param limitedGameState  game state information that is available to this player
     * @param target            target position to visit next
     * @return                  the final message in JSON format for the ProxyReferee
     */
    private JsonElement getJsonForSetup(Optional<LimitedGS> limitedGameState, Position target) {
        JsonArray mappedArguments = new JsonArray();
        JsonElement stateArgument = getStateArgument(limitedGameState);
        JsonElement positionArgument = MappedCoordinate.convertPositionToJsonElement(target);
        mappedArguments.add(stateArgument);
        mappedArguments.add(positionArgument);

        return getMethodWithArgumentJson("setup", mappedArguments);
    }

    /**
     * Get the JSON representation of a method call
     * @param methodName    the name of the method
     * @param arguments     the arguments in JSON format
     * @return              the JSON representation of a method call
     */
    private JsonElement getMethodWithArgumentJson(String methodName, JsonElement arguments) {
        JsonArray finalMessage = new JsonArray();
        finalMessage.add(methodName);
        finalMessage.add(arguments);
        return finalMessage;
    }

    /**
     * Get the state argument from a LimitedGS in the specified JSON format. If the game state
     * is an Optional empty, return a JSON false. Otherwise, return the MappedGameState.
     * @param limitedGameState  game state information that is available to this player.
     * @return                  if the game state is empty, return JSON false. Otherwise, a JSON representing the state.
     */
    private JsonElement getStateArgument(Optional<LimitedGS> limitedGameState) {
        if (limitedGameState.isEmpty()) {
            return new JsonPrimitive(false);
        } else {
            MappedGS mappedGS = MappedGS.gameStateToMappedGS(limitedGameState.get());
            return GsonSingleton.getInstance().toJsonTree(mappedGS, MappedGS.class);
        }
    }

    @Override
    public Optional<PlayInfo> takeTurn(LimitedGS state) {
        JsonElement takeTurnInfo = getJsonForTakeTurn(state);
        connection.sendMessage(takeTurnInfo);
        JsonElement clientMessage = connection.receiveMessage();
        if (!JsonValidator.isValidChoice(clientMessage)) {
            throw new RuntimeException("Client did not return a valid Choice");
        }
        return MappedPlayInfo.convertJsonElementToPlayInfo(clientMessage);
    }

    private JsonElement getJsonForTakeTurn(LimitedGS state) {
        JsonElement stateArgument = MappedGS.convertLimitedGSToJson(state);
        JsonArray argumentsAsArray = new JsonArray();
        argumentsAsArray.add(stateArgument);
        return getMethodWithArgumentJson("take-turn", argumentsAsArray);
    }

    @Override
    public void won(boolean winStatus) {
        JsonElement winStatusInfo = getJsonForWon(winStatus);
        connection.sendMessage(winStatusInfo);
        checkForVoidReturn(connection.receiveMessage());
    }

    /**
     * Get the final message to send back to the ProxyReferee in the client to carry out the won function.
     * @param winStatus     whether the player has won or not.
     * @return              the final message in JSON format for the ProxyReferee.
     */
    private JsonElement getJsonForWon(boolean winStatus) {
        JsonElement booleanArgument = new JsonPrimitive(winStatus);
        JsonArray argumentsAsArray = new JsonArray();
        argumentsAsArray.add(booleanArgument);
        return getMethodWithArgumentJson("win", argumentsAsArray);
    }

    /**
     * Checks if the given JsonElement is a JSON "void" string, and throws an exception to alert the
     * referee if it is not.
     * @param receivedJson  the received JsonElement to check.
     */
    private void checkForVoidReturn(JsonElement receivedJson) {
        if (!(receivedJson.isJsonPrimitive() && receivedJson.getAsJsonPrimitive().isString()
                && receivedJson.getAsString().equals("void"))) {
            throw new RuntimeException("Client returned " + receivedJson + " when \"void\" was expected");
        }
    }

    /**
     * Closes the socket connection stored by this ProxyPlayer. This method should be called
     * when the connection between the server and client needs to be severed.
     */
    public void close() {
        connection.closeConnection();
    }
}
