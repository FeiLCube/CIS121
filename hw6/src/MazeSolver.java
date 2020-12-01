import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayDeque;

final public class MazeSolver {
    
    static class BFSTreeNode {
        
        Integer value;
        BFSTreeNode parent;
        
        BFSTreeNode() {
        }
        
        BFSTreeNode(Integer v) {
            this.value = v;
        }
        
        BFSTreeNode(Integer v, BFSTreeNode parent) {
            this.value = v;
            this.parent = parent;
        }
    }
    
    static Graph graphRep = new Graph(1);
    static HashMap<Coordinate, Integer> discovered = new HashMap<>();
    static List<Set<BFSTreeNode>> bfsTreeNodeLayers;
    
    private MazeSolver() {}

    /**
     * Returns a list of coordinates on the shortest path from {@code src} to {@code tgt}
     * by executing a breadth-first search on the graph represented by a 2D-array of size m x n.
     * <p>
     * Input {@code maze} guaranteed to be a non-empty and valid matrix.
     * Input {@code src} and {@code tgt} are guaranteed to be valid, in-bounds, and not blocked.
     * <p>
     * Do NOT modify this method header.
     *
     * @param maze the input maze, which is a 2D-array of size m x n
     * @param src The starting Coordinate of the path on the matrix
     * @param tgt The target Coordinate of the path on the matrix
     * @return an empty list if there is no path from {@param src} to {@param tgt}, otherwise an
     * ordered list of vertices in the shortest path from {@param src} to {@param tgt},
     * with the first element being {@param src} and the last element being {@param tgt}.
     * If {@param src} = {@param tgt}, you should return a SINGLETON list.
     * @implSpec This method should run in worst-case O(m x n) time.
     */
    public static List<Coordinate> getShortestPath(int[][] maze, Coordinate src, Coordinate tgt) {
        discovered.clear();
        bfsTreeNodeLayers = new ArrayList<>();
        
        createGraphRep(maze);
        
        int x = src.getX();
        int y = src.getY();
        int srcVertex = x * maze[x].length + y;
        
        bfs(graphRep, srcVertex);
        
        x = tgt.getX();
        y = tgt.getY();
        int tgtVertex = x * maze[x].length + y;
        
        return findPath(bfsTreeNodeLayers, srcVertex, tgtVertex, maze[0].length);
    }
    
    // Function that creates the graph representation of the maze.
    static void createGraphRep(int[][] maze) {
        
        graphRep = new Graph(maze.length * maze[0].length);
        
        // Traverse through the maze, and add edges to the graph based on maze pathing.
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y] == 0) {
                    int currVertex = (x) * maze[x].length + y;
                    
                    // Add appropriate edges
                    if (inBounds(maze, x, y - 1)) {
                        if (maze[x][y - 1] == 0) {
                            int leftVertex = (x) * maze[x].length + y - 1;
                            graphRep.addEdge(currVertex, leftVertex, 1);
                        }
                    }
                    
                    if (inBounds(maze, x, y + 1)) {
                        if (maze[x][y + 1] == 0) {
                            int rightVertex = (x) * maze[x].length + y + 1;
                            graphRep.addEdge(currVertex, rightVertex, 1);
                        }
                    }
                    
                    if (inBounds(maze, x - 1, y)) {
                        if (maze[x - 1][y] == 0) {
                            int downVertex = (x - 1) * maze[x].length + y;
                            graphRep.addEdge(currVertex, downVertex, 1);
                        }
                    }
                    
                    if (inBounds(maze, x + 1, y)) {
                        if (maze[x + 1][y] == 0) {
                            int upVertex = (x + 1) * maze[x].length + y;
                            graphRep.addEdge(currVertex, upVertex, 1);
                        }
                    }
                }
            }
        }
    }
    
    // Helper function for createGraphRep, checks if a coordinate is within the bounds of the maze.
    static boolean inBounds(int[][] maze, int x, int y) {
        return (x >= 0 && y >= 0 && x <= maze.length - 1 && y <= maze[0].length - 1);
    }
    
    // Function that runs BFS. Inputs are the graph and the source vertex.
    static void bfs(Graph g, int srcVertex) {
        if (g == null) {
            throw new IllegalArgumentException();
        }
        
        List<Set<BFSTreeNode>> layers = new ArrayList<>();
        int[] discovered = new int[g.getSize()];
        discovered[srcVertex] = 1;
        
        int layer = 0;
        
        layers.add(new HashSet<BFSTreeNode>());
        BFSTreeNode source = new BFSTreeNode(srcVertex);
        layers.get(0).add(source);
        
        while (!layers.get(layer).isEmpty()) {
            layers.add(new HashSet<BFSTreeNode>());
            
            for (BFSTreeNode node : layers.get(layer)) {
                Integer vertex = node.value;
                Set<Integer> neighbors = g.outNeighbors(vertex);
                
                for (Integer neighborVertex : neighbors) {
                    if (discovered[neighborVertex] == 0) {
                        discovered[neighborVertex] = 1;
                        BFSTreeNode currNode = new BFSTreeNode(neighborVertex, node);
                        layers.get(layer + 1).add(currNode);
                    }
                }
            }
            
            layer++;
        }
        
        layers.remove(layers.size() - 1);
        bfsTreeNodeLayers = layers;
    }
    
    static List<Coordinate> findPath(List<Set<BFSTreeNode>> layers, 
            int srcVertex, int tgtVertex, int mazeWidth) {
        if (layers == null) {
            throw new IllegalArgumentException();
        }
        
        List<Coordinate> soln = new ArrayList<>();
        ArrayDeque<Coordinate> deque = new ArrayDeque<>();
        BFSTreeNode tgtNode = null;
        boolean tgtNodeFound = false;
        
        for (Set<BFSTreeNode> layer : bfsTreeNodeLayers) {
            
            for (BFSTreeNode node : layer) {
                if (node.value.equals(tgtVertex)) {
                    tgtNode = node;
                    tgtNodeFound = true;
                    break;
                }
            }
            
            if (tgtNodeFound) {
                break;
            }
        }
        
        if (tgtNode == null) {
            return soln;
        }
        
        Coordinate tgtCoord = calculateCoord(tgtVertex, mazeWidth);
        deque.push(tgtCoord);
        
        while (tgtNode.parent != null) {
            tgtNode = tgtNode.parent;
            Coordinate c = calculateCoord(tgtNode.value, mazeWidth);
            deque.push(c);
        }
        
        Coordinate c = deque.pop();
        soln.add(c);
        
        while (!deque.isEmpty()) {
            c = deque.pop();
            soln.add(c);
        }
        
        return soln;
    }
    
    // Helper function to calculate the coordinates from the value of a BFSTreeNode
    static Coordinate calculateCoord(int value, int mazeWidth) {
        int x = value / mazeWidth;
        int y = value % mazeWidth;
        return new Coordinate(x, y);
    }
}