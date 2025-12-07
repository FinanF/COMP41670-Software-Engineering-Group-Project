package org.example.model;

/**
 * Represents a discrete event in the simulation.
 * An event marks a point in time when a source changes from OFF to ON or vice versa.
 * Events are immutable and ordered by timestamp.
 * This class implements Comparable to enable automatic ordering in a PriorityQueue,
 * ensuring events are always processed in chronological order.
 */
public class Event implements Comparable<Event>{
    private final double timestamp;      // When this event occurs (simulation time)
    private final EventType eventType;   // What type of event (SOURCE_TURNS_ON or OFF)
    private final int sourceId;          // Which traffic source this event affects

    /**
     * Creates a new Event with the specified parameters.
     */
    public Event(double timestamp, EventType eventType, int sourceId) {
        if (timestamp < 0) {
            throw new IllegalArgumentException("Timestamp must be non-negative");
        }
        if (sourceId < 0) {
            throw new IllegalArgumentException("Source ID must be non-negative");
        }
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        this.timestamp = timestamp;
        this.eventType = eventType;
        this.sourceId = sourceId;
    }

    /**
     * Compares this event with another event for ordering.
     * Events are ordered primarily by timestamp (earliest first).
     * If two events have the same timestamp, they are ordered by source ID
     * This method is used by PriorityQueue to maintain the min-heap property,
     * ensuring the earliest event is always dequeued first.
     *
     * @param other the other event to compare to
     * @return negative if this event occurs before other,
     *         positive if this event occurs after other,
     *         zero if they occur at the same time
     */
    @Override
    public int compareTo(Event other) {
        // First compare by timestamp
        if (this.timestamp < other.timestamp) {
            return -1;
        }
        if (this.timestamp > other.timestamp) {
            return 1;
        }

        // If timestamps are equal, compare by source ID
        return Integer.compare(this.sourceId, other.sourceId);
    }

    // GETTER METHODS
    /**
     * Gets the timestamp (simulation time) of this event.
     */
    public double getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the type of this event.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the ID of the traffic source involved in this event.
     */
    public int getSourceId() {
        return sourceId;
    }

    // UTILITY METHODS

    /**
     * Compares this event with another object for equality.
     * Two events are equal if they have the same timestamp, event type, and source ID.
     *
     * @param o the object to compare to
     * @return true if o is an Event with the same timestamp, type, and source ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;

        return Double.compare(event.timestamp, timestamp) == 0 &&
                eventType == event.eventType &&
                sourceId == event.sourceId;
    }

    /**
     * Returns the hash code of this event.
     */
    @Override
    public int hashCode() {
        return Double.hashCode(timestamp) * 31 +
                eventType.hashCode() * 31 +
                sourceId;
    }

    /**
     * Returns a string representation of this event.
     * Format: Event(timestamp=T, type=TYPE, sourceId=ID)
     *
     * @return a string representation of this event
     */
    @Override
    public String toString() {
        return String.format("Event(timestamp=%.4f, type=%s, sourceId=%d)",
                timestamp, eventType, sourceId);
    }

}
