package utils;

import datastructures.HashTable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * M10 - Empirical Efficiency Lab (hash table load-factor experiment)
 *
 * HashTable resizes itself automatically once load factor exceeds 0.75
 * (see HashTable.java), so it should stay close to O(1) amortized for
 * put()/get() no matter how large n gets - that's the behavior this
 * experiment is meant to demonstrate empirically.
 *
 * NOTE: HashTable exposes no getter for its internal table length, so
 * this runner can't report the actual load factor reached at each size -
 * only the resulting put()/get() timing. Worth asking Daniel (owner) for
 * a getCapacity()-style method if the report needs the real load factor
 * number, rather than just the timing curve shape.
 *
 * Output: results/hashtable_runs.csv
 */
public class HashTablePerformanceLabRunner {

    private static final int[] INPUT_SIZES = {100, 1000, 5000, 10000, 50000, 100000};
    private static final int TRIALS = 5;
    private static final String OUTPUT_PATH = "results/hashtable_runs.csv";

    public static void main(String[] args) throws IOException {
        new java.io.File("results").mkdirs();

        try (PrintWriter csv = new PrintWriter(new FileWriter(OUTPUT_PATH))) {
            csv.println("algorithm_name,input_size,trial,time_ms,memory_bytes,timestamp");
            PerformanceTimer timer = new PerformanceTimer();

            for (int size : INPUT_SIZES) {
                for (int trial = 1; trial <= TRIALS; trial++) {

                    HashTable<Integer> table = new HashTable<>();
                    String[] keys = generateKeys(size);

                    // ---- put(): bulk-insert n keys, timed as one block ----
                    timeOperation(csv, timer, "hashPut", size, trial, () -> {
                        for (int i = 0; i < size; i++) {
                            table.put(keys[i], i);
                        }
                    });

                    // ---- get(): bulk-lookup the same n keys, timed as one block ----
                    timeOperation(csv, timer, "hashGet", size, trial, () -> {
                        for (int i = 0; i < size; i++) {
                            table.get(keys[i]);
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

    private static String[] generateKeys(int size) {
        String[] keys = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = "key" + i;
        }
        return keys;
    }
}