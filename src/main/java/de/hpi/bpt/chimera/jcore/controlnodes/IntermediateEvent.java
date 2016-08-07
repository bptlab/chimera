package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;

/**
 * Representation of an intermediate event.
 */
public class IntermediateEvent extends AbstractEvent {

    public IntermediateEvent(int controlNodeId, int fragmentInstanceId,
                             ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
    }

    public IntermediateEvent(int controlNodeId, int fragmentInstanceId,
                             ScenarioInstance scenarioInstance, int controlNodeInstanceId) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance, controlNodeInstanceId);
    }

    @Override
    public String getType() {
        return "IntermediateCatchEvent";
    }
}
