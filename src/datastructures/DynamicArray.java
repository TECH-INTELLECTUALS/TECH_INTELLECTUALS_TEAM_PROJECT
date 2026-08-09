package datastructures;

import interfaces.DataStructure;

/**
 * A simple dynamic array (like a beginner's version of ArrayList).
 * It starts with a small array and grows it when it gets full.
 */
public class DynamicArray<T> implements DataStructure<T> {

    private Object[] data;   // the actual storage
    private int count;       // how many elements are currently used

    public DynamicArray() {
        data = new Object[10]; // start with room for 10 items
        count = 0;
    }

    @Override
    public void add(T item) {
        // If the array is full, make a bigger one
        if (count == data.length) {
            growArray();
        }
        data[count] = item;
        count = count + 1;
    }

    @Override
    public void remove(T item) {
        // First, find where the item is
        int indexToRemove = -1;
        for (int i = 0; i < count; i++) {
            if (data[i].equals(item)) {
                indexToRemove = i;
                break;
            }
        }

        // If we didn't find it, do nothing
        if (indexToRemove == -1) {
            return;
        }

        // Shift every element after it one spot to the left
        for (int i = indexToRemove; i < count - 1; i++) {
            data[i] = data[i + 1];
        }

        data[count - 1] = null; // clear the last, now-unused spot
        count = count - 1;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Bad index: " + index);
        }
        // We stored a T, so this cast is safe
        return (T) data[index];
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Bad index: " + index);
        }
        data[index] = item;
    }

    // Makes a new, bigger array and copies everything over
    private void growArray() {
        Object[] biggerArray = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            biggerArray[i] = data[i];
        }
        data = biggerArray;
    }
}