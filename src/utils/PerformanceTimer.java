package utils;

/**
 * Utility placeholder for measuring algorithm and service performance.
 */
public class PerformanceTimer {

    private long startTimeNanos;
    private long startMemoryBytes;
    private double lastElapsedMs;
    private long lastMemoryUsedBytes;

    /**
     * Starts the timer.
     */
    public void start() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // reduce garbage-collection noise before measuring (not perfectly reliable, but standard practice)
        startMemoryBytes = runtime.totalMemory() - runtime.freeMemory();
        startTimeNanos = System.nanoTime();
    }

    /**
     * Stops the timer and reports elapsed time.
     */
    public void stop() {
        long endTimeNanos = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long endMemoryBytes = runtime.totalMemory() - runtime.freeMemory();

        lastElapsedMs = (endTimeNanos - startTimeNanos) / 1_000_000.0;
        // clamp to 0: GC timing can make this read negative on some runs, which isn't meaningful
        lastMemoryUsedBytes = Math.max(0, endMemoryBytes - startMemoryBytes);

        System.out.printf("Elapsed: %.4f ms | Memory used: %d bytes%n", lastElapsedMs, lastMemoryUsedBytes);
    }

    /** @return elapsed time in milliseconds from the most recent start()/stop() pair */
    public double getElapsedMs() {
        return lastElapsedMs;
    }

    /** @return approximate memory used in bytes from the most recent start()/stop() pair */
    public long getMemoryUsedBytes() {
        return lastMemoryUsedBytes;
    }
}