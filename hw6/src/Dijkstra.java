import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;

/**
 * Provides access to Dijkstra's algorithm for a weighted graph.
 */
final public class Dijkstra {
    
    static class TreeNode {
        
        Integer id;
        Integer distance;
        TreeNode parent;
        
        TreeNode() {
        }
        
        TreeNode(Integer v) {
            this.id = v;
        }
        TreeNode(Integer v, Integer d) {
            this.id = v;
            this.distance = d;
        }
        
        TreeNode(Integer v, TreeNode parent) {
            this.id = v;
            this.parent = parent;
        }
    }

    static List<TreeNode> nodes;
    static TreeNode outputTree;
    static BinaryMinHeapImpl<Integer, TreeNode> minHeap;

    private Dijkstra() {}

    /**
     * Computes the shortest path between two nodes in a weighted graph.
     * Input graph is guaranteed to be valid and have no negative-weighted edges.
     *
     * @param g   the weighted graph to compute the shortest path on
     * @param src the source node
     * @param tgt the target node
     * @return an empty list if there is no path from {@param src} to {@param tgt}, otherwise an
     * ordered list of vertices in the shortest path from {@param src} to {@param tgt},
     * with the first element being {@param src} and the last element being {@param tgt}.
     * If {@param src} = {@param tgt}, you should return a SINGLETON list.
     */
    public static List<Integer> getShortestPath(Graph g, int src, int tgt) {
        
        // Handle case where tgt = src
        if (tgt == src) {
            List<Integer> path = new ArrayList<>();
            path.add(tgt);
            return path;
        }

        minHeap = new BinaryMinHeapImpl<Integer, TreeNode>();
        nodes = new ArrayList<>();
        
        // Populate minHeap
        for (int i = 0; i < g.listRep.length; i++) {
            
            if (i == src) {
                TreeNode node = new TreeNode(i, 0);
                nodes.add(node);
                minHeap.add(0, node);
                continue;
            }
            
            TreeNode node = new TreeNode(i, Integer.MAX_VALUE);
            nodes.add(node);
            minHeap.add(Integer.MAX_VALUE, node);
        }
        
        // Set outputTree to the source node
        outputTree = (TreeNode) minHeap.peek().value;
        
        dijkstra(g, src, tgt);
        
        TreeNode tgtNode = new TreeNode();
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        List<Integer> path = new ArrayList<>();
        
        // Find tgtNode
        for (TreeNode node : nodes) {
            if (node.id == tgt) {
                tgtNode = node;
                break;
            }
        }
        
        deque.push(tgtNode.id);
        
        // Travel to source node via current node's parent.
        while (tgtNode.parent != null) {
            deque.push(tgtNode.parent.id);
            tgtNode = tgtNode.parent;
        }
        
        // Handle case where there is no path from src to tgt.
        if (tgtNode.id != src) {
            return path;
        }
        
        // Pop from deque and add to path.
        while (!deque.isEmpty()) {
            path.add(deque.pop());
        }
        
        return path;
    }
    
    // Function that implements Dijkstra's algorithm
    static void dijkstra(Graph g, int src, int tgt) {

        TreeNode currNode;
        boolean tgtNodeFound = false;
        
        // Run Dijkstra. The Dijkstra tree should also be created as the algorithm runs, 
        // with outputTree being the root node.
        while (!minHeap.isEmpty()) {
            BinaryMinHeapImpl.Entry<Integer, TreeNode> nearestNode = minHeap.extractMin();
            currNode = nearestNode.value;
            int currDist = nearestNode.key;
            Set<Integer> neighbors = g.outNeighbors(currNode.id);
            
            for (Integer neighbor : neighbors) {
                
                TreeNode neighborNode = nodes.get(neighbor);
                int neighborDist = neighborNode.distance;
                int weight = g.getWeight(currNode.id, neighbor);
                
                if (neighborDist > currDist + weight) {
                    neighborDist = currDist + weight;
                    minHeap.decreaseKey(neighborNode, neighborDist);
                    neighborNode.parent = currNode;
                    neighborNode.distance = neighborDist;
                }
                
                if (neighbor == tgt) {
                    tgtNodeFound = true;
                    break;
                }
            }
            
            if (tgtNodeFound) {
                break;
            }
        }
    }
}
