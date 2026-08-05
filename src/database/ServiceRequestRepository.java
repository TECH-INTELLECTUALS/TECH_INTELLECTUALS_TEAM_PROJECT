package database;

import models.ServiceRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for ServiceRequest persistence operations.
 */
public class ServiceRequestRepository {

    private final DatabaseConnection databaseConnection;

    public ServiceRequestRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Saves a service request. Inserts if the ID doesn't exist,
     * otherwise updates the existing row.
     *
     * @param request service request entity
     */
    public void save(ServiceRequest request) {
        String sql = """
            INSERT INTO service_requests (
                request_id, source_location_id, destination_location_id,
                category, urgency, time_submitted, deadline, status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(request_id) DO UPDATE SET
                source_location_id = excluded.source_location_id,
                destination_location_id = excluded.destination_location_id,
                category = excluded.category,
                urgency = excluded.urgency,
                time_submitted = excluded.time_submitted,
                deadline = excluded.deadline,
                status = excluded.status;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getRequestId());
            stmt.setInt(2, request.getSourceLocationId());
            stmt.setInt(3, request.getDestinationLocationId());
            stmt.setString(4, request.getCategory());
            stmt.setString(5, request.getUrgency());
            stmt.setString(6, request.getTimeSubmitted());
            stmt.setString(7, request.getDeadline());
            stmt.setString(8, request.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save service request: " + request.getRequestId(), e);
        }
    }

    /**
     * Finds a service request by its ID.
     */
    public ServiceRequest findById(int requestId) {
        String sql = "SELECT * FROM service_requests WHERE request_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find service request: " + requestId, e);
        }
    }

    /**
     * Retrieves all service requests.
     */
    public List<ServiceRequest> findAll() {
        String sql = "SELECT * FROM service_requests;";
        List<ServiceRequest> requests = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve service requests", e);
        }

        return requests;
    }

    /**
     * Retrieves all service requests with a given status
     * (e.g. "PENDING", "IN_PROGRESS", "COMPLETED").
     */
    public List<ServiceRequest> findByStatus(String status) {
        String sql = "SELECT * FROM service_requests WHERE status = ?;";
        List<ServiceRequest> requests = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find service requests by status: " + status, e);
        }

        return requests;
    }

    /**
     * Retrieves all service requests with a given urgency level
     * (e.g. "LOW", "MEDIUM", "HIGH", "CRITICAL").
     */
    public List<ServiceRequest> findByUrgency(String urgency) {
        String sql = "SELECT * FROM service_requests WHERE urgency = ?;";
        List<ServiceRequest> requests = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urgency);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find service requests by urgency: " + urgency, e);
        }

        return requests;
    }

    /**
     * Deletes a service request by its ID.
     */
    public void delete(int requestId) {
        String sql = "DELETE FROM service_requests WHERE request_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete service request: " + requestId, e);
        }
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
                rs.getInt("request_id"),
                rs.getInt("source_location_id"),
                rs.getInt("destination_location_id"),
                rs.getString("category"),
                rs.getString("urgency"),
                rs.getString("time_submitted"),
                rs.getString("deadline"),
                rs.getString("status")
        );
    }
}