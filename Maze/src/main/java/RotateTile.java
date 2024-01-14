/**
 * Represents validation and action of rotating the spare tile in a game of Labyrinth.
 */
public class RotateTile implements IAction {
     final Tile spare;
     final int degree;
     final GameState gameState;

     public RotateTile(int degree, GameState gameState) {
          this.spare = gameState.getSpare();
          this.degree = degree;
          this.gameState = gameState;
     }

     /**
      * Returns a new game state with a rotated spare tile
      */
     public GameState perform() {
          int initialDegree = this.spare.getConnector().getOrientation().getDegree();
          Orientation orientation = Orientation.getOrientation(this.degree + initialDegree);
          Tile rotatedSpare = new Tile(this.spare.getGems(), new Connector(this.spare.getConnector().getUnicode(), orientation));
          Board boardCopy = new Board(this.gameState.getBoard().getGridDeepCopy());
          return new GameState(rotatedSpare, boardCopy, this.gameState.getPlayerOrder(), this.gameState.getLastAction(),
                  this.gameState.getGoalsToDistribute());
     }
}