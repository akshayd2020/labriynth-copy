import com.google.gson.annotations.SerializedName;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * The mapped player representing the JSON object representing a referee player. A referee player has a  "goto" field
 */
public class MappedRefPlayer extends AbstractMappedPlayer {
    @SerializedName("goto")
    private MappedCoordinate target;

    public MappedRefPlayer(MappedCoordinate current, MappedCoordinate home, MappedCoordinate target, String color) {
        super(current, home, color);
        this.target = target;
    }

    /**
     * Returns the player state of this MappedRefPlayer, which contains the given IPlayer.
     * @param player    the player that this player state contains.
     * @return          the player state.
     */
    public PlayerState getPlayerState(IPlayer player) {
        return new PlayerState(this.getColor(), this.home.getPosition(), this.target.getPosition(),
                this.current.getPosition(), 0, player);
    }

    public PlayerStateWithoutIPlayer getPlayerStateWithoutIPlayer() {
        return new PlayerStateWithoutIPlayer(this.getColor(), this.home.getPosition(), this.target.getPosition(),
                this.current.getPosition(), 0);
    }

    public static MappedRefPlayer[] playerStatesToRefPlayers(Queue<PlayerState> ps) {
        MappedRefPlayer[] ans = new MappedRefPlayer[ps.size()];
        List<PlayerState> psList = new ArrayList<>(ps);
        for (int i = 0; i < ps.size(); i += 1) {
            PlayerState currentPS = psList.get(i);
            MappedCoordinate currentMS = currentPS.getCurrentPosition().getMappedCoordinate();
            MappedCoordinate homeMS = currentPS.getHomePosition().getMappedCoordinate();
            MappedCoordinate targetMS = currentPS.getGoalPosition().getMappedCoordinate();
            String color = getHexString(currentPS.getAvatar());
            ans[i] = new MappedRefPlayer(currentMS,homeMS, targetMS, color);
        }

        return ans;
    }

    public MappedCoordinate getCurrent() {
        return this.current;
    }

    public MappedCoordinate getHome() {
        return home;
    }

    public MappedCoordinate getTarget() {
        return target;
    }

    public String getMappedRefPlayerColor() {
        return color;
    }

    @Override
    public String toString() {
        return "current " + this.current.toString() + " home " + this.home.toString() + " goto "
                + this.target.toString() + " color " + this.color;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof MappedRefPlayer)) {
            return false;
        }

        MappedRefPlayer toCompare = (MappedRefPlayer) o;
        return this.current.equals(toCompare.getCurrent()) && this.home.equals(toCompare.getHome()) &&
                this.target.equals(toCompare.getTarget()) && this.color.equals(toCompare.getMappedRefPlayerColor());

    }
}
