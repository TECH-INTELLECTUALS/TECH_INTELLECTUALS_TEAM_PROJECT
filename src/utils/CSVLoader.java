package utils;

import models.Location;
import models.Road;
import models.ServiceRequest;
import models.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Loads CSV data files and converts each row into the corresponding
 * domain model. Maps snake_case CSV headers to the camelCase fields
 * used by the Java models.
 */
public class CSVLoader {

    private static final String DELIMITER = ",";

    /**
     * Loads locations from a CSV file.
     * Expected columns: location_id,name,area,location_type,x_coord,y_coord
     *
     * @param path path to the CSV file
     * @return list of parsed Location objects
     */
    public List<Location> loadLocations(String path) {
        List<Location> locations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(DELIMITER, -1);

                Location location = new Location(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        fields[3].trim(),
                        Double.parseDouble(fields[4].trim()),
                        Double.parseDouble(fields[5].trim())
                );
                locations.add(location);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load locations from: " + path, e);
        }

        return locations;
    }

    /**
     * Loads roads from a CSV file.
     * Expected columns: road_id,from_location_id,to_location_id,distance_km,travel_time_min,condition_weight
     *
     * @param path path to the CSV file
     * @return list of parsed Road objects
     */
    public List<Road> loadRoads(String path) {
        List<Road> roads = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(DELIMITER, -1);

                Road road = new Road(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        Double.parseDouble(fields[3].trim()),
                        Double.parseDouble(fields[4].trim()),
                        Double.parseDouble(fields[5].trim())
                );
                roads.add(road);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load roads from: " + path, e);
        }

        return roads;
    }

    /**
     * Loads service requests from a CSV file.
     * Expected columns: request_id,source_location_id,destination_location_id,
     * category,urgency,time_submitted,deadline,status
     *
     * @param path path to the CSV file
     * @return list of parsed ServiceRequest objects
     */
    public List<ServiceRequest> loadServiceRequests(String path) {
        List<ServiceRequest> requests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(DELIMITER, -1);

                ServiceRequest request = new ServiceRequest(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        fields[3].trim(),
                        Integer.parseInt(fields[4].trim()),
                        fields[5].trim(),
                        fields[6].trim(),
                        fields[7].trim()
                );
                requests.add(request);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load service requests from: " + path, e);
        }

        return requests;
    }

    /**
     * Loads resources from a CSV file.
     * Expected columns: resource_id,resource_type,home_location_id,capacity,availability_status
     *
     * @param path path to the CSV file
     * @return list of parsed Resource objects
     */
    public List<Resource> loadResources(String path) {
        List<Resource> resources = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] fields = line.split(DELIMITER, -1);

                Resource resource = new Resource(
                        fields[0].trim(),
                        fields[1].trim(),
                        fields[2].trim(),
                        Integer.parseInt(fields[3].trim()),
                        fields[4].trim()
                );
                resources.add(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resources from: " + path, e);
        }

        return resources;
    }
}