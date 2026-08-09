package tests;

import datastructures.BTree;
import datastructures.BinarySearchTree;
import datastructures.CircularQueue;
import datastructures.Deque;
import datastructures.DynamicArray;
import datastructures.DisjointSet;
import datastructures.Graph;

public class DataStructureTests {

   public static void main(String[] args) {
      new DataStructureTests().runTests();
   }

   public void runTests() {
      this.testBinarySearchTree();
      this.testBTree();
      this.testCircularQueue();
      this.testDeque();
      this.testDynamicArray();
      this.testDisjointSet();
      this.testGraph();
      System.out.println("Data structure tests passed.");
   }

   private void testBinarySearchTree() {
      BinarySearchTree tree = new BinarySearchTree();
      this.assertTrue(tree.isEmpty(), "BST should start empty");
      tree.add(10);
      tree.add(5);
      tree.add(15);
      this.assertEquals(3, tree.size(), "BST size after inserts");
      this.assertEquals(5, tree.get(0), "BST inorder first element");
      this.assertEquals(10, tree.get(1), "BST inorder second element");
      this.assertEquals(15, tree.get(2), "BST inorder third element");
      tree.remove(10);
      this.assertEquals(2, tree.size(), "BST size after removal");
      this.assertEquals(5, tree.get(0), "BST value after removal at index 0");
      this.assertEquals(15, tree.get(1), "BST value after removal at index 1");
   }

   private void testBTree() {
      BTree tree = new BTree();
      this.assertTrue(tree.isEmpty(), "BTree should start empty");
      tree.add(30);
      tree.add(10);
      tree.add(20);
      tree.add(40);
      this.assertEquals(4, tree.size(), "BTree size after inserts");
      this.assertEquals(10, tree.get(0), "BTree sorted value at index 0");
      this.assertEquals(20, tree.get(1), "BTree sorted value at index 1");
      this.assertEquals(30, tree.get(2), "BTree sorted value at index 2");
      this.assertEquals(40, tree.get(3), "BTree sorted value at index 3");
      tree.remove(20);
      this.assertEquals(3, tree.size(), "BTree size after removal");
   }

   private void testCircularQueue() {
      CircularQueue queue = new CircularQueue();
      this.assertTrue(queue.isEmpty(), "CircularQueue should start empty");
      queue.add("a");
      queue.add("b");
      queue.add("c");
      this.assertEquals(3, queue.size(), "CircularQueue size after enqueues");
      this.assertEquals("a", queue.get(0), "CircularQueue first element");
      this.assertEquals("b", queue.get(1), "CircularQueue second element");
      queue.remove("b");
      this.assertEquals(2, queue.size(), "CircularQueue size after removal");
      this.assertEquals("c", queue.get(1), "CircularQueue second element after removal");
   }

   private void testDeque() {
      Deque deque = new Deque();
      this.assertTrue(deque.isEmpty(), "Deque should start empty");
      deque.addFirst(1);
      deque.addLast(2);
      deque.addFirst(0);
      this.assertEquals(3, deque.size(), "Deque size after adds");
      this.assertEquals(0, deque.get(0), "Deque first value");
      this.assertEquals(2, deque.get(2), "Deque last value");
      this.assertEquals(0, deque.removeFirst(), "Deque removeFirst returns first value");
      this.assertEquals(2, deque.removeLast(), "Deque removeLast returns last value");
      this.assertEquals(1, deque.size(), "Deque size after removals");
      deque.set(0, 5);
      this.assertEquals(5, deque.get(0), "Deque set updates value");
   }

