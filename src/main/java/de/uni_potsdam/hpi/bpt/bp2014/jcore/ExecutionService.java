package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


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


/**
 * Handles all scenario instances.
 */
public class ExecutionService {
    /**
     * This are the Lists for all opened scenario instances.
     */
    private final LinkedList<ScenarioInstance> scenarioInstances = new LinkedList<ScenarioInstance>();
    private final HashMap<Integer, ScenarioInstance> sortedScenarioInstances = new HashMap<Integer, ScenarioInstance>();
    /**
     * Database Connection.
     */
    private final DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private final DbScenario dbScenario = new DbScenario();
    private final DbControlNode dbControlNode = new DbControlNode();

    public ExecutionService() {
    }

    /**
     * Starts a new scenario instance for the given scenario id.
     *
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
     *
     * @param scenario_id         This is the id of the scenario.
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
     *
     * @return all list with all ids of all scenarios in the database.
     */
    public LinkedList<Integer> getAllScenarioIDs() {
        return dbScenario.getScenarioIDs();
    }

    /**
     * Checks if the scenario instance have been open.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return true if the scenario instance have been open. false if not.
     */
    public boolean scenarioInstanceIsRunning(int scenarioInstance_id) {
        return sortedScenarioInstances.containsKey(scenarioInstance_id);
    }

    /**
     * Checks if the scenario instance exist in the database.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return true if the scenario instance exist in the database. false if not.
     */
    public boolean existScenarioInstance(int scenarioInstance_id) {
        return dbScenarioInstance.existScenario(scenarioInstance_id);
    }

