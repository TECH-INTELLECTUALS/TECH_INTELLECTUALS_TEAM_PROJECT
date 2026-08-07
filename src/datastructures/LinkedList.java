package datastructures;

import interfaces.DataStructure;

/**
 * Custom singly linked list implementation.
 * @param <T> element type
 */
public class LinkedList<T> implements DataStructure<T> {
    
    /**
     * Inner class representing a node in the linked list
     */
    private class Node {
        T data;
        Node next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node head;
    private int size;
    
    /**
     * Constructor initializes an empty linked list
     */
    public LinkedList() {
        head = null;
        size = 0;
    }
    
    /**
     * Appends an item to the end of the linked list
     */
    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        
        Node newNode = new Node(item);
        
        // If list is empty, set as head
        if (head == null) {
            head = newNode;
            size++;
            return;
        }
        
        // Traverse to the end of the list
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        
        // Append the new node
        current.next = newNode;
        size++;
    }
    
    /**
     * Inserts an item at the beginning of the linked list
     */
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        
        Node newNode = new Node(item);
        newNode.next = head;
        head = newNode;
        size++;
    }
    
    /**
     * Inserts an item at a specific index
     */
    public void add(int index, T item) {
        if (item == null) {
            throw new IllegalArgumentException("Null elements not allowed");
        }
        
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        if (index == 0) {
            addFirst(item);
            return;
        }
        
        Node newNode = new Node(item);
        Node current = head;
        
        // Traverse to the node before the insertion point
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }
    
    /**
     * Removes the first occurrence of the specified item
     */
    @Override
    public void remove(T item) {
        if (item == null || head == null) {
            return;
        }
        
        // If the item is at the head
        if (head.data.equals(item)) {
            head = head.next;
            size--;
            return;
        }
        
        // Traverse the list to find and remove the item
        Node current = head;
        while (current.next != null) {
            if (current.next.data.equals(item)) {
                current.next = current.next.next;
                size--;
                return;
            }
            current = current.next;
        }
    }
    
    /**
     * Removes the node at a specific index
     */
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        T removedData;
        
        // If removing the head
        if (index == 0) {
            removedData = head.data;
            head = head.next;
            size--;
            return removedData;
        }
        
        // Traverse to the node before the removal point
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        
        removedData = current.next.data;
        current.next = current.next.next;
        size--;
        
        return removedData;
    }
    
    /**
     * Retrieves the element at a specific index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        
        return current.data;
    }
    
    /**
     * Retrieves the first element without removing it
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }
    
    /**
     * Retrieves the last element without removing it
     */
    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        
        return current.data;
    }
    
    /**
     * Checks if the list contains a specific item
     */
    public boolean contains(T item) {
        if (item == null) {
            return false;
        }
        
        Node current = head;
        while (current != null) {
            if (current.data.equals(item)) {
                return true;
            }
            current = current.next;
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
     * Returns a string representation of the linked list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        
        sb.append("]");
        return sb.toString();
    }
}