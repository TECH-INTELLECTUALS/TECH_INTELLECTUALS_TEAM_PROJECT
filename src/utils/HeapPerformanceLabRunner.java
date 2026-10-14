package utils;

import datastructures.Heap;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * M10 - Empirical Efficiency Lab (heap-dispatch experiment)
 *
 * Models the project's real use case: service requests arrive with a
 * priority/urgency value, get inserted into the heap, and are dispatched
 * (extracted) highest-priority-first. Heap.java is confirmed a max-heap
 * (see heapifyUp/heapifyDown logic), matching "highest urgency first".
 *
 * Insert phase and dispatch phase are timed separately, since they have
 * different theoretical shapes: n inserts should total O(n log n), and
 * fully draining the heap via n removals is also O(n log n) - but it's
 * worth seeing whether the empirical curves actually match each other.
 *
 * Output: results/heap_runs.csv
 */
public class HeapPerformanceLabRunner {

    private static final int[] INPUT_SIZES = {100, 1000, 5000, 10000, 50000, 100000};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "results/heap_runs.csv";

    private static class PriorityItem implements Comparable<PriorityItem> {
        final int priority;

        PriorityItem(int priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityItem other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    public static void main(String[] args) throws IOException {
        new java.io.File("results").mkdirs();

        try (PrintWriter csv = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            csv.println("algorithm_name,input_size,trial,time_ms,memory_bytes,timestamp");
            PerformanceTimer timer = new PerformanceTimer();

            for (int size : INPUT_SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {

                    PriorityItem[] items = generateItems(size);
                    Heap<PriorityItem> heap = new Heap<>();

                    // ---- Insert phase: n items, timed as one block ----
                    timeOperation(csv, timer, "heapInsert", size, trial, () -> {
                        for (PriorityItem item : items) {
                            heap.add(item);
                        }
                    });

                    // ---- Dispatch phase: drain the heap highest-priority-first ----
                    timeOperation(csv, timer, "heapDispatch", size, trial, () -> {
                        while (!heap.isEmpty()) {
                            heap.remove();
                        }
                    });
                }
                System.out.println("Finished size " + size);
            }
        }
        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }

    private static void timeOperation(PrintWriter csv, PerformanceTimer timer,
                                       String label, int inputSize, int trial, Runnable task) {
        timer.start();
        task.run();
        timer.stop();

        csv.printf("%s,%d,%d,%.4f,%d,%d%n",
                label, inputSize, trial, timer.getElapsedMs(), timer.getMemoryUsedBytes(),
                System.currentTimeMillis());
    }

    private static PriorityItem[] generateItems(int size) {
        Random rand = new Random(42);
        PriorityItem[] items = new PriorityItem[size];
        for (int i = 0; i < size; i++) {
            items[i] = new PriorityItem(rand.nextInt(size * 10));
        }
        return items;
    }
}