public class Position {

    final int row;
    final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position getDeepCopy() {
        return new Position(this.row, this.column);
    }

    /**
     * Get zero-indexed row
     * @return  zero-indexed row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Get zero-indexed column
     * @return  zero-indexed column
     */
    public int getColumn() {
        return this.column;
    }

    public MappedCoordinate getMappedCoordinate() {
        return new MappedCoordinate(this.row, this.column);
    }

    public static int calculatedSquareDistance(Position p, Position target) {
        int dX = p.getRow() - target.getRow();
        int dY = p.getColumn() - target.getColumn();
        return (dX * dX) + (dY * dY);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position)) {
            return false;
        }
        Position positionToCompare = (Position) o;
        if (this.row != positionToCompare.getRow() || this.column != positionToCompare.getColumn()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (Integer.toString(this.row) + "," + Integer.toString(this.column)).hashCode();
    }

    @Override
    public String toString() {
        return "row: " + Integer.toString(this.row) + " column: " + Integer.toString(this.column);
    }
}
