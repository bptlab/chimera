package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.database.DbScenario;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.data.DbDataClass;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataAttributeJaxBean;
import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.data.DbDataNode;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.DataObject;
import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataObjectJaxBean;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles all scenario instances.
 */
public class ExecutionService {

	private static Logger log = Logger.getLogger(ExecutionService.class);
	/**
	 * Define the maximum allowable Map size.
	 */
	private static final int MAX_MAP_SIZE = 100;
	private static Map<Integer, ExecutionService> instances = new HashMap<>();
	private int scenarioId = -1;
	private boolean newVersionAvailable = true;

	/**
	 * This are the Lists for all opened scenario instances.
	 */
	private Map<Integer, ScenarioInstance> scenarioInstanceMap = new HashMap<>();
	private Queue<Integer> instanceHistory = new LinkedList<>();

	/**
	 * Database ConnectionWrapper.
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
		ExecutionService service = instances.get(scenarioId);
		if (service == null) {
			service = new ExecutionService(scenarioId);
			instances.put(scenarioId, service);
		}
		//if there is a new version available, reload all instances from the database.
		//if (service.newVersionAvailable) {
			service.scenarioInstanceMap.clear();
			service.instanceHistory.clear();
		//}
		return service;
	}

	/**
	 * Starts a new scenario instance for the given scenario id.
	 *
	 * @return the id of the new scenario instance.
	 */
	public int startNewScenarioInstance() {
		ScenarioInstance scenarioInstance = new ScenarioInstance(this.scenarioId);
		addScenarioInstanceToMap(scenarioInstance);
		return scenarioInstance.getId();
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
			// refresh data objects
			ScenarioInstance instance = scenarioInstanceMap.get(scenarioInstanceId);
			instance.getDataManager().loadFromDatabase();
		} else if (existScenarioInstance(scenarioId, scenarioInstanceId)) {
			log.info("Load existing scenario instance with scenarioId=" + scenarioId
					+ " and scenarioInstanceId="
					+ scenarioInstanceId + " from database.");
			ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId,
					scenarioInstanceId);
			addScenarioInstanceToMap(scenarioInstance);
		} else {
			return false;
		}
		updateHistory(scenarioInstanceId);
		return true;
	}

	private void addScenarioInstanceToMap(ScenarioInstance scenarioInstance) {
		scenarioInstanceMap.put(scenarioInstance.getId(), scenarioInstance);
		updateHistory(scenarioInstance.getId());
		if (scenarioInstanceMap.size() > MAX_MAP_SIZE) {
			int oldestInstance = instanceHistory.poll();
			scenarioInstanceMap.remove(oldestInstance);
			log.info("Inserted scenario with ID="
					+ scenarioInstance.getId()
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
	 * and then adding it to the end of the queue.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 */
	private void updateHistory(int scenarioInstanceId) {
		instanceHistory.remove(scenarioInstanceId);
		instanceHistory.offer(scenarioInstanceId);
	}




	/**
	 * Checks if the scenario instance exist in the database.
	 *
	 * @param scenarioId         This is the id of the scenario.
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return true if the scenario instance exist in the database. false if not.
	 */
	public boolean existScenarioInstance(int scenarioId, int scenarioInstanceId) {
		return dbScenarioInstance.doesScenarioInstanceBelongToScenario(scenarioId, scenarioInstanceId);
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
	public List<Integer> getEnabledActivityIdsForScenarioInstance(
			int scenarioInstanceId) {
		List<Integer> ids = new ArrayList<>();
		ScenarioInstance scenarioInstance =
				scenarioInstanceMap.get(scenarioInstanceId);
		ids.addAll(scenarioInstance.getEnabledControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(AbstractControlNodeInstance::getControlNodeId)
				.collect(Collectors.toList()));
		return ids;
	}


	/**
	 * Gives all activity ids for a scenario instance which are running.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a list of the ids of activities.
	 */
	public List<Integer> getRunningActivityIdsForScenarioInstance(
			int scenarioInstanceId) {
		List<Integer> ids = new ArrayList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		ids.addAll(scenarioInstance.getRunningControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(AbstractControlNodeInstance::getControlNodeId)
				.collect(Collectors.toList()));
		return ids;
	}

	/**
	 * Starts the execution of an activity which is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityId         This is the id of the activity.
	 * @return true if the activity could been started. false if not.
	 */
	public void beginActivityInstance(int scenarioInstanceId, int activityId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getEnabledControlNodeInstances()) {
			if (nodeInstance.getControlNodeId() == activityId) {
				((ActivityInstance) nodeInstance).begin();
			}
		}
	}

	/**
	 * Starts the execution of an activity specified by the params.
	 * The state will only be changed if the activity is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId Specifies the activity id.
	 * @return Indicates the success. True if the activity has been started, else false.
	 */
	public void beginActivityInstance(int scenarioInstanceId, int activityInstanceId,
                                         List<Integer> usedDataObjects) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getEnabledControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
                ((ActivityInstance) nodeInstance).begin(usedDataObjects);
			}
		}
	}

	/**
	 * Returns information about all enabled Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are enabled and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario.
	 */
	public Collection<ActivityInstance> getEnabledActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> enabledActivities = new ArrayList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		enabledActivities.addAll(scenarioInstance.getEnabledControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(nodeInstance -> (ActivityInstance) nodeInstance)
				.collect(Collectors.toList()));
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
		Collection<ActivityInstance> cfEnabledActivities = new ArrayList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		cfEnabledActivities.addAll(
				scenarioInstance.getControlFlowEnabledControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(nodeInstance -> (ActivityInstance) nodeInstance)
				.collect(Collectors.toList()));
		return cfEnabledActivities;
	}

	/**
	 * Returns information about all data enabled Activities of a given scenario instance.
	 *
	 * @return a Collection of Activity instances, which are data enabled and part of the
	 * specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario.
	 */
	public Collection<ActivityInstance> getDataEnabledActivities(int scenarioInstanceId) {
		Collection<ActivityInstance> allDataEnabledActivities = new ArrayList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		allDataEnabledActivities.addAll(
				scenarioInstance.getDataEnabledControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(nodeInstance -> (ActivityInstance) nodeInstance)
				.collect(Collectors.toList()));
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
		terminatedActivities.addAll(
				scenarioInstance.getTerminatedControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(nodeInstance -> (ActivityInstance) nodeInstance)
				.collect(Collectors.toList()));
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
		Collection<ActivityInstance> runningActivities = new ArrayList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		runningActivities.addAll(scenarioInstance.getRunningControlNodeInstances().stream()
				.filter(nodeInstance -> nodeInstance instanceof ActivityInstance)
				.map(nodeInstance -> (ActivityInstance) nodeInstance)
				.collect(Collectors.toList()));
		return runningActivities;
	}

	/**
	 * Terminates a running activity.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId         This is the id of the activity.
	 * @return true if the activity could been terminated. false if not.
	 */
	public void terminateActivityInstance(int scenarioInstanceId, int activityInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				nodeInstance.terminate();
			}
		}
	}

	/**
	 * Terminates an activity instance.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId This is the id of the activity instance.
	 * @return true if the activity could been terminated. false if not.
	 */
	public void terminateActivityInstance(
			int scenarioInstanceId, int activityInstanceId, Map<String, String> classToState) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				((ActivityInstance) nodeInstance).terminate(classToState);
			}
		}
	}

	/**
	 * Gives the label of a given control node.
	 *
	 * @param controlNodeId This is the database id from the control node.
	 * @return the label of the control node.
	 */
	public String getLabelForControlNodeId(int controlNodeId) {
		return dbControlNode.getLabel(controlNodeId);
	}

	/**
	 * Gives all data object ids for a scenario instance.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a list of the ids of data objects.
	 */
	public List<Integer> getAllDataObjectIds(int scenarioInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
        DataManager dataManager = scenarioInstance.getDataManager();

		return dataManager.getDataObjects().stream()
                .map(DataObject::getId)
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
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a instance of the class ScenarioInstance.
	 */
	public ScenarioInstance getScenarioInstance(int scenarioInstanceId) {
		return scenarioInstanceMap.get(scenarioInstanceId);
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
					dataAttributeInstance.getId(), values);
		}
		return attributeInstances;
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
			dataAttribute.setId(dataAttributeInstance.getId());
			dataAttribute.setName(dataAttributeInstance.getName());
			dataAttribute.setType(dataAttributeInstance.getType());
			dataAttribute.setValue(dataAttributeInstance.getValue().toString());
			dataAttributes[i] = dataAttribute;
			i++;
		}
		return dataAttributes;
	}

	static int getInstancesSize() {
		int size = -1;
		if (instances != null) {
			size = instances.size();
		}
		return size;
	}

	static void dropCachedInstances() {
		instances.clear();
	}

	/**
	 * Test for the existence of an activity instance in a given scenarioInstance.
	 *
	 * @param activityInstanceId The databaseID of the activityInstance which is to be checked.
	 * @return a boolean. True == activity is existing/ False == activity does not exist.
	 */
	public boolean testActivityInstanceExists(int activityInstanceId) {
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		return dbControlNodeInstance.existControlNodeInstance(activityInstanceId);
	}
}
