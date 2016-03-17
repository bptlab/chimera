package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbBoundaryEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

/**
 *
 */
public class BoundaryEventOutgoingBehavior extends EventOutgoingBehavior {
    public BoundaryEventOutgoingBehavior(int controlNodeId, ScenarioInstance scenarioInstance,
                                 int fragmentInstanceId) {
        super(controlNodeId, scenarioInstance, fragmentInstanceId);
    }

    @Override public void terminate() {
        DbBoundaryEvent boundaryEvent = new DbBoundaryEvent();
        int attachedControlNode = boundaryEvent.getControlNodeAttachedToEvent(
                this.getControlNodeId(), this.getFragmentInstanceId());
        ScenarioInstance scenario = this.getScenarioInstance();

        scenario.getControlNodeInstances();
        ActivityInstance attachedActivity = (ActivityInstance) scenario.getControlNodeInstanceWithId(
                attachedControlNode);
        attachedActivity.cancel();

        super.terminate();
    }

}
