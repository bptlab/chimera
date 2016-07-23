package de.hpi.bpt.chimera.jcore.controlnodes;

import de.hpi.bpt.chimera.database.data.DbDataFlow;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.data.DataManager;

import java.util.List;
import java.util.Map;

/**
 * Receive activities are used to represent events that also change the states of data
 * objects.
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

    /**
     * Terminates a ReceiveActivity.
     * When terminating a , the terminating behavior of the
     * {@link AbstractEvent} is executed setting the data attribute values from the
     * json.
     *
     * Additional to this, the
     *
     * @param eventJson the json string containing the values.
     */
    @Override
    public void terminate(String eventJson) {
        super.terminate(eventJson);
        List<Integer> outputSets = new DbDataFlow().getOutputSetsForControlNode(
                this.getControlNodeId());
        assert outputSets.size() == 1 : "Receive tasks should have exactly one output set.";
        int outputSetId = outputSets.get(0);
        Map<Integer, Integer> idToState = new DbDataNode().getDataSetClassToStateMap(outputSetId);
        // TODO use data object id instead of data class id also test this
        for (Map.Entry<Integer, Integer> entry : idToState.entrySet()) {
            this.changeDataObjectInstanceState(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Change the state of the given data object to the new state.
     *
     * @param dataObjectId This is the database id from the data object
     * @param stateId      The state the data object should be set to
     */
    private void changeDataObjectInstanceState(int dataObjectId, int stateId) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        dataManager.changeDataObjectState(dataObjectId, stateId,
                this.getControlNodeInstanceId());
    }
}
