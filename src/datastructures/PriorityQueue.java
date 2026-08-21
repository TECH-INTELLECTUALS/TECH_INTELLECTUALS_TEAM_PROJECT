package datastructures;

import interfaces.DataStructure;

/**
 * Custom priority queue implementation intended for scheduling and routing.
 * Uses a max-heap structure where higher priority values are dequeued first.
 * @param <T> element type (must implement Comparable or be used with a Comparator)
 */
public class PriorityQueue<T extends Comparable<T>> implements DataStructure<T> {

    /**
     * Static generic inner class representing a priority queue entry.
     * Made static + generic so we can create arrays of Entry safely with a single unchecked cast.
     */
    private static class Entry<E> implements Comparable<Entry<E>> {
        E element;
        int priority;

        Entry(E element, int priority) {
            this.element = element;
            this.priority = priority;
        }

        @Override
        public int compareTo(Entry<E> other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    private static final int DEFAULT_CAPACITY = 10;

    private Entry<T>[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    public PriorityQueue() {
        heap = (Entry<T>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<T>[] oldHeap = heap;
        heap = (Entry<T>[]) new Entry[oldHeap.length * 2];
        System.arraycopy(oldHeap, 0, heap, 0, oldHeap.length);
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void swap(int i, int j) {
        Entry<T> temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void heapifyUp(int index) {
        while (index > 0 && heap[index].compareTo(heap[parent(index)]) > 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    private void heapifyDown(int index) {
        while (leftChild(index) < size) {
            int largerChild = leftChild(index);
            if (rightChild(index) < size &&
                heap[rightChild(index)].compareTo(heap[largerChild]) > 0) {
                largerChild = rightChild(index);
            }
            if (heap[index].compareTo(heap[largerChild]) > 0) {
                break;
            }
            swap(index, largerChild);
            index = largerChild;
        }
    }

    @Override
    public void add(T item) {
        add(item, 0);
    }

    public void add(T item, int priority) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }

        if (size >= heap.length) {
            resize();
        }

        Entry<T> entry = new Entry<>(item, priority);
        heap[size] = entry;
        heapifyUp(size);
        size++;
    }

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

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0].element;
    }

    public int peekPriority() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap[0].priority;
    }

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
            if (index > 0 && heap[index].compareTo(heap[parent(index)]) > 0) {
                heapifyUp(index);
            }
        }
    }

    public void updatePriority(T item, int newPriority) {
        if (item == null) {
            return;
        }

        for (int i = 0; i < size; i++) {
            if (heap[i].element.equals(item)) {
                int oldPriority = heap[i].priority;
                heap[i].priority = newPriority;

                if (newPriority > oldPriority) {
                    heapifyUp(i);
                } else if (newPriority < oldPriority) {
                    heapifyDown(i);
                }
                return;
            }
        }
    }

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