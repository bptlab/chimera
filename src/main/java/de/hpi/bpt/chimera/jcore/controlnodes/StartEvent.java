package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;

/**
 * Representation for start events.
 */
public class StartEvent extends AbstractEvent {

    /**
     * @param controlNodeId id of the abstract control node which represents the event.
     * @param fragmentInstanceId databaseId of the Fragment where start event belongs to.
     * @param scenarioInstance ScenarioInstance object which holds control nodes.
     */
    public StartEvent(int controlNodeId, int fragmentInstanceId,
                      ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
    }

    @Override
    public String getType() {
        return "StartEvent";
    }
}
