import java.awt.*;

/**
 * Represents a PlayerState that does not contain a IPlayer.
 */
public class PlayerStateWithoutIPlayer {
    private Color avatar;
    private Position home;
    private Position goal;
    private Position current;
    private int numGoalsReached;

    public PlayerStateWithoutIPlayer(Color avatar, Position home, Position goal, Position current, int numGoalsReached) {
        this.avatar = avatar;
        this.home = home;
        this.goal = goal;
        this.current = current;
        this.numGoalsReached = numGoalsReached;
    }

    public PlayerState createPlayerStateWithIPlayer(IPlayer player) {
        return new PlayerState(avatar, home, goal, current, 0, player);
    }
}
