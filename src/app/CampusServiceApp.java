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

import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the University Campus Service Hub application.
 *
 * Startup sequence:
 *   1. Open the DB connection and create tables if they don't exist
 *   2. Load the 4 CSV files and persist their rows into the DB
 *   3. Build an in-memory weighted Graph of locations/roads for routing
 *   4. Launch the console menu
 */
public class CampusServiceApp {

    private static DatabaseConnection dbConnection;
    private static LocationRepository locationRepository;
    private static RoadRepository roadRepository;
    private static ServiceRequestRepository requestRepository;
    private static ResourceRepository resourceRepository;
    private static Graph<String> campusGraph;

    public static void main(String[] args) {
        System.out.println("=== University Campus Service Hub ===");

        setUpDatabase();
        loadAndPersistData();
        buildCampusGraph();
        runMenu();

        dbConnection.close();
        System.out.println("Goodbye.");
    }

    // --- Startup steps ---

    private static void setUpDatabase() {
        System.out.println("Connecting to database...");
        dbConnection = new DatabaseConnection();
        DatabaseManager dbManager = new DatabaseManager(dbConnection);
        dbManager.initialize(); // creates tables if they don't exist

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
            // Round travel time to the nearest whole minute; Graph requires
            // a positive integer weight. Minimum of 1 avoids a rejected
            // zero-weight edge for very short roads.
            int weight = (int) Math.round(road.getTravelTimeMin());
            if (weight < 1) {
                weight = 1;
            }
            campusGraph.addEdge(road.getFromLocationId(), road.getToLocationId(), weight);
        }
        System.out.println("  Graph built: " + campusGraph.size() + " locations connected.");
    }

    // --- Menu ---

    private static void runMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View Locations");
            System.out.println("2. View Service Requests");
            System.out.println("3. View Resources");
            System.out.println("4. Find Route (fastest path between two locations)");
            System.out.println("5. Dispatch (assign resources to pending requests)");
            System.out.println("6. Exit");
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
                    dispatch();
                    break;
                case "6":
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
            System.out.println("  " + request.getRequestId() + " - " + request.getCategory()
                    + " (urgency " + request.getUrgency() + ", status " + request.getStatus() + ")");
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
        System.out.print("From location ID: ");
        String from = scanner.nextLine().trim();
        System.out.print("To location ID: ");
        String to = scanner.nextLine().trim();

        if (campusGraph.indexOfVertex(from) == -1 || campusGraph.indexOfVertex(to) == -1) {
            System.out.println("One or both location IDs were not found.");
            return;
        }

        DijkstraAlgorithm<String> dijkstra = new DijkstraAlgorithm<>();
        dijkstra.shortestPath(campusGraph, from);

        int distance = dijkstra.getDistance(to);
        if (distance == -1 || distance == Integer.MAX_VALUE) {
            System.out.println("No route found between " + from + " and " + to + ".");
            return;
        }

        int[] pathIndices = dijkstra.getPath(to);
        System.out.print("Route (" + distance + " min): ");
        for (int i = 0; i < pathIndices.length; i++) {
            System.out.print(campusGraph.get(pathIndices[i]));
            if (i < pathIndices.length - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    private static void dispatch() {
        List<ServiceRequest> allRequests = requestRepository.findAll();
        List<Resource> allResources = resourceRepository.findAll();

        DynamicArray<ServiceRequest> pending = new DynamicArray<>();
        for (ServiceRequest request : allRequests) {
            if ("NEW".equals(request.getStatus())) {
                pending.add(request);
            }
        }

        DynamicArray<Resource> resourcePool = new DynamicArray<>();
        for (Resource resource : allResources) {
            resourcePool.add(resource);
        }

        if (pending.isEmpty()) {
            System.out.println("No pending requests to dispatch.");
            return;
        }

        GreedyScheduler scheduler = new GreedyScheduler();
        scheduler.assign(pending, resourcePool);
        System.out.println("Dispatch complete. (Note: this run does not persist assignment status back to the DB yet.)");
    }
}