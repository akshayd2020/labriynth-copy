import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import javafx.util.Pair;
import java.util.HashMap;


/*
1. Connects to a client
2. Reads client input
3. Computes output
4. Print to client output
5. Closes connection
 */
public class EServer {
    ServerSocket serverSocket;
    Socket clientSocket;
    InputStream in;
    OutputStream out;
    List<Pair<String, String>> jsonFields;
    List<Character> characters;

    public EServer(int port, List<Pair<String, String>> jsons,
        List<Character> characters) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientSocket = serverSocket.accept();
        this.in = clientSocket.getInputStream();
        this.out = clientSocket.getOutputStream();
        this.jsonFields = jsons;
        this.characters = characters;
    }

    // Reads client input, Computes output, Prints to client output
    public void start() throws IOException {
        new C(this.jsonFields, this.characters, this.in, this.out).start();
        this.close();
    }

    // Closes the connection
    public void close() throws IOException {
        this.clientSocket.close();
        this.serverSocket.close();
    }

}