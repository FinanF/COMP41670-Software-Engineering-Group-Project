package org.example;

import org.example.model.SimulationConfig;
import org.example.simulation.NetworkSimulator;
import org.example.util.InputValidator;

import java.util.logging.Logger;

/**
 * Event-driven ON/OFF self-similar traffic simulation using SimulationConfig + EventQueue.
 */
public class Simulation {

    private final InputValidator inputValidator;
    private boolean continueRunning;
    private static final Logger logger = Logger.getLogger(Simulation.class.getName());

    public Simulation() {
        this.inputValidator = new InputValidator();
        this.continueRunning = true;
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        Simulation app = new Simulation();
        app.run();
    }

    /**
     * Main application loop.
     * Displays welcome, runs simulations in loop until user quits.
     */
    public void run() {
        displayWelcome();

        while(continueRunning) {
            try {
                // Get parameters from user
                SimulationConfig config = collectSimulationParameters();

                // Run simulation
                System.out.println("Running simulation...");

                NetworkSimulator simulator = new NetworkSimulator(config);
                simulator.run();
                System.out.println("Simulation complete.");

                // Display results
                simulator.printSummary();

                // Ask if user wants to continue
                continueRunning = inputValidator.readYesNo("\nRun another simulation? (y/n): ");

            } catch (Exception e) {
                System.err.println("Error during simulation: " + e.getMessage());
                logger.severe("Exception during simulation: " + e);
                continueRunning = inputValidator.readYesNo("\nTry again? (y/n): ");
            }
        }
        displayGoodbye();
        inputValidator.close();
    }
    /**
     * Collect simulation parameters from user via console prompts.
     *
     * @return SimulationConfig with user-provided parameters
     */
    private SimulationConfig collectSimulationParameters() {
        System.out.println("SIMULATION PARAMETERS");

        // Total simulation time
        double totalTime = inputValidator.readDouble(
                "Enter total simulation time (seconds): ",
                0.1,
                1000.0
        );

        // Number of sources
        int numSources = inputValidator.readInt(
                "Enter number of traffic sources: ",
                1,
                100
        );

        // Pareto shape parameter
        double paretoShape = inputValidator.readDouble(
                "Enter Pareto shape parameter (>0): ",
                0.5,
                3.0
        );

        // Pareto scale parameter
        double paretoScale = inputValidator.readDouble(
                "Enter Pareto scale parameter (>0): ",
                0.1,
                10.0
        );

        // Output interval (snapshot frequency)
        double outputInterval = inputValidator.readDouble(
                "Enter snapshot interval (seconds, 0.01-" + totalTime + "): ",
                0.01,
                totalTime
        );

        // Event logging
        boolean enableEventLogging = inputValidator.readYesNo(
                "Enable event logging to console? (y/n): "
        );

        // CSV output
        String outputPath = null;
        boolean wantCSV = inputValidator.readYesNo(
                "Export results to CSV file? (y/n): "
        );
        if (wantCSV) {
            outputPath = inputValidator.readString(
                    "Enter output filename (e.g., 'simulation_output.csv'): "
            );
        }

        // Create and return config
        return new SimulationConfig(
                totalTime,
                numSources,
                paretoShape,
                paretoScale,
                outputInterval,
                enableEventLogging,
                outputPath
        );
    }
    /**
     * Display welcome message and usage instructions.
     */
    private void displayWelcome() {
        System.out.println("TELECOM NETWORK TRAFFIC SIMULATOR WITH SELF-SIMILAR STATISTICS");
        System.out.println();
        System.out.println("This simulator models network traffic using an aggregated ON/OFF");
        System.out.println("source model with Pareto-distributed ON and OFF durations.");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  • Event-driven simulation");
        System.out.println("  • Self-similar traffic generation");
        System.out.println("  • Real-time snapshots and statistics");
        System.out.println("  • CSV export for analysis");
        System.out.println("  • Event logging for debugging");
        System.out.println();
        System.out.println("Type 'quit' or 'q' at any prompt to exit.");
        System.out.println();
    }

    /**
     * Display goodbye message.
     */
    private void displayGoodbye() {
        System.out.println("Thank you for using the Telecom Network Traffic Simulator!");
        System.out.println("Goodbye!");
    }
}
