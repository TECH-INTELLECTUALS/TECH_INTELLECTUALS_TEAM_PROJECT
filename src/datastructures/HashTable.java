package datastructures;

import interfaces.DataStructure;

/**
 * Custom hash table placeholder for fast lookup operations.
 * @param <K> key type
 * @param <V> value type
 */
public class HashTable<K, V> implements DataStructure<V> {
    
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    
    private Entry<K, V>[] table;
    private int size;
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
    }
    
    /**
     * Inner class representing a key-value pair in the hash table
     */
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    /**
     * Computes hash code for a given key
     */
    private int hash(K key) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % table.length;
    }
    
    /**
     * Resizes the table when load factor is exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
        size = 0;
        
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }
    
    /**
     * Adds a key-value pair to the hash table
     */
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        Entry<K, V> entry = table[index];
        
        // Check if key already exists
        while (entry != null) {
            if ((entry.key == null && key == null) || 
                (entry.key != null && entry.key.equals(key))) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }
        
        // Add new entry at the beginning
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }
    
    /**
     * Retrieves a value by key
     */
    public V get(K key) {
        int index = hash(key);
        Entry<K, V> entry = table[index];
        
        while (entry != null) {
            if ((entry.key == null && key == null) || 
                (entry.key != null && entry.key.equals(key))) {
                return entry.value;
            }
            entry = entry.next;
        }
        
        return null;
    }

    @Override
    public void add(V item) {
        // For this implementation, you would need a key
        // If implementing with just values, consider using a HashSet instead
        throw new UnsupportedOperationException("Use put(K key, V value) instead");
    }

    @Override
    public void remove(V item) {
        // TODO: Remove an item from the hash table
        // Note: Removing by value requires iterating through all entries
        for (int i = 0; i < table.length; i++) {
            Entry<K, V> entry = table[i];
            Entry<K, V> prev = null;
            
            while (entry != null) {
                if ((entry.value == null && item == null) || 
                    (entry.value != null && entry.value.equals(item))) {
                    
                    if (prev == null) {
                        table[i] = entry.next;
                    } else {
                        prev.next = entry.next;
                    }
                    size--;
                    return;
                }
                
                prev = entry;
                entry = entry.next;
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
}