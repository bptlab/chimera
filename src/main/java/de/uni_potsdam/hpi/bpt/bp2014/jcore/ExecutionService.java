package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbReference;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.rest.RestInterface;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Handles all scenario instances.
 */
public class ExecutionService /*implements Runnable*/ {

	private static Logger log = Logger.getLogger(ExecutionService.class.getName());
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
		//if (instance.newVersionAvailable) {j
			instance.scenarioInstanceMap.clear();
			instance.instanceHistory.clear();
		//}
		return instance;
	}

	/**
	 * Starts a new scenario instance for the given scenario id.
	 *
	 * @param scenarioId This is the id of the scenario.
	 * @return the id of the new scenario instance.
	 * @deprecated please use startNewScenarioInstanceStatic instead.
	 */
	@Deprecated public int startNewScenarioInstance(int scenarioId) {
		ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId);
		scenarioInstances.add(scenarioInstance);
		//scenarioInstanceMap.put(scenarioInstance
		// 	.getScenarioInstanceId(), scenarioInstance);
		addScenarioInstanceToMap(scenarioInstance);
		sortedScenarioInstances.put(
				scenarioInstance.getScenarioInstanceId(), scenarioInstance);
		return scenarioInstance.getScenarioInstanceId();
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
			//else if (!sortedScenarioInstances.containsKey(scenarioInstanceId)) {
		} else if (existScenarioInstance(scenarioInstanceId)) {
			log.info("Load existing scenario instance with scenarioId=" + scenarioId
					+ " and scenarioInstanceId="
					+ scenarioInstanceId + " from database.");
			ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId,
					scenarioInstanceId);
			scenarioInstances.add(scenarioInstance);
			sortedScenarioInstances.put(scenarioInstanceId, scenarioInstance);
			//scenarioInstanceMap.put(scenarioInstanceId, scenarioInstance);
			addScenarioInstanceToMap(scenarioInstance);
		} else {
			return false;
		}
		//        }
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
	 * Gives all scenarios in the database.
	 *
	 * @return all list with all ids of all scenarios in the database.
	 */
	public LinkedList<Integer> getAllScenarioIDs() {
		return dbScenario.getScenarioIDs();
	}

	/**
	 * Checks if the scenario instance have been open.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return true if the scenario instance have been open. false if not.
	 */
	public boolean scenarioInstanceIsRunning(int scenarioInstanceId) {
		return scenarioInstanceMap.containsKey(scenarioInstanceId);
	}

	/**
	 * Checks if the scenario instance exist in the database.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return true if the scenario instance exist in the database. false if not.
	 */
	public boolean existScenarioInstance(int scenarioInstanceId) {
		return dbScenarioInstance.existScenario(scenarioInstanceId);
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
	 * Gives all scenario instance id for a scenario id.
	 *
	 * @param scenarioId This is the id of the scenario.
	 * @return a list of all scenario instance ids for a the given scenario id.
	 */
	public LinkedList<Integer> listAllScenarioInstancesForScenario(int scenarioId) {
		return dbScenarioInstance.getScenarioInstances(scenarioId);
	}

	/**
	 * Gives the database id from the scenario for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return the database id for a scenario.
	 */
	public int getScenarioIDForScenarioInstance(int scenarioInstanceId) {
		return dbScenarioInstance.getScenarioID(scenarioInstanceId);
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
	 * Returns the Labels of enabled activities for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the activity ids. Values are the labels of the activities.
	 */
	public Map<Integer, String> getEnabledActivityLabelsForScenarioInstance(
			int scenarioInstanceId) {
		Map<Integer, String> labels = new HashMap<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				labels.put(nodeInstance.getControlNodeId(),
						((ActivityInstance) nodeInstance).getLabel());
			}
		}
		return labels;
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
	 * Returns the Labels of running activities for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the activity ids. Values are the labels of the activities.
	 */
	public Map<Integer, String> getRunningActivityLabelsForScenarioInstance(
			int scenarioInstanceId) {
		Map<Integer, String> labels = new HashMap<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				labels.put(nodeInstance.getControlNodeId(),
						((ActivityInstance) nodeInstance).getLabel());
			}
		}
		return labels;
	}

	/**
	 * Starts the execution of an activity which is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityId         This is the id of the activity.
	 * @return true if the activity could been started. false if not.
	 */
	public boolean beginActivity(int scenarioInstanceId, int activityId) {
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
	 * @param activityInstanceId Specifies the activity instance id.
	 * @return Indicates the success. True if the activity has been started, else false.
	 */
	public boolean beginActivityInstance(int scenarioInstanceId, int activityInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getEnabledControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				return ((ActivityInstance) nodeInstance).begin();
			}
		}
		return false;
	}

	/**
	 * Terminates the execution of an activity specified by the params.
	 * The state will only be changed if the activity is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId Specifies the activity instance id.
	 * @return Indicates the success. True if the activity has been started, else false.
	 */
	public boolean terminateActivityInstance(int scenarioInstanceId, int activityInstanceId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId()
					== activityInstanceId) {
				return nodeInstance.terminate();
			}
		}
		return false;
	}

	/**
	 * Terminates the execution of an activity specified by the params.
	 * The state will only be changed if the activity is enabled.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityInstanceId Specifies the activity instance id.
	 * @param outputSetId This is the id of the output set.
	 * @return Indicates the success. True if the activity has been started, else false.
	 */
	public boolean terminateActivityInstance(int scenarioInstanceId, int activityInstanceId,
			int outputSetId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceId) {
				return ((ActivityInstance) nodeInstance).terminate(outputSetId);
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
		Collection<ActivityInstance> activities = new LinkedList<>();
		List<ActivityInstance> enabledActivities = new LinkedList<>(allEnabledActivities);
		for (ActivityInstance activityInstance : allEnabledActivities) {
			if (!activities.contains(activityInstance)) {
				Collection<ActivityInstance> references = this
						.getReferentialEnabledActivities(
								scenarioInstanceId,
								activityInstance
								.getControlNodeInstanceId()
						);
				enabledActivities.removeAll(references);
				activities.addAll(references);
			}
		}
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
	 * Returns information about all referential Activities
	 * of a given scenario instance and activity instance.
	 *
	 * @return a Collection of referential Activity instances for an Activity,
	 * which are enabled and part of the specified scenario instance.
	 * @param scenarioInstanceId The id which specifies the scenario
	 * @param activityInstanceId The id which specifies the activity
	 */
	public Collection<ActivityInstance> getReferentialEnabledActivities(int scenarioInstanceId,
			int activityInstanceId) {
		Collection<ActivityInstance> enabledActivities = new LinkedList<>();
		DbReference dbReference = new DbReference();
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		LinkedList<Integer> references = dbReference.getReferenceActivitiesForActivity(
				dbControlNodeInstance.getControlNodeID(activityInstanceId));
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);

		for (AbstractControlNodeInstance nodeInstance : scenarioInstance
				.getControlFlowEnabledControlNodeInstances()) {
			if (nodeInstance instanceof ActivityInstance) {
				for (int id : references) {
					if (id == nodeInstance.getControlNodeId()) {
						enabledActivities.add(
								(ActivityInstance) nodeInstance);
					}
				}
			}
		}
		return enabledActivities;
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
	 * @param activityId         This is the id of the activity.
	 * @return true if the activity could been terminated. false if not.
	 */
	public boolean terminateActivity(int scenarioInstanceId, int activityId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		//ScenarioInstance scenarioInstance =
		//	sortedScenarioInstances.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeId() == activityId) {
				return nodeInstance.terminate();
			}
		}
		return false;
	}

	/**
	 * Terminates an activity which is running.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @param activityId         This is the id of the activity.
	 * @param outputSetId		 This is the id of the output set.
	 * @return true if the activity could been terminated. false if not.
	 */
	public boolean terminateActivity(
			int scenarioInstanceId, int activityId, int outputSetId) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeId() == activityId) {
				return ((ActivityInstance) nodeInstance).terminate(outputSetId);
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
	public LinkedList<Integer> getAllDataObjectIDs(int scenarioInstanceId) {
		LinkedList<Integer> dataObjectIDs = new LinkedList<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		if (scenarioInstance != null) {
			for (DataObjectInstance dataObject
					: scenarioInstance.getDataObjectInstances()) {
				dataObjectIDs.add(dataObject.getDataObjectId());
			}
			for (DataObjectInstance dataObject : scenarioInstance
					.getDataObjectInstancesOnChange()) {
				dataObjectIDs.add(dataObject.getDataObjectId());
			}
		}
		return dataObjectIDs;
	}

	/**
	 * Returns the states of data objects for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
	 */
	public Map<Integer, String> getAllDataObjectStates(int scenarioInstanceId) {
		DbState dbState = new DbState();
		Map<Integer, String> dataObjectStates = new HashMap<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		if (scenarioInstance != null) {
			for (DataObjectInstance dataObject
					: scenarioInstance.getDataObjectInstances()) {
				dataObjectStates.put(dataObject.getDataObjectId(),
						dbState.getStateName(dataObject.getStateId()));
			}
			for (DataObjectInstance dataObject : scenarioInstance
					.getDataObjectInstancesOnChange()) {
				dataObjectStates.put(dataObject.getDataObjectId(),
						dbState.getStateName(dataObject.getStateId()));
			}
		}
		return dataObjectStates;
	}

	/**
	 * Returns the names of data objects for a scenario instance id.
	 *
	 * @param scenarioInstanceId This is the id of the scenario instance.
	 * @return a Map. Keys are the data objects ids. Values are the names of the data objects.
	 */
	public Map<Integer, String> getAllDataObjectNames(int scenarioInstanceId) {
		DbDataObject dbDataObject = new DbDataObject();
		Map<Integer, String> dataObjectNames = new HashMap<>();
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		if (scenarioInstance != null) {
			for (DataObjectInstance dataObject
					: scenarioInstance.getDataObjectInstances()) {
				dataObjectNames.put(dataObject.getDataObjectId(),
						dbDataObject.getName(dataObject.getDataObjectId()));
			}
			for (DataObjectInstance dataObject : scenarioInstance
					.getDataObjectInstancesOnChange()) {
				dataObjectNames.put(dataObject.getDataObjectId(),
						dbDataObject.getName(dataObject.getDataObjectId()));
			}
		}
		return dataObjectNames;
	}

	/**
	 * Checks if a scenario instance is terminated.
	 *
	 * @param scenarioInstanceId This is the database id from the scenario instance id.
	 * @return true if the scenario instance ist terminated. false if not.
	 */
	public boolean checkTerminationForScenarioInstance(int scenarioInstanceId) {
		DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
		return dbScenarioInstance.getTerminated(scenarioInstanceId) == 1;
	}

	/**
	 * Returns the scenario name for a given scenario id.
	 *
	 * @param scenarioId This is the database id from the scenario id.
	 * @return the scenario name in a String.
	 */
	public String getScenarioName(int scenarioId) {
		return dbScenario.getScenarioName(scenarioId);
	}

	/**
	 * Returns the scenario name for a given scenario instance id.
	 *
	 * @param scenarioInstanceId This is the database id from the scenario instance id.
	 * @return the scenario name in a String.
	 */
	public String getScenarioNameForScenarioInstance(int scenarioInstanceId) {
		return dbScenario.getScenarioName(
				dbScenarioInstance.getScenarioID(scenarioInstanceId));
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
	 * Terminates the given scenario instance.
	 *
	 * @param scenarioInstanceID The id of the scenario instance.
	 */
	public void terminateScenarioInstance(int scenarioInstanceID) {
		dbScenarioInstance.setTerminated(scenarioInstanceID, true);
	}

	/**
	 * Sets the values of the data attributes for an activity instance.
	 *
	 * @param scenarioInstanceId The id of the scenario instance.
	 * @param activityInstanceID  The id of the activity instance.
	 * @param values              A Map with the data attribute instance id as key
	 *                            	and the value of the data attribute as value.
	 * @return true if attributes were set (else false).
	 */
	public boolean setDataAttributeValues(
			int scenarioInstanceId, int activityInstanceID,
			Map<Integer, String> values) {
		ScenarioInstance scenarioInstance = scenarioInstanceMap.get(scenarioInstanceId);
		for (AbstractControlNodeInstance nodeInstance
				: scenarioInstance.getRunningControlNodeInstances()) {
			if (nodeInstance.getControlNodeInstanceId() == activityInstanceID) {
				((ActivityInstance) nodeInstance).setDataAttributeValues(values);
				return true;
			}
		}
		return false;
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
	 * Returns a Map with all OutputSets, DataOjects and Data States for activity instance.
	 *
	 * @param activityInstanceId The id of the activity instance.
	 * @return a Map with outputsets.
	 */
	public Map<Integer, Map<String, String>> getOutputSetsForActivityInstance(
			int activityInstanceId) {
		DbDataFlow dbDataFlow = new DbDataFlow();
		DbDataNode dbDataNode = new DbDataNode();
		DbDataObject dbDataObject = new DbDataObject();
		DbState dbState = new DbState();
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		int controlNodeId = dbControlNodeInstance.getControlNodeID(activityInstanceId);

		Map<Integer, Map<String, String>> allOutputSets = new HashMap<>();
		LinkedList<Integer> outputSets =
				dbDataFlow.getOutputSetsForControlNode(controlNodeId);
		for (int outputSet : outputSets) {
			LinkedList<DataObject> dataObjects =
					dbDataNode.getDataObjectsForDataSets(outputSet);
			for (DataObject dataObject : dataObjects) {
				allOutputSets.put(outputSet, new HashMap<String, String>());
				allOutputSets.get(outputSet).put(
						dbDataObject.getName(dataObject.getId()),
						dbState.getStateName(dataObject.getStateID()));
			}
		}
		return allOutputSets;
	}

	/**
	 * This method gets all dataObjectInstances for a specific set of a scenarioInstance.
	 *
	 * @param setID              This is the databaseID of a DataSet (either Input or Output).
	 * @param scenarioInstanceID This is the databaseID of a scenarioInstance.
	 * @return an array of dataObjectInstances of dataObjects belonging to the dataSet.
	 */
	public DataObjectInstance[] getDataObjectInstancesForDataSetId(int setID,
			int scenarioInstanceID) {
		DbDataNode dbDataNode = new DbDataNode();
		LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(setID);
		int j = 0;
		DataObjectInstance[] dataObjectInstancesArray =
				new DataObjectInstance[dataObjects.size()];
		for (DataObject dataObject : dataObjects) {
			LinkedList<DataObjectInstance> dataObjectInstances = scenarioInstanceMap
					.get(scenarioInstanceID).getDataObjectInstances();
			for (DataObjectInstance dataObjectInstance : dataObjectInstances) {
				if (dataObject.getId() == dataObjectInstance.getDataObjectId()) {
					dataObjectInstancesArray[j] = dataObjectInstance;
					j++;
				}
			}
			dataObjectInstances = scenarioInstanceMap.get(scenarioInstanceID)
					.getDataObjectInstancesOnChange();
			for (DataObjectInstance dataObjectInstance : dataObjectInstances) {
				if (dataObject.getId() == dataObjectInstance.getDataObjectId()) {
					dataObjectInstancesArray[j] = dataObjectInstance;
					j++;
				}
			}
		}
		return dataObjectInstancesArray;
	}

	/**
	 * This method is used to get the stateName corresponding to a dataObjectInstance.
	 *
	 * @param dataObjectInstance This is an object of the DataObjectInstance class.
	 * @return the name of the state of the dataObjectInstance as a String.
	 */
	public String getStateNameForDataObjectInstanceInput(
			DataObjectInstance dataObjectInstance) {
		DbState dbState = new DbState();
		return dbState.getStateName(dataObjectInstance.getStateId());
	}

	/**
	 * This method is used to get the stateName corresponding to a dataObjectInstance.
	 *
	 * @param dataObjectInstance This is an object of the DataObjectInstance class.
	 * @param setID This is the databaseID of a DataSet (either Input or Output).
	 * @return the name of the state of the dataObjectInstance as a String.
	 */
	public String getStateNameForDataObjectInstanceOutput(DataObjectInstance dataObjectInstance,
			int setID) {
		DbState dbState = new DbState();
		DbDataNode dbDataNode = new DbDataNode();
		LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(setID);
		for (DataObject dataObject : dataObjects) {
			if (dataObject.getId() == dataObjectInstance.getDataObjectId()) {
				return dbState.getStateName(dataObject.getStateID());
			}
		}
		return "";
	}

	/**
	 * Returns a Map with all InputSets, DataObjects and Data States for activity instance.
	 *
	 * @param activityInstanceId The id of the activity instance.
	 * @return a Map with inputsets.
	 */
	public Map<Integer, Map<String, String>> getInputSetsForActivityInstance(
			int activityInstanceId) {
		DbDataFlow dbDataFlow = new DbDataFlow();
		DbDataNode dbDataNode = new DbDataNode();
		DbDataObject dbDataObject = new DbDataObject();
		DbState dbState = new DbState();
		DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
		int controlNodeId = dbControlNodeInstance.getControlNodeID(activityInstanceId);

		Map<Integer, Map<String, String>> allInputSets = new HashMap<>();
		LinkedList<Integer> inputSets =
				dbDataFlow.getInputSetsForControlNode(controlNodeId);
		for (int inputSet : inputSets) {
			LinkedList<DataObject> dataObjects =
					dbDataNode.getDataObjectsForDataSets(inputSet);
			for (DataObject dataObject : dataObjects) {
				allInputSets.put(inputSet, new HashMap<String, String>());
				allInputSets.get(inputSet).put(
						dbDataObject.getName(dataObject.getId()),
						dbState.getStateName(dataObject.getStateID()));
			}
		}
		return allInputSets;

	}

	/**
	 * This method generates an array of all dataAttributes for a given dataObjectInstance.
	 *
	 * @param dataObjectInstance This is the dataObjectInstance.
	 * @return an array of DataAttributeJaxBean belonging to this dataObjectInstance.
	 */
	public RestInterface.DataAttributeJaxBean[] getDataAttributesForDataObjectInstance(
			DataObjectInstance dataObjectInstance) {
		RestInterface.DataAttributeJaxBean[] dataAttributes =
				new RestInterface.DataAttributeJaxBean[dataObjectInstance
				.getDataAttributeInstances().size()];
		int i = 0;
		LinkedList<DataAttributeInstance> dataAttributeInstances = dataObjectInstance
				.getDataAttributeInstances();
		for (DataAttributeInstance dataAttributeInstance : dataAttributeInstances) {
			RestInterface.DataAttributeJaxBean dataAttribute =
					new RestInterface.DataAttributeJaxBean();
			dataAttribute.setId(dataAttributeInstance.getDataAttributeInstanceId());
			dataAttribute.setName(dataAttributeInstance.getName());
			dataAttribute.setType(dataAttributeInstance.getType());
			dataAttribute.setValue(dataAttributeInstance.getValue().toString());
			dataAttributes[i] = dataAttribute;
			i++;
		}
		return dataAttributes;
	}

	public void setNewVersionAvailable(boolean newVersionAvailable) {
		this.newVersionAvailable = newVersionAvailable;
	}

	public boolean isNewVersionAvailable() {
		return newVersionAvailable;
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

    /*
	@Override
	public void run() {
	}    
    */
}
