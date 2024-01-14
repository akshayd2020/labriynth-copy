import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

/**
 * The strategy for a Euclid play
 */
public class EuclidStrategy extends AbstractStrategy {

     /**
      * Get the list of alternative candidates in row-column order to reach from the current position
      * @return     alternative candidates in row-column order
      */
     @Override
     protected List<Position> getAlternativeCandidates() {
         List<Position> ans = new ArrayList<>();
         for (int i = 0; i < this.getBoard().getNumRows(); i += 1) {
             for (int j = 0; j < this.getBoard().getNumColumns(); j += 1) {
                 boolean notGoal = !(new Position(i, j).equals(this.getGoalPos()));
                 if (notGoal) {
                     ans.add(new Position(i, j));
                 }
             }
         }
         Collections.sort(ans, new Comparator<Position>() {
             @Override
             public int compare(Position p1, Position p2) {
                 double epsilon = 0.000001d;
                 double d1 = calculatedDistance(p1);
                 double d2 = calculatedDistance(p2);
                 if (Math.abs(d1 - d2) < epsilon) {
                     if (p1.getRow() != p2.getRow()) {
                         return (int) p1.getRow()-p2.getRow();
                     }
                     return (int) p1.getColumn()-p2.getColumn();
                 } else {
                     if (d1 < d2) {
                         return -1;
                     } else {
                         return 1;
                     }
                 }
             }
         });
         return ans;
     }

    private double calculatedDistance(Position p) {
        double dX = (double) p.getRow() - this.getGoalPos().getRow();
        double dY = (double) p.getColumn() - this.getGoalPos().getColumn();
        return Math.sqrt((dX * dX) + (dY * dY));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EuclidStrategy)) {
            return false;
        }

        return true;

    }

    @Override
    public int hashCode() {
        return ("Euclid").hashCode();
    }
}