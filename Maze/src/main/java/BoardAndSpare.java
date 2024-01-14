import org.jgrapht.alg.util.UnorderedPair;

public class BoardAndSpare {
     private Board board;
     private Tile spare;

     public BoardAndSpare(Board board, Tile spare) {
          this.board = board;
          this.spare = spare;
     }

     public Board getBoard() {
          return new Board(this.board.getGridDeepCopy());
     }

     public Tile getSpare() {
          return this.spare.getDeepCopy();
     }

     /**
      * Generates a random board with a deterministic spare
      * @param numRows        number of rows to make the generated board
      * @param numColumns     number of columns to make the generated board
      * @return               returns a board with the given dimensions and a valid spare
      */
     public static BoardAndSpare generateBoardAndSpare(int numRows, int numColumns) {
          return new BoardAndSpare(Board.generateBoard(numRows, numColumns),
                  new Tile(new UnorderedPair(Gem.BERYL, Gem.ZIRCON), new Connector(Unicode.CORNER, Orientation.ZERO)));
     }

     @Override
     public boolean equals(Object o) {
          if (!(o instanceof BoardAndSpare)) {
               return false;
          }
          BoardAndSpare toCompare = (BoardAndSpare) o;
          if (!this.board.equals(toCompare.getBoard()) ||
              !this.spare.equals(toCompare.getSpare())) {
               return false;
          }
          return true;
     }

     @Override
     public int hashCode() {
          return this.board.hashCode() + this.spare.hashCode();
     }
}