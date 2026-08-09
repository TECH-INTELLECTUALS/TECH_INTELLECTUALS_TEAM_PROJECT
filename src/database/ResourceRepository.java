package database;

import models.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Resource persistence operations.
 */
public class ResourceRepository {

    private final DatabaseConnection databaseConnection;

    public ResourceRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Saves a resource. Inserts if the ID doesn't exist,
     * otherwise updates the existing row.
     *
     * @param resource resource entity
     */
    public void save(Resource resource) {
        String sql = """
            INSERT INTO resources (resource_id, resource_type, home_location_id, capacity, availability_status)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(resource_id) DO UPDATE SET
                resource_type = excluded.resource_type,
                home_location_id = excluded.home_location_id,
                capacity = excluded.capacity,
                availability_status = excluded.availability_status;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resource.getResourceId());
            stmt.setString(2, resource.getResourceType());
            stmt.setString(3, resource.getHomeLocationId());
            stmt.setInt(4, resource.getCapacity());
            stmt.setString(5, resource.getAvailabilityStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save resource: " + resource.getResourceId(), e);
        }
    }

    /**
     * Finds a resource by its ID.
     */
    public Resource findById(int resourceId) {
        String sql = "SELECT * FROM resources WHERE resource_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resourceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find resource: " + resourceId, e);
        }
    }

    /**
     * Retrieves all resources.
     */
    public List<Resource> findAll() {
        String sql = "SELECT * FROM resources;";
        List<Resource> resources = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve resources", e);
        }

        return resources;
    }

    /**
     * Retrieves all resources with a given availability status
     * (e.g. "AVAILABLE", "IN_USE", "UNDER_MAINTENANCE").
     */
    public List<Resource> findByAvailabilityStatus(String availabilityStatus) {
        String sql = "SELECT * FROM resources WHERE availability_status = ?;";
        List<Resource> resources = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, availabilityStatus);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find resources by status: " + availabilityStatus, e);
        }

        return resources;
    }

    /**
     * Deletes a resource by its ID.
     */
    public void delete(int resourceId) {
        String sql = "DELETE FROM resources WHERE resource_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resourceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete resource: " + resourceId, e);
        }
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        return new Resource(
                rs.getString("resource_id"),
                rs.getString("resource_type"),
                rs.getString("home_location_id"),
                rs.getInt("capacity"),
                rs.getString("availability_status")
        );
    }
}