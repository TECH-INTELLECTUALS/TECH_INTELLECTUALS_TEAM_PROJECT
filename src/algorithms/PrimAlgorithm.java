package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.Heap;

public class PrimAlgorithm<T> implements Algorithm {

    private static class Entry implements Comparable<Entry> {
        int fromIndex;
        int toIndex;
        int weight;

        Entry(int fromIndex, int toIndex, int weight) {
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.weight = weight;
        }

        @Override
        public int compareTo(Entry other) {
            return Integer.compare(other.weight, this.weight); // reversed: smallest weight first
        }
    }

    /**
     * Builds a Minimum Spanning Tree starting from vertex index 0.
     * @return array of edges in the MST, each as {from, to, weight}
     */
    public int[][] mst(Graph<T> g) {
        int n = g.size();
        if (n == 0) {
            return new int[0][3];
        }

        boolean[] visited = new boolean[n];
        int[][] mstEdges = new int[n - 1][3];
        int edgeCount = 0;

        Heap<Entry> queue = new Heap<>();

        int startIndex = 0;
        visited[startIndex] = true;
        addEdgesToQueue(startIndex, g, queue);

        while (!queue.isEmpty() && edgeCount < n - 1) {
            Entry current = queue.remove();
            int to = current.toIndex;

            if (visited[to]) {
                continue;
            }

            visited[to] = true;
            mstEdges[edgeCount][0] = current.fromIndex;
            mstEdges[edgeCount][1] = current.toIndex;
            mstEdges[edgeCount][2] = current.weight;
            edgeCount++;

            addEdgesToQueue(to, g, queue);
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

    private void addEdgesToQueue(int fromIndex, Graph<T> g, Heap<Entry> queue) {
        int[][] edges = g.getEdgesFrom(fromIndex);
        for (int[] edge : edges) {
            int to = edge[0];
            int weight = edge[1];
            queue.add(new Entry(fromIndex, to, weight));
        }
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in mst() above.
    }
}