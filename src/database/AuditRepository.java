package database;

import models.AuditEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for AuditEvent persistence operations.
 */
public class AuditRepository {

    private final DatabaseConnection databaseConnection;

    public AuditRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Saves an audit event. Inserts if the ID doesn't exist,
     * otherwise updates the existing row.
     *
     * @param event audit event entity
     */
    public void save(AuditEvent event) {
        String sql = """
            INSERT INTO audit_events (
                event_id, action, actor, timestamp, description
            )
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(event_id) DO UPDATE SET
                action = excluded.action,
                actor = excluded.actor,
                timestamp = excluded.timestamp,
                description = excluded.description;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event.getEventId());
            stmt.setString(2, event.getAction());
            stmt.setString(3, event.getActor());
            stmt.setString(4, event.getTimestamp());
            stmt.setString(5, event.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save audit event: " + event.getEventId(), e);
        }
    }

    /**
     * Finds an audit event by its ID.
     */
    public AuditEvent findById(int eventId) {
        String sql = "SELECT * FROM audit_events WHERE event_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find audit event: " + eventId, e);
        }
    }

    /**
     * Retrieves all audit events.
     */
    public List<AuditEvent> findAll() {
        String sql = "SELECT * FROM audit_events;";
        List<AuditEvent> events = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                events.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve audit events", e);
        }

        return events;
    }

    /**
     * Retrieves all audit events performed by a given actor.
     */
    public List<AuditEvent> findByActor(String actor) {
        String sql = "SELECT * FROM audit_events WHERE actor = ?;";
        List<AuditEvent> events = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find audit events by actor: " + actor, e);
        }

        return events;
    }

    /**
     * Retrieves all audit events of a given action type.
     */
    public List<AuditEvent> findByAction(String action) {
        String sql = "SELECT * FROM audit_events WHERE action = ?;";
        List<AuditEvent> events = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, action);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find audit events by action: " + action, e);
        }

        return events;
    }

    /**
     * Deletes an audit event by its ID.
     */
    public void delete(int eventId) {
        String sql = "DELETE FROM audit_events WHERE event_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete audit event: " + eventId, e);
        }
    }

    private AuditEvent mapRow(ResultSet rs) throws SQLException {
        return new AuditEvent(
                rs.getInt("event_id"),
                rs.getString("action"),
                rs.getString("actor"),
                rs.getString("timestamp"),
                rs.getString("description")
        );
    }
}