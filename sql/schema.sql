-- ============================================================
-- University Campus Service Hub
-- Database Schema (SQLite)
-- Team C - Backend & Data Management
-- ============================================================

-- Locations: campus nodes (buildings, hostels, labs, shuttle stops)
CREATE TABLE IF NOT EXISTS locations (
    location_id     INTEGER PRIMARY KEY,
    name            TEXT NOT NULL,
    area            TEXT,
    location_type   TEXT,
    x_coord         REAL,
    y_coord         REAL
);

-- Roads: weighted, directional edges between locations
CREATE TABLE IF NOT EXISTS roads (
    road_id             INTEGER PRIMARY KEY,
    from_location_id    INTEGER NOT NULL,
    to_location_id      INTEGER NOT NULL,
    distance_km         REAL,
    travel_time_min     REAL,
    condition_weight    REAL,
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id)
);

-- Service Requests: maintenance/service jobs submitted by users
CREATE TABLE IF NOT EXISTS service_requests (
    request_id                 INTEGER PRIMARY KEY,
    source_location_id         INTEGER NOT NULL,
    destination_location_id    INTEGER NOT NULL,
    category                   TEXT,
    urgency                    TEXT,
    time_submitted              TEXT,
    deadline                   TEXT,
    status                     TEXT,
    FOREIGN KEY (source_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (destination_location_id) REFERENCES locations(location_id)
);

-- Resources: campus assets (vehicles, equipment, staff, etc.)
CREATE TABLE IF NOT EXISTS resources (
    resource_id             INTEGER PRIMARY KEY,
    resource_type           TEXT NOT NULL,
    home_location_id        INTEGER,
    capacity                INTEGER,
    availability_status     TEXT,
    FOREIGN KEY (home_location_id) REFERENCES locations(location_id)
);

-- Algorithm Runs: empirical performance measurements
CREATE TABLE IF NOT EXISTS algorithm_runs (
    algorithm_run_id     INTEGER PRIMARY KEY,
    algorithm_name       TEXT NOT NULL,
    input_size           INTEGER,
    execution_time_ns    INTEGER,
    memory_kb            INTEGER,
    result_summary       TEXT,
    timestamp            TEXT
);

-- Audit Events: system event / action log
CREATE TABLE IF NOT EXISTS audit_events (
    event_id        INTEGER PRIMARY KEY,
    action          TEXT NOT NULL,
    actor           TEXT,
    timestamp       TEXT,
    description     TEXT
);
