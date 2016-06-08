package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbSelectedDataObjects;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataClass;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.data.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataManager;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataAttributeJaxBean;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all scenario instances.
 */
public class ExecutionService /*implements Runnable*/ {

	private static Logger log = Logger.getLogger(ExecutionService.class);
	/**
	 * Define the maximum allowable Map size.
	 */
	public static final int MAX_MAP_SIZE = 100;
	private static Map<Integer, ExecutionService> instances = new HashMap<>();
	private int scenarioId = -1;
	private boolean newVersionAvailable = true;

	/**
	 * This are the Lists for all opened scenario instances.
	 */
	private Map<Integer, ScenarioInstance> scenarioInstanceMap = new HashMap<>();
	private LinkedList<Integer> instanceHistory = new LinkedList<>();

	private final LinkedList<ScenarioInstance> scenarioInstances = new LinkedList<>();
	private final Map<Integer, ScenarioInstance> sortedScenarioInstances = new HashMap<>();
	/**
	 * Database Connection.
	 */
	private final DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
	private final DbScenario dbScenario = new DbScenario();
	private final DbControlNode dbControlNode = new DbControlNode();

	protected ExecutionService(int scenarioId) {
		this.scenarioId = scenarioId;
		log.info("Created a new ExecutionService for scenarioId = " + scenarioId);
	}

	/**
	 *
	 * @param scenarioId a scenario ID.
	 * @return the ExecutionService for the specified Instance.
	 */
	public static ExecutionService getInstance(int scenarioId) {
		ExecutionService instance = instances.get(scenarioId);
		if (instance == null) {
			instance = new ExecutionService(scenarioId);
			instances.put(scenarioId, instance);
			//    		instance.run();
		}
		//if there is a new version available, reload all instances from the database.
		//if (instance.newVersionAvailable) {
			instance.scenarioInstanceMap.clear();
			instance.instanceHistory.clear();
		//}
		return instance;
	}

	/**
	 * Starts a new scenario instance for the given scenario id.
	 *
	 * @return the id of the new scenario instance.
	 */
	public int startNewScenarioInstance() {
		ScenarioInstance scenarioInstance = new ScenarioInstance(this.scenarioId);
		scenarioInstances.add(scenarioInstance);
		//scenarioInstanceMap.put(scenarioInstance
		// 	.getScenarioInstanceId(), scenarioInstance);
		addScenarioInstanceToMap(scenarioInstance);
		sortedScenarioInstances.put(
				scenarioInstance.getScenarioInstanceId(), scenarioInstance);
		return scenarioInstance.getScenarioInstanceId();
	}

	/**
	 *
	 * @param scenarioId a scenario ID to instantiate.
	 * @return the ID of the new scenario instance.
	 */
	public static int startNewScenarioInstanceStatic(int scenarioId) {
		ExecutionService ex = ExecutionService.getInstance(scenarioId);
		return ex.startNewScenarioInstance();
	}

    public List<DataObjectJaxBean> getSelectedWorkingItems(int scenarioInstanceId, int activityInstanceId) {
        ScenarioInstance scenarioInstance = this.getScenarioInstance(scenarioInstanceId);
        DbSelectedDataObjects workItems = new DbSelectedDataObjects();
        List<Integer> dataObjectIds = workItems.getDataObjectSelection(
                scenarioInstanceId, activityInstanceId);
        List<DataObjectJaxBean> dataObjectJaxBeen = new ArrayList<>();
        for (Integer dataObjectId : dataObjectIds) {
            DataObject dataObject = new DataObject(dataObjectId, scenarioInstance);
            DataObjectJaxBean dataObjectJaxBean = new DataObjectJaxBean(dataObject, this);
            dataObjectJaxBeen.add(dataObjectJaxBean);
        }
        return dataObjectJaxBeen;
    }

