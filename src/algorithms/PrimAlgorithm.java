package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;

/**
 * Prim's algorithm for building a Minimum Spanning Tree (MST).
 *
 * NOTE: Same dependency situation as DijkstraAlgorithm — this relies on
 * Graph.getAdjacencyList() returning a List<List<int[]>> where each
 * int[] is {toNode, weight}. Using java.util.PriorityQueue as a
 * temporary stand-in for Samuel's Heap until it supports extract-min.
 */
public class PrimAlgorithm implements Algorithm {

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
        // TODO: replace with Samuel's Heap once it supports extract-min
        // Each entry: {from, to, weight}
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[2] - b[2]);

        int startNode = 0;
        visited.add(startNode);
        addEdgesToQueue(startNode, adjacencyList, queue);

        while (!queue.isEmpty() && visited.size() < adjacencyList.size()) {
            int[] edge = queue.poll();
            int to = edge[1];

            if (visited.contains(to)) {
                continue; // both endpoints already in MST, skip
            }

            visited.add(to);
            mstEdges.add(edge);
            addEdgesToQueue(to, adjacencyList, queue);
        }

        return mstEdges;
    }

    private void addEdgesToQueue(int node, List<List<int[]>> adjacencyList, PriorityQueue<int[]> queue) {
        for (int[] edge : adjacencyList.get(node)) {
            int to = edge[0];
            int weight = edge[1];
            queue.add(new int[] { node, to, weight });
        }
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in mst() above.
    }
}