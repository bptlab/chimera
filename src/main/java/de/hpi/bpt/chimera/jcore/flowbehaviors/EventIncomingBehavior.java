package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;

/**
 *
 */
public class EventIncomingBehavior extends AbstractIncomingBehavior {
    private final AbstractEvent event;

    public EventIncomingBehavior(AbstractEvent event) {
        this.event = event;
    }

    @Override
    public void enableControlFlow() {
        event.begin();
    }
}
