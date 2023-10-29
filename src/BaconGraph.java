import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class BaconGraph {

    //
    public AdjacencyMapGraph graph;
    private Map<Integer, String> actorIDs;
    private Map<Integer, String> movieIDs;
    private Map<String, Set<String>> moviesToActors;


    public void setActorIDs(){
        actorIDs = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("inputs/actorsTest.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                actorIDs.put(Integer.parseInt(split[0]), split[1]);
            }
            br.close();
        } catch(Exception e) {
            System.out.println("fuck");
        }
    }

    public void setMovieIDs() {
        movieIDs = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("inputs/moviesTest.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                movieIDs.put(Integer.parseInt(split[0]), split[1]);
            }
            br.close();
        } catch(Exception e) {
            System.out.println("fuck");
        }
    }


    public void setMoviesToActors() {
        moviesToActors = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("inputs/movie-actorsTest.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                String movie = movieIDs.get(Integer.parseInt(split[0]));
                String actor = actorIDs.get(Integer.parseInt(split[1]));
                if (moviesToActors.containsKey(movie)){
                    moviesToActors.get(movie).add(actor);
                } else {
                    Set<String> actors = new HashSet();
                    actors.add(actor);
                    moviesToActors.put(movie, actors);
                }
            }
            br.close();
        } catch(Exception e) {
            System.out.println("fuck");
        }
    }

    public void createGraph(){
        graph = new AdjacencyMapGraph<>();

        //insert all actors as vertices
        for (String s : actorIDs.values()) {
            graph.insertVertex(s);
        }
        //insert all edges
        for (String movie : moviesToActors.keySet()) { // for each movie
            for (Object actor1 : moviesToActors.get(movie)) { // for each actor in movie
                //for every other actor, add undirected edge labeled with movie
                for (Object actor2 : moviesToActors.get(movie)) {
                    if (!actor2.equals(actor1)) {
                        graph.insertUndirected(actor1, actor2, movie);
                    }
                }
            }
        }
    }

    // TODO: Does just the graph live here or also the methods to drive the game

    // TODO: Create the code to instantiate the graph based on the input files
    public BaconGraph() {
        setActorIDs();
        setMovieIDs();
        setMoviesToActors();
        createGraph();
    }


    public static void main(String[] args) {
        BaconGraph baconGraph = new BaconGraph();
        System.out.println(baconGraph.graph);
    }

}
