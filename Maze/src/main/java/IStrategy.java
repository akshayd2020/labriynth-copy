import javafx.util.Pair;

import java.util.Optional;

public interface IStrategy {

    /**
     * Compute the information to play one turn.
     * If a play cannot be made, return an Optional.empty() which represents a pass.
     * @param currPos       the current position
     * @param targetPos     the target position
     * @param board         the board
     * @param spare         the spare tile
     * @param lastAction    the last action to check against so that we do not undo this in our resulting action
     * @return              the information to play one turn
     */
    public Optional<PlayInfo> computePlayInfo(Position currPos, Position targetPos,
                                              Board board, Tile spare, Optional<SlidingAction> lastAction);

    public Position getCurrPos();
    public Position getGoalPos();
    public Board getBoard();
    public Tile getSpare();
    public Optional<SlidingAction> getLastAction();
}