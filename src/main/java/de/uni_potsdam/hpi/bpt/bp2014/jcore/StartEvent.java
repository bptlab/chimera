package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventOutgoingBehavior;

/**
 *
 */
public class StartEvent extends AbstractEvent {

    /**
     * @param controlNodeId id of the abstract control node which represents the event.
     * @param fragmentInstanceId databaseId of the Fragment where start event belongs to.
     * @param scenarioInstance ScenarioInstance object which holds control nodes.
     */
    public StartEvent(int controlNodeId, int fragmentInstanceId,
                      ScenarioInstance scenarioInstance) {
        super(controlNodeId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        EventOutgoingBehavior outgoingBehavior = new EventOutgoingBehavior(this.getControlNodeId(),
                this.scenarioInstance, this.getFragmentInstanceId());
        outgoingBehavior.terminate();
        return true;
    }

}
