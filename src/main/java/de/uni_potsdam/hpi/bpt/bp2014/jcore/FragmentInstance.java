package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AndGatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.EventBasedGatewayInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.EventFactory;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.StartEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.XorGatewayInstance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a fragment instance.
 */
public class FragmentInstance {
	private final ScenarioInstance scenarioInstance;
	private final int fragmentId;
	private int fragmentInstanceId; //final
	private int scenarioInstanceId; //final
	/**
	 * Database Connection objects
	 */
	private final DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
	private final DbControlNode dbControlNode = new DbControlNode();
	private final DbControlFlow dbControlFlow = new DbControlFlow();
	private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();

	private Boolean start = false;

	/**
	 * Creates and initializes a new fragment instance.
	 * Reads the information for an existing fragment instance from the database
	 * or creates a new one if none exist in the database.
	 *
	 * @param fragmentId         This is the database id from the fragment.
	 * @param scenarioInstanceId This is the database id from the scenario instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 */
	public FragmentInstance(int fragmentId, int scenarioInstanceId,
			ScenarioInstance scenarioInstance) {
		this.scenarioInstance = scenarioInstance;
		this.fragmentId = fragmentId;
		this.scenarioInstanceId = scenarioInstanceId;
        this.createDatabaseFragmentInstance();
	}


    public List<AbstractEvent> getRegisteredEvents() {
        DbEventMapping mapping = new DbEventMapping();
        EventFactory factory = new EventFactory(this.scenarioInstance);
        List<AbstractEvent> events = new ArrayList<>();
        for (Integer event : mapping.getRegisteredEventsForFragment(this.fragmentInstanceId)) {
            events.add(factory.getEventForControlNodeId(event, this.fragmentInstanceId));
        }

        return events;
    }

	public void createDatabaseFragmentInstance () {
		if (dbFragmentInstance.existFragment(fragmentId, scenarioInstanceId)) {
			//creates an existing DatabaseFragment Instance using the database information
			this.fragmentInstanceId = dbFragmentInstance
					.getFragmentInstanceID(fragmentId, scenarioInstanceId);
			this.initializeExistingNodeInstanceForFragment();
		} else {
			//creates a new DatabaseFragment Instance also in database
			this.fragmentInstanceId = dbFragmentInstance
					.createNewFragmentInstance(fragmentId, scenarioInstanceId);
			this.initializeNewNodeInstanceForFragment();
		}
	}

	/**
	 * Creates and initializes control node instances from the database
	 */
	private void initializeExistingNodeInstanceForFragment() {
		//initializes all Activity Instances in the database
		LinkedList<Integer> activities = dbControlNodeInstance
				.getActivitiesForFragmentInstanceID(fragmentInstanceId);
		LinkedList<Integer> activityInstances = dbControlNodeInstance
				.getActivityInstancesForFragmentInstanceID(fragmentInstanceId);
		for (int i = 0; activities.size() > i; i++) {
			new ActivityInstance(
					activities.get(i), fragmentInstanceId,
					scenarioInstance, activityInstances.get(i));
		}
		//initializes all Gateway Instances in the database
		LinkedList<Integer> gateways = dbControlNodeInstance
				.getGatewaysForFragmentInstanceID(fragmentInstanceId);
		LinkedList<Integer> gatewayInstances = dbControlNodeInstance
				.getGatewayInstancesForFragmentInstanceID(fragmentInstanceId);
		for (int i = 0; gateways.size() > i; i++) {
			switch(dbControlNode.getType(gateways.get(i))) {
				case "AND":
					new AndGatewayInstance(gateways.get(i), fragmentInstanceId, scenarioInstance,
							gatewayInstances.get(i));
					break;
				case "XOR":
					new XorGatewayInstance(gateways.get(i), fragmentInstanceId, scenarioInstance,
							gatewayInstances.get(i));
					break;
				case "EVENT_BASED":
					new EventBasedGatewayInstance(gateways.get(i), fragmentInstanceId, scenarioInstance,
							gatewayInstances.get(i));
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
        StartEvent startEvent = new StartEvent(startEventDatabaseId,
                this.fragmentInstanceId, this.scenarioInstance);
        startEvent.enableControlFlow();
	}

	/**
	 * Sets the fragment instances to terminated in the database.
	 */
	public void terminate() {
		dbFragmentInstance.terminateFragmentInstance(fragmentInstanceId);
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
	public int getFragmentInstanceId() {
		return fragmentInstanceId;
	}

	/**
	 * @return the scenario instance id.
	 */
	public int getScenarioInstanceId() {
		return scenarioInstanceId;
	}

}
