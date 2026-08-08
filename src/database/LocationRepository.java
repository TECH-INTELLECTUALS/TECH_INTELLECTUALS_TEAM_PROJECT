package database;

import models.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Location persistence operations.
 */
public class LocationRepository {

    private final DatabaseConnection databaseConnection;

    public LocationRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Saves a campus location. Inserts if the ID doesn't exist,
     * otherwise updates the existing row.
     *
     * @param location location entity
     */
    public void save(Location location) {
        String sql = """
            INSERT INTO locations (location_id, name, area, location_type, x_coord, y_coord)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(location_id) DO UPDATE SET
                name = excluded.name,
                area = excluded.area,
                location_type = excluded.location_type,
                x_coord = excluded.x_coord,
                y_coord = excluded.y_coord;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location.getLocationId());
            stmt.setString(2, location.getName());
            stmt.setString(3, location.getArea());
            stmt.setString(4, location.getLocationType());
            stmt.setDouble(5, location.getXCoord());
            stmt.setDouble(6, location.getYCoord());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save location: " + location.getLocationId(), e);
        }
    }

    /**
     * Finds a location by its ID.
     *
     * @param locationId the location's ID
     * @return the matching Location, or null if not found
     */
    public Location findById(int locationId) {
        String sql = "SELECT * FROM locations WHERE location_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find location: " + locationId, e);
        }
    }

    /**
     * Retrieves all locations.
     *
     * @return list of all Location entities
     */
    public List<Location> findAll() {
        String sql = "SELECT * FROM locations;";
        List<Location> locations = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve locations", e);
        }

        return locations;
    }

    /**
     * Deletes a location by its ID.
     *
     * @param locationId the location's ID
     */
    public void delete(int locationId) {
        String sql = "DELETE FROM locations WHERE location_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete location: " + locationId, e);
        }
    }

    /**
     * Maps the current row of a ResultSet to a Location object.
     */
    private Location mapRow(ResultSet rs) throws SQLException {
        return new Location(
                rs.getString("location_id"),
                rs.getString("name"),
                rs.getString("area"),
                rs.getString("location_type"),
                rs.getDouble("x_coord"),
                rs.getDouble("y_coord")
        );
    }
}