import java.util.Optional;

/**
 * This player will go into an infinite loop upon calling the takeTurn method.
 */
public class PlayerLoopsTakeTurn extends AbstractPlayerLoops {

    public PlayerLoopsTakeTurn(String name, IStrategy strategy) {
        super(name, strategy, 1);
    }

    public PlayerLoopsTakeTurn(String name, IStrategy strategy, int count) {
        super(name, strategy, count);
    }

    @Override
    public Optional<PlayInfo> takeTurn(LimitedGS limitedGS) {
        infiniteLoopOnKthMethodCall();
        return super.takeTurn(limitedGS);
    }
}