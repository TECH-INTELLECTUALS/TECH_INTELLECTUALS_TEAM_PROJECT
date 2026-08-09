package datastructures;

/**
 * A simple integer-based priority queue that stores lower urgency values first.
 * Higher urgency numbers are treated as lower priority, so they are dequeued later.
 */
public class PriorityQueue {
    private final Heap<Integer> heap;

    public PriorityQueue() {
        this.heap = new Heap<>();
    }

    /**
     * Adds an item to the queue using the provided urgency value.
     * Lower urgency values have higher priority.
     */
    public void enqueue(int urgency) {
        heap.add(-urgency);
    }

    /**
     * Removes and returns the highest-priority item.
     */
    public int dequeue() {
        return -heap.remove();
    }

    /**
     * Returns the highest-priority item without removing it.
     */
    public int peek() {
        return -heap.peek();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }
}