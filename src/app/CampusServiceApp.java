package app;

import database.DatabaseConnection;
import database.DatabaseManager;
import database.LocationRepository;
import database.RoadRepository;
import database.ServiceRequestRepository;
import database.ResourceRepository;
import models.Location;
import models.Road;
import models.ServiceRequest;
import models.Resource;
import utils.CSVLoader;
import datastructures.Graph;
import algorithms.DijkstraAlgorithm;
import algorithms.GreedyScheduler;
import datastructures.DynamicArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Campus service dispatch app.
 *
 * This version:
 * - keeps GreedyScheduler unchanged
 * - normalizes category/resource labels so the old scheduler still works
 * - shows locations, resources, and route names
 * - displays submitted time, deadline, and execution/travel time
 * - supports batch approve/reject and individual review
 */
public class CampusServiceApp {

    private static DatabaseConnection dbConnection;
    private static LocationRepository locationRepository;
    private static RoadRepository roadRepository;
    private static ServiceRequestRepository requestRepository;
    private static ResourceRepository resourceRepository;
    private static Graph<String> campusGraph;

    private static final int DISPATCH_BATCH_SIZE = 5;

    public static void main(String[] args) {
        System.out.println("=== University Campus Service Hub ===");

        setUpDatabase();
        loadAndPersistData();
        buildCampusGraph();

        Scanner scanner = new Scanner(System.in);
        runMenu(scanner);

        if (dbConnection != null) {
            dbConnection.close();
        }
        System.out.println("Goodbye.");
    }

    // --- Startup steps ---

    private static void setUpDatabase() {
        System.out.println("Connecting to database...");
        dbConnection = new DatabaseConnection();
        DatabaseManager dbManager = new DatabaseManager(dbConnection);
        dbManager.initialize();

        locationRepository = new LocationRepository(dbConnection);
        roadRepository = new RoadRepository(dbConnection);
        requestRepository = new ServiceRequestRepository(dbConnection);
        resourceRepository = new ResourceRepository(dbConnection);
        System.out.println("Database ready.");
    }

    private static void loadAndPersistData() {
        System.out.println("Loading CSV data...");
        CSVLoader loader = new CSVLoader();

        List<Location> locations = loader.loadLocations("data/locations.csv");
        for (Location location : locations) {
            locationRepository.save(location);
        }
        System.out.println("  Locations loaded: " + locations.size());

        List<Road> roads = loader.loadRoads("data/roads.csv");
        for (Road road : roads) {
            roadRepository.save(road);
        }
        System.out.println("  Roads loaded: " + roads.size());

        List<ServiceRequest> requests = loader.loadServiceRequests("data/service_requests.csv");
        for (ServiceRequest request : requests) {
            requestRepository.save(request);
        }
        System.out.println("  Service requests loaded: " + requests.size());

        List<Resource> resources = loader.loadResources("data/resources.csv");
        for (Resource resource : resources) {
            resourceRepository.save(resource);
        }
        System.out.println("  Resources loaded: " + resources.size());
    }

    private static void buildCampusGraph() {
        System.out.println("Building campus road network...");
        campusGraph = new Graph<>();

        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            int weight = (int) Math.round(road.getTravelTimeMin());
            if (weight < 1) {
                weight = 1;
            }
            campusGraph.addEdge(road.getFromLocationId(), road.getToLocationId(), weight);
        }
        System.out.println("  Graph built: " + campusGraph.size() + " locations connected.");
    }

    // --- Request timing calculation ---

    private static int calculateExecutionMinutes(ServiceRequest request) {
        if (request == null || request.getSourceLocationId() == null || request.getDestinationLocationId() == null) {
            return -1;
        }

        String fromId = request.getSourceLocationId();
        String toId = request.getDestinationLocationId();

        if (campusGraph == null) {
            buildCampusGraph();
        }

        if (campusGraph.indexOfVertex(fromId) == -1 || campusGraph.indexOfVertex(toId) == -1) {
            return -1;
        }

        DijkstraAlgorithm<String> dijkstra = new DijkstraAlgorithm<>();
        dijkstra.shortestPath(campusGraph, fromId);
        int minutes = dijkstra.getDistance(toId);

        return minutes == Integer.MAX_VALUE || minutes < 0 ? -1 : minutes;
    }

    private static String formatRequestTiming(ServiceRequest request) {
        String fromName = getLocationDisplayName(request.getSourceLocationId());
        String toName = getLocationDisplayName(request.getDestinationLocationId());
        int travelMinutes = calculateExecutionMinutes(request);

        if (travelMinutes < 0) {
            return fromName + " -> " + toName + " (route time unavailable)";
        }

        return fromName + " -> " + toName + " (" + travelMinutes + " min)";
    }

    // --- Normalization (keeps GreedyScheduler untouched) ---

    /**
     * Maps the real category text onto the canonical value recognized by
     * GreedyScheduler.isCompatible().
     */
    private static String canonicalCategory(String realCategory) {
        if (realCategory == null) {
            return null;
        }
        String upper = realCategory.toUpperCase();
        if (upper.contains("MEDICAL")) {
            return "Medical";
        }
        if (upper.contains("SHUTTLE")) {
            return "Shuttle";
        }
        if (upper.contains("MAINTENANCE")) {
            return "Maintenance";
        }
        return null;
    }

    /**
     * Maps real resource labels onto the canonical "Van"/"Rider" form
     * used by GreedyScheduler.isCompatible().
     */
    private static String canonicalResourceType(String realType) {
        if (realType == null) {
            return null;
        }
        switch (realType) {
            case "Van":
            case "Campus Van":
            case "Pickup":
            case "Utility Van":
            case "Delivery Van":
            case "Ambulance":
            case "Service Van":
            case "Security Truck":
            case "Maintenance Truck":
            case "Electric Vehicle":
                return "Van";
            case "Motorbike":
            case "Bike":
            case "Rider":
                return "Rider";
            default:
                return null;
        }
    }

    /** Human-readable label for what a category needs using the real category text. */
    private static String compatibleResourceLabel(String realCategory) {
        String canonical = canonicalCategory(realCategory);
        if ("Medical".equals(canonical)) {
            return "Van-class resource (Van, Ambulance, Pickup, etc.)";
        }
        if ("Maintenance".equals(canonical)) {
            return "Rider-class resource (Motorbike, Bike)";
        }
        if ("Shuttle".equals(canonical)) {
            return "Van-class or Rider-class resource";
        }
        return "compatible resource (unrecognized category)";
    }

    private static DynamicArray<ServiceRequest> buildNormalizedRequests(
            DynamicArray<ServiceRequest> realRequests, Map<String, ServiceRequest> realRequestsById) {

        DynamicArray<ServiceRequest> normalized = new DynamicArray<>();
        for (int i = 0; i < realRequests.size(); i++) {
            ServiceRequest real = realRequests.get(i);
            realRequestsById.put(real.getRequestId(), real);

            ServiceRequest copy = new ServiceRequest(
                    real.getRequestId(),
                    real.getSourceLocationId(),
                    real.getDestinationLocationId(),
                    canonicalCategory(real.getCategory()),
                    real.getUrgency(),
                    real.getTimeSubmitted(),
                    real.getDeadline(),
                    real.getStatus()
            );
            normalized.add(copy);
        }
        return normalized;
    }

    private static DynamicArray<Resource> buildNormalizedResources(
            DynamicArray<Resource> realResources, Map<String, Resource> realResourcesById) {

        DynamicArray<Resource> normalized = new DynamicArray<>();
        for (int i = 0; i < realResources.size(); i++) {
            Resource real = realResources.get(i);
            realResourcesById.put(real.getResourceId(), real);

            Resource copy = new Resource(
                    real.getResourceId(),
                    canonicalResourceType(real.getResourceType()),
                    real.getHomeLocationId(),
                    real.getCapacity(),
                    real.getAvailabilityStatus()
            );
            normalized.add(copy);
        }
        return normalized;
    }

    private static class RealAssignment {
        final ServiceRequest request;
        final Resource resource;

        RealAssignment(ServiceRequest request, Resource resource) {
            this.request = request;
            this.resource = resource;
        }
    }

    private static DynamicArray<RealAssignment> toRealAssignments(
            DynamicArray<GreedyScheduler.Assignment> normalizedAssignments,
            Map<String, ServiceRequest> realRequestsById,
            Map<String, Resource> realResourcesById) {

        DynamicArray<RealAssignment> result = new DynamicArray<>();
        for (int i = 0; i < normalizedAssignments.size(); i++) {
            GreedyScheduler.Assignment a = normalizedAssignments.get(i);
            ServiceRequest realRequest = realRequestsById.get(a.request.getRequestId());
            Resource realResource = (a.resource != null) ? realResourcesById.get(a.resource.getResourceId()) : null;
            result.add(new RealAssignment(realRequest, realResource));
        }
        return result;
    }

    // --- Auto-dispatch on startup ---

    private static void runAutoDispatch(Scanner scanner) {
        System.out.println("\n=== Automated Dispatch ===");

        DynamicArray<ServiceRequest> pending = loadPendingRequests();
        if (pending.isEmpty()) {
            System.out.println("No pending requests at startup.");
            return;
        }

        System.out.println(pending.size() + " pending requests found. Processing in batches of "
                + DISPATCH_BATCH_SIZE + ".");

        runDispatchLoop(pending, scanner);
        System.out.println("\nAutomated dispatch complete.");
    }

    // --- Menu ---

    private static void runMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. View Locations");
            System.out.println("2. View Service Requests");
            System.out.println("3. View Resources");
            System.out.println("4. Find Route (by location ID or name)");
            System.out.println("5. Dispatch pending requests");
            System.out.println("6. Run startup auto-dispatch");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewLocations();
                    break;
                case "2":
                    viewServiceRequests();
                    break;
                case "3":
                    viewResources();
                    break;
                case "4":
                    findRoute(scanner);
                    break;
                case "5":
                    dispatch(scanner);
                    break;
                case "6":
                    runAutoDispatch(scanner);
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void viewLocations() {
        List<Location> locations = locationRepository.findAll();
        System.out.println("\n" + locations.size() + " locations:");
        for (Location location : locations) {
            System.out.println("  " + location.getLocationId() + " - " + location.getName()
                    + " (" + location.getLocationType() + ")");
        }
    }

    private static void viewServiceRequests() {
        List<ServiceRequest> requests = requestRepository.findAll();
        System.out.println("\n" + requests.size() + " service requests:");
        for (ServiceRequest request : requests) {
            int execMinutes = calculateExecutionMinutes(request);
            String timingText = (execMinutes >= 0)
                    ? " | execution time: " + execMinutes + " min"
                    : " | execution time: unavailable";

            System.out.println("  " + request.getRequestId() + " - " + request.getCategory()
                    + " (urgency " + request.getUrgency()
                    + ", status " + request.getStatus()
                    + ", from " + getLocationDisplayName(request.getSourceLocationId())
                    + " -> " + getLocationDisplayName(request.getDestinationLocationId())
                    + ", submitted: " + request.getTimeSubmitted()
                    + ", deadline: " + request.getDeadline()
                    + timingText + ")");
        }
    }

    private static void viewResources() {
        List<Resource> resources = resourceRepository.findAll();
        System.out.println("\n" + resources.size() + " resources:");
        for (Resource resource : resources) {
            System.out.println("  " + resource.getResourceId() + " - " + resource.getResourceType()
                    + " (" + resource.getAvailabilityStatus() + ")");
        }
    }

    private static void findRoute(Scanner scanner) {
        System.out.print("From location ID or name: ");
        String fromInput = scanner.nextLine().trim();
        System.out.print("To location ID or name: ");
        String toInput = scanner.nextLine().trim();

        String from = resolveLocationKey(fromInput);
        String to = resolveLocationKey(toInput);

        if (campusGraph.indexOfVertex(from) == -1 || campusGraph.indexOfVertex(to) == -1) {
            System.out.println("One or both locations were not found.");
            return;
        }

        DijkstraAlgorithm<String> dijkstra = new DijkstraAlgorithm<>();
        dijkstra.shortestPath(campusGraph, from);

        int distance = dijkstra.getDistance(to);
        if (distance == -1 || distance == Integer.MAX_VALUE) {
            System.out.println("No route found between " + getLocationDisplayName(from)
                    + " and " + getLocationDisplayName(to) + ".");
            return;
        }

        int[] pathIndices = dijkstra.getPath(to);
        System.out.print("Route (" + distance + " min): ");

        for (int i = 0; i < pathIndices.length; i++) {
            String vertexId = campusGraph.get(pathIndices[i]);
            String name = getLocationDisplayName(vertexId);

            System.out.print(vertexId + " (" + name + ")");
            if (i < pathIndices.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    // --- Dispatch ---

    private static DynamicArray<ServiceRequest> loadPendingRequests() {
        List<ServiceRequest> allRequests = requestRepository.findAll();
        DynamicArray<ServiceRequest> pending = new DynamicArray<>();
        for (ServiceRequest request : allRequests) {
            if ("PENDING".equalsIgnoreCase(request.getStatus())) {
                pending.add(request);
            }
        }
        return pending;
    }

    private static void dispatch(Scanner scanner) {
        DynamicArray<ServiceRequest> pending = loadPendingRequests();
        if (pending.isEmpty()) {
            System.out.println("No pending requests to dispatch.");
            return;
        }
        runDispatchLoop(pending, scanner);
        System.out.println("\nDispatch complete.");
    }

    private static void runDispatchLoop(DynamicArray<ServiceRequest> pending, Scanner scanner) {
        int batchStart = 0;
        int batchNumber = 1;

        while (batchStart < pending.size()) {
            int batchEnd = Math.min(batchStart + DISPATCH_BATCH_SIZE, pending.size());

            DynamicArray<ServiceRequest> batch = new DynamicArray<>();
            for (int i = batchStart; i < batchEnd; i++) {
                batch.add(pending.get(i));
            }

            List<Resource> allResources = resourceRepository.findAll();
            DynamicArray<Resource> resourcePool = new DynamicArray<>();
            for (Resource resource : allResources) {
                resourcePool.add(resource);
            }

            Map<String, ServiceRequest> realRequestsById = new HashMap<>();
            Map<String, Resource> realResourcesById = new HashMap<>();
            DynamicArray<ServiceRequest> normalizedBatch = buildNormalizedRequests(batch, realRequestsById);
            DynamicArray<Resource> normalizedPool = buildNormalizedResources(resourcePool, realResourcesById);

            System.out.println("\n--- Batch " + batchNumber + " (" + batch.size() + " requests) ---");
            GreedyScheduler scheduler = new GreedyScheduler();
            DynamicArray<GreedyScheduler.Assignment> normalizedAssignments =
                    scheduler.assign(normalizedBatch, normalizedPool);

            DynamicArray<RealAssignment> assignments =
                    toRealAssignments(normalizedAssignments, realRequestsById, realResourcesById);

            printProposedPlan(assignments, resourcePool);

            System.out.println("\nBatch options:");
            System.out.println("  A - approve all");
            System.out.println("  R - reject all");
            System.out.println("  I - approve/reject individually");
            System.out.println("  S - skip this batch");
            System.out.print("Choose action: ");
            String answer = scanner.nextLine().trim().toLowerCase();

            int approvedThisBatch = 0;
            int rejectedThisBatch = 0;

            switch (answer) {
                case "a":
                    approvedThisBatch = approveAllBatch(assignments);
                    rejectedThisBatch = countAlreadyUnassigned(assignments);
                    break;
                case "r":
                    rejectedThisBatch = rejectAllBatch(assignments);
                    break;
                case "i":
                    approvedThisBatch = processIndividualReview(assignments, scanner, resourcePool);
                    rejectedThisBatch = assignments.size() - approvedThisBatch;
                    break;
                case "s":
                    System.out.println("Batch " + batchNumber + " skipped, no changes saved.");
                    break;
                default:
                    System.out.println("Invalid option. Batch skipped.");
            }

            printBatchSummary(batchNumber, approvedThisBatch, rejectedThisBatch);
            batchStart = batchEnd;
            batchNumber++;
        }
    }

    private static void printProposedPlan(DynamicArray<RealAssignment> assignments,
                                          DynamicArray<Resource> resourcePool) {
        System.out.println("\nProposed plan:");
        for (int i = 0; i < assignments.size(); i++) {
            RealAssignment a = assignments.get(i);
            String reason = explainAssignment(a, resourcePool);

            String fromName = getLocationDisplayName(a.request.getSourceLocationId());
            String toName = getLocationDisplayName(a.request.getDestinationLocationId());
            int executionMinutes = calculateExecutionMinutes(a.request);

            if (a.resource != null) {
                System.out.println("  " + a.request.getRequestId()
                        + " | " + a.request.getCategory()
                        + " | urgency " + a.request.getUrgency()
                        + " | from " + fromName + " -> to " + toName
                        + " | submitted: " + a.request.getTimeSubmitted()
                        + " | deadline: " + a.request.getDeadline()
                        + " | execution time: " + ((executionMinutes >= 0) ? executionMinutes + " min" : "unavailable")
                        + " | assigned: " + a.resource.getResourceId() + " (" + a.resource.getResourceType() + ")"
                        + " | reason: " + reason);
            } else {
                System.out.println("  " + a.request.getRequestId()
                        + " | " + a.request.getCategory()
                        + " | urgency " + a.request.getUrgency()
                        + " | from " + fromName + " -> to " + toName
                        + " | submitted: " + a.request.getTimeSubmitted()
                        + " | deadline: " + a.request.getDeadline()
                        + " | execution time: " + ((executionMinutes >= 0) ? executionMinutes + " min" : "unavailable")
                        + " | unassigned"
                        + " | reason: " + reason);
            }
        }
    }

    private static String explainAssignment(RealAssignment assignment, DynamicArray<Resource> resourcePool) {
        if (assignment.resource == null) {
            if (hasCompatibleAvailableResource(assignment.request, resourcePool)) {
                return "higher-priority requests consumed the remaining compatible resources";
            }
            return "no compatible AVAILABLE " + compatibleResourceLabel(assignment.request.getCategory())
                    + " remained in the current pool";
        }

        String category = assignment.request.getCategory();
        String resourceId = assignment.resource.getResourceId();
        String resourceType = assignment.resource.getResourceType();
        String canonical = canonicalCategory(category);

        if ("Medical".equals(canonical)) {
            return category + " requires a Van-class resource, and " + resourceId
                    + " (" + resourceType + ") was the first compatible AVAILABLE match";
        } else if ("Maintenance".equals(canonical)) {
            return category + " requires a Rider-class resource, and " + resourceId
                    + " (" + resourceType + ") was the first compatible AVAILABLE match";
        } else if ("Shuttle".equals(canonical)) {
            return category + " can use a Van-class or Rider-class resource, and " + resourceId
                    + " (" + resourceType + ") was the first compatible AVAILABLE option";
        }
        return "matched the first compatible AVAILABLE resource in the pool";
    }

    /** Checks the REAL resource pool for anything still AVAILABLE and compatible with this request. */
    private static boolean hasCompatibleAvailableResource(ServiceRequest request, DynamicArray<Resource> resourcePool) {
        String neededCategory = canonicalCategory(request.getCategory());
        for (int i = 0; i < resourcePool.size(); i++) {
            Resource resource = resourcePool.get(i);
            String resourceClass = canonicalResourceType(resource.getResourceType());
            if ("AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())
                    && isCompatibleClass(neededCategory, resourceClass)) {
                return true;
            }
        }
        return false;
    }

    /** Same rule as GreedyScheduler.isCompatible(), applied to already-canonical values. */
    private static boolean isCompatibleClass(String canonicalCategory, String canonicalResourceClass) {
        if (canonicalCategory == null || canonicalResourceClass == null) {
            return false;
        }
        switch (canonicalCategory) {
            case "Medical":
                return "Van".equals(canonicalResourceClass);
            case "Shuttle":
                return "Van".equals(canonicalResourceClass) || "Rider".equals(canonicalResourceClass);
            case "Maintenance":
                return "Rider".equals(canonicalResourceClass);
            default:
                return false;
        }
    }

    private static int approveAllBatch(DynamicArray<RealAssignment> assignments) {
        int approved = 0;
        for (int i = 0; i < assignments.size(); i++) {
            RealAssignment assignment = assignments.get(i);
            if (assignment.resource != null) {
                assignment.request.setStatus("ASSIGNED");
                requestRepository.save(assignment.request);

                assignment.resource.setAvailabilityStatus("ASSIGNED");
                resourceRepository.save(assignment.resource);

                approved++;
            } else {
                assignment.request.setStatus("REJECTED");
                requestRepository.save(assignment.request);
            }
        }
        System.out.println("Batch approved and saved.");
        return approved;
    }

    private static int rejectAllBatch(DynamicArray<RealAssignment> assignments) {
        int rejected = 0;
        for (int i = 0; i < assignments.size(); i++) {
            RealAssignment assignment = assignments.get(i);
            assignment.request.setStatus("REJECTED");
            requestRepository.save(assignment.request);
            rejected++;
        }
        System.out.println("Batch rejected and recorded.");
        return rejected;
    }

    private static int countAlreadyUnassigned(DynamicArray<RealAssignment> assignments) {
        int count = 0;
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).resource == null) {
                count++;
            }
        }
        return count;
    }

    private static int processIndividualReview(DynamicArray<RealAssignment> assignments, Scanner scanner,
                                             DynamicArray<Resource> resourcePool) {
        System.out.println("\n--- Individual review ---");
        int approved = 0;

        for (int i = 0; i < assignments.size(); i++) {
            RealAssignment assignment = assignments.get(i);

            if (assignment.resource == null) {
                System.out.println("  " + assignment.request.getRequestId()
                        + " | " + assignment.request.getCategory()
                        + " | urgency: " + assignment.request.getUrgency()
                        + " | execution time: " + ((calculateExecutionMinutes(assignment.request) >= 0)
                            ? calculateExecutionMinutes(assignment.request) + " min" : "unavailable")
                        + " | UNASSIGNED"
                        + " | reason: " + explainAssignment(assignment, resourcePool));

                assignment.request.setStatus("REJECTED");
                requestRepository.save(assignment.request);
                continue;
            }

            System.out.println("\n  Request: " + assignment.request.getRequestId());
            System.out.println("  Category: " + assignment.request.getCategory());
            System.out.println("  Urgency: " + assignment.request.getUrgency());
            System.out.println("  Route: " + getLocationDisplayName(assignment.request.getSourceLocationId())
                    + " -> " + getLocationDisplayName(assignment.request.getDestinationLocationId()));
            System.out.println("  Estimated execution time: " + ((calculateExecutionMinutes(assignment.request) >= 0)
                    ? calculateExecutionMinutes(assignment.request) + " min" : "unavailable"));
            System.out.println("  Proposed resource: " + assignment.resource.getResourceId()
                    + " (" + assignment.resource.getResourceType() + ")");
            System.out.println("  Reason: " + explainAssignment(assignment, resourcePool));

            System.out.print("Approve this assignment? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if ("y".equals(response)) {
                assignment.request.setStatus("ASSIGNED");
                requestRepository.save(assignment.request);

                assignment.resource.setAvailabilityStatus("ASSIGNED");
                resourceRepository.save(assignment.resource);

                approved++;
                System.out.println("  Approved.");
            } else {
                assignment.request.setStatus("REJECTED");
                requestRepository.save(assignment.request);
                System.out.println("  Rejected.");
            }
        }

        System.out.println("Individual review complete.");
        return approved;
    }

    private static void printBatchSummary(int batchNumber, int approved, int rejected) {
        int pendingRemaining = countPendingRequests();
        int availableResourcesLeft = countAvailableResources();

        System.out.println("\n--- Batch " + batchNumber + " summary ---");
        System.out.println("Approved: " + approved);
        System.out.println("Rejected: " + rejected);
        System.out.println("Pending requests remaining: " + pendingRemaining);
        System.out.println("Available resources left: " + availableResourcesLeft);
    }

    private static int countPendingRequests() {
        List<ServiceRequest> all = requestRepository.findAll();
        int pending = 0;
        for (ServiceRequest request : all) {
            if ("PENDING".equalsIgnoreCase(request.getStatus())) {
                pending++;
            }
        }
        return pending;
    }

    private static int countAvailableResources() {
        List<Resource> all = resourceRepository.findAll();
        int count = 0;
        for (Resource resource : all) {
            if ("AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())) {
                count++;
            }
        }
        return count;
    }

    private static String resolveLocationKey(String input) {
        if (input == null) {
            return "";
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return trimmed;
        }
        if (campusGraph.indexOfVertex(trimmed) != -1) {
            return trimmed;
        }
        List<Location> allLocations = locationRepository.findAll();
        for (Location location : allLocations) {
            if (location.getName().equalsIgnoreCase(trimmed) || location.getLocationId().equalsIgnoreCase(trimmed)) {
                return location.getLocationId();
            }
        }
        return trimmed;
    }

    private static String getLocationDisplayName(String locationId) {
        Location location = locationRepository.findById(locationId);
        if (location != null) {
            return location.getName();
        }
        return locationId;
    }
}