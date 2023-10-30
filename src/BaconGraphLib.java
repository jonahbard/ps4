import java.util.*;

public class BaconGraphLib {

    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        Graph<V, E> outG = new AdjacencyMapGraph<>();
        if (!g.hasVertex(source)) return outG;

        Set<V> visited = new HashSet<>();
        Queue<V> toVisit = new LinkedList<>();

        outG.insertVertex(source);
        visited.add(source);
        toVisit.add(source);

        while (!toVisit.isEmpty()) {
            V curV = toVisit.remove();
            for (V nextV: g.outNeighbors(curV)) {
                if (!visited.contains(nextV)) {
                    toVisit.add(nextV);
                    visited.add(nextV);
                    outG.insertVertex(nextV);
                    outG.insertDirected(curV, nextV, g.getLabel(curV, nextV));
                }
            }
        }

        return outG;
    }

    /***
     * @param tree = the tree that bfs returns (shortest path from start node to each other node)
     * @param v = the vertex we're trying to reach
     * @return
     * @param <V>
     * @param <E>
     */
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
        List<V> path = new ArrayList<>();
        if (!tree.hasVertex(v)) return path;
        path.add(v);
        V currentV = v;
        while (tree.inDegree(currentV) == 1) {
            for (V parentV: tree.inNeighbors(currentV)) {
                path.add(parentV);
                currentV = parentV;
            }
        }

        return path;
    }

    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        Set<V> subgraphSet = new HashSet<>();
        for(V v : subgraph.vertices()) subgraphSet.add(v);

        Set<V> differenceSet = new HashSet<>();
        for(V v : graph.vertices()) {
            if (!subgraphSet.contains(v)) differenceSet.add(v);
        }
        return differenceSet;
    }

    /***
     * the recursive way
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

    private static <V,E> int getPathSum(Graph<V,E> tree, V parent, int depth) {
        int sum = depth;
        for (V v : tree.outNeighbors(parent)) {
            sum += getPathSum(tree, v, depth + 1);
        }
        return sum;
    }


}
