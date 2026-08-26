package utils;

import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;
import algorithms.DijkstraAlgorithm;
import algorithms.PrimAlgorithm;
import algorithms.KruskalAlgorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * M10 - Empirical Efficiency Lab (graph algorithms)
 * Times BFS, DFS, Dijkstra, Prim, and Kruskal across increasing graph sizes.
 *
 * NOTE: Graph uses an adjacency matrix (int[V][V]), so memory grows with V^2,
 * not linearly. Sizes here are deliberately much smaller than the sort/search
 * runner's - 5,000 vertices already means a ~100MB matrix per graph.
 *
 * All five algorithms start from vertex 0 (or Integer 0 for Dijkstra, which
 * takes the vertex itself rather than an index) - matches how the graph
 * generator always guarantees connectivity from that vertex.
 *
 * Output: results/graph_algorithm_runs.csv
 */
public class GraphPerformanceLabRunner {

    private static final int[] INPUT_SIZES = {50, 100, 500, 1000, 2000, 5000};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "results/graph_algorithm_runs.csv";

    public static void main(String[] args) throws IOException {
        new java.io.File("results").mkdirs();

        try (PrintWriter csv = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            csv.println("algorithm_name,input_size,trial,time_ms,memory_bytes,timestamp");
            PerformanceTimer timer = new PerformanceTimer();

            for (int size : INPUT_SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {

                    Graph<Integer> graph = generateConnectedGraph(size);

                    // ---- BFS ---- 
                    // Temporarily disabled because it has afixed capacity of 16 with no resize
                    //BreadthFirstSearch<Integer> bfs = new BreadthFirstSearch<>();
                    //timeGraphCall(csv, timer, "bfs", size, trial, () -> bfs.traverse(graph, 0));

                    // ---- DFS ----
                    DepthFirstSearch<Integer> dfs = new DepthFirstSearch<>();
                    timeGraphCall(csv, timer, "dfs", size, trial, () -> dfs.traverse(graph, 0));

                    // ---- Dijkstra (takes the vertex itself, not an index) ----
                    DijkstraAlgorithm<Integer> dijkstra = new DijkstraAlgorithm<>();
                    timeGraphCall(csv, timer, "dijkstra", size, trial, () -> dijkstra.shortestPath(graph, 0));

                    // ---- Prim (always starts internally from index 0) ----
                    PrimAlgorithm<Integer> prim = new PrimAlgorithm<>();
                    timeGraphCall(csv, timer, "prim", size, trial, () -> prim.mst(graph));

                    // ---- Kruskal (needs a fresh DisjointSet every run - it's stateful) ----
                    KruskalAlgorithm<Integer> kruskal = new KruskalAlgorithm<>();
                    DisjointSet<Integer> ds = new DisjointSet<>();
                    timeGraphCall(csv, timer, "kruskal", size, trial, () -> kruskal.mst(graph, ds));
                }
                System.out.println("Finished size " + size);
            }
        }
        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }

    /**
     * Generic timing wrapper for graph algorithms. Unlike the sort/search runner,
     * these classes' execute() is a no-op - the real work is in differently-named
     * methods (traverse(), mst(), shortestPath()) - so we time a Runnable wrapping
     * the real call instead of relying on the shared Algorithm interface.
     */
    private static void timeGraphCall(PrintWriter csv, PerformanceTimer timer,
                                       String label, int inputSize, int trial, Runnable task) {
        timer.start();
        task.run();
        timer.stop();

        csv.printf("%s,%d,%d,%.4f,%d,%d%n",
                label, inputSize, trial, timer.getElapsedMs(), timer.getMemoryUsedBytes(),
                System.currentTimeMillis());
    }

    /**
     * Builds a connected, undirected weighted graph with `size` vertices (0..size-1).
     * Guarantees connectivity via a random spanning-tree pass (every vertex reachable
     * from vertex 0, which all five algorithms rely on), then adds extra random edges
     * for density (~3 more per vertex on average).
     */
    private static Graph<Integer> generateConnectedGraph(int size) {
        Graph<Integer> g = new Graph<>();
        Random rand = new Random(42); // fixed seed - reproducible runs

        for (int i = 0; i < size; i++) {
            g.add(i);
        }

        // Spanning tree pass: connect each vertex to a random earlier vertex.
        for (int i = 1; i < size; i++) {
            int parent = rand.nextInt(i);
            int weight = 1 + rand.nextInt(20);
            g.addEdge(i, parent, weight);
        }

        // Extra random edges for density.
        int extraEdges = size * 3;
        for (int i = 0; i < extraEdges; i++) {
            int a = rand.nextInt(size);
            int b = rand.nextInt(size);
            if (a != b) {
                int weight = 1 + rand.nextInt(20);
                g.addEdge(a, b, weight);
            }
        }

        return g;
    }
}