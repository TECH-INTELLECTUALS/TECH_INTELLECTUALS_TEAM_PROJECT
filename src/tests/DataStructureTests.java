package tests;

import datastructures.BTree;
import datastructures.BinarySearchTree;
import datastructures.CircularQueue;
import datastructures.Deque;
import datastructures.DisjointSet;
import datastructures.DynamicArray;
import datastructures.Graph;
import datastructures.HashTable;
import datastructures.RedBlackTree;

public class DataStructureTests {

   public static void main(String[] args) {
      new DataStructureTests().runTests();
   }

   public void runTests() {
      this.testBinarySearchTree();
      this.testBTree();
      this.testCircularQueue();
      this.testDeque();
                this.testHashTableNormal();
                this.testHashTableBoundary();
                this.testHashTableCollisionAndResize();
      this.testDynamicArray();
      this.testDisjointSet();
      this.testGraph();
      System.out.println("Data structure tests passed.");
   }
private void testBinarySearchTree() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<>();

    this.assertTrue(tree.isEmpty(),
            "BinarySearchTree should start empty");

    tree.add(50);
    tree.add(30);
    tree.add(70);
    tree.add(20);
    tree.add(40);

    this.assertEquals(5, tree.size(),
            "BinarySearchTree size after inserts");

    this.assertEquals(20, tree.get(0),
            "BST smallest value");

    this.assertEquals(40, tree.get(2),
            "BST middle value");

    this.assertEquals(70, tree.get(4),
            "BST largest value");
}

private void testBinarySearchTreeRemoval() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<>();

    tree.add(50);
    tree.add(30);
    tree.add(70);
    tree.add(20);
    tree.add(40);
    tree.add(60);
    tree.add(80);

    tree.remove(30);

    this.assertEquals(6, tree.size(),
            "BST size after removing a value");

    this.assertEquals(20, tree.get(0),
            "BST value after removal");

    this.assertEquals(40, tree.get(1),
            "BST should preserve sorted order after removal");

    tree.remove(999);

    this.assertEquals(6, tree.size(),
            "BST size should not change when removing missing value");
}

private void testBinarySearchTreeSet() {
    BinarySearchTree<Integer> tree = new BinarySearchTree<>();

    tree.add(10);
    tree.add(20);
    tree.add(30);

    tree.set(1, 25);

    this.assertEquals(3, tree.size(),
            "BST size should remain unchanged after set");

    this.assertEquals(10, tree.get(0),
            "BST first value after set");

    this.assertEquals(25, tree.get(1),
            "BST set should update value");

    this.assertEquals(30, tree.get(2),
            "BST last value after set");
}

private void testBTree() {
    BTree<Integer> tree = new BTree<>();

    this.assertTrue(tree.isEmpty(),
            "BTree should start empty");

    tree.add(10);
    tree.add(20);
    tree.add(30);
    tree.add(40);
    tree.add(50);
    tree.add(60);
    tree.add(70);

    this.assertEquals(7, tree.size(),
            "BTree size after inserts");

    this.assertEquals(10, tree.get(0),
            "BTree smallest value");

    this.assertEquals(40, tree.get(3),
            "BTree middle value");

    this.assertEquals(70, tree.get(6),
            "BTree largest value");
}

private void testBTreeRemoval() {
    BTree<Integer> tree = new BTree<>();

    tree.add(10);
    tree.add(20);
    tree.add(30);
    tree.add(40);
    tree.add(50);
    tree.add(60);
    tree.add(70);

    tree.remove(40);

    this.assertEquals(6, tree.size(),
            "BTree size after removal");

    this.assertEquals(30, tree.get(2),
            "BTree value before removed item");

    this.assertEquals(50, tree.get(3),
            "BTree value after removed item");

    tree.remove(999);

    this.assertEquals(6, tree.size(),
            "BTree size should not change when removing missing value");
}

private void testBTreeSet() {
    BTree<Integer> tree = new BTree<>();

    tree.add(10);
    tree.add(20);
    tree.add(30);
    tree.add(40);

    tree.set(1, 25);

    this.assertEquals(4, tree.size(),
            "BTree size should remain unchanged after set");

    this.assertEquals(10, tree.get(0),
            "BTree first value after set");

    this.assertEquals(25, tree.get(1),
            "BTree set should update value");

    this.assertEquals(40, tree.get(3),
            "BTree last value after set");
}

private void testRedBlackTree() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    this.assertTrue(tree.isEmpty(),
            "RedBlackTree should start empty");

    tree.add(50);
    tree.add(30);
    tree.add(70);
    tree.add(20);
    tree.add(40);

    this.assertEquals(5, tree.size(),
            "RedBlackTree size after inserts");

    this.assertEquals(20, tree.get(0),
            "RedBlackTree smallest value");

    this.assertEquals(40, tree.get(2),
            "RedBlackTree middle value");

    this.assertEquals(70, tree.get(4),
            "RedBlackTree largest value");
}

