import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SocketWrapper class.
 */
public class SocketWrapperTest {

    @BeforeEach
    public void setup() {
    }

    @Test
    public void sendMessageWorks() {
        ByteArrayOutputStream outputStream = getByteArrayOutputStream();
        MockSocket mockSocket = new MockSocket(getByteArrayInputStream(""), outputStream);
        SocketWrapper socketWrapper = new SocketWrapper(mockSocket);
        JsonArray testJsonArray = new JsonArray();
        testJsonArray.add("Jamie");
        testJsonArray.add(4);
        socketWrapper.sendMessage(testJsonArray);

        String expected = "[ \"Jamie\", 4 ]";
        String actual = outputStream.toString();
        assertTrue(areBothStringsInJsonEqual(expected, actual));
    }

    @Test
    public void sendMessageWorks2() {
        ByteArrayOutputStream outputStream = getByteArrayOutputStream();
        MockSocket mockSocket = new MockSocket(getByteArrayInputStream(""), outputStream);
        SocketWrapper socketWrapper = new SocketWrapper(mockSocket);
        LimitedGS limitedGS = generateSeededGameState().getLimitedGS();
        MappedGS mappedGS = MappedGS.gameStateToMappedGS(limitedGS);
        JsonElement gameStateJsonElement = GsonSingleton.getInstance().toJsonTree(mappedGS, MappedGS.class);
        socketWrapper.sendMessage(gameStateJsonElement);

        String expected = GsonSingleton.getInstance().toJson(gameStateJsonElement);
        String actual = outputStream.toString();
        assertTrue(areBothStringsInJsonEqual(expected, actual));
    }

    @Test
    public void receiveMessageWorks() {
        ByteArrayInputStream inputStream = getByteArrayInputStream("[\"test json\"]");
        MockSocket mockSocket = new MockSocket(inputStream, getByteArrayOutputStream());
        SocketWrapper socketWrapper = new SocketWrapper(mockSocket);
        JsonElement receivedElement = socketWrapper.receiveMessage();
        JsonArray expectedElement = new JsonArray();
        expectedElement.add("test json");
        assertEquals(expectedElement, receivedElement);
    }

    @Test
    public void receiveMessageWorks2() {
        LimitedGS limitedGS = generateSeededGameState().getLimitedGS();
        MappedGS mappedGS = MappedGS.gameStateToMappedGS(limitedGS);
        JsonElement gameStateJsonElement = GsonSingleton.getInstance().toJsonTree(mappedGS, MappedGS.class);
        String gameStateJsonString = GsonSingleton.getInstance().toJson(gameStateJsonElement);

        ByteArrayInputStream inputStream = getByteArrayInputStream(gameStateJsonString);
        MockSocket mockSocket = new MockSocket(inputStream, getByteArrayOutputStream());
        SocketWrapper socketWrapper = new SocketWrapper(mockSocket);
        JsonElement receivedElement = socketWrapper.receiveMessage();
        assertEquals(gameStateJsonElement, receivedElement);
    }

    @Test
    public void closeConnectionWorks() {
        Socket mockSocket = new MockSocket(getByteArrayInputStream(""), getByteArrayOutputStream());
        SocketWrapper socketWrapper = new SocketWrapper(mockSocket);
        assertFalse(mockSocket.isClosed());
        socketWrapper.closeConnection();
        assertTrue(mockSocket.isClosed());
    }

    /**
     * Given two string inputs that are represent JSON, compare whether their JSON values are equal.
     * @param jsonString1   the first given string representing JSON.
     * @param jsonString2   the first given string representing JSON.
     * @return              whether the two string inputs' JSON value are equal.
     */
    private boolean areBothStringsInJsonEqual(String jsonString1, String jsonString2) {
        Gson gson = GsonSingleton.getInstance();
        JsonElement json1 = gson.fromJson(jsonString1, JsonElement.class);
        JsonElement json2 = gson.fromJson(jsonString2, JsonElement.class);
        return json1.equals(json2);
    }

    public static ByteArrayInputStream getByteArrayInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    public static ByteArrayOutputStream getByteArrayOutputStream() {
        return new ByteArrayOutputStream();
    }

    public static GameState generateSeededGameState() {
        return generateSeededGameState(new LinkedList<>());
    }

    public static GameState generateSeededGameState(Queue<PlayerState> playerStates) {
        UnorderedPair<Gem, Gem> berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        Tile bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        Board testBoard7x7 = BoardTest.generateStaticBoard();
        return new GameState(bZTile, testBoard7x7, playerStates);
    }
}