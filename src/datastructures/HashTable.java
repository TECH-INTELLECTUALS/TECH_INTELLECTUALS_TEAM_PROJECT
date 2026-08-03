package datastructures;

import interfaces.DataStructure;

/**
 * Custom hash table placeholder for fast lookup operations.
 * @param <K> key type
 * @param <V> value type
 */
public class HashTable<K, V> implements DataStructure<V> {

    @Override
    public void add(V item) {
        // TODO: Insert an item into the hash table using a key/value mapping
    }

    @Override
    public void remove(V item) {
        // TODO: Remove an item from the hash table
    }

    @Override
    public int size() {
        // TODO: Return number of stored entries
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO: Return true if no entries exist
        return false;
    }
}
