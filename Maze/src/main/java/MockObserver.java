import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MockObserver extends AbstractObserver {

    Appendable appendable;

    public MockObserver(GameState gameState, Appendable appendable) {
        super(new ArrayList<>(Arrays.asList(gameState)), 0);
        this.appendable = appendable;
    }

    /**
     * Save the current state into a file specified by the filepath
     * @param filePath      file path to save into
     */
    public void saveCurrentState(String filePath) throws IOException {
        this.appendable.append("handleSaveCurrentState" + filePath);
    }
}
