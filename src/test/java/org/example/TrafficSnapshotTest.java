package org.example;

import org.example.model.TrafficSnapshot;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * Unit tests for TrafficSnapshot (immutable time-series data point).
 * Tests immutability, defensive copying, and validation.
 */
public class TrafficSnapshotTest {

    @Test
    public void testConstructor() {
        Set<Integer> activeIds = new HashSet<>();
        activeIds.add(0);
        activeIds.add(1);

        TrafficSnapshot snapshot = new TrafficSnapshot(1.5, 2, activeIds);

        assertNotNull(snapshot);
        assertEquals(1.5, snapshot.getTimestamp(), 0.001);
        assertEquals(2, snapshot.getTrafficRate());
    }

    @Test
    public void testNegativeTimestamp() {
        Set<Integer> activeIds = new HashSet<>();

        try {
            new TrafficSnapshot(-1.0, 2, activeIds);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testNegativeRateRejected() {
        Set<Integer> activeIds = new HashSet<>();

        try {
            new TrafficSnapshot(1.0, -5, activeIds);
            fail("Should reject negative rate");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("rate"));
        }
    }

    @Test
    public void testNullActiveIdsRejected() {
        try {
            new TrafficSnapshot(1.0, 2, null);
            fail("Should reject null activeIds");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDefensiveCopyOnInput() {
        Set<Integer> activeIds = new HashSet<>();
        activeIds.add(0);
        activeIds.add(1);

        TrafficSnapshot snapshot = new TrafficSnapshot(1.0, 2, activeIds);

        // Modify original set
        activeIds.add(999);

        // Snapshot should not be affected
        Set<Integer> snapshotIds = snapshot.getActiveSourceIds();
        assertFalse("Snapshot should not be modified by external changes",
                snapshotIds.contains(999));
    }

    @Test
    public void testDefensiveCopyOnOutput() {
        Set<Integer> activeIds = new HashSet<>();
        activeIds.add(0);
        activeIds.add(1);

        TrafficSnapshot snapshot = new TrafficSnapshot(1.0, 2, activeIds);

        // Try to modify returned set
        Set<Integer> returned = snapshot.getActiveSourceIds();
        try {
            returned.add(999);
            // Should not be modifiable
            fail("Returned set should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // Expected - returned set is unmodifiable
            assertTrue(true);
        }
    }

    @Test
    public void testImmutability() {
        Set<Integer> activeIds = new HashSet<>();
        activeIds.add(0);

        TrafficSnapshot snapshot1 = new TrafficSnapshot(1.0, 2, activeIds);
        TrafficSnapshot snapshot2 = new TrafficSnapshot(1.0, 2, activeIds);

        // Should be different objects but same data
        assertNotSame(snapshot1, snapshot2);
        assertEquals(snapshot1.getTimestamp(), snapshot2.getTimestamp(), 0.001);
        assertEquals(snapshot1.getTrafficRate(), snapshot2.getTrafficRate());
    }

    @Test
    public void testEmptyActiveIds() {
        Set<Integer> activeIds = new HashSet<>();  // Empty!

        TrafficSnapshot snapshot = new TrafficSnapshot(1.0, 0, activeIds);

        assertEquals(0, snapshot.getTrafficRate());
        assertTrue(snapshot.getActiveSourceIds().isEmpty());
    }

    @Test
    public void testManyActiveSources() {
        Set<Integer> activeIds = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            activeIds.add(i);
        }

        TrafficSnapshot snapshot = new TrafficSnapshot(1.0, 100, activeIds);

        assertEquals(100, snapshot.getActiveSourceIds().size());
    }

    @Test
    public void testZeroTimestamp() {
        Set<Integer> activeIds = new HashSet<>();
        TrafficSnapshot snapshot = new TrafficSnapshot(0.0, 0, activeIds);

        assertEquals(0.0, snapshot.getTimestamp(), 0.001);
    }

    @Test
    public void testLargeTimestamp() {
        Set<Integer> activeIds = new HashSet<>();
        TrafficSnapshot snapshot = new TrafficSnapshot(1000000.0, 0, activeIds);

        assertEquals(1000000.0, snapshot.getTimestamp(), 0.001);
    }

    @Test
    public void testActiveSourceCount(){
        Set<Integer> activeIds = new HashSet<>();
        activeIds.add(0);
        activeIds.add(5);
        activeIds.add(10);

        TrafficSnapshot snapshot = new TrafficSnapshot(1.0, 3L, activeIds);

        assertEquals(3, snapshot.getActiveSourceCount());
    }
}
