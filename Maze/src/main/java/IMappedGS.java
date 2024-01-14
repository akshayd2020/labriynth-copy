import java.util.Queue;
import java.util.Map;

import java.awt.Color;

import javafx.util.Pair;

import java.util.Optional;

/**
 * The mapped game state representing the JSON object representing a game state
 */
public interface IMappedGS {
    public Tile getSpare();

    public Board getBoard();

    public Optional<SlidingAction> getLastAction();
}