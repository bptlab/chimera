package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ReceiveActivity extends AbstractEvent {

    public ReceiveActivity(int controlNodeId, int fragmentInstanceId,
                           ScenarioInstance scenarioInstance) {
        super(controlNodeId, fragmentInstanceId, scenarioInstance);
        this.setFragmentInstanceId(fragmentInstanceId);
        scenarioInstance.getControlNodeInstances().add(this);
    }

    @Override
    public String getType() {
        return "ReceiveActivity";
    }


    @Override
    public boolean skip() {
        return false;
    }

    @Override
    public boolean terminate() {
        return this.terminate("");
    }

    @Override
    public boolean terminate(String eventJson) {
        super.terminate(eventJson);
        List<Integer> outputSets = new DbDataFlow().getOutputSetsForControlNode(
                this.getControlNodeId());
        assert outputSets.size() == 1 : "Receive tasks should have exactly one output set.";
        int outputSetId = outputSets.get(0);
        Map<Integer, Integer> idToState = new DbDataNode().getDataObjectIdToState(outputSetId);
        for (Map.Entry<Integer, Integer> entry : idToState.entrySet()) {
            this.changeDataObjectInstanceState(
                    entry.getKey(), entry.getValue());
        }
        return true;
    }

    /**
     * Change the state of the given data object to the new state.
     *
     * @param dataObjectId This is the database id from the data object
     * @param stateId      The state the data object should be set to
     */
    private void changeDataObjectInstanceState(int dataObjectId, int stateId) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        // TODO change this to new structure
        dataManager.changeDataObjectInstanceState(dataObjectId, stateId,
                this.getControlNodeInstanceId());
    }
}
