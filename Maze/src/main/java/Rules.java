import java.util.Optional;

/**
 * Represents the rule book of a game of Labyrinth
 */
public class Rules {
    private static Rules instance = new Rules();

    private Rules(){}

    public static Rules getInstance() {
        return instance;
    }

    // Only even-indexed rows can be moved
    public boolean isMovableRow(int rowIndex) {
        return rowIndex % 2 == 0;
    }

    // Only even-indexed columns can be moved
    public boolean isMovableColumn(int columnIndex) {
        return columnIndex % 2 == 0;
    }

    /**
     * Returns whether a play is valid. A play is invalid if:
     * - slide and insert undoes the last move
     * - moving an immoveable row/column
     * - rotating the tile not a multiple of 90
     * - not moving to a new position while taking a turn (not a pass)
     * - moving to an unreachable or invalid position
     * @param play          play information.
     * @param gameState     the state of the game.
     * @return              whether this play is valid.
     */
    public boolean isValidMove(PlayInfo play, GameState gameState) {
        Position currentPos = gameState.getActivePlayer().getCurrentPosition();
        Position positionAfterSlide = getPositionAfterSlide(currentPos, gameState, play);
        Board board = gameState.getBoard();
        Position newPosition = play.getNewPosition();
        int degrees = play.getDegreeToRotate();

        return isMovable(play, board)
                && Direction.isValidDegreeToRotateBy(degrees)
                && !newPosition.equals(positionAfterSlide)
                && board.isWithinRange(newPosition)
                && canReachNewPosition(play, gameState)
                && !doesPlayUndoLastAction(play, gameState.getLastAction());
    }

    // Returns the new position after the slide in the given PlayInfo.
    private Position getPositionAfterSlide(Position position, GameState gameState, PlayInfo play) {
        switch (play.getDirection()) {
            case RIGHT:
            case LEFT:
                return position.getRow() == play.getIndex()
                        ? gameState.getBoard().safelyUpdatesPosition(position, play.getDirection())
                        : position;
            default:
                return position.getColumn() == play.getIndex()
                        ? gameState.getBoard().safelyUpdatesPosition(position, play.getDirection())
                        : position;
        }
    }

    /**
     * Return whether the given play information undoes the given last action.
     * @param play          play information.
     * @param lastAction    the last action.
     * @return              whether the given play information undoes the last action.
     */
    public boolean doesPlayUndoLastAction(PlayInfo play, Optional<SlidingAction> lastAction) {
        return doesActionUndoLastAction(play.getSlidingAction(), lastAction);
    }

    /**
     * Return whether the given play information undoes the given last action.
     * @param action        the action that may or may not undo the last action.
     * @param lastAction    the last action.
     * @return              whether the given play information undoes the last action.
     */
    public boolean doesActionUndoLastAction(SlidingAction action, Optional<SlidingAction> lastAction) {
        if (lastAction.isEmpty()) {
            return false;
        }
        return lastAction.get().isUndoneBy(action);
    }

    /**
     * Can this play information move to the specified new position after rotating the spare and
     * sliding and inserting the spare?
     * @param play          the play information to take.
     * @param gameState     the state of the game.
     * @return              whether the new position is reachable after rotating the spare,
     *                      sliding the row/column, and inserting it.
     */
    private boolean canReachNewPosition(PlayInfo play, GameState gameState) {
        GameState testCanReach = gameState.performAction(new RotateTile(play.getDegreeToRotate(), gameState));
        testCanReach = testCanReach.performAction(new SlideAndInsert(play.getIndex(), play.getDirection(), testCanReach));
        return testCanReach.canReach(play.getNewPosition());
    }

    private boolean isMovable(PlayInfo play, Board board) {
        boolean isRow = Direction.isHorizontalDirection(play.getDirection());
        return board.isMoveableIndex(isRow,play.getIndex());
    }
}
