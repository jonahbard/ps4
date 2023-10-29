import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BaconGraphLibTestDriver {
    public static void main(String[] args) {
        Graph<String, Set<String>> g = new AdjacencyMapGraph<>();

        g.insertVertex("Kevin Bacon");
        g.insertVertex("Dartmouth (Earl thereof)");
        g.insertVertex("Alice");
        g.insertVertex("Bob");
        g.insertVertex("Charlie");
        g.insertVertex("Nobody");
        g.insertVertex("Nobody’s Friend");

        String aMovie = "A";
        String bMovie = "B";
        String cMovie = "C";
        String dMovie = "D";
        String eMovie = "E";
        String fMovie = "F";

        g.insertUndirected("Kevin Bacon", "Alice", new HashSet<>(Arrays.asList(aMovie, eMovie)));
        g.insertUndirected("Kevin Bacon", "Bob", new HashSet<>(Arrays.asList(aMovie)));
        g.insertUndirected("Alice", "Bob", new HashSet<>(Arrays.asList(aMovie)));
        g.insertUndirected("Alice", "Charlie", new HashSet<>(Arrays.asList(dMovie)));
        g.insertUndirected("Bob", "Charlie", new HashSet<>(Arrays.asList(cMovie)));
        g.insertUndirected("Charlie", "Dartmouth (Earl thereof)", new HashSet<>(Arrays.asList(bMovie)));
        g.insertUndirected("Nobody", "Nobody’s Friend", new HashSet<>(Arrays.asList(fMovie)));

        Graph<String, Set<String>> bfsTree =  BaconGraphLib.bfs(g, "Kevin Bacon");
        System.out.println(bfsTree);

        System.out.println(BaconGraphLib.getPath(bfsTree, "Dartmouth (Earl thereof)"));
        System.out.println(BaconGraphLib.averageSeparation(bfsTree, "Kevin Bacon"));
    }
}
