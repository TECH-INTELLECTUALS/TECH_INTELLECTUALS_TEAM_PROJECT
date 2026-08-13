package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.Heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Prim's algorithm for building a Minimum Spanning Tree (MST).
 *
 * Uses Samuel's Heap<T> (a real min-heap) as the priority queue.
 * Heap requires T to be Comparable, so we wrap each edge in a small
 * Edge class that compares by weight.
 */
public class PrimAlgorithm implements Algorithm {

    /**
     * Wraps an edge (from, to, weight) so it can be compared and
     * stored in the min-heap.
     */
    private static class Edge implements Comparable<Edge> {
        int from;
        int to;
        int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    /**
     * Builds a Minimum Spanning Tree starting from node 0.
     *
     * @param g the graph to build the MST from
     * @return list of edges in the MST, each as {from, to, weight}
     */
    public List<int[]> mst(Graph<Integer> g) {
        List<int[]> mstEdges = new ArrayList<>();
        List<List<int[]>> adjacencyList = g.getAdjacencyList();

        if (adjacencyList.isEmpty()) {
            return mstEdges;
        }

        Set<Integer> visited = new HashSet<>();
        Heap<Edge> queue = new Heap<>();

        int startNode = 0;
        visited.add(startNode);
        addEdgesToQueue(startNode, adjacencyList, queue);

        while (!queue.isEmpty() && visited.size() < adjacencyList.size()) {
            Edge edge = queue.remove(); // removes and returns the smallest-weight edge
            int to = edge.to;

            if (visited.contains(to)) {
                continue; // both endpoints already in MST, skip
            }

            visited.add(to);
            mstEdges.add(new int[] { edge.from, edge.to, edge.weight });
            addEdgesToQueue(to, adjacencyList, queue);
        }

        return mstEdges;
    }

    private void addEdgesToQueue(int node, List<List<int[]>> adjacencyList, Heap<Edge> queue) {
        for (int[] edge : adjacencyList.get(node)) {
            int to = edge[0];
            int weight = edge[1];
            queue.add(new Edge(node, to, weight));
        }
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in mst() above.
    }
}