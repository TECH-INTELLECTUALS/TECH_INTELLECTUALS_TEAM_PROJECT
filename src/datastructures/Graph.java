package datastructures;

import interfaces.DataStructure;

/**
 * A simple undirected, WEIGHTED graph using an adjacency matrix.
 * edges[i][j] holds the weight of the connection between vertex i and
 * vertex j, or -1 if no edge exists between them.
 */
public class Graph<T> implements DataStructure<T> {

    private static final int NO_EDGE = -1;

    private Object[] vertices;      // the list of vertices (nodes)
    private int[][] edges;          // edges[i][j] = weight, or NO_EDGE if not connected
    private int count;              // how many vertices are currently stored

    public Graph() {
        vertices = new Object[10];
        edges = new int[10][10];
        fillNoEdge(edges);
        count = 0;
    }

    private void fillNoEdge(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = NO_EDGE;
            }
        }
    }

    @Override
    public void add(T item) {
        if (item == null || indexOf(item) != -1) {
            return; // skip nulls and duplicate vertices
        }
        if (count == vertices.length) {
            growArrays();
        }
        vertices[count] = item;
        count = count + 1;
    }

    @Override
    public void remove(T item) {
        int index = indexOf(item);
        if (index == -1) {
            return;
        }

        for (int i = index; i < count - 1; i++) {
            vertices[i] = vertices[i + 1];
        }
        vertices[count - 1] = null;

        for (int row = index; row < count - 1; row++) {
            for (int col = 0; col < count; col++) {
                edges[row][col] = edges[row + 1][col];
            }
        }
        for (int col = index; col < count - 1; col++) {
            for (int row = 0; row < count; row++) {
                edges[row][col] = edges[row][col + 1];
            }
        }

        count = count - 1;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Bad index: " + index);
        }
        return (T) vertices[index];
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Bad index: " + index);
        }
        vertices[index] = item;
        for (int i = 0; i < count; i++) {
            edges[index][i] = NO_EDGE;
            edges[i][index] = NO_EDGE;
        }
    }

    // --- Graph specific methods ---

    /** Adds a weighted edge between a and b. Adds them as vertices first if missing. */
    public void addEdge(T a, T b, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive: " + weight);
        }
        add(a);
        add(b);
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        edges[indexA][indexB] = weight;
        edges[indexB][indexA] = weight; // undirected, so both directions
    }

    /** Removes the edge between a and b, if it exists. */
    public void removeEdge(T a, T b) {
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        if (indexA != -1 && indexB != -1) {
            edges[indexA][indexB] = NO_EDGE;
            edges[indexB][indexA] = NO_EDGE;
        }
    }

    /** Returns true if a and b are directly connected. */
    public boolean hasEdge(T a, T b) {
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        if (indexA == -1 || indexB == -1) {
            return false;
        }
        return edges[indexA][indexB] != NO_EDGE;
    }

    /** Returns the weight of the edge between a and b, or -1 if no edge exists. */
    public int getWeight(T a, T b) {
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        if (indexA == -1 || indexB == -1) {
            return NO_EDGE;
        }
        return edges[indexA][indexB];
    }

    /** Returns the vertex index for a given item, or -1 if not present. */
    public int indexOfVertex(T item) {
        return indexOf(item);
    }

    /**
     * Returns every edge leaving vertexIndex as pairs of {neighborIndex, weight}.
     * No java.util collections used — plain array, consistent with the rest
     * of this class. Algorithms (Dijkstra/Prim/Kruskal) should iterate this
     * with a plain for-loop.
     */
    public int[][] getEdgesFrom(int vertexIndex) {
        if (vertexIndex < 0 || vertexIndex >= count) {
            throw new IndexOutOfBoundsException("Bad index: " + vertexIndex);
        }

        int edgeCount = 0;
        for (int j = 0; j < count; j++) {
            if (edges[vertexIndex][j] != NO_EDGE) {
                edgeCount++;
            }
        }

        int[][] result = new int[edgeCount][2];
        int pos = 0;
        for (int j = 0; j < count; j++) {
            if (edges[vertexIndex][j] != NO_EDGE) {
                result[pos][0] = j;
                result[pos][1] = edges[vertexIndex][j];
                pos++;
            }
        }
        return result;
    }

    /** Returns every edge in the whole graph as {fromIndex, toIndex, weight} — useful for Kruskal. */
    public int[][] getAllEdges() {
        int edgeCount = 0;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (edges[i][j] != NO_EDGE) {
                    edgeCount++;
                }
            }
        }

        int[][] result = new int[edgeCount][3];
        int pos = 0;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (edges[i][j] != NO_EDGE) {
                    result[pos][0] = i;
                    result[pos][1] = j;
                    result[pos][2] = edges[i][j];
                    pos++;
                }
            }
        }
        return result;
    }

    /** Prints all vertices and which ones each vertex connects to, with weights (for debugging/testing). */
    public void printGraph() {
        for (int i = 0; i < count; i++) {
            System.out.print(vertices[i] + " connects to: ");
            for (int j = 0; j < count; j++) {
                if (edges[i][j] != NO_EDGE) {
                    System.out.print(vertices[j] + "(w=" + edges[i][j] + ") ");
                }
            }
            System.out.println();
        }
    }

    // --- Helper methods ---

    private int indexOf(T item) {
        for (int i = 0; i < count; i++) {
            if (vertices[i] != null && vertices[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    private void growArrays() {
        Object[] biggerVertices = new Object[vertices.length * 2];
        for (int i = 0; i < vertices.length; i++) {
            biggerVertices[i] = vertices[i];
        }

        int[][] biggerEdges = new int[edges.length * 2][edges.length * 2];
        fillNoEdge(biggerEdges);
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                biggerEdges[i][j] = edges[i][j];
            }
        }

        vertices = biggerVertices;
        edges = biggerEdges;
    }
}