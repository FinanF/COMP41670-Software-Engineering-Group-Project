package org.example.model;

import org.example.distribution.ParetoDistribution;

/**
 * Represents a single traffic source with ON/OFF state behavior.
 * A TrafficSource is a state machine that alternates between ON and OFF states.
 * When transitioning between states, it samples a duration from a Pareto distribution.
 * State Machine:
 *   OFF --[sample ON_duration]--> ON --[sample OFF_duration]--> OFF
 * When processEvent() is called:
 *   - Source toggles to opposite state
 *   - Samples duration for next state
 *   - Returns timestamp of next event
 * This class models individual traffic source behavior. Many TrafficSources
 * are aggregated together to create self-similar traffic patterns.
 */
public class TrafficSource {

    private int sourceId;                          // Unique identifier
    private SourceState state;                     // Current state (ON or OFF)
    private double timeRemainingInState;           // Countdown timer
    private ParetoDistribution paretoDistribution; // Random duration sampler

    public TrafficSource(int sourceId, ParetoDistribution pd){
        this.sourceId = sourceId;
        this.paretoDistribution = pd;
        this.state = SourceState.OFF;
    }

}
