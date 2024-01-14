import org.jgrapht.alg.util.UnorderedPair;

public class Tile {

    private UnorderedPair<Gem, Gem> gems;
    private Connector connector;
    // degree to rotate must be a multiple of this
    private int VALID_DEGREE_MULTIPLE = 90;

    public Tile(UnorderedPair<Gem, Gem> gems, Connector connector) {
        this.gems = gems;
        this.connector = connector;
    }

    public UnorderedPair<Gem, Gem> getGems() {
        UnorderedPair<Gem, Gem> optGems = this.gems;
        return optGems;
    }

    public Connector getConnector() {
        return new Connector(this.connector.getUnicode(), this.connector.getOrientation());
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isPresent() {
        return true;
    }

    /**
     * Set the connector of this tile to be the given connector
     * @param connector to update this tile with
     */
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Tile getDeepCopy() {
        return new Tile(this.gems, this.getConnector().getDeepCopy());
    }

    public Tile rotateTile(int degreeToRotate) {
        checkValidDegreeToRotate(degreeToRotate);
        int initialDegree = this.connector.getOrientation().getDegree();
        Orientation orientation = Orientation.getOrientation(degreeToRotate + initialDegree);
        return new Tile(this.getGems(), new Connector(this.getConnector().getUnicode(), orientation));
    }

    private void checkValidDegreeToRotate(int d) {
        if (d % VALID_DEGREE_MULTIPLE != 0) {
            throw new IllegalArgumentException("Attempted to rotate an invalid degree");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tile)) {
            return false;
        }
        Tile tileToCompare = (Tile) o;
        return tileToCompare.getGems().equals(this.gems);
    }

    @Override
    public int hashCode() {
        return this.getGems().hashCode() + this.getConnector().hashCode();
    }

    @Override
    public String toString() {
        return connector.toString();
//                + this.gems.toString();
    }
}