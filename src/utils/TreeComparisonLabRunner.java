package utils;

import datastructures.BinarySearchTree;
import datastructures.RedBlackTree; // NOTE: mislabeled per its own doc comment - actually a simplified AVL tree

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * M10 - Empirical Efficiency Lab (BST vs balanced-tree experiment)
 *
 * Compares BinarySearchTree (plain, unbalanced) against RedBlackTree
 * (actually a simplified AVL tree - see its own doc comment) under TWO
 * insertion orders per tree type: random and already-sorted.
 *
 * Sorted-order insertion into a plain BST is the classic worst case -
 * every new value becomes the new rightmost node, degenerating the tree
 * toward a linked list (O(n) per insert, O(n^2) total). A balanced tree
 * is specifically built to avoid this via rotations. Tracking the final
 * tree height() alongside timing makes this difference undeniable, even
 * where the timing gap alone might look subtle at smaller sizes.
 *
 * NOTE: max input size here is much smaller than the array/graph
 * experiments - a sorted-order BST insert of n items is O(n^2), so even
 * 20,000 already means roughly 200 million pointer operations worst-case.
 *
 * Output: results/tree_comparison_runs.csv
 */
public class TreeComparisonLabRunner {

    private static final int[] INPUT_SIZES = {100, 500, 1000, 5000, 10000, 20000};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "results/tree_comparison_runs.csv";

    public static void main(String[] args) throws IOException {
        new java.io.File("results").mkdirs();

        try (PrintWriter csv = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            // extra "resulting_height" column vs. the other runners - the whole
            // point of this experiment is the height gap, not just timing
            csv.println("algorithm_name,input_size,trial,time_ms,memory_bytes,resulting_height,timestamp");
            PerformanceTimer timer = new PerformanceTimer();

            for (int size : INPUT_SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {

                    Integer[] randomData = generateRandomData(size);
                    Integer[] sortedData = generateSortedData(size);

                    BinarySearchTree<Integer> bstRandom = new BinarySearchTree<>();
                    timeInsert(csv, timer, "bst_random", size, trial, randomData, bstRandom::add, bstRandom::height);

                    BinarySearchTree<Integer> bstSorted = new BinarySearchTree<>();
                    timeInsert(csv, timer, "bst_sorted", size, trial, sortedData, bstSorted::add, bstSorted::height);

                    RedBlackTree<Integer> balancedRandom = new RedBlackTree<>();
                    timeInsert(csv, timer, "balanced_random", size, trial, randomData, balancedRandom::add, balancedRandom::height);

                    RedBlackTree<Integer> balancedSorted = new RedBlackTree<>();
                    timeInsert(csv, timer, "balanced_sorted", size, trial, sortedData, balancedSorted::add, balancedSorted::height);
                }
                System.out.println("Finished size " + size);
            }
        }
        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }

    private static void timeInsert(PrintWriter csv, PerformanceTimer timer, String label, int inputSize,
                                    int trial, Integer[] data, Consumer<Integer> insertFn,
                                    IntSupplier heightFn) {
        timer.start();
        for (Integer value : data) {
            insertFn.accept(value);
        }
        timer.stop();

        int height = heightFn.getAsInt();

        csv.printf("%s,%d,%d,%.4f,%d,%d,%d%n",
                label, inputSize, trial, timer.getElapsedMs(), timer.getMemoryUsedBytes(),
                height, System.currentTimeMillis());
    }

    /** Unique values 0..size-1 in random order (via Fisher-Yates shuffle). */
    private static Integer[] generateRandomData(int size) {
        Random rand = new Random(42);
        Integer[] data = new Integer[size];
        for (int i = 0; i < size; i++) data[i] = i;
        for (int i = size - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Integer tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }
        return data;
    }

    /** Values 0..size-1 in ascending order - the BST worst case. */
    private static Integer[] generateSortedData(int size) {
        Integer[] data = new Integer[size];
        for (int i = 0; i < size; i++) data[i] = i;
        return data;
    }
}