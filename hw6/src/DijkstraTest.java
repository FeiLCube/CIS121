import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DijkstraTest {
    
    Graph g1;
    Graph g2;
    
    @Before
    public void setupFields() {
        g1 = new Graph(8);
        g1.addEdge(0, 1, 3);
        g1.addEdge(0, 2, 1);
        g1.addEdge(1, 3, 2);
        g1.addEdge(2, 4, 2);
        g1.addEdge(3, 4, 2);
        g1.addEdge(6, 0, 5);
        g1.addEdge(6, 7, 1);
        g1.addEdge(7, 4, 2);
        
        g2 = new Graph(6);
        g2.addEdge(0, 1, 1);
        g2.addEdge(0, 2, 1);
        g2.addEdge(1, 4, 2);
        g2.addEdge(2, 3, 2);
        g2.addEdge(3, 5, 1);
        g2.addEdge(4, 5, 1);
    }
    
    @Test
    public void testDijkstraCorrectness() {
        List<Integer> result = Dijkstra.getShortestPath(g1, 0, 4);
        assertEquals(8, Dijkstra.nodes.size());
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).intValue());
        assertEquals(0, result.get(0).intValue());
        assertEquals(0, result.get(0).intValue());
    }
    
    @Test
    public void testDijkstraMultipleShortestPaths() {
        List<Integer> result = Dijkstra.getShortestPath(g2, 0, 5);
        assertEquals(4, result.size());
    }
    
    @Test
    public void testInnerClassContructors() {
        Dijkstra.TreeNode node1 = new Dijkstra.TreeNode(2);
        node1.distance = 0;
        Dijkstra.TreeNode node2 = new Dijkstra.TreeNode(2, node1);
        node2.distance = 0;
    }
    
    @Test
    public void testDjikstraSameSrcAndTgt() {
        List<Integer> result = Dijkstra.getShortestPath(g1, 6, 6);
        assertEquals(1, result.size());
        assertEquals(6, result.get(0).intValue());
    }
    
    @Test
    public void testDijkstraNoPathFound() {
        List<Integer> result = Dijkstra.getShortestPath(g1, 0, 7);
        assertTrue(result.isEmpty());
    }
}
