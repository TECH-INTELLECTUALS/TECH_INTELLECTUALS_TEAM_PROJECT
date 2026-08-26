package tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import algorithms.GreedyScheduler;
import algorithms.DynamicProgrammingOptimizer;
import models.ServiceRequest;
import models.Resource;
import datastructures.DynamicArray;

/**
 * SchedulerAlgorithmsTest
 *
 * Lightweight, dependency-free test harness (no JUnit required).
 *
 * Updated to use the project's current models and DynamicArray API.
 */
public class SchedulerAlgorithmsTest {

    private static int totalTests = 0;
    private static int passedTests = 0;

    private static void check(String testName, boolean condition, String failureDetail) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName + " - " + failureDetail);
        }
    }

    // ==================== GreedyScheduler tests ====================

    /** Test 1: a request with exactly one compatible, available resource gets assigned. */
    private static void testGreedyAssignsMatchingResource() {
        DynamicArray<Resource> pool = new DynamicArray<>();
        pool.add(new Resource("Van-1", "Van", "L001", 4, "AVAILABLE"));

        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest("Req-1", "S1", "D1", "Medical", 5, "ts", "dl", "NEW"));

        GreedyScheduler scheduler = new GreedyScheduler();
        DynamicArray<GreedyScheduler.Assignment> result = scheduler.assign(requests, pool);

        boolean assignedCorrectly = result.size() == 1
                && result.get(0).resource != null
                && result.get(0).resource.getResourceId().equals("Van-1");

        check("testGreedyAssignsMatchingResource", assignedCorrectly,
                "expected Req-1 assigned to Van-1, got " + describe(result));
    }

    /** Test 2: a request with NO compatible resource type in the pool is left unassigned. */
    private static void testGreedyLeavesUnassignedWhenNoCompatibleResource() {
        DynamicArray<Resource> pool = new DynamicArray<>();
        pool.add(new Resource("Rider-1", "Rider", "L001", 1, "AVAILABLE"));

        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest("Req-1", "S1", "D1", "Medical", 5, "ts", "dl", "NEW"));

        GreedyScheduler scheduler = new GreedyScheduler();
        DynamicArray<GreedyScheduler.Assignment> result = scheduler.assign(requests, pool);

        boolean correctlyUnassigned = result.size() == 1 && result.get(0).resource == null;

        check("testGreedyLeavesUnassignedWhenNoCompatibleResource", correctlyUnassigned,
                "expected Req-1 to be unassigned, got " + describe(result));
    }

    /** Test 3: with only one resource, the HIGHER urgency request wins regardless of input list order. */
    private static void testGreedyProcessesHigherUrgencyFirst() {
        DynamicArray<Resource> pool = new DynamicArray<>();
        pool.add(new Resource("Van-1", "Van", "L001", 4, "AVAILABLE"));

        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest("Req-Low", "S1", "D1", "Shuttle", 3, "ts", "dl", "NEW"));
        requests.add(new ServiceRequest("Req-High", "S2", "D2", "Shuttle", 9, "ts", "dl", "NEW"));

        GreedyScheduler scheduler = new GreedyScheduler();
        DynamicArray<GreedyScheduler.Assignment> result = scheduler.assign(requests, pool);

        boolean highUrgencyWon = false;
        boolean lowUrgencyLost = false;
        for (int i = 0; i < result.size(); i++) {
            GreedyScheduler.Assignment a = result.get(i);
            if (a.request.getRequestId().equals("Req-High") && a.resource != null) highUrgencyWon = true;
            if (a.request.getRequestId().equals("Req-Low") && a.resource == null) lowUrgencyLost = true;
        }

        check("testGreedyProcessesHigherUrgencyFirst", highUrgencyWon && lowUrgencyLost,
                "expected Req-High assigned and Req-Low unassigned, got " + describe(result));
    }

    /**
     * Test 4 (regression test for the documented counterexample).
     */
    private static void testGreedyCounterexampleRegression() {
        DynamicArray<Resource> pool = new DynamicArray<>();
        pool.add(new Resource("Van-1", "Van", "L001", 4, "AVAILABLE"));
        pool.add(new Resource("Rider-1", "Rider", "L001", 1, "AVAILABLE"));

        DynamicArray<ServiceRequest> requests = new DynamicArray<>();
        requests.add(new ServiceRequest("R-Shuttle", "S1", "D1", "Shuttle", 10, "ts", "dl", "NEW"));
        requests.add(new ServiceRequest("R-Medical", "S2", "D2", "Medical", 9, "ts", "dl", "NEW"));

        GreedyScheduler scheduler = new GreedyScheduler();
        DynamicArray<GreedyScheduler.Assignment> result = scheduler.assign(requests, pool);

        boolean shuttleGotVan = false;
        boolean medicalUnassigned = false;
        for (int i = 0; i < result.size(); i++) {
            GreedyScheduler.Assignment a = result.get(i);
            if (a.request.getRequestId().equals("R-Shuttle") && a.resource != null
                    && a.resource.getResourceId().equals("Van-1")) shuttleGotVan = true;
            if (a.request.getRequestId().equals("R-Medical") && a.resource == null) medicalUnassigned = true;
        }

        check("testGreedyCounterexampleRegression", shuttleGotVan && medicalUnassigned,
                "expected Shuttle takes Van-1 and Medical unassigned, got " + describe(result));
    }

    private static String describe(DynamicArray<GreedyScheduler.Assignment> result) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            GreedyScheduler.Assignment a = result.get(i);
            sb.append(a.request.getRequestId()).append("->")
              .append(a.resource != null ? a.resource.getResourceId() : "UNASSIGNED").append(" ");
        }
        return sb.toString().trim();
    }

    // ================ DynamicProgrammingOptimizer tests ================

    /** Test 1: known small case — check max value via public knapsack API. */
    private static void testDpBasicKnapsackMaxValueAndSelection() {
        int[] weights = {2, 3, 4, 5};
        int[] values =  {3, 4, 5, 8};
        int capacity = 10;

        int maxValue = DynamicProgrammingOptimizer.knapsack(weights, values, capacity);

        boolean correct = maxValue == 15;
        check("testDpBasicKnapsackMaxValueAndSelection", correct,
                "expected maxValue=15; got maxValue=" + maxValue);
    }

    /** Test 2: zero budget means nothing can be selected and value is 0. */
    private static void testDpZeroBudgetYieldsZeroValue() {
        int[] weights = {2};
        int[] values =  {3};
        int maxValue = DynamicProgrammingOptimizer.knapsack(weights, values, 0);

        boolean correct = maxValue == 0;
        check("testDpZeroBudgetYieldsZeroValue", correct,
                "expected maxValue=0; got maxValue=" + maxValue);
    }

    /** Test 3: a single item that exactly fits the budget must be selected in full. */
    private static void testDpSingleItemExactFit() {
        int[] weights = {5};
        int[] values =  {7};
        int maxValue = DynamicProgrammingOptimizer.knapsack(weights, values, 5);

        boolean correct = maxValue == 7;
        check("testDpSingleItemExactFit", correct,
                "expected maxValue=7; got maxValue=" + maxValue);
    }

    /** Test 4: 5-request / budget-15 example (verify max value). */
    private static void testDpFiveRequestBudgetFifteen() {
        int[] weights = {3,4,5,2,6};
        int[] values  = {4,5,6,3,9};
        int maxValue = DynamicProgrammingOptimizer.knapsack(weights, values, 15);

        boolean correct = maxValue == 21;
        check("testDpFiveRequestBudgetFifteen", correct,
                "expected maxValue=21; got maxValue=" + maxValue);
    }

    // ==================== Runner ====================

    public static void main(String[] args) {
        System.out.println("========== Running SchedulerAlgorithmsTest ==========");

        System.out.println("\n-- GreedyScheduler --");
        testGreedyAssignsMatchingResource();
        testGreedyLeavesUnassignedWhenNoCompatibleResource();
        testGreedyProcessesHigherUrgencyFirst();
        testGreedyCounterexampleRegression();

        System.out.println("\n-- DynamicProgrammingOptimizer --");
        testDpBasicKnapsackMaxValueAndSelection();
        testDpZeroBudgetYieldsZeroValue();
        testDpSingleItemExactFit();
        testDpFiveRequestBudgetFifteen();

        System.out.println("\n======================================================");
        System.out.println("SUMMARY: " + passedTests + " / " + totalTests + " tests passed");
        System.out.println("======================================================");

        if (passedTests != totalTests) {
            System.exit(1);
        }
    }
}