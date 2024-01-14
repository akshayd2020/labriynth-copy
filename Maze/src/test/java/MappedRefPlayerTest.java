import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedRefPlayerTest {
    Color avatar0;
    Position homePos0;
    Position goalPos0;
    Position currenPos0;
    PlayerState playerStateZero;

    Queue<PlayerState> ps;

    MappedCoordinate current;
    MappedCoordinate home;
    MappedCoordinate goal;
    String color;

    @BeforeEach
    public void setup() {
        avatar0 = Color.GREEN;
        homePos0 = new Position(1, 1);
        goalPos0 = new Position(5, 5);
        currenPos0 = new Position(1, 1);
        playerStateZero = new PlayerState(
                avatar0,
                homePos0, goalPos0, currenPos0, 0, new Player("Test Ref Player", new RiemannStrategy()));
        current = new MappedCoordinate(1,1);
        home = new MappedCoordinate(1,1);
        goal = new MappedCoordinate(5,5);
        color = "00FF00";
        ps = new LinkedList<>(new ArrayList<>(Arrays.asList(playerStateZero)));
    }

    @Test
    public void playerStatesToRefPlayers() {
        MappedRefPlayer[] expected = new MappedRefPlayer[] {new MappedRefPlayer(current, home, goal, color)};
        assertArrayEquals(expected, MappedRefPlayer.playerStatesToRefPlayers(ps));
    }

    @Test
    public void mappedRefPlayerEqualsWorks() {
        assertEquals(new MappedRefPlayer(current, home, goal, color), new MappedRefPlayer(current, home, goal, color));
    }
}
