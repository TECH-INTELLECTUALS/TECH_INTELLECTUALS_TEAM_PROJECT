package tests;

import datastructures.BTree;
import datastructures.BinarySearchTree;
import datastructures.CircularQueue;
import datastructures.Deque;
import datastructures.DisjointSet;
import datastructures.DynamicArray;
import datastructures.Graph;
import datastructures.HashTable;
import datastructures.Heap;
import datastructures.PriorityQueue;
import datastructures.Queue;
import datastructures.RedBlackTree;
import datastructures.Stack;

public class DataStructureTests {

    public static void main(String[] args) {
        new DataStructureTests().runTests();
    }

    public void runTests() {
        // --- Trees ---
        this.testBinarySearchTree();
        this.testBinarySearchTreeRemoval();
        this.testBinarySearchTreeSet();
        this.testBTree();
        this.testBTreeRemoval();
        this.testBTreeSet();
        this.testRedBlackTree();
        this.testRedBlackTreeRemoval();
        this.testRedBlackTreeBalance();

        // --- HashTable ---
        this.testHashTableNormal();
        this.testHashTableBoundary();
        this.testHashTableCollision();
        this.testHashTableUpdate();
        this.testHashTableRemoveByKey();
        this.testHashTableRemoveByValue();
        this.testHashTableNullKey();
        this.testHashTableResize();
        this.testHashTableMissingKey();
        this.testHashTableUnsupportedOperations();

        // --- Other sequence structures ---
        this.testCircularQueue();
        this.testDeque();
        this.testDynamicArray();
        this.testGraph();
         // --- Queue / Heap / PriorityQueue tests ---
        this.testQueueNormal();
        this.testQueueBoundary();
        this.testQueueInvalid();

        this.testHeapNormal();
        this.testHeapBoundary();
        this.testHeapInvalid();

        this.testPriorityQueueNormal();
        this.testPriorityQueueBoundary();
        this.testPriorityQueueInvalid();
        
        // --- DisjointSet ---
        this.testDisjointSetEmpty();
        this.testDisjointSetConstructor();
        this.testDisjointSetAdd();
        this.testDisjointSetDuplicateAdd();
        this.testDisjointSetUnion();
        this.testDisjointSetFind();
        this.testDisjointSetPathCompression();
        this.testDisjointSetGrow();
        this.testDisjointSetClear();
        this.testDisjointSetGet();
        this.testDisjointSetExceptions();
        this.testDisjointSetUnsupportedOperations();

        // --- Stack tests ---
         testStackNormal();
         testStackBoundary();
         testStackInvalid();

        System.out.println("Data structure tests passed.");
    }

    // ==================== BinarySearchTree Tests ====================

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

    // ==================== BTree Tests ====================

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

    // ==================== RedBlackTree Tests ====================

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

    // ==================== HashTable Tests ====================

    private void testHashTableNormal() {
        HashTable<Integer> table = new HashTable<>();

        this.assertTrue(table.isEmpty(),
                "New HashTable should be empty");

        this.assertEquals(0, table.size(),
                "New HashTable size should be 0");

        table.put("alice", 90);
        table.put("bob", 85);
        table.put("carol", 92);

        this.assertEquals(3, table.size(),
                "HashTable size after 3 puts");

        this.assertTrue(!table.isEmpty(),
                "HashTable should not be empty after puts");

        this.assertEquals(90, table.get("alice"),
                "HashTable get() should return the stored value for alice");

        this.assertEquals(85, table.get("bob"),
                "HashTable get() should return the stored value for bob");

        this.assertEquals(92, table.get("carol"),
                "HashTable get() should return the stored value for carol");
    }

    private void testHashTableBoundary() {
        HashTable<Object> table = new HashTable<>();

        this.assertTrue(table.isEmpty(),
                "New HashTable should be empty");

        this.assertEquals(null, table.get("missing"),
                "get() on a missing key should return null");

        table.put("solo", "only");

        this.assertTrue(!table.isEmpty(),
                "HashTable with one item is not empty");

        this.assertEquals(1, table.size(),
                "HashTable size should be 1 after one put");

        table.remove("solo");

        this.assertEquals(0, table.size(),
                "remove() should shrink size to 0");

        this.assertTrue(table.isEmpty(),
                "HashTable should be empty after removing the only key");
    }

