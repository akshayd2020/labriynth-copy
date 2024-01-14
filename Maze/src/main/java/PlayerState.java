import java.awt.Color;
import java.util.Optional;

public class PlayerState {
    private Color avatar;
    private Position home;
    private Position goal;
    private Position current;
    private IPlayer player;
    private int numberOfGoalsReached;

    // Constructor that defaults the number of goals reached to 0.
    public PlayerState(Color avatar, Position home, Position goal,
                       Position current, IPlayer player) {
        this(avatar, home, goal, current, 0, player);
    }

    public PlayerState(Color avatar, Position home, Position goal,
                       Position current, int numberOfGoalsReached, IPlayer player) {
        this.avatar = avatar;
        this.home = home;
        this.goal = goal;
        this.current = current;
        this.numberOfGoalsReached = numberOfGoalsReached;
        this.player = player;
    }

    // Copy constructor
    public PlayerState(PlayerState other) {
        this.avatar = other.getAvatar();
        this.home = other.getHomePosition();
        this.goal = other.getGoalPosition();
        this.current = other.getCurrentPosition();
        this.numberOfGoalsReached = other.numberOfGoalsReached;
        this.player = other.player;
    }

    public Color getAvatar() {
        return this.avatar;
    }
    public IPlayer getPlayer() {
        return this.player;
    }

    /**
     * Determines if the player's next target goal is their home.
     * This does NOT necessarily mean the player will win when reaching their home,
     * just that the player's goal is the same tile as their home.
     * @return  true if the player is heading to their home tile, false otherwise.
     */
    public boolean isPlayerGoingHome() {
        return this.goal.equals(this.home);
    }

    /**
     * Get the home position of a player in a game
     * @return  the home position
     */
    public Position getHomePosition() {
        return this.home;
    }

    /**
     * Get the player's next target goal. If the intermediary goal is empty, the player
     * must go home, so this will return their home position.
     * @return  the next goal position.
     */
    public Position getGoalPosition() {
        return this.goal;
    }

    /**
     * Gives this player a new goal, updating the number of goals this
     * player has reached so far.
     * @param newGoal   the new goal to give this player.
     */
    public void giveNewGoal(Position newGoal) {
        this.goal = newGoal;
        numberOfGoalsReached++;
    }

    public int getNumberOfGoalsReached() {
        return this.numberOfGoalsReached;
    }

    /**
     * Get the current position of a player in a game
     * @return  the current position
     */
    public Position getCurrentPosition() {
        return this.current;
    }

    /**
     * Set the current position of a player
     */
    public void setCurrentPosition(Position pos) {
        this.current = pos;
    }

    public PlayerState getDeepCopy() {
        return new PlayerState(this.avatar, new Position(this.home.getRow(), this.home.getColumn()),
            this.getGoalPosition(), new Position(this.current.getRow(), this.current.getColumn()),
                this.numberOfGoalsReached, this.player);
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlayerState)) {
            return false;
        }
        PlayerState toCompare = (PlayerState) o;
        return this.avatar.equals(toCompare.getAvatar())
                && this.home.equals(toCompare.getHomePosition())
                && this.goal.equals(toCompare.getGoalPosition())
                && this.current.equals(toCompare.getCurrentPosition())
                && this.numberOfGoalsReached == toCompare.getNumberOfGoalsReached()
                && this.player.equals(toCompare.getPlayer());
    }

    @Override
    public String toString() {
        return this.avatar.toString() + " home: " + this.home.toString() + " goal: " + this.goal.toString()
                        + " current: " + this.current.toString() + " " + this.numberOfGoalsReached;
    }

    @Override
    public int hashCode() {
        return (
            this.avatar.toString() +
                this.home.toString() + "," + this.goal.toString()
            + "," + this.current.toString()).hashCode() + Integer.hashCode(this.numberOfGoalsReached)
                + this.player.hashCode();
    }
}