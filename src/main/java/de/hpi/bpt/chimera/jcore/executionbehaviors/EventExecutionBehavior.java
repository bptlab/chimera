package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.events.DbEvent;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractEvent;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.jcore.executionbehaviors.AbstractExecutionBehavior;

/**
 *
 */
public class EventExecutionBehavior extends AbstractExecutionBehavior {
    AbstractEvent event;

    public EventExecutionBehavior(AbstractEvent event) {
        this.event = event;
    }

    @Override
    public void begin() {
        DbEvent eventDao = new DbEvent();

        String queryString = eventDao.getQueryForControlNode(event.getControlNodeId());
        if (queryString.trim().isEmpty()) {
            event.terminate();
        } else {
            registerEvent();
        }
    }

    protected void registerEvent() {
        EventDispatcher.registerEvent(event, event.getFragmentInstanceId(),
                event.getScenarioInstance().getId(),
                event.getScenarioInstance().getScenarioId());
    }
}
