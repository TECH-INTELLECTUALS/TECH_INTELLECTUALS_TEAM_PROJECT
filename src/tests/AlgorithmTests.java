package tests;

import algorithms.DijkstraAlgorithm;
import algorithms.PrimAlgorithm;
import algorithms.KruskalAlgorithm;
import datastructures.Graph;
import datastructures.DisjointSet;

/**
 * Test suite for algorithm classes.
 */
public class AlgorithmTests {

    /**
     * Runs all algorithm tests.
     */
    public void runTests() {
        testDijkstraPrimKruskal();
    }

    // =========================================================
    // Dzifa's tests:
    // DijkstraAlgorithm, PrimAlgorithm, KruskalAlgorithm
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
    // HELPER METHODS
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
}