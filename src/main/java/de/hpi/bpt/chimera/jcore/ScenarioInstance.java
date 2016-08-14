package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.database.DbScenario;
import de.hpi.bpt.chimera.database.DbScenarioInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.*;
import de.hpi.bpt.chimera.database.controlnodes.events.DbEventMapping;
import de.hpi.bpt.chimera.database.DbFragment;
import de.hpi.bpt.chimera.jcore.data.DataAttributeInstance;
import de.hpi.bpt.chimera.jcore.data.DataManager;
import de.hpi.bpt.chimera.jcore.data.TerminationCondition;

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
	private final int id;
	private final int scenarioId;
	private final String name;
	/**
	 * database connection objects.
	 */
	private final DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
	private final DbFragment dbFragment = new DbFragment();
	private final DbScenario dbScenario = new DbScenario();
	/**
	 * Lists to save all fragments and all control nodes sorted by state.
	 */
	private List<AbstractControlNodeInstance> controlNodeInstances = new ArrayList<>();

    private List<FragmentInstance> fragmentInstances = new ArrayList<>();
	private Map<Integer, DataAttributeInstance> dataAttributeInstances = new HashMap<>();


    private DataManager dataManager;

    private boolean canTerminate;
    private TerminationCondition terminationCondition;
    /**
     * Creates and initializes a new scenario instance.
     * Also save this new instance in database.
     *
     * @param scenarioId This is the database id from the scenario.
     */
    public ScenarioInstance(int scenarioId) {
        this.name = dbScenario.getScenarioName(scenarioId);
        this.scenarioId = scenarioId;
        this.id = dbScenarioInstance.createNewScenarioInstance(scenarioId);

        // Initialize data manager after setting scenarioId
        this.dataManager = new DataManager(this);
        this.terminationCondition = new TerminationCondition(this);
        this.initializeFragments();
        this.startAutomaticControlNodes();
    }

	/**
	 * Creates and initializes a new scenario instance from database.
	 * Reads the information for an existing scenario instance from the database.
	 * If there is no match in the database it creates a new scenario instance.
	 *
	 * @param scenarioId         This is the database id from the scenario.
	 * @param id This is the database id from the scenario instance.
	 */
	public ScenarioInstance(int scenarioId, int id) {
        this.name = dbScenario.getScenarioName(scenarioId);
		this.scenarioId = scenarioId;
		this.terminationCondition = new TerminationCondition(this);
        this.id = id;
        this.dataManager = new DataManager(this);
        this.reloadFromDatabase();

		if (dbScenarioInstance.getTerminated(this.id) == 0) {
			this.initializeFragments();
		}

        this.startAutomaticControlNodes();
        canTerminate = checkTerminationCondition();
    }

    private void reloadFromDatabase() {
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        List<Integer> controlNodeInstanceIds = dbControlNodeInstance.getControlNodeInstances(
                this.id);
        controlNodeInstanceIds.forEach(x -> ControlNodeFactory.loadControlNodeInstance(x, this));
    }

	/**
	 * Creates and initializes all fragments for the scenario.
	 */
	private void initializeFragments() {
		List<Integer> fragmentIds = dbFragment.getFragmentsForScenario(scenarioId);
		for (int fragmentId : fragmentIds) {
			this.initializeFragment(fragmentId);
		}
	}

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
            Integer fragmentInstanceId = fragmentInstance.getId();
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
				fragmentId, id,	this);
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
			if (f.getId() == fragmentInstanceId) {
				fragmentInstance = f;
			}
		}
		fragmentInstances.remove(fragmentInstance);
		if (fragmentInstance != null) {
			fragmentInstance.terminate();
		}

		//removes the old control node instances
		List<AbstractControlNodeInstance> updatedList = new ArrayList<>(
				this.getTerminatedControlNodeInstances());
		for (AbstractControlNodeInstance controlNodeInstance : updatedList) {
			if (controlNodeInstance.getFragmentInstanceId() == fragmentInstanceId) {
				this.controlNodeInstances.remove(controlNodeInstance);
			}
		}
		updatedList = new ArrayList<>(controlNodeInstances);
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
	 * Checks if the control flow enabled control nodes can set to data flow enabled.
	 */
	public void updateDataFlow() {
		for (AbstractControlNodeInstance activityInstance : controlNodeInstances) {
			if (activityInstance.getClass() == ActivityInstance.class) {
				((ActivityInstance) activityInstance).checkDataFlowEnabled();
			}
		}
        checkTerminationCondition();
    }

    /**
     * Checks whether the
     * @param controlNodeInstanceId The id of the control node instance, which was transferred
     *                              to the state ready.
     */
    public void skipAlternativeControlNodes(int controlNodeInstanceId) {
        List<XorGatewayInstance> gateways = this.getExecutingGateways();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        int controlNodeId = dbControlNodeInstance.getControlNodeId(controlNodeInstanceId);
        for (XorGatewayInstance gateway : gateways) {
            if (gateway.containsControlNodeInFollowing(controlNodeId)) {
                gateway.skipAlternativeBranches(controlNodeId);
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
		for (AbstractControlNodeInstance controlNodeInstance :
                this.getTerminatedControlNodeInstances()) {
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
		for (AbstractControlNodeInstance controlNodeInstance : getExecutingGateways()) {
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
                checkTerminationCondition(this.dataManager.getDataObjects());
        return this.canTerminate;
	}


	/**
	 * Terminates a scenario instance.
	 * Write the termination in the database and clears all lists.
	 */
    public void terminate() {
		dbScenarioInstance.setTerminated(id, true);
		controlNodeInstances.clear();
	}

	/**
	 * Starts automatic running control node instances.
	 * For example it starts the email tasks.
	 */
	@SuppressWarnings("unchecked") public void startAutomaticControlNodes() {
		List<AbstractControlNodeInstance> instancesClone = new ArrayList<>(
                this.getEnabledControlNodeInstances());
        for (AbstractControlNodeInstance controlNodeInstance : instancesClone) {
			if (controlNodeInstance.getClass() == ActivityInstance.class
					&& ((ActivityInstance) controlNodeInstance).isAutomaticTask()) {
				((ActivityInstance) controlNodeInstance).begin();
			}
		}
        // Don't begin tasks at the moment.
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
	public int getId() {
		return id;
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
	 * @return a ArrayList of control node instances.
	 */
	public List<AbstractControlNodeInstance> getControlNodeInstances() {
		return controlNodeInstances;
	}

	/**
	 * @return a ArrayList of enabled control node instances.
	 */
	public List<AbstractControlNodeInstance> getEnabledControlNodeInstances() {
        return this.controlNodeInstances.stream().filter(x -> x.getState().equals(State.READY))
                .collect(Collectors.toList());
	}

	/**
	 * @return a ArrayList of flow enabled control node instances.
	 */
	public List<AbstractControlNodeInstance> getControlFlowEnabledControlNodeInstances() {
        return this.controlNodeInstances.stream().filter(x -> x.getState().equals(
                State.CONTROLFLOW_ENABLED)).collect(Collectors.toList());
	}

	/**
	 * @return a ArrayList of data enabled control node instances.
	 */
	public List<AbstractControlNodeInstance> getDataEnabledControlNodeInstances() {
		return this.controlNodeInstances.stream().filter(x -> x.getState().equals(
                State.DATAFLOW_ENABLED)).collect(Collectors.toList());
	}

	/**
	 * @return a ArrayList of running control node instances.
	 */
	public List<AbstractControlNodeInstance> getRunningControlNodeInstances() {
        return this.controlNodeInstances.stream().filter(x -> x.getState().equals(
                State.RUNNING)).collect(Collectors.toList());
	}

	/**
	 * @return a ArrayList of terminated control node instances.
	 */
	public List<AbstractControlNodeInstance> getTerminatedControlNodeInstances() {
        return this.controlNodeInstances.stream().filter(x -> x.getState().equals(
                State.TERMINATED)).collect(Collectors.toList());
    }

	public List<FragmentInstance> getFragmentInstances() {
		return fragmentInstances;
	}

	/**
	 * @return a ArrayList of executing gateways.
	 */
	public List<XorGatewayInstance> getExecutingGateways() {
        return this.controlNodeInstances.stream()
                .filter(x -> x.getClass() == XorGatewayInstance.class)
                .map(x -> (XorGatewayInstance) x)
                .collect(Collectors.toList());
	}

	/**
	 * @return a Map with DataAttributeInstances.
	 */
	@Deprecated
	public Map<Integer, DataAttributeInstance> getDataAttributeInstances() {
		return dataAttributeInstances;
	}

    public boolean canTerminate() {
        return canTerminate;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
