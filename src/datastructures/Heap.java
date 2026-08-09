package datastructures;

import interfaces.DataStructure;

/**
 * Custom heap implementation for efficient priority operations.
 * Uses a min-heap structure with a dynamic array backing.
 * @param <T> element type (must implement Comparable)
 */
public class Heap<T extends Comparable<T>> implements DataStructure<T> {
    
    private static final int DEFAULT_CAPACITY = 10;
    
    private T[] heap;
    private int size;
    
    @SuppressWarnings("unchecked")
    public Heap() {
        heap = (T[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }
    
    /**
     * Returns the parent index of a given index
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }
    
    /**
     * Returns the left child index of a given index
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }
    
    /**
     * Returns the right child index of a given index
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    
    /**
     * Swaps two elements in the heap
     */
    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    /**
     * Resizes the heap array when capacity is exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        T[] oldHeap = heap;
        heap = (T[]) new Comparable[oldHeap.length * 2];
        System.arraycopy(oldHeap, 0, heap, 0, oldHeap.length);
    }
    
    /**
     * Moves an element up the heap to maintain min-heap property
     * Called after inserting a new element
     */
    private void heapifyUp(int index) {
        while (index > 0 && heap[index].compareTo(heap[parent(index)]) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }
    
    /**
     * Moves an element down the heap to maintain min-heap property
     * Called after removing the root
     */
    private void heapifyDown(int index) {
        while (leftChild(index) < size) {
            int smallerChild = leftChild(index);
            
            // Find the smaller of the two children
            if (rightChild(index) < size && 
                heap[rightChild(index)].compareTo(heap[smallerChild]) < 0) {
                smallerChild = rightChild(index);
            }
            
            // If current element is smaller than smallest child, heap property is satisfied
            if (heap[index].compareTo(heap[smallerChild]) < 0) {
                break;
            }
            
            swap(index, smallerChild);
            index = smallerChild;
        }
    }
    
    /**
     * Inserts an item into the heap and maintains min-heap property
     */
    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        
        // Resize if necessary
        if (size >= heap.length) {
            resize();
        }
        
        // Insert at the end
        heap[size] = item;
        
        // Move up to maintain heap property
        heapifyUp(size);
        size++;
    }
    
    /**
     * Removes the minimum element (root) from the heap
     */
    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        
        T root = heap[0];
        
        // Move last element to root
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        // Restore heap property if heap is not empty
        if (size > 0) {
            heapifyDown(0);
        }
        
        return root;
    }
    
    /**
     * Removes a specific item from the heap
     * Note: This requires searching the entire heap, O(n) operation
     */
    @Override
    public void remove(T item) {
        if (item == null) {
            return;
        }
        
        // Find the item in the heap
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(item)) {
                index = i;
                break;
            }
        }
        
        // Item not found
        if (index == -1) {
            return;
        }
        
        // Move last element to the position of removed item
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        // Restore heap property
        if (index < size) {
            // Try heapifying down first
            heapifyDown(index);
            
            // If element is still out of place, heapify up
            if (index > 0 && heap[index].compareTo(heap[parent(index)]) < 0) {
                heapifyUp(index);
            }
        }
    }
    
    /**
     * Returns the minimum element without removing it
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap[0];
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