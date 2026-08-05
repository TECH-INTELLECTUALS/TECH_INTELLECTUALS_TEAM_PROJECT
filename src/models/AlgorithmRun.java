package models;

/**
 * Tracks a single algorithm execution instance for analytics and review.
 */
public class AlgorithmRun {

    private int algorithmRunId;
    private String algorithmName;
    private int inputSize;
    private long executionTimeMs;
    private String resultSummary;
    private String timestamp;


    public AlgorithmRun(
            int algorithmRunId,
            String algorithmName,
            int inputSize,
            long executionTimeMs,
            String resultSummary,
            String timestamp
    ) {

        this.algorithmRunId = algorithmRunId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.executionTimeMs = executionTimeMs;
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


    public long getExecutionTimeMs() {
        return executionTimeMs;
    }


    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
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