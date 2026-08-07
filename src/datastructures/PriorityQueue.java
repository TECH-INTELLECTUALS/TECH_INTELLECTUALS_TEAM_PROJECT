package datastructures;

import interfaces.DataStructure;

/**
 * Custom priority queue implementation intended for scheduling and routing.
 * Uses a min-heap structure where lower priority values have higher priority.
 * @param <T> element type (must implement Comparable or be used with a Comparator)
 */
public class PriorityQueue<T extends Comparable<T>> implements DataStructure<T> {
    
    /**
     * Inner class representing a priority queue entry
     */
    private class Entry implements Comparable<Entry> {
        T element;
        int priority;
        
        Entry(T element, int priority) {
            this.element = element;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(Entry other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
    
    private static final int DEFAULT_CAPACITY = 10;
    
    private Entry[] heap;
    private int size;
    
    @SuppressWarnings("unchecked")
    public PriorityQueue() {
        heap = new Entry[DEFAULT_CAPACITY];
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
     * Swaps two entries in the heap
     */
    private void swap(int i, int j) {
        Entry temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
    
    /**
     * Resizes the heap array when capacity is exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Entry[] oldHeap = heap;
        heap = new Entry[oldHeap.length * 2];
        System.arraycopy(oldHeap, 0, heap, 0, oldHeap.length);
    }
    
    /**
     * Moves an entry up the heap to maintain min-heap property
     */
    private void heapifyUp(int index) {
        while (index > 0 && heap[index].compareTo(heap[parent(index)]) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }
    
    /**
     * Moves an entry down the heap to maintain min-heap property
     */
    private void heapifyDown(int index) {
        while (leftChild(index) < size) {
            int smallerChild = leftChild(index);
            
            if (rightChild(index) < size && 
                heap[rightChild(index)].compareTo(heap[smallerChild]) < 0) {
                smallerChild = rightChild(index);
            }
            
            if (heap[index].compareTo(heap[smallerChild]) < 0) {
                break;
            }
            
            swap(index, smallerChild);
            index = smallerChild;
        }
    }
    
    /**
     * Inserts an item with default priority (0)
     */
    @Override
    public void add(T item) {
        add(item, 0);
    }
    
    /**
     * Inserts an item with a specified priority
     * Lower priority values are dequeued first
     */
    public void add(T item, int priority) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        
        if (size >= heap.length) {
            resize();
        }
        
        Entry entry = new Entry(item, priority);
        heap[size] = entry;
        heapifyUp(size);
        size++;
    }
    
    /**
     * Removes and returns the highest priority element (lowest priority value)
     */
    public T poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        
        T element = heap[0].element;
        
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        if (size > 0) {
            heapifyDown(0);
        }
        
        return element;
    }
    
    /**
     * Returns the highest priority element without removing it
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0].element;
    }
    
    /**
     * Returns the priority of the highest priority element
     */
    public int peekPriority() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0].priority;
    }
    
    /**
     * Removes a specific item from the priority queue
     * O(n) operation as it requires searching
     */
    @Override
    public void remove(T item) {
        if (item == null) {
            return;
        }
        
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].element.equals(item)) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            return;
        }
        
        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        
        if (index < size) {
            heapifyDown(index);
            
            if (index > 0 && heap[index].compareTo(heap[parent(index)]) < 0) {
                heapifyUp(index);
            }
        }
    }
    
    /**
     * Updates the priority of an existing element
     */
    public void updatePriority(T item, int newPriority) {
        if (item == null) {
            return;
        }
        
        for (int i = 0; i < size; i++) {
            if (heap[i].element.equals(item)) {
                int oldPriority = heap[i].priority;
                heap[i].priority = newPriority;
                
                if (newPriority < oldPriority) {
                    heapifyUp(i);
                } else if (newPriority > oldPriority) {
                    heapifyDown(i);
                }
                return;
            }
        }
    }
    
    /**
     * Checks if the priority queue contains a specific item
     */
    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        
        for (int i = 0; i < size; i++) {
            if (heap[i].element.equals(item)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns a string representation of the priority queue
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append("(").append(heap[i].element).append(":").append(heap[i].priority).append(")");
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}