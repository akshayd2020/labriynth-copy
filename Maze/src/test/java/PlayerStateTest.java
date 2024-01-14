import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.awt.Color;
import java.util.Optional;

public class PlayerStateTest {
     Color green;
     Position homePos;
     Position goalPos;
     Position currentPos;
     PlayerState playerStateOne;
     IPlayer playerOne;

     @BeforeEach
     public void setUp() {
          green = Color.GREEN;
          homePos = new Position(1, 1);
          goalPos = new Position(5, 5);
          currentPos = new Position(1, 1);
          playerOne = new Player("Test Player", new RiemannStrategy());
          playerStateOne = new PlayerState(green, homePos, goalPos, currentPos, 0, playerOne);
     }

     @Test
     public void testSetCurrentPosAndGoalReached() {
          playerStateOne.setCurrentPosition(goalPos);
          assertEquals(goalPos, playerStateOne.getCurrentPosition(), "Current position was set "
              + "incorectly");
          assertFalse(playerStateOne.isPlayerGoingHome(), "Goal position was updated to true: the goal position should NOT be updated by setCurrentPosition");
     }

     @Test
     public void passPlayerStateEquals() {
          assertTrue(playerStateOne.equals(new PlayerState(
              green,
              homePos, goalPos, currentPos, 0, playerOne)));
     }

     //start region playerstate deepcopy()
     @Test
     public void playerStateDeepCopy() {
          assertEquals(new PlayerState(green, homePos, goalPos, currentPos, 0, playerOne), playerStateOne.getDeepCopy());
     }

     // Tests the functionality of a PlayerState being given a new intermediary goal, where the
     // number of goals reached and the intermediary goal is updated appropriately.
     @Test
     public void testPlayerStateIntermediaryGoals() {
          assertEquals(goalPos, playerStateOne.getGoalPosition());
          assertFalse(playerStateOne.isPlayerGoingHome());
          assertEquals(goalPos, playerStateOne.getGoalPosition());
          assertEquals(0, playerStateOne.getNumberOfGoalsReached());

          playerStateOne.giveNewGoal(new Position(3, 5));

          assertEquals(new Position(3,5), playerStateOne.getGoalPosition());
          assertFalse(playerStateOne.isPlayerGoingHome());
          assertEquals(new Position(3,5), playerStateOne.getGoalPosition());
          assertEquals(1, playerStateOne.getNumberOfGoalsReached());

          playerStateOne.giveNewGoal(playerStateOne.getHomePosition());

          assertEquals(playerStateOne.getHomePosition(), playerStateOne.getGoalPosition());
          assertTrue(playerStateOne.isPlayerGoingHome());
          assertEquals(homePos, playerStateOne.getGoalPosition());
          assertEquals(2, playerStateOne.getNumberOfGoalsReached());
     }

}
