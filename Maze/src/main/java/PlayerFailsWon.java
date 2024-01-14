/**
 * This player divides 1 by 0 to indicate a test harness failure in the the method that sets
 * the player's winning status.
 */
public class PlayerFailsWon extends Player {

    public PlayerFailsWon(String name, IStrategy strategy) {
        super(name, strategy);
    }

    @Override
    public void won(boolean won) {
        int x = 1 / 0;
    }
}