import java.util.*;

/**
 * Code to run the Bacon Game.
 * @author Jonah Bard, Daniel Katz
 */

public class BaconGame {

    BaconGraph baconGraph;
    Graph<String, Set<String>> bfsGraph;
    String currentCenter = "Kevin Bacon";

    // This boolean determines what data set is used for the live run of the game.
    private static final boolean testing = false;

    /**
     * Get the number of vertices that are connected to the center of the universe
     * @return
     */
    private int getConnectionsToCenter(){
        return bfsGraph.numVertices() - 1;
    }

    /**
     * Get average path length on current center
     * @return
     */
    private double getAvgSeparation(){
        return BaconGraphLib.averageSeparation(bfsGraph, currentCenter);
    }

    /**
     * Print the prompt
     */
    void printPrompt() {
        System.out.println("\n"+currentCenter + " game >");
    }

    /**
     * Print intro dialog and correct spacing
     */
    void printIntroDialouge() {
        System.out.println("Commands:\n" +
                "c <#>: list top (positive number) or bottom (negative) <#> centers of the Bacon connected universe," +
                "sorted by average separation\n" +
                "d <low> <high>: list actors sorted by degree, with degree between low and high\n" +
                "i: list actors with infinite separation from the current center\n" +
                "p <name>: find path from <name> to current center of the universe\n" +
                "s <low> <high>: list actors sorted by non-infinite separation from the current center," +
                "with separation between low and high\n" +
                "u <name>: make <name> the center of the universe\n" +
                "q: quit game\n");

        printCurrentCenterDialog();
    }

    /**
     * Print context about the current center of the universe
     */
    void printCurrentCenterDialog(){
        System.out.println(currentCenter + " is now the center of the acting universe, connected to " +
                getConnectionsToCenter() + "/"+ baconGraph.getGraph().numVertices() +
                " actors with average separation " + getAvgSeparation());
    }

    /**
     * Change which actor is in the center of the universe
     * @param newCenter
     */
    public void changeCenter(String newCenter) {
        if (!baconGraph.getGraph().hasVertex(newCenter)) {
            System.out.println(newCenter + " is not a valid actors name.");
            return;
        }
        currentCenter = newCenter;
        bfsGraph = BaconGraphLib.bfs(baconGraph.getGraph(), currentCenter);
        printCurrentCenterDialog();
    }

    /**
     * Print path from the selected actor to the center of the universe
     * @param name
     */
    void printPathFromName(String name){
        List<String> path = BaconGraphLib.getPath(bfsGraph, name);

        // Make sure a valid path exists
        if (path.isEmpty()) {
            System.out.println("There is no connection");
            return;
        }

        System.out.println(name + "'s number is " + (path.size() - 1));

        // Print out the path in order
        for (int i = 0; i < path.size() - 1; i++){
            String actor1 = path.get(i), actor2 = path.get(i+1);
            String movies = baconGraph.getGraph().getLabel(actor1, actor2).toString();
            System.out.println(actor1 + " appeared in " + movies + " with " + actor2);
        }
    }

    /**
     * Print the top centers of the universe based on average path length
     * @param numCenters
     */
    void printTopCenters(int numCenters) {
        Map<String, Double> actorToAvgPathLength = new HashMap<>();

        // Compute  average path length for each actor in the Kevin Bacon Universe
        Graph<String, Set<String>> baconUniverseGraph = BaconGraphLib.bfs(baconGraph.getGraph(), "Kevin Bacon");
        for (String actor : baconUniverseGraph.vertices()) {
            Graph<String, Set<String>> tree = BaconGraphLib.bfs(baconGraph.getGraph(), actor);
            actorToAvgPathLength.put(actor, BaconGraphLib.averageSeparation(tree, actor));
        }

        // Create pq with comparator to be increasing or decreasing based on if the number is negative or not
        PriorityQueue<String> pq = new PriorityQueue<>((a,b) -> numCenters < 0 ?
                actorToAvgPathLength.get(b).compareTo(actorToAvgPathLength.get(a)) :
                actorToAvgPathLength.get(a).compareTo(actorToAvgPathLength.get(b)));

        for (String actor: actorToAvgPathLength.keySet()) {
            pq.add(actor);
        }

        // For the number of centers inputted, print that many while ensuring there are enough to do so
        for(int i = 0; i < Math.abs(numCenters) && !pq.isEmpty(); i++) {
            String actor = pq.poll();
            System.out.println(actor + ": average separation " + actorToAvgPathLength.get(actor));
        }
    }


    /**
     * Print actors which are not in the same network as the current center of the universe
     */
    public void printInfiniteSeperationActors() {
        System.out.println("actors with infinite separation: ");
        for (String actor : BaconGraphLib.missingVertices(baconGraph.getGraph(), bfsGraph)) {
            System.out.println(actor);
        }
    }

    /**
     * Print actors by how networked they are and bound by inputs
     * @param low
     * @param high
     */
    public void printActorsByDegree(int low, int high) {
        // Create max PQ based on the out degree of an actor
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a,b) -> Integer.compare(baconGraph.getGraph().outDegree(b), baconGraph.getGraph().outDegree(a))
            );

        // Add all bounded actors to the pq
        for(String actor : baconGraph.getGraph().vertices()) {
            if (baconGraph.getGraph().outDegree(actor) >= low && baconGraph.getGraph().outDegree(actor) <= high) {
                pq.add(actor);
            }
        }

        // Print out actors
        System.out.println("Actors sorted by degree decrementally bound by selected high and low");
        while (!pq.isEmpty()) {
            String actor = pq.poll();
            System.out.println(actor + ": out degree " + baconGraph.getGraph().outDegree(actor));
        }
    }

    /**
     * Print actors by separation from the center of the universe bounded by inputs
     * @param low
     * @param high
     */
    public void printActorsBySeparation(int low, int high) {
        // Use a priority queue with a comparator that sorts actors by their separation.
        PriorityQueue<String> pq = new PriorityQueue<>((a, b) ->
                Integer.compare(
                        BaconGraphLib.getPath(bfsGraph, b).size() - 1,
                        BaconGraphLib.getPath(bfsGraph, a).size() - 1
                )
            );

        // Iterate through all vertices in the bfsGraph.
        for (String actor : bfsGraph.vertices()) {
            List<String> path = BaconGraphLib.getPath(bfsGraph, actor);
            // Check for non-null and non-empty path, then get separation.
            if (!path.isEmpty()) {
                int separation = path.size() - 1;
                // Add the actor to the priority queue if the separation is within the range.
                if (separation >= low && separation <= high) {
                    pq.add(actor);
                }
            }
        }

        // Print actors and their separations.
        System.out.println("Actors sorted by separation decrementally bound by selected high and low");
        while (!pq.isEmpty()) {
            String actor = pq.poll();
            int separation = BaconGraphLib.getPath(bfsGraph, actor).size() - 1;
            System.out.println(actor + ": separation " + separation);
        }
    }

    /**
     * Run the game on a loop
     */
    private void runGame(){
        printIntroDialouge();

        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        // Main loop that runs the game
        while (gameRunning) {
            printPrompt();
            String n = scanner.nextLine();

            // if an input is misprocessed, tell the user to try again
            try {
                if (n.equals("q")){
                    System.out.println("Game ended");
                    gameRunning = false;
                } else if(n.equals("i")) {
                    printInfiniteSeperationActors();
                } else if (n.startsWith("u ")) {
                    changeCenter(n.substring(2)); // extract name
                } else if (n.startsWith("p ")){
                    printPathFromName(n.substring(2).trim()); // extract name
                } else if (n.startsWith("c ")) {
                    printTopCenters(Integer.parseInt(n.substring(2))); // extract number
                } else if (n.startsWith("d ")) {
                    String[] parts = n.substring(2).split(" "); // extract numbers
                    if (parts.length != 2) throw new Exception("invalid numbers");
                    int low = Integer.parseInt(parts[0]), high = Integer.parseInt(parts[1]);
                    printActorsByDegree(low, high);
                } else if (n.startsWith("s ")) {
                    String[] parts = n.substring(2).split(" ");  // extract numbers
                    if (parts.length != 2) throw new Exception("invalid numbers");
                    int low = Integer.parseInt(parts[0]), high = Integer.parseInt(parts[1]);
                    printActorsBySeparation(low, high);
                } else {
                    System.out.println("Try a valid prompt");
                }
            }
            catch (Exception e) {
                System.out.println("Try a valid prompt");
            }
        }

        scanner.close();
    }

    /**
     * Constructor for the game running class
     */
    public BaconGame() {
        baconGraph = new BaconGraph(testing);
        bfsGraph = BaconGraphLib.bfs(baconGraph.getGraph(), currentCenter);
    }

    /**
     * Constructor for testing purposes
     * @param testParam
     * @param specificCenter
     */
    public BaconGame(boolean testParam, String specificCenter) {
        baconGraph = new BaconGraph(testParam);
        bfsGraph = BaconGraphLib.bfs(baconGraph.getGraph(), specificCenter);
    }

    /**
     * Run the game
     * @param args
     */
    public static void main(String[] args){
        BaconGame bg = new BaconGame();
        bg.runGame();
    }
}
