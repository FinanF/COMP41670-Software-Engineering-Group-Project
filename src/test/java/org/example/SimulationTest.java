package org.example;


import org.example.model.SimulationConfig;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulationTest {
    @Test
    public void testCollectSimulationParameters() {
        // Simulate all inputs in correct order:
        // totalTime, numSources, paretoShape, paretoScale, interval, logging, csv?, filename
        String input =
                """
                        100.0
                        5
                        1.5
                        0.5
                        10.0
                        y
                        y
                        results.csv
                        """;

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Simulation sim = new Simulation();

        SimulationConfig config = sim.collectSimulationParameters();
        double DELTA = 1e-9;
        assertEquals(100.0, config.getTotalSimulationTime(), DELTA);
        assertEquals(5, config.getNumSources());
        assertEquals(1.5, config.getParetoShape(), DELTA);
        assertEquals(0.5, config.getParetoScale(), DELTA);
        assertEquals(10.0, config.getOutputInterval(), DELTA);
        assertTrue(config.isEventLoggingEnabled());
        assertEquals("results.csv", config.getOutputFilePath());
    }

    @Test
    public void testFullSimulationFlow() {
        String input =
                "10\n" + // total time
                        "3\n" +  // sources
                        "1\n" +  // shape
                        "1\n" +  // scale
                        "1\n" +  // interval
                        "n\n" +  // event logging off
                        "n\n" +  // no CSV output
                        "n\n";   // don't run again

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Simulation simulation = new Simulation();
        simulation.run();

        String out = output.toString();
        assertTrue(out.contains("Simulation complete."));
        assertTrue(out.contains("Goodbye"));
    }


}

