import java.util.Optional;

/**
 * This player will go into an infinite loop upon calling the takeTurn method.
 */
public class PlayerLoopsSetUp extends AbstractPlayerLoops {


    public PlayerLoopsSetUp(String name, IStrategy strategy) {
        super(name, strategy, 1);
    }

    public PlayerLoopsSetUp(String name, IStrategy strategy, int count) {
        super(name, strategy, count);
    }

    @Override
    public void setup(Optional<LimitedGS> limitedGS, Position target) {
        infiniteLoopOnKthMethodCall();
        super.setup(limitedGS, target);
    }
}