package datastructures;

import interfaces.DataStructure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic Disjoint Set / Union-Find data structure.
 *
 * Supports:
 * - Path Compression
 * - Union by Rank
 *
 * Provides near O(1) amortized time operations.
 *
 * @param <T> type of elements stored
 */
public class DisjointSet<T> implements DataStructure<T> {

    private int[] parent;
    private int[] rank;

    // Maps actual elements to internal integer indices
    private Map<T, Integer> indexMap;

    private int size;


    /**
     * Creates an empty Disjoint Set with default capacity.
     */
    public DisjointSet() {
        parent = new int[16];
        rank = new int[16];
        indexMap = new HashMap<>();
        size = 0;
    }


    /**
     * Creates a Disjoint Set with n empty indexed elements.
     *
     * Mostly useful for graph algorithms where vertices are numbered.
     *
     * @param n number of elements
     */
    public DisjointSet(int n) {

        if (n < 0) {
            throw new IllegalArgumentException(
                    "Size cannot be negative"
            );
        }

        parent = new int[Math.max(n, 1)];
        rank = new int[Math.max(n, 1)];

        indexMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        size = n;
    }


    /**
     * Finds the root index of an element.
     *
     * Uses path compression.
     */
    private int findIndex(int index) {

        if (parent[index] != index) {
            parent[index] = findIndex(parent[index]);
        }

        return parent[index];
    }


    /**
     * Finds the representative element of a set.
     */
    public T find(T item) {

        checkExists(item);

        int index = indexMap.get(item);

        int root = findIndex(index);

        // Find the element that owns this root
        for (Map.Entry<T, Integer> entry : indexMap.entrySet()) {

            if (entry.getValue() == root) {
                return entry.getKey();
            }
        }

        return null;
    }


    /**
     * Combines two sets.
     *
     * Uses union by rank.
     */
    public void union(T item1, T item2) {

        checkExists(item1);
        checkExists(item2);


        int root1 = findIndex(indexMap.get(item1));
        int root2 = findIndex(indexMap.get(item2));


        if (root1 == root2) {
            return;
        }


        // Union by rank
        if (rank[root1] < rank[root2]) {

            parent[root1] = root2;

        } else if (rank[root1] > rank[root2]) {

            parent[root2] = root1;

        } else {

            parent[root2] = root1;
            rank[root1]++;
        }
    }


    /**
     * Checks if two elements belong to the same set.
     */
    public boolean connected(T item1, T item2) {

        checkExists(item1);
        checkExists(item2);

        return findIndex(indexMap.get(item1))
                ==
                findIndex(indexMap.get(item2));
    }



    /**
     * Adds a new element.
     *
     * New elements start as their own set.
     */
    @Override
    public void add(T item) {

        if (indexMap.containsKey(item)) {
            return;
        }


        if (size == parent.length) {
            resize();
        }


        parent[size] = size;
        rank[size] = 0;


        indexMap.put(item, size);

        size++;
    }



    /**
     * Removing elements is not supported because
     * indices define the internal tree structure.
     */
    @Override
    public void remove(T item) {

        throw new UnsupportedOperationException(
                "DisjointSet does not support removal"
        );
    }



    /**
     * Returns number of elements.
     */
    @Override
    public int size() {
        return size;
    }



    /**
     * Checks whether structure is empty.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }



    /**
     * Removes all elements.
     */
    public void clear() {

        parent = new int[16];
        rank = new int[16];

        indexMap.clear();

        size = 0;
    }



    /**
     * Doubles internal storage.
     */
    private void resize() {

        int newCapacity =
                parent.length == 0
                        ? 1
                        : parent.length * 2;


        parent =
                Arrays.copyOf(parent, newCapacity);

        rank =
                Arrays.copyOf(rank, newCapacity);
    }



    /**
     * Checks if an element exists.
     */
    private void checkExists(T item) {

        if (!indexMap.containsKey(item)) {

            throw new IllegalArgumentException(
                    "Element does not exist: " + item
            );
        }
    }


    /**
     * Gets element at index.
     */
    @Override
    public T get(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds: " + index
            );
        }

        for (Map.Entry<T, Integer> entry : indexMap.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }

        return null;
    }


    /**
     * Sets element at index.
     */
    @Override
    public void set(int index, T item) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index out of bounds: " + index
            );
        }

        throw new UnsupportedOperationException(
                "DisjointSet does not support set operation"
        );
    }
}