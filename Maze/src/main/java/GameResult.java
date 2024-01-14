import java.util.HashSet;
import java.util.Set;

/**
 * The resulting information of a completed game:
 * - winners are a set of players who have won based on the rules of the Referee
 * - misbehaved are a set of players who have misbehaved based on the failure modes of the
 *   Referee
 */
public class GameResult {
    private Set<IPlayer> winners;
    private Set<IPlayer> misbehaved;

    public GameResult(Set<IPlayer> winners, Set<IPlayer> misbehaved) {
        this.winners = winners;
        this.misbehaved = misbehaved;
    }

    public GameResult() {
        this.winners = new HashSet<>();
        this.misbehaved = new HashSet<>();
    }

    public Set<IPlayer> getWinners() {
        return winners;
    }

    public Set<IPlayer> getMisbehaved() {
        return misbehaved;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameResult)) {
            return false;
        }

        GameResult toCompare = (GameResult) o;

        return this.winners.equals(toCompare.getWinners())
                && this.misbehaved.equals(toCompare.getMisbehaved());
    }

    @Override
    public int hashCode() {
        return (this.winners.toString() + "," + this.misbehaved.toString()).hashCode();
    }
}