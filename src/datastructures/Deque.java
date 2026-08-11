package datastructures;

import interfaces.DataStructure;

/**
 * Custom double-ended queue implementation.
 * 
 * @param <T> element type
 */
public class Deque<T> implements DataStructure<T> {

    private static final int DEFAULT_CAPACITY = 16;

    private T[] buffer;
    private int head;
    private int tail;
    private int size;

    @SuppressWarnings("unchecked")
    public Deque() {
        this.buffer = (T[]) new Object[DEFAULT_CAPACITY];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        addLast(item);
    }

    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        if (size == buffer.length) {
            throw new IllegalStateException("Deque is full");
        }
        head = (head - 1 + buffer.length) % buffer.length;
        buffer[head] = item;
        size++;
    }

    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        if (size == buffer.length) {
            throw new IllegalStateException("Deque is full");
        }
        buffer[tail] = item;
        tail = (tail + 1) % buffer.length;
        size++;
    }

    @Override
    public void remove(T item) {
        if (item == null || isEmpty()) {
            return;
        }

        int current = head;
        for (int i = 0; i < size; i++) {
            if ((buffer[current] == null && item == null)
                    || (buffer[current] != null && buffer[current].equals(item))) {
                removeAt(i);
                return;
            }
            current = (current + 1) % buffer.length;
        }
    }

    private void removeAt(int index) {
        int current = (head + index) % buffer.length;
        for (int i = 0; i < size - index - 1; i++) {
            int next = (current + 1) % buffer.length;
            buffer[current] = buffer[next];
            current = next;
        }
        tail = (tail - 1 + buffer.length) % buffer.length;
        buffer[tail] = null;
        size--;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }
        T value = buffer[head];
        buffer[head] = null;
        head = (head + 1) % buffer.length;
        size--;
        return value;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty");
        }
        tail = (tail - 1 + buffer.length) % buffer.length;
        T value = buffer[tail];
        buffer[tail] = null;
        size--;
        return value;
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
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return buffer[(head + index) % buffer.length];
    }

    @Override
    public void set(int index, T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        buffer[(head + index) % buffer.length] = item;
    }
}
