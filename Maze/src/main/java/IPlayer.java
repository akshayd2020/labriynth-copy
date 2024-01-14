import java.util.Optional;

import java.awt.Color;

/**
 * A player in the game of Labyrinth
 */
public interface IPlayer {
    public String getName();

    /**
     * Compute a board given the number of rows and columns
     * @param rows      number of rows
     * @param columns   number of columns
     * @return          a board
     */
    public Board proposeBoard(int rows, int columns);

    /**
     * Sets this player up with the given game state information and the target to get to
     * @param limitedGameState  the game state that is visible to the public
     * @param target            target position to visit next
     */
    public void setup(Optional<LimitedGS> limitedGameState, Position target);


    /**
     * Get the move that can be made for this player given the current state of the game. If no move can
     * be made, pass.
     * @param state     current state of the game.
     * @return          play information that represents a turn. If no move can be made, return an Optional.empty()
     *                  representing a pass.
     */
    public Optional<PlayInfo> takeTurn(LimitedGS state);

    /**
     * Inform the player of the status of whether it has won or not.
     * @param winStatus     status of whether this player has won.
     */
    public void won(boolean winStatus);
}
