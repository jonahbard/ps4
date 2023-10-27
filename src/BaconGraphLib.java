import java.util.*;

public class BaconGraphLib {

    // TODO: implement all these methods

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
            for (V v: g.outNeighbors(curV)) {
                if (!visited.contains(v)) {
                    toVisit.add(v);
                    visited.add(v);
                    outG.insertDirected(curV, v, g.getLabel(curV, v));
                }
            }
        }

        return outG;
    }
    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {

        return null;
    }
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {

        return null;
    }
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {

        return 0.0;
    }
}
