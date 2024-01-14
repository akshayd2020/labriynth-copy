import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for JsonValidator class.
 */
public class JsonValidatorTest {
    @Test
    public void isValidMethodCallReturnsTrueForSetupWithFalseState() {
        JsonArray setupCall = new JsonArray();
        setupCall.add("setup");
        JsonArray arguments = new JsonArray();
        arguments.add(new JsonPrimitive(false));
        arguments.add(MappedCoordinate.convertPositionToJsonElement(new Position(1,1)));
        setupCall.add(arguments);
        assertTrue(JsonValidator.isValidMethodCall(setupCall));
    }

    @Test
    public void isValidMethodCallReturnsTrueForSetupWithState() {
        JsonArray setupCall = new JsonArray();
        setupCall.add("setup");
        JsonArray arguments = new JsonArray();
        arguments.add(MappedGS.convertLimitedGSToJson(SocketWrapperTest.generateSeededGameState().getLimitedGS()));
        arguments.add(MappedCoordinate.convertPositionToJsonElement(new Position(1,1)));
        setupCall.add(arguments);
        assertTrue(JsonValidator.isValidMethodCall(setupCall));
    }

    @Test
    public void isValidMethodCallReturnsTrueForTakeTurn() {
        JsonArray setupCall = new JsonArray();
        setupCall.add("take-turn");
        JsonArray arguments = new JsonArray();
        arguments.add(MappedGS.convertLimitedGSToJson(SocketWrapperTest.generateSeededGameState().getLimitedGS()));
        setupCall.add(arguments);
        assertTrue(JsonValidator.isValidMethodCall(setupCall));
    }

    @Test
    public void isValidMethodCallReturnsTrueForTakeTurn2() {
        Player player = new Player("Client", new RiemannStrategy());
        Queue<PlayerState> playerStates = new LinkedList<>();
        playerStates.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(3, 3), new Position(5, 5), 0, player));
        GameState testGameState = SocketWrapperTest.generateSeededGameState(playerStates);

        JsonElement gameStateJson = MappedGS.convertLimitedGSToJson(testGameState.getLimitedGS());
        String string = "[ \"take-turn\", [" + gameStateJson + "] ]";

        JsonElement takeTurnCall = GsonSingleton.getInstance().fromJson(string, JsonElement.class);
        assertTrue(JsonValidator.isValidMethodCall(takeTurnCall));
    }

    @Test
    public void isValidMethodCallReturnsTrueForWin() {
        JsonArray winCall = new JsonArray();
        winCall.add("win");
        JsonArray arguments = new JsonArray();
        arguments.add(false);
        winCall.add(arguments);
        assertTrue(JsonValidator.isValidMethodCall(winCall));
    }

    @Test
    public void isValidMethodCallReturnsTrueForWin2() {
        JsonArray winCall = new JsonArray();
        winCall.add("win");
        JsonArray arguments = new JsonArray();
        arguments.add(true);
        winCall.add(arguments);
        assertTrue(JsonValidator.isValidMethodCall(winCall));
    }

    @Test
    public void isValidMethodCallReturnsFalse() {
        JsonArray winCall = new JsonArray();
        winCall.add("win");
        JsonArray arguments = new JsonArray();
        arguments.add("true");
        winCall.add(arguments);
        assertFalse(JsonValidator.isValidMethodCall(winCall));
    }

    @Test
    public void isValidMethodCallReturnsFalse2() {
        JsonArray failCall = new JsonArray();
        failCall.add("void");
        JsonArray arguments = new JsonArray();
        arguments.add("hello");
        failCall.add(arguments);
        assertFalse(JsonValidator.isValidMethodCall(failCall));
    }

    @Test
    public void isValidMethodCallReturnsFalse3() {
        JsonArray setupCall = new JsonArray();
        setupCall.add("setup");
        JsonArray arguments = new JsonArray();
        arguments.add(new JsonPrimitive(true));
        arguments.add(MappedCoordinate.convertPositionToJsonElement(new Position(1,1)));
        setupCall.add(arguments);
        assertFalse(JsonValidator.isValidMethodCall(setupCall));
    }

    @Test
    public void isValidChoiceReturnsTrue() {
        JsonArray choiceArray = new JsonArray();
        choiceArray.add(1);
        choiceArray.add("LEFT");
        choiceArray.add(180);
        choiceArray.add(MappedCoordinate.convertPositionToJsonElement(new Position(1, 1)));
        assertTrue(JsonValidator.isValidChoice(choiceArray));
    }

    @Test
    public void isValidChoiceReturnsTrue2() {
        JsonPrimitive choicePass = new JsonPrimitive("PASS");
        assertTrue(JsonValidator.isValidChoice(choicePass));
    }

    @Test
    public void isValidChoiceReturnsFalse() {
        JsonArray choiceArray = new JsonArray();
        choiceArray.add(1);
        choiceArray.add("LEFT");
        choiceArray.add(181);
        choiceArray.add(MappedCoordinate.convertPositionToJsonElement(new Position(1, 1)));
        assertFalse(JsonValidator.isValidChoice(choiceArray));
    }

    @Test
    public void isValidNameReturnsTrue() {
        JsonElement validName = new JsonPrimitive("TestPlayer1");
        assertTrue(JsonValidator.isValidJsonName(validName));
        validName = new JsonPrimitive("12345678901234567890");
        assertTrue(JsonValidator.isValidJsonName(validName));
        validName = new JsonPrimitive("a");
        assertTrue(JsonValidator.isValidJsonName(validName));
    }

    @Test
    public void isValidNameReturnsFalse() {
        JsonElement validName = new JsonPrimitive("Test Player 1");
        assertFalse(JsonValidator.isValidJsonName(validName));
        validName = new JsonPrimitive("123456789012345678901");
        assertFalse(JsonValidator.isValidJsonName(validName));
        validName = new JsonPrimitive("");
        assertFalse(JsonValidator.isValidJsonName(validName));
        validName = new JsonPrimitive("TestPl@yer1");
        assertFalse(JsonValidator.isValidJsonName(validName));
    }
}
