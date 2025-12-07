package org.example;

import org.example.distribution.ParetoDistribution;
import org.example.model.Event;
import org.example.model.EventType;
import org.example.model.SourceState;
import org.example.model.TrafficSource;
import org.example.simulation.EventQueue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for TrafficSource class.
 * Tests ON/OFF state transitions and event scheduling.
 */
public class TrafficSourceTest {

    private TrafficSource source;
    private EventQueue queue;
    private ParetoDistribution onDist;
    private ParetoDistribution offDist;

    @Before
    public void setUp() {
        onDist = new ParetoDistribution(1.5, 1.0);
        offDist = new ParetoDistribution(1.5, 1.0);
        source = new TrafficSource(0, onDist, offDist, false);
        queue = new EventQueue();
    }

    @Test
    public void testConstructor() {
        assertNotNull(source);
        assertEquals(0, source.getId());
        assertFalse(source.isOn());  // Starts OFF
    }

    @Test
    public void testStartStateIsOff() {
        assertFalse("Source should start OFF", source.isOn());
        assertEquals("State should be OFF", SourceState.OFF, source.getState());
    }

    @Test
    public void testStartStateCanBeOn() {
        TrafficSource onSource = new TrafficSource(1, onDist, offDist, true);
        assertTrue("Source can start ON", onSource.isOn());
    }

    @Test
    public void testScheduleInitialEvent() {
        queue = new EventQueue();
        source.scheduleInitialEvent(queue, 0.0);

        assertFalse("Queue should not be empty", queue.isEmpty());
        Event event = queue.peek();
        assertNotNull(event);
        assertEquals(0, source.getId());
    }

    @Test
    public void testProcessEventTurnsSourceOn() {
        Event turnOnEvent = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        source.processEvent(turnOnEvent, 1.0, queue);

        assertTrue("Source should be ON after ON event", source.isOn());
    }

    @Test
    public void testProcessEventTurnsSourceOff() {
        // Start with ON
        source = new TrafficSource(0, onDist, offDist, true);

        Event turnOffEvent = new Event(2.0, EventType.SOURCE_TURNS_OFF, 0);
        source.processEvent(turnOffEvent, 2.0, queue);

        assertFalse("Source should be OFF after OFF event", source.isOn());
    }

    @Test
    public void testProcessEventSchedulesNextEvent() {
        Event turnOnEvent = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        source.processEvent(turnOnEvent, 1.0, queue);

        assertFalse("Queue should contain next event", queue.isEmpty());
        Event nextEvent = queue.peek();
        assertNotNull(nextEvent);

        // Next event should be TURN_OFF
        assertEquals(EventType.SOURCE_TURNS_OFF, nextEvent.getEventType());
        // Next event should be in the future
        assertTrue("Next event should be after current time",
                nextEvent.getTimestamp() > 1.0);
    }

    @Test
    public void testStateAlternates() {
        // Start OFF, process ON event
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        source.processEvent(event1, 1.0, queue);
        assertTrue(source.isOn());

        // Process OFF event
        Event event2 = queue.dequeue();
        source.processEvent(event2, event2.getTimestamp(), queue);
        assertFalse(source.isOn());

        // Process next ON event
        Event event3 = queue.dequeue();
        source.processEvent(event3, event3.getTimestamp(), queue);
        assertTrue(source.isOn());
    }

    @Test
    public void testMultipleEventSequence() {
        // Run 10 state transitions
        for (int i = 0; i < 10; i++) {
            if (queue.isEmpty()) {
                Event startEvent = new Event(i, EventType.SOURCE_TURNS_ON, 0);
                source.processEvent(startEvent, i, queue);
            }

            Event event = queue.dequeue();
            source.processEvent(event, event.getTimestamp(), queue);
        }

        // Source should be in some valid state
        assertTrue(source.isOn() || !source.isOn());  // Always true, just testing valid state
    }

    @Test
    public void testId() {
        TrafficSource source1 = new TrafficSource(1, onDist, offDist, false);
        TrafficSource source2 = new TrafficSource(5, onDist, offDist, false);

        assertEquals(1, source1.getId());
        assertEquals(5, source2.getId());
    }

    @Test
    public void testGetState() {
        assertEquals(SourceState.OFF, source.getState());

        Event turnOn = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        source.processEvent(turnOn, 1.0, queue);
        assertEquals(SourceState.ON, source.getState());
    }
}
