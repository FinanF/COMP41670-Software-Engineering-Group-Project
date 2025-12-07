package org.example;

import org.example.model.SimulationConfig;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for SimulationConfig (configuration DTO).
 * Tests parameter validation and getters.
 */
public class SimulationConfigTest {

    @Test
    public void testValidConfig() {
        SimulationConfig config = new SimulationConfig(
                10.0, 5, 1.5, 1.0, 0.5, true, "output.csv");

        assertNotNull(config);
        assertTrue(config.validateParameters());
    }

    @Test
    public void testGetters() {
        SimulationConfig config = new SimulationConfig(
                10.0, 5, 1.5, 1.0, 0.5, true, "output.csv");

        assertEquals(10.0, config.getTotalSimulationTime(), 0.001);
        assertEquals(5, config.getNumSources());
        assertEquals(1.5, config.getParetoShape(), 0.001);
        assertEquals(1.0, config.getParetoScale(), 0.001);
        assertEquals(0.5, config.getOutputInterval(), 0.001);
        assertTrue(config.isEventLoggingEnabled());
        assertEquals("output.csv", config.getOutputFilePath());
    }

    @Test
    public void testNegativeTotalTime() {
        try {
            new SimulationConfig(-5.0, 5, 1.5, 1.0, 0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testZeroTotalTime() {
        try {
            new SimulationConfig(0.0, 5, 1.5, 1.0, 0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeSources() {
        try {
            new SimulationConfig(10.0, -5, 1.5, 1.0, 0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeParetoShape() {
        try {
            new SimulationConfig(10.0, 5, -1.5, 1.0, 0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeParetoScale() {
        try {
            new SimulationConfig(10.0, 5, 1.5, -1.0, 0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeOutputInterval() {
        try {
            new SimulationConfig(10.0, 5, 1.5, 1.0, -0.5, true, null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testValidConfigWithNullPath() {
        SimulationConfig config = new SimulationConfig(
                10.0, 5, 1.5, 1.0, 0.5, false, null);

        assertTrue(config.validateParameters());
        assertNull(config.getOutputFilePath());
    }

    @Test
    public void testEventLoggingFlag() {
        SimulationConfig withLogging = new SimulationConfig(
                10.0, 5, 1.5, 1.0, 0.5, true, null);
        SimulationConfig noLogging = new SimulationConfig(
                10.0, 5, 1.5, 1.0, 0.5, false, null);

        assertTrue(withLogging.isEventLoggingEnabled());
        assertFalse(noLogging.isEventLoggingEnabled());
    }

    @Test
    public void testMinimumValidValues() {
        SimulationConfig config = new SimulationConfig(
                0.1, 1, 0.5, 0.1, 0.01, false, null);

        assertTrue(config.validateParameters());
    }

    @Test
    public void testLargeValues() {
        SimulationConfig config = new SimulationConfig(
                1000.0, 100, 3.0, 10.0, 100.0, true, "output.csv");

        assertTrue(config.validateParameters());
    }
}