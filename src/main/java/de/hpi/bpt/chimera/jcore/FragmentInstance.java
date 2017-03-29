package de.hpi.bpt.chimera.jcore;

import de.hpi.bpt.chimera.database.DbFragmentInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.controlnodes.events.DbEventMapping;
import de.hpi.bpt.chimera.jcore.controlnodes.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a fragment instance.
 */
public class FragmentInstance {
	private final int fragmentId;
	private final ScenarioInstance scenarioInstance;
	/**
	 * Database ConnectionWrapper objects
	 */
	private final DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
	private final DbControlNode dbControlNode = new DbControlNode();
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
	private int id;
	private int scenarioInstanceId;

	/**
	 * Creates and initializes a new fragment instance.
	 *
	 * @param fragmentId         This is the database id from the fragment.
	 * @param scenarioInstanceId This is the database id from the scenario instance.
	 * @param scenarioInstance   This is an instance from the class ScenarioInstance.
	 */
	public FragmentInstance(int fragmentId, int scenarioInstanceId, ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.fragmentId = fragmentId;
		this.scenarioInstanceId = scenarioInstanceId;
		this.createDatabaseFragmentInstance();
	}

	/**
	 * Recreates an existing fragment instance.
	 * Reloads Activities and Gateways in the process.
	 *
	 * @param fragmentId         This is the database id of the fragment.
	 * @param scenarioInstanceId This is the database id of the scenario instance.
	 * @param scenarioInstance   This is the corresponding scenario instance.
	 * @param fragmentInstanceId This is the id of the already existing fragment instance.
	 */
	public FragmentInstance(int fragmentId, int scenarioInstanceId, ScenarioInstance scenarioInstance, int fragmentInstanceId) {
		this.id = fragmentInstanceId;
		this.scenarioInstance = scenarioInstance;
		this.fragmentId = fragmentId;
		this.scenarioInstanceId = scenarioInstanceId;
		this.initializeExistingNodeInstanceForFragment();
	}


	List<AbstractEvent> getRegisteredEvents() {
		DbEventMapping mapping = new DbEventMapping();
		EventFactory factory = new EventFactory(this.scenarioInstance);
		return mapping.getRegisteredEventsForFragment(this.id).stream().map(event -> factory.getEventForControlNodeId(event, this.id)).collect(Collectors.toList());
	}

	private void createDatabaseFragmentInstance() {
		//creates a new DatabaseFragment Instance also in database
		this.id = dbFragmentInstance.createNewFragmentInstance(fragmentId, scenarioInstanceId);
		this.initializeNewNodeInstanceForFragment();
	}

	/**
	 * Creates and initializes control node instances from the database
	 */
	private void initializeExistingNodeInstanceForFragment() {
		//initializes all Activity Instances in the database
		List<Integer> activities = dbControlNodeInstance.getActivitiesForFragmentInstanceId(id);
		List<Integer> activityInstances = dbControlNodeInstance.getActivityInstancesForFragmentInstanceID(id);
		for (int i = 0; activities.size() > i; i++) {
			new ActivityInstance(activities.get(i), id, scenarioInstance, activityInstances.get(i));
		}
		//initializes all Gateway Instances in the database
		List<Integer> gateways = dbControlNodeInstance.getGatewaysForFragmentInstanceID(id);
		List<Integer> gatewayInstances = dbControlNodeInstance.getGatewayInstancesForFragmentInstanceId(id);
		for (int i = 0; gateways.size() > i; i++) {
			switch (dbControlNode.getType(gateways.get(i))) {
				case "AND":
					new AndGatewayInstance(gateways.get(i), id, scenarioInstance, gatewayInstances.get(i));
					break;
				case "XOR":
					new XorGatewayInstance(gateways.get(i), id, scenarioInstance, gatewayInstances.get(i));
					break;
				case "EVENT_BASED":
					new EventBasedGatewayInstance(gateways.get(i), id, scenarioInstance, gatewayInstances.get(i));
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Creates new control node instances.
	 * Write the new instances in the database
	 */
	private void initializeNewNodeInstanceForFragment() {
		//gets the Start Event and then the following Control Node to initialize it
		int startEventDatabaseId = dbControlNode.getStartEventID(fragmentId);
		StartEvent startEvent = new StartEvent(startEventDatabaseId, this.id, this.scenarioInstance);
		startEvent.enableControlFlow();
	}

	/**
	 * Sets the fragment instances to terminated in the database.
	 */
	public void terminate() {
		dbFragmentInstance.terminateFragmentInstance(id);
	}

	// ****************************** Getter **********************************

	/**
	 * @return the scenario instance.
	 */
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	/**
	 * @return the fragment id.
	 */
	public int getFragmentId() {
		return fragmentId;
	}

	/**
	 * @return the fragment instance id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the scenario instance id.
	 */
	public int getScenarioInstanceId() {
		return scenarioInstanceId;
	}

}
