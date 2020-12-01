import java.util.HashSet;
import java.util.Set;

final public class IslandBridge {

    static Set<Integer> finishedNodes = new HashSet<>();
    static Set<Integer> unfinishedNodes = new HashSet<>();
        
    private IslandBridge() {}

    /**
     * See question details in the write-up. Input is guaranteed to be valid.
     *
     * @param g the input graph representing the islands as vertices and bridges as directed edges
     * @param x the starting node
     * @return true, if no matter how you navigate through the one-way bridges from node x,
     * there is always a way to drive back to node x, and false otherwise.
     * @implSpec This method should run in worst-case O(n + m) time.
     */
    public static boolean allNavigable(Graph g, int x) {
        
        // Handle edge case where the starting vertex is a sink.
        if (g.outNeighbors(x).isEmpty()) {
            return true;
        }
        
        finishedNodes.clear();
        unfinishedNodes.clear();
        
        finishedNodes.add(x);
        
        searchGraph(g, x);
        
        unfinishedNodes.remove(x);
        return (unfinishedNodes.isEmpty());
    }
    
    // This function is modeled after DFS with a few modifications
    static boolean searchGraph(Graph g, int srcVertex) {
        
        Set<Integer> neighbors = g.outNeighbors(srcVertex);
        unfinishedNodes.add(srcVertex);
        boolean metFinishedNode = false;
        
        for (Integer neighbor : neighbors) {
            if (!finishedNodes.contains(neighbor)) {
                metFinishedNode = searchGraph(g, neighbor);
            } else {
                unfinishedNodes.remove(srcVertex);
                finishedNodes.add(srcVertex);
                return true;
            }
        }
        
        if (metFinishedNode) {
            unfinishedNodes.remove(srcVertex);
            finishedNodes.add(srcVertex);
            return true;
        }
        
        return false;
    }
}