    private void testHashTableCollision() {
        HashTable<Integer> table = new HashTable<>();
        // "Aa" and "BB" have the exact same hash code in Java, so they MUST collide
        table.put("Aa", 1);
        table.put("BB", 2);

        this.assertEquals(2, table.size(),
                "Colliding keys should both be stored");

        this.assertEquals(1, table.get("Aa"),
                "Colliding key Aa lost");

        this.assertEquals(2, table.get("BB"),
                "Colliding key BB lost");
    }

    private void testHashTableUpdate() {
        HashTable<Integer> table = new HashTable<>();
        table.put("alice", 90);
        table.put("bob", 85);

        table.put("alice", 95); // Update existing key

        this.assertEquals(95, table.get("alice"),
                "put() on an existing key should update the value");

        this.assertEquals(2, table.size(),
                "size() should not increase when updating an existing key");
    }

    private void testHashTableRemoveByKey() {
        HashTable<Integer> table = new HashTable<>();
        table.put("alice", 90);
        table.put("bob", 85);
        table.put("carol", 92);

        table.remove("bob");

        this.assertEquals(2, table.size(),
                "size() should decrease after remove(key)");

        this.assertEquals(null, table.get("bob"),
                "get() on a removed key should return null");

        this.assertEquals(90, table.get("alice"),
                "Remaining keys should still be retrievable");

        this.assertEquals(92, table.get("carol"),
                "Remaining keys should still be retrievable");
    }

    private void testHashTableRemoveByValue() {
        HashTable<Integer> table = new HashTable<>();
        table.put("alice", 90);
        table.put("bob", 85);
        table.put("carol", 92);

        table.remove((Integer) 85); // remove by VALUE (interface method)

        this.assertEquals(2, table.size(),
                "size() after remove(value) should shrink by 1");

        this.assertEquals(null, table.get("bob"),
                "Entry whose value was removed should be gone");

        this.assertEquals(90, table.get("alice"),
                "Entries with different values should remain");

        this.assertEquals(92, table.get("carol"),
                "Entries with different values should remain");
    }

    private void testHashTableNullKey() {
        HashTable<Object> table = new HashTable<>();

        table.put(null, "nullValue");

        this.assertEquals(1, table.size(),
                "put() with a null key should store the entry");

        this.assertEquals("nullValue", table.get(null),
                "get() with a null key should return the stored value");

        this.assertEquals("nullValue", table.get((String) null),
                "Null lookup should work via explicit cast");

        table.put("real", "realValue");
        this.assertEquals(2, table.size(),
                "Null and non-null keys should coexist");

        table.remove((String) null); // remove by KEY (remove(String) overload)

        this.assertEquals(1, table.size(),
                "remove(null) should remove the null-key entry");

        this.assertEquals(null, table.get(null),
                "get(null) after removal should return null");

        this.assertEquals("realValue", table.get("real"),
                "Non-null entries should survive removing the null key");
    }

    private void testHashTableResize() {
        HashTable<Integer> table = new HashTable<>();
        // "Aa" and "BB" have IDENTICAL hashCode -> guaranteed same bucket
        table.put("Aa", 1);
        table.put("BB", 2);

        // Force two resizes (16 -> 32 -> 64) and verify nothing is lost
        for (int i = 0; i < 26; i++) {
            table.put("key" + i, i);
        }

        this.assertEquals(28, table.size(),
                "size() should count all 28 entries");

        this.assertEquals(1, table.get("Aa"),
                "Colliding key Aa must survive resizes");

        this.assertEquals(2, table.get("BB"),
                "Colliding key BB must survive resizes");

        for (int i = 0; i < 26; i++) {
            this.assertEquals(i, table.get("key" + i),
                    "key" + i + " must survive resizes");
        }

        // Update after resize must still work
        table.put("key0", 100);

        this.assertEquals(100, table.get("key0"),
                "put() should update an existing key after a resize");

        this.assertEquals(28, table.size(),
                "Updating after resize should not grow size");
    }

