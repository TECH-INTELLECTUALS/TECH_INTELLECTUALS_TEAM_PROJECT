package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Kruskal's algorithm for building a Minimum Spanning Tree (MST).
 *
 * Uses Daniel's DisjointSet<T>, which works on actual elements (not
 * raw indices): add(item), find(item), union(item1, item2), and
 * connected(item1, item2). We use Integer node IDs as the elements.
 */
public class KruskalAlgorithm implements Algorithm {

    /**
     * Builds a Minimum Spanning Tree using Kruskal's algorithm.
     *
     * @param g  the graph to build the MST from
     * @param ds a DisjointSet used to detect cycles (pass in a fresh,
     *           empty DisjointSet<Integer>)
     * @return list of edges in the MST, each as {from, to, weight}
     */
    public List<int[]> mst(Graph<Integer> g, DisjointSet<Integer> ds) {
        List<int[]> mstEdges = new ArrayList<>();
        List<List<int[]>> adjacencyList = g.getAdjacencyList();

        if (adjacencyList.isEmpty()) {
            return mstEdges;
        }

        // Collect all edges as {from, to, weight}
        List<int[]> allEdges = new ArrayList<>();
        for (int from = 0; from < adjacencyList.size(); from++) {
            for (int[] edge : adjacencyList.get(from)) {
                int to = edge[0];
                int weight = edge[1];
                allEdges.add(new int[] { from, to, weight });
            }
        }

        // Sort edges by weight, ascending
        allEdges.sort(Comparator.comparingInt(e -> e[2]));

        // Initialize each node as its own set
        for (int node = 0; node < adjacencyList.size(); node++) {
            ds.add(node); // autoboxed to Integer
        }

        for (int[] edge : allEdges) {
            int from = edge[0];
            int to = edge[1];

            if (!ds.connected(from, to)) {
                ds.union(from, to);
                mstEdges.add(edge);
            }
        }

        return mstEdges;
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in mst() above.
    }
}