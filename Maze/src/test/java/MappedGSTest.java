import com.google.gson.*;
import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedGSTest {
    UnorderedPair<Gem, Gem> berylZircon;
    Tile bZTile;
    Board board7x7;
    IPlayer player;
    LimitedGS limitedGS;
    String[][] connectors;
    String[][][] treasures;

    MappedTile berylZirconMapped;
    Queue<LimitedPlayerState> playerStates;
    MappedBoard mappedBoard;

    @BeforeEach
    public void setUp() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        board7x7 = BoardTest.generateStaticBoard();
        player = new Player("Test Player", new RiemannStrategy());
        limitedGS = new LimitedGS(bZTile, board7x7, new LinkedList<>(), Optional.empty());
        berylZirconMapped = new MappedTile(Character.toString('\u2514'), "beryl", "zircon");

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
        Color avatar0 = Color.GREEN;
        Position homePos0 = new Position(1, 1);
        Position currenPos0 = new Position(1, 1);
        LimitedPlayerState limitedPlayerState0 = new LimitedPlayerState(avatar0, homePos0, currenPos0);
        playerStates = new LinkedList<>(new ArrayList<>(Arrays.asList(limitedPlayerState0)));
        limitedGS = new LimitedGS(bZTile, board7x7, playerStates, Optional.empty());
        mappedBoard = new MappedBoard(connectors, treasures);
    }


    @Test
    public void convertLimitedGSToJsonWorks() {
        JsonObject expected = new JsonObject();
        expected.add("board", GsonSingleton.getInstance().toJsonTree(mappedBoard, MappedBoard.class));
        expected.add("spare", GsonSingleton.getInstance().toJsonTree(MappedTile.tileToMappedTile(bZTile), MappedTile.class));
        MappedPlayer[] mappedPlayers = MappedPlayer.playerStatesToPlayers(playerStates);
        expected.add("plmt", MappedPlayer.convertMappedPlayersToJsonArray(mappedPlayers));
        expected.add("last", JsonNull.INSTANCE);

        assertEquals(expected, MappedGS.convertLimitedGSToJson(limitedGS));
    }

    @Test
    public void convertJsonToLimitedGSWorks() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("board", GsonSingleton.getInstance().toJsonTree(mappedBoard, MappedBoard.class));
        jsonObject.add("spare", GsonSingleton.getInstance().toJsonTree(MappedTile.tileToMappedTile(bZTile), MappedTile.class));
        MappedPlayer[] mappedPlayers = MappedPlayer.playerStatesToPlayers(playerStates);
        jsonObject.add("plmt", MappedPlayer.convertMappedPlayersToJsonArray(mappedPlayers));
        jsonObject.add("last", JsonNull.INSTANCE);

        LimitedGS testLimitedGS = MappedGS.convertJsonElementToLimitedGS(jsonObject);

        assertEquals(limitedGS, testLimitedGS);
    }
}
