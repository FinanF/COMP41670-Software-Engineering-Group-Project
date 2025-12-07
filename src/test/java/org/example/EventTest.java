package org.example;

import org.example.model.Event;
import org.example.model.EventType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Event class.
 * Tests event creation, comparison, and properties.
 */
public class EventTest {

    @Test
    public void testConstructor() {
        Event event = new Event(1.5, EventType.SOURCE_TURNS_ON, 0);

        assertNotNull(event);
        assertEquals(1.5, event.getTimestamp(), 0.001);
        assertEquals(EventType.SOURCE_TURNS_ON, event.getEventType());
        assertEquals(0, event.getSourceId());
    }

    @Test
    public void testCompareToDifferentTimestamps() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event event2 = new Event(2.0, EventType.SOURCE_TURNS_OFF, 1);

        assertTrue("Earlier event should be less than later event",
                event1.compareTo(event2) < 0);
        assertTrue("Later event should be greater than earlier event",
                event2.compareTo(event1) > 0);
    }

    @Test
    public void testCompareSameTimestampDifferentSourceId() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);  // Source 0
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_OFF, 1); // Source 1

        // Same timestamp, so compare by source ID
        // Source 0 < Source 1, so event1 comes first
        assertTrue("Event with lower source ID should come first",
                event1.compareTo(event2) < 0);
        assertTrue("Event with higher source ID should come later",
                event2.compareTo(event1) > 0);
    }

    @Test
    public void testCompareSameTimestampSameSourceId() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_OFF, 0);  // Same source

        // Same timestamp AND same source ID -> equal
        assertEquals("Events with same timestamp and source ID should be equal",
                0, event1.compareTo(event2));
    }

    @Test
    public void testCompareSameTimestampLowerSourceId() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_OFF, 10);

        // Same timestamp, event1 has lower source ID
        assertTrue("Lower source ID should come first",
                event1.compareTo(event2) < 0);
    }

    @Test
    public void testCompareToNegativeTimestamp() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event event2 = new Event(2.0, EventType.SOURCE_TURNS_OFF, 0);

        assertTrue(event1.compareTo(event2) < 0);
    }

    @Test
    public void testComparisonChain() {
        Event e1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event e2 = new Event(2.0, EventType.SOURCE_TURNS_OFF, 0);
        Event e3 = new Event(3.0, EventType.SOURCE_TURNS_ON, 0);

        assertTrue("e1 < e2", e1.compareTo(e2) < 0);
        assertTrue("e2 < e3", e2.compareTo(e3) < 0);
        assertTrue("e1 < e3", e1.compareTo(e3) < 0);
    }

    @Test
    public void testZeroTimestamp() {
        Event event = new Event(0.0, EventType.SOURCE_TURNS_ON, 5);

        assertEquals(0.0, event.getTimestamp(), 0.001);
    }

    @Test
    public void testLargeTimestamp() {
        Event event = new Event(999999.0, EventType.SOURCE_TURNS_OFF, 10);

        assertEquals(999999.0, event.getTimestamp(), 0.001);
    }

    @Test
    public void testDifferentSourceIds() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_ON, 99);

        assertEquals(0, event1.getSourceId());
        assertEquals(99, event2.getSourceId());
    }

    @Test
    public void testEventTypes() {
        Event onEvent = new Event(1.0, EventType.SOURCE_TURNS_ON, 0);
        Event offEvent = new Event(1.0, EventType.SOURCE_TURNS_OFF, 0);

        assertEquals(EventType.SOURCE_TURNS_ON, onEvent.getEventType());
        assertEquals(EventType.SOURCE_TURNS_OFF, offEvent.getEventType());
        assertNotEquals(onEvent.getEventType(), offEvent.getEventType());
    }

    @Test
    public void testNegativeTimestampThrowsException() {
        try {
            new Event(-1.0, EventType.SOURCE_TURNS_ON, 0);
            fail("Should throw IllegalArgumentException for negative timestamp");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeSourceIdThrowsException() {
        try {
            new Event(1.0, EventType.SOURCE_TURNS_ON, -5);
            fail("Should throw IllegalArgumentException for negative source ID");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Source ID"));
        }
    }

    @Test
    public void testNullEventTypeThrowsException() {
        try {
            new Event(1.0, null, 0);
            fail("Should throw IllegalArgumentException for null event type");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Event type"));
        }
    }

    @Test
    public void testEqualsWithSameValues() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);

        assertEquals("Events with same values should be equal", event1, event2);
    }

    @Test
    public void testEqualsWithDifferentTimestamp() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(2.0, EventType.SOURCE_TURNS_ON, 5);

        assertNotEquals("Events with different timestamps should not be equal",
                event1, event2);
    }

    @Test
    public void testEqualsWithDifferentType() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_OFF, 5);

        assertNotEquals("Events with different types should not be equal",
                event1, event2);
    }

    @Test
    public void testEqualsWithDifferentSourceId() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_ON, 10);

        assertNotEquals("Events with different source IDs should not be equal",
                event1, event2);
    }

    @Test
    public void testHashCode() {
        Event event1 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);
        Event event2 = new Event(1.0, EventType.SOURCE_TURNS_ON, 5);

        assertEquals("Equal events should have equal hash codes",
                event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testToString() {
        Event event = new Event(1.5, EventType.SOURCE_TURNS_ON, 5);
        String str = event.toString();

        assertTrue("String should contain timestamp", str.contains("1.5"));
        assertTrue("String should contain event type", str.contains("SOURCE_TURNS_ON"));
        assertTrue("String should contain source ID", str.contains("5"));
    }
}
