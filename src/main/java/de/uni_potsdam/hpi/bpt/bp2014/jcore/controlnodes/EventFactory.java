package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

/**
 * This class hides the logic of dispatching the type of event, by returning the appropriate
 * subclass.
 */
public class EventFactory {
    private final ScenarioInstance scenarioInstance;

    public EventFactory(ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
    }

    /**
     *
     * @param dbControlNodeInstanceId
     * @return
     */
    public AbstractEvent getEventForInstanceId(int dbControlNodeInstanceId) {
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        int controlNodeId = dbControlNodeInstance.getControlNodeID(dbControlNodeInstanceId);
        int fragmentInstanceId = dbControlNodeInstance.getFragmentInstanceId(
                dbControlNodeInstanceId);
        return this.getEventForControlNodeId(controlNodeId, fragmentInstanceId);
    }

    public AbstractEvent getEventForControlNodeId(
            Integer controlNodeId, Integer fragmentInstanceId) {
        DbControlNode dbControlNode = new DbControlNode();
        String eventType = dbControlNode.getType(controlNodeId);
        switch (eventType) {
            case "IntermediateEvent": return new IntermediateEvent(
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