    private void testHashTableMissingKey() {
        HashTable<Integer> table = new HashTable<>();

        this.assertEquals(null, table.get("never-put"),
                "get() on an absent key should return null");

        table.remove("never-there");
        // must not throw

        this.assertEquals(0, table.size(),
                "remove() on an absent key should be a no-op");
    }

    private void testHashTableUnsupportedOperations() {
        HashTable<Integer> table = new HashTable<>();

        boolean addThrew = false;
        try {
            table.add(10);
        } catch (UnsupportedOperationException e) {
            addThrew = true;
        }
        this.assertTrue(addThrew,
                "add() should throw UnsupportedOperationException (use put instead)");

        boolean getIndexThrew = false;
        try {
            table.get(0);
        } catch (UnsupportedOperationException e) {
            getIndexThrew = true;
        }
        this.assertTrue(getIndexThrew,
                "get(index) should throw UnsupportedOperationException (use get(key) instead)");

        boolean setThrew = false;
        try {
            table.set(0, 5);
        } catch (UnsupportedOperationException e) {
            setThrew = true;
        }
        this.assertTrue(setThrew,
                "set() should throw UnsupportedOperationException (use put instead)");
    }

    // ==================== CircularQueue Tests ====================

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

    // ==================== Deque Tests ====================

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

    // ==================== DynamicArray Tests ====================

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

    // ==================== Graph Tests ====================

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

    // ==================== DisjointSet Tests ====================

    private void testDisjointSetEmpty() {
        DisjointSet<Integer> ds = new DisjointSet<>();

        this.assertTrue(ds.isEmpty(),
                "New DisjointSet should be empty");

        this.assertEquals(0, ds.size(),
                "New DisjointSet size should be 0");
    }

    private void testDisjointSetConstructor() {
        // Constructor with n pre-indexed elements
        DisjointSet<Integer> ds = new DisjointSet<>(6);

        this.assertEquals(6, ds.size(),
                "DisjointSet(n) should contain n elements");

        this.assertTrue(!ds.isEmpty(),
                "DisjointSet(n) should not be empty");

        // DisjointSet(0) should be empty
        DisjointSet<Integer> empty = new DisjointSet<>(0);
        this.assertTrue(empty.isEmpty(),
                "DisjointSet(0) should be empty");

        this.assertEquals(0, empty.size(),
                "DisjointSet(0) size should be 0");

        // Negative size must throw
        boolean negativeThrew = false;
        try {
            new DisjointSet<Integer>(-1);
        } catch (IllegalArgumentException e) {
            negativeThrew = true;
        }
        this.assertTrue(negativeThrew,
                "DisjointSet(-1) should throw IllegalArgumentException");
    }

    private void testDisjointSetAdd() {
        DisjointSet<String> ds = new DisjointSet<>();

        this.assertTrue(ds.isEmpty(),
                "New DisjointSet should be empty");

        ds.add("apple");
        ds.add("banana");
        ds.add("cherry");

        this.assertEquals(3, ds.size(),
                "size() should be 3 after three adds");

        this.assertTrue(!ds.isEmpty(),
                "DisjointSet should not be empty after adds");

        this.assertTrue(ds.connected("apple", "apple"),
                "An element is connected to itself");

        this.assertTrue(!ds.connected("apple", "banana"),
                "Different elements should not be connected before union");
    }

    private void testDisjointSetDuplicateAdd() {
        DisjointSet<Integer> ds = new DisjointSet<>();

        ds.add(42);
        ds.add(42); // duplicate add should be a no-op
        ds.add(43);

        this.assertEquals(2, ds.size(),
                "Adding a duplicate element should not increase size");
    }

    private void testDisjointSetUnion() {
        DisjointSet<Integer> ds = new DisjointSet<>();
        for (int i = 0; i < 6; i++) {
            ds.add(i);
        }

        ds.union(0, 1);
        this.assertTrue(ds.connected(0, 1),
                "union() should connect two elements");

        // Union is symmetric
        this.assertTrue(ds.connected(1, 0),
                "connected() should be symmetric");

        // Transitive connectivity
        ds.union(1, 2);
        this.assertTrue(ds.connected(0, 2),
                "union() should connect 0 and 2 transitively");

        this.assertTrue(ds.connected(1, 2),
                "Direct union should connect 1 and 2");

        // Union of already-connected elements is a no-op
        ds.union(0, 2);
        this.assertTrue(ds.connected(0, 2),
                "Elements should remain connected after redundant union");

        // Larger chain
        ds.union(2, 3);
        ds.union(3, 4);
        this.assertTrue(ds.connected(0, 4),
                "Long chains of unions should produce full connectivity");

        // Merge two separate components
        this.assertTrue(!ds.connected(0, 5),
                "5 should start in its own component");
        ds.union(4, 5);
        this.assertTrue(ds.connected(0, 5),
                "Merging components should connect all elements");
    }

    private void testDisjointSetFind() {
        DisjointSet<String> ds = new DisjointSet<>();

        ds.add("a");
        ds.add("b");
        ds.add("c");

        // Each element initially is its own representative
        this.assertEquals("a", ds.find("a"),
                "find() should return the element itself for a singleton set");

        this.assertEquals("b", ds.find("b"),
                "find() should return the element itself for a singleton set");

        ds.union("a", "b");

        // After union, both elements must share the same representative
        String rep1 = ds.find("a");
        String rep2 = ds.find("b");

        this.assertEquals(rep1, rep2,
                "find() should return the same representative for connected elements");

        // "c" is not connected to "a" or "b", so it must not share their representative
        this.assertTrue(!ds.connected("a", "c"),
                "Unrelated elements should not be connected");

        this.assertEquals("c", ds.find("c"),
                "find() should return the element itself for an untouched singleton set");

        // find() must be stable across calls
        this.assertEquals(rep1, ds.find("a"),
                "find() should be stable across calls");
    }

    private void testDisjointSetPathCompression() {
        DisjointSet<Integer> ds = new DisjointSet<>();
        for (int i = 0; i < 8; i++) {
            ds.add(i);
        }

        // Build a long chain: 0-1, 0-2, ..., 0-7 becomes one tree
        for (int i = 1; i < 8; i++) {
            ds.union(0, i);
        }

        // All elements must be connected
        for (int i = 1; i < 8; i++) {
            this.assertTrue(ds.connected(0, i),
                    "Element 0 should connect to " + i);
        }

        // Path compression: repeated find() on deep elements must succeed
        this.assertTrue(ds.connected(7, 0),
                "Deepest element should find its way back to the root");

        // Verify entire component remains connected after compression
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.assertTrue(ds.connected(i, j),
                        "All elements should be connected after path compression");
            }
        }
    }

    private void testDisjointSetGrow() {
        DisjointSet<Integer> ds = new DisjointSet<>();

        // Grow past the default internal capacity (16)
        for (int i = 0; i < 40; i++) {
            ds.add(i);
        }

        this.assertEquals(40, ds.size(),
                "size() should count all elements after growing");

        // Union a chain across the whole set
        for (int i = 0; i < 39; i++) {
            ds.union(i, i + 1);
        }

        this.assertTrue(ds.connected(0, 39),
                "First and last elements should be connected after growing");

        this.assertTrue(ds.connected(5, 30),
                "Middle elements should be connected after growing");
    }

    private void testDisjointSetClear() {
        DisjointSet<String> ds = new DisjointSet<>();

        ds.add("x");
        ds.add("y");
        ds.union("x", "y");

        this.assertEquals(2, ds.size(),
                "size() before clear");

        ds.clear();

        this.assertEquals(0, ds.size(),
                "size() should be 0 after clear()");

        this.assertTrue(ds.isEmpty(),
                "DisjointSet should be empty after clear()");

        // Must be reusable after clear
        ds.add("z");
        this.assertEquals(1, ds.size(),
                "DisjointSet should be usable again after clear()");

        this.assertTrue(ds.connected("z", "z"),
                "Element added after clear() should be connected to itself");
    }

    private void testDisjointSetGet() {
        DisjointSet<String> ds = new DisjointSet<>();

        ds.add("alpha");
        ds.add("beta");
        ds.add("gamma");

        this.assertEquals(3, ds.size(),
                "size() before get()");

        this.assertEquals("alpha", ds.get(0),
                "get(0) should return the first element added");

        this.assertEquals("beta", ds.get(1),
                "get(1) should return the second element added");

        this.assertEquals("gamma", ds.get(2),
                "get(2) should return the third element added");

        boolean threwNegative = false;
        try {
            ds.get(-1);
        } catch (IndexOutOfBoundsException e) {
            threwNegative = true;
        }
        this.assertTrue(threwNegative,
                "get(-1) should throw IndexOutOfBoundsException");

        boolean threwTooHigh = false;
        try {
            ds.get(3);
        } catch (IndexOutOfBoundsException e) {
            threwTooHigh = true;
        }
        this.assertTrue(threwTooHigh,
                "get(size) should throw IndexOutOfBoundsException");
    }

    private void testDisjointSetExceptions() {
        DisjointSet<Integer> ds = new DisjointSet<>();
        ds.add(0);
        ds.add(1);
        ds.add(2);

        // union() on a missing element must throw
        boolean unionThrew = false;
        try {
            ds.union(0, 10);
        } catch (IllegalArgumentException e) {
            unionThrew = true;
        }
        this.assertTrue(unionThrew,
                "union() should throw IllegalArgumentException for an invalid element");

        // connected() on a missing element must throw
        boolean connectedThrew = false;
        try {
            ds.connected(0, 10);
        } catch (IllegalArgumentException e) {
            connectedThrew = true;
        }
        this.assertTrue(connectedThrew,
                "connected() should throw IllegalArgumentException for an invalid element");

        // find() on a missing element must throw
        boolean findThrew = false;
        try {
            ds.find(10);
        } catch (IllegalArgumentException e) {
            findThrew = true;
        }
        this.assertTrue(findThrew,
                "find() should throw IllegalArgumentException for an invalid element");
    }

    private void testDisjointSetUnsupportedOperations() {
        DisjointSet<Integer> ds = new DisjointSet<>();
        ds.add(1);

        // remove() is not supported
        boolean removeThrew = false;
        try {
            ds.remove(1);
        } catch (UnsupportedOperationException e) {
            removeThrew = true;
        }
        this.assertTrue(removeThrew,
                "remove() should throw UnsupportedOperationException");

        // set() is not supported
        boolean setThrew = false;
        try {
            ds.set(0, 2);
        } catch (UnsupportedOperationException e) {
            setThrew = true;
        }
        this.assertTrue(setThrew,
                "set() should throw UnsupportedOperationException");
    }


// ==================== Queue Tests ====================

private void testQueueNormal() {
    Queue<String> q = new Queue<>();
    this.assertTrue(q.isEmpty(), "Queue should start empty");

    q.add("a");
    q.add("b");
    q.add("c");

    this.assertEquals(3, q.size(), "Queue size after adds");
    this.assertEquals("a", q.peek(), "Queue peek returns front element");
    this.assertEquals("a", q.dequeue(), "dequeue returns and removes front element");
    this.assertEquals(2, q.size(), "Queue size after one dequeue");
    this.assertEquals("b", q.get(0), "get(0) after dequeue");
    q.set(0, "bb");
    this.assertEquals("bb", q.get(0), "set(0) updates element");
    q.remove("bb");
    this.assertEquals(1, q.size(), "remove(item) should remove matching element");
}

private void testQueueBoundary() {
    Queue<Integer> q = new Queue<>();
    // DEFAULT_CAPACITY is 16; fill to capacity
    for (int i = 0; i < 16; i++) {
        q.add(i);
    }
    this.assertEquals(16, q.size(), "Queue size at capacity");

    boolean addThrew = false;
    try {
        q.add(99);
    } catch (IllegalStateException e) {
        addThrew = true;
    }
    this.assertTrue(addThrew, "Adding past capacity should throw IllegalStateException");

    // Drain the queue
    for (int i = 0; i < 16; i++) {
        q.dequeue();
    }
    this.assertTrue(q.isEmpty(), "Queue should be empty after draining");
}

private void testQueueInvalid() {
    Queue<Integer> q = new Queue<>();

    boolean nullAddThrew = false;
    try {
        q.add(null);
    } catch (IllegalArgumentException e) {
        nullAddThrew = true;
    }
    this.assertTrue(nullAddThrew, "add(null) should throw IllegalArgumentException");

    boolean dequeueEmptyThrew = false;
    try {
        q.dequeue();
    } catch (IllegalStateException e) {
        dequeueEmptyThrew = true;
    }
    this.assertTrue(dequeueEmptyThrew, "dequeue() on empty queue should throw IllegalStateException");

    boolean peekEmptyThrew = false;
    try {
        q.peek();
    } catch (IllegalStateException e) {
        peekEmptyThrew = true;
    }
    this.assertTrue(peekEmptyThrew, "peek() on empty queue should throw IllegalStateException");

    q.add(1);
    boolean getInvalidThrew = false;
    try {
        q.get(5);
    } catch (IndexOutOfBoundsException e) {
        getInvalidThrew = true;
    }
    this.assertTrue(getInvalidThrew, "get(invalid) should throw IndexOutOfBoundsException");

    boolean setNullThrew = false;
    try {
        q.set(0, null);
    } catch (IllegalArgumentException e) {
        setNullThrew = true;
    }
    this.assertTrue(setNullThrew, "set(index, null) should throw IllegalArgumentException");
}

// ==================== Heap Tests ====================

private void testHeapNormal() {
    Heap<Integer> h = new Heap<>();
    this.assertTrue(h.isEmpty(), "Heap should start empty");

    h.add(10);
    h.add(5);
    h.add(20);

    this.assertEquals(3, h.size(), "Heap size after adds");
    this.assertEquals(20, h.peek(), "peek() returns maximum element for max-heap");
    this.assertEquals(20, h.remove(), "remove() returns and removes max element");
    this.assertEquals(2, h.size(), "Heap size after remove");

    h.add(15);
    this.assertEquals(15, h.peek(), "peek() after further inserts");

    h.remove((Integer) 10);
    this.assertEquals(2, h.size(), "remove(item) should remove specific element");
}

private void testHeapBoundary() {
    Heap<Integer> h = new Heap<>();
    // DEFAULT_CAPACITY is 10; force resize by adding more elements
    for (int i = 0; i < 15; i++) {
        h.add(i);
    }
    this.assertEquals(15, h.size(), "Heap size after growing past initial capacity");
    this.assertEquals(14, h.peek(), "peek() should reflect max after many inserts");

    // Remove all elements to ensure stability after resize
    int last = h.remove();
    while (!h.isEmpty()) {
        last = h.remove();
    }
    this.assertTrue(h.isEmpty(), "Heap should be empty after removing all elements");
}

private void testHeapInvalid() {
    Heap<Integer> h = new Heap<>();

    boolean nullAddThrew = false;
    try {
        h.add(null);
    } catch (IllegalArgumentException e) {
        nullAddThrew = true;
    }
    this.assertTrue(nullAddThrew, "add(null) should throw IllegalArgumentException");

    boolean removeEmptyThrew = false;
    try {
        h.remove();
    } catch (IllegalStateException e) {
        removeEmptyThrew = true;
    }
    this.assertTrue(removeEmptyThrew, "remove() on empty heap should throw IllegalStateException");

    h.add(1);
    boolean getInvalidThrew = false;
    try {
        h.get(5);
    } catch (IndexOutOfBoundsException e) {
        getInvalidThrew = true;
    }
    this.assertTrue(getInvalidThrew, "get(invalid) should throw IndexOutOfBoundsException");

    boolean setNullThrew = false;
    try {
        h.set(0, null);
    } catch (IllegalArgumentException e) {
        setNullThrew = true;
    }
    this.assertTrue(setNullThrew, "set(index, null) should throw IllegalArgumentException");
}

// ==================== PriorityQueue Tests ====================

