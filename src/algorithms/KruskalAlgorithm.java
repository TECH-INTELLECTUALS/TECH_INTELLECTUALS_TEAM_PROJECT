/*package algorithms;

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
/* 
public class KruskalAlgorithm implements Algorithm {

    /**
     * Builds a Minimum Spanning Tree using Kruskal's algorithm.
     *
     * @param g  the graph to build the MST from
     * @param ds a DisjointSet used to detect cycles
     * @return list of edges in the MST, each as {from, to, weight}
     */
    /*public List<int[]> mst(Graph<Integer> g, DisjointSet<Integer> ds) {
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
}*/



package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

public class KruskalAlgorithm<T> implements Algorithm {

    /**
     * Builds a Minimum Spanning Tree using Kruskal's algorithm.
     * @return array of edges in the MST, each as {from, to, weight}
     */
    public int[][] mst(Graph<T> g, DisjointSet<Integer> ds) {
        int n = g.size();
        int[][] allEdges = g.getAllEdges(); // {from, to, weight}

        sortByWeight(allEdges);

        for (int i = 0; i < n; i++) {
            ds.add(i);
        }

        int[][] mstEdges = new int[Math.max(n - 1, 0)][3];
        int edgeCount = 0;

        for (int[] edge : allEdges) {
            if (edgeCount == mstEdges.length) {
                break;
            }
            int from = edge[0];
            int to = edge[1];

            if (!ds.connected(from, to)) {
                ds.union(from, to);
                mstEdges[edgeCount][0] = from;
                mstEdges[edgeCount][1] = to;
                mstEdges[edgeCount][2] = edge[2];
                edgeCount++;
            }
        }

        if (edgeCount < mstEdges.length) {
            int[][] trimmed = new int[edgeCount][3];
            for (int i = 0; i < edgeCount; i++) {
                trimmed[i] = mstEdges[i];
            }
            return trimmed;
        }

        return mstEdges;
    }

    /** Simple insertion sort by weight — avoids java.util.Collections/Comparator. */
    private void sortByWeight(int[][] edges) {
        for (int i = 1; i < edges.length; i++) {
            int[] key = edges[i];
            int j = i - 1;
            while (j >= 0 && edges[j][2] > key[2]) {
                edges[j + 1] = edges[j];
                j--;
            }
            edges[j + 1] = key;
        }
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in mst() above.
    }
}