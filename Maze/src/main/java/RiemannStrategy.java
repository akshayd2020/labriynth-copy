import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

/**
 * The strategy for a Riemann play
 */
public class RiemannStrategy extends AbstractStrategy {

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
          return ans;
     }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RiemannStrategy)) {
            return false;
        }

        return true;

    }

    @Override
    public int hashCode() {
        return ("Riemann").hashCode();
    }
}