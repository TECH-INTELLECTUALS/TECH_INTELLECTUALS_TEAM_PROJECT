package algorithms;

import interfaces.Algorithm;
import datastructures.Graph;
import datastructures.Heap;

/**
 * Dijkstra's algorithm for shortest-path planning.
 *
 * Works directly against Graph's array-based methods (getEdgesFrom,
 * indexOfVertex, get) — no java.util collections used anywhere.
 *
 * Uses Samuel's Heap as the priority queue. Heap is a max-heap (for
 * scheduling), so Entry.compareTo() is deliberately reversed to make
 * it behave like a min-heap here — the smallest distance must come
 * out first for Dijkstra to work correctly.
 */
public class DijkstraAlgorithm<T> implements Algorithm {

    private static final int NO_PATH = -1;

    private int[] distances;
    private int[] previous;
    private Graph<T> graph;

    private static class Entry implements Comparable<Entry> {
        int vertexIndex;
        int distance;

        Entry(int vertexIndex, int distance) {
            this.vertexIndex = vertexIndex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Entry other) {
            // Reversed on purpose: Heap is max-heap, but Dijkstra needs
            // the SMALLEST distance to come out first.
            return Integer.compare(other.distance, this.distance);
        }
    }

    /**
     * Computes the shortest distance from source to every other vertex.
     *
     * @param g      the graph to search
     * @param source the starting vertex
     * @return distances indexed by vertex index (Integer.MAX_VALUE = unreachable)
     */
    public int[] shortestPath(Graph<T> g, T source) {
        this.graph = g;
        int n = g.size();
        distances = new int[n];
        previous = new int[n];

        for (int i = 0; i < n; i++) {
            distances[i] = Integer.MAX_VALUE;
            previous[i] = NO_PATH;
        }

        int sourceIndex = g.indexOfVertex(source);
        if (sourceIndex == -1) {
            throw new IllegalArgumentException("Source vertex not found in graph");
        }
        distances[sourceIndex] = 0;

        Heap<Entry> queue = new Heap<>();
        queue.add(new Entry(sourceIndex, 0));

        while (!queue.isEmpty()) {
            Entry current = queue.remove();
            int currentIndex = current.vertexIndex;
            int currentDist = current.distance;

            if (currentDist > distances[currentIndex]) {
                continue; // stale entry, skip
            }

            int[][] edgesFromCurrent = g.getEdgesFrom(currentIndex);
            for (int[] edge : edgesFromCurrent) {
                int neighborIndex = edge[0];
                int weight = edge[1];
                int newDist = currentDist + weight;

                if (newDist < distances[neighborIndex]) {
                    distances[neighborIndex] = newDist;
                    previous[neighborIndex] = currentIndex;
                    queue.add(new Entry(neighborIndex, newDist));
                }
            }
        }

        return distances;
    }

    /**
     * Reconstructs the shortest path from source to dest as an array of
     * VERTEX INDICES (use graph.get(index) to convert back to T if needed).
     * Must be called after shortestPath(). Returns an empty array if no
     * path exists.
     */
    public int[] getPath(T dest) {
        if (previous == null || graph == null) {
            throw new IllegalStateException("shortestPath() must be called before getPath()");
        }

        int destIndex = graph.indexOfVertex(dest);
        if (destIndex == -1 || distances[destIndex] == Integer.MAX_VALUE) {
            return new int[0]; // no path exists
        }

        int length = 0;
        int step = destIndex;
        while (step != NO_PATH) {
            length++;
            step = previous[step];
        }

        int[] path = new int[length];
        step = destIndex;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = step;
            step = previous[step];
        }

        return path;
    }

    /** Returns the shortest distance to dest. Must be called after shortestPath(). */
    public int getDistance(T dest) {
        if (distances == null || graph == null) {
            throw new IllegalStateException("shortestPath() must be called before getDistance()");
        }
        int destIndex = graph.indexOfVertex(dest);
        return destIndex == -1 ? NO_PATH : distances[destIndex];
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in shortestPath() / getPath() / getDistance() above.
    }
}