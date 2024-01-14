import java.util.Map;
import java.util.Optional;

import java.awt.Color;
import java.util.Queue;

import javafx.util.Pair;

/**
 * Represents validation and action of sliding a row or a column and inserting a tile in a game of
 * Labyrinth.
 */
public class SlideAndInsert implements IAction {
     final int index;
     final Direction direction;
     final GameState gameState;

     public SlideAndInsert(int index, Direction direction, GameState gameState) {
          this.index = index;
          this.direction = direction;
          this.gameState = gameState;
     }

     /**
      * Return a new game state after sliding and inserting the game state's spare tile
      */
     public GameState perform() {
          checkValid();
          BoardAndSpare bs = this.gameState.getBoard().slideAndInsert(this.gameState.getSpare(), this.index,
                  this.direction);
          Queue<PlayerState> updatedPlayerStates =
              this.gameState.movePlayersOnShift(this.index, this.direction);

          GameState newGs = new GameState(bs.getSpare(), bs.getBoard(), updatedPlayerStates,
                  Optional.of(new SlidingAction(index, direction)), gameState.getGoalsToDistribute());
          return newGs;
     }


     /**
      * Throws an error if any parameters are not valid
      */
     private void checkValid() {
          checkBounds();
          checkMoveableIndex();
     }

     private void checkMoveableIndex() {
          if (this.index % 2 != 0) {
               throw new IllegalArgumentException("Attempted to slide invalid row or column");
          }
     }

     private void checkBounds() throws IllegalArgumentException {
          Tile[][] grid = this.gameState.getBoard().getGrid();
          boolean isRow = this.direction == Direction.LEFT || this.direction == Direction.RIGHT;
          if ((isRow && (0 > this.index || this.index >= grid.length)) ||
              (!isRow && (0 > this.index || this.index >= grid[0].length))) {
               throw new IllegalArgumentException("Index is out of bounds to slide");
          }
     }
}