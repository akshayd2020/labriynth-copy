import org.jgrapht.alg.util.UnorderedPair;

import java.util.Arrays;

/**
 * Used to map JSON objects representing a board and a board to a JSON object
 */
public class MappedBoard {
    String[][] connectors;
    String[][][] treasures;

    public MappedBoard(String[][] connectors, String[][][] treasures) {
        this.connectors = connectors;
        this.treasures = treasures;
    }

    public Board getBoard() {
        return new Board(createGrid());
    }

    /**
     * Create a grid of tiles with this mapped board's information
     * @return  grid of tiles with this mapped board's information
     */
    private Tile[][] createGrid() {
        int numRows = connectors.length;
        int numCols = connectors[0].length;

        Tile[][] result = new Tile[numRows][numCols];
        for (int row = 0; row < numRows; row += 1) {
            for (int col = 0; col < numCols; col += 1) {
                String[] currTreasure = treasures[row][col];
                String currConnector = connectors[row][col];
                result[row][col] = new Tile(getTreasure(currTreasure), getConnector(currConnector));
            }
        }
        return result;
    }

    private UnorderedPair<Gem, Gem> getTreasure(String[] arr) {
        Gem gem1 = Gem.getGem(arr[0]);
        Gem gem2 = Gem.getGem(arr[1]);
        return new UnorderedPair(gem1, gem2);
    }

    /**
     * Get a connector given a string
     * @param str   string to convert to a connector
     * @return      connector
     */
    private Connector getConnector(String str) {
        char unicode = str.charAt(0);
        return Connector.getCharToConnector().get(unicode);
    }

    public String[][] getConnectors() {
        return this.connectors;
    }

    public String[][][] getTreasures() {
        return this.treasures;
    }

    /**
     * Get a board representation that will be used to convert to JSON from the given board
     * @param board the board
     * @return      a board representation that will be used to convert to JSON
     */
    public static MappedBoard boardToMappedBoard(Board board) {
        String[][] connectors = board.getConnectorsAsStringArray();
        String[][][] treasures = board.getTreasuresAsStringArray();

        return new MappedBoard(connectors, treasures);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MappedBoard)) {
            return false;
        }
        MappedBoard boardToCompare = (MappedBoard) o;
        return Arrays.deepEquals(this.connectors, boardToCompare.getConnectors())
                && Arrays.deepEquals(this.treasures, boardToCompare.getTreasures());
    }

}