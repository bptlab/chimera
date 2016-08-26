package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.database.controlnodes.DbBoundaryEvent;
import de.hpi.bpt.chimera.database.data.*;
import de.hpi.bpt.chimera.database.history.DbLogEntry;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.eventhandling.EventDispatcher;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Handles the behavior of a terminating activity instance.
 */
public class TaskOutgoingBehavior extends AbstractParallelOutgoingBehavior {
	private final ActivityInstance activityInstance;
    /**
	 * Initializes the TaskOutgoingBehavior.
	 *
	 * @param activityId         This is the database id from the activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 * @param activityInstance 	This is an AbstractControlNodeInstance.
	 */
	public TaskOutgoingBehavior(int activityId, ScenarioInstance scenarioInstance,
                                int fragmentInstanceId, ActivityInstance activityInstance) {
		this.setControlNodeId(activityId);
		this.setScenarioInstance(scenarioInstance);
		this.setFragmentInstanceId(fragmentInstanceId);
		this.activityInstance = activityInstance;
	}


    /**
     * Terminates a running ActivityInstance without specifying the states, to which
     * the data objects used by the ActivityInstance are set to.
     *
     * TODO check whether this works when there is more than one possible output
     */
    @Override public void terminate() {
        this.terminate(new HashMap<>());
    }

    @Override
    public void skip() {
        new DbLogEntry().logActivity(
                this.activityInstance.getControlNodeInstanceId(), "skipped",
                this.getScenarioInstance().getId());
    }

    /**
     * Terminates a running ActivityInstance.
     * Enables the following control nodes and sets the data outputs, according to the
     * passed specification.
     *
     * @param dataClassNameToStateName the specification for each data object to
     *                                 which state it should be set.
     */
    public void terminate(Map<String, String> dataClassNameToStateName) {
        int scenarioInstanceId = this.getScenarioInstance().getId();
        new DbLogEntry().logActivity(
                this.activityInstance.getControlNodeInstanceId(), "terminated", scenarioInstanceId);
        activityInstance.setState(State.TERMINATED);
        cancelAttachedEvents();
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
        List<DataObject> toUpdate = usedDataObjects.stream().filter(
                x ->  outputClassIds.contains(x.getDataClassId())).collect(Collectors.toList());

        updateDataObjects(dataClassNameToStateName, toUpdate);
        usedDataObjects.forEach(DataObject::unlock);

        ScenarioInstance scenarioInstance = this.getScenarioInstance();
        scenarioInstance.updateDataFlow();
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

    // TODO Maybe pass to update instead
    private void updateDataObjects(Map<String, String> dataClassNameToStateName,
                                   List<DataObject> toUpdate){
        DataManager dataManager = this.getScenarioInstance().getDataManager();
        Map<Integer, Integer> dataClassIdToStateId = translate(
                this.getScenarioInstance().getScenarioId(), dataClassNameToStateName);

        int controlNodeInstanceId = activityInstance.getControlNodeInstanceId();
        Map<Integer, Integer> dataClassToSelectedObject = getClassToSelectedObjectIdMap();

        Set<Integer> usedDataclassIds = toUpdate.stream().map(DataObject::getDataClassId)
                .collect(Collectors.toSet());
        for (Map.Entry<Integer, Integer> entry : dataClassToSelectedObject.entrySet()) {
            if (!usedDataclassIds.contains(entry.getKey())) {
                continue;
            }
            int dataclassId = entry.getKey();
            int dataobjectId = dataClassToSelectedObject.get(dataclassId);
            int stateId = dataClassIdToStateId.get(dataclassId);
            dataManager.changeDataObjectState(dataobjectId, stateId, controlNodeInstanceId);
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
        int scenarioInstanceId = this.getScenarioInstance().getId();
        List<Integer> dataObjectSelection = dbSelection.getDataObjectSelection(
                scenarioInstanceId, controlNodeInstanceId);
        return dataObjectSelection.stream().collect(Collectors.toMap(
                dbDataObject::getDataClassId, Function.identity()
        ));
    }

    /**
     * Converts map from data class name to state name to it's respective database
     * ids.
     * @param scenarioId This id is needed because the scope of a data class name is only
     *                   unique in one scenario
     * @param dataClassNameToStateName
     * @return
     */
    private Map<Integer, Integer> translate(int scenarioId,
            Map<String, String> dataClassNameToStateName) {
        DbDataClass dbDataClass = new DbDataClass();
        DbState dbState = new DbState();
        Map<Integer, Integer> dataclassIdToStateId = new HashMap<>();
        for (Map.Entry<String, String> class_state : dataClassNameToStateName.entrySet()) {
            String name = class_state.getKey();
            int dataclassId = dbDataClass.getId(name, scenarioId);
            String stateToSet = dataClassNameToStateName.get(name);
            Integer stateId = dbState.getStateId(dataclassId, stateToSet);
            dataclassIdToStateId.put(dataclassId, stateId);
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
                instance.getId(), this.activityInstance.getControlNodeInstanceId()));
        List<DataObject> dataObjects = instance.getDataManager().getDataObjects();
        return dataObjects.stream().filter(x -> workItems.contains(x.getId())).collect(
                Collectors.toList());
    }

    /**
     * Unlocks all data objects bound to the activity instance.
     * Since at the moment each output set can only contain the same data objects with different
     * states it is enough to look only at one output set and free all data objects in this.
     */
    public void cancel() {
        DbSelectedDataObjects dbSelectedDataObjects = new DbSelectedDataObjects();
        List<Integer> usedDataObjects = dbSelectedDataObjects.getDataObjectSelection(
                this.getScenarioInstance().getId(), this.getControlNodeId());
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
                dataManager.getDataObjectForId(dataObjectId);
        assert dataObjectInstance.isPresent(): "invalid data object id";
        dataObjectInstance.get().unlock();
	}

    private void cancelAttachedEvents() {
        DbBoundaryEvent boundaryEventDao = new DbBoundaryEvent();
        int boundaryEventId = boundaryEventDao.getBoundaryEventForActivity(this.getControlNodeId());
        // if activity has attached event
        if (boundaryEventId > 0) {
            EventDispatcher.unregisterEvent(boundaryEventId, this.getFragmentInstanceId());
        }
    }
}
