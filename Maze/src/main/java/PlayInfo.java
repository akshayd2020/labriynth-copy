/**
 * Represents a non-pass action made by a player where
 * - the index is the row/column index to move
 * - the direction is the direction to move the specified row/column
 * - degree is the number of degrees to turn
 * - the position to move to
 */
public class PlayInfo {
     private SlidingAction slidingAction;
     private int deg;
     private Position pos;

     public PlayInfo(int idx, Direction dir, int deg, Position pos) {
          this.slidingAction = new SlidingAction(idx, dir);
          this.deg = deg;
          this.pos = pos;
     }

     public PlayInfo(SlidingAction slidingAction, int deg, Position pos) {
          this.slidingAction = slidingAction;
          this.deg = deg;
          this.pos = pos;
     }

     /**
      * Get the index of the row or column to move
      * @return     index of the row or column to move
      */
     public int getIndex() {
          return this.slidingAction.getIndex();
     }

     public Direction getDirection() {
          return this.slidingAction.getDirection();
     }

     public SlidingAction getSlidingAction() {
          return this.slidingAction;
     }

     /**
      * Get the degree to rotate the spare
      * @return     the degree to rotate the spare
      */
     public int getDegreeToRotate() {
          return this.deg;
     }

     /**
      * Return the new position that the player should move to
      * @return     the new position that the player should move to
      */
     public Position getNewPosition() {
          return new Position(this.pos.getRow(), this.pos.getColumn());
     }

     @Override
     public boolean equals(Object o) {
          if (!(o instanceof PlayInfo)) {
               return false;
          }
          PlayInfo toCompare = (PlayInfo) o;
          return (this.getIndex() == toCompare.getIndex()) &&
                  (this.getDirection() == toCompare.getDirection()) &&
                  (this.deg == toCompare.getDegreeToRotate()) &&
                  (this.pos.equals(toCompare.getNewPosition()));
     }

     @Override
     public int hashCode() {
          return (Integer.toString(this.getIndex()) + "," + this.getDirection().toString().hashCode() + ","
                  + Integer.toString(this.deg) + "," + this.pos.toString()).hashCode();
     }

     @Override
     public String toString() {
          return "index: " + Integer.toString(this.getIndex()) + " direction: " + this.getDirection().toString() +
                  " degree: " + Integer.toString(this.deg) + " position: " + this.pos.toString();
     }
}