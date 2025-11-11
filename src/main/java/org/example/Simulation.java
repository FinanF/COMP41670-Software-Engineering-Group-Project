package org.example;

import org.example.model.Event;
import org.example.distribution.ParetoDistribution;
import org.example.model.SimulationConfig;
import org.example.model.TrafficSource;
import org.example.simulation.EventQueue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Event-driven ON/OFF self-similar traffic simulation using SimulationConfig + EventQueue.
 */
public class Simulation {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // === Read inputs for SimulationConfig ===
        System.out.print("Enter total simulation time (seconds): ");
        double totalSimTime = in.nextDouble();

        System.out.print("Enter number of traffic sources: ");
        int numSources = in.nextInt();

        System.out.print("Enter Pareto shape (>0): ");
        double paretoShape = in.nextDouble();

        System.out.print("Enter Pareto scale (>0): ");
        double paretoScale = in.nextDouble();

        System.out.print("Enter output interval (seconds): ");
        double outputInterval = in.nextDouble();

        System.out.print("Enable event logging? (true/false): ");
        boolean enableEventLogging = in.nextBoolean();

        in.nextLine(); // consume newline
        System.out.print("Enter CSV output file path (blank for none): ");
        String outputPath = in.nextLine().trim();
        if (outputPath.isEmpty()) outputPath = null;

        // === Build config ===
        SimulationConfig config = new SimulationConfig(
                totalSimTime,
                numSources,
                paretoShape,
                paretoScale,
                outputInterval,
                enableEventLogging,
                outputPath
        );

        if (!config.validateParameters()) {
            System.err.println("Invalid simulation parameters. Exiting.");
            return;
        }

        // === Init sources and queue (all start OFF; schedule first ON) ===
        List<TrafficSource> sources = new ArrayList<>();
        EventQueue queue = new EventQueue();

        for (int i = 0; i < config.getNumSources(); i++) {
            ParetoDistribution onDist  = new ParetoDistribution(config.getParetoShape(), config.getParetoScale());
            ParetoDistribution offDist = new ParetoDistribution(config.getParetoShape(), config.getParetoScale());
            TrafficSource src = new TrafficSource(i, onDist, offDist, /*startOn=*/false);
            sources.add(src);
            src.scheduleInitialEvent(queue, /*startTime=*/0.0);
        }

        // === Run event-driven loop ===
        double now = 0.0;
        int activeSources = 0;

        // time-series sampling
        double nextSampleT = 0.0;
        StringBuilder csv = new StringBuilder();
        csv.append("time,active_sources\n");

        System.out.printf(Locale.US,
                "Config: T=%.3f s, N=%d, shape=%.3f, scale=%.3f, t=%.3f s, logging=%s%s%n",
                config.getTotalSimulationTime(), config.getNumSources(),
                config.getParetoShape(), config.getParetoScale(),
                config.getOutputInterval(), config.isEventLoggingEnabled(),
                config.getOutputFilePath() != null ? (", csv=" + config.getOutputFilePath()) : "");

        while (!queue.isEmpty()) {
            Event e = queue.peek();
            if (e == null) break;

            // stop if next event exceeds sim horizon
            if (e.getTimestamp() > config.getTotalSimulationTime()) {
                break;
            }

            // advance time
            e = queue.dequeue();
            now = e.getTimestamp();

            // process event with its source
            TrafficSource ts = sources.get(e.getSourceId());
            boolean wasOn = ts.isOn();

            if (config.isEventLoggingEnabled()) {
                System.out.printf(Locale.US,
                        "t=%.6f | ts=%d | %s%n",
                        now, e.getSourceId(), e.getEventType());
            }

            // state update + schedule opposite event
            ts.processEvent(e, now, queue);

            boolean isOn = ts.isOn();
            if (wasOn != isOn) {
                activeSources += isOn ? 1 : -1;
            }
        }

//        // write CSV if requested
//        if (config.getOutputFilePath() != null) {
//            try {
//                Files.writeString(Path.of(config.getOutputFilePath()), csv.toString());
//                System.out.println("Wrote CSV to: " + config.getOutputFilePath());
//            } catch (Exception ex) {
//                System.err.println("Failed to write CSV: " + ex.getMessage());
//            }
//        }

        System.out.println("Simulation complete.");
    }
}
