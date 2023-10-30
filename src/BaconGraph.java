import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class BaconGraph {

    //
    private Graph<String, Set<String>> graph;
    private Map<Integer, String> actorIDs;
    private Map<Integer, String> movieIDs;
    private Map<String, Set<String>> moviesToActors;

    private final boolean testing;

    public BaconGraph(boolean testing) {
        this.testing = testing;

        setActorIDs();
        setMovieIDs();
        setMoviesToActors();
        createGraph();
    }

    public Graph<String, Set<String>> getGraph() { return graph;}


    private void setActorIDs(){
        actorIDs = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/actorsTest.txt" : "inputs/actors.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                actorIDs.put(Integer.parseInt(split[0]), split[1]);
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setMovieIDs() {
        movieIDs = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/moviesTest.txt" : "inputs/movies.txt"));
            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\\|");
                movieIDs.put(Integer.parseInt(split[0]), split[1]);
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private void setMoviesToActors() {
        moviesToActors = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(testing ? "inputs/movie-actorsTest.txt": "inputs/movie-actors.txt"));
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
            e.printStackTrace();
        }
    }

    private void createGraph(){
        graph = new AdjacencyMapGraph<>();

        //insert all actors as vertices
        for (String s : actorIDs.values()) {
            graph.insertVertex(s);
        }
        //insert all edges
        for (String movie : moviesToActors.keySet()) { // for each movie
            ArrayList<String> actorsInMovies = new ArrayList<>(moviesToActors.get(movie));

            for(int i = 0; i < actorsInMovies.size() - 1; i++) {
                for(int j = i+1; j < actorsInMovies.size(); j++) {
                    String actor1 = actorsInMovies.get(i), actor2 = actorsInMovies.get(j);
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

    public static void main(String[] args) {
        BaconGraph baconGraph = new BaconGraph(true);
        System.out.println(baconGraph.graph);
    }

}
