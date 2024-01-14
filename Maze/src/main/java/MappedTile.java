import com.google.gson.annotations.SerializedName;

import org.jgrapht.alg.util.UnorderedPair;

public class MappedTile {
    private String tilekey;

    @SerializedName("1-image")
    private String gem1;

    @SerializedName("2-image")
    private String gem2;

    public MappedTile(String tilekey, String gem1, String gem2) {
        this.tilekey = tilekey;
        this.gem1 = gem1;
        this.gem2 = gem2;
    }

    public Tile getTile() {
        return new Tile(new UnorderedPair(Gem.getGem(gem1), Gem.getGem(gem2)), this.getConnector());
    }

    private Connector getConnector() {
        char unicode = tilekey.charAt(0);
        return Connector.getCharToConnector().get(unicode);
    }

    /**
     * Get a mapped tile that represents the given tile
     * @param t     given tile
     * @return      mapped tile
     */
    public static MappedTile tileToMappedTile(Tile t) {
        return new MappedTile(t.getConnector().toString(), t.getGems().getFirst().toString(),
                t.getGems().getSecond().toString());
    }

    public String getTilekey() {
        return tilekey;
    }

    public String getGem1() {
        return gem1;
    }

    public String getGem2() {
        return gem2;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof MappedTile)) {
            return false;
        }

        MappedTile toCompare = (MappedTile) o;
        return this.tilekey.equals(toCompare.getTilekey()) && this.gem1.equals(toCompare.getGem1())
                && this.gem2.equals(toCompare.getGem2());
    }
}