   private void testDynamicArray() {
      DynamicArray<Integer> array = new DynamicArray<>();
      this.assertTrue(array.isEmpty(), "DynamicArray should start empty");

      array.add(10);
      array.add(20);
      array.add(30);
      this.assertEquals(3, array.size(), "DynamicArray size after adds");
      this.assertTrue(!array.isEmpty(), "DynamicArray should not be empty after adds");
      this.assertEquals(10, array.get(0), "DynamicArray value at index 0");
      this.assertEquals(20, array.get(1), "DynamicArray value at index 1");
      this.assertEquals(30, array.get(2), "DynamicArray value at index 2");

      array.set(1, 99);
      this.assertEquals(99, array.get(1), "DynamicArray value after set");

      array.remove(99);
      this.assertEquals(2, array.size(), "DynamicArray size after removal");
      this.assertEquals(10, array.get(0), "DynamicArray value at index 0 after removal");
      this.assertEquals(30, array.get(1), "DynamicArray value at index 1 after removal, shifted left");

      array.remove(12345);
      this.assertEquals(2, array.size(), "DynamicArray size unchanged after removing missing item");

      DynamicArray<Integer> growArray = new DynamicArray<>();
      for (int i = 0; i < 25; i++) {
         growArray.add(i);
      }
      this.assertEquals(25, growArray.size(), "DynamicArray size after growing past initial capacity");
      this.assertEquals(0, growArray.get(0), "DynamicArray first value survives resize");
      this.assertEquals(24, growArray.get(24), "DynamicArray last value survives resize");
   }

   private void testDisjointSet() {
      DisjointSet<Integer> set = new DisjointSet<>();
      this.assertTrue(set.isEmpty(), "DisjointSet should start empty");

      set.add(1);
      set.add(2);
      set.add(3);
      set.add(4);
      this.assertEquals(4, set.size(), "DisjointSet size after adds");
      this.assertTrue(!set.connected(1, 2), "1 and 2 should not be connected before union");

      set.union(1, 2);
      this.assertTrue(set.connected(1, 2), "1 and 2 should be connected after union");
      this.assertTrue(!set.connected(1, 3), "1 and 3 should not be connected yet");

      set.union(3, 4);
      set.union(2, 3);
      this.assertTrue(set.connected(1, 4), "1 and 4 should be connected transitively through 2 and 3");

      Integer root1 = set.find(1);
      Integer root4 = set.find(4);
      this.assertEquals(root1, root4, "find() should return the same root for connected elements");

      set.add(1);
      this.assertEquals(4, set.size(), "DisjointSet size unchanged after re-adding existing element");

      boolean threwOnRemove = false;
      try {
         set.remove(1);
      } catch (UnsupportedOperationException e) {
         threwOnRemove = true;
      }
      this.assertTrue(threwOnRemove, "DisjointSet.remove should throw UnsupportedOperationException");

      boolean threwOnMissing = false;
      try {
         set.union(1, 999);
      } catch (IllegalArgumentException e) {
         threwOnMissing = true;
      }
      this.assertTrue(threwOnMissing, "DisjointSet.union should throw for an element that was never added");
   }

   private void testGraph() {
      Graph<String> graph = new Graph<>();
      this.assertTrue(graph.isEmpty(), "Graph should start empty");

      graph.add("A");
      graph.add("B");
      graph.add("C");
      this.assertEquals(3, graph.size(), "Graph size after adding vertices");
      this.assertTrue(!graph.hasEdge("A", "B"), "A and B should not be connected before addEdge");

      graph.addEdge("A", "B");
      this.assertTrue(graph.hasEdge("A", "B"), "A and B should be connected after addEdge");
      this.assertTrue(graph.hasEdge("B", "A"), "Graph should be undirected: B to A also connected");
      this.assertTrue(!graph.hasEdge("A", "C"), "A and C should not be connected yet");

      graph.addEdge("C", "D");
      this.assertEquals(4, graph.size(), "Graph size after addEdge auto-adds a new vertex");
      this.assertTrue(graph.hasEdge("C", "D"), "C and D should be connected after addEdge");

      graph.removeEdge("A", "B");
      this.assertTrue(!graph.hasEdge("A", "B"), "A and B should not be connected after removeEdge");
      this.assertEquals(4, graph.size(), "Graph size unchanged after removing an edge, not a vertex");

      graph.remove("D");
      this.assertEquals(3, graph.size(), "Graph size after removing a vertex");
      this.assertTrue(!graph.hasEdge("C", "D"), "Edges to a removed vertex should be gone too");
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