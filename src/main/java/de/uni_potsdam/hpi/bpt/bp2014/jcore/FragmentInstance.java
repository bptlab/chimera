package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;

import java.util.LinkedList;


/**
 * Represents a fragment instance.
 */
public class FragmentInstance {
    private final ScenarioInstance scenarioInstance;
    private final int fragment_id;
    private final int fragmentInstance_id;
    private final int scenarioInstance_id;
    /**
     * Database Connection objects
     */
    private final DbFragmentInstance dbFragmentInstance = new DbFragmentInstance();
    private final DbControlNode dbControlNode = new DbControlNode();
    private final DbControlFlow dbControlFlow = new DbControlFlow();
    private final DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();

    /**
     * Creates and initializes a new fragment instance.
     * Reads the information for an existing fragment instance from the database or creates a new one if no one
     * exist in the database.
     *
     * @param fragment_id         This is the database id from the fragment.
     * @param scenarioInstance_id This is the database id from the scenario instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     */
    public FragmentInstance(int fragment_id, int scenarioInstance_id, ScenarioInstance scenarioInstance) {
        this.scenarioInstance = scenarioInstance;
        this.fragment_id = fragment_id;
        this.scenarioInstance_id = scenarioInstance_id;
        if (dbFragmentInstance.existFragment(fragment_id, scenarioInstance_id)) {
            //creates an existing Fragment Instance using the database information
            fragmentInstance_id = dbFragmentInstance.getFragmentInstanceID(fragment_id, scenarioInstance_id);
            this.initializeExistingNodeInstanceForFragment();
        } else {
            //creates a new Fragment Instance also in database
            this.fragmentInstance_id = dbFragmentInstance.createNewFragmentInstance(fragment_id, scenarioInstance_id);
            this.initializeNewNodeInstanceForFragment();
        }
    }


    /**
     * Creates and initializes control node instances from the database
     */
    private void initializeExistingNodeInstanceForFragment() {
        //initializes all Activity Instances in the database
        LinkedList<Integer> activities = dbControlNodeInstance.getActivitiesForFragmentInstanceID(fragmentInstance_id);
        LinkedList<Integer> activityInstances = dbControlNodeInstance.getActivityInstancesForFragmentInstanceID(fragmentInstance_id);
        for (int i = 0; activities.size() > i; i++) {
            new ActivityInstance(activities.get(i), fragmentInstance_id, scenarioInstance, activityInstances.get(i));
        }
        //initializes all Gateway Instances in the database
        LinkedList<Integer> gateways = dbControlNodeInstance.getGatewaysForFragmentInstanceID(fragmentInstance_id);
        LinkedList<Integer> gatewayInstances = dbControlNodeInstance.getGatewayInstancesForFragmentInstanceID(fragmentInstance_id);
        for (int i = 0; gateways.size() > i; i++) {
            new GatewayInstance(gateways.get(i), fragmentInstance_id, scenarioInstance, gatewayInstances.get(i));
        }
    }

    /**
     * Creates new control node instances.
     * Write the new instances in the database
     */
    private void initializeNewNodeInstanceForFragment() {
        //gets the Start Event and then the following Control Node to initialize it
        int startEvent = dbControlNode.getStartEventID(fragment_id);
        int controlNode = dbControlFlow.getNextControlNodeAfterStartEvent(startEvent);
        String controlNodeType = dbControlNode.getType(controlNode);
        ControlNodeInstance controlNodeInstance = null;
        switch (controlNodeType) {
            case "Activity":
            case "EmailTask":
            case "WebServiceTask":
                controlNodeInstance = new ActivityInstance(controlNode, fragmentInstance_id, scenarioInstance);
                break;
            case "AND":
            case "XOR":
                controlNodeInstance = new GatewayInstance(controlNode, fragmentInstance_id, scenarioInstance);
                break;
        }
        controlNodeInstance.enableControlFlow();
    }

    /**
     * Sets the fragment instances to terminated in the database.
     */
    public void terminate() {
        dbFragmentInstance.terminateFragmentInstance(fragmentInstance_id);
    }

    // ****************************** Getter **********************************

    /**
     * @return
     */
    public ScenarioInstance getScenarioInstance() {
        return scenarioInstance;
    }

    /**
     * @return
     */
    public int getFragment_id() {
        return fragment_id;
    }

    /**
     * @return
     */
    public int getFragmentInstance_id() {
        return fragmentInstance_id;
    }

    /**
     * @return
     */
    public int getScenarioInstance_id() {
        return scenarioInstance_id;
    }
}
