package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbState;

import java.util.HashMap;
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


//handles all Scenario Instances, can create new Instances, can activate Activities
public class ExecutionService {
    /**
     * This are the Lists for all opened scenario instances.
     */
    private LinkedList<ScenarioInstance> scenarioInstances = new LinkedList<ScenarioInstance>();
    private HashMap<Integer, ScenarioInstance> sortedScenarioInstances = new HashMap<Integer, ScenarioInstance>();
    /**
     * Database Connection.
     */
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbScenario dbScenario = new DbScenario();
    private DbControlNode dbControlNode = new DbControlNode();

    public ExecutionService() {
    }

    /**
     * Starts a new scenario instance for the given scenario id.
     * @param scenario_id This is the id of the scenario.
     * @return the id of the new scenario instance.
     */
    public int startNewScenarioInstance(int scenario_id) {
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenario_id);
        scenarioInstances.add(scenarioInstance);
        sortedScenarioInstances.put(scenarioInstance.getScenarioInstance_id(), scenarioInstance);
        return scenarioInstance.getScenarioInstance_id();
    }

    /**
     * Open a existing scenario instance.
     * @param scenario_id This is the id of the scenario.
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return true if the scenario instance could been open and exist. false if the scenario instance couldn't been open.
     */
     public boolean openExistingScenarioInstance(int scenario_id, int scenarioInstance_id) {
        if (!sortedScenarioInstances.containsKey(scenarioInstance_id)) {
            if (existScenarioInstance(scenarioInstance_id)) {
                ScenarioInstance scenarioInstance = new ScenarioInstance(scenario_id, scenarioInstance_id);
                scenarioInstances.add(scenarioInstance);
                sortedScenarioInstances.put(scenarioInstance_id, scenarioInstance);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Gives all scenarios in the database.
     * @return all list with all ids of all scenarios in the database.
     */
    public LinkedList<Integer> getAllScenarioIDs() {
        return dbScenario.getScenarioIDs();
    }

    /**
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return true if the scenario instance have been open. false if not.
     */
    public boolean scenarioInstanceIsRunning(int scenarioInstance_id) {
        return sortedScenarioInstances.containsKey(scenarioInstance_id);
    }

    public boolean existScenarioInstance(int scenarioInstance_id) {
        if (dbScenarioInstance.existScenario(scenarioInstance_id)) return true;
        return false;
    }

    public boolean existScenarioInstance(int scenario_id, int scenarioInstance_id) {
        if (dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id)) return true;
        return false;
    }

    public boolean existScenario(int scenario_id) {
        if (dbScenario.existScenario(scenario_id)) return true;
        return false;
    }

    public LinkedList<Integer> listAllScenarioInstancesForScenario(int scenario_id) {
        return dbScenarioInstance.getScenarioInstances(scenario_id);
    }

    public int getScenarioIDForScenarioInstance(int scenarioInstance_id) {
        return dbScenarioInstance.getScenarioID(scenarioInstance_id);
    }

    public LinkedList<Integer> getEnabledActivitiesIDsForScenarioInstance(int scenarioInstance_id) {
        LinkedList<Integer> ids = new LinkedList<Integer>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }
        return ids;
    }

    public HashMap<Integer, String> getEnabledActivityLabelsForScenarioInstance(int scenarioInstance_id) {
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                labels.put(((ActivityInstance) nodeInstance).controlNode_id, ((ActivityInstance) nodeInstance).getLabel());
            }
        }
        return labels;
    }

    public LinkedList<Integer> getRunningActivitiesIDsForScenarioInstance(int scenarioInstance_id) {
        LinkedList<Integer> ids = new LinkedList<Integer>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }
        return ids;
    }

    public HashMap<Integer, String> getRunningActivityLabelsForScenarioInstance(int scenarioInstance_id) {
        HashMap<Integer, String> labels = new HashMap<Integer, String>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                labels.put(((ActivityInstance) nodeInstance).controlNode_id, ((ActivityInstance) nodeInstance).getLabel());
            }
        }
        return labels;
    }

    public boolean beginActivity(int scenarioInstance_id, int activity_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNode_id == activity_id) {
                return ((ActivityInstance) nodeInstance).begin();
            }
        }
        return false;
    }

    public String getLabelForControlNodeID(int controlNode_id) {
        return dbControlNode.getLabel(controlNode_id);
    }

    public boolean terminateActivity(int scenarioInstance_id, int activity_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNode_id == activity_id) {
                return ((ActivityInstance) nodeInstance).terminate();
            }
        }
        return false;
    }

    public LinkedList<Integer> getAllDataObjectIDs(int scenarioInstance_id) {
        LinkedList<Integer> dataObjectIDs = new LinkedList<Integer>();
        for (DataObjectInstance dataObject : sortedScenarioInstances.get(scenarioInstance_id).getDataObjectInstances())
            dataObjectIDs.add(dataObject.getDataObject_id());
        return dataObjectIDs;
    }

    public HashMap<Integer, String> getAllDataObjectStates(int scenarioInstance_id) {
        DbState dbState = new DbState();
        HashMap<Integer, String> dataObjectStates = new HashMap<Integer, String>();
        for (DataObjectInstance dataObject : sortedScenarioInstances.get(scenarioInstance_id).getDataObjectInstances()) {
            dataObjectStates.put(dataObject.getDataObject_id(), dbState.getStateName(dataObject.getState_id()));
        }
        return dataObjectStates;
    }

    public boolean checkTerminationForScenarioInstance(int scenarioInstance) {
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        if (dbScenarioInstance.getTerminated(scenarioInstance) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public String getScenarioName(int scenario_id) {
        return dbScenario.getScenarioName(scenario_id);
    }

    public String getScenarioNameForScenarioInstance(int scenarioInstance_id) {
        return dbScenario.getScenarioName(dbScenarioInstance.getScenarioID(scenarioInstance_id));
    }

    //for tests
    public ScenarioInstance getScenarioInstance(int scenarioInstanceID) {
        return sortedScenarioInstances.get(scenarioInstanceID);
    }
}
