package datastructures;

import interfaces.DataStructure;

/**
 * Custom hash table implementation using separate chaining.
 * Keys are Strings, Values are generic type T.
 * 
 * @param <T> value type
 */
public class HashTable<T> implements DataStructure<T> {
    
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    
    private Entry<T>[] table;
    private int size;
    
    /**
     * Inner class representing a node in the linked list chain.
     */
    private static class Entry<T> {
        String key;
        T value;
        Entry<T> next;
        
        Entry(String key, T value) {
            this.key = key;
            this.value = value;
        }
    }
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }
    
    /**
     * Computes hash code for a given key.
     * Uses bitwise AND to prevent Integer.MIN_VALUE bug.
     */
    private int hash(String key) {
        if (key == null) return 0;
        return (key.hashCode() & 0x7fffffff) % table.length;
    }
    
    /**
     * Resizes the table when load factor is exceeded.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<T>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0; // Reset size as put() will increment it
        
        for (Entry<T> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }
    
    /**
     * REQUIRED METHOD 1: Adds or updates a key-value pair.
     */
    public void put(String key, T value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        Entry<T> curr = table[index];
        
        // 1. Check if key already exists -> update value
        while (curr != null) {
            if ((curr.key == null && key == null) || (curr.key != null && curr.key.equals(key))) {
                curr.value = value;
                return;
            }
            curr = curr.next;
        }
        
        // 2. Key not found -> prepend new entry (O(1) insertion)
        Entry<T> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }
    
    /**
     * REQUIRED METHOD 2: Retrieves a value by key.
     */
    public T get(String key) {
        int index = hash(key);
        Entry<T> curr = table[index];
        
        while (curr != null) {
            if ((curr.key == null && key == null) || (curr.key != null && curr.key.equals(key))) {
                return curr.value;
            }
            curr = curr.next;
        }
        
        return null;
    }

    /**
     * REQUIRED METHOD 3: Removes an item by KEY (Fast O(1) average).
     * This is the method you should show in your demo!
     */
    public void remove(String key) {
        int index = hash(key);
        Entry<T> curr = table[index];
        Entry<T> prev = null;
        
        while (curr != null) {
            if ((curr.key == null && key == null) || (curr.key != null && curr.key.equals(key))) {
                // Unlink the node
                if (prev == null) {
                    table[index] = curr.next; // Removing the head of the list
                } else {
                    prev.next = curr.next;    // Removing from middle or end
                }
                size--;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    // --- Interface Methods (DataStructure<T>) ---
    // These are required to compile against the interface, but are secondary to the 3 methods above.

    @Override
    public void add(T item) {
        // A hash table requires a key to add an item.
        throw new UnsupportedOperationException("Use put(key, value) instead.");
    }

    @Override
    public void remove(T item) {
        // Note: This removes by VALUE, which is slow O(N).
        // It is kept here only to satisfy the DataStructure interface contract.
        for (int i = 0; i < table.length; i++) {
            Entry<T> curr = table[i];
            Entry<T> prev = null;
            
            while (curr != null) {
                if ((curr.value == null && item == null) || (curr.value != null && curr.value.equals(item))) {
                    if (prev == null) table[i] = curr.next;
                    else prev.next = curr.next;
                    size--;
                    return;
                }
                prev = curr;
                curr = curr.next;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException("Use get(key) instead for hash table access.");
    }

    @Override
    public void set(int index, T value) {
        throw new UnsupportedOperationException("Use put(key, value) instead for hash table access.");
    }
}