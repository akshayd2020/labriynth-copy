import org.jgrapht.alg.util.UnorderedPair;

import javafx.util.Pair;

import java.util.*;

/**
 * Board representation for a game of Labyrinth.
 */
public class Board {
    private Tile[][] grid;

    // rowLength represents the number of rows
    private int numRows;

    // columnLength represents the number of columns
    private int numColumns;

    public Board(Tile[][] grid) {
        checkGemUniqueness(grid);
        this.grid = grid;
        this.numRows = grid.length;
        this.numColumns = this.grid[0].length;
    }

    private void checkGemUniqueness(Tile[][] grid) {
        Set<UnorderedPair<Gem, Gem>> seenGems = new HashSet<>();
        for (Tile[] tileRow : grid) {
            for (Tile tile : tileRow) {
                if (seenGems.contains(tile.getGems())) {
                    throw new IllegalArgumentException("Gem pairs on tiles must be distinct from each other");
                }
                seenGems.add(tile.getGems());
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    /**
     * Slides the row assuming the row/column can be shifted and numShifts is greater than 0 and no more
     * than maximum size of the grid row/column
     * @return the updated board and the tile that slid off
     */
    public BoardAndSpare slideAndInsert(Tile tile, int index, Direction direction) {
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            return slideRowAndInsert(tile, index, direction);
        } else {
            return slideColumnAndInsert(tile, index, direction);
        }
    }

    /**
     * Slides the given row the given number of positions in the given direction
     * @param tile      tile to insert into the open spot
     * @param index     index of row to slide
     * @param direction direction to move the row
     * @return          the updated board and the tile that slid off
     */
    private BoardAndSpare slideRowAndInsert(Tile tile, int index, Direction direction) {
        if (direction == Direction.LEFT) {
            return slideLeftAndInsert(tile, index);
        } else{
            return slideRightAndInsert(tile, index);
        }
    }

    /**
     * Slides the given column the given number of positions in the given direction
     * @param tile      tile to insert into the open spot
     * @param index     index of column to slide
     * @param direction direction to move the column
     * @return          updated board and the tile that slid off
     */
    private BoardAndSpare slideColumnAndInsert(Tile tile, int index, Direction direction) {
        if (direction == Direction.UP) {
            return slideUpAndInsert(tile, index);
        } else {
            return slideDownAndInsert(tile, index);
        }
    }

    private BoardAndSpare slideLeftAndInsert(Tile tile,int row) {
        Tile[][] gridCopy = this.getGridDeepCopy();
        Tile[] updatedRow = gridCopy[row];
        Tile slidOff = updatedRow[0];
        Tile[] remainingTiles = Arrays.copyOfRange(gridCopy[row], 1, gridCopy[row].length);
        System.arraycopy(remainingTiles, 0, updatedRow, 0, remainingTiles.length);
        updatedRow[gridCopy[row].length - 1] = tile;
        return new BoardAndSpare(new Board(gridCopy), slidOff);
    }

    private BoardAndSpare slideRightAndInsert(Tile tile, int row) {
        Tile[][] gridCopy = this.getGridDeepCopy();
        Tile[] updatedRow = gridCopy[row];
        Tile slidOff = updatedRow[numColumns - 1];
        Tile[] remainingTiles = Arrays.copyOfRange(gridCopy[row], 0, numColumns - 1);
        System.arraycopy(remainingTiles, 0, updatedRow, 1, remainingTiles.length);
        updatedRow[0] = tile;
        return new BoardAndSpare(new Board(gridCopy), slidOff);

    }

    private BoardAndSpare slideUpAndInsert(Tile tile, int column) {
        Tile[][] gridCopy = this.getGridDeepCopy();
        int lengthOfGrid = gridCopy.length;
        Tile slidOff = gridCopy[0][column];
        for (int index = 0; index < gridCopy.length; index += 1) {
            if (index >= lengthOfGrid - 1) {
                gridCopy[index][column] = tile;
            } else {
                gridCopy[index][column] = gridCopy[index + 1][column];
            }
        }
        return new BoardAndSpare(new Board(gridCopy), slidOff);
    }

    private BoardAndSpare slideDownAndInsert(Tile tile, int column) {
        Tile[][] gridCopy = this.getGridDeepCopy();
        int lengthOfGrid = gridCopy.length;
        Tile slidOff = gridCopy[numRows - 1][column];
        for (int index = gridCopy.length - 1; index > -1; index -= 1) {
            if (index >=  1) {
                gridCopy[index][column] = gridCopy[index - 1][column];
            } else {
                gridCopy[index][column] = tile;
            }
        }
        return new BoardAndSpare(new Board(gridCopy), slidOff);
    }

    /**
     * Get the change in the row index that sliding in the given direction would cause.
     * @param direction the direction that the position will be moved in
     * @return          the change in the row index
     */
    private int getRowDelta(Direction direction) {
        switch(direction) {
            case UP:
                return -1;
            case DOWN:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Get the change in the column index that sliding in the given direction would cause.
     * @param direction the direction that the position will be moved in.
     * @return          the change in the column index.
     */
    private int getColumnDelta(Direction direction) {
        switch(direction) {
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Safely updates the given position. Ensures that if the new position
     * will be off this board, their new position will be on the opposite side of the row/colum they are on
     * @param position      the position to be updated after moving in the row and/or column direction
     */
    public Position safelyUpdatesPosition(Position position, Direction direction) {
        int rowDelta = getRowDelta(direction);
        int columnDelta = getColumnDelta(direction);
        int numRows = this.getNumRows();
        int numColumns = this.getNumColumns();
        int newRowIndex = ((position.getRow() + rowDelta) + numRows) % numRows;
        int newColumnIndex = ((position.getColumn() + columnDelta) + numColumns) % numColumns;
        return new Position(newRowIndex, newColumnIndex);
    }

    /**
     * Is the given current position able to reach the target position?
     * @param current   current position
     * @param target    target position
     * @return          whether or not the current position can reach the target
     */
    public boolean isReachable(Position current, Position target) {
        List<Pair<Tile, Position>> queue = new ArrayList<>();
        Tile tile = this.grid[current.getRow()][current.getColumn()];
        queue.add(new Pair(tile, current));
        Set<Position> visited = new HashSet<>(Arrays.asList(current));
        return isReachableHelper(queue, visited, target);
    }

    /**
     * Runs BFS on the given queue and visited positions until the target position is reached, in which case
     * the search is stopped immediately.
     * @param queue     tiles and their corresponding positions to visit next.
     * @param visited   positions that have been visited so far.
     * @param target    the target position to search for.
     * @return          true if the target can be reached, false otherwise.
     */
    private boolean isReachableHelper(List<Pair<Tile, Position>> queue, Set<Position> visited, Position target) {
        while (!queue.isEmpty()) {

            Pair<Tile, Position> popped = queue.remove(0);
            Tile currTile = popped.getKey();
            Position currPosition = popped.getValue();

            getConnectedNeighbors(queue, visited, currTile, currPosition);
            if (visited.contains(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a set of reachable positions including the given tile
     */
    public Set<Position> getReachableTiles(int row, int column) {
        List<Pair<Tile, Position>> queue = new ArrayList<>();
        Tile tile = this.grid[row][column];
        queue.add(new Pair(tile, new Position(row, column)));
        Set<Position> visited = new HashSet<>(Arrays.asList(new Position(row, column)));
        Set<Position> result = new HashSet<>(Arrays.asList(new Position(row, column)));
        result.addAll(getReachablePositions(queue, visited));

        return result;
    }

    /**
     * Gets list of positions that are reachable from queue items
     * @param queue     tiles and their corresponding positions to visit next
     * @param visited   positions that have been visited so far
     * @return          list of positions that are reachable from queue items
     */
    private Set<Position> getReachablePositions(List<Pair<Tile, Position>> queue, Set<Position> visited) {
        Set<Position> result = new HashSet<>();
        while (!queue.isEmpty()) {

            Pair<Tile, Position> popped = queue.remove(0);
            Tile currTile = popped.getKey();
            Position currPosition = popped.getValue();

            result.addAll(getConnectedNeighbors(queue, visited, currTile, currPosition));
        }
        return result;
    }

    /**
     * Return positions that are connected to the current tile
     * @param queue         tiles and their corresponding positions to visit next
     * @param visited       positions that have been visited so far
     * @param currTile      current tile
     * @param currPosition  current position
     * @return              positions that are connected to the current tile
     */
    private Set<Position> getConnectedNeighbors(List<Pair<Tile, Position>> queue,
        Set<Position> visited, Tile currTile, Position currPosition) {
        Set<Position> result = new HashSet<>();
        Map<Direction, Integer[]> directionToShift = getDirectionDeltas();
        Connector currConnector = currTile.getConnector();
        List<Direction> openDirections = new ArrayList<>(currConnector.getOpenDir());
        for (Direction direction : openDirections) {
            Optional<Position> neighborPos = getPosIfValidAndConnected(queue, visited,
                currTile,
                currPosition,
                directionToShift.get(direction), direction);
            if (neighborPos.isPresent()) {
                result.add(neighborPos.get());
            }
        }

        return result;
    }

    /**
     * Return a position if neighbor of current position is in range, not an empty tile, and is
     * connected from the given direction
     * @param queue             (tiles, positions) to visit
     * @param visited           positions seen so far
     * @param currTile          current tile
     * @param currPosition      current position
     * @param directionDelta    [row, column] representing delta for a direction
     * @param direction         relationship between current tile to neighbor tile
     * @return                  position representing the neighbor's position if reachable from
     *                          the current position or an empty optional
     */
    private Optional<Position> getPosIfValidAndConnected(List<Pair<Tile, Position>> queue,
        Set<Position> visited, Tile currTile, Position currPosition,
        Integer[] directionDelta, Direction direction) {
        Position neighborPos = new Position(currPosition.getRow() + directionDelta[0], 
            currPosition.getColumn() + directionDelta[1]);

        if (isWithinRange(neighborPos) && this.grid[neighborPos.getRow()][neighborPos.getColumn()].isPresent()) {
            return getPosIfConnected(queue, visited, currTile, direction, neighborPos);
        }

        return Optional.empty();
    }

    /**
     * Return a position if neighbor of current position is connected from the given direction
     * @param queue             (tiles, positions) to visit
     * @param visited           positions seen so far
     * @param currTile          current tile
     * @param direction         relationship between current tile to neighbor tile
     * @param neighborPos       position of neighbor tile
     * @return                  position representing the neighbor's position if reachable from
     *                          the current position or an empty optional
     */
    private Optional<Position> getPosIfConnected(List<Pair<Tile, Position>> queue,
        Set<Position> visited, Tile currTile, Direction direction,
        Position neighborPos) {
        Tile neighborTile = this.grid[neighborPos.getRow()][neighborPos.getColumn()];
        Connector neighborConnector = neighborTile.getConnector();
        Connector currentConnector = currTile.getConnector();
        boolean isConnected =  currentConnector.isConnected(neighborConnector, direction);
        boolean notVisited = !visited.contains(neighborPos);
        if (notVisited && isConnected) {
            queue.add(new Pair(neighborTile, neighborPos));
            visited.add(neighborPos);
            return Optional.of(neighborPos);
        }

        return Optional.empty();
    }

    public boolean isWithinRange(Position position) {
        int row = position.getRow();
        int col = position.getColumn();
        return 0 <= row && row < this.grid.length && 0 <= col && col < this.grid[row].length;
    }

    private Map<Direction, Integer[]> getDirectionDeltas() {
        Map<Direction, Integer[]> directionToShift = new HashMap<>();
        directionToShift.put(Direction.DOWN, new Integer[]{ 1, 0 });
        directionToShift.put(Direction.UP, new Integer[]{ -1, 0 });
        directionToShift.put(Direction.LEFT, new Integer[]{ 0, -1 });
        directionToShift.put(Direction.RIGHT, new Integer[]{ 0, 1 });
        return directionToShift;
    }

    public Tile[][] getGrid() {
        // TODO have this return a new grid so that this grid field cannot be mutated
        return this.grid;
    }

    public Tile getTile(Position p) {
        Tile tile = this.grid[p.getRow()][p.getColumn()];
        return new Tile(tile.getGems(), tile.getConnector());
    }

    public Tile getTile(int row, int col) {
        if (!isWithinRange(new Position(row, col))) {
            throw new IllegalArgumentException("Cannot get a tile that is out of bounds");
        }
        return new Tile(this.grid[row][col].getGems(), this.grid[row][col].getConnector());
    }

    public Set<UnorderedPair<Gem, Gem>> getAllGemPairs() {
        Set<UnorderedPair<Gem, Gem>> gemPairs = new HashSet<>();
        for (Tile[] row : this.grid) {
            for (Tile tile : row) {
                if (tile.isPresent()) {
                    gemPairs.add(tile.getGems());
                }
            }
        }
        return gemPairs;
    }

    public Tile[][] getGridDeepCopy() {
        Tile[][] newGrid = new Tile[this.numRows][this.numColumns];
        for (int i = 0; i < this.numRows; i += 1) {
            for (int j = 0; j < this.numColumns; j += 1) {
                newGrid[i][j] = this.getTile(new Position(i, j));
            }
        }
        return newGrid;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }
        Board boardToCompare = (Board) o;
        boolean areHeightsDifferent = boardToCompare.getGrid().length != this.grid.length;
        boolean areWidthsDifferent = boardToCompare.getGrid()[0].length != this.grid[0].length;
        if (areHeightsDifferent || areWidthsDifferent) {
            return false;
        }
        for (int row = 0; row < this.grid.length; row += 1) {
            for (int col = 0; col < this.grid[row].length; col += 1) {
                    Tile tileToCompare = boardToCompare.getGrid()[row][col];
                    Tile currentTile = this.grid[row][col];
                    if (!tileToCompare.equals(currentTile)) {
                        System.out.println("compare: " + tileToCompare.getGems().toString() + " , " + tileToCompare.getConnector().toString() + ", " + tileToCompare.getConnector().getOrientation().toString());
                        System.out.println("current: " + currentTile.getGems().toString() + " , " + currentTile.getConnector().toString() + ", " + currentTile.getConnector().getOrientation().toString());
                        System.out.println("broke here");
                        System.out.println(tileToCompare.toString());
                        System.out.println(currentTile.toString());
                        return false;
                    }

            }
        }
        return true;
    }

    /**
     * Get pairs (if it is a row, index) which represent rows and columns in row column order that are moveable in this board
     * @return          pairs (if it is a row, index)
     */
    public List<Pair<Boolean, Integer>> getMoveable() {
        List<Pair<Boolean, Integer>> moveableIndexes = new ArrayList<>();
        moveableIndexes.addAll(this.getMoveableIndexes(true));
        moveableIndexes.addAll(this.getMoveableIndexes(false));
        return moveableIndexes;
    }

    /**
     * Returns the number of fixed rows or columns in this board.
     * @param isRow     true if this should return the number of fixed rows, false if
     *                  this should return the number of fixed columns.
     * @return          the number of fixed rows or columns.
     */
    public int getNumFixedRowsOrColumns(boolean isRow) {
        List<Pair<Boolean, Integer>> movableRowIndices = getMoveableIndexes(isRow);
        int totalIndices = isRow ? getNumRows() : getNumColumns();
        return totalIndices - movableRowIndices.size();
    }

    /**
     * Returns the total number of fixed tiles on this board.
     * @return  the number of fixed tiles.
     */
    public int getNumFixedTiles() {
        int numFixedRows = getNumFixedRowsOrColumns(true);
        int numFixedColumns = getNumFixedRowsOrColumns(false);
        return numFixedRows * numFixedColumns;
    }

    /**
     * Return a list of either all moveable rows or all moveable columns.
     * The list contains a Pair of Boolean and Integer, where:
     * - the Boolean represents if the index is for a row (true for row, false for column)
     * - the Integer represents the index of that row / column.
     * @param isRow true if the list should contain all moveable rows, false if it should contain all moveable columns.
     * @return either all moveable rows or all moveable columns.
     */
    private List<Pair<Boolean, Integer>> getMoveableIndexes(boolean isRow) {
        List<Pair<Boolean, Integer>> moveableIndexes = new ArrayList<>();
        int length = 0;
        if (isRow) {
            length = this.numRows;
        } else {
            length = this.numColumns;
        }
        for (int i = 0; i < length; i += 1) {
            if (this.isMoveable(i, isRow)) {
                moveableIndexes.add(new Pair(isRow, i));
            }
        }
        return moveableIndexes;
    }

    public boolean isMoveableIndex(boolean isRow, int index) {
        return getMoveableIndexes(isRow).contains(new Pair(isRow, index));
    }

    private boolean isMoveable(int index, boolean isRow) {
        Rules rules = Rules.getInstance();
        return isRow ? rules.isMovableRow(index) : rules.isMovableColumn(index);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return Arrays.deepToString(grid);
    }

    /**
     * Get the grid of this board as an array of strings that represent the connectors
     * @return  string array that represents the connectors
     */
    public String[][] getConnectorsAsStringArray() {
        String[][] ans = new String[this.numRows][this.numColumns];
        for (int row = 0; row < this.numRows; row += 1) {
            String[] rowOfStrings = new String[this.numColumns];
            for (int col = 0; col < this.numColumns; col += 1) {
                Tile tile = this.grid[row][col];
                String connectorAsString = tile.getConnector().toString();
                rowOfStrings[col] = connectorAsString;
            }
            ans[row] = rowOfStrings;
        }
        return ans;
    }

    /**
     * Get treasure of this board as strings
     * @return
     */
    public String[][][] getTreasuresAsStringArray() {
        String[][][] ans = new String[this.numRows][this.numColumns][2];
        for (int row = 0; row < this.numRows; row += 1) {
            String[][] rowOfGems = new String[this.numColumns][2];
            for (int col = 0; col < this.numColumns; col += 1) {
                rowOfGems[col] = convertGemPairToStringArray(this.grid[row][col].getGems());
            }
            ans[row] = rowOfGems;
        }

        return ans;
    }

    private String[] convertGemPairToStringArray(UnorderedPair<Gem, Gem> gems) {
        String[] gemPairString = new String[2];
        gemPairString[0] = gems.getFirst().toString();
        gemPairString[1] = gems.getSecond().toString();
        return gemPairString;
    }

    /**
     * Generates a board, with both grid
     * @param numRows         height of the grid
     * @param numColumns     width of the grid
     * @return               a board, with both grid and spare tile
     */
    public static Board generateBoard(int numRows, int numColumns) {
        int totalTiles = numColumns * numRows;
        Random random = new Random();
        List<Tile> tiles = generateRandomTiles(random, totalTiles);
        return createBoard(numColumns, numRows, tiles);
    }

    /**
     * Get all of the fixed positions of this board in row-column order
     * @return  all the fixed positions of this board
     */
    public List<Position> getFixedPositions() {
        List<Position> fixed = new ArrayList<>();
        for (int row = 0; row < this.numRows; row += 1) {
            for (int col = 0; col < this.numColumns; col += 1) {
                Pair<Boolean, Integer> rowIndex = new Pair(true, row);
                Pair<Boolean, Integer> columnIndex = new Pair(false, col);
                List<Pair<Boolean, Integer>> moveable = this.getMoveable();
                if (!moveable.contains(rowIndex) && !moveable.contains(columnIndex)) {
                    fixed.add(new Position(row, col));
                }
            }
        }
        return fixed;
    }

    /**
     * Creates a board with the given dimensions and tiles
     * @param numColumns     width of board
     * @param numRows        height of board
     * @param tiles          tiles to be placed on board including spare
     * @return               board
     */
    private static Board createBoard(int numColumns, int numRows, List<Tile> tiles) {
        Tile[][] grid = new Tile[numRows][numColumns];
        int count = 0;
        int tilesSize = tiles.size();
        for (int row = 0; row < numRows; row += 1) {
            Tile[] rowTiles = new Tile[numColumns];
            for (int col = 0; col < numColumns; col += 1) {
                int index = count;
                rowTiles[col] = tiles.get(index);
                count += 1;
            }
            grid[row] = rowTiles;
        }
        return new Board(grid);
    }

    /**
     * Generates a tiles with random pathways
     * @param random         random object
     * @param totalTiles     number of tiles to generate
     * @return               list of tiles
     */
    private static List<Tile> generateRandomTiles(Random random, int totalTiles) {
        List<UnorderedPair<Gem, Gem>> gemPairs = generateGemPairs(random, totalTiles);
        List<Connector> connectors = generateConnectors(random, totalTiles);
        return generateTiles(gemPairs, connectors, totalTiles);
    }

    /**
     * Generates a list of tiles
     * @param gemPairs       gem pairs for the corresponding tile
     * @param connectors     connector for the corresponding tile
     * @param totalTiles     total number of tiles to generate
     * @return               list of tiles from the given gem pairs and connectors
     */
    private static List<Tile> generateTiles(List<UnorderedPair<Gem, Gem>> gemPairs,
                                            List<Connector> connectors,
                                            int totalTiles) {
        List<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < totalTiles; i += 1) {
            tiles.add(new Tile(gemPairs.get(i), connectors.get(i)));
        }
        return tiles;
    }

    /**
     * Generate gem sets of the given size, where each pair of gems is unique and no pair contains
     * two of the same gems.
     * @param totalGemPairs     number of gem pairs.
     * @param random            the random object.
     * @return                  list of gem pairs.
     */
    private static List<UnorderedPair<Gem, Gem>> generateGemPairs(Random random, int totalGemPairs) {
        List<UnorderedPair<Gem, Gem>> gemPairs = new ArrayList<>();
        int count = 0;
        while (count != totalGemPairs) {
            Gem gem1 = Gem.values()[random.nextInt(Gem.values().length)];
            Gem gem2 = Gem.values()[random.nextInt(Gem.values().length)];
            UnorderedPair<Gem, Gem> randomGemPair = new UnorderedPair<>(gem1, gem2);
            if (!gem1.equals(gem2) && !gemPairs.contains(randomGemPair)) {
                gemPairs.add(randomGemPair);
                count++;
            }
        }
        return gemPairs;
    }

    private static List<List<String>> generateGemPairStrings(int totalGemPairs) {
        List<List<String>> gemPairs = new ArrayList<>();
        // TODO can this be cleaner?
        int count = 0;
        for (Gem gem1 :  Gem.values()) {
            for (Gem gem2 :  Gem.values()) {
                List<String> curr = new ArrayList<>();
                curr.add("\"" + gem1.toString() + "\"");
                curr.add("\"" + gem2.toString() + "\"");
                gemPairs.add(curr);
                count += 1;
                if (count == totalGemPairs) {
                    return gemPairs;
                }
            }
        }
        return gemPairs;
    }

    /**
     * Generate connectors of the given size
     * @param random              random object
     * @param totalConnectors     number of connectors
     * @return                    list of connectors
     */
    private static List<Connector> generateConnectors(Random random, int totalConnectors) {
        List<Connector> connectors = new ArrayList<>();

        for (int index = 0; index < totalConnectors; index += 1) {
            connectors.add(Connector.getRandom(random));
        }
        return connectors;
    }
}