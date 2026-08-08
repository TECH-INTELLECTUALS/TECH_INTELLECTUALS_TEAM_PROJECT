package datastructures;

import interfaces.DataStructure;

/**
 * Custom queue implementation for FIFO operations.
 * @param <T> element type
 */
public class Queue<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Enqueue an item at the rear of the queue
    }

    @Override
    public void remove(T item) {
        // TODO: Dequeue or remove an item from the queue
    }

    @Override
    public int size() {
        // TODO: Return current queue size
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Return true when the queue is empty
        return false;
    }
}
