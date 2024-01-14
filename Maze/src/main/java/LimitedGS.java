import java.util.Optional;
import java.util.Queue;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import java.awt.Color;

import javafx.util.Pair;

/**
 * Game state visiblw to an individual player
 */
public class LimitedGS {
    private Tile spare;
    private Board board;
    private Queue<LimitedPlayerState> playerOrder;
    private Optional<SlidingAction> lastAction;

    public LimitedGS(Tile spare, Board board, Queue<LimitedPlayerState> playerOrder, Optional<SlidingAction> lastAction) {
        if (board.getAllGemPairs().contains(spare.getGems())) {
            throw new IllegalArgumentException("Board contains the spare's pair of gems");
        }
        this.spare = spare;
        this.board = board;
        this.playerOrder = playerOrder;
        this.lastAction = lastAction;
    }

    public Tile getSpare() {
        return new Tile(this.spare.getGems(), this.spare.getConnector());
    }

    public Board getBoard() {
        return new Board(board.getGridDeepCopy());
    }


    /**
     * Get the current position of the active player
     * @return  the current position of the active player
     */
    public Position getCurrentPosition() {
        return playerOrder.peek().getCurrent();
    }

    public Queue<LimitedPlayerState> getPlayerOrder() {
        return playerOrder;
    }


    public Optional<SlidingAction> getLastAction() {
        return lastAction;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LimitedGS)) {
            return false;
        }
        LimitedGS toCompare = (LimitedGS) o;

        return this.spare.equals(toCompare.getSpare()) && this.board.equals(toCompare.getBoard())
                && this.playerOrder.equals(toCompare.getPlayerOrder())
                && this.lastAction.equals(toCompare.getLastAction());
    }
}