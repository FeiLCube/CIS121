import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IslandBridgeTest {

    Graph good;
    Graph bad;
    Graph unconnectedGood;
    Graph isolatedSource;

    @Before
    public void setupFields() {
        IslandBridge.finishedNodes.clear();
        IslandBridge.unfinishedNodes.clear();
        IslandBridge.finishedNodes.add(0);
        
        good = new Graph(5);
        good.addEdge(0, 2, 1);
        good.addEdge(0, 3, 1);
        good.addEdge(1, 0, 1);
        good.addEdge(2, 4, 1);
        good.addEdge(3, 1, 1);
        good.addEdge(4, 3, 1);
        
        bad = new Graph(5);
        bad.addEdge(0, 2, 1);
        bad.addEdge(1, 0, 1);
        bad.addEdge(1, 4, 1);
        bad.addEdge(2, 1, 1);
        bad.addEdge(2, 3, 1);
        bad.addEdge(4, 2, 1);
        
        unconnectedGood = new Graph(10);
        unconnectedGood.addEdge(0, 1, 1);
        unconnectedGood.addEdge(1, 2, 1);
        unconnectedGood.addEdge(2, 3, 1);
        unconnectedGood.addEdge(3, 4, 1);
        unconnectedGood.addEdge(4, 0, 1);
        unconnectedGood.addEdge(5, 0, 1);
        unconnectedGood.addEdge(5, 8, 1);
        unconnectedGood.addEdge(7, 5, 1);
        unconnectedGood.addEdge(9, 5, 1);
        unconnectedGood.addEdge(9, 8, 1);
        
        isolatedSource = new Graph(2);
    }
    
    @Test
    public void testSearchGraphSuccess() {
        assertTrue(IslandBridge.searchGraph(good, 0));
    }
    
    @Test
    public void testSearchGraphFailure() {
        assertFalse(IslandBridge.searchGraph(bad, 0));
    }
    
    @Test
    public void testSearchUnconnectedGraphSuccess() {
        assertTrue(IslandBridge.searchGraph(unconnectedGood, 0));
    }
    
    @Test
    public void testIsolatedSourceGraph() {
        assertTrue(IslandBridge.allNavigable(isolatedSource, 0));
    }
    
    @Test
    public void testAllNavigableGraphSuccess() {
        assertTrue(IslandBridge.allNavigable(good, 0));
    }
    
    @Test
    public void testAllNavigableGraphFailure() {
        assertFalse(IslandBridge.allNavigable(bad, 0));
    }
    
    @Test
    public void testAllNavigableUnconnectedGraphSuccess() {
        assertTrue(IslandBridge.allNavigable(unconnectedGood, 0));
    }
}
