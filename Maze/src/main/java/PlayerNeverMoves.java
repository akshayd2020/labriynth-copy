import java.util.Optional;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;

import java.awt.Color;

/**
 * Represents a Player which never moves in their turn.
 */
public class PlayerNeverMoves extends Player {

    public PlayerNeverMoves(String name, IStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Move to the current position for testing
     *
     * @param state current state of the game
     * @return play information that represents a turn
     */
    @Override
    public Optional<PlayInfo> takeTurn(LimitedGS state) {
        return Optional.of(new PlayInfo(0, Direction.UP, 0, state.getCurrentPosition()));
    }

}