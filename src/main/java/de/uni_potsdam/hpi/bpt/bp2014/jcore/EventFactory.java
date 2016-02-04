package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;

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
    public AbstractEvent getEventForId(int dbControlNodeInstanceId) {
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        int controlNodeId = dbControlNodeInstance.getControlNodeID(dbControlNodeInstanceId);
        int fragmentInstanceId = dbControlNodeInstance.getFragmentInstanceId(
                dbControlNodeInstanceId);
        DbControlNode dbControlNode = new DbControlNode();
        String eventType = dbControlNode.getType(controlNodeId);
        if ("IntermediateEvent".equals(eventType)) {
            return new IntermediateEvent(controlNodeId, fragmentInstanceId, this.scenarioInstance);
        } else if ("BoundaryEvent".equals(eventType)) {
            return new BoundaryEvent(controlNodeId, fragmentInstanceId, this.scenarioInstance);
        } else {
            throw new IllegalArgumentException("Unsupported Event Type");
        }
    }
}
