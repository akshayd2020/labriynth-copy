import com.google.gson.*;
import javafx.util.Pair;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Observes one game of Labyrinth
 */
public class Observer extends AbstractObserver {
    /**
     * Constructs an observer with the given game state and renders the GUI
     * @param gameState the current game state
     */
    public Observer(GameState gameState) {
        super(new ArrayList<>(Arrays.asList(gameState)), 0);
    }

    public Observer() {
        super();
    }

    /**
     * Save the current state into a file specified by the filepath
     * @param filePath      file path to save into
     */
    public void saveCurrentState(String filePath) throws IOException {
        MappedRefGS mappedGS = MappedRefGS.gameStateToMappedRefGS(getCurrentGameState());
        Writer writer = new FileWriter(filePath);
        GsonSingleton.getInstance().toJson(mappedGS, writer);
        writer.flush();
        writer.close();
    }
}
