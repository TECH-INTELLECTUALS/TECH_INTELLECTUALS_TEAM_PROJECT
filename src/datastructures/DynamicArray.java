package datastructures;

import interfaces.DataStructure;

/**
 * Custom dynamic array implementation for the campus service hub.
 * @param <T> element type
 */
public class DynamicArray<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Add element to dynamic array and resize storage as needed
    }

    @Override
    public void remove(T item) {
        // TODO: Remove element and manage shifting elements
    }

    @Override
    public int size() {
        // TODO: Return current element count
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Return true when no elements are stored
        return false;
    }
}
