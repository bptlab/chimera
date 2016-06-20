package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;


import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors.SendEventOutgoingBehavior;

public class IntermediateThrowEvent extends AbstractControlNodeInstance {

    private final int controlNodeId;
    private final String type = "IntermediateThrowEvent";

    public IntermediateThrowEvent(int controlNodeId, int fragmentInstanceId,
                                  ScenarioInstance scenarioInstance) {
        this.controlNodeId = controlNodeId;
        this.scenarioInstance = scenarioInstance;
        this.setFragmentInstanceId(fragmentInstanceId);

        DbControlNodeInstance databaseNodeInstance = new DbControlNodeInstance();
        if (!databaseNodeInstance.existControlNodeInstance(controlNodeId, fragmentInstanceId)) {
            int controlNodeInstanceId = databaseNodeInstance.createNewControlNodeInstance(
                    controlNodeId, this.type, fragmentInstanceId);
            this.setControlNodeInstanceId(controlNodeInstanceId);
        } else {
            this.setControlNodeInstanceId(databaseNodeInstance.getControlNodeInstanceID(
                    controlNodeId, fragmentInstanceId));
        }

        this.setOutgoingBehavior(
                new SendEventOutgoingBehavior(scenarioInstance, getControlNodeId(), getControlNodeInstanceId()));
    }

    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        getOutgoingBehavior().terminate();
        return true;
    }
}
