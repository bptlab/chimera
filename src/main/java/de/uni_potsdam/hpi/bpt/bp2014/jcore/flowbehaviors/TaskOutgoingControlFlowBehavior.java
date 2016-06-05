package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbSelectedDataObjects;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handles the behavior of a terminating activity instance.
 */
public class TaskOutgoingControlFlowBehavior extends AbstractParallelOutgoingBehavior {
	private final ActivityInstance activityInstance;
    /**
	 * Initializes the TaskOutgoingControlFlowBehavior.
	 *
	 * @param activityId         This is the database id from the activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param activityInstance 	This is an AbstractControlNodeInstance.
	 */
	public TaskOutgoingControlFlowBehavior(int activityId, ScenarioInstance scenarioInstance,
			int fragmentInstanceId, ActivityInstance activityInstance) {
		this.setControlNodeId(activityId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.activityInstance = activityInstance;
	}

	@Override public void terminate() {
		this.terminate(new HashMap<>());
	}

	/**
	 * Terminates the activity.
	 *
	 * @param dataClassNameToStateName Map from name of the data class to new state
	 */
	public void terminate(Map<String, String> dataClassNameToStateName) {
        DbDataFlow dataFlow = new DbDataFlow();
        List<Integer> inputClassIds = dataFlow.getPrecedingDataClassIds(this.getControlNodeId());
        List<Integer> outputClassIds = dataFlow.getFollowingDataClassIds(this.getControlNodeId());
        Set<Integer> toCreate = new HashSet<>(outputClassIds);
        toCreate.removeAll(inputClassIds);
        if (dataClassNameToStateName.isEmpty()) {
            dataClassNameToStateName = loadOnlyOutputSet();
        }

        createDataObjects(toCreate, dataClassNameToStateName);

        List<DataObject> usedDataObjects = getUsedDataobjects();
        List<DataObject> toUpdate = usedDataObjects.stream().filter(outputClassIds::contains)
                .collect(Collectors.toList());

        updateDataObjects(dataClassNameToStateName, toUpdate);
        usedDataObjects.forEach(DataObject::unlock);

        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
        scenarioInstance.checkXorGatewaysForTermination(this.getControlNodeId());
		this.enableFollowing();
		this.runAutomaticTasks();
	}

    private void createDataObjects(Set<Integer> toCreate, Map<String, String> dataClassNameToStateName) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Map<Integer, Integer> dataClassIdToStateId =
                dataManager.translate(dataClassNameToStateName);
        for (int classId : toCreate) {
            dataManager.initializeDataObject(
                    classId, dataClassIdToStateId.get(classId));
        }
    }

    private void updateDataObjects(Map<String, String> dataClassNameToStateName,
                                   List<DataObject> toUpdate){
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Map<Integer, Integer> dataClassIdToStateId = translate(
                toUpdate, dataClassNameToStateName);

        int controlNodeInstanceId = activityInstance.getControlNodeInstanceId();
        Map<Integer, Integer> dataClassToSelectedObject = getClassToSelectedObjectIdMap();

        for (Map.Entry<Integer, Integer> entry : dataClassIdToStateId.entrySet()) {
            dataManager.changeDataObjectState(
                    dataClassToSelectedObject.get(entry.getKey()), entry.getValue(),
                    controlNodeInstanceId);
        }
    }


    /**
     * This method is used to load the default output of an activity.
     * If there is more than one possible output set an IllegalArgumentException
     * is thrown.
     * @return Map from data class id to state id, if the dataobject has only one possible output set
     */
    private Map<String, String> loadOnlyOutputSet() {
        DbDataFlow dbDataFlow = new DbDataFlow();
        List<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(
                this.getControlNodeId());
        if (outputSets.size() > 1) {
            throw new IllegalArgumentException("Should only be used when there are no "
                    + "alternative output sets.");
        }
        DbDataConditions dataConditions = new DbDataConditions();
        Map<String, Set<String>> outputMapWithSet = dataConditions.loadOutputSets(this.getControlNodeId());
        Map<String, String> outputMapForOnlyOutput = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : outputMapWithSet.entrySet()) {
            assert 1 == entry.getValue().size();
            outputMapForOnlyOutput.put(entry.getKey(), entry.getValue().iterator().next());
        }
        return outputMapForOnlyOutput;
    }

    /**
     * Returns a Map from the dataclass to the object that is selected to use
     * for this specific dataclass
     */
    private Map<Integer, Integer> getClassToSelectedObjectIdMap() {
        int controlNodeInstanceId = activityInstance.getControlNodeInstanceId();
        DbSelectedDataObjects dbSelection = new DbSelectedDataObjects();
        DbDataObject dbDataObject = new DbDataObject();
        int scenarioInstanceId = this.getScenarioInstance().getScenarioInstanceId();
        List<Integer> dataObjectSelection = dbSelection.getDataObjectSelection(
                scenarioInstanceId, controlNodeInstanceId);
        return dataObjectSelection.stream().collect(Collectors.toMap(
                dbDataObject::getDataClassId, Function.identity()
        ));
    }

    /**
     * Converts map from data class name to state name to it's respective database
     * ids.
     * @param dataObjects
     * @param dataClassNameToStateName
     * @return
     */
    private Map<Integer, Integer> translate(List<DataObject> dataObjects,
            Map<String, String> dataClassNameToStateName) {
        DbDataObject dbDataObject = new DbDataObject();
        DbState dbState = new DbState();
        Map<Integer, Integer> dataclassIdToStateId = new HashMap<>();
        for (DataObject dataObject : dataObjects) {
            String name = dbDataObject.getName(dataObject.getId());
            String stateToSet = dataClassNameToStateName.get(name);
            Integer stateId = dbState.getStateId(dataObject.getDataClassId(), stateToSet);
            dataclassIdToStateId.put(dataObject.getDataClassId(), stateId);
        }
        return dataclassIdToStateId;
    }

    /**
     * @return List of the data objects on which the activity works
     */
    private List<DataObject> getUsedDataobjects() {
        DbSelectedDataObjects dbSelectedDataObjects = new DbSelectedDataObjects();
        ScenarioInstance instance = this.getScenarioInstance();
        Set<Integer> workItems =  new HashSet<>(dbSelectedDataObjects.getDataObjectSelection(
                instance.getScenarioInstanceId(), this.activityInstance.getControlNodeInstanceId()));
        List<DataObject> dataObjects = instance.getDataManager().getDataObjects();
        return dataObjects.stream().filter(x -> workItems.contains(x.getId())).collect(
                Collectors.toList());
    }

    /**
     * Since at the moment each output set can only contain the same data objects with different
     * states it is enough to look only at one output set and free all data objects in this.
     */
    public void cancel() {
        DbSelectedDataObjects dbSelectedDataObjects = new DbSelectedDataObjects();
        List<Integer> usedDataObjects = dbSelectedDataObjects.getDataObjectSelection(
                this.getScenarioInstance().getScenarioInstanceId(), this.getControlNodeId());
        usedDataObjects.forEach(this::unlockDataObject);
    }


	/**
	 * Change the state of the given data object.
	 *
	 * @param dataObjectId This is the database id from the data object.
	 * @param stateId      This is the database id from the state.
	 * @return true if the data object state could been changed. false if not
	 */
	public Boolean changeDataObjectState(int dataObjectId, int stateId) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        return dataManager.changeDataObjectState(dataObjectId, stateId,
                activityInstance.getControlNodeInstanceId());
	}

	/**
	 * Sets the data object to not on change.
	 * @param dataObjectId This is the database id from the data object.
	 */
	public void unlockDataObject(int dataObjectId) {
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Optional<DataObject> dataObjectInstance =
                dataManager.getDataobjectForId(dataObjectId);
        assert dataObjectInstance.isPresent(): "invalid data object id";
        dataObjectInstance.get().unlock();
	}
}
