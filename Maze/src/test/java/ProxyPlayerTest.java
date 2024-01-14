import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ProxyPlayer class.
 */
public class ProxyPlayerTest {
    Position position55;

    @BeforeEach
    public void setup() {
        position55 = new Position(5, 5);
    }
    @Test
    public void setupWorks() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"void\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        testProxyPlayer.setup(Optional.empty(), position55);
        JsonArray expected = new JsonArray();
        expected.add("setup");
        JsonArray expectedArgs = new JsonArray();
        expectedArgs.add(new JsonPrimitive(false));
        expectedArgs.add(MappedCoordinate.convertPositionToJsonElement(position55));
        expected.add(expectedArgs);
        assertEquals(expected.toString(), byteArrayOutputStream.toString());
    }

    @Test
    public void setupFailsWithInvalidResponse() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"flip flop boop bop\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);


        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testProxyPlayer.setup(Optional.empty(), position55));

        assertEquals(
                "Client returned \"flip flop boop bop\" when \"void\" was expected",
                exception.getMessage());
    }

    @Test
    public void takeTurnReturnsPassWorks() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"PASS\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();
        Optional<PlayInfo> actualPlayInfo = testProxyPlayer.takeTurn(limitedGS);
        assertTrue(actualPlayInfo.isEmpty());
        JsonArray expected = new JsonArray();
        expected.add(new JsonPrimitive("take-turn"));
        JsonArray expectedArrayArguments = new JsonArray();
        expectedArrayArguments.add(MappedGS.convertLimitedGSToJson(limitedGS));
        expected.add(expectedArrayArguments);
        assertEquals(expected.toString(), byteArrayOutputStream.toString());
    }

    @Test
    public void takeTurnReturnsPlayInfoWorks() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[5, \"LEFT\", 90, { \"row#\": 1, \"column#\": 1}]"),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();
        Optional<PlayInfo> actualPlayInfo = testProxyPlayer.takeTurn(limitedGS);
        assertEquals(new PlayInfo(5, Direction.LEFT, 90, new Position(1, 1)), actualPlayInfo.get());
        JsonArray expected = new JsonArray();
        expected.add(new JsonPrimitive("take-turn"));
        JsonArray expectedArrayArguments = new JsonArray();
        expectedArrayArguments.add(MappedGS.convertLimitedGSToJson(limitedGS));
        expected.add(expectedArrayArguments);
        assertEquals(expected.toString(), byteArrayOutputStream.toString());
    }

    @Test
    public void takeTurnFailsWithInvalidResponse() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[5, \"LEFT\", \"90\", { \"row#\": 1, \"column#\": 1}]"),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);
        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testProxyPlayer.takeTurn(limitedGS));

        assertEquals(
                "Client did not return a valid Choice",
                exception.getMessage());
    }

    @Test
    public void wonWorksWhenTrue() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"void\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();
        testProxyPlayer.won(true);
        JsonArray expected = new JsonArray();
        expected.add(new JsonPrimitive("win"));
        JsonArray expectedArrayArguments = new JsonArray();
        expectedArrayArguments.add(true);
        expected.add(expectedArrayArguments);
        assertEquals(expected.toString(), byteArrayOutputStream.toString());
    }

    @Test
    public void wonWorksWhenFalse() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"void\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();
        testProxyPlayer.won(false);
        JsonArray expected = new JsonArray();
        expected.add(new JsonPrimitive("win"));
        JsonArray expectedArrayArguments = new JsonArray();
        expectedArrayArguments.add(false);
        expected.add(expectedArrayArguments);
        assertEquals(expected.toString(), byteArrayOutputStream.toString());
    }

    @Test
    public void wonFailsWithInvalidResponse() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("void"),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testProxyPlayer.won(false));

        assertEquals(
                "SocketWrapper received invalid JSON from the connection",
                exception.getMessage());
    }

    @Test
    public void wonFailsWithInvalidResponse2() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket playerSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("\"void\" \"void\""),
                byteArrayOutputStream);
        ProxyPlayer testProxyPlayer = new ProxyPlayer("ProxyPlayer", playerSocket);

        LimitedGS limitedGS = SocketWrapperTest.generateSeededGameState()
                .getLimitedGS();

        testProxyPlayer.won(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> testProxyPlayer.won(false));

        assertEquals(
                "SocketWrapper received invalid JSON from the connection",
                exception.getMessage());
    }
}