package org.example.model;

/**
 * Enumeration representing the types of events that can occur in the simulation.
 * Each event represents a state transition for a traffic source.
 *
 * @author Jordan Shodipo
 * @version 1.0
 */
public enum EventType {
    /**
     * This event indicates the source starts generating traffic.
     */
    SOURCE_TURNS_ON,

    /**
     * This event indicates the source stops generating traffic.
     */
    SOURCE_TURNS_OFF
}
