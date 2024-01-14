import java.util.Map;
import java.awt.Color;
/**
 * Represents validation and action of moving a player in a game of Labyrinth.
 */
public class MovePlayer implements IAction {
     Position position;
     GameState gameState;

     public MovePlayer(Position position, GameState gameState) {
          this.gameState = gameState;
          this.position = position;
     }

     /**
      * Performs this action
      */
     public GameState perform() {
          checkValid();
          PlayerState activePlayer = this.gameState.getActivePlayer();
          activePlayer.setCurrentPosition(this.position);
          return new GameState(gameState.getSpare(), gameState.getBoard(), gameState.getPlayerOrder(),
                  this.gameState.getLastAction(), this.gameState.getGoalsToDistribute());
     }


     /**
      * Throws an error if the position is not within range to move
      * @return whether this action is valid
      */
     private void checkValid() {
          if (!this.gameState.getBoard().isWithinRange(this.position)) {
               throw new IllegalArgumentException("Cannot move to a position out of bounds");
          }
     }

     /**
      * Return the given Color if it is valid
      * Color is one of:
      *
      *      - a String that matches the regular expression
      *
      *         "^[A-F|\d][A-F|\d][A-F|\d][A-F|\d][A-F|\d][A-F|\d]$"
      *
      *      - "purple",
      *
      *      - "orange",
      *
      *      - "pink",
      *
      *      - "red",
      *
      *      - "blue",
      *
      *      - "green",
      *
      *      - "yellow",
      *
      *      - "white",
      *
      *      - "black".
      * @param s        string that represents a color
      * @return         valid color
      */
     private Color returnValidColor(String s) throws IllegalArgumentException {
          try {
               return Color.decode(s);
          } catch (NumberFormatException nfe) {
               switch(s) {
                    case "purple":
                         return Color.MAGENTA;
                    case "orange":
                         return Color.ORANGE;
                    case "pink":
                         return Color.PINK;
                    case "red":
                         return Color.RED;
                    case "blue":
                         return Color.BLUE;
                    case "green":
                         return Color.GREEN;
                    case "yellow":
                         return Color.YELLOW;
                    case "white":
                         return Color.WHITE;
                    case "black":
                         return Color.BLACK;
                    default:
                         throw new IllegalArgumentException("Color is invalid " + s);
               }
          }
     }
}