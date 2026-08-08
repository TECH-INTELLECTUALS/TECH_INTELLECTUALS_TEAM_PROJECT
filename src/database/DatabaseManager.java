package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database lifecycle and persistence orchestration.
 */
public class DatabaseManager {

    private final DatabaseConnection databaseConnection;

    public DatabaseManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Initializes the database environment: opens the connection and
     * creates all required tables if they do not already exist.
     */
    public void initialize() {
        Connection conn = databaseConnection.open();

        try (Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS locations (
                    location_id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    area TEXT,
                    location_type TEXT,
                    x_coord REAL,
                    y_coord REAL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS roads (
                    road_id TEXT PRIMARY KEY,
                    from_location_id TEXT NOT NULL,
                    to_location_id TEXT NOT NULL,
                    distance_km REAL,
                    travel_time_min REAL,
                    condition_weight REAL,
                    FOREIGN KEY (from_location_id) REFERENCES locations(location_id),
                    FOREIGN KEY (to_location_id) REFERENCES locations(location_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS service_requests (
                    request_id TEXT PRIMARY KEY,
                    source_location_id TEXT NOT NULL,
                    destination_location_id TEXT NOT NULL,
                    category TEXT,
                    urgency TEXT,
                    time_submitted TEXT,
                    deadline TEXT,
                    status TEXT,
                    FOREIGN KEY (source_location_id) REFERENCES locations(location_id),
                    FOREIGN KEY (destination_location_id) REFERENCES locations(location_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS resources (
                    resource_id TEXT PRIMARY KEY,
                    resource_type TEXT NOT NULL,
                    home_location_id INTEGER,
                    capacity INTEGER,
                    availability_status TEXT,
                    FOREIGN KEY (home_location_id) REFERENCES locations(location_id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS algorithm_runs (
                    algorithm_run_id INTEGER PRIMARY KEY,
                    algorithm_name TEXT NOT NULL,
                    input_size INTEGER,
                    execution_time_ns INTEGER,
                    memory_kb INTEGER,
                    result_summary TEXT,
                    timestamp TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS audit_events (
                    event_id INTEGER PRIMARY KEY,
                    action TEXT NOT NULL,
                    actor TEXT,
                    timestamp TEXT,
                    description TEXT
                );
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    /**
     * Closes the underlying database connection.
     */
    public void shutdown() {
        databaseConnection.close();
    }
}