import java.util.Optional;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.Color;


/**
 * Represents a Player which always tries to shift an immoveable row or columns.
 */
public class PlayerShiftImmoveable extends Player {
    public PlayerShiftImmoveable(String name, IStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Return an immoveable row/column for testing
     * @param state     current state of the game
     * @return          play information that represents a turn
     */
    public Optional<PlayInfo> takeTurn(LimitedGS state) {
        return Optional.of(new PlayInfo(3, Direction.UP, 0, new Position(1, 1)));
    }
}