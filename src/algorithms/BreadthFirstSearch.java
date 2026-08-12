package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.Queue;

/**
 * Breadth-First Search — explores a graph level by level from a
 * starting vertex. Uses the team's own Queue for the frontier and a
 * plain boolean[] for visited tracking — no java.util collections.
 */
public class BreadthFirstSearch<T> implements Algorithm {

    /**
     * Returns the order in which vertices are visited from startIndex,
     * as an array of vertex indices.
     */
    public int[] traverse(Graph<T> g, int startIndex) {
        int n = g.size();
        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderCount = 0;

        Queue<Integer> frontier = new Queue<>();
        frontier.add(startIndex);
        visited[startIndex] = true;

        while (!frontier.isEmpty()) {
            int current = frontier.dequeue();
            order[orderCount] = current;
            orderCount++;

            int[][] edges = g.getEdgesFrom(current);
            for (int[] edge : edges) {
                int neighbor = edge[0];
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    frontier.add(neighbor);
                }
            }
        }

        if (orderCount < order.length) {
            int[] trimmed = new int[orderCount];
            for (int i = 0; i < orderCount; i++) {
                trimmed[i] = order[i];
            }
            return trimmed;
        }
        return order;
    }

    /** Returns true if dest is reachable from source. */
    public boolean isReachable(Graph<T> g, int sourceIndex, int destIndex) {
        int[] visitOrder = traverse(g, sourceIndex);
        for (int index : visitOrder) {
            if (index == destIndex) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in traverse() / isReachable() above.
    }
}