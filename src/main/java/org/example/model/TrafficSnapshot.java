package org.example.model;

import java.util.*;

/**
 * Immutable snapshot of traffic state at a specific point in time.
 * A TrafficSnapshot captures the simulation state at a sampling interval:
 *   - When the sample was taken
 *   - How many sources were ON (aggregate traffic rate)
 *   - Which specific sources were ON
 * This class is immutable: once created, its state cannot change.
 * This ensures that stored snapshots cannot be accidentally modified.
 * Snapshots are collected periodically during simulation and stored in
 * SimulationStatistics for later analysis and reporting.
 */
public class TrafficSnapshot {

    private final double timestamp;
    private final long trafficRate;
    private final Set<Integer> activeSourceIds;

    /**
     * Constructor - validates inputs and makes defensive copies
     *
     * @param timestamp Current simulation time (seconds)
     * @param trafficRate Current traffic rate (packets/sec)
     * @param activeSourceIds Set of currently active source IDs
     * @throws IllegalArgumentException if validation fails
     */
    public TrafficSnapshot(double timestamp, long trafficRate, Set<Integer> activeSourceIds) {
        // Validation
        if (timestamp < 0) {
            throw new IllegalArgumentException("Timestamp cannot be negative: " + timestamp);
        }
        if (trafficRate < 0) {
            throw new IllegalArgumentException("Traffic rate cannot be negative: " + trafficRate);
        }
        if (activeSourceIds == null) {
            throw new IllegalArgumentException("Active source IDs cannot be null");
        }

        this.timestamp = timestamp;
        this.trafficRate = trafficRate;
        this.activeSourceIds = new HashSet<>(activeSourceIds);  // Defensive copy
    }

    /**
     * @return Current simulation time in seconds
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * @return Current traffic rate in packets per second
     */
    public long getTrafficRate() {
        return trafficRate;
    }

    /**
     * Returns a copy of active source IDs to prevent external modification
     * @return Unmodifiable set of active source IDs
     */
    public Set<Integer> getActiveSourceIds() {
        return Collections.unmodifiableSet(new HashSet<>(activeSourceIds));
    }

    /**
     * @return Number of currently active sources
     */
    public int getActiveSourceCount() {
        return activeSourceIds.size();
    }
}
