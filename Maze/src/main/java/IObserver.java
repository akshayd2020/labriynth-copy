import java.io.IOException;
/**
 * Represents an observer of one game of labyrinth
 */
public interface IObserver {

    /**
     * Get the current game state that this observer is observing.
     * @return      the current game state.
     */
    public GameState getCurrentGameState();


    public boolean isGameOver();


    /**
     * Informs this observer that there is a state change
     * @param newState     new state
     */
    public void informNewState(GameState newState);

    /**
     * Save the current state into a file specified by the filepath
     * @param filePath      file path to save into
     */
    public void saveCurrentState(String filePath) throws IOException;

    /**
     * Increments the current state to the next state if the new index is within the size of the game states.
     */
    public void safeIncrementNextState();

    /**
     * Alerts this observer that the current game is over
     */
    public void gameOver();
}
