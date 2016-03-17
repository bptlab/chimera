package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.GatewayInstance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a scenario instance.
 * The constructor looks for an scenario instance in the database or creates a new one.
 * The constructor also initialize the fragment instances and data object instances.
 * The scenario instance has Lists for all enabled, control flow enabled, data flow enabled,
 * running and terminated activity instances, fragment instances and all data object instances.
 * The scenario instance provide methods for the administration of the data object instances
 */
public class ScenarioInstance {
	private final int scenarioInstanceId;
	private final int scenarioId;
	private final String name;
	/**
	 * database connection objects.
	 */
	private final DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
	private final DbFragment dbFragment = new DbFragment();
	private final DbDataObject dbDataObject = new DbDataObject();
	private final DbScenario dbScenario = new DbScenario();
	/**
	 * Lists to save all fragments and all control nodes sorted by state.
	 */
	private LinkedList<AbstractControlNodeInstance> controlNodeInstances = new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> enabledControlNodeInstances
			= new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> controlFlowEnabledControlNodeInstances
			= new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> dataEnabledControlNodeInstances
			= new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> runningControlNodeInstances
			= new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> terminatedControlNodeInstances
			= new LinkedList<>();
	private LinkedList<DataObjectInstance> dataObjectInstances = new LinkedList<>();
	private LinkedList<DataObjectInstance> dataObjectInstancesOnChange = new LinkedList<>();
	private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<>();
	private LinkedList<AbstractControlNodeInstance> referentialRunningControlNodeInstances
			= new LinkedList<>();
	private LinkedList<GatewayInstance> executingGateways = new LinkedList<>();
	private Map<Integer, DataAttributeInstance> dataAttributeInstances = new HashMap<>();

    private boolean canTerminate;
    private TerminationCondition terminationCondition;
	/**
	 * Creates and initializes a new scenario instance from database.
	 * Reads the information for an existing scenario instance from the database.
	 * If there is no match in the database it creates a new scenario instance.
	 *
	 * @param scenarioId         This is the database id from the scenario.
	 * @param scenarioInstanceId This is the database id from the scenario instance.
	 */
	public ScenarioInstance(int scenarioId, int scenarioInstanceId) {

        this.name = dbScenario.getScenarioName(scenarioId);
		this.scenarioId = scenarioId;
		this.terminationCondition = new TerminationCondition(this);
        if (dbScenarioInstance.existScenario(scenarioId, scenarioInstanceId)) {
			//creates an existing Scenario Instance using the database information
			this.scenarioInstanceId = scenarioInstanceId;
		} else {
			//creates a new Scenario Instance also in database,
			// using autoincrement to getting the scenario instances id
			this.scenarioInstanceId = dbScenarioInstance.createNewScenarioInstance(
					scenarioId);
		}
		this.initializeDataObjects();
		if (dbScenarioInstance.getTerminated(this.scenarioInstanceId) == 0) {
			this.initializeFragments();
		}
		this.startAutomaticControlNodes();
        canTerminate = checkTerminationCondition();
	}

	/**
	 * Creates and initializes a new scenario instance.
	 * Also save this new instance in database.
	 *
	 * @param scenarioId This is the database id from the scenario.
	 */
	public ScenarioInstance(int scenarioId) {
		this.name = dbScenario.getScenarioName(scenarioId);
		this.scenarioId = scenarioId;
		this.scenarioInstanceId = dbScenarioInstance.createNewScenarioInstance(scenarioId);
        this.terminationCondition = new TerminationCondition(this);
        this.initializeDataObjects();
		this.initializeFragments();
		this.startAutomaticControlNodes();
	}

	/**
	 * Creates and initializes all fragments for the scenario.
	 */
	private void initializeFragments() {
		LinkedList<Integer> fragmentIds = dbFragment.getFragmentsForScenario(scenarioId);
		for (int fragmentId : fragmentIds) {
			this.initializeFragment(fragmentId);
		}
	}

    // TODO Test this method
    public List<AbstractEvent> getEventsForScenarioInstance() {
        return this.fragmentInstances.stream().map(FragmentInstance::getRegisteredEvents).
                flatMap(Collection::stream).collect(Collectors.toList());
    }

    public AbstractControlNodeInstance getControlNodeInstanceWithId(int controlNodeInstanceId) {
        for (AbstractControlNodeInstance controlNodeInstance : this.getControlNodeInstances()) {
            if (controlNodeInstance.getControlNodeInstanceId() == controlNodeInstanceId) {
                return controlNodeInstance;
            }
        }
        return null;
    }

    /**
     *
     * @return returns a List of all registered keys to events.
     */
    public List<String> getRegisteredEventKeys() {
        List<String> eventKeys = new ArrayList<>();
        for (FragmentInstance fragmentInstance : this.fragmentInstances) {
            Integer fragmentInstanceId = fragmentInstance.getFragmentInstanceId();
            DbEventMapping eventMapping = new DbEventMapping();
            eventKeys.addAll(eventMapping.getRequestKeysForFragment(fragmentInstanceId));
        }
        return eventKeys;
    }

    /**
	 * Creates and initializes the fragment with the specific fragment id.
	 *
	 * @param fragmentId This is the database id from the fragment.
	 */
	private void initializeFragment(int fragmentId) {
		FragmentInstance fragmentInstance = new FragmentInstance(
				fragmentId, scenarioInstanceId,	this);
		fragmentInstances.add(fragmentInstance);
	}

	/**
	 * Restarts the fragment specified by the fragment id.
	 * Removes all control nodes from the fragment
	 * from all lists and create and initializes the fragment again.
	 *
	 * @param fragmentInstanceId This is the database id from the fragment instance.
	 */
	public void restartFragment(int fragmentInstanceId) {
		FragmentInstance fragmentInstance = null;
		for (FragmentInstance f : fragmentInstances) {
			if (f.getFragmentInstanceId() == fragmentInstanceId) {
				fragmentInstance = f;
			}
		}
		fragmentInstances.remove(fragmentInstance);
		if (fragmentInstance != null) {
			fragmentInstance.terminate();
		}

		//removes the old control node instances
		LinkedList<AbstractControlNodeInstance> updatedList = new LinkedList<>(
				terminatedControlNodeInstances);
		for (AbstractControlNodeInstance controlNodeInstance : updatedList) {
			if (controlNodeInstance.getFragmentInstanceId() == fragmentInstanceId) {
				terminatedControlNodeInstances.remove(controlNodeInstance);
			}
		}
		updatedList = new LinkedList<>(controlNodeInstances);
		for (AbstractControlNodeInstance controlNodeInstance : updatedList) {
			if (controlNodeInstance.getFragmentInstanceId() == fragmentInstanceId) {
				controlNodeInstances.remove(controlNodeInstance);
			}
		}
		if (fragmentInstance != null) {
			initializeFragment(fragmentInstance.getFragmentId());
		}
		this.startAutomaticControlNodes();
	}

	/**
	 * Initializes all data objects for the scenario instance.
	 */
	private void initializeDataObjects() {
		LinkedList<Integer> data = dbDataObject.getDataObjectsForScenario(scenarioId);
		for (Integer dataObject : data) {
			DataObjectInstance dataObjectInstance = new DataObjectInstance(
					dataObject, scenarioId, scenarioInstanceId, this);
			//checks if dataObjectInstance is locked
			if (dataObjectInstance.getOnChange()) {
				dataObjectInstancesOnChange.add(dataObjectInstance);
			} else {
				dataObjectInstances.add(dataObjectInstance);
			}
		}
	}

	/**
	 * Checks if the control flow enabled control nodes can set to data flow enabled.
	 */
	public void checkDataFlowEnabled() {
		for (AbstractControlNodeInstance activityInstance
				: controlFlowEnabledControlNodeInstances) {
			if (activityInstance.getClass() == ActivityInstance.class) {
				((ActivityInstance) activityInstance).checkDataFlowEnabled();
			}
		}
	}

	/**
	 * Checks if a executing gateway can terminate.
	 *
	 * @param controlNodeId the Id of a control node.
	 */
	@SuppressWarnings("unchecked") public void checkExecutingGateways(int controlNodeId) {
		for (GatewayInstance gatewayInstance : (
				(LinkedList<GatewayInstance>) executingGateways.clone())) {
			if (gatewayInstance.checkTermination(controlNodeId)) {
				gatewayInstance.terminate();
			}
		}
	}

	/**
	 * Checks if the list terminatedControlNodeInstances contains the control node.
	 *
	 * @param controlNodeId This is the database id from the control node.
	 * @return true if the terminated control node instances contains the control node.
	 */
	public boolean terminatedControlNodeInstancesContainControlNodeID(int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance
				: terminatedControlNodeInstances) {
			if (controlNodeInstance.getControlNodeId() == controlNodeId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the list executingGateways contains the control node.
	 *
	 * @param controlNodeId This is the database id from the control node.
	 * @return true if the executingGateways contains the control node. false if not.
	 */
	public boolean executingGatewaysContainControlNodeID(int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : executingGateways) {
			if (controlNodeInstance.getControlNodeId() == controlNodeId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check the termination condition.
	 * Get all termination condition and prove the condition for every condition set.
	 *
	 * @return true if the condition is true. false if not.
	 */
	public boolean checkTerminationCondition() {
        this.canTerminate = this.terminationCondition.
                checkTerminationCondition(this.dataObjectInstances);
        return this.canTerminate;
	}


	/**
	 * Terminates a scenario instance.
	 * Write the termination in the database and clears all lists.
	 */
    public void terminate() {
		dbScenarioInstance.setTerminated(scenarioInstanceId, true);
		controlNodeInstances.clear();
		enabledControlNodeInstances.clear();
		controlFlowEnabledControlNodeInstances.clear();
		dataEnabledControlNodeInstances.clear();
		runningControlNodeInstances.clear();
		terminatedControlNodeInstances.clear();
	}

	/**
	 * Starts automatic running control node instances.
	 * For example it starts the email tasks.
	 */
	@SuppressWarnings("unchecked") public void startAutomaticControlNodes() {
		for (AbstractControlNodeInstance controlNodeInstance : (
				(LinkedList<AbstractControlNodeInstance>)
						enabledControlNodeInstances
				.clone())) {
			if (controlNodeInstance.getClass() == ActivityInstance.class
					&& ((ActivityInstance) controlNodeInstance)
					.isAutomaticExecution()) {
				((ActivityInstance) controlNodeInstance).begin();
			}
		}
	}

	/**
	 * Get the control node instance for a given control node id.
	 *
	 * @param controlNodeId This is a id of a control node.
	 * @return the control instance for the given control node id.
	 */
	public AbstractControlNodeInstance getControlNodeInstanceForControlNodeId(
			int controlNodeId) {
		for (AbstractControlNodeInstance controlNodeInstance : controlNodeInstances) {
			if (controlNodeInstance.getControlNodeId() == controlNodeId) {
				return controlNodeInstance;
			}
		}
		return null;
	}
	// ******************** Getter ********************//

	/**
	 * @return the scenario instance id.
	 */
	public int getScenarioInstanceId() {
		return scenarioInstanceId;
	}

	/**
	 * @return the scenario id.
	 */
	public int getScenarioId() {
		return scenarioId;
	}

	/**
	 * @return the name of the scenario instance.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a LinkedList of control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getControlNodeInstances() {
		return controlNodeInstances;
	}

	/**
	 * @return a LinkedList of enabled control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getEnabledControlNodeInstances() {
		return enabledControlNodeInstances;
	}

	/**
	 * @return a LinkedList of flow enabled control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getControlFlowEnabledControlNodeInstances() {
		return controlFlowEnabledControlNodeInstances;
	}

	/**
	 * @return a LinkedList of data enabled control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getDataEnabledControlNodeInstances() {
		return dataEnabledControlNodeInstances;
	}

	/**
	 * @return a LinkedList of running control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getRunningControlNodeInstances() {
		return runningControlNodeInstances;
	}

	/**
	 * @return a LinkedList of terminated control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getTerminatedControlNodeInstances() {
		return terminatedControlNodeInstances;
	}

	/**
	 * @return a LinkedList of data object instances.
	 */
	public LinkedList<DataObjectInstance> getDataObjectInstances() {
		return dataObjectInstances;
	}

	/**
	 * @return a LinkedList of data object instances on change.
	 */
	public LinkedList<DataObjectInstance> getDataObjectInstancesOnChange() {
		return dataObjectInstancesOnChange;
	}

	/**
	 * @return a LinkedList of referential running control node instances.
	 */
	public LinkedList<AbstractControlNodeInstance> getReferentialRunningControlNodeInstances() {
		return referentialRunningControlNodeInstances;
	}

	/**
	 * @return a LinkedList of executing gateways.
	 */
	public LinkedList<GatewayInstance> getExecutingGateways() {
		return executingGateways;
	}

	/**
	 * @return a Map with DataAttributeInstances.
	 */
	public Map<Integer, DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

    public boolean canTerminate() {
        return canTerminate;
    }
}
