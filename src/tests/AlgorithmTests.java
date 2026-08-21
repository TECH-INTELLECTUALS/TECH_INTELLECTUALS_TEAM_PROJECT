package tests;

import algorithms.DijkstraAlgorithm;
import algorithms.PrimAlgorithm;
import algorithms.KruskalAlgorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

// Emmanuel's imports (added)
import algorithms.BinarySearch;
import algorithms.InsertionSort;
import algorithms.LinearSearch;
import algorithms.MergeSort;
import algorithms.QuickSort;
import algorithms.SelectionSort;
import java.util.Arrays;

import algorithms.BreadthFirstSearch;
import algorithms.DepthFirstSearch;

/**
 * Test suite for algorithm classes.
 */
public class AlgorithmTests {

    /**
     * Runs all algorithm tests.
     */
    public void runTests() {
        // =========================================================
        // Emmanuel's search/sort tests (added)
        // =========================================================
        testBinarySearch();
        testLinearSearch();
        testSelectionSort();
        testInsertionSort();
        testMergeSort();
        testQuickSort();

        // =========================================================
        // Dzifa's graph algorithm tests (original)
        // =========================================================
        testDijkstraPrimKruskal();


    testBFSNormal();
    testBFSDisconnected();
    testBFSInvalidStart();
    testDFSNormal();
    testDFSDisconnected();
    testDFSInvalidStart();
    }

    // =========================================================
    // Dzifa's tests (unchanged)
    // =========================================================

    /**
     * Shared connected test graph used for Dijkstra, Prim and Kruskal.
     *
     * Edges:
     * 0-1 (4)
     * 0-2 (1)
     * 1-2 (2)
     * 1-3 (1)
     * 2-3 (5)
     */
    private Graph<Integer> buildConnectedTestGraph() {
        Graph<Integer> g = new Graph<>();

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 5);

        return g;
    }

    /**
     * Connected graph plus an isolated vertex 4.
     * Used to test disconnected/unreachable cases.
     */
    private Graph<Integer> buildDisconnectedTestGraph() {
        Graph<Integer> g = buildConnectedTestGraph();

        g.add(4);

        return g;
    }

    /**
     * Runs all 9 tests.
     */
    public void testDijkstraPrimKruskal() {

        // Dijkstra - 3 tests
        testDijkstraNormal();
        testDijkstraDisconnected();
        testDijkstraInvalidInput();

        // Prim - 3 tests
        testPrimNormal();
        testPrimSingleVertex();
        testPrimDisconnected();

        // Kruskal - 3 tests
        testKruskalNormal();
        testKruskalDisconnected();
        testKruskalEmptyGraph();
    }

    // =========================================================
    // DIJKSTRA TESTS
    // =========================================================

    /**
     * Test 1:
     * Normal shortest-path case.
     *
     * Expected shortest path from 0 to 3:
     * 0 -> 2 -> 1 -> 3
     *
     * Total distance:
     * 1 + 2 + 1 = 4
     */
    private void testDijkstraNormal() {

        Graph<Integer> g = buildConnectedTestGraph();

        DijkstraAlgorithm<Integer> dijkstra =
                new DijkstraAlgorithm<>();

        dijkstra.shortestPath(g, 0);

        int distanceTo3 = dijkstra.getDistance(3);

        int[] path = dijkstra.getPath(3);

        int expectedDistance = 4;

        int[] expectedPath = {0, 2, 1, 3};

        boolean passed =
                distanceTo3 == expectedDistance
                && arraysEqual(path, expectedPath);

        printResult(
                "Dijkstra - normal case (0 -> 3)",
                passed,
                "expected distance "
                        + expectedDistance
                        + ", got "
                        + distanceTo3
                        + " | expected path "
                        + toStr(expectedPath)
                        + ", got "
                        + toStr(path)
        );
    }

    /**
     * Test 2:
     * Disconnected/unreachable vertex.
     *
     * Vertex 4 exists but has no connecting edges.
     *
     * Dijkstra represents an unreachable vertex with
     * Integer.MAX_VALUE and returns an empty path.
     */
    private void testDijkstraDisconnected() {

        Graph<Integer> g = buildDisconnectedTestGraph();

        DijkstraAlgorithm<Integer> dijkstra =
                new DijkstraAlgorithm<>();

        dijkstra.shortestPath(g, 0);

        int distanceToIsolated =
                dijkstra.getDistance(4);

        int[] pathToIsolated =
                dijkstra.getPath(4);

        boolean passed =
                distanceToIsolated == Integer.MAX_VALUE
                && pathToIsolated.length == 0;

        printResult(
                "Dijkstra - edge case (disconnected vertex)",
                passed,
                "expected unreachable distance and empty path, got distance "
                        + distanceToIsolated
                        + " and path "
                        + toStr(pathToIsolated)
        );
    }

    /**
     * Test 3:
     * Calling getDistance() before shortestPath()
     * should throw IllegalStateException.
     */
    private void testDijkstraInvalidInput() {

        DijkstraAlgorithm<Integer> dijkstra =
                new DijkstraAlgorithm<>();

        boolean threwException = false;

        try {

            dijkstra.getDistance(1);

        } catch (IllegalStateException e) {

            threwException = true;
        }

        printResult(
                "Dijkstra - invalid input (getDistance before shortestPath)",
                threwException,
                threwException
                        ? "correctly threw IllegalStateException"
                        : "expected IllegalStateException but none was thrown"
        );
    }

    // =========================================================
    // PRIM TESTS
    // =========================================================

    /**
     * Test 4:
     * Normal connected graph.
     *
     * A connected graph with 4 vertices should produce
     * 3 MST edges.
     *
     * Expected MST weight = 1 + 2 + 1 = 4.
     */
    private void testPrimNormal() {

        Graph<Integer> g = buildConnectedTestGraph();

        PrimAlgorithm<Integer> prim =
                new PrimAlgorithm<>();

        int[][] mst = prim.mst(g);

        int totalWeight = sumWeights(mst);

        boolean passed =
                mst.length == 3
                && totalWeight == 4;

        printResult(
                "Prim - normal case (MST weight)",
                passed,
                "expected 3 edges totaling 4, got "
                        + mst.length
                        + " edges totaling "
                        + totalWeight
        );
    }

    /**
     * Test 5:
     * Single-vertex graph.
     *
     * A graph containing only one vertex has no edges,
     * so its MST should contain zero edges.
     */
    private void testPrimSingleVertex() {

        Graph<Integer> g = new Graph<>();

        g.add(0);

        PrimAlgorithm<Integer> prim =
                new PrimAlgorithm<>();

        int[][] mst = prim.mst(g);

        boolean passed =
                mst.length == 0;

        printResult(
                "Prim - edge case (single vertex)",
                passed,
                "expected 0 edges, got "
                        + mst.length
        );
    }

    /**
     * Test 6:
     * Disconnected graph.
     *
     * Since an MST cannot connect all vertices of a
     * disconnected graph, fewer than n - 1 edges should
     * be returned.
     */
    private void testPrimDisconnected() {

        Graph<Integer> g =
                buildDisconnectedTestGraph();

        PrimAlgorithm<Integer> prim =
                new PrimAlgorithm<>();

        int[][] mst = prim.mst(g);

        boolean passed =
                mst.length < g.size() - 1;

        printResult(
                "Prim - invalid case (disconnected graph)",
                passed,
                "expected fewer than "
                        + (g.size() - 1)
                        + " edges, got "
                        + mst.length
        );
    }

    // =========================================================
    // KRUSKAL TESTS
    // =========================================================

    /**
     * Test 7:
     * Normal connected graph.
     *
     * Expected:
     * 3 MST edges
     * Total MST weight = 4
     */
    private void testKruskalNormal() {

        Graph<Integer> g =
                buildConnectedTestGraph();

        KruskalAlgorithm<Integer> kruskal =
                new KruskalAlgorithm<>();

        DisjointSet<Integer> ds =
                new DisjointSet<>();

        int[][] mst =
                kruskal.mst(g, ds);

        int totalWeight =
                sumWeights(mst);

        boolean passed =
                mst.length == 3
                && totalWeight == 4;

        printResult(
                "Kruskal - normal case (MST weight)",
                passed,
                "expected 3 edges totaling 4, got "
                        + mst.length
                        + " edges totaling "
                        + totalWeight
        );
    }

    /**
     * Test 8:
     * Disconnected graph.
     *
     * Kruskal should return fewer than n - 1 edges
     * because the graph cannot have a complete MST.
     */
    private void testKruskalDisconnected() {

        Graph<Integer> g =
                buildDisconnectedTestGraph();

        KruskalAlgorithm<Integer> kruskal =
                new KruskalAlgorithm<>();

        DisjointSet<Integer> ds =
                new DisjointSet<>();

        int[][] mst =
                kruskal.mst(g, ds);

        boolean passed =
                mst.length < g.size() - 1;

        printResult(
                "Kruskal - invalid case (disconnected graph)",
                passed,
                "expected fewer than "
                        + (g.size() - 1)
                        + " edges, got "
                        + mst.length
        );
    }

    /**
     * Test 9:
     * Empty graph.
     *
     * An empty graph should produce an empty MST.
     */
    private void testKruskalEmptyGraph() {

        Graph<Integer> g =
                new Graph<>();

        KruskalAlgorithm<Integer> kruskal =
                new KruskalAlgorithm<>();

        DisjointSet<Integer> ds =
                new DisjointSet<>();

        int[][] mst =
                kruskal.mst(g, ds);

        boolean passed =
                mst.length == 0;

        printResult(
                "Kruskal - edge case (empty graph)",
                passed,
                "expected 0 edges, got "
                        + mst.length
        );
    }

    // =========================================================
    // Dzifa's HELPER METHODS
    // =========================================================

    /**
     * Calculates the total weight of MST edges.
     */
    private int sumWeights(int[][] edges) {

        int total = 0;

        for (int[] edge : edges) {

            total += edge[2];
        }

        return total;
    }

    /**
     * Compares two integer arrays.
     */
    private boolean arraysEqual(int[] a, int[] b) {

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {

            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts an integer array to readable text.
     */
    private String toStr(int[] arr) {

        StringBuilder sb =
                new StringBuilder("[");

        for (int i = 0; i < arr.length; i++) {

            sb.append(arr[i]);

            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * Prints the result of each test.
     */
    private void printResult(
            String testName,
            boolean passed,
            String details) {

        System.out.println(
                (passed ? "PASS" : "FAIL")
                        + " - "
                        + testName
        );

        System.out.println(
                "    "
                        + details
        );
    }

    // =========================================================
    // Emmanuel's tests (appended)
    // =========================================================

    private void testBinarySearch() {
        // Normal case: sorted array with duplicates.
        int[] arr1 = {1, 3, 3, 5, 7, 9, 9, 11};
        BinarySearch bs1 = new BinarySearch(arr1, 9);
        bs1.execute();
        int r1 = bs1.getResult();
        this.assertTrue(r1 == 5 || r1 == 6, "BinarySearch should find value 9 at index 5 or 6");

        // Boundary case: empty array
        BinarySearch bs2 = new BinarySearch(new int[]{}, 5);
        bs2.execute();
        this.assertEquals(-1, bs2.getResult(), "BinarySearch on empty array returns -1");

        // Boundary case: single element array, found
        BinarySearch bs3 = new BinarySearch(new int[]{42}, 42);
        bs3.execute();
        this.assertEquals(0, bs3.getResult(), "BinarySearch single-element array, value present");

        // Boundary case: single element array, not found
        BinarySearch bs4 = new BinarySearch(new int[]{42}, 7);
        bs4.execute();
        this.assertEquals(-1, bs4.getResult(), "BinarySearch single-element array, value absent");

        // Invalid case / required counterexample: unsorted array.
        int[] unsorted = {50, 10, 40, 20, 5, 30};
        BinarySearch bs5 = new BinarySearch(unsorted, 20);
        bs5.execute();
        this.assertTrue(bs5.getResult() != 3,
                "Counterexample: BinarySearch should NOT reliably find index 3 in an "
                + "unsorted array, proving sorted input is a required precondition");
    }

    private void testLinearSearch() {
        LinearSearch ls1 = new LinearSearch(new int[]{8, 3, 5, 3, 9, 1, 5}, 5);
        ls1.execute();
        this.assertEquals(2, ls1.getResult(), "LinearSearch returns first match at index 2");

        LinearSearch ls2 = new LinearSearch(new int[]{}, 5);
        ls2.execute();
        this.assertEquals(-1, ls2.getResult(), "LinearSearch on empty array returns -1");

        LinearSearch ls3 = new LinearSearch(new int[]{7}, 7);
        ls3.execute();
        this.assertEquals(0, ls3.getResult(), "LinearSearch single-element array, value present");

        LinearSearch ls4 = new LinearSearch(new int[]{7}, 99);
        ls4.execute();
        this.assertEquals(-1, ls4.getResult(), "LinearSearch single-element array, value absent");

        LinearSearch ls5 = new LinearSearch(new int[]{1, 2, 3, 4, 5}, 4);
        ls5.execute();
        this.assertEquals(3, ls5.getResult(), "LinearSearch on already-sorted array");
    }

    private void testSelectionSort() {
        SelectionSort ss1 = new SelectionSort(new int[]{29, 10, 14, 37, 10, 5, 29});
        ss1.execute();
        this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, ss1.getResult(),
                "SelectionSort on unsorted array with duplicates");

        SelectionSort ss2 = new SelectionSort(new int[]{});
        ss2.execute();
        this.assertArrayEquals(new int[]{}, ss2.getResult(), "SelectionSort on empty array");

        SelectionSort ss3 = new SelectionSort(new int[]{42});
        ss3.execute();
        this.assertArrayEquals(new int[]{42}, ss3.getResult(), "SelectionSort on single-element array");

        SelectionSort ss4 = new SelectionSort(new int[]{1, 2, 3, 4, 5});
        ss4.execute();
        this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, ss4.getResult(),
                "SelectionSort on already-sorted array");

        SelectionSort ss5 = new SelectionSort(new int[]{4, 2, 2, 1});
        ss5.execute();
        this.assertArrayEquals(new int[]{1, 2, 2, 4}, ss5.getResult(),
                "SelectionSort correctness check relevant to stability discussion");
    }

    private void testInsertionSort() {
        InsertionSort is1 = new InsertionSort(new int[]{29, 10, 14, 37, 10, 5, 29});
        is1.execute();
        this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, is1.getResult(),
                "InsertionSort on unsorted array with duplicates");

        InsertionSort is2 = new InsertionSort(new int[]{});
        is2.execute();
        this.assertArrayEquals(new int[]{}, is2.getResult(), "InsertionSort on empty array");

        InsertionSort is3 = new InsertionSort(new int[]{42});
        is3.execute();
        this.assertArrayEquals(new int[]{42}, is3.getResult(), "InsertionSort on single-element array");

        InsertionSort is4 = new InsertionSort(new int[]{1, 2, 3, 4, 5});
        is4.execute();
        this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, is4.getResult(),
                "InsertionSort on already-sorted array (best case)");
    }

    private void testMergeSort() {
        MergeSort ms1 = new MergeSort(new int[]{29, 10, 14, 37, 10, 5, 29});
        ms1.execute();
        this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, ms1.getResult(),
                "MergeSort on unsorted array with duplicates");

        MergeSort ms2 = new MergeSort(new int[]{});
        ms2.execute();
        this.assertArrayEquals(new int[]{}, ms2.getResult(), "MergeSort on empty array");

        MergeSort ms3 = new MergeSort(new int[]{42});
        ms3.execute();
        this.assertArrayEquals(new int[]{42}, ms3.getResult(), "MergeSort on single-element array");

        MergeSort ms4 = new MergeSort(new int[]{1, 2, 3, 4, 5});
        ms4.execute();
        this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, ms4.getResult(),
                "MergeSort on already-sorted array");
    }

    private void testQuickSort() {
        QuickSort qs1 = new QuickSort(new int[]{29, 10, 14, 37, 10, 5, 29});
        qs1.execute();
        this.assertArrayEquals(new int[]{5, 10, 10, 14, 29, 29, 37}, qs1.getResult(),
                "QuickSort on unsorted array with duplicates");

        QuickSort qs2 = new QuickSort(new int[]{});
        qs2.execute();
        this.assertArrayEquals(new int[]{}, qs2.getResult(), "QuickSort on empty array");

        QuickSort qs3 = new QuickSort(new int[]{42});
        qs3.execute();
        this.assertArrayEquals(new int[]{42}, qs3.getResult(), "QuickSort on single-element array");

        QuickSort qs4 = new QuickSort(new int[]{1, 2, 3, 4, 5});
        qs4.execute();
        this.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, qs4.getResult(),
                "QuickSort on already-sorted array (worst-case timing, correctness ok)");
    }

    // =========================================================
    // Emmanuel's assertion helpers
    // =========================================================

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

    private void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError(message + ": expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }


    // ==================== BFS Tests ====================

private void testBFSNormal() {
    BreadthFirstSearch<Integer> bfs = new BreadthFirstSearch<>();
    Graph<Integer> g = buildConnectedTestGraph();
    int[] order = bfs.traverse(g, 0);
    this.assertArrayEquals(new int[]{0, 1, 2, 3}, order, "BFS traverse from 0 on connected graph");
}

private void testBFSDisconnected() {
    BreadthFirstSearch<Integer> bfs = new BreadthFirstSearch<>();
    Graph<Integer> g = buildDisconnectedTestGraph();
    int[] order = bfs.traverse(g, 0);
    this.assertEquals(4, order.length, "BFS should visit only reachable vertices in disconnected graph");
    this.assertTrue(!bfs.isReachable(g, 0, 4), "BFS.isReachable should return false for isolated vertex");
}

private void testBFSInvalidStart() {
    BreadthFirstSearch<Integer> bfs = new BreadthFirstSearch<>();
    boolean threw = false;
    try {
        bfs.traverse(buildConnectedTestGraph(), 99);
    } catch (IndexOutOfBoundsException e) {
        threw = true;
    }
    this.assertTrue(threw, "BFS.traverse with invalid start index should throw IndexOutOfBoundsException");
}

// ==================== DFS Tests ====================

private void testDFSNormal() {
    DepthFirstSearch<Integer> dfs = new DepthFirstSearch<>();
    Graph<Integer> g = buildConnectedTestGraph();
    int[] order = dfs.traverse(g, 0);
    this.assertArrayEquals(new int[]{0, 1, 2, 3}, order, "DFS traverse from 0 on connected graph");
}

private void testDFSDisconnected() {
    DepthFirstSearch<Integer> dfs = new DepthFirstSearch<>();
    Graph<Integer> g = buildDisconnectedTestGraph();
    int[] order = dfs.traverse(g, 0);
    this.assertEquals(4, order.length, "DFS should visit only reachable vertices in disconnected graph");
    this.assertTrue(!dfs.isReachable(g, 0, 4), "DFS.isReachable should return false for isolated vertex");
}

private void testDFSInvalidStart() {
    DepthFirstSearch<Integer> dfs = new DepthFirstSearch<>();
    boolean threw = false;
    try {
        dfs.traverse(buildConnectedTestGraph(), -1);
    } catch (IndexOutOfBoundsException e) {
        threw = true;
    }
    this.assertTrue(threw, "DFS.traverse with invalid start index should throw IndexOutOfBoundsException");
}



    // =========================================================
    // Main entry point
    // =========================================================

    public static void main(String[] args) {
        new AlgorithmTests().runTests();
    }
}