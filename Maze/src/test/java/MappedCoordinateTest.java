import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappedCoordinateTest {
    @Test
    public void mappedCoordinateEqualsWorks() {
        assertEquals(new MappedCoordinate(1,1), new MappedCoordinate(1,1));
    }
}
