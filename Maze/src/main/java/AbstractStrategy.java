import java.util.Optional;
import java.util.List;

import javafx.util.Pair;

public abstract class AbstractStrategy implements IStrategy {
    private Position currPos;
    private Position goalPos;
    private Board board;
    private Tile spare;
    private Optional<SlidingAction> lastAction;

    public Position getCurrPos() {
        return currPos;
    }

    public Position getGoalPos() {
        return goalPos;
    }

    public Board getBoard() {
        return new Board(board.getGridDeepCopy());
    }

    public Tile getSpare() {
        return spare.getDeepCopy();
    }

    public Optional<SlidingAction> getLastAction() {
        return lastAction;
    }

    public void setCurrentPos(Position pos) {
        this.currPos = pos;
    }

    public void setTargetPos(Position pos) {
        this.goalPos = pos;
    }

    public void setBoard(Board b) {
        this.board = b;
    }

    public void setSpare(Tile s) {
        this.spare = s;
    }
    public void setLastAction(Optional<SlidingAction> lastAction) {
        this.lastAction = lastAction;
    }

    public Optional<PlayInfo> computePlayInfo(Position currPos, Position goalPos, Board board, Tile spare,
                                                       Optional<SlidingAction> lastAction) {

        setCurrentPos(currPos);
        setTargetPos(goalPos);
        setBoard(board);
        setSpare(spare);
        setLastAction(lastAction);
        return computePlayInfoWithCandidates(getAlternativeCandidates());
    }

    protected abstract List<Position> getAlternativeCandidates();

    protected Optional<PlayInfo> computePlayInfoWithCandidates(List<Position> candidates) {
        if (this.currPos.equals(this.goalPos)) {
            return getInfoForAlternatives(candidates);
        }

        Optional<PlayInfo> reachGoalInfo = getInfoWithoutShifting(this.goalPos);
        if (reachGoalInfo.isPresent()) {
            return reachGoalInfo;
        }
        return getInfoForAlternatives(candidates);
    }


    /**
     * Get the play information if the target is reachable via a slide and shift
     * @return          optional play information
     */
    private Optional<PlayInfo> getInfoIfTargetReachable(Position target) {
        return getInfoWithoutShifting(target);
    }

    /**
     * Get play information if the target is reachable from the current position without shifting other row columns first
     * @return          play information to get the target without shifting rows or columns first
     */
    private Optional<PlayInfo> getInfoWithoutShifting(Position target) {
        return findInfoFromIndices(board.getMoveable(), target);
    }

    private Direction[] getDirArr(boolean isRow) {
        if (isRow) {
            return new Direction[]{Direction.LEFT, Direction.RIGHT};
        }
        return new Direction[]{Direction.UP, Direction.DOWN};
    }

    /**
     * Get information for a specified play if exists
     * @param idx         index to move
     * @param target    target position
     * @param dirArr    directions to move
     * @return          play information if moving the specified indexes and rotating in a direction results
     *                  in being able to reach the target
     */
    private Optional<PlayInfo> getInfoForSpecifiedMove(int idx, Position target, Direction[] dirArr) {
        for (Direction dir : dirArr) {
            if (Rules.getInstance().doesActionUndoLastAction(new SlidingAction(idx, dir), this.lastAction)) {
                continue;
            }
            for (Orientation orientation : Orientation.values()) {
                int degree = orientation.getDegree();
                BoardAndSpare bs = this.board.slideAndInsert(this.spare.rotateTile(degree), idx, dir);
                Position positionAfterSlide = this.currPos;
                if (shouldMovePosition(this.currPos, idx, dir)) {
                    positionAfterSlide = this.board.safelyUpdatesPosition(this.currPos, dir);
                }
                if (bs.getBoard().isReachable(positionAfterSlide, target)
                        && !positionAfterSlide.equals(target)) {
                    return Optional.of(new PlayInfo(idx, dir, degree, target));
                }
            }
        }
        return Optional.empty();
    }

    private boolean shouldMovePosition(Position position, int index, Direction direction) {
        switch(direction) {
            case LEFT:
            case RIGHT:
                return position.getRow() == index;
            default:
                return position.getColumn() == index;
        }
    }

    /**
     * Adds rows and columns if adjacent to the given direction to the list that contains rows and columns to shift
     * and is moveable
     * @param targetRow         target row
     * @param targetColumn      target column
     * @param toShift           list of rows and columns to shift
     * @param dir               the open direction
     */
    private void addIndexToShift(int targetRow, int targetColumn, List<Pair<Boolean, Integer>> toShift, Direction dir) {
        boolean isRow = Direction.isHorizontalDirection(dir);
        if (dir == Direction.LEFT && this.board.isWithinRange(new Position(targetRow, targetColumn -1))
                && this.board.isMoveableIndex(isRow, targetColumn -1)) {
            toShift.add(new Pair(false, targetColumn -1));
        }
        if (dir == Direction.RIGHT && this.board.isWithinRange(new Position(targetRow, targetColumn +1))
                && this.board.isMoveableIndex(isRow, targetColumn +1)) {
            toShift.add(new Pair(false, targetColumn +1));
        }
        if (dir == Direction.UP && this.board.isWithinRange(new Position(targetRow -1, targetColumn))
                && this.board.isMoveableIndex(isRow, targetRow -1)) {
            toShift.add(new Pair(true, targetRow -1));
        }
        if (dir == Direction.DOWN && this.board.isWithinRange(new Position(targetRow +1, targetColumn))
                && this.board.isMoveableIndex(isRow, targetRow +1)) {
            toShift.add(new Pair(true, targetRow +1));
        }
    }


    /**
     * Given list of moveable indexes, return play information to reach the target if it exists
     * @param indexesToShift    pairs (if is row, index) representing rows/columns that need to be shifted
     * @param target            target to reach
     * @return                  moveable indexes, return play information to reach the target if it exists
     */
    private Optional<PlayInfo> findInfoFromIndices(List<Pair<Boolean, Integer>> indexesToShift, Position target) {
        for (Pair<Boolean, Integer> index : indexesToShift) {
            Direction[] dirArr = getDirArr(index.getKey());
            Optional<PlayInfo> playInfo = getInfoForSpecifiedMove(index.getValue(), target, dirArr);
            if (playInfo.isPresent()) {
                return playInfo;
            }
        }
        return Optional.empty();
    }

    /**
     * Return play information if this current position can reach any of the candidates
     * @param candidates    the alternative candidates to reach
     * @return  play information if a candidate is reachable
     */
    private Optional<PlayInfo> getInfoForAlternatives(List<Position> candidates) {
        for (Position candidate : candidates) {
            Optional<PlayInfo> reachCandidateInfo = getInfoIfTargetReachable(candidate);
            if (reachCandidateInfo.isPresent()) {
                return reachCandidateInfo;
            }
        }
        return Optional.empty();
    }
}