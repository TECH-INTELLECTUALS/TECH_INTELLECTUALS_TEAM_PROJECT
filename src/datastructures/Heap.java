package datastructures;

import interfaces.DataStructure;

/**
 * Custom heap implementation for efficient priority operations.
 * @param <T> element type
 */
public class Heap<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Insert item and maintain heap order
    }

    @Override
    public void remove(T item) {
        // TODO: Remove item and reheapify as needed
    }

    @Override
    public int size() {
        // TODO: Return heap size
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Check if heap contains no elements
        return false;
    }
}