    /**
     * Checks if the scenario instance exist in the database.
     *
     * @param scenario_id         This is the id of the scenario.
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return true if the scenario instance exist in the database. false if not.
     */
    public boolean existScenarioInstance(int scenario_id, int scenarioInstance_id) {
        return dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id);
    }

    /**
     * Checks if the scenario exist in the database.
     *
     * @param scenario_id This is the id of the scenario.
     * @return true if the scenario exist in the database. false if not.
     */
    public boolean existScenario(int scenario_id) {
        return dbScenario.existScenario(scenario_id);
    }

    /**
     * Gives all scenario instance id for a scenario id.
     *
     * @param scenario_id This is the id of the scenario.
     * @return a list of all scenario instance ids for a the given scenario id.
     */
    public LinkedList<Integer> listAllScenarioInstancesForScenario(int scenario_id) {
        return dbScenarioInstance.getScenarioInstances(scenario_id);
    }

    /**
     * Gives the database id from the scenario for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return the database id for a scenario.
     */
    public int getScenarioIDForScenarioInstance(int scenarioInstance_id) {
        return dbScenarioInstance.getScenarioID(scenarioInstance_id);
    }

    /**
     * Gives all activity ids for a scenario instance which are enabled.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a list of the ids of activities.
     */
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

    /**
     * Returns the Labels of enabled activities for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a Map. Keys are the activity ids. Values are the labels of the activities.
     */
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

    /**
     * Gives all activity ids for a scenario instance which are running.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a list of the ids of activities.
     */
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

    /**
     * Returns the Labels of running activities for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a Map. Keys are the activity ids. Values are the labels of the activities.
     */
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

    /**
     * Starts the execution of an activity which is enabled.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activity_id         This is the id of the activity.
     * @return true if the activity could been started. false if not.
     */
    public boolean beginActivity(int scenarioInstance_id, int activity_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNode_id == activity_id) {
                return ((ActivityInstance) nodeInstance).begin();
            }
        }
        return false;
    }

    /**
     * Starts the execution of an activity specified by the params.
     * The state will only be changed if the activity is enabled.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activityInstance_id Specifies the activity instance id.
     * @return Indicates the success. True if the activity has been started, else false.
     */
    public boolean beginActivityInstance(int scenarioInstance_id, int activityInstance_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNodeInstance_id == activityInstance_id) {
                return ((ActivityInstance) nodeInstance).begin();
            }
        }
        return false;
    }

    /**
     * Terminates the execution of an activity specified by the params.
     * The state will only be changed if the activity is enabled.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activityInstance_id Specifies the activity instance id.
     * @return Indicates the success. True if the activity has been started, else false.
     */
    public boolean terminateActivityInstance(int scenarioInstance_id, int activityInstance_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNodeInstance_id == activityInstance_id) {
                return ((ActivityInstance) nodeInstance).terminate();
            }
        }
        return false;
    }

    /**
     * Terminates the execution of an activity specified by the params.
     * The state will only be changed if the activity is enabled.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activityInstance_id Specifies the activity instance id.
     * @return Indicates the success. True if the activity has been started, else false.
     */
    public boolean terminateActivityInstance(int scenarioInstance_id, int activityInstance_id, int outputSet_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).controlNodeInstance_id == activityInstance_id) {
                return ((ActivityInstance) nodeInstance).terminate(outputSet_id);
            }
        }
        return false;
    }

    /**
     * Returns information about all enabled Activities of a given scenario instance
     *
     * @scenarioInstanceId The id which specifies the scenario
     * @return a Collection of Activity instances, which are enabled and part of the
     *         specified scenario instance.
     */
    public Collection<ActivityInstance> getEnabledActivities(int scenarioInstanceId) {
        Collection<ActivityInstance> allEnabledActivities = new LinkedList<>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstanceId);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                allEnabledActivities.add((ActivityInstance) nodeInstance);
            }
        }
        Collection<ActivityInstance> activities = new LinkedList<>();
        Collection<ActivityInstance> enabledActivities = allEnabledActivities;
        for(ActivityInstance activityInstance: allEnabledActivities){
            if(!activities.contains(activityInstance)){
                Collection<ActivityInstance> references = this.getReferentialEnabledActivities(scenarioInstanceId, activityInstance.getControlNodeInstance_id());
                enabledActivities.removeAll(references);
                activities.addAll(references);
            }
        }
        return enabledActivities;
    }

    /**
     * Returns information about all referential Activities of a given scenario instance and activity instance
     *
     * @scenarioInstanceId The id which specifies the scenario
     * @activityInstanceId The id which specifies the activity
     * @return a Collection of referential Activity instances for an Activity, which are enabled and part of the
     *         specified scenario instance.
     */
    public Collection<ActivityInstance> getReferentialEnabledActivities(int scenarioInstanceId, int activityInstanceId) {
        Collection<ActivityInstance> enabledActivities = new LinkedList<>();
        DbReference dbReference = new DbReference();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        LinkedList<Integer> references = dbReference.getReferenceActivitiesForActivity(dbControlNodeInstance.getControlNodeID(activityInstanceId));
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstanceId);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getEnabledControlNodeInstances()) {
            if (nodeInstance instanceof ActivityInstance) {
                for (int id : references){
                    if(id == nodeInstance.getControlNode_id()){
                        enabledActivities.add((ActivityInstance) nodeInstance);
                        break;
                    }
                }
            }
        }
        return enabledActivities;
    }


    /**
     * Returns information about all Activities of a given scenario instance
     *
     * @scenarioInstanceId The id which specifies the scenario
     * @return a Collection of Activity instances, which are terminated and part of the
     *         specified scenario instance.
     */
    public Collection<ActivityInstance> getTerminatedActivities(int scenarioInstanceId) {
        Collection<ActivityInstance> terminatedActivities = new LinkedList<>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstanceId);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getTerminatedControlNodeInstances()) {
            if ( nodeInstance instanceof ActivityInstance) {
                terminatedActivities.add((ActivityInstance) nodeInstance);
            }
        }
        return terminatedActivities;
    }




    /**
     * Returns information about all running Activities of a given scenario instance
     *
     * @scenarioInstanceId The id which specifies the scenario
     * @return a Collection of Activity instances, which are running and part of the
     *         specified scenario instance.
     */
    public Collection<ActivityInstance> getRunningActivities(int scenarioInstanceId) {
        Collection<ActivityInstance> runningActivities = new LinkedList<>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstanceId);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if ( nodeInstance instanceof ActivityInstance) {
                runningActivities.add((ActivityInstance) nodeInstance);
            }
        }
        return runningActivities;
    }

    /**
     * Terminates an activity which is running.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activity_id         This is the id of the activity.
     * @return true if the activity could been terminated. false if not.
     */
    public boolean terminateActivity(int scenarioInstance_id, int activity_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).getControlNode_id() == activity_id) {
                return ((ActivityInstance) nodeInstance).terminate();
            }
        }
        return false;
    }

    /**
     * Terminates an activity which is running.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @param activity_id         This is the id of the activity.
     * @return true if the activity could been terminated. false if not.
     */
    public boolean terminateActivity(int scenarioInstance_id, int activity_id, int outputSet_id) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (((ActivityInstance) nodeInstance).getControlNode_id() == activity_id) {
                return ((ActivityInstance) nodeInstance).terminate(outputSet_id);
            }
        }
        return false;
    }

    /**
     * Gives the label of a given control node.
     *
     * @param controlNode_id This is the database id from the control node.
     * @return the label of the control node.
     */
    public String getLabelForControlNodeID(int controlNode_id) {
        return dbControlNode.getLabel(controlNode_id);
    }

    /**
     * Gives all data object ids for a scenario instance.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a list of the ids of data objects.
     */
    public LinkedList<Integer> getAllDataObjectIDs(int scenarioInstance_id) {
        LinkedList<Integer> dataObjectIDs = new LinkedList<Integer>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        if (scenarioInstance != null) {
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstances()) {
                dataObjectIDs.add(dataObject.getDataObject_id());
            }
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstancesOnChange()) {
                dataObjectIDs.add(dataObject.getDataObject_id());
            }
        }
        return dataObjectIDs;
    }

    /**
     * Returns the states of data objects for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a Map. Keys are the data objects ids. Values are the states of the data objects.
     */
    public HashMap<Integer, String> getAllDataObjectStates(int scenarioInstance_id) {
        DbState dbState = new DbState();
        HashMap<Integer, String> dataObjectStates = new HashMap<Integer, String>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        if (scenarioInstance != null) {
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstances()) {
                dataObjectStates.put(dataObject.getDataObject_id(), dbState.getStateName(dataObject.getState_id()));
            }
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstancesOnChange()) {
                dataObjectStates.put(dataObject.getDataObject_id(), dbState.getStateName(dataObject.getState_id()));
            }
        }
        return dataObjectStates;
    }

    /**
     * Returns the names of data objects for a scenario instance id.
     *
     * @param scenarioInstance_id This is the id of the scenario instance.
     * @return a Map. Keys are the data objects ids. Values are the names of the data objects.
     */
    public HashMap<Integer, String> getAllDataObjectNames(int scenarioInstance_id) {
        DbDataObject dbDataObject = new DbDataObject();
        HashMap<Integer, String> dataObjectNames = new HashMap<Integer, String>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        if (scenarioInstance != null) {
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstances()) {
                dataObjectNames.put(dataObject.getDataObject_id(), dbDataObject.getName(dataObject.getDataObject_id()));
            }
            for (DataObjectInstance dataObject : scenarioInstance.getDataObjectInstancesOnChange()) {
                dataObjectNames.put(dataObject.getDataObject_id(), dbDataObject.getName(dataObject.getDataObject_id()));
            }
        }
        return dataObjectNames;
    }


    /**
     * Checks if a scenario instance is terminated.
     *
     * @param scenarioInstance_id This is the database id from the scenario instance id.
     * @return true if the scenario instance ist terminated. false if not.
     */
    public boolean checkTerminationForScenarioInstance(int scenarioInstance_id) {
        DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
        return dbScenarioInstance.getTerminated(scenarioInstance_id) == 1;
    }

    /**
     * Returns the scenario name for a given scenario id.
     *
     * @param scenario_id This is the database id from the scenario id.
     * @return the scenario name in a String.
     */
    public String getScenarioName(int scenario_id) {
        return dbScenario.getScenarioName(scenario_id);
    }

    /**
     * Returns the scenario name for a given scenario instance id.
     *
     * @param scenarioInstance_id This is the database id from the scenario instance id.
     * @return the scenario name in a String.
     */
    public String getScenarioNameForScenarioInstance(int scenarioInstance_id) {
        return dbScenario.getScenarioName(dbScenarioInstance.getScenarioID(scenarioInstance_id));
    }

    /**
     * Gives the scenario instance for a scenario instance id.
     * Only for Tests.
     *
     * @param scenarioInstanceID This is the id of the scenario instance.
     * @return a instance of the class ScenarioInstance.
     */
    public ScenarioInstance getScenarioInstance(int scenarioInstanceID) {
        return sortedScenarioInstances.get(scenarioInstanceID);
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
     * @param scenarioInstance_id The id of the scenario instance.
     * @param activityInstanceID  The id of the activity instance.
     * @param values              A Map with the data attribute instance id as key and the value of the data attribute as value.
     */
    public void setDataAttributeValues(int scenarioInstance_id, int activityInstanceID, Map<Integer, String> values) {
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for (ControlNodeInstance nodeInstance : scenarioInstance.getRunningControlNodeInstances()) {
            if (nodeInstance.getControlNodeInstance_id() == activityInstanceID) {
                ((ActivityInstance) nodeInstance).setDataAttributeValues(values);
            }
        }
    }

    /**
     * Returns a Map with all DataAttributeInstanceIDs, values and types for scenario instance.
     *
     * @param scenarioInstance_id The id of the scenario instance.
     * @return a Map with data attribute instances.
     */
    public Map<Integer, Map<String, String>> getAllDataAttributeInstances(int scenarioInstance_id) {
        Map<Integer, Map<String, String>> attributeInstances = new HashMap<>();
        for (DataAttributeInstance dataAttributeInstance : sortedScenarioInstances.get(scenarioInstance_id).getDataAttributeInstances().values()) {
            Map<String, String> values = new HashMap<>();
            values.put("type", dataAttributeInstance.getType());
            values.put("value", dataAttributeInstance.getValue().toString());
            values.put("name", dataAttributeInstance.getName());
            attributeInstances.put(dataAttributeInstance.getDataAttributeInstance_id(), values);
        }
        return attributeInstances;
    }

    /**
     * Returns a Map with all OutputSets, DataOjects and Data States for activity instance.
     * @param activityInstanceId The id of the activity instance.
     * @return a Map with outputsets.
     */
    public Map<Integer,Map<String, String>> getOutputSetsForActivityInstance(int activityInstanceId){
        DbDataFlow dbDataFlow = new DbDataFlow();
        DbDataNode dbDataNode = new DbDataNode();
        DbDataObject dbDataObject = new DbDataObject();
        DbState dbState = new DbState();
        DbControlNodeInstance dbControlNodeInstance = new DbControlNodeInstance();
        int controlNode_id = dbControlNodeInstance.getControlNodeID(activityInstanceId);

        Map<Integer, Map<String, String>> allOutputSets = new HashMap<>();
        LinkedList<Integer> outputSets = dbDataFlow.getOutputSetsForControlNode(controlNode_id);
        for (int outputSet : outputSets) {
            LinkedList<DataObject> dataObjects = dbDataNode.getDataObjectsForDataSets(outputSet);
            for (DataObject dataObject : dataObjects) {
                allOutputSets.put(outputSet, new HashMap<String, String>());
                allOutputSets.get(outputSet).put(dbDataObject.getName(dataObject.getId()), dbState.getStateName(dataObject.getStateID()));
            }
        }
        return allOutputSets;
    }
}
