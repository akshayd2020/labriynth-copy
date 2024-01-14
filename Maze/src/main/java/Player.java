import java.util.Optional;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.jgrapht.alg.util.UnorderedPair;

public class Player implements IPlayer {
     private String name;
     private Optional<Position> target;
     private IStrategy strategy;

     // whether this player won their current game
     private boolean won;

     public Player(String name, IStrategy strategy) {
          this.name = name;
          this.strategy = strategy;
          this.won = false;
          this.target = Optional.empty();
     }

     public Player(String name, Position target, IStrategy strategy, boolean won) {
          this.name = name;
          this.target = Optional.of(target);
          this.strategy = strategy;
          this.won = won;
     }

     public void setStrategy(IStrategy strategy) {
          this.strategy = strategy;
     }

     public IStrategy getStrategy() {
          return this.strategy;
     }

     public Optional<Position> getTarget() {
          return this.target;
     }

     public String getName() {
          return this.name;
     }

     public boolean getWon() {
          return this.won;
     }

     /**
      * Compute a board given the number of rows and columns
      * @param rows      number of rows
      * @param columns   number of columns
      * @return          a board
      */
     public Board proposeBoard(int rows, int columns) {
          return Board.generateBoard(rows, columns);
     }

     /**
      * Sets this player up with the given game state information and the target to get to
      * @param limitedGS the game state that is visible to the public
      * @param target    target position to visit next
      */
     public void setup(Optional<LimitedGS> limitedGS, Position target) {
          this.target = Optional.of(target);
     }

     /**
      * Get the move that can be made for this player given the current state of the game. If no move can
      * be made, pass.
      * @param state     current state of the game.
      * @return          play information that represents a turn. If no move can be made, return an Optional.empty()
      *                  representing a pass.
      */
     public Optional<PlayInfo> takeTurn(LimitedGS state) {
          if (this.getTarget().isEmpty()) {
               throw new IllegalStateException("A player should not be taking a turn " +
                       "if they have not been setup with a target position to get to");
          }
          return this.strategy.computePlayInfo(state.getCurrentPosition(), this.getTarget().get(),
                  state.getBoard(), state.getSpare(), state.getLastAction());
     }

     /**
      * Inform the player of the status of whether it has won or not?
      * @param w     status of whether this player has won
      */
     public void won(boolean w) {
          this.won = w;
     }

     /**
      * Return whether this player won the game
      * @return  whether this player won the game
      */
     public boolean didWin() {
          return this.won;
     }
     @Override
     public boolean equals(Object o) {
          if (!(o instanceof Player)) {
               return false;
          }

          Player toCompare = (Player) o;

          return this.name.equals(toCompare.getName())
                  && this.target.equals(toCompare.getTarget())
                  && this.strategy.equals(toCompare.getStrategy())
                  && this.won == toCompare.won;

     }

     @Override
     public String toString() {
          return "name " + this.name + " target " + this.target.toString() + " strategy " + this.strategy.toString() + " won " + Boolean.toString(this.won);
     }

     @Override
     public int hashCode() {
          return (this.name.hashCode() + this.target.hashCode() + this.strategy.hashCode()
                  + Boolean.hashCode(this.won));
     }
}