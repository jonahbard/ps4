import java.util.*;

public class BaconGame {

    BaconGraph baconGraph;
    Graph<String, Set<String>> bfsGraph;
    String currentCenter = "Kevin Bacon";

    private static final boolean testing = true;

    private int getConnectionsToCenter(){
        return baconGraph.getGraph().numVertices()-BaconGraphLib.missingVertices(baconGraph.getGraph(), bfsGraph).size();
    }

    private double getAvgSeparation(){
        return BaconGraphLib.averageSeparation(bfsGraph, currentCenter);
    }


    private void printIntroDialouge() {
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
        System.out.println();
    }

    private void printCurrentCenterDialog(){
        System.out.println(currentCenter + " is now the center of the acting universe, connected to " +
                getConnectionsToCenter() + "/"+ baconGraph.getGraph().numVertices() +
                " actors with average separation " + getAvgSeparation());
    }


    private void printPathFromName(String name){
        List<String> path = BaconGraphLib.getPath(bfsGraph, name);

        if (path.isEmpty()) {
            System.out.println("There is no connection");
            return;
        }

        System.out.println(name + "'s number is " + (path.size() - 1));

        for (int i = 0; i < path.size() - 1; i++){
            String actor1 = path.get(i), actor2 = path.get(i+1);
            String movies = baconGraph.getGraph().getLabel(actor1, actor2).toString();
            System.out.println(actor1 + " appeared in " + movies + " with " + actor2);
        }
    }

    private void printTopCenters(int numCenters) {
        Map<String, Double> actorToAvgPathLength = new HashMap<>();
        Graph<String, Set<String>> baconUniverseGraph = BaconGraphLib.bfs(baconGraph.getGraph(), "Kevin Bacon");
        for (String actor : baconUniverseGraph.vertices()) {
            Graph<String, Set<String>> tree = BaconGraphLib.bfs(baconGraph.getGraph(), actor);
            actorToAvgPathLength.put(actor, BaconGraphLib.averageSeparation(tree, actor));
        }

        PriorityQueue<String> pq = new PriorityQueue<>((a,b) -> numCenters < 0 ?
                actorToAvgPathLength.get(b).compareTo(actorToAvgPathLength.get(a)) :
                actorToAvgPathLength.get(a).compareTo(actorToAvgPathLength.get(b)));

        for (String actor: actorToAvgPathLength.keySet()) {
            pq.add(actor);
        }

        for(int i = 0; i < Math.abs(numCenters) && !pq.isEmpty(); i++) {
            String actor = pq.poll();
            System.out.println(actor + ": average seperation " + actorToAvgPathLength.get(actor));
        }
    }

    private void printPrompt() {
        System.out.println("\n"+currentCenter + " game >");
    }

    private void printInfiniteSeperationActors() {
        System.out.println("actors with infinite seperation: ");
        for (String actor : BaconGraphLib.missingVertices(baconGraph.getGraph(), bfsGraph)) {
            System.out.println(actor);
        }
    }

    private void changeCenter(String newCenter) {
        currentCenter = newCenter;
        bfsGraph = BaconGraphLib.bfs(baconGraph.getGraph(), currentCenter);
        printCurrentCenterDialog();
    }

    private void printActorsByDegree(int low, int high) {
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a,b) -> Integer.compare(baconGraph.getGraph().outDegree(b), baconGraph.getGraph().outDegree(a))
            );

        for(String actor : baconGraph.getGraph().vertices()) {
            pq.add(actor);
        }

        System.out.println("Actors sorted by degree decrementally bound by selected high and low");
        while (!pq.isEmpty()) {
            String actor = pq.poll();
            if (baconGraph.getGraph().outDegree(actor) >= low  && baconGraph.getGraph().outDegree(actor) <= high) {
                System.out.println(actor + ": out degree " + baconGraph.getGraph().outDegree(actor));
            }
        }
    }

    private void printActorsBySeperation(int low, int high) {

    }

    private void runGame(){
        printIntroDialouge();

        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        while (gameRunning) {
            printPrompt();
            String n = scanner.nextLine();

            if (n.equals("q")){
                System.out.println("Game ended");
                gameRunning = false;
            } else if(n.equals("i")) {
                printInfiniteSeperationActors();
            } else if (n.startsWith("u ")) {
                changeCenter(n.substring(2));
            } else if (n.startsWith("p ")){
                printPathFromName(n.substring(2));
            } else if (n.startsWith("c ")) {
                printTopCenters(Integer.parseInt(n.substring(2)));
            } else if (n.startsWith("d ")) {
                String[] parts = n.substring(2).split(" ");
                int low = Integer.parseInt(parts[0]), high = Integer.parseInt(parts[1]);
                printActorsByDegree(low, high);
            } else if (n.startsWith("s ")) {
                String[] parts = n.substring(2).split(" ");
                int low = Integer.parseInt(parts[0]), high = Integer.parseInt(parts[1]);
                printActorsBySeperation(low, high);
            }
        }

        scanner.close();
    }

    public BaconGame() {
        baconGraph = new BaconGraph(testing);
        bfsGraph = BaconGraphLib.bfs(baconGraph.getGraph(), currentCenter);
    }

    public static void main(String[] args){
        BaconGame bg = new BaconGame();
        bg.runGame();
    }
}
