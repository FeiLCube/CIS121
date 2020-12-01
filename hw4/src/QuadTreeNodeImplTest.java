import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class QuadTreeNodeImplTest {

    private int[][] worstCase;
    private int[][] averageCase;
    private int[][] bestCase;
    private int[][] lengthEightArray;
    private int[][] lengthOneArray;

    @Before
    public void setupIntArrays() {
        worstCase = new int[][] { 
            { 1, 2, 3, 4 }, 
            { 5, 6, 7, 8 }, 
            { 9, 10, 11, 12 }, 
            { 13, 14, 15, 16 } 
        };

        averageCase = new int[][] { 
            { 1, 2, 3, 3 }, 
            { 5, 6, 3, 3 }, 
            { 9, 10, 10, 12 }, 
            { 13, 9, 15, 16 } 
        };

        bestCase = new int[][] { 
            { 1, 1, 1, 1 }, 
            { 1, 1, 1, 1 }, 
            { 1, 1, 1, 1 }, 
            { 1, 1, 1, 1 } 
        };

        lengthEightArray = new int[][] { 
            { 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0 }, 
            { 0, 0, 0, 0, 0, 0, 0, 0 } 
        };
        
        lengthOneArray = new int[][] { 
            { 0 } 
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareArray() {
        int[][] invalidArr = new int[][] { { 0, 2, 1, 1 }, { 3, 0, 0, 1 }, { 4, 5, 9, 10 } };
        QuadTreeNodeImpl.buildFromIntArray(invalidArr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArray() {
        int[][] invalidArr = null;
        QuadTreeNodeImpl.buildFromIntArray(invalidArr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyArray() {
        int[][] invalidArr = new int[0][0];
        QuadTreeNodeImpl.buildFromIntArray(invalidArr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPowerOfTwoArray() {
        int[][] invalidArr = new int[][] { 
            { 0, 2, 1, 1, 0 }, 
            { 3, 0, 0, 1, 5 }, 
            { 4, 5, 9, 10, 3 }, 
            { 2, 1, 1, 2, 7 },
            { 1, 1, 0, 10, 9 } 
        };
        QuadTreeNodeImpl.buildFromIntArray(invalidArr);
    }

    @Test
    public void testOneByOneEdgeCase() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertNull(testTree.tr);
        assertNull(testTree.br);
        assertNull(testTree.tl);
        assertNull(testTree.bl);
        assertEquals(0, testTree.x);
        assertEquals(0, testTree.y);
        assertEquals(0, testTree.height);
        assertEquals(Integer.valueOf(0), testTree.rgb);
    }

    @Test
    public void testGetQuandrantWorstCaseArray() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        QuadTreeNodeImpl trQuadrant = (QuadTreeNodeImpl) testTree.getQuadrant(
                QuadTreeNode.QuadName.TOP_RIGHT);
        assertEquals(1, trQuadrant.height);
        assertEquals(2, trQuadrant.x);
        assertEquals(0, trQuadrant.y);
        QuadTreeNodeImpl trbrQuadrant = (QuadTreeNodeImpl) trQuadrant.getQuadrant(
                QuadTreeNode.QuadName.BOTTOM_RIGHT);
        assertEquals(0, trbrQuadrant.height);
        assertEquals(3, trbrQuadrant.x);
        assertEquals(1, trbrQuadrant.y);
    }

    @Test
    public void testGetQuandrantNullReturn() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        assertNull(testTree.getQuadrant(QuadTreeNode.QuadName.TOP_LEFT));
        assertNull(testTree.getQuadrant(QuadTreeNode.QuadName.TOP_RIGHT));
        assertNull(testTree.getQuadrant(QuadTreeNode.QuadName.BOTTOM_LEFT));
        assertNull(testTree.getQuadrant(QuadTreeNode.QuadName.BOTTOM_RIGHT));
    }

    @Test
    public void testIsLeaf() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        assertTrue(testTree.isLeaf());
        testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertTrue(testTree.isLeaf());
        testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        assertFalse(testTree.isLeaf());
        assertTrue(testTree.tr.isLeaf());
        testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        assertFalse(testTree.isLeaf());
        assertFalse(testTree.br.isLeaf());
        assertTrue(testTree.br.br.isLeaf());
    }

    @Test
    public void testMergeAverageCaseArray() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        assertEquals(1, testTree.tr.height);
        assertEquals(Integer.valueOf(3), testTree.tr.rgb);
        assertNull(testTree.tr.tr);
        assertNull(testTree.tr.tl);
        assertNull(testTree.tr.br);
        assertNull(testTree.tr.bl);
    }

    @Test
    public void testMergeBestCaseArray() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        assertNull(testTree.tl);
        assertNull(testTree.tr);
        assertNull(testTree.bl);
        assertNull(testTree.br);
        assertEquals(2, testTree.height);
        assertEquals(Integer.valueOf(1), testTree.rgb);
    }

    @Test
    public void testBuildTreeAverageCase() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        assertEquals(2, testTree.height);
        assertEquals(0, testTree.x);
        assertEquals(0, testTree.y);
        assertNull(testTree.rgb);
        QuadTreeNodeImpl tl = testTree.tl;
        QuadTreeNodeImpl tr = testTree.tr;
        QuadTreeNodeImpl bl = testTree.bl;
        QuadTreeNodeImpl br = testTree.br;
        assertEquals(1, tl.height);
        assertEquals(2, tr.x);
        assertEquals(0, tr.y);
        assertEquals(2, br.y);
        assertEquals(Integer.valueOf(3), tr.rgb);
        assertNull(tr.tr);
        assertEquals(0, tl.tl.height);
        assertEquals(1, tl.br.x);
        assertEquals(3, bl.bl.y);
        assertEquals(Integer.valueOf(13), bl.bl.rgb);
    }

    @Test
    public void testGetDimension() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(lengthEightArray);
        assertEquals(8, testTree.getDimension());
    }

    @Test
    public void testGetDimensionOfLengthOneArray() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertEquals(1, testTree.getDimension());
    }

    @Test
    public void testGetSize() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        assertEquals(21, testTree.getSize());
        QuadTreeNodeImpl tl = testTree.tl;
        assertEquals(5, tl.getSize());
        testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertEquals(1, testTree.getSize());
        testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        assertEquals(17, testTree.getSize());
        testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        assertEquals(1, testTree.getSize());
    }

    @Test
    public void testGetCompressionRatio() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        assertEquals(21.0 / 16, testTree.getCompressionRatio(), 0);
        testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertEquals(1.0, testTree.getCompressionRatio(), 0);
        testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        assertEquals(1.0 / 16, testTree.getCompressionRatio(), 0);
    }

    @Test
    public void testDecompressWorstCase() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        int[][] image = testTree.decompress();
        assertArrayEquals(worstCase, image);
    }

    @Test
    public void testDecompressAverageCase() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        int[][] image = testTree.decompress();
        assertArrayEquals(averageCase, image);
    }

    @Test
    public void testDecompressBestCase() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        int[][] image = testTree.decompress();
        assertArrayEquals(bestCase, image);
    }

    @Test
    public void testDecompressLengthOneArray() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        int[][] image = testTree.decompress();
        assertArrayEquals(lengthOneArray, image);
    }

    @Test
    public void testGetColor() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(lengthOneArray);
        assertEquals(0, testTree.getColor(0, 0));
        testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        assertEquals(8, testTree.getColor(3, 1));
        assertEquals(15, testTree.br.getColor(0, 1));
        assertEquals(13, testTree.bl.getColor(0, 1));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColorInvalidXCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.getColor(4, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColorInvalidYCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.getColor(2, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColorNegativeXCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.getColor(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetColorNegativeYCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.getColor(2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetColorInvalidXCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(4, 2, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetColorInvalidYCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(2, 4, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetColorNegativeXCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(-1, 2, -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetColorNegativeYCoord() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(2, -1, -10);
    }

    @Test
    public void testSetColorNothingChanges() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(3, 0, 4);
        assertEquals(4, testTree.getColor(3, 0));

        testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        testTree.setColor(2, 3, 1);
        assertEquals(1, testTree.getSize());
        assertEquals(1, testTree.getColor(2, 3));
    }

    @Test
    public void testSetColorChange() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(worstCase);
        testTree.setColor(3, 0, 5);
        assertEquals(5, testTree.getColor(3, 0));
        testTree.setColor(0, 3, -100);
        assertEquals(-100, testTree.getColor(0, 3));
    }

    @Test
    public void testSetColorSplitLeaf() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        assertEquals(17, testTree.getSize());
        testTree.setColor(3, 0, 25);
        assertEquals(25, testTree.getColor(3, 0));
        assertEquals(3, testTree.tr.getColor(0, 0));
        testTree.tr.setColor(1, 0, 25);
        assertEquals(25, testTree.getColor(3, 0));
        assertEquals(21, testTree.getSize());

        testTree = QuadTreeNodeImpl.buildFromIntArray(bestCase);
        testTree.setColor(2, 2, 2);
        assertEquals(9, testTree.getSize());
        assertEquals(5, testTree.br.getSize());
    }

    @Test
    public void testSetColorMergeLeaves() {
        QuadTreeNodeImpl testTree = QuadTreeNodeImpl.buildFromIntArray(averageCase);
        testTree.setColor(0, 0, 3);
        testTree.setColor(0, 1, 3);
        testTree.setColor(1, 0, 3);
        testTree.setColor(1, 1, 3);
        assertEquals(13, testTree.getSize());
        testTree.setColor(0, 2, 3);
        testTree.setColor(0, 3, 3);
        testTree.setColor(1, 2, 3);
        testTree.setColor(1, 3, 3);
        assertEquals(9, testTree.getSize());
        testTree.setColor(2, 2, 3);
        testTree.setColor(2, 3, 3);
        testTree.setColor(3, 2, 3);
        testTree.setColor(3, 3, 3);
        assertEquals(1, testTree.getSize());
    }
}
