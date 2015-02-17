package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;


import java.util.LinkedList;


/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */



/*
represents a scenario instance
the constructor looks for an scenario instance in the database or create a new one in the database
the constructor also initialize the fragment instances and data object instances
the scenario instance has Lists for all enabled, control flow enabled, data flow enabled, running and terminated activity
instances, fragment instances and all data object instances
the scenario instance provide methods for the administration of the data object instances
 */
public class ScenarioInstance {
    private LinkedList<ControlNodeInstance> controlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<ControlNodeInstance> enabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<ControlNodeInstance> controlFlowEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<ControlNodeInstance> dataEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<ControlNodeInstance> runningControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<ControlNodeInstance> terminatedControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<DataObjectInstance> dataObjectInstances = new LinkedList<DataObjectInstance>();
    private LinkedList<DataObjectInstance> dataObjectInstancesOnChange = new LinkedList<DataObjectInstance>();
    private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<FragmentInstance>();
    private LinkedList<ControlNodeInstance> referentialRunningControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private int scenarioInstance_id;
    private int scenario_id;
    private String name;
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbFragment dbFragment = new DbFragment();
    private DbDataObject dbDataObject = new DbDataObject();
    private DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();
    private DbScenario dbScenario = new DbScenario();

    //open existing scenario instance
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
    }

    //starts a new scenario instance
    public ScenarioInstance(int scenario_id) {
        this.name = dbScenario.getScenarioName(scenario_id);
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = dbScenarioInstance.createNewScenarioInstance(scenario_id);
        this.initializeDataObjects();
        this.initializeFragments();
    }

    private void initializeFragments() {
        LinkedList<Integer> fragment_ids = dbFragment.getFragmentsForScenario(scenario_id);
        for (int fragment_id : fragment_ids) {
            this.initializeFragment(fragment_id);
        }
    }

    public void initializeFragment(int fragment_id) {
        FragmentInstance fragmentInstance = new FragmentInstance(fragment_id, scenarioInstance_id, this);
        fragmentInstances.add(fragmentInstance);
    }

    public void restartFragment(int fragmentInstance_id) {
        FragmentInstance fragmentInstance = null;
        for (FragmentInstance f : fragmentInstances) {
            if (f.getFragmentInstance_id() == fragmentInstance_id) fragmentInstance = f;
        }
        fragmentInstances.remove(fragmentInstance);
        fragmentInstance.terminate();

        //removes the old control node instances
        LinkedList<ControlNodeInstance> updatedList = new LinkedList<ControlNodeInstance>(terminatedControlNodeInstances);
        for (ControlNodeInstance controlNodeInstance : updatedList) {
            if (controlNodeInstance.fragmentInstance_id == fragmentInstance_id) {
                terminatedControlNodeInstances.remove(controlNodeInstance);
            }
        }
        updatedList = new LinkedList<ControlNodeInstance>(controlNodeInstances);
        for (ControlNodeInstance controlNodeInstance : updatedList) {
            if (controlNodeInstance.fragmentInstance_id == fragmentInstance_id) {
                controlNodeInstances.remove(controlNodeInstance);
            }
        }
        initializeFragment(fragmentInstance.getFragment_id());
    }

    public void initializeDataObjects() {
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

    public Boolean checkDataObjectState(int dataObject_id, int state_id) {
        for (DataObjectInstance dataObjectInstance : dataObjectInstances) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id && dataObjectInstance.getState_id() == state_id)
                return true;
        }
        return false;
    }


    public Boolean changeDataObjectInstanceState(int dataObject_id, int state_id) {
        for (DataObjectInstance dataObjectInstance : dataObjectInstances) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstance.setState(state_id);
                return true;
            }
        }
        return false;
    }

    public Boolean setDataObjectInstanceToOnChange(int dataObject_id) {
        DataObjectInstance dataObjectInstanceOnChange = null;
        for (DataObjectInstance dataObjectInstance : dataObjectInstances) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if (dataObjectInstanceOnChange != null) {
            dataObjectInstances.remove(dataObjectInstanceOnChange);
            dataObjectInstancesOnChange.add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(true);
            return true;
        }
        return false;
    }

    public Boolean setDataObjectInstanceToNotOnChange(int dataObject_id) {
        DataObjectInstance dataObjectInstanceOnChange = null;
        for (DataObjectInstance dataObjectInstance : dataObjectInstancesOnChange) {
            if (dataObjectInstance.getDataObject_id() == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if (dataObjectInstanceOnChange != null) {
            dataObjectInstancesOnChange.remove(dataObjectInstanceOnChange);
            dataObjectInstances.add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(false);
            return true;
        }
        return false;
    }

    public void checkDataFlowEnabled() {
        for (ControlNodeInstance activityInstance : controlFlowEnabledControlNodeInstances) {
            if (activityInstance.getClass() == ActivityInstance.class) {
                ((ActivityInstance) activityInstance).checkDataFlowEnabled();
            }
        }
    }

    public Boolean terminatedControlNodeInstancesContainControlNodeID(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : terminatedControlNodeInstances) {
            if (controlNodeInstance.controlNode_id == controlNode_id) return true;
        }
        return false;
    }

    /**
     * checks if the referenced controlNode can be started.
     * The referenced controlNode have to be control flow enabled and (data flow enabled or must have the same data output)
     *
     * @param controlNode_id
     * @param referencedControlNode_id
     */
    public void beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(int controlNode_id, int referencedControlNode_id) {
        for (ControlNodeInstance controlNodeInstance : controlFlowEnabledControlNodeInstances) {
            if (controlNodeInstance.controlNode_id == referencedControlNode_id) {
                if (controlNodeInstance.getClass() == ActivityInstance.class) {
                    DbControlNode dbControlNode = new DbControlNode();
                    if (enabledControlNodeInstances.contains(controlNodeInstance) || dbControlNode.controlNodesHaveSameOutputs(controlNode_id, referencedControlNode_id)) {
                        ((ActivityInstance) controlNodeInstance).referenceStarted();
                        return;
                    }
                }
            }
        }

    }

    /**
     * checks if the referenced controlNode can be terminated.
     * The referenced controlNode have to be referential running
     *
     * @param controlNode_id
     */
    public void terminateReferenceControlNodeInstanceForControlNodeInstanceID(int controlNode_id) {
        for (ControlNodeInstance controlNodeInstance : referentialRunningControlNodeInstances) {
            if (controlNodeInstance.controlNode_id == controlNode_id) {
                if (controlNodeInstance.getClass() == ActivityInstance.class) {
                    ((ActivityInstance) controlNodeInstance).referenceTerminated();
                    return;
                }
            }
        }
    }

    /**
     * check termination condition
     * get all termination condition and prove the condition for every condition set
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
            if (terminated) break;
        }
        //terminate the scenario
        if (terminated) {
            this.terminate();
        }
        return terminated;
    }

    private void terminate() {
        dbScenarioInstance.setTerminated(scenarioInstance_id, true);
        controlNodeInstances.clear();
        enabledControlNodeInstances.clear();
        controlFlowEnabledControlNodeInstances.clear();
        dataEnabledControlNodeInstances.clear();
        runningControlNodeInstances.clear();
        terminatedControlNodeInstances.clear();
    }

    /*
     * Getter
     */

    public int getScenarioInstance_id() {
        return scenarioInstance_id;
    }

    public int getScenario_id() {
        return scenario_id;
    }

    public String getName() {
        return name;
    }

    public LinkedList<ControlNodeInstance> getControlNodeInstances() {
        return controlNodeInstances;
    }

    public LinkedList<ControlNodeInstance> getEnabledControlNodeInstances() {
        return enabledControlNodeInstances;
    }

    public LinkedList<ControlNodeInstance> getControlFlowEnabledControlNodeInstances() {
        return controlFlowEnabledControlNodeInstances;
    }

    public LinkedList<ControlNodeInstance> getDataEnabledControlNodeInstances() {
        return dataEnabledControlNodeInstances;
    }

    public LinkedList<ControlNodeInstance> getRunningControlNodeInstances() {
        return runningControlNodeInstances;
    }

    public LinkedList<ControlNodeInstance> getTerminatedControlNodeInstances() {
        return terminatedControlNodeInstances;
    }

    public LinkedList<DataObjectInstance> getDataObjectInstances() {
        return dataObjectInstances;
    }

    public LinkedList<DataObjectInstance> getDataObjectInstancesOnChange() {
        return dataObjectInstancesOnChange;
    }

    public LinkedList<ControlNodeInstance> getReferentialRunningControlNodeInstances() {
        return referentialRunningControlNodeInstances;
    }


}
