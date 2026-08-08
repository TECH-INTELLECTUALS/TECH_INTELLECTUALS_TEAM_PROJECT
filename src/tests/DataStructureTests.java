package tests;

import datastructures.BinarySearchTree;
import datastructures.BTree;
import datastructures.CircularQueue;
import datastructures.Deque;

/**
 * Test suite for the first four custom data structures.
 */
public class DataStructureTests {

    public static void main(String[] args) {
        new DataStructureTests().runTests();
    }

    /**
     * Runs data structure tests.
     */
    public void runTests() {
        testBinarySearchTree();
        testBTree();
        testCircularQueue();
        testDeque();
        System.out.println("Data structure tests passed.");
    }

    private void testBinarySearchTree() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        assertTrue(tree.isEmpty(), "BST should start empty");
        tree.add(10);
        tree.add(5);
        tree.add(15);
        assertEquals(3, tree.size(), "BST size after inserts");
        assertEquals(5, tree.get(0), "BST inorder first element");
        assertEquals(10, tree.get(1), "BST inorder second element");
        assertEquals(15, tree.get(2), "BST inorder third element");
        tree.remove(10);
        assertEquals(2, tree.size(), "BST size after removal");
        assertEquals(5, tree.get(0), "BST value after removal at index 0");
        assertEquals(15, tree.get(1), "BST value after removal at index 1");
    }

    private void testBTree() {
        BTree<Integer> tree = new BTree<>();
        assertTrue(tree.isEmpty(), "BTree should start empty");
        tree.add(30);
        tree.add(10);
        tree.add(20);
        tree.add(40);
        assertEquals(4, tree.size(), "BTree size after inserts");
        assertEquals(10, tree.get(0), "BTree sorted value at index 0");
        assertEquals(20, tree.get(1), "BTree sorted value at index 1");
        assertEquals(30, tree.get(2), "BTree sorted value at index 2");
        assertEquals(40, tree.get(3), "BTree sorted value at index 3");
        tree.remove(20);
        assertEquals(3, tree.size(), "BTree size after removal");
    }

    private void testCircularQueue() {
        CircularQueue<String> queue = new CircularQueue<>();
        assertTrue(queue.isEmpty(), "CircularQueue should start empty");
        queue.add("a");
        queue.add("b");
        queue.add("c");
        assertEquals(3, queue.size(), "CircularQueue size after enqueues");
        assertEquals("a", queue.get(0), "CircularQueue first element");
        assertEquals("b", queue.get(1), "CircularQueue second element");
        queue.remove("b");
        assertEquals(2, queue.size(), "CircularQueue size after removal");
        assertEquals("c", queue.get(1), "CircularQueue second element after removal");
    }

    private void testDeque() {
        Deque<Integer> deque = new Deque<>();
        assertTrue(deque.isEmpty(), "Deque should start empty");
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        assertEquals(3, deque.size(), "Deque size after adds");
        assertEquals(0, deque.get(0), "Deque first value");
        assertEquals(2, deque.get(2), "Deque last value");
        assertEquals(0, deque.removeFirst(), "Deque removeFirst returns first value");
        assertEquals(2, deque.removeLast(), "Deque removeLast returns last value");
        assertEquals(1, deque.size(), "Deque size after removals");
        deque.set(0, 5);
        assertEquals(5, deque.get(0), "Deque set updates value");
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    private void assertEquals(Object expected, Object actual, String message) {
        if (expected == null) {
            if (actual != null) {
                throw new AssertionError(message + ": expected null but got " + actual);
            }
        } else if (!expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + " but got " + actual);
        }
    }
}
