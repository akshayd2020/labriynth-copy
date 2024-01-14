import com.google.gson.JsonElement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MappedRefGS2 extends MappedRefGS{
  private MappedCoordinate[] goals;

  public MappedRefGS2(MappedBoard board, MappedTile spare, MappedCoordinate[] goals,
      MappedRefPlayer[] plmt, JsonElement last) {
    super(board, spare, plmt, last);
    this.goals = goals;
  }

  public MappedCoordinate[] getMappedGoals() {
    return this.goals;
  }

  public Queue<Position> getGoals() throws IllegalArgumentException {
    Queue<Position> goalPositions = new LinkedList<>();
    for (MappedCoordinate goal: goals) {
      goalPositions.add(goal.getPosition());
    }
    return goalPositions;
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof MappedRefGS)) {
      return false;
    }

    MappedRefGS2 toCompare = (MappedRefGS2) o;
    return this.board.equals(MappedBoard.boardToMappedBoard(toCompare.getBoard()))
        && this.spare.equals(MappedTile.tileToMappedTile(toCompare.getSpare()))
        && Arrays.deepEquals(this.getPlmt(), toCompare.getPlmt())
        && this.last.equals(toCompare.getLast())
        && Arrays.deepEquals(this.goals, toCompare.getMappedGoals());
  }

  @Override
  public int hashCode() {
    return (this.board.toString() + "," +  this.spare.toString() + "," + this.getPlmt().toString() + this.last.toString() + this.goals.toString()).hashCode();
  }

  public GameState getGameState(List<IPlayer> players) {
    return new GameState(this.getSpare(), this.getBoard(), this.getPlayerOrder(players), this.getLastAction(), this.getGoals());
  }

  public GameStateWithoutIPlayers getGameStateWithoutIPlayers() {
    return new GameStateWithoutIPlayers(this.getSpare(), this.getBoard(),
        this.getPlayerOrderWithoutIPlayers(), this.getLastAction(), this.getGoals());
  }
}
