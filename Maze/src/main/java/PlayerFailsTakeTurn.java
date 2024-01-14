import java.util.Optional;

/**
 * This player divides 1 by 0 to indicate a test harness failure in the takeTurn method of a
 * game.
 */
public class PlayerFailsTakeTurn extends Player {

    public PlayerFailsTakeTurn(String name, IStrategy strategy) {
        super(name, strategy);
    }

    @Override
    public Optional<PlayInfo> takeTurn(LimitedGS limitedGS) {
        int x = 1 / 0;
        return Optional.empty();
    }
}