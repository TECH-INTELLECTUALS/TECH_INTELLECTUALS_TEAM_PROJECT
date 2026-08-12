package algorithms;

import interfaces.Algorithm;
import models.ServiceRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic Programming Optimizer — classic 0/1 Knapsack applied to
 * request selection under a resource/time budget.
 *
 * Time Complexity : O(n * budget)
 * Space Complexity: O(n * budget)
 */
public class DynamicProgrammingOptimizer implements Algorithm {

    @Override
    public void execute() {
        // Demo / test run when called from the console menu
        System.out.println("Running Dynamic Programming Optimizer...");
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 8};
        int capacity = 10;
        int result = knapsack(weights, values, capacity);
        System.out.println("Max value: " + result);
    }

    /**
     * Required method signature from the spec.
     * Calls the internal DP solver and returns the max value.
     */
    public static int knapsack(int[] weights, int[] values, int capacity) {
        // Convert arrays to internal Request objects
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            requests.add(new Request("REQ-" + i, weights[i], values[i]));
        }
        OptimizationResult result = solve(requests, capacity);
        return result.maxValue;
    }

    // ---- Internal DP Logic (Opoku's code, adapted) ----

    private static class Request {
        public final String requestId;
        public final int cost;
        public final int value;

        public Request(String requestId, int cost, int value) {
            this.requestId = requestId;
            this.cost = cost;
            this.value = value;
        }
    }

    private static class OptimizationResult {
        public final int maxValue;
        public final List<Request> selectedRequests;

        public OptimizationResult(int maxValue, List<Request> selectedRequests) {
            this.maxValue = maxValue;
            this.selectedRequests = selectedRequests;
        }
    }

    public static OptimizationResult solve(List<Request> requests, int budget) {
        int requestCount = requests.size();
        int[][] dpTable = new int[requestCount + 1][budget + 1];

        System.out.println("\n--- Dynamic Programming Optimizer: DP Table Construction ---");

        for (int itemIndex = 1; itemIndex <= requestCount; itemIndex++) {
            Request currentRequest = requests.get(itemIndex - 1);
            for (int capacityIndex = 0; capacityIndex <= budget; capacityIndex++) {
                int valueWithoutRequest = dpTable[itemIndex - 1][capacityIndex];

                if (currentRequest.cost <= capacityIndex) {
                    int valueWithRequest = currentRequest.value
                            + dpTable[itemIndex - 1][capacityIndex - currentRequest.cost];
                    dpTable[itemIndex][capacityIndex] = Math.max(valueWithoutRequest, valueWithRequest);
                } else {
                    dpTable[itemIndex][capacityIndex] = valueWithoutRequest;
                }
            }
        }

        printDpTable(dpTable, requests, budget);

        List<Request> selectedRequests = reconstructSelection(dpTable, requests, budget);
        int maxValue = dpTable[requestCount][budget];

        return new OptimizationResult(maxValue, selectedRequests);
    }

    private static List<Request> reconstructSelection(int[][] dpTable, List<Request> requests, int budget) {
        System.out.println("\n--- Reconstruction Trace (backtracking selected requests) ---");
        System.out.printf("%-6s %-10s %-10s %-10s %-12s%n",
                "Step", "Request", "dp[i][w]", "dp[i-1][w]", "Decision");

        List<Request> selectedRequests = new ArrayList<>();
        int remainingBudget = budget;
        int stepCount = 0;

        for (int itemIndex = requests.size(); itemIndex >= 1; itemIndex--) {
            stepCount++;
            Request currentRequest = requests.get(itemIndex - 1);
            int currentValue = dpTable[itemIndex][remainingBudget];
            int valueWithoutItem = dpTable[itemIndex - 1][remainingBudget];

            if (currentValue != valueWithoutItem) {
                selectedRequests.add(currentRequest);
                remainingBudget -= currentRequest.cost;
                System.out.printf("%-6d %-10s %-10d %-10d %-12s%n",
                        stepCount, currentRequest.requestId, currentValue, valueWithoutItem, "INCLUDE");
            } else {
                System.out.printf("%-6d %-10s %-10d %-10d %-12s%n",
                        stepCount, currentRequest.requestId, currentValue, valueWithoutItem, "skip");
            }
        }
        return selectedRequests;
    }

    private static void printDpTable(int[][] dpTable, List<Request> requests, int budget) {
        System.out.printf("%-10s", "Req\\Cap");
        for (int c = 0; c <= budget; c++) {
            System.out.printf("%-4d", c);
        }
        System.out.println();

        System.out.printf("%-10s", "(none)");
        for (int c = 0; c <= budget; c++) {
            System.out.printf("%-4d", dpTable[0][c]);
        }
        System.out.println();

        for (int i = 1; i <= requests.size(); i++) {
            System.out.printf("%-10s", requests.get(i - 1).requestId);
            for (int c = 0; c <= budget; c++) {
                System.out.printf("%-4d", dpTable[i][c]);
            }
            System.out.println();
        }
    }
}