import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BaconGame {

    static BaconGraph baconGraph;

    static AdjacencyMapGraph bfsGraph;
    static String currentCenter = "Kevin Bacon";

    private static int getConnectionsToCenter(){
        //return BaconGraphLib.getSumOfEdgesToEachChild(baconGraph.graph, currentCenter, 0);
        return 0;
    }

    private static double getAvgSeparationFromCenter(){
        //return BaconGraphLib.averageSeparation(baconGraph.graph, currentCenter);
        return 0;

    }

    private static void printCurrentCenterDialog(){
        System.out.println(currentCenter + " is now the center of the acting universe, connected to " +
                getConnectionsToCenter() + "/9235 actors with average separation "+ getAvgSeparationFromCenter());
    }

    private static void printPathFromName(String name){
        System.out.println("Path from " + name + " to " + currentCenter + ":");
        List<String> path = BaconGraphLib.getPath(bfsGraph, name);
        for (String n: path){
            System.out.println(n);
        }
    }

    private static void runGame(){
        boolean gameRunning = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Game starting!");
        printCurrentCenterDialog();
        while (gameRunning) {
            String n = scanner.nextLine();
            if (n.equals("q")){
                System.out.println("Game ended");
                gameRunning = false;
            } else if (n.substring(0, 2).equals("u ")){
                currentCenter = n.substring(2);
                bfsGraph = (AdjacencyMapGraph) BaconGraphLib.bfs(baconGraph.graph, currentCenter);
                printCurrentCenterDialog();
            } else if (n.substring(0, 2).equals("p ")){
                printPathFromName(n.substring(2));
            }
        }
        scanner.close();

    }
    public static void main(String[] args){
        //make the graph
        baconGraph = new BaconGraph();
        bfsGraph = (AdjacencyMapGraph) BaconGraphLib.bfs(baconGraph.graph, currentCenter);
        runGame();

    }

}