private void testRedBlackTreeRemoval() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    tree.add(50);
    tree.add(30);
    tree.add(70);
    tree.add(20);
    tree.add(40);
    tree.add(60);
    tree.add(80);

    tree.remove(30);

    this.assertEquals(6, tree.size(),
            "RedBlackTree size after removal");

    this.assertEquals(20, tree.get(0),
            "RedBlackTree value after removal");

    this.assertEquals(40, tree.get(1),
            "RedBlackTree should preserve sorted order");

    tree.remove(999);

    this.assertEquals(6, tree.size(),
            "RedBlackTree size should not change when removing missing value");
}

private void testRedBlackTreeBalance() {
    RedBlackTree<Integer> tree = new RedBlackTree<>();

    for (int i = 1; i <= 7; i++) {
        tree.add(i);
    }

    this.assertEquals(7, tree.size(),
            "RedBlackTree size after sorted inserts");

    this.assertEquals(3, tree.height(),
            "Balanced tree should have height 3 for values 1-7");

    this.assertEquals(1, tree.get(0),
            "RedBlackTree smallest value");

    this.assertEquals(7, tree.get(6),
            "RedBlackTree largest value");
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

   private void testHashTableNormal() {
    HashTable<Integer> table = new HashTable<>();
    table.put("alice", 90);
    table.put("bob", 85);
    table.put("carol", 92);

    assertTrue(table.get("alice") == 90, "get() should return the stored value");
    assertTrue(table.get("bob") == 85, "get() should return the stored value");
    assertTrue(table.size() == 3, "size() should count entries");

    table.put("alice", 95); // existing key -> update, not duplicate
    assertTrue(table.get("alice") == 95, "put() on existing key should update the value");
    assertTrue(table.size() == 3, "updating a key must not change size()");
}

private void testHashTableBoundary() {
    HashTable<Integer> table = new HashTable<>();
    assertTrue(table.isEmpty(), "new HashTable should be empty");
    assertTrue(table.get("missing") == null, "get() on absent key should return null");

    table.put("solo", 1);
    assertTrue(!table.isEmpty(), "table with one entry is not empty");
    assertTrue(table.size() == 1, "size() should be 1 after one put");

    table.remove("solo");
    assertTrue(table.size() == 0, "remove() should shrink size");
    assertTrue(table.get("solo") == null, "removed key should be gone");
    table.remove("never-there"); // must not throw
    assertTrue(table.size() == 0, "remove() on absent key should be a no-op");
}

private void testHashTableCollisionAndResize() {
    HashTable<Integer> table = new HashTable<>();
    // "Aa" and "BB" have IDENTICAL hashCode (2112) -> guaranteed same bucket
    table.put("Aa", 1);
    table.put("BB", 2);
    assertTrue(table.get("Aa") == 1 && table.get("BB") == 2,
            "colliding keys must both stay retrievable (separate chaining)");

    // Force two resizes (16 -> 32 -> 64) and verify nothing is lost
    for (int i = 0; i < 26; i++) {
        table.put("key" + i, i);
    }
    assertTrue(table.size() == 28, "size() should count all 28 entries");

    boolean allPresent = table.get("Aa") == 1 && table.get("BB") == 2;
    for (int i = 0; i < 26; i++) {
        allPresent = allPresent && table.get("key" + i) == i;
    }
    assertTrue(allPresent, "every key must survive both resizes");
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

    graph.addEdge("A", "B", 5);
    this.assertTrue(graph.hasEdge("A", "B"), "A and B should be connected after addEdge");
    this.assertTrue(graph.hasEdge("B", "A"), "Graph should be undirected: B to A also connected");
    this.assertEquals(5, graph.getWeight("A", "B"), "Edge weight should be stored correctly");
    this.assertTrue(!graph.hasEdge("A", "C"), "A and C should not be connected yet");

    graph.addEdge("C", "D", 8);
    this.assertEquals(4, graph.size(), "Graph size after addEdge auto-adds a new vertex");
    this.assertTrue(graph.hasEdge("C", "D"), "C and D should be connected after addEdge");
    this.assertEquals(8, graph.getWeight("C", "D"), "Edge weight for C-D should be correct");

    boolean threwOnBadWeight = false;
    try {
        graph.addEdge("A", "C", 0);
    } catch (IllegalArgumentException e) {
        threwOnBadWeight = true;
    }
    this.assertTrue(threwOnBadWeight, "addEdge should reject a weight of 0 or less");

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