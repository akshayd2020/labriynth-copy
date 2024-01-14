import java.util.Optional;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.Color;


/**
 * Represents a Player which always passes
 */
public class PlayerPass extends Player {
    public PlayerPass(String name, IStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Decide to take a turn or pass
     * @param state     current state of the game
     * @return          play information that represents a turn
     */
    public Optional<PlayInfo> takeTurn(LimitedGS state) {
        return Optional.empty();
    }
}