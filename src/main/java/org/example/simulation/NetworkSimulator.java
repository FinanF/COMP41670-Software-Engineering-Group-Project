package org.example.simulation;

import org.example.distribution.ParetoDistribution;
import org.example.model.*;
import java.util.*;
import java.io.IOException;

/**
 * Event-driven network traffic simulator.
 * Manages traffic sources, processes events, and collects statistics.
 * The simulator operates as follows:
 * 1. Initialize all traffic sources with initial ON/OFF events
 * 2. Process events from the EventQueue in chronological order
 * 3. For each event, update the corresponding source state
 * 4. Periodically (at outputInterval) record traffic snapshots
 * 5. Continue until simulation time is reached
 * This class is the core of the simulation engine.
 */
public class NetworkSimulator {

    private final SimulationConfig config;
    private final EventQueue eventQueue;
    private final List<TrafficSource> sources;
    private final SimulationStatistics statistics;
    private double currentTime;
    private double nextSnapshotTime;

    /**
     * Constructor - initializes the simulator with configuration
     *
     * @param config SimulationConfig containing all simulation parameters
     * @throws IllegalArgumentException if config is null
     */
    public NetworkSimulator(SimulationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("SimulationConfig cannot be null");
        }

        this.config = config;
        this.eventQueue = new EventQueue();
        this.sources = new ArrayList<>();
        this.statistics = new SimulationStatistics();
        this.currentTime = 0.0;
        this.nextSnapshotTime = config.getOutputInterval();

        // Initialize traffic sources
        initializeSources();
    }

    /**
     * Initializes all traffic sources with their initial events.
     * Each source starts in OFF state and gets its first ON event scheduled.
     * Sources are randomly initialized: some start ON, some start OFF.
     * This adds realistic variety to the initial conditions.
     */
    private void initializeSources() {
        Random random = new Random();

        // Create Pareto distributions for ON and OFF durations
        ParetoDistribution onDist = new ParetoDistribution(
                config.getParetoShape(),
                config.getParetoScale()
        );
        ParetoDistribution offDist = new ParetoDistribution(
                config.getParetoShape(),
                config.getParetoScale()
        );

        // Create each traffic source and schedule initial event
        for (int i = 0; i < config.getNumSources(); i++) {
            // Randomly decide if source starts ON or OFF
            boolean startOn = random.nextBoolean();

            TrafficSource src = new TrafficSource(i, onDist, offDist, startOn);
            sources.add(src);

            // Schedule the source's first event
            src.scheduleInitialEvent(eventQueue, currentTime);
        }
    }

    /**
     * Calculate current aggregate traffic rate.
     * The aggregate rate is the number of sources currently in the ON state.
     *
     * @return Number of currently active sources
     */
    private long calculateAggregateRate() {
        int activeCount = 0;
        for (TrafficSource source : sources) {
            if (source.isOn()) {
                activeCount++;
            }
        }
        return (long) activeCount;
    }

    /**
     * Get the set of currently active source IDs.
     * Used for populating TrafficSnapshot.
     *
     * @return Set of IDs of sources that are currently ON
     */
    private Set<Integer> getActiveSources() {
        Set<Integer> active = new HashSet<>();
        for (TrafficSource source : sources) {
            if (source.isOn()) {
                active.add(source.getId());
            }
        }
        return active;
    }

    /**
     * Record a traffic snapshot at the current simulation time.
     * Captures the current traffic rate and active sources.
     */
    private void recordSnapshot() {
        long rate = calculateAggregateRate();
        Set<Integer> activeSources = getActiveSources();
        TrafficSnapshot snapshot = new TrafficSnapshot(currentTime, rate, activeSources);
        statistics.addSnapshot(snapshot);
    }

    /**
     * Run the complete simulation.
     * Algorithm:
     * 1. Record initial snapshot at time 0
     * 2. While current time < total simulation time:
     *    a. Check if it's time to record a snapshot
     *    b. If yes, record snapshot and update next snapshot time
     *    c. Process next event from queue (if available)
     *    d. If no events, break
     * 3. Record final snapshot at end time
     * This ensures snapshots are recorded at regular intervals regardless
     * of event timing, providing accurate time-series data.
     */
    public void run() {
        // Record initial snapshot at time 0
        recordSnapshot();

        // Process events until simulation time is reached
        while (currentTime < config.getTotalSimulationTime() && !eventQueue.isEmpty()) {
            // Check if it's time to record a snapshot
            if (currentTime >= nextSnapshotTime) {
                recordSnapshot();
                nextSnapshotTime += config.getOutputInterval();
            }

            // Get next event
            Event nextEvent = eventQueue.peek();
            if (nextEvent == null) {
                break;
            }

            // Update current time to the event time
            currentTime = nextEvent.getTimestamp();

            // Stop if this event is beyond simulation end time
            if (currentTime >= config.getTotalSimulationTime()) {
                break;
            }

            // Dequeue and process the event
            Event event = eventQueue.dequeue();
            int sourceId = event.getSourceId();
            TrafficSource source = sources.get(sourceId);

            // Process the event - this updates source state and schedules next event
            source.processEvent(event, currentTime, eventQueue);

            // Log event if enabled
            if (config.isEventLoggingEnabled()) {
                logEvent(event, sourceId);
            }
        }

        // Record final snapshot at simulation end time
        currentTime = config.getTotalSimulationTime();
        recordSnapshot();
    }

    /**
     * Log an event to the console for debugging/analysis.
     *
     * @param event The event that occurred
     * @param sourceId The ID of the source involved
     */
    private void logEvent(Event event, int sourceId) {
        String eventTypeStr = (event.getEventType() == EventType.SOURCE_TURNS_ON) ? "ON" : "OFF";
        System.out.printf("Time [%.4f] Source %d turned %s%n", currentTime, sourceId, eventTypeStr);
    }

    // ===== GETTER METHODS =====

    /**
     * Get the simulation statistics collected during the run.
     *
     * @return SimulationStatistics object with collected snapshots and computed statistics
     */
    public SimulationStatistics getStatistics() {
        return statistics;
    }

    /**
     * Get the current simulation time.
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * Get the number of events currently in the queue.
     */
    public int getEventQueueSize() {
        return eventQueue.size();
    }

    /**
     * Get the number of traffic sources.
     */
    public int getNumSources() {
        return sources.size();
    }

    /**
     * Get a specific traffic source by ID.
     *
     * @param sourceId The ID of the source to retrieve
     * @return The TrafficSource with the given ID
     * @throws IndexOutOfBoundsException if sourceId is invalid
     */
    public TrafficSource getSource(int sourceId) {
        return sources.get(sourceId);
    }

    /**
     * Get all traffic sources.
     *
     * @return Unmodifiable list of all TrafficSource objects
     */
    public List<TrafficSource> getAllSources() {
        return Collections.unmodifiableList(sources);
    }

    // ===== UTILITY METHODS =====

    /**
     * Summary of simulation results to console.
     * Includes statistics and file output information.
     */
    public void printSummary() {
        System.out.println("\n" + statistics.getSummary());
        System.out.println("Snapshots Recorded: " + statistics.getSnapshotCount());
        System.out.println("Sources: " + sources.size());

        // Export to CSV if configured
        if (config.getOutputFilePath() != null) {
            try {
                statistics.exportToCSV(config.getOutputFilePath());
                System.out.println("CSV exported to: " + config.getOutputFilePath());
            } catch (IOException e) {
                System.err.println("Failed to export CSV: " + e.getMessage());
            }
        }
    }

    /**
     * Returns a string representation of the simulator state.
     *
     * @return String describing current simulator state
     */
    @Override
    public String toString() {
        return String.format(
                "NetworkSimulator{time=%.4f, sources=%d, queueSize=%d, snapshots=%d}",
                currentTime,
                sources.size(),
                eventQueue.size(),
                statistics.getSnapshotCount()
        );
    }
}