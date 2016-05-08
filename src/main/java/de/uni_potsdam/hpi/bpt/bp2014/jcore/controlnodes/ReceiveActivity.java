package de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.LinkedList;
import java.util.List;

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
         List<Integer> outputSets = new DbDataFlow().getOutputSetsForControlNode(
                this.getControlNodeId());
        assert outputSets.size() == 1: "Receive tasks should have exactly one output set.";
        int outputSet = outputSets.get(0);
        List<DataObject> dataObjects = new DbDataNode().getDataObjectsForDataSets(outputSet);
        for (DataObject dataObject : dataObjects) {
            this.changeDataObjectInstanceState(
                    dataObject.getId(), dataObject.getStateID());
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
        dataManager.changeDataObjectInstanceState(dataObjectId, stateId,
                this.getControlNodeInstanceId());
    }
}
