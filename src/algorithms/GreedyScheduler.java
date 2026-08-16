package algorithms;

import interfaces.Algorithm;
import models.ServiceRequest;
import models.Resource;
import datastructures.DynamicArray;

/**
 * Greedy Scheduler.
 *
 * Strategy: sort pending requests by urgency (descending, using our own
 * sort — see sortByUrgencyDescending()). For each request, in urgency
 * order, assign the FIRST available resource whose type is compatible
 * with the request's category:
 *
 *   Medical     -> Van only
 *   Shuttle     -> Van or Rider
 *   Maintenance -> Rider only
 *
 * Time Complexity : O(n^2 + n * m)  (n requests, m resources — insertion
 *                    sort is O(n^2), acceptable at our dataset scale)
 * Space Complexity: O(n + m)
 *
 * IMPORTANT - Greedy is NOT globally optimal here. See the documented
 * counterexample in main(): because Shuttle requests can use either
 * resource type, a greedy "first available match" can consume a Van
 * that a later, more constrained Medical request strictly needs -
 * even though a Rider could have served the Shuttle request just as
 * well. The result: a resource sits idle and a request that COULD have
 * been served is left unassigned, even though total urgency served
 * would have been higher under a different (optimal) assignment.
 */
public class GreedyScheduler implements Algorithm {

    public static class Assignment {
        public final ServiceRequest request;
        public final Resource resource; // null if unassigned

        public Assignment(ServiceRequest request, Resource resource) {
            this.request = request;
            this.resource = resource;
        }
    }

    /** Returns true if a resource of this type is allowed to serve this category. */
    private static boolean isCompatible(String category, String resourceType) {
        if (category == null || resourceType == null) {
            return false;
        }
        switch (category) {
            case "Medical":
                return resourceType.equals("Van");
            case "Shuttle":
                return resourceType.equals("Van") || resourceType.equals("Rider");
            case "Maintenance":
                return resourceType.equals("Rider");
            default:
                return false;
        }
    }

    /**
     * Sorts requests by urgency descending. Simple insertion sort — kept
     * in-house rather than using java.util's built-in sort, consistent
     * with the rest of the project's structures/algorithms.
     */
    private static void sortByUrgencyDescending(DynamicArray<ServiceRequest> requests) {
        for (int i = 1; i < requests.size(); i++) {
            ServiceRequest key = requests.get(i);
            int j = i - 1;
            while (j >= 0 && requests.get(j).getUrgency() < key.getUrgency()) {
                requests.set(j + 1, requests.get(j));
                j--;
            }
            requests.set(j + 1, key);
        }
    }

    /**
     * Assigns resources to requests, highest urgency first.
     *
     * @param pendingRequests requests awaiting assignment
     * @param resourcePool    all resources (availability checked internally)
     * @return one Assignment per request, in the order they were processed
     */
    public DynamicArray<Assignment> assign(DynamicArray<ServiceRequest> pendingRequests,
                                            DynamicArray<Resource> resourcePool) {

        System.out.println("\n--- Greedy Scheduler Trace ---");
        System.out.printf("%-10s %-8s %-14s %-10s %-14s%n",
                "Request", "Urgency", "Category", "Assigned", "Status");

        DynamicArray<ServiceRequest> sortedByUrgency = new DynamicArray<>();
        for (int i = 0; i < pendingRequests.size(); i++) {
            sortedByUrgency.add(pendingRequests.get(i));
        }
        sortByUrgencyDescending(sortedByUrgency);

        DynamicArray<Assignment> assignments = new DynamicArray<>();

        for (int i = 0; i < sortedByUrgency.size(); i++) {
            ServiceRequest request = sortedByUrgency.get(i);
            Resource matchedResource = null;

            for (int j = 0; j < resourcePool.size(); j++) {
                Resource resource = resourcePool.get(j);
                if (resource.getAvailabilityStatus().equals("AVAILABLE")
                        && isCompatible(request.getCategory(), resource.getResourceType())) {
                    matchedResource = resource;
                    break; // first available matching resource, per spec
                }
            }

            if (matchedResource != null) {
                matchedResource.setAvailabilityStatus("ASSIGNED");
                assignments.add(new Assignment(request, matchedResource));
                System.out.printf("%-10s %-8d %-14s %-10s %-14s%n",
                        request.getRequestId(), request.getUrgency(), request.getCategory(),
                        matchedResource.getResourceId(), "ASSIGNED");
            } else {
                assignments.add(new Assignment(request, null));
                System.out.printf("%-10s %-8d %-14s %-10s %-14s%n",
                        request.getRequestId(), request.getUrgency(), request.getCategory(),
                        "-", "UNASSIGNED");
            }
        }
        return assignments;
    }

    @Override
    public void execute() {
        // Marker method required by Algorithm interface.
        // Real work happens in assign() above.
    }

    /**
     * Demonstration + REQUIRED DELIVERABLE: documented counterexample
     * showing greedy assignment producing a worse outcome than optimal.
     */
    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println(" Counterexample: Greedy vs Optimal Assignment");
        System.out.println("================================================");
        System.out.println("Resource pool (in this fixed order): Van-1 (Van), Rider-1 (Rider)");
        System.out.println("Requests:");
        System.out.println("  R-Shuttle : category=Shuttle, urgency=93  (can use Van or Rider)");
        System.out.println("  R-Medical : category=Medical, urgency=90  (can use Van only)");

        DynamicArray<Resource> resourcePool = new DynamicArray<>();
        resourcePool.add(new Resource("Van-1", "Van", "L001", 4, "AVAILABLE"));
        resourcePool.add(new Resource("Rider-1", "Rider", "L001", 1, "AVAILABLE"));

        DynamicArray<ServiceRequest> pendingRequests = new DynamicArray<>();
        pendingRequests.add(new ServiceRequest("R-Shuttle", "L010", "L020", "Shuttle", 93,
                "2026-08-10T08:00", "2026-08-10T09:00", "NEW"));
        pendingRequests.add(new ServiceRequest("R-Medical", "L015", "L003", "Medical", 90,
                "2026-08-10T08:05", "2026-08-10T08:30", "NEW"));

        GreedyScheduler scheduler = new GreedyScheduler();
        DynamicArray<Assignment> greedyResult = scheduler.assign(pendingRequests, resourcePool);

        int greedyUrgencyServed = 0;
        System.out.println("\nGreedy outcome:");
        for (int i = 0; i < greedyResult.size(); i++) {
            Assignment a = greedyResult.get(i);
            String outcome = a.resource != null ? "served by " + a.resource.getResourceId() : "NOT SERVED";
            System.out.println("  " + a.request.getRequestId() + " (urgency " + a.request.getUrgency() + ") -> " + outcome);
            if (a.resource != null) {
                greedyUrgencyServed += a.request.getUrgency();
            }
        }
        System.out.println("  Total urgency served by GREEDY = " + greedyUrgencyServed);

        System.out.println("\nOptimal outcome (assign Rider-1 -> R-Shuttle, Van-1 -> R-Medical):");
        int optimalUrgencyServed = 93 + 90; // both requests served
        System.out.println("  R-Shuttle (urgency 93) -> served by Rider-1");
        System.out.println("  R-Medical (urgency 90) -> served by Van-1");
        System.out.println("  Total urgency served by OPTIMAL = " + optimalUrgencyServed);

        System.out.println("\nConclusion:");
        System.out.println("  Greedy served only " + greedyUrgencyServed + " urgency units (the Medical");
        System.out.println("  request went unserved) because it let the flexible Shuttle request consume");
        System.out.println("  the only Van, even though a Rider could have served it just as well. The");
        System.out.println("  optimal assignment reserves the constrained resource (Van) for the request");
        System.out.println("  that has no alternative (Medical), serving " + optimalUrgencyServed + " units total.");
        System.out.println("  This is a classic greedy failure: local 'first available match' choice");
        System.out.println("  ignores downstream resource constraints, unlike interval/activity");
        System.out.println("  scheduling where earliest-finish-time greedy IS provably optimal.");
    }
}