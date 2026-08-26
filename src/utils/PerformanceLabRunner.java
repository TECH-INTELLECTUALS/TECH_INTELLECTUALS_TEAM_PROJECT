package utils; // lives alongside PerformanceTimer

import algorithms.BinarySearch;
import algorithms.LinearSearch;   
import algorithms.InsertionSort;  
import algorithms.SelectionSort;  
import algorithms.MergeSort;      
import algorithms.QuickSort;      
import interfaces.Algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * M10 - Empirical Efficiency Lab
 * Times each algorithm across increasing input sizes and writes
 * one CSV row per (algorithm, input_size, trial).
 *
 * Output: results/algorithm_runs.csv
 */
public class PerformanceLabRunner {

    private static final int[] INPUT_SIZES = {100, 500, 1000, 5000, 10000, 50000};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "results/algorithm_runs.csv";

    public static void main(String[] args) throws IOException {
        new java.io.File("results").mkdirs();

        try (PrintWriter csv = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            csv.println("algorithm_name,input_size,trial,time_ms,memory_bytes,timestamp");
            PerformanceTimer timer = new PerformanceTimer();

            for (int size : INPUT_SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {

                    // ---- Searches: need sorted data + a target that exists in the array ----
                    int[] sortedData = generateSortedArray(size);
                    int target = sortedData[new Random(42).nextInt(size)];

                    Algorithm binarySearch = new BinarySearch(sortedData.clone(), target);
                    timeAlgorithm(csv, timer, "binarySearch", size, trial, binarySearch);

                    Algorithm linearSearch = new LinearSearch(sortedData.clone(), target);
                    timeAlgorithm(csv, timer, "linearSearch", size, trial, linearSearch);

                    // ---- Sorts: fresh random array each trial, cloned per algorithm ----
                    int[] randomData = generateRandomArray(size);

                    Algorithm insertionSort = new InsertionSort(randomData.clone());
                    timeAlgorithm(csv, timer, "insertionSort", size, trial, insertionSort);

                    Algorithm selectionSort = new SelectionSort(randomData.clone());
                    timeAlgorithm(csv, timer, "selectionSort", size, trial, selectionSort);

                    Algorithm mergeSort = new MergeSort(randomData.clone());
                    timeAlgorithm(csv, timer, "mergeSort", size, trial, mergeSort);

                    Algorithm quickSort = new QuickSort(randomData.clone());
                    timeAlgorithm(csv, timer, "quickSort", size, trial, quickSort);

                    // ---- TODO: graph algorithms (BFS, DFS, Dijkstra, Prim, Kruskal) and
                    // GreedyScheduler / DynamicProgramming go here once their constructors
                    // are confirmed - they'll need a Graph object built at each size instead
                    // of a plain int[], so the setup step will look different from the above.
                }
                System.out.println("Finished size " + size);
            }
        }
        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }

    /**
     * Generic timing wrapper - works for ANY class implementing Algorithm,
     * since every algorithm shares start-execute-stop regardless of its constructor.
     */
    private static void timeAlgorithm(PrintWriter csv, PerformanceTimer timer,
                                       String label, int inputSize, int trial, Algorithm algo) {
        timer.start();
        algo.execute();
        timer.stop();

        csv.printf("%s,%d,%d,%.4f,%d,%d%n",
                label, inputSize, trial, timer.getElapsedMs(), timer.getMemoryUsedBytes(),
                System.currentTimeMillis());
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random(42);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = rand.nextInt(size * 10);
        return arr;
    }

    private static int[] generateSortedArray(int size) {
        int[] arr = generateRandomArray(size);
        Arrays.sort(arr); // test-data setup only, not graded algorithm logic
        return arr;
    }
}