import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.net.Socket;

/**
 * Wrapper class for a socket that will handle sending JSON inputs and outputs over the connection.
 */
public class SocketWrapper {
    private final Socket socket;
    private JsonWriter writer;
    private JsonReader reader;

    public SocketWrapper(Socket socket) {
        this.socket = socket;
        initJsonWriterAndReader(socket);
    }

    /**
     * Initializes the JsonWriter and JsonReader for the given socket.
     * @param socket    the socket to make a JsonWriter and JsonReader for.
     */
    private void initJsonWriterAndReader(Socket socket) {
        try {
            OutputStream output = socket.getOutputStream();
            writer = new JsonWriter(new OutputStreamWriter(output));
            writer.setLenient(false);
        }
        catch (IOException e) {
            throw new RuntimeException("SocketWrapper caused IOException while initializing Json reader and writer");
        }
    }

    /**
     * Sends the given JsonElement over the TCP connection represented by this SocketWrapper.
     * If the output stream throws an IOException, the output stream is NOT closed by this method.
     * @param element   the JsonElement to send.
     */
    public void sendMessage(JsonElement element) {
        GsonSingleton.getInstance().toJson(element, writer);
        flushWriter();
    }

    private void flushWriter() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("SocketWrapper caused IOException while writing to output stream");
        }
    }

    /**
     * Waits to receive a JsonMessage over the TCP connection represented by this SocketWrapper.
     * @return      the JsonElement received over TCP connection.
     */
    public JsonElement receiveMessage() {
        try {
            InputStream input = socket.getInputStream();
            reader = new JsonReader(new InputStreamReader(input));
            reader.setLenient(false);
            return GsonSingleton.getInstance().getAdapter(JsonElement.class).read(reader);
        } catch (IOException e) {
            throw new RuntimeException("SocketWrapper received invalid JSON from the connection");
        }
    }
    /**
     * Is this socket closed?
     * @return  whether this socket is closed
     */
    public boolean isClosed() {
        return socket.isClosed();
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
