import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the ProxyReferee class.
 */
public class ProxyRefereeTest {
    GameState testGameState;
    Player clientPlayer;

    @BeforeEach
    public void setup() {
        clientPlayer = new Player("Client", new RiemannStrategy());
        Queue<PlayerState> playerStates = new LinkedList<>();
        playerStates.add(new PlayerState(Color.GREEN, new Position(1, 1),
                new Position(3, 3), new Position(5, 5), 0, clientPlayer));
        testGameState = SocketWrapperTest.generateSeededGameState(playerStates);
    }

    @Test
    public void testInterpretMethodSetup() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"setup\", [ false, { \"row#\": 1 , \"column#\": 1 } ] ]"),
                byteArrayOutputStream);
        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);
        assertEquals(Optional.empty(), clientPlayer.getTarget());

        proxyReferee.interpretMethodJsonAndRespond();
        String actual = byteArrayOutputStream.toString();
        String expected = "\"void\"";
        assertEquals(expected, actual);
        assertEquals(Optional.of(new Position(1, 1)), clientPlayer.getTarget());
    }

    @Test
    public void testInterpretMethodSetupFail() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"setup\", [ true, { \"row#\": 1 , \"column#\": 1 } ] ]"),
                byteArrayOutputStream);
        Player clientPlayer = new Player("Client", new RiemannStrategy());
        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        RuntimeException exception = assertThrows(RuntimeException.class,
                proxyReferee::interpretMethodJsonAndRespond);

        assertEquals(
                "ProxyReferee did not receive a valid JSON method message",
                exception.getMessage());
    }

    @Test
    public void testInterpretMethodTakeTurn() {
        JsonElement gameStateJson = MappedGS.convertLimitedGSToJson(testGameState.getLimitedGS());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"take-turn\", [" + gameStateJson + "] ]"),
                byteArrayOutputStream);
        clientPlayer.setup(Optional.empty(), new Position(1, 1));
        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        proxyReferee.interpretMethodJsonAndRespond();
        String actual = byteArrayOutputStream.toString();
        String expected = "[4,\"RIGHT\",0,{\"row#\":1,\"column#\":1}]";
        assertEquals(expected, actual);
    }

    @Test
    public void testInterpretMethodTakeTurnFail() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"take-turn\", [] ]"),
                byteArrayOutputStream);
        Player clientPlayer = new Player("Client", new RiemannStrategy());
        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        RuntimeException exception = assertThrows(RuntimeException.class,
                proxyReferee::interpretMethodJsonAndRespond);

        assertEquals(
                "ProxyReferee did not receive a valid JSON method message",
                exception.getMessage());
    }

    @Test
    public void testInterpretMethodWonFalseWorks() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"win\", [ false ] ]"),
                byteArrayOutputStream);
        Player clientPlayer = new Player("Client", new RiemannStrategy());

        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        proxyReferee.interpretMethodJsonAndRespond();
        String actual = byteArrayOutputStream.toString();
        String expected = "\"void\"";
        assertEquals(expected, actual);
        assertFalse(clientPlayer.getWon());
    }

    @Test
    public void testInterpretMethodWonTrueWorks() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"win\", [ true ] ]"),
                byteArrayOutputStream);
        Player clientPlayer = new Player("Client", new RiemannStrategy());

        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        proxyReferee.interpretMethodJsonAndRespond();
        String actual = byteArrayOutputStream.toString();
        String expected = "\"void\"";
        assertEquals(expected, actual);
        assertTrue(clientPlayer.getWon());
    }

    @Test
    public void testInterpretMethodWonFail() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MockSocket refSocket = new MockSocket(
                SocketWrapperTest.getByteArrayInputStream("[ \"win\", { \"row#\": 1 , \"column#\": 1 } ]"),
                byteArrayOutputStream);
        Player clientPlayer = new Player("Client", new RiemannStrategy());

        ProxyReferee proxyReferee = new ProxyReferee(clientPlayer, refSocket);

        RuntimeException exception = assertThrows(RuntimeException.class,
                proxyReferee::interpretMethodJsonAndRespond);

        assertEquals(
                "ProxyReferee did not receive a valid JSON method message",
                exception.getMessage());
    }
}
