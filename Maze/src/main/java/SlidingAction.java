import java.util.Objects;

/**
 * Represents a sliding action performed by a player, including the index and direction
 * that was slid on the board.
 */
public class SlidingAction {
    private final int index;
    private final Direction direction;

    public SlidingAction(int index, Direction direction) {
        this.index = index;
        this.direction = direction;
    }

    public int getIndex() {
        return index;
    }

    public Direction getDirection() {
        return direction;
    }

    /**
     * Determines if the given action undoes this action.
     * @param other the action to compare with this action.
     * @return      true if the given action undoes this action, false otherwise.
     */
    public boolean isUndoneBy(SlidingAction other) {
        Direction oppositeDirection = Direction.getOpposite(this.getDirection());
        return other.getIndex() == this.getIndex() && other.getDirection() == oppositeDirection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SlidingAction)) {
            return false;
        }
        SlidingAction other = (SlidingAction) o;
        return index == other.index && direction == other.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, direction);
    }
}
