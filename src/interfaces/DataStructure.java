package interfaces;

/**
 * Base interface for custom core data structures.
 * @param <T> the element type
 */
public interface DataStructure<T> {

    /**
     * Adds an element to the data structure.
     * @param item element to add
     */
    void add(T item);

    /**
     * Removes an element from the data structure.
     * @param item element to remove
     */
    void remove(T item);

    /**
     * Returns the number of stored elements.
     * @return size of the structure
     */
    int size();

    /**
     * Indicates whether the structure is empty.
     * @return true if empty, false otherwise
     */
    boolean isEmpty();
}
