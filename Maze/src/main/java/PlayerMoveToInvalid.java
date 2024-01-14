import java.util.Optional;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.Color;


/**
 * Represents a Player which always tries to move an invalid position.
 */
public class PlayerMoveToInvalid extends Player {

    public PlayerMoveToInvalid(String name, IStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Move to an invalid position for testing
     * @param state     current state of the game
     * @return          play information that represents a turn
     */
    @Override
    public Optional<PlayInfo> takeTurn(LimitedGS state) {
        return Optional.of(new PlayInfo(0, Direction.UP, 0, new Position(-1, -1)));
    }

}