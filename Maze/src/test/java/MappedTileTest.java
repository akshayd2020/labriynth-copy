import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedTileTest {
    UnorderedPair<Gem, Gem> berylZircon;
    Tile bZTile;

    @BeforeEach
    public void setUp() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
    }
    @Test
    public void tileToMappedTileWorks() {
        assertEquals(new MappedTile(Character.toString('\u2514'), "beryl", "zircon"), MappedTile.tileToMappedTile(bZTile));
    }

    @Test
    public void mappedTileEquals() {
        MappedTile expected = new MappedTile(Character.toString('\u2514'), "beryl", "zircon");
        assertEquals(expected, new MappedTile(Character.toString('\u2514'), "beryl", "zircon"));
    }

    @Test
    public void getGem1Works() {
        assertEquals("beryl", new MappedTile(Character.toString('\u2514'), "beryl", "zircon").getGem1());
    }

    @Test
    public void getGem2Works() {
        assertEquals("zircon", new MappedTile(Character.toString('\u2514'), "beryl", "zircon").getGem2());
    }

    @Test
    public void getTilekeyWorks() {
        assertEquals(Character.toString('\u2514'), new MappedTile(Character.toString('\u2514'), "beryl", "zircon").getTilekey());
    }
}
