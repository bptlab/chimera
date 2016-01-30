package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.EventOutgoingBehavior;

/**
 *
 */
public class IntermediateEvent extends AbstractEvent {
    private final ScenarioInstance scenarioInstance;

    public IntermediateEvent(int controlNodeId, int fragmentInstanceId,
                             ScenarioInstance scenarioInstance) {
        super(controlNodeId);
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);

        scenarioInstance.getControlNodeInstances().add(this);
        saveToDatabase();
    }


    private void saveToDatabase() {
        DbControlNode dbControlNode = new DbControlNode();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();

        this.setControlNodeInstanceId(dbControlNodeInstance
                .createNewControlNodeInstance(this.getControlNodeId(), "IntermediateEvent",
                        this.getFragmentInstanceId()));

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
