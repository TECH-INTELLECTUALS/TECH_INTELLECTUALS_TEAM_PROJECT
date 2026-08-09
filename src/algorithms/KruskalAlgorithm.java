package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Kruskal's algorithm for building a Minimum Spanning Tree (MST).
 *
 * NOTE: This depends on:
 *  - Graph.getAdjacencyList() returning List<List<int[]>>, each int[]
 *    being {toNode, weight}, as agreed in the team spec.
 *  - Daniel's DisjointSet exposing find(int) and union(int, int) methods.
 *    DisjointSet currently only has the generic add/remove/size/isEmpty
 *    placeholder methods, so this class will not compile/run until
 *    find()/union() are added. The logic below is written against those
 *    expected method names, so it should work once they exist.
 */
public class KruskalAlgorithm implements Algorithm {

    /**
     * Builds a Minimum Spanning Tree using Kruskal's algorithm.
     *
     * @param g  the graph to build the MST from
     * @param ds a DisjointSet used to detect cycles
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
            ds.add(node); // NOTE: relies on DisjointSet.add() creating a new singleton set for this node
        }

        for (int[] edge : allEdges) {
            int from = edge[0];
            int to = edge[1];

            // TODO: replace with Daniel's find()/union() once implemented
            if (ds.find(from) != ds.find(to)) {
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