import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Code to instantiate the graph based on files stored
 * @author Jonah Bard, Daniel Katz
 */

public class BaconGraph {

    private Graph<String, Set<String>> graph;
    private Map<Integer, String> actorIDs;
    private Map<Integer, String> movieIDs;
    private Map<String, Set<String>> moviesToActors;

    // flag that determines if testing or general files are used
    private final boolean testing;

    /**
     * Create the BaconGraph with the testing param specified
     * @param testing
     */
    public BaconGraph(boolean testing) {
        this.testing = testing;

        setActorIDs();
        setMovieIDs();
        setMoviesToActors();
        createGraph();
    }

    /**
     * Return the underlying graph
     * @return
     */
    public Graph<String, Set<String>> getGraph() { return graph;}


    /**
     * Create the map of actor IDs to actor names
     */
    private void setActorIDs(){
        actorIDs = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/actorsTest.txt" : "inputs/actors.txt"));

            // For each line in the file, create the map entry after doing the necessary split.
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                actorIDs.put(Integer.parseInt(split[0]), split[1]);
            }

            br.close();
        } catch(Exception e) {
            if (testing) e.printStackTrace();
        }
    }

    /**
     * Create the map of movie IDs to movie names
     */
    private void setMovieIDs() {

        movieIDs = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/moviesTest.txt" : "inputs/movies.txt"));

            // For each line in the file, create the map entry after doing the necessary split.

            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                movieIDs.put(Integer.parseInt(split[0]), split[1]);
            }

            br.close();
        } catch(Exception e) {
            if (testing) e.printStackTrace();
        }
    }


    /**
     * Create the map of movies to actors
     */
    private void setMoviesToActors() {

        moviesToActors = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/movie-actorsTest.txt": "inputs/movie-actors.txt"));

            // For each line in the file, create the map entry after doing the necessary splitting
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                String movie = movieIDs.get(Integer.parseInt(split[0]));
                String actor = actorIDs.get(Integer.parseInt(split[1]));

                // Either add to or create the set that is set of actors present in each film
                if (moviesToActors.containsKey(movie)){
                    moviesToActors.get(movie).add(actor);
                } else {
                    Set<String> actors = new HashSet<>();
                    actors.add(actor);
                    moviesToActors.put(movie, actors);
                }

            }

            br.close();
        } catch(Exception e) {
            if (testing) e.printStackTrace();
        }
    }

    /**
     * Create the main graph based on the pre-processed maps.
     */
    private void createGraph(){
        graph = new AdjacencyMapGraph<>();

        //insert all actors as vertices
        for (String s : actorIDs.values()) {
            graph.insertVertex(s);
        }

        // insert all edges
        for (String movie : moviesToActors.keySet()) { // for each movie
            ArrayList<String> actorsInMovies = new ArrayList<>(moviesToActors.get(movie));

            // Iterate over every costar combination for the movie
            for(int i = 0; i < actorsInMovies.size() - 1; i++) {
                for(int j = i+1; j < actorsInMovies.size(); j++) {

                    String actor1 = actorsInMovies.get(i), actor2 = actorsInMovies.get(j);

                    // Either add to or create the set that is the edge label
                    if (graph.hasEdge(actor1, actor2)) {
                        graph.getLabel(actor1, actor2).add(movie);
                    } else {
                        Set<String> newEdge = new HashSet<>();
                        newEdge.add(movie);
                        graph.insertUndirected(actor1, actor2, newEdge);
                    }
                }
            }

        }
    }

    /**
     * Test that the graph was made successfully
     * @param args
     */
    public static void main(String[] args) {
        BaconGraph baconGraph = new BaconGraph(true);
        System.out.println(baconGraph.graph);
    }

}
