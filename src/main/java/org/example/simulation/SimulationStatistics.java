package org.example.simulation;

import org.example.model.TrafficSnapshot;
import java.io.*;
import java.util.*;

/**
 * Aggregates traffic snapshots and computes statistics.
 * Mutable - snapshots are added during simulation.
 */
public class SimulationStatistics {

    private final List<TrafficSnapshot> snapshots;

    /**
     * Constructor - initializes empty statistics collector
     */
    public SimulationStatistics() {
        this.snapshots = new ArrayList<>();
    }

    /**
     * Add a snapshot to the collection
     * @param snapshot The traffic snapshot to record
     * @throws IllegalArgumentException if snapshot is null
     */
    public void addSnapshot(TrafficSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException("Cannot add null snapshot");
        }
        snapshots.add(snapshot);
    }

    /**
     * @return Number of snapshots collected
     */
    public int getSnapshotCount() {
        return snapshots.size();
    }

    /**
     * @return Unmodifiable list of all snapshots
     */
    public List<TrafficSnapshot> getSnapshots() {
        return List.copyOf(snapshots);
    }

    /**
     * Compute aggregate statistics from collected snapshots
     * @return Map containing: minRate, maxRate, avgRate, totalSnapshots
     * @throws IllegalStateException if no snapshots collected
     */
    public Map<String, Object> computeStatistics() {
        if(snapshots.isEmpty()){
            throw new IllegalStateException("No snapshots collected - cannot compute statistics");
        }

        Map<String, Object> stats = new HashMap<>();

        long minRate = Long.MAX_VALUE;
        long maxRate = Long.MIN_VALUE;
        long sumRate = 0;

        // looping through snapshots to find min/max/sum
        for (TrafficSnapshot snapshot : snapshots) {
            long rate = snapshot.getTrafficRate();
            minRate = Math.min(minRate, rate);
            maxRate = Math.max(maxRate, rate);
            sumRate += rate;
        }
        double avgRate = (double) sumRate / snapshots.size();
        stats.put("minRate", minRate);
        stats.put("maxRate", maxRate);
        stats.put("avgRate", avgRate);
        stats.put("totalSnapshots", snapshots.size());
        return stats;
    }

    /**
     * Export time-series data to CSV file
     * Format: timestamp,trafficRate,activeSourceCount
     *
     * @param filename Output CSV filename
     * @throws IOException if file writing fails
     */
    public void exportToCSV(String filename) throws IOException {
        if(snapshots.isEmpty()){
            throw new IllegalStateException("No snapshots to export");
        }

        try (FileWriter writer = new FileWriter(filename);
             BufferedWriter buffer = new BufferedWriter(writer)) {

            // Write header
            buffer.write("timestamp,trafficRate,activeSourceCount\n");

            // Write data rows
            for (TrafficSnapshot snapshot : snapshots) {
                String line = String.format(
                        "%.4f,%d,%d\n",
                        snapshot.getTimestamp(),
                        snapshot.getTrafficRate(),
                        snapshot.getActiveSourceCount()
                );
                buffer.write(line);
            }
        }
    }

    /**
     * Get time range covered by snapshots
     * @return Map with "startTime" and "endTime"
     */
    public Map<String, Double> getTimeRange() {
        if(snapshots.isEmpty()){
            throw new IllegalStateException("No snapshots collected");
        }

        double startTime = snapshots.get(0).getTimestamp();
        double endTime = snapshots.get(snapshots.size() - 1).getTimestamp();

        Map<String, Double> range = new HashMap<>();
        range.put("startTime", startTime);
        range.put("endTime", endTime);
        return range;
    }

    /**
     * Summary statistics as formatted string
     */
    public String getSummary() {
        Map<String, Object> stats = computeStatistics();
        Map<String, Double> timeRange = getTimeRange();

        return String.format(
                """
                        === Simulation Statistics ===
                        Total Snapshots: %d
                        Time Range: %.2f - %.2f seconds
                        Min Rate: %d packets/sec
                        Max Rate: %d packets/sec
                        Avg Rate: %.2f packets/sec
                        """,
                snapshots.size(),
                timeRange.get("startTime"),
                timeRange.get("endTime"),
                (long) stats.get("minRate"),
                (long) stats.get("maxRate"),
                (double) stats.get("avgRate")
        );
    }
}