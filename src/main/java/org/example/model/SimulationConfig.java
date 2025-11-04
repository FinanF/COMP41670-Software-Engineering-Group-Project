package org.example.model;

/**
 * Data Transfer Object containing all configuration parameters for the simulation.
 * This class encapsulates all the user-provided parameters that control the
 * simulation behavior. All fields are final (immutable) for thread safety and
 * to prevent accidental modification.
 *
 * @author Jordan Shodipo
 * @version 1.0
 */
public class SimulationConfig {
    private final double totalSimulationTime;    // Total duration (seconds)
    private final int numSources;               // Number of traffic sources
    private final double paretoShape;           // Pareto shape parameter (α)
    private final double paretoScale;           // Pareto scale parameter (β)
    private final double outputInterval;        // Sampling interval (seconds)
    private final boolean enableEventLogging;   // Whether to log individual events
    private final String outputFilePath;        // Optional path for CSV export

    /**
     * Creates a new SimulationConfig with the specified parameters.
     *
     * @param totalSimulationTime  total simulation duration in seconds (must be > 0)
     * @param numSources           number of traffic sources (must be > 0)
     * @param paretoShape          Pareto shape parameter α (must be > 0)
     * @param paretoScale          Pareto scale parameter β (must be > 0)
     * @param outputInterval       sampling interval in seconds (must be > 0)
     *
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public SimulationConfig(double totalSimulationTime,
                            int numSources,
                            double paretoShape,
                            double paretoScale,
                            double outputInterval) {
        this(totalSimulationTime, numSources, paretoShape, paretoScale,
                outputInterval, false, null);
    }

    /**
     * Creates a new SimulationConfig with all parameters including optional ones.
     *
     * @param enableEventLogging   whether to log all events
     * @param outputFilePath       path for CSV output (null for no file output)
     *
     * @throws IllegalArgumentException if validation fails
     */
    public SimulationConfig(double totalSimulationTime,
                            int numSources,
                            double paretoShape,
                            double paretoScale,
                            double outputInterval,
                            boolean enableEventLogging,
                            String outputFilePath) {

        if (totalSimulationTime <= 0) {
            throw new IllegalArgumentException("Total simulation time must be positive");
        }
        if (numSources <= 0) {
            throw new IllegalArgumentException("Number of sources must be positive");
        }
        if (paretoShape <= 0) {
            throw new IllegalArgumentException("Pareto shape must be positive");
        }
        if (paretoScale <= 0) {
            throw new IllegalArgumentException("Pareto scale must be positive");
        }
        if (outputInterval <= 0) {
            throw new IllegalArgumentException("Output interval must be positive");
        }

        this.totalSimulationTime = totalSimulationTime;
        this.numSources = numSources;
        this.paretoShape = paretoShape;
        this.paretoScale = paretoScale;
        this.outputInterval = outputInterval;
        this.enableEventLogging = enableEventLogging;
        this.outputFilePath = outputFilePath;
    }

    /**
     * Validates all configuration parameters.
     *
     * @return true if all parameters are valid, false otherwise
     */
    public boolean validateParameters() {
        if (totalSimulationTime <= 0) return false;
        if (numSources <= 0) return false;
        if (paretoShape <= 0) return false;
        if (paretoScale <= 0) return false;
        return !(outputInterval <= 0);
    }

    // GETTER METHODS

    /**
     * Gets the total simulation time.
     */
    public double getTotalSimulationTime() {
        return totalSimulationTime;
    }

    /**
     * Gets the number of traffic sources.
     */
    public int getNumSources() {
        return numSources;
    }

    /**
     * Gets the Pareto shape parameter (α).
     */
    public double getParetoShape() {
        return paretoShape;
    }

    /**
     * Gets the Pareto scale parameter (β).
     */
    public double getParetoScale() {
        return paretoScale;
    }

    /**
     * Gets the output sampling interval.
     * @return the sampling interval in seconds
     */
    public double getOutputInterval() {
        return outputInterval;
    }

    /**
     * Checks if event logging is enabled.
     * @return true if event logging is enabled
     */
    public boolean isEventLoggingEnabled() {
        return enableEventLogging;
    }

    /**
     * Gets the output file path.
     * @return the file path, or null if no file output
     */
    public String getOutputFilePath() {
        return outputFilePath;
    }
}
