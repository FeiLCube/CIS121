import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashMap;

/**
 * Contains the API necessary for a simple, (optionally) weighted directed graph.
 * We call the graph "optionally weighted" because it can be used by algorithms that use weights
 * (like Dijkstra's) and by algorithms that do not (like BFS). An algorithm like BFS would simply
 * ignore any weights present.
 * <p>
 * By convention, the n vertices will be labeled 0,1,...,n-1. The edge weights can be any int value.
 * Since we are labeling vertices from 0 to n-1, you may find arrays/arraylists helpful!
 * Self loops and parallel edges are not allowed. Your implementation should use O(m + n) space.
 * Please DO NOT use adjacency matrices!
 * <p>
 * Also note that the runtimes given are expected runtimes. As a result, you should be
 * implementing your graph using a HashMap as the primary data structure for the adjacency list.
 * <p>
 * Notice that this class also supports undirected graph. Which means you can implement an
 * undirected graph as each undirected edge between u and v being two directed edge from u to v and
 * from v to u.
 */
public class Graph {
    
    HashMap<Integer, Integer>[] listRep;
    /**
     * Initializes a graph of size {@code n}. All valid vertices in this graph thus have integer
     * indices in the half-open range {@code [0, n)}, n > 0.
     * <p/>
     * Do NOT modify this constructor header.
     *
     * @param n the number of vertices in the graph
     * @throws IllegalArgumentException if {@code n} is zero or negative
     * @implSpec This method should run in expected O(n) time
     */
    @SuppressWarnings("unchecked")
    public Graph(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Graph has size zero or less.");
        }
        
        listRep = new HashMap[n];
        
        for (int i = 0; i < listRep.length; i++) {
            listRep[i] = new HashMap<Integer, Integer>();
        }
    }

    /**
     * Returns the number of vertices in the graph.
     * <p/>
     * Do NOT modify this method header.
     *
     * @return the number of vertices in the graph
     * @implSpec This method should run in expected O(1) time.
     */
    public int getSize() {
        return listRep.length;
    }

    /**
     * Determines if there's an directed edge from u to v.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u a vertex
     * @param v a vertex
     * @return {@code true} if the {@code u-v} edge is in this graph
     * @throws IllegalArgumentException if a specified vertex does not exist
     * @implSpec This method should run in expected O(1) time.
     */
    public boolean hasEdge(int u, int v) {
        if (u > listRep.length - 1 || u < 0) {
            throw new IllegalArgumentException("Vertex " + u + " does not exist.");
        } else if (v > listRep.length - 1 || v < 0) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        } else {
            return (listRep[u].containsKey(v));
        }
    }

    /**
     * Returns the weight of an the directed edge {@code u-v}.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u source vertex
     * @param v target vertex
     * @return the edge weight of {@code u-v}
     * @throws NoSuchElementException   if the {@code u-v} edge does not exist
     * @throws IllegalArgumentException if a specified vertex does not exist
     * @implSpec This method should run in expected O(1) time.
     */
    public int getWeight(int u, int v) {
        if (u > listRep.length - 1 || u < 0) {
            throw new IllegalArgumentException("Vertex " + u + " does not exist.");
        } else if (v > listRep.length - 1 || v < 0) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        } else if (!listRep[u].containsKey(v)) {
            throw new NoSuchElementException("There is no edge from " + "u" + " to " + "v.");
        } else {
            return listRep[u].get(v);
        }
    }

    /**
     * Creates an edge from {@code u} to {@code v} if it does not already exist. A call to this
     * method should <em>not</em> modify the edge weight if the {@code u-v} edge already exists.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param u      the source vertex to connect
     * @param v      the target vertex to connect
     * @param weight the edge weight
     * @return {@code true} if the graph changed as a result of this call, false otherwise (i.e., if
     * the edge is already present)
     * @throws IllegalArgumentException if a specified vertex does not exist or if u == v
     * @implSpec This method should run in expected O(1) time
     */
    public boolean addEdge(int u, int v, int weight) {
        if (u > listRep.length - 1 || u < 0) {
            throw new IllegalArgumentException("Vertex " + u + " does not exist.");
        } else if (v > listRep.length - 1 || v < 0) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        } else if (u == v) {
            throw new IllegalArgumentException(u + " = " + v);
        } else if (listRep[u].containsKey(v)) {
            if (listRep[u].get(v) == weight) {
                return false;
            }
        }
        
        listRep[u].put(v, weight);
        return true;
    }

    /**
     * Returns the out-neighbors of the specified vertex.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param v the vertex
     * @return all out neighbors of the specified vertex or an empty set if there are no out
     * neighbors
     * @throws IllegalArgumentException if the specified vertex does not exist
     * @implSpec This method should run in expected O(outdeg(v)) time.
     */
    public Set<Integer> outNeighbors(int v) {
        if (v > listRep.length || v < 0) {
            throw new IllegalArgumentException("Vertex " + v + " does not exist.");
        }
        
        return listRep[v].keySet();
    }
}