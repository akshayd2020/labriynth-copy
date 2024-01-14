import com.google.gson.JsonArray;
import javafx.util.Pair;
import org.jgrapht.alg.util.UnorderedPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedRefGSTest {
    UnorderedPair<Gem, Gem> berylZircon;
    Tile bZTile;
    Board board7x7;
    IPlayer player;
    GameState gameState;
    String[][] connectors;
    String[][][] treasures;

    MappedTile berylZirconMapped;

    @BeforeEach
    public void setUp() {
        berylZircon = new UnorderedPair(Gem.BERYL, Gem.ZIRCON);
        bZTile = new Tile(berylZircon, new Connector(Unicode.CORNER, Orientation.ZERO));
        board7x7 = BoardTest.generateStaticBoard();
        player = new Player("Test Player", new RiemannStrategy());
        gameState = new GameState(bZTile, board7x7, new LinkedList<>(), Optional.empty());
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

    }


    @Test
    public void gameStateToMappedRefGSWithNoPlayersOrLastAction() {
        MappedBoard mappedBoard = new MappedBoard(connectors, treasures);
        MappedRefGS expected = new MappedRefGS(mappedBoard, berylZirconMapped, new MappedRefPlayer[]{}, null);
        assertEquals(expected, MappedRefGS.gameStateToMappedRefGS(gameState));
    }

    @Test
    public void gameStatedToMappedRefGSWithLastAction() {
        Color avatar0 = Color.GREEN;
        Position homePos0 = new Position(1, 1);
        Position goalPos0 = new Position(5, 5);
        Position currenPos0 = new Position(1, 1);
        PlayerState playerState0 = new PlayerState(avatar0, homePos0, goalPos0, currenPos0, 0, player);
        Queue<PlayerState> playerStates = new LinkedList<>(new ArrayList<>(Arrays.asList(playerState0)));
        gameState = new GameState(bZTile, board7x7, playerStates, Optional.of(new SlidingAction(0, Direction.LEFT)));
        MappedBoard mappedBoard = new MappedBoard(connectors, treasures);

        MappedRefPlayer[] expectedMappedPlayers = new MappedRefPlayer[1];
        expectedMappedPlayers[0] = new MappedRefPlayer(currenPos0.getMappedCoordinate(), homePos0.getMappedCoordinate(),
                goalPos0.getMappedCoordinate(), "00FF00");
        JsonArray lastAction = new JsonArray();
        lastAction.add(0);
        lastAction.add("LEFT");
        MappedRefGS expected = new MappedRefGS(mappedBoard, berylZirconMapped, expectedMappedPlayers, lastAction);
        assertEquals(expected, MappedRefGS.gameStateToMappedRefGS(gameState));
    }

    @Test
    public void mappedRefGSEquals() {
        Color avatar0 = Color.GREEN;
        Position homePos0 = new Position(1, 1);
        Position goalPos0 = new Position(5, 5);
        Position currenPos0 = new Position(1, 1);
        PlayerState playerState0 = new PlayerState(avatar0,
                homePos0, goalPos0, currenPos0, 0, player);
        Queue<PlayerState> playerStates = new LinkedList<>(new ArrayList<>(Arrays.asList(playerState0)));
        gameState = new GameState(bZTile, board7x7, playerStates, Optional.of(new SlidingAction(0, Direction.LEFT)));
        MappedBoard mappedBoard = new MappedBoard(connectors, treasures);

        MappedRefPlayer[] expectedMappedPlayers = new MappedRefPlayer[1];
        expectedMappedPlayers[0] = new MappedRefPlayer(currenPos0.getMappedCoordinate(), homePos0.getMappedCoordinate(),
                goalPos0.getMappedCoordinate(), "#00ff00");
        JsonArray lastAction = new JsonArray();
        lastAction.add(0);
        lastAction.add("LEFT");
        assertEquals(new MappedRefGS(mappedBoard, berylZirconMapped, expectedMappedPlayers, lastAction),
                new MappedRefGS(mappedBoard, berylZirconMapped, expectedMappedPlayers, lastAction));
    }
}
