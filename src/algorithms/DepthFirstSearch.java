package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;

/**
 * Depth-First Search — explores as far as possible along each branch
 * before backtracking. Implemented recursively, so no extra structure
 * is needed for the call stack (the JVM's own call stack handles it) —
 * still zero java.util collections used.
 */
public class DepthFirstSearch<T> implements Algorithm {

    /**
     * Returns the order in which vertices are visited from startIndex,
     * as an array of vertex indices.
     */
    public int[] traverse(Graph<T> g, int startIndex) {
        int n = g.size();
        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int[] orderCount = new int[] { 0 }; // boxed in a 1-element array so the recursive helper can update it

        visitRecursively(g, startIndex, visited, order, orderCount);

        if (orderCount[0] < order.length) {
            int[] trimmed = new int[orderCount[0]];
            for (int i = 0; i < orderCount[0]; i++) {
                trimmed[i] = order[i];
            }
            return trimmed;
        }
        return order;
    }

    private void visitRecursively(Graph<T> g, int current, boolean[] visited, int[] order, int[] orderCount) {
        visited[current] = true;
        order[orderCount[0]] = current;
        orderCount[0]++;

        int[][] edges = g.getEdgesFrom(current);
        for (int[] edge : edges) {
            int neighbor = edge[0];
            if (!visited[neighbor]) {
                visitRecursively(g, neighbor, visited, order, orderCount);
            }
        }
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