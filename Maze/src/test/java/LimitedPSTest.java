import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitedPSTest {
    Position homePos0 = new Position(1, 1);
    Position currenPos0 = new Position(1, 1);
    LimitedPlayerState limitedPS1 = new LimitedPlayerState(Color.GREEN, homePos0, currenPos0);

    @Test
    public void limitedPSEqualsWorks() {
        assertEquals(new LimitedPlayerState(Color.GREEN, homePos0, currenPos0), limitedPS1);
    }
}
