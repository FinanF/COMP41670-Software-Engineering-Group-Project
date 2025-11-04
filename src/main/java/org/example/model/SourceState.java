package org.example.model;

/**
 * Enumeration representing the state of a traffic source.
 * A traffic source can be in one of two states at any given time.
 *
 * @author Jordan Shodipo
 * @version 1.0
 */
public enum SourceState {
    /**
     * The traffic source is currently ON and generating traffic.
     */
    ON,

    /**
     * The traffic source is currently OFF and not generating traffic.
     */
    OFF
}
