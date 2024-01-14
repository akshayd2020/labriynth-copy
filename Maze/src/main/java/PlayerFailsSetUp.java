import java.util.Optional;

/**
 * This player divides 1 by 0 to indicate a test harness failure in the setUp method of a
 * game.
 */
public class PlayerFailsSetUp extends Player {

    public PlayerFailsSetUp(String name, IStrategy strategy) {
        super(name, strategy);
    }

    @Override
    public void setup(Optional<LimitedGS> limitedGS, Position pos) {
        int x = 1 / 0;
    }
}