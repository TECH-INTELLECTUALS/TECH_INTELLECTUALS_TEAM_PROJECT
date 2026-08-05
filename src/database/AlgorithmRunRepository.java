package database;

import models.AlgorithmRun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for AlgorithmRun persistence operations.
 */
public class AlgorithmRunRepository {

    private final DatabaseConnection databaseConnection;

    public AlgorithmRunRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void save(AlgorithmRun run) {
        String sql = """
            INSERT INTO algorithm_runs (
                algorithm_run_id, algorithm_name, input_size,
                execution_time_ns, memory_kb, result_summary, timestamp
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT(algorithm_run_id) DO UPDATE SET
                algorithm_name = excluded.algorithm_name,
                input_size = excluded.input_size,
                execution_time_ns = excluded.execution_time_ns,
                memory_kb = excluded.memory_kb,
                result_summary = excluded.result_summary,
                timestamp = excluded.timestamp;
        """;

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, run.getAlgorithmRunId());
            stmt.setString(2, run.getAlgorithmName());
            stmt.setInt(3, run.getInputSize());
            stmt.setLong(4, run.getExecutionTimeNs());
            stmt.setLong(5, run.getMemoryKb());
            stmt.setString(6, run.getResultSummary());
            stmt.setString(7, run.getTimestamp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save algorithm run: " + run.getAlgorithmRunId(), e);
        }
    }

    public AlgorithmRun findById(int algorithmRunId) {
        String sql = "SELECT * FROM algorithm_runs WHERE algorithm_run_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, algorithmRunId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find algorithm run: " + algorithmRunId, e);
        }
    }

    public List<AlgorithmRun> findAll() {
        String sql = "SELECT * FROM algorithm_runs;";
        List<AlgorithmRun> runs = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                runs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve algorithm runs", e);
        }

        return runs;
    }

    /**
     * Retrieves all runs for a given algorithm name — useful for
     * building performance comparison graphs (Section 9 experiments).
     */
    public List<AlgorithmRun> findByAlgorithmName(String algorithmName) {
        String sql = "SELECT * FROM algorithm_runs WHERE algorithm_name = ?;";
        List<AlgorithmRun> runs = new ArrayList<>();

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, algorithmName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    runs.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find algorithm runs by name: " + algorithmName, e);
        }

        return runs;
    }

    public void delete(int algorithmRunId) {
        String sql = "DELETE FROM algorithm_runs WHERE algorithm_run_id = ?;";

        Connection conn = databaseConnection.open();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, algorithmRunId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete algorithm run: " + algorithmRunId, e);
        }
    }

    private AlgorithmRun mapRow(ResultSet rs) throws SQLException {
        return new AlgorithmRun(
                rs.getInt("algorithm_run_id"),
                rs.getString("algorithm_name"),
                rs.getInt("input_size"),
                rs.getLong("execution_time_ns"),
                rs.getLong("memory_kb"),
                rs.getString("result_summary"),
                rs.getString("timestamp")
        );
    }
}