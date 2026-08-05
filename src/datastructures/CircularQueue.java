package datastructures;

import interfaces.DataStructure;

/**
 * Custom circular queue implementation with fixed capacity semantics.
 * @param <T> element type
 */
public class CircularQueue<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Enqueue an item in circular queue storage
    }

    @Override
    public void remove(T item) {
        // TODO: Dequeue or remove an item in circular fashion
    }

    @Override
    public int size() {
        // TODO: Return number of stored elements
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Check if queue is empty
        return false;
    }
}
