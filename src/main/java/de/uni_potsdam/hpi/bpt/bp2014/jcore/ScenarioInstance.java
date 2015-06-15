package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Represents a scenario instance.
 * The constructor looks for an scenario instance in the database or create a new one in the database.
 * The constructor also initialize the fragment instances and data object instances.
 * The scenario instance has Lists for all enabled, control flow enabled, data flow enabled, running and
 * terminated activity instances, fragment instances and all data object instances.
 * The scenario instance provide methods for the administration of the data object instances
 */
public class ScenarioInstance {
    private final int scenarioInstance_id;
    private final int scenario_id;
    private final String name;
    /**
     * database connection objects.
     */
    private final DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private final DbFragment dbFragment = new DbFragment();
    private final DbDataObject dbDataObject = new DbDataObject();
    private final DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
    private final DbScenario dbScenario = new DbScenario();
    /**
     * Lists to save all fragments and all control nodes sorted by state.
     */
    private LinkedList<ControlNodeInstance> controlNodeInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> enabledControlNodeInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> controlFlowEnabledControlNodeInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> dataEnabledControlNodeInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> runningControlNodeInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> terminatedControlNodeInstances = new LinkedList<>();
    private LinkedList<DataObjectInstance> dataObjectInstances = new LinkedList<>();
    private LinkedList<DataObjectInstance> dataObjectInstancesOnChange = new LinkedList<>();
    private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<>();
    private LinkedList<ControlNodeInstance> referentialRunningControlNodeInstances = new LinkedList<>();
    private LinkedList<GatewayInstance> executingGateways = new LinkedList<>();
    private HashMap<Integer, DataAttributeInstance> dataAttributeInstances = new HashMap<>();

    /**
     * Creates and initializes a new scenario instance from database.
     * Reads the information for an existing scenario instance from the database.
     * If there is no match in the database it creates a new scenario instance.
     *
     * @param scenario_id         This is the database id from the scenario.
     * @param scenarioInstance_id This is the database id from the scenario instance.
     */
    public ScenarioInstance(int scenario_id, int scenarioInstance_id) {
        this.name = dbScenario.getScenarioName(scenario_id);
        this.scenario_id = scenario_id;
        if (dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id)) {
            //creates an existing Scenario Instance using the database information
            this.scenarioInstance_id = scenarioInstance_id;
        } else {
            //creates a new Scenario Instance also in database, using autoincrement to getting the scenario instances id
            this.scenarioInstance_id = dbScenarioInstance.createNewScenarioInstance(scenario_id);
        }
        this.initializeDataObjects();
        if (dbScenarioInstance.getTerminated(this.scenarioInstance_id) == 0) {
            this.initializeFragments();
        }
        this.startAutomaticControlNodes();
    }

    /**
     * Creates and initializes a new scenario instance.
     * Also save this new instance in database.
     *
     * @param scenario_id This is the database id from the scenario.
     */
    public ScenarioInstance(int scenario_id) {
        this.name = dbScenario.getScenarioName(scenario_id);
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = dbScenarioInstance.createNewScenarioInstance(scenario_id);
        this.initializeDataObjects();
        this.initializeFragments();
        this.startAutomaticControlNodes();
    }

    /**
     * Creates and initializes all fragments for the scenario.
     */
    private void initializeFragments() {
        LinkedList<Integer> fragment_ids = dbFragment.getFragmentsForScenario(scenario_id);
        for (int fragment_id : fragment_ids) {
            this.initializeFragment(fragment_id);
        }
    }

    /**
     * Creates and initializes the fragment with the specific fragment id.
     *
     * @param fragment_id This is the database id from the fragment.
     */
    private void initializeFragment(int fragment_id) {
        FragmentInstance fragmentInstance = new FragmentInstance(fragment_id, scenarioInstance_id, this);
        fragmentInstances.add(fragmentInstance);
    }

    /**
     * Restarts the fragment specified by the fragment id.
     * Removes all control nodes from the fragment from all lists and create and initializes the fragment again.
     *
     * @param fragmentInstance_id This is the database id from the fragment instance.
     */
    public void restartFragment(int fragmentInstance_id) {
        FragmentInstance fragmentInstance = null;
        for (FragmentInstance f : fragmentInstances) {
            if (f.getFragmentInstance_id() == fragmentInstance_id) {
                fragmentInstance = f;
            }
        }
        fragmentInstances.remove(fragmentInstance);
        fragmentInstance.terminate();

        //removes the old control node instances
        LinkedList<ControlNodeInstance> updatedList = new LinkedList<>(terminatedControlNodeInstances);
        for (ControlNodeInstance controlNodeInstance : updatedList) {
            if (controlNodeInstance.fragmentInstance_id == fragmentInstance_id) {
                terminatedControlNodeInstances.remove(controlNodeInstance);
            }
        }
        updatedList = new LinkedList<>(controlNodeInstances);
        for (ControlNodeInstance controlNodeInstance : updatedList) {
            if (controlNodeInstance.fragmentInstance_id == fragmentInstance_id) {
                controlNodeInstances.remove(controlNodeInstance);
            }
        }
        initializeFragment(fragmentInstance.getFragment_id());
        this.startAutomaticControlNodes();
    }

    /**
     * Initializes all data objects for the scenario instance.
     */
    private void initializeDataObjects() {
        LinkedList<Integer> data = dbDataObject.getDataObjectsForScenario(scenario_id);
        for (Integer dataObject : data) {
            DataObjectInstance dataObjectInstance = new DataObjectInstance(dataObject, scenario_id, scenarioInstance_id, this);
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
        for (ControlNodeInstance activityInstance : controlFlowEnabledControlNodeInstances) {
            if (activityInstance.getClass() == ActivityInstance.class) {
                ((ActivityInstance) activityInstance).checkDataFlowEnabled();
            }
        }
    }

    /**
     * Checks if a executing gateway can terminate.
     */
    public void checkExecutingGateways(int controlNode_id) {
        for (GatewayInstance gatewayInstance : ((LinkedList<GatewayInstance>) executingGateways.clone())) {
            if (gatewayInstance.checkTermination(controlNode_id)) {
                gatewayInstance.terminate();
            }
        }
    }

    /**
     * Checks if the list terminatedControlNodeInstances contains the control node.
     *
     * @param controlNode_id This is the database id from the control node.
     * @return true if the terminated control node instances contains the control node. false if not.
     */
    public boolean terminatedControlNodeInstancesContainControlNodeID(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : terminatedControlNodeInstances) {
            if (controlNodeInstance.controlNode_id == controlNode_id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the list executingGateways contains the control node.
     *
     * @param controlNode_id This is the database id from the control node.
     * @return true if the executingGateways contains the control node. false if not.
     */
    public boolean executingGatewaysContainControlNodeID(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : executingGateways) {
            if (controlNodeInstance.controlNode_id == controlNode_id) {
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
        boolean terminated = false;
        //get the condition Set IDs
        LinkedList<Integer> conditionsSets = dbTerminationCondition.getConditionsSetIDsForScenario(scenario_id);
        for (int conditionSet : conditionsSets) {
            LinkedList<Condition> conditions = dbTerminationCondition.getConditionsForConditionSetAndScenario(scenario_id, conditionSet);
            //prove every condition in condition set
            for (Condition condition : conditions) {
                DataObjectInstance dataObjectInstance = null;
                for (DataObjectInstance currentDataObjectInstance : dataObjectInstances) {
                    if (currentDataObjectInstance.getDataObject_id() == condition.getDataObject_id()) {
                        dataObjectInstance = currentDataObjectInstance;
                    }
                }
                if (dataObjectInstance != null) {
                    if (dataObjectInstance.getState_id() == condition.getState_id()) {
                        terminated = true;
                    } else {
                        terminated = false;
                        break;
                    }
                }
            }
            //termination condition is true
            if (terminated) {
                break;
            }
        }
        //terminate the scenario
        if (terminated) {
            this.terminate();
        }
        return terminated;
    }

    /**
     * Terminates a scenario instance.
     * Write the termination in the database and clears all lists.
     */
    private void terminate() {
        dbScenarioInstance.setTerminated(scenarioInstance_id, true);
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
    public void startAutomaticControlNodes() {
        for (ControlNodeInstance controlNodeInstance : ((LinkedList<ControlNodeInstance>) enabledControlNodeInstances.clone())) {
            if (controlNodeInstance.getClass() == ActivityInstance.class && ((ActivityInstance) controlNodeInstance).isAutomaticExecution()) {
                ((ActivityInstance) controlNodeInstance).begin();
            }
        }
    }

    /**
     * Get the control node instance for a given control node id.
     *
     * @param controlNode_id This is a id of a control node.
     * @return the control instance for the given control node id.
     */
    public ControlNodeInstance getControlNodeInstanceForControlNodeId(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : controlNodeInstances) {
            if (controlNodeInstance.getControlNode_id() == controlNode_id) {
                return controlNodeInstance;
            }
        }
        return null;
    }
    // ************************************************ Getter ***********************************************//


    /**
     * @return
     */
    public int getScenarioInstance_id() {
        return scenarioInstance_id;
    }

    /**
     * @return
     */
    public int getScenario_id() {
        return scenario_id;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getControlNodeInstances() {
        return controlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getEnabledControlNodeInstances() {
        return enabledControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getControlFlowEnabledControlNodeInstances() {
        return controlFlowEnabledControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getDataEnabledControlNodeInstances() {
        return dataEnabledControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getRunningControlNodeInstances() {
        return runningControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getTerminatedControlNodeInstances() {
        return terminatedControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<DataObjectInstance> getDataObjectInstances() {
        return dataObjectInstances;
    }

    /**
     * @return
     */
    public LinkedList<DataObjectInstance> getDataObjectInstancesOnChange() {
        return dataObjectInstancesOnChange;
    }

    /**
     * @return
     */
    public LinkedList<ControlNodeInstance> getReferentialRunningControlNodeInstances() {
        return referentialRunningControlNodeInstances;
    }

    /**
     * @return
     */
    public LinkedList<GatewayInstance> getExecutingGateways() {
        return executingGateways;
    }

    /**
     * @return
     */
    public HashMap<Integer, DataAttributeInstance> getDataAttributeInstances() {
        return dataAttributeInstances;
    }
}
