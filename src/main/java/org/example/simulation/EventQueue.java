package org.example.simulation;
import org.example.model.Event;
import java.util.PriorityQueue;
/**
 * Manages a queue of events, ensuring they are processed in chronological order.
 * This is a simple wrapper around Java's PriorityQueue<Event> that:
 *   - Maintains events in min-heap order (earliest first)
 *   - Provides event-driven simulation interface
 *   - Hides internal implementation details
 * Usage:
 *   EventQueue queue = new EventQueue();
 *   queue.enqueue(new Event(1.5, SOURCE_TURNS_ON, 0));
 *   queue.enqueue(new Event(3.2, SOURCE_TURNS_OFF, 1));
 *   queue.enqueue(new Event(2.0, SOURCE_TURNS_ON, 2));
 *   Event e1 = queue.dequeue(); // Returns event at 1.5 (earliest)
 *   Event e2 = queue.dequeue(); // Returns event at 2.0
 *   Event e3 = queue.dequeue(); // Returns event at 3.2
 */
public class EventQueue {
    private final PriorityQueue<Event> queue;

    public EventQueue() {
        // The PriorityQueue constructor with no arguments will use
        // the natural ordering (compareTo method) of Event objects
        this.queue = new PriorityQueue<>();
    }

    /**
     * Adds an event to the queue.
     */
    public void enqueue(Event event) {
        if (event == null) {
            throw new NullPointerException("Cannot enqueue null event");
        }
        queue.add(event);  // O(log n)
    }

    /**
     * Removes and returns the earliest event from the queue.
     * The event with the smallest timestamp is always removed first.
     */
    public Event dequeue() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Cannot dequeue from empty queue");
        }
        return queue.poll();  // O(log n)
    }

    /**
     * Views the earliest event without removing it.
     * Useful for checking what the next event will be without
     * consuming it. Takes O(1) time.
     */
    public Event peek() {
        return queue.peek();  // O(1)
    }

    // ===== QUERY METHODS =====

    /**
     * Checks if the queue is empty.
     *
     * @return true if queue contains no events, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Gets the number of events currently in the queue.
     */
    public int size() {
        return queue.size();
    }

    // ===== UTILITY METHODS =====

    /**
     * Returns a string representation of this queue.
     * Format: EventQueue(size=N)
     * Note: Does not print all events (would be inefficient with large queues)
     *
     * @return a string describing the queue
     */
    @Override
    public String toString() {
        return String.format("EventQueue(size=%d)", queue.size());
    }

}
