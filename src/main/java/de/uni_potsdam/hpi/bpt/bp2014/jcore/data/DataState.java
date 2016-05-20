package de.uni_potsdam.hpi.bpt.bp2014.jcore.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.*;

/**
 *
 */
public class DataState {

    private List<DataObject> dataObjects = new ArrayList<>();

    public DataState(ScenarioInstance scenarioInstance) {
        DbDataObject dbDataObject = new DbDataObject();
        List<Integer> dataObjectIds = dbDataObject.getDataObjectIds(
                scenarioInstance.getScenarioInstanceId());
        for (Integer dataObjectId : dataObjectIds) {
            this.dataObjects.add(new DataObject(dataObjectId, scenarioInstance));
        }
    }

    /**
     * Returns the states of data objects for a scenario instance id.
     *
     * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
     */
    public Map<Integer, Set<Integer>> getAvailableDataClassStates() {
        Map<Integer, Set<Integer>> dataclassToStates = new HashMap<>();
        for (DataObject dataObject : this.dataObjects) {
            // If the dataobject is locked it can not be used by other activities
            if (dataObject.isLocked()) {
                continue;
            }
            int dataClassId = dataObject.getDataClassId();
            if (!dataclassToStates.containsKey(dataClassId)) {
                dataclassToStates.put(dataClassId, new HashSet<>());
            }
            dataclassToStates.get(dataClassId).add(dataObject.getStateId());
        }
        return dataclassToStates;
    }



    public boolean checkInputSet(int inputSetId) {
        DbDataNode dbDataNode = new DbDataNode();
        Map<Integer, Set<Integer>> dataClassIdToStateId = getAvailableDataClassStates();
        DataSet inputSet = dbDataNode.getInputSetFor(inputSetId);
        for (Map.Entry<Integer, Integer> cond : inputSet.getDataclassToState().entrySet()) {
            assert dataClassIdToStateId.containsKey(cond.getKey()) : "Invalid dataclass id";
            Set<Integer> availableStates = dataClassIdToStateId.get(cond.getKey());
            if (!(availableStates.contains(cond.getValue()))) {
                return false;
            }
        }
        return true;
    }


    public List<DataObject> getDataObjects() {
        return dataObjects;
    }
}
