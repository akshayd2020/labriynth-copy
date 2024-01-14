import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket used for testing purposes.
 */
public class MockSocket extends Socket {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private boolean wasClosed;

    public MockSocket(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        wasClosed = false;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

//    @Override
//    public void close() {
//        wasClosed = true;
//    }
}
