package models;

/**
 * Tracks a single algorithm execution instance for analytics and review.
 */
public class AlgorithmRun {

    private int algorithmRunId;
    private String algorithmName;
    private int inputSize;
    private long executionTimeNs;
    private long memoryKb;
    private String resultSummary;
    private String timestamp;


    public AlgorithmRun(
            int algorithmRunId,
            String algorithmName,
            int inputSize,
            long executionTimeNs,
            long memoryKb,
            String resultSummary,
            String timestamp
    ) {

        this.algorithmRunId = algorithmRunId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.executionTimeNs = executionTimeNs;
        this.memoryKb = memoryKb;
        this.resultSummary = resultSummary;
        this.timestamp = timestamp;
    }


    public int getAlgorithmRunId() {
        return algorithmRunId;
    }

    public void setAlgorithmRunId(int algorithmRunId) {
        this.algorithmRunId = algorithmRunId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public void setExecutionTimeNs(long executionTimeNs) {
        this.executionTimeNs = executionTimeNs;
    }

    public long getMemoryKb() {
        return memoryKb;
    }

    public void setMemoryKb(long memoryKb) {
        this.memoryKb = memoryKb;
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}