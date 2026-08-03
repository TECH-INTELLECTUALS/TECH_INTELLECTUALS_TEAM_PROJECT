package datastructures;

import interfaces.DataStructure;

/**
 * Custom priority queue implementation intended for scheduling and routing.
 * @param <T> element type
 */
public class PriorityQueue<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Insert item according to priority order
    }

    @Override
    public void remove(T item) {
        // TODO: Remove the specified item from the priority queue
    }

    @Override
    public int size() {
        // TODO: Return number of elements
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Determine if queue has no elements
        return false;
    }
}
