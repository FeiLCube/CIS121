
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;

public class GraphTest {
    
    Graph test1;
    
    @Before
    public void setUpGraphs() {
        test1 = new Graph(5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegative() {
        Graph test = new Graph(-1);
        test.getSize();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorSizeZero() {
        Graph test = new Graph(0);
        test.getSize();
    }
    
    @Test
    public void testGetSize() {
        assertEquals(test1.getSize(), 5);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testHasEdgeNegativeFirstVertex() {
        assertFalse(test1.hasEdge(-1, 2));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testHasEdgeNegativeSecondVertex() {
        assertFalse(test1.hasEdge(0, -3));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testHasEdgeFirstVertexTooBig() {
        assertFalse(test1.hasEdge(5, 0));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testHasEdgeSecondVertexTooBig() {
        assertFalse(test1.hasEdge(2, 10));
    }
    
    @Test
    public void testHasEdge() {
        assertFalse(test1.hasEdge(0, 1));
        test1.listRep[0].put(4, 1);
        assertTrue(test1.hasEdge(0, 4));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetWeightFirstVertexTooBig() {
        test1.listRep[0].put(1, 2);
        assertEquals(test1.getWeight(5, 0), 2);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetWeightSecondVertexTooBig() {
        test1.listRep[0].put(1, 2);
        assertEquals(test1.getWeight(0, 5), 2);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetWeightNegativeFirstVertex() {
        test1.listRep[0].put(1, 2);
        assertEquals(test1.getWeight(-5, 1), 2);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetWeightNegativeSecondVertex() {
        test1.listRep[0].put(1, 2);
        assertEquals(test1.getWeight(0, -2), 2);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void testGetWeightEdgeDoesNotExist() {
        assertEquals(test1.getWeight(0, 1), 2);
    }
    
    @Test
    public void testGetWeight() {
        test1.listRep[0].put(4, 3);
        assertEquals(test1.getWeight(0, 4), 3);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeFirstVertexTooBig() {
        test1.addEdge(7, 0, 3);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeSecondVertexTooBig() {
        test1.addEdge(0, 8, 3);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeNegativeFirstVertex() {
        test1.addEdge(-10, 0, 3);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeNegativeSecondVertex() {
        test1.addEdge(3, -4, 3);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAddEdgeSameVertices() {
        test1.addEdge(3, 3, 5);
    }
    
    @Test
    public void testAddEdge() {
        assertTrue(test1.addEdge(0, 1, 3));
        assertTrue(test1.listRep[0].containsKey(1));
        assertEquals(test1.listRep[0].get(1).intValue(), 3);
        assertFalse(test1.addEdge(0, 1, 3));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testOutNeighborsNegativeVertex() {
        Set<Integer> result = test1.outNeighbors(-1);
        result.size();
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testOutNeighborsOutOfBoundsVertex() {
        Set<Integer> result = test1.outNeighbors(7);
        result.size();
    }
    
    @Test
    public void testOutNeighbors() {
        test1.addEdge(3, 0, 3);
        test1.addEdge(3, 2, 5);
        test1.addEdge(3, 4, 1);
        Set<Integer> result3 = test1.outNeighbors(3);
        Set<Integer> result0 = test1.outNeighbors(0);
        assertTrue(result0.isEmpty());
        assertEquals(result3.size(), 3);
    }
}
