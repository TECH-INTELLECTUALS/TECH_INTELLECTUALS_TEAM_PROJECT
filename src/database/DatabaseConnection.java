package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the JDBC connection to the SQLite database file.
 */
public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:campus_hub.db";

    private Connection connection;

    public Connection open() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open database connection", e);
        }
        return connection;
    }

    public Connection getConnection() {
        return open();
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection", e);
        }
    }
}