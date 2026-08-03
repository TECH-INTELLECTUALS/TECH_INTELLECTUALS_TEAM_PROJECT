package datastructures;

import interfaces.DataStructure;

/**
 * Custom red-black tree placeholder for balanced ordered storage.
 * @param <T> element type
 */
public class RedBlackTree<T> implements DataStructure<T> {

    @Override
    public void add(T item) {
        // TODO: Insert item and rebalance the red-black tree
    }

    @Override
    public void remove(T item) {
        // TODO: Remove item and restore red-black invariants
    }

    @Override
    public int size() {
        // TODO: Return number of nodes
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Check if tree has any nodes
        return false;
    }
}
