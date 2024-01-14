/**
 * This player will go into an infinite loop upon calling the won method.
 */
public class PlayerLoopsWon extends AbstractPlayerLoops {

    public PlayerLoopsWon(String name, IStrategy strategy) {
        super(name, strategy, 1);
    }

    public PlayerLoopsWon(String name, IStrategy strategy, int count) {
        super(name, strategy, count);
    }

    @Override
    public void won(boolean w) {
        infiniteLoopOnKthMethodCall();
        super.won(w);
    }
}