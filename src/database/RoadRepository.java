package database;

import models.Road;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Road persistence operations.
 */
public class RoadRepository {

    private final DatabaseConnection databaseConnection;

    public RoadRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Saves a road. Inserts if the ID doesn't exist,
     * otherwise updates the existing row.
     *
     * @param road road entity
     */
    public void save(Road road) {
        String sql = """
            INSERT INTO roads (
                road_id, from_location_id, to_location_id,
                distance_km, travel_time_min, condition_weight
            )
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(road_id) DO UPDATE SET
                from_location_id = excluded.from_location_id,
                to_location_id = excluded.to_location_id,
                distance_km = excluded.distance_km,
                travel_time_min = excluded.travel_time_min,
                condition_weight = excluded.condition_weight;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, road.getRoadId());
            stmt.setString(2, road.getFromLocationId());
            stmt.setString(3, road.getToLocationId());
            stmt.setDouble(4, road.getDistanceKm());
            stmt.setDouble(5, road.getTravelTimeMin());
            stmt.setDouble(6, road.getConditionWeight());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save road: " + road.getRoadId(), e);
        }
    }

    /**
     * Finds a road by its ID.
     */
    public Road findById(String roadId) {
        String sql = "SELECT * FROM roads WHERE road_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roadId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find road: " + roadId, e);
        }
    }

    /**
     * Retrieves all roads.
     */
    public List<Road> findAll() {
        String sql = "SELECT * FROM roads;";
        List<Road> roads = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roads.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve roads", e);
        }

        return roads;
    }

    /**
     * Retrieves all roads originating from a given location — useful for
     * building the Graph adjacency structure (each location's outgoing edges).
     */
    public List<Road> findByFromLocationId(String fromLocationId) {
        String sql = "SELECT * FROM roads WHERE from_location_id = ?;";
        List<Road> roads = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fromLocationId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roads.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find roads from location: " + fromLocationId, e);
        }

        return roads;
    }

    /**
     * Deletes a road by its ID.
     */
    public void delete(String roadId) {
        String sql = "DELETE FROM roads WHERE road_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roadId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete road: " + roadId, e);
        }
    }

    private Road mapRow(ResultSet rs) throws SQLException {
        return new Road(
                rs.getString("road_id"),
                rs.getString("from_location_id"),
                rs.getString("to_location_id"),
                rs.getDouble("distance_km"),
                rs.getDouble("travel_time_min"),
                rs.getDouble("condition_weight")
        );
    }
}