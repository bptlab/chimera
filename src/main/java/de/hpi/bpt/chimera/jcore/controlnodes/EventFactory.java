package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;

/**
 * This class encapsulates the logic of dispatching the type of event, by returning the appropriate
 * subclass.
 */
public class EventFactory {
    private final ScenarioInstance scenarioInstance;

    public EventFactory(ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
    }

    /**
     * Creates a new event for the given control node id.
     * @param controlNodeId the id for the control node, which should be instantiated.
     * @param fragmentInstanceId the id of the fragment the control node instance id should be
     *                           stored in.
     * @return The event instance for the given control node id.
     */
    public AbstractEvent getEventForControlNodeId(
            Integer controlNodeId, Integer fragmentInstanceId) {
        DbControlNode dbControlNode = new DbControlNode();
        String eventType = dbControlNode.getType(controlNodeId);
        switch (eventType) {
            case "IntermediateCatchEvent": return new IntermediateEvent(
                    controlNodeId, fragmentInstanceId, this.scenarioInstance);
            case "BoundaryEvent": return new BoundaryEvent(
                    controlNodeId, fragmentInstanceId, this.scenarioInstance);
            case "StartEvent": return new StartEvent(
                    controlNodeId, fragmentInstanceId, this.scenarioInstance);
            case "TimerEvent": return new TimerEventInstance(
                    controlNodeId, fragmentInstanceId, this.scenarioInstance);
            case "ReceiveActivity": return new ReceiveActivity(
                    controlNodeId, fragmentInstanceId, this.scenarioInstance);
            default: throw new IllegalArgumentException("Unsupported Event Type");
        }
    }
}