private void testPriorityQueueNormal() {
    PriorityQueue<String> pq = new PriorityQueue<>();
    this.assertTrue(pq.isEmpty(), "PriorityQueue should start empty");

    pq.add("low", 1);
    pq.add("med", 5);
    pq.add("high", 10);

    this.assertEquals(3, pq.size(), "PriorityQueue size after adds");
    this.assertEquals("high", pq.peek(), "peek() returns highest priority element");
    this.assertEquals(10, pq.peekPriority(), "peekPriority() returns priority of top element");

    this.assertEquals("high", pq.poll(), "poll() returns and removes highest priority");
    this.assertEquals(2, pq.size(), "size() after poll");

    pq.add("newHigh", 7);
    pq.updatePriority("low", 9); // promote low
    this.assertTrue(pq.contains("low"), "contains() should find promoted element");
    this.assertEquals("low", pq.peek(), "promoted element should become new top");
}

private void testPriorityQueueBoundary() {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    // DEFAULT_CAPACITY is 10; force resize
    for (int i = 0; i < 15; i++) {
        pq.add(i, i);
    }
    this.assertEquals(15, pq.size(), "PriorityQueue size after growing past initial capacity");
    this.assertEquals(14, pq.peek(), "peek() should be highest priority after many inserts");

    // Poll a few and ensure order
    int first = pq.poll();
    int second = pq.poll();
    this.assertTrue(first > second, "polled elements should be in descending priority order");
}

private void testPriorityQueueInvalid() {
    PriorityQueue<Integer> pq = new PriorityQueue<>();

    boolean nullAddThrew = false;
    try {
        pq.add(null);
    } catch (IllegalArgumentException e) {
        nullAddThrew = true;
    }
    this.assertTrue(nullAddThrew, "add(null) should throw IllegalArgumentException");

    boolean pollEmptyThrew = false;
    try {
        pq.poll();
    } catch (IllegalStateException e) {
        pollEmptyThrew = true;
    }
    this.assertTrue(pollEmptyThrew, "poll() on empty priority queue should throw IllegalStateException");

    boolean peekEmptyThrew = false;
    try {
        pq.peek();
    } catch (IllegalStateException e) {
        peekEmptyThrew = true;
    }
    this.assertTrue(peekEmptyThrew, "peek() on empty priority queue should throw IllegalStateException");

    this.assertTrue(!pq.contains(null), "contains(null) should return false");
}



// ==================== Stack Tests ====================

private void testStackNormal() {
    Stack<Integer> stack = new Stack<>();
    stack.push(10);
    stack.push(20);
    stack.push(30);

    this.assertEquals(3, stack.size(), "Stack size after 3 pushes");
    this.assertEquals(30, stack.peek(), "Stack peek should return the most recent item");
    this.assertEquals(30, stack.pop(), "Stack pop should return LIFO order (30)");
    this.assertEquals(20, stack.pop(), "Stack pop should return LIFO order (20)");
    this.assertEquals(1, stack.size(), "Stack size after 2 pops");
}

private void testStackBoundary() {
    Stack<Integer> stack = new Stack<>();
    this.assertTrue(stack.isEmpty(), "Stack should start empty");

    stack.push(99);
    this.assertTrue(!stack.isEmpty(), "Stack should not be empty after push");
    this.assertEquals(1, stack.size(), "Stack size after single push");
    this.assertEquals(99, stack.peek(), "Stack peek should return the single item");
    this.assertEquals(99, stack.pop(), "Stack pop should return the single item");

    this.assertTrue(stack.isEmpty(), "Stack should be empty after popping its only item");
}

private void testStackInvalid() {
    Stack<Integer> stack = new Stack<>();

    boolean threwOnPop = false;
    try {
        stack.pop();
    } catch (IllegalStateException e) {
        threwOnPop = true;
    }
    this.assertTrue(threwOnPop, "pop() on empty Stack should throw IllegalStateException");

    boolean threwOnPeek = false;
    try {
        stack.peek();
    } catch (IllegalStateException e) {
        threwOnPeek = true;
    }
    this.assertTrue(threwOnPeek, "peek() on empty Stack should throw IllegalStateException");
}

    // ==================== Assertion Helpers ====================

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