import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.net.Socket;
import java.util.Optional;

/**
 * Handles method calls from the associated ProxyPlayer by converting those JSON calls to Java data representations,
 * and calls those methods on this ProxyReferee's player. This ProxyReferee will delegate sending back its player's
 * corresponding responses in JSON format via this ProxyReferee's SocketWrapper over TCP.
 */
public class ProxyReferee {
    private final SocketWrapper connection;
    private final IPlayer player;

    public ProxyReferee(IPlayer player, SocketWrapper connection) {
        this.player = player;
        this.connection = connection;
    }

    public ProxyReferee(IPlayer player, Socket socket) {
        this(player, new SocketWrapper(socket));
    }

    /**
     * Continues to listen for messages from this connection's input and delegates method calls when
     * a JSON message is received until this ProxyReferee's connection is closed.
     */
    public void beginListening() {
        while (!connection.isClosed()) {
            interpretMethodJsonAndRespond();
        }
     }

    /**
     * Waits until a JSON message is received from the TCP connection. When the message
     * is received, convert the JSON to a Java data representation and delegate the call
     * to this ProxyReferee's player and send the response.
     */
    public void interpretMethodJsonAndRespond() {
        JsonElement receivedJsonElement = connection.receiveMessage();
        if (!JsonValidator.isValidMethodCall(receivedJsonElement)) {
            throw new RuntimeException("ProxyReferee did not receive a valid JSON method message");
        }
        JsonElement result = callMethodFromJson(receivedJsonElement);
        connection.sendMessage(result);
    }

    /**
     * Calls the corresponding method from the JSON representation of a method call and delegates that call
     * to this ProxyReferee's player
     * @param receivedJsonElement   the received JSON message to be converted to be called on this ProxyReferee's player
     * @return                      the result of the method call as a JSONElement
     */
    private JsonElement callMethodFromJson(JsonElement receivedJsonElement) {
        JsonArray receivedArray = receivedJsonElement.getAsJsonArray();
        String methodName = receivedArray.get(0).getAsString();
        switch (methodName) {
            case "setup":
                return delegateSetupToPlayer(receivedArray.get(1));
            case "take-turn":
                return delegateTakeTurnToPlayer(receivedArray.get(1));
            case "win":
                return delegateWinToPlayer(receivedArray.get(1));
            default:
                throw new RuntimeException("ProxyReferee received a JSON method message with an invalid method name");
        }
    }

    /**
     * Call the setup function on the player of this ProxyReferee with arguments that are in Java data representation.
     * @param argumentElement   argument to convert to Java data representation that will be passed
     *                          into the setup function call
     * @return                  the corresponding JsonElement result from this method call
     */
    private JsonElement delegateSetupToPlayer(JsonElement argumentElement) {
        Optional<LimitedGS> optionalState = getOptionalState(argumentElement.getAsJsonArray().get(0));
        Position targetPosition = MappedCoordinate.convertsJsonToPosition(argumentElement.getAsJsonArray().get(1));
        this.player.setup(optionalState, targetPosition);
        return getVoidJsonElement();
    }

    /**
     * Call the takeTurn function on the player of this ProxyReferee with arguments that are in Java data representation.
     * @param argumentElement   argument to convert to Java data representation that will be passed
     *                          into the takeTurn function call
     * @return                  the corresponding JsonElement result from this method call
     */
    private JsonElement delegateTakeTurnToPlayer(JsonElement argumentElement) {
        LimitedGS state = MappedGS.convertJsonElementToLimitedGS(argumentElement.getAsJsonArray().get(0));
        Optional<PlayInfo> playInfo = this.player.takeTurn(state);
        return MappedPlayInfo.convertPlayInfoToJsonElement(playInfo);
    }

    /**
     * Call the won function on the player of this ProxyReferee with arguments that are in Java data representation.
     * @param argumentElement   argument to convert to Java data representation that will be passed
     *                          into the won function call
     * @return                  the corresponding JsonElement result from this method call
     */
    private JsonElement delegateWinToPlayer(JsonElement argumentElement) {
        boolean didWin = argumentElement.getAsJsonArray().get(0).getAsBoolean();
        this.player.won(didWin);
        return getVoidJsonElement();
    }

    private JsonElement getVoidJsonElement() {
        return new JsonPrimitive("void");
    }


    /**
     * Get the corresponding optional limited game state of the JsonElement.
     * @param element   the element to convert to an Optional<LimitedGS>.
     * @return          the corresponding optional limited game state of the JsonElement.
     */
    private Optional<LimitedGS> getOptionalState(JsonElement element) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return Optional.empty();
        }
        return Optional.of(MappedGS.convertJsonElementToLimitedGS(element));
    }
}
