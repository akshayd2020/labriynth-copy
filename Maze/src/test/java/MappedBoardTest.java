import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedBoardTest {
    String[][] connectors;
    String[][][] treasures;
    Board board7x7;

    @BeforeEach
    public void setup() {
        board7x7 = BoardTest.generateStaticBoard();
        connectors = new String[][]{
                {"┬", "┘", "│", "┐", "┼", "│", "┼"},
                {"┼", "┼", "┼", "┌", "└", "┘", "┤"},
                {"│", "┼", "┬", "─", "┐", "─", "├"},
                {"┼", "─", "┬", "┴", "─", "└", "┴"},
                {"┤", "─", "┬", "│", "│", "─", "─"},
                {"┬", "┼", "│", "┌", "─", "┌", "│"},
                {"┼", "─", "┬", "┬", "─", "┴", "┼"}};
        treasures = new String[][][]
                {{{"pink-spinel-cushion","purple-spinel-trillion"},{"unakite","ammolite"},
                        {"aquamarine","lemon-quartz-briolette"},{"ruby","green-princess-cut"},
                        {"spinel","yellow-jasper"},{"black-spinel-cushion","yellow-heart"},{"zircon","pink-opal"}},
                        {{"spinel","chrysolite"},{"blue-ceylon-sapphire","ametrine"},{"zoisite","pink-emerald-cut"},
                                {"beryl","red-spinel-square-emerald-cut"},{"stilbite","prasiolite"},
                                {"red-spinel-square-emerald-cut","spinel"},{"aquamarine","zircon"}},
                        {{"yellow-baguette","sphalerite"},{"blue-pear-shape","tanzanite-trillion"},
                                {"garnet","emerald"},{"apatite","white-square"},{"tanzanite-trillion","purple-oval"},
                                {"blue-spinel-heart","red-spinel-square-emerald-cut"},
                                {"alexandrite-pear-shape","stilbite"}},{{"apatite","padparadscha-oval"},
                        {"lemon-quartz-briolette","chrysolite"},{"gray-agate","heliotrope"},
                        {"pink-spinel-cushion","pink-opal"},{"lapis-lazuli","black-onyx"},
                        {"blue-spinel-heart","pink-round"},{"lemon-quartz-briolette","pink-emerald-cut"}},
                        {{"kunzite","moss-agate"},{"lapis-lazuli","yellow-beryl-oval"},{"diamond","clinohumite"},
                                {"white-square","tourmaline-laser-cut"},{"pink-round","blue-cushion"},
                                {"prehnite","prasiolite"},{"kunzite-oval","blue-cushion"}},
                        {{"pink-opal","rock-quartz"},{"heliotrope","purple-cabochon"},{"ametrine","carnelian"},
                                {"azurite","australian-marquise"},{"citrine","chrysoberyl-cushion"},
                                {"grossular-garnet","orange-radiant"},{"raw-citrine","grandidierite"}},
                        {{"apricot-square-radiant","iolite-emerald-cut"},{"purple-square-cushion","citrine"},
                                {"garnet","moss-agate"},{"tanzanite-trillion","purple-cabochon"},
                                {"garnet","heliotrope"},{"goldstone","black-onyx"},{"unakite","hackmanite"}}};
        }


        @Test
        public void boardToMappedBoardWorks() {
            MappedBoard expected = new MappedBoard(connectors, treasures);
            assertEquals(expected, MappedBoard.boardToMappedBoard(board7x7));
        }

        @Test
        public void getConnectorsWorks() {
            assertArrayEquals(connectors, new MappedBoard(connectors, treasures).getConnectors());
        }

        @Test
        public void getTreasuresWorks() {
            assertArrayEquals(treasures, new MappedBoard(connectors, treasures).getTreasures());
        }

        @Test
        public void mappedBoardEquals() {
            MappedBoard mappedBoard2 = new MappedBoard(connectors, treasures);
            assertEquals(new MappedBoard(connectors, treasures), mappedBoard2);
        }
    }
