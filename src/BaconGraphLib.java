import java.util.*;

/**
 * Graph library code.
 * @author Jonah Bard, Daniel Katz
 */

public class BaconGraphLib {

    /**
     * Create graph-based tree modeling BFS traversal
     * @param g
     * @param source
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        Graph<V, E> outG = new AdjacencyMapGraph<>();

        // If the source is not present in the original graph, return an empty graph
        if (!g.hasVertex(source)) return outG;

        Set<V> visited = new HashSet<>();
        Queue<V> toVisit = new LinkedList<>();

        // Set up based on initial vertex
        outG.insertVertex(source);
        visited.add(source);
        toVisit.add(source);


        // Primary BFS loop
        while (!toVisit.isEmpty()) {
            V curV = toVisit.remove();
            for (V nextV: g.outNeighbors(curV)) {
                if (!visited.contains(nextV)) {
                    // Add neighboring vertices after
                    toVisit.add(nextV);
                    visited.add(nextV);
                    outG.insertVertex(nextV);

                    // Directed edge to show movement from vertex to vertex during exploration
                    outG.insertDirected(curV, nextV, g.getLabel(curV, nextV));
                }
            }
        }

        return outG;
    }

    /**
     * Return the path from specified vertex to the root of the tree
     * @param tree = the tree that bfs returns (shortest path from start node to each other node)
     * @param v = the vertex we're trying to reach
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
        List<V> path = new ArrayList<>();

        // If the vertex was not found during BFS, return an empty path.
        if (!tree.hasVertex(v)) return path;

        // Do necessary one-time setup
        path.add(v);
        V currentV = v;

        // Loop until the end of the tree
        while (tree.inDegree(currentV) == 1) {
            // this loop will only ever run once
            for (V parentV: tree.inNeighbors(currentV)) {
                path.add(parentV);
                currentV = parentV;
            }
        }

        return path;
    }

    /**
     * Return the set of vertices that are in the graph but were not found during BFS
     * @param graph
     * @param subgraph
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        // Create a set of the tree vertices
        Set<V> subgraphSet = new HashSet<>();
        for(V v : subgraph.vertices()) subgraphSet.add(v);

        // If a vertex is in the general one but not the tree one, it is not discoverable from tree root node
        Set<V> differenceSet = new HashSet<>();
        for(V v : graph.vertices()) {
            if (!subgraphSet.contains(v)) differenceSet.add(v);
        }
        return differenceSet;
    }

    /**
     * Figure out the average path length to each reachable node from the root of BFS exploration
     * @param tree
     * @param root
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        double numberOfEdges = getPathSum(tree, root, 0);
        return numberOfEdges / (double) (tree.numVertices() - 1);

    }

    /**
     * Helper method to recursively traverse the tree while keeping track of exisitng distance
     * @param tree
     * @param parent
     * @param depth
     * @return
     * @param <V>
     * @param <E>
     */
    private static <V,E> int getPathSum(Graph<V,E> tree, V parent, int depth) {
        int sum = depth;
        for (V v : tree.outNeighbors(parent)) {
            sum += getPathSum(tree, v, depth + 1);
        }
        return sum;
    }


}
