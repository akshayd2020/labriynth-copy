import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.net.Socket;

/**
 * Connects to a server over TCP and sends over one or more names over that connection to play a game with
 * those player(s).
 */
public class Client {
    private ProxyReferee proxyReferee;
    private SocketWrapper socketWrapper;
    private boolean successfullyConnected;
    private static final IStrategy DEFAULT_STRATEGY = new RiemannStrategy();

    /**
     * Connects this client to a server on the given port and given ip address and then requests
     * to make a player with the given name and defaults the strategy of the created player to the global
     * default strategy.
     * @param ip            ip address of the server this client wants to connect to.
     * @param port          port number of the server this client wants to connect to.
     * @param name          the name of the player to send over the connection.
     */
    public void connectToServerAndCreatePlayer(String ip, int port, String name) {
        connectToServerAndCreatePlayer(ip, port, name, DEFAULT_STRATEGY);
    }

    /**
     * Connects this client to a server on the given port and given ip address and then requests
     * to make a player with the given name and strategy.
     * @param ip            ip address of the server this client wants to connect to.
     * @param port          port number of the server this client wants to connect to.
     * @param name          the name of the player to send over the connection.
     * @param strategy      the strategy of the player used to create the player.
     */
    public void connectToServerAndCreatePlayer(String ip, int port, String name, IStrategy strategy) {
        getConnectionToServer(ip, port);
        sendNameAndStartProxyReferee(new Player(name, strategy));
    }

    /**
     * Connects this client to a server on the given port and given ip address and then requests
     * to make a player with the given name and strategy.
     * @param ip            ip address of the server this client wants to connect to.
     * @param port          port number of the server this client wants to connect to.
     * @param player        the player for this client's ProxyReferee to use.
     */
    public void connectToServerAndCreatePlayer(String ip, int port, IPlayer player) {
        getConnectionToServer(ip, port);
        sendNameAndStartProxyReferee(player);
    }

    /**
     * Determines if this client has successfully established a connection with a server.
     * @return  true if this client is connected to a server, false otherwise.
     */
    public boolean isSuccessfullyConnected() {
        return successfullyConnected;
    }

    /**
     * Get the connection to the server at the given ip address and given port number
     * @param ip    ip address of the server this client wants to connect to.
     * @param port  port number of the server this client wants to connect to.
     */
    public void getConnectionToServer(String ip, int port) {
        createSocketConnection(ip, port);
    }

    /**
     * Send the given name to the server to request the creation of a Player and
     * creates a ProxyReferee with a given IPlayer.
     * @param player        the player to add to the new ProxyReferee.
     */
    private void sendNameAndStartProxyReferee(IPlayer player) {
        socketWrapper.sendMessage(new JsonPrimitive(player.getName()));
        this.proxyReferee = new ProxyReferee(player, socketWrapper);
        proxyReferee.beginListening();
    }

    private void createSocketConnection(String ip, int port) {
        while (!successfullyConnected) {
            successfullyConnected = isConnectionSuccessful(ip, port);
        }
    }

    private boolean isConnectionSuccessful(String ip, int port) {
        try {
            socketWrapper = new SocketWrapper(new Socket(ip, port));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