	/**
	 * Open a existing scenario instance.
	 *
	 * @param scenarioId         This is the id of the scenario.
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return true if the scenario instance opened successfully (else false).
	 */
	public boolean openExistingScenarioInstance(int scenarioId, int scenarioInstanceId) {
		if (scenarioInstanceMap.containsKey(scenarioInstanceId)) {
			log.info("Open existing scenario instance with scenarioId=" + scenarioId
					+ " and scenarioInstanceId=" + scenarioInstanceId + ".");
		} else if (existScenarioInstance(scenarioId, scenarioInstanceId)) {
			log.info("Load existing scenario instance with scenarioId=" + scenarioId
					+ " and scenarioInstanceId="
					+ scenarioInstanceId + " from database.");
			ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId,
					scenarioInstanceId);
			scenarioInstances.add(scenarioInstance);
			sortedScenarioInstances.put(scenarioInstanceId, scenarioInstance);
			addScenarioInstanceToMap(scenarioInstance);
		} else {
			return false;
		}
		updateHistory(scenarioInstanceId);
		return true;
	}

	private void addScenarioInstanceToMap(ScenarioInstance scenarioInstance) {
		scenarioInstanceMap.put(scenarioInstance.getScenarioInstanceId(), scenarioInstance);
		updateHistory(scenarioInstance.getScenarioInstanceId());
		if (scenarioInstanceMap.size() > MAX_MAP_SIZE) {
			int oldestInstance = instanceHistory.removeLast();
			scenarioInstanceMap.remove(oldestInstance);
			log.info("Inserted scenario with ID="
					+ scenarioInstance.getScenarioInstanceId()
					+ " and removed scenario with ID=" + oldestInstance
					+ ". The History contains "
					+ instanceHistory.size() + " elements.");
		}
		newVersionAvailable = false;
	}

	/**
	 *
	 * @param scenarioId a scenario ID.
	 * @param scenarioInstanceId a scenario instance ID.
	 */
	public void reloadScenarioInstanceFromDatabase(int scenarioId, int scenarioInstanceId) {
		scenarioInstanceMap.remove(scenarioInstanceId);
		openExistingScenarioInstance(scenarioId, scenarioInstanceId);
	}

	/**
	 * Updates the scenario-history by removing the scenarioInstanceId first
	 * and then adding it at the first position of the LinkedList.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 */
	private void updateHistory(int scenarioInstanceId) {
		instanceHistory.remove((Object) scenarioInstanceId);
		instanceHistory.addFirst(scenarioInstanceId);
	}




	/**
	 * Checks if the scenario instance exist in the database.
	 *
	 * @param scenarioId         This is the id of the scenario.
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return true if the scenario instance exist in the database. false if not.
	 */
	public boolean existScenarioInstance(int scenarioId, int scenarioInstanceId) {
		return dbScenarioInstance.existScenario(scenarioId, scenarioInstanceId);
	}

	/**
	 * Checks if the scenario exist in the database.
	 *
	 * @param scenarioId This is the id of the scenario.
	 * @return true if the scenario exist in the database. false if not.
	 */
	public boolean existScenario(int scenarioId) {
		return dbScenario.existScenario(scenarioId);
	}


	/**
	 * Gives all activity ids for a scenario instance which are enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a list of the ids of activities.
	 */
	public LinkedList<Integer> getEnabledActivitiesIDsForScenarioInstance(
			int scenarioInstanceId) {
		LinkedList<Integer> ids = new LinkedList<>();
		ScenarioInstance scenarioInstance =
				scenarioInstanceMap.get(scenarioInstanceId);
		//ScenarioInstance scenarioInstance =
		// sortedScenarioInstances.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				ids.add(nodeInstance.getControlNodeId());
			}
		}
		return ids;
	}


	/**
	 * Gives all activity ids for a scenario instance which are running.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a list of the ids of activities.
	 */
	public LinkedList<Integer> getRunningActivitiesIDsForScenarioInstance(
			int scenarioInstanceId) {
		LinkedList<Integer> ids = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				ids.add(nodeInstance.getControlNodeId());
			}
		}
		return ids;
	}

	/**
	 * Starts the execution of an activity which is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityId         This is the id of the activity.
	 * @return true if the activity could been started. false if not.
	 */
	public boolean beginActivityInstance(int scenarioInstanceId, int activityId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getEnabledControlNodeInstances()) {
			if (nodeInstance.getControlNodeId() == activityId) {
				return ((ActivityInstance) nodeInstance).begin();
			}
		}
		return false;
	}

	/**
	 * Starts the execution of an activity specified by the params.
	 * The state will only be changed if the activity is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId Specifies the activity id.
	 * @return Indicates the success. True if the activity has been started, else false.
	 */
	public boolean beginActivityInstance(int scenarioInstanceId, int activityInstanceId,
                                         List<Integer> usedDataObjects) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getEnabledControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
                return ((ActivityInstance) nodeInstance).begin(usedDataObjects);
			}
		}
		return false;
	}

	/**
	 * Returns information about all enabled Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are enabled and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario.
	 */
	public Collection<ActivityInstance> getEnabledActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> allEnabledActivities = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				allEnabledActivities.add((ActivityInstance) nodeInstance);
			}
		}
		List<ActivityInstance> enabledActivities = new LinkedList<>(allEnabledActivities);
		return enabledActivities;
	}

	/**
	 * Returns information about all controlflow enabled Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are cf-enabled and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario.
	 */
	public Collection<ActivityInstance> getControlFlowEnabledActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> allCFEnabledActivities = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getControlFlowEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				allCFEnabledActivities.add((ActivityInstance) nodeInstance);
			}
		}

		return allCFEnabledActivities;
	}

	/**
	 * Returns information about all data enabled Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are data enabled and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario.
	 */
	public Collection<ActivityInstance> getDataEnabledActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> allDataEnabledActivities = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getDataEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				allDataEnabledActivities.add((ActivityInstance) nodeInstance);
			}
		}

		return allDataEnabledActivities;
	}


	/**
	 * Returns information about all Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are terminated and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario
	 */
	public Collection<ActivityInstance> getTerminatedActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> terminatedActivities = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getTerminatedControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				terminatedActivities.add((ActivityInstance) nodeInstance);
			}
		}
		return terminatedActivities;
	}

	/**
	 * Returns information about all running Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are running and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario
	 */
	public Collection<ActivityInstance> getRunningActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> runningActivities = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				runningActivities.add((ActivityInstance) nodeInstance);
			}
		}
		return runningActivities;
	}

	/**
	 * Terminates an activity which is running.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId         This is the id of the activity.
	 * @return true if the activity could been terminated. false if not.
	 */
	public boolean terminateActivityInstance(int scenarioInstanceId, int activityInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				return nodeInstance.terminate();
			}
		}
		return false;
	}

	/**
	 * Terminates an activity instance.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId This is the id of the activity instance.
	 * @return true if the activity could been terminated. false if not.
	 */
	public boolean terminateActivityInstance(
			int scenarioInstanceId, int activityInstanceId, Map<String, String> classToState) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				return ((ActivityInstance) nodeInstance).terminate(classToState);
			}
		}
		return false;
	}

	/**
	 * Gives the label of a given control node.
	 *
	 * @param controlNodeId This is the database id from the control node.
	 * @return the label of the control node.
	 */
	public String getLabelForControlNodeID(int controlNodeId) {
		return dbControlNode.getLabel(controlNodeId);
	}

	/**
	 * Gives all data object ids for a scenario instance.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a list of the ids of data objects.
	 */
	public List<Integer> getAllDataObjectIDs(int scenarioInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        DataManager dataManager = scenarioInstance.getDataManager();

		return dataManager.getDataObjects().stream()
                .map(DataObject::getDataClassId)
                .collect(Collectors.toList());
	}

	/**
	 * Returns the states of data objects for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
	 */
	public Map<Integer, String> getDataObjectStates(int scenarioInstanceId) {
		DbState dbState = new DbState();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        DataManager dataManager = scenarioInstance.getDataManager();
        return dataManager.getDataObjects().stream().collect(Collectors.toMap(
                DataObject::getId,
                x -> dbState.getStateName(x.getStateId()))
        );
	}

	/**
	 * Returns the names of data objects for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the data objects ids. Values are the names of the data objects.
	 */
	public Map<Integer, String> getAllDataObjectNames(int scenarioInstanceId) {
		DbDataClass dataClass = new DbDataClass();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        DataManager dataManager = scenarioInstance.getDataManager();
        return dataManager.getDataObjects().stream().collect(Collectors.toMap(
                DataObject::getId,
                x -> dataClass.getName(x.getDataClassId())
        ));
	}

	/**
	 * Gives the scenario instance for a scenario instance id.
	 * Only for Tests.
	 *
	 * @param scenarioInstanceID This is the id of the scenario instance.
	 * @return a instance of the class ScenarioInstance.
	 */
	public ScenarioInstance getScenarioInstance(int scenarioInstanceID) {
		return scenarioInstanceMap.get(scenarioInstanceID);
	}
	/**
	 * Sets the values of the data attributes for an activity instance.
	 *
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @param activityInstanceId The id of the activity instance.
	 * @param idToValue          A Map with the data attribute instance id as key
	 *                           and the value of the data attribute as value.
	 * @return true if attributes were set (else false).
	 */
	public boolean setDataAttributeValues(int scenarioInstanceId, int activityInstanceId,
			Map<Integer, String> idToValue) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		return scenarioInstance.getDataManager().setAttributeValues(activityInstanceId, idToValue);
	}

	/**
	 * Returns a Map with all DataAttributeInstanceIDs, values and types for scenario instance.
	 *
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @return a Map with data attribute instances.
	 */
	public Map<Integer, Map<String, String>> getAllDataAttributeInstances(
			int scenarioInstanceId) {
		Map<Integer, Map<String, String>> attributeInstances = new HashMap<>();
		for (DataAttributeInstance dataAttributeInstance : scenarioInstanceMap
				.get(scenarioInstanceId).getDataAttributeInstances().values()) {
			Map<String, String> values = new HashMap<>();
			values.put("type", dataAttributeInstance.getType());
			values.put("value", dataAttributeInstance.getValue().toString());
			values.put("name", dataAttributeInstance.getName());
			attributeInstances.put(
					dataAttributeInstance.getDataAttributeInstanceId(), values);
		}
		return attributeInstances;
	}


	/**
	 * This method gets all dataObjectInstances for a specific set of a scenarioInstance.
	 *
	 * @param setId              This is the databaseID of a DataConditions (either Input or Output).
	 * @param scenarioInstanceID This is the databaseID of a scenarioInstance.
	 * @return an array of dataObjectInstances of dataObjects belonging to the dataSet.
	 */
	public DataObject[] getDataObjectInstancesForDataSetId(int setId,
                                                           int scenarioInstanceID) {
		DbDataNode dbDataNode = new DbDataNode();
		List<Integer> dataObjectsInSet = dbDataNode.getDataClassIdsForDataSets(setId);
        DataManager dataManager = scenarioInstanceMap.get(scenarioInstanceID).getDataManager();

        List<DataObject> dataObjects = dataManager.getDataObjects()
                .stream().filter(x -> dataObjectsInSet.contains(x.getDataClassId()))
                .collect(Collectors.toList());
        DataObject[] dataObjectArray =
                dataObjects.toArray(new DataObject[dataObjects.size()]);
		return dataObjectArray;
	}



	/**
	 * This method generates an array of all dataAttributes for a given dataObject.
	 *
	 * @param dataObject This is the dataObject.
	 * @return an array of DataAttributeJaxBean belonging to this dataObject.
	 */
	public DataAttributeJaxBean[] getDataAttributesForDataObjectInstance(
			DataObject dataObject) {
		DataAttributeJaxBean[] dataAttributes =
				new DataAttributeJaxBean[dataObject
				.getDataAttributeInstances().size()];
		int i = 0;
		List<DataAttributeInstance> dataAttributeInstances = dataObject
				.getDataAttributeInstances();
		for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances) {
			DataAttributeJaxBean dataAttribute =
					new DataAttributeJaxBean();
			dataAttribute.setId(dataAttributeInstance.getDataAttributeInstanceId());
			dataAttribute.setName(dataAttributeInstance.getName());
			dataAttribute.setType(dataAttributeInstance.getType());
			dataAttribute.setValue(dataAttributeInstance.getValue().toString());
			dataAttributes[i] = dataAttribute;
			i++;
		}
		return dataAttributes;
	}

	protected static int getInstancesSize() {
		int size = -1;
		if (instances != null) {
			size = instances.size();
		}
		return size;
	}

	protected static void dropCachedInstances() {
		instances.clear();
	}

	/**
	 * Test for the existence of an activity instance in a given scenarioInstance.
	 *
	 * @param activityID The databaseID of the activityInstance which is to be checked.
	 * @return a boolean. True == activity is existing/ False == activity does not exist.
	 */
	public boolean testActivityInstanceExists(int activityID) {
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		return dbControlNodeInstance.existControlNodeInstance(activityID);
	}
}
