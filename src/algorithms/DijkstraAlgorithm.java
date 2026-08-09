package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Dijkstra's algorithm for shortest-path planning.
 *
 * NOTE: This depends on Graph.getAdjacencyList() returning a
 * List<List<int[]>> where each int[] is {toNode, weight}, as agreed
 * in the team spec (Elton's Graph.java). Until Graph is fully built,
 * this class will not compile/run — but the logic below is written
 * against the agreed method signatures, so it should work as-is once
 * Graph is implemented.
 *
 * Also using java.util.PriorityQueue as a temporary stand-in for
 * Samuel's Heap.java, since Heap doesn't yet expose a poll()/extractMin()
 * method. Swap the PriorityQueue below for Heap once that's ready —
 * the rest of the algorithm won't need to change.
 */
public class DijkstraAlgorithm implements Algorithm {

    private Map<Integer, Integer> distances;
    private Map<Integer, Integer> previous;

    /**
     * Computes the shortest distance from source to every other node.
     *
     * @param g      the graph to search
     * @param source the starting node
     * @return a map of node -> shortest distance from source
     */
    public Map<Integer, Integer> shortestPath(Graph<Integer> g, int source) {
        distances = new HashMap<>();
        previous = new HashMap<>();

        List<List<int[]>> adjacencyList = g.getAdjacencyList();

        // Initialize all distances as "infinity"
        for (int node = 0; node < adjacencyList.size(); node++) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(source, 0);

        // TODO: replace with Samuel's Heap once it supports extract-min
        PriorityQueue<int[]> queue = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        queue.add(new int[] { source, 0 }); // {node, distance}

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentNode = current[0];
            int currentDist = current[1];

            if (currentDist > distances.get(currentNode)) {
                continue; // stale entry, skip
            }

            for (int[] edge : adjacencyList.get(currentNode)) {
                int neighbor = edge[0];
                int weight = edge[1];
                int newDist = currentDist + weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, currentNode);
                    queue.add(new int[] { neighbor, newDist });
                }
            }
        }

        return distances;
    }

    /**
     * Reconstructs the shortest path between two nodes.
     * Must be called after shortestPath() has been run.
     *
     * @param source the starting node
     * @param dest   the destination node
     * @return the list of nodes from source to dest, in order
     */
    public List<Integer> getPath(int source, int dest) {
        if (previous == null) {
            throw new IllegalStateException("shortestPath() must be called before getPath()");
        }

        java.util.LinkedList<Integer> path = new java.util.LinkedList<>();
        Integer step = dest;

        if (!distances.containsKey(dest) || distances.get(dest) == Integer.MAX_VALUE) {
            return path; // no path exists
        }

        while (step != null) {
            path.addFirst(step);
            step = previous.get(step);
        }

        return path;
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in shortestPath() / getPath() above.
    }
}