import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains tests for both the Server and Client classes.
 */
public class ServerAndClientTest {

    Thread.UncaughtExceptionHandler uncaughtExceptionHandler =
            (thread, exception) -> System.out.println("Uncaught exception: " + exception);

    @Test
    public void testRunningServerWithZeroClients() {
        Server testServer = new Server(12347);

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        waitForServerThreadToFinish(serverThread);

        JsonArray expected = new JsonArray();
        expected.add(new JsonArray());
        expected.add(new JsonArray());
        JsonElement actual = MappedGameResult.convertGameResultToJsonElement(serverThread.getResults());
        assertEquals(expected, actual);
        assertEquals(0, testServer.getNumberOfPlayersSignedUp());
    }

    @Test
    public void testRunningServerWithOneClient() {
        Server testServer = new Server(12346);
        Client testClient = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread = new ClientThread(testClient, "localhost", 12346, "TestClientPlayer", new EuclidStrategy());
        clientThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread.start();
        waitForServerThreadToFinish(serverThread);

        JsonArray expected = new JsonArray();
        expected.add(new JsonArray());
        expected.add(new JsonArray());
        JsonElement actual = MappedGameResult.convertGameResultToJsonElement(serverThread.getResults());
        assertEquals(expected, actual);
        assertEquals(1, testServer.getNumberOfPlayersSignedUp());
    }

    // Server will time out players that do not send over a name
    @Test
    public void testRunningServerWithClientThatSendsNoName() {
        int portNum = 33333;
        Server testServer = new Server(portNum);
        Client testClient1 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThreadWithoutNameSending(testClient1, "localhost", portNum);
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(0, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server accepts a sign-up from a client that signs up in the second waiting period
    @Test
    public void testRunningServerWithClientThatSignsUpInSecondWaitingPeriod() {
        int portNum = 55555;
        Server testServer = new Server(portNum);
        Client testClient1 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost",
                portNum, "Yippy", new RiemannStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        waitSpecifiedAmountOfSeconds(Server.SIGN_UP_PERIOD_DURATION);
        clientThread1.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(1, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server accepts a sign-up from a client that signs up in the first waiting period,
    // and then accepts two players from the second waiting period
    @Test
    public void testRunningServerWithOneFirstPeriodTwoSecondPeriod() {
        int portNum = 46666;
        Server testServer = new Server(portNum);
        Client testClient1 = new Client();
        Client testClient2 = new Client();
        Client testClient3 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost",
                portNum, "Yippy", new RiemannStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread2 = new ClientThread(testClient2, "localhost",
                portNum, "Pippy", new RiemannStrategy());
        clientThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread3 = new ClientThread(testClient3, "localhost",
                portNum, "Tippy", new RiemannStrategy());
        clientThread3.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        waitSpecifiedAmountOfSeconds(Server.SIGN_UP_PERIOD_DURATION);
        clientThread2.start();
        waitSpecifiedAmountOfSeconds(Server.SIGN_UP_PERIOD_DURATION / 2);
        clientThread3.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(3, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server accepts a sign-up from three clients that signs up in the first waiting period,
    // and then runs the game without the other three clients in the second period.
    @Test
    public void testRunningServerWithThreeFirstPeriodThreeSecondPeriod() {
        int portNum = 36666;
        Server testServer = new Server(portNum);
        Client testClient1 = new Client();
        Client testClient2 = new Client();
        Client testClient3 = new Client();
        Client testClient4 = new Client();
        Client testClient5 = new Client();
        Client testClient6 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost",
                portNum, "Yippy", new RiemannStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread2 = new ClientThread(testClient2, "localhost",
                portNum, "Pippy", new RiemannStrategy());
        clientThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread3 = new ClientThread(testClient3, "localhost",
                portNum, "Tippy", new RiemannStrategy());
        clientThread3.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread4 = new ClientThread(testClient4, "localhost",
                portNum, "Chippy", new RiemannStrategy());
        clientThread4.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread5 = new ClientThread(testClient5, "localhost",
                portNum, "Lippy", new RiemannStrategy());
        clientThread5.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread6 = new ClientThread(testClient6, "localhost",
                portNum, "Buford", new RiemannStrategy());
        clientThread6.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        clientThread2.start();
        clientThread3.start();
        waitSpecifiedAmountOfSeconds(Server.SIGN_UP_PERIOD_DURATION);
        waitSpecifiedAmountOfSeconds(1);
        clientThread4.start();
        waitSpecifiedAmountOfSeconds(1);
        clientThread5.start();
        waitSpecifiedAmountOfSeconds(1);
        clientThread6.start();

        waitForServerThreadToFinish(serverThread);
        assertEquals(3, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server with 6 different clients runs to completion
    @Test
    public void testRunningServerWith6DifferentClients() {
        Server testServer = new Server(11111);
        Client testClient1 = new Client();
        Client testClient2 = new Client();
        Client testClient3 = new Client();
        Client testClient4 = new Client();
        Client testClient5 = new Client();
        Client testClient6 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost", 11111,
                "TestClientPlayer1", new EuclidStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread2 = new ClientThread(testClient2, "localhost", 11111,
                "TestClientPlayer2", new EuclidStrategy());
        clientThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread3 = new ClientThread(testClient3, "localhost", 11111,
                "TestClientPlayer3", new EuclidStrategy());
        clientThread3.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread4 = new ClientThread(testClient4, "localhost", 11111,
                "TestClientPlayer4", new EuclidStrategy());
        clientThread4.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread5 = new ClientThread(testClient5, "localhost", 11111,
                "TestClientPlayer5", new EuclidStrategy());
        clientThread5.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread6 = new ClientThread(testClient6, "localhost", 11111,
                "TestClientPlayer6", new EuclidStrategy());
        clientThread6.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        clientThread2.start();
        clientThread3.start();
        clientThread4.start();
        clientThread5.start();
        clientThread6.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(6, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server with 7 different clients runs to completion (7th player can't join
    // but the server will still run with the first 6 players)
    @Test
    public void testRunningServerWith7DifferentClients() {
        int portNum = 22222;
        Server testServer = new Server(portNum);
        Client testClient1 = new Client();
        Client testClient2 = new Client();
        Client testClient3 = new Client();
        Client testClient4 = new Client();
        Client testClient5 = new Client();
        Client testClient6 = new Client();
        Client testClient7 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost", portNum,
                "TestClientPlayer1", new EuclidStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread2 = new ClientThread(testClient2, "localhost", portNum,
                "TestClientPlayer2", new EuclidStrategy());
        clientThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread3 = new ClientThread(testClient3, "localhost", portNum,
                "TestClientPlayer3", new EuclidStrategy());
        clientThread3.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread4 = new ClientThread(testClient4, "localhost", portNum,
                "TestClientPlayer4", new EuclidStrategy());
        clientThread4.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread5 = new ClientThread(testClient5, "localhost", portNum,
                "TestClientPlayer5", new EuclidStrategy());
        clientThread5.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread6 = new ClientThread(testClient6, "localhost", portNum,
                "TestClientPlayer6", new EuclidStrategy());
        clientThread6.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread7 = new ClientThread(testClient7, "localhost", portNum,
                "TestClientPlayer7", new EuclidStrategy());
        clientThread7.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        clientThread2.start();
        clientThread3.start();
        clientThread4.start();
        clientThread5.start();
        clientThread6.start();
        clientThread7.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(6, testServer.getNumberOfPlayersSignedUp());
    }

    // Tests that a server with 2 different clients runs to completion
    @Test
    public void testRunningServerWith2DifferentClients() {
        Server testServer = new Server(12345);
        Client testClient1 = new Client();
        Client testClient2 = new Client();

        ServerThread serverThread = new ServerThread(testServer);
        serverThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread1 = new ClientThread(testClient1, "localhost", 12345,
                "TestClientPlayer1", new EuclidStrategy());
        clientThread1.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        ClientThread clientThread2 = new ClientThread(testClient2, "localhost", 12345,
                "TestClientPlayer2", new EuclidStrategy());
        clientThread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);

        serverThread.start();
        clientThread1.start();
        clientThread2.start();
        waitForServerThreadToFinish(serverThread);
        assertEquals(2, testServer.getNumberOfPlayersSignedUp());
    }

    /**
     * Waits for the given ServerThread to finish, joining that thread to the main thread
     * once it has finished.
     * @param thread    the thread to wait for and join.
     */
    private void waitForServerThreadToFinish(ServerThread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Thread class that will run the given client as a separate thread, for use in
     * running both servers and clients in parallel for testing.
     */
    private static class ClientThread extends Thread {
        protected final Client client;
        protected final String ip;
        protected final int port;
        private final String name;
        private final IStrategy strategy;

        public ClientThread(Client client, String ip, int port, String name, IStrategy strategy) {
            this.client = client;
            this.ip = ip;
            this.port = port;
            this.name = name;
            this.strategy = strategy;
        }

        @Override
        public void run() {
            client.connectToServerAndCreatePlayer(ip, port, name, strategy);
        }
    }

    /**
     * Thread class that will run the given client as a separate thread without sending over a name,
     * for use in running both servers and clients in parallel for testing.
     */
    private static class ClientThreadWithoutNameSending extends ClientThread {
        public ClientThreadWithoutNameSending(Client client, String ip, int port) {
            super(client, ip, port, "", new RiemannStrategy());
        }

        @Override
        public void run() {
            client.getConnectionToServer(this.ip, this.port);
        }
    }

    private void waitSpecifiedAmountOfSeconds(int numSeconds) {
        synchronized (this) {
            try {
                this.wait(numSeconds * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Thread class that will run the given server as a separate thread, for use in
     * running both servers and clients in parallel for testing.
     * When the server is done running, it will store the results of the game (in JsonElement form)
     * inside this Thread class.
     */
    private static class ServerThread extends Thread {
        private final Server server;
        private GameResult results;

        public ServerThread(Server server) {
            this.server = server;
        }

        public GameResult getResults() {
            return results;
        }

        @Override
        public void run() {
            results = server.runServer();
        }
    }
}