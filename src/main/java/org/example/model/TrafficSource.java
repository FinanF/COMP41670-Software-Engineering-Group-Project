package org.example.model;

import org.example.simulation.EventQueue;

/**
 * Represents a single ON/OFF traffic source in the simulation.
 */
public class TrafficSource {

    private final int id;
    private SourceState state;
    private final ParetoDistribution onDist;
    private final ParetoDistribution offDist;

    public TrafficSource(int id, ParetoDistribution onDist, ParetoDistribution offDist, boolean startOn) {
        this.id = id;
        this.onDist = onDist;
        this.offDist = offDist;
        this.state = startOn ? SourceState.ON : SourceState.OFF;
    }

    public int getId() { return id; }
    public boolean isOn() { return state == SourceState.ON; }
    public SourceState getState() { return state; }

    public void processEvent(Event e, double currentTime, EventQueue queue) {
        if (e.getEventType() == EventType.SOURCE_TURNS_ON) {
            state = SourceState.ON;
            double offDelay = onDist.TimeDuration();
            queue.enqueue(new Event(currentTime + offDelay, EventType.SOURCE_TURNS_OFF, id));
        }
        else if (e.getEventType() == EventType.SOURCE_TURNS_OFF) {
            state = SourceState.OFF;
            double onDelay = offDist.TimeDuration();
            queue.enqueue(new Event(currentTime + onDelay, EventType.SOURCE_TURNS_ON, id));
        }
    }

    public void scheduleInitialEvent(EventQueue queue, double startTime) {
        if (state == SourceState.ON) {
            double delay = onDist.TimeDuration();
            queue.enqueue(new Event(startTime + delay, EventType.SOURCE_TURNS_OFF, id));
        } else {
            double delay = offDist.TimeDuration();
            queue.enqueue(new Event(startTime + delay, EventType.SOURCE_TURNS_ON, id));
        }
    }
}
