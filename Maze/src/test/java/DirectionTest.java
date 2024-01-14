import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DirectionTest {
    //region getDirectionFromString()
    @Test
    public void passGetDirectionFromString() {
        assertEquals(Direction.LEFT, Direction.getDirectionFromString("LEFT"));
    }
    //endregion
}