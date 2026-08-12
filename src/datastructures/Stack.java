package datastructures;

import interfaces.DataStructure;

/**
 * Custom stack implementation, built on top of our own LinkedList.
 * LIFO: last item added is the first one removed.
 */
public class Stack<T> implements DataStructure<T> {

    private LinkedList<T> items;

    public Stack() {
        items = new LinkedList<>();
    }

    /** Pushes an item onto the top of the stack. */
    public void push(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        items.addFirst(item);
    }

    /** Removes and returns the item on top of the stack. */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return items.removeAt(0);
    }

    /** Returns the item on top of the stack without removing it. */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return items.getFirst();
    }

    @Override
    public void add(T item) {
        push(item);
    }

    @Override
    public void remove(T item) {
        items.remove(item);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }
}