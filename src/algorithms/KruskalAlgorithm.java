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