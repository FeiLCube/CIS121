import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MazeSolverTest {
    
    Graph g;
    Graph g2;
    
    @Before
    public void setUpFields() {
        g = new Graph(20);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(1, 6, 1);
        g.addEdge(6, 1, 1);
        g.addEdge(6, 11, 1);
        g.addEdge(11, 6, 1);
        g.addEdge(10, 11, 1);
        g.addEdge(11, 10, 1);
        g.addEdge(10, 15, 1);
        g.addEdge(15, 10, 1);
        g.addEdge(11, 12, 1);
        g.addEdge(12, 11, 1);
        g.addEdge(12, 13, 1);
        g.addEdge(13, 12, 1);
        g.addEdge(13, 18, 1);
        g.addEdge(18, 13, 1);
        g.addEdge(13, 8, 1);
        g.addEdge(8, 13, 1);
        g.addEdge(8, 9, 1);
        g.addEdge(9, 8, 1);
        g.addEdge(8, 3, 1);
        g.addEdge(3, 8, 1);

        g2 = new Graph(12);
        g2.addEdge(0, 1, 1);
        g2.addEdge(1, 0, 1);
        g2.addEdge(0, 4, 1);
        g2.addEdge(4, 0, 1);
        g2.addEdge(4, 8, 1);
        g2.addEdge(8, 4, 1);
        g2.addEdge(1, 2, 1);
        g2.addEdge(2, 1, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(3, 2, 1);
        g2.addEdge(8, 9, 1);
        g2.addEdge(9, 8, 1);
        g2.addEdge(9, 10, 1);
        g2.addEdge(10, 9, 1);
        g2.addEdge(10, 11, 1);
        g2.addEdge(11, 10, 1);
        g2.addEdge(11, 7, 1);
        g2.addEdge(7, 11, 1);
        g2.addEdge(7, 3, 1);
        g2.addEdge(3, 7, 1);
    }
    
    @Test
    public void testOutOfBounds() {
        int[][] test = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8}
        };
        
        assertTrue(MazeSolver.inBounds(test, 0, 2));
        assertFalse(MazeSolver.inBounds(test, -1, 2));
        assertFalse(MazeSolver.inBounds(test, 3, 2));
        assertFalse(MazeSolver.inBounds(test, 1, 3));
        assertFalse(MazeSolver.inBounds(test, 1, -3));
    }
    
    @Test
    public void testCalcCoord() {
        MazeSolver.BFSTreeNode c = new MazeSolver.BFSTreeNode();
        c.value = 1;
        int[][] test = {
            {0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15}
        };
        int v1 = 13;
        int v2 = 6;
        
        assertTrue(MazeSolver.calculateCoord(v1, test[0].length).equals(new Coordinate(3, 1)));
        assertTrue(MazeSolver.calculateCoord(v2, test[0].length).equals(new Coordinate(1, 2)));
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testBFSNullGraph() {
        MazeSolver.bfs(null, 0);
    }
    
    @Test
    public void testBFS() {
        int srcVertex = 0;
        MazeSolver.bfs(g, srcVertex);
        Set<MazeSolver.BFSTreeNode> layer0 = new HashSet<>();
        layer0.add(new MazeSolver.BFSTreeNode(0));
        Set<MazeSolver.BFSTreeNode> layer4 = new HashSet<>();
        layer4.add(new MazeSolver.BFSTreeNode(10));
        layer4.add(new MazeSolver.BFSTreeNode(12));
        
        Set<MazeSolver.BFSTreeNode>actualLayer0 = MazeSolver.bfsTreeNodeLayers.get(0);
        for (MazeSolver.BFSTreeNode node : actualLayer0) {
            assertEquals(node.value.intValue(), 0);
            assertNull(node.parent);
        }
        
        Set<MazeSolver.BFSTreeNode>actualLayer4 = MazeSolver.bfsTreeNodeLayers.get(4);
        for (MazeSolver.BFSTreeNode node : actualLayer4) {
            if (node.parent.value.intValue() == 12) {
                assertEquals(12, node.parent.value.intValue());
            }
            if (node.parent.value.intValue() == 10) {
                assertEquals(10, node.parent.value.intValue());
            }
            if (node.parent.value.intValue() == 11) {
                assertEquals(11, node.parent.value.intValue());
            }
        }
        
        assertEquals(MazeSolver.bfsTreeNodeLayers.size(), 8);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testFindPathNullList() {
        MazeSolver.findPath(null, 0, 1, 2);
    }
    
    @Test
    public void testFindPath() {
        int srcVertex = 0;
        MazeSolver.bfs(g2, srcVertex);
        List<Coordinate> soln = MazeSolver.findPath(MazeSolver.bfsTreeNodeLayers, 0, 3, 4);
        assertEquals(4, soln.size());
    }
    
    @Test
    public void testCreateGraphRepMaze1() {
        int[][] testMaze1 = {
            {0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0},
            {0, 0, 0, 0, 1},
            {0, 1, 1, 0, 1}
        };
        MazeSolver.createGraphRep(testMaze1);
        Graph actual = MazeSolver.graphRep;
        assertFalse(actual.hasEdge(0, 5));
        assertTrue(actual.hasEdge(6, 11));
        assertTrue(actual.hasEdge(11, 6));
    }
    
    @Test public void testCreateGraphRepMaze2() {
        int[][] testMaze2 = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        };
        MazeSolver.createGraphRep(testMaze2);
        Graph actual = MazeSolver.graphRep;
        assertFalse(actual.hasEdge(0, 5));
        assertTrue(actual.hasEdge(3, 7));
        assertTrue(actual.hasEdge(7, 3));
        assertTrue(actual.hasEdge(9, 10));
        assertTrue(actual.hasEdge(10, 9));
    }
    
    @Test
    public void testGetShortestPathMaze2() {
        int[][] testMaze2 = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        };
        List<Coordinate> soln = MazeSolver.getShortestPath(testMaze2, 
                new Coordinate(0, 0), new Coordinate(0, 3));
        Coordinate finalCoord = soln.get(soln.size() - 1);
        assertEquals(0, finalCoord.getX());
        assertEquals(3, finalCoord.getY());
        assertEquals(4, soln.size());
        assertEquals(0, soln.get(1).getX());
        assertEquals(1, soln.get(1).getY());
    }
    
    @Test
    public void testGetShortestPathSameStartAndFinish() {
        int[][] testMaze2 = {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        };
        List<Coordinate> soln = MazeSolver.getShortestPath(testMaze2, 
                new Coordinate(0, 0), new Coordinate(0, 0));
        assertEquals(1, soln.size());
        assertEquals(0, soln.get(0).getX());
        assertEquals(0, soln.get(0).getY());
    }
    
    @Test
    public void testGetShortestPathNoSoln() {
        int[][] m = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        List<Coordinate> soln = MazeSolver.getShortestPath(m, 
                new Coordinate(0, 0), new Coordinate(0, 2));
        assertTrue(soln.isEmpty());
    }
}
