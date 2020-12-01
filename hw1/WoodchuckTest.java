import static org.junit.Assert.*;

import org.junit.Test;

public class WoodchuckTest {

    @Test
    public void testSmallValidWriteupExample() {
        long[] validLengths = {3, 5};
        assertEquals(1, Woodchuck.canCompleteFloor(validLengths));
    }

    @Test
    public void testMediumValidWriteupExample() {
        long[] validLengths = {90, 6, 18, 10, 10};
        assertEquals(2, Woodchuck.canCompleteFloor(validLengths));
    }

    @Test
    public void testInvalidWriteupExample() {
        long[] validLengths = {363, 7, 51};
        assertEquals(-1, Woodchuck.canCompleteFloor(validLengths));
    }

}

