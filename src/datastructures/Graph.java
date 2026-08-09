package datastructures;

import interfaces.DataStructure;

/**
 * A simple undirected graph using an adjacency matrix
 * (a grid of true/false values showing which vertices connect).
 *
 * This is easier to follow than an adjacency list for beginners,
 * though it uses more memory for graphs with few edges.
 */
public class Graph<T> implements DataStructure<T> {

    private Object[] vertices;      // the list of vertices (nodes)
    private boolean[][] edges;      // edges[i][j] = true means vertex i connects to vertex j
    private int count;              // how many vertices are currently stored

    public Graph() {
        vertices = new Object[10];
        edges = new boolean[10][10];
        count = 0;
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

        // Shift vertices after "index" one spot to the left
        for (int i = index; i < count - 1; i++) {
            vertices[i] = vertices[i + 1];
        }
        vertices[count - 1] = null;

        // Shift the edges grid to match (both rows and columns)
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
        // Clear this vertex's old connections since it's now a different item
        for (int i = 0; i < count; i++) {
            edges[index][i] = false;
            edges[i][index] = false;
        }
    }

    // --- Graph specific methods ---

    /** Adds an edge (connection) between a and b. Adds them as vertices first if missing. */
    public void addEdge(T a, T b) {
        add(a);
        add(b);
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        edges[indexA][indexB] = true;
        edges[indexB][indexA] = true; // undirected, so both directions
    }

    /** Removes the edge between a and b, if it exists. */
    public void removeEdge(T a, T b) {
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        if (indexA != -1 && indexB != -1) {
            edges[indexA][indexB] = false;
            edges[indexB][indexA] = false;
        }
    }

    /** Returns true if a and b are directly connected. */
    public boolean hasEdge(T a, T b) {
        int indexA = indexOf(a);
        int indexB = indexOf(b);
        if (indexA == -1 || indexB == -1) {
            return false;
        }
        return edges[indexA][indexB];
    }

    /** Prints all vertices and which ones each vertex connects to (for debugging/testing). */
    public void printGraph() {
        for (int i = 0; i < count; i++) {
            System.out.print(vertices[i] + " connects to: ");
            for (int j = 0; j < count; j++) {
                if (edges[i][j]) {
                    System.out.print(vertices[j] + " ");
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

        boolean[][] biggerEdges = new boolean[edges.length * 2][edges.length * 2];
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < edges.length; j++) {
                biggerEdges[i][j] = edges[i][j];
            }
        }

        vertices = biggerVertices;
        edges = biggerEdges;
    }
}