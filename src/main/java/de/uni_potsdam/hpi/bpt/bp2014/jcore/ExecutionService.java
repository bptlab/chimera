package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenario;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;

import java.util.HashMap;
import java.util.LinkedList;

public class ExecutionService {
    //Debug only
    private ScenarioInstance scenarioInstance_debug;


    private LinkedList<ScenarioInstance> scenarioInstances = new LinkedList<ScenarioInstance>();
    private HashMap<Integer, ScenarioInstance> sortedScenarioInstances = new HashMap<Integer, ScenarioInstance>();
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbScenario dbScenario = new DbScenario();

    public ExecutionService(){

    }

    public int startNewScenarioInstance(int scenario_id){
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenario_id);
        scenarioInstances.add(scenarioInstance);
        sortedScenarioInstances.put(scenarioInstance.scenarioInstance_id, scenarioInstance);
        return scenarioInstance.scenarioInstance_id;
    }

    public void openExistingScenarioInstance(int scenario_id, int scenarioInstance_id){
        if(!sortedScenarioInstances.containsKey(scenarioInstance_id)) {
            ScenarioInstance scenarioInstance = new ScenarioInstance(scenario_id, scenarioInstance_id);
            scenarioInstances.add(scenarioInstance);
            sortedScenarioInstances.put(scenarioInstance_id, scenarioInstance);
        }
    }

    public LinkedList<Integer> getAllScenarioIDs(){
        return dbScenario.getScenarioIDs();
    }

    public Boolean scenarioInstanceIsRunning(int scenarioInstance_id){
        return sortedScenarioInstances.containsKey(scenarioInstance_id);
    }

    public Boolean existScenarioInstance(int scenarioInstance_id){
        if(dbScenarioInstance.existScenario(scenarioInstance_id)) return true;
        return false;
    }

    public Boolean existScenarioInstance(int scenario_id, int scenarioInstance_id){
        if(dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id)) return true;
        return false;
    }

    public LinkedList<Integer> listAllScenarioInstancesForScenario(int scenario_id){
        return dbScenarioInstance.getScenarioInstances(scenario_id);
    }

    public LinkedList<Integer> getEnabledActivitiesIDsForScenarioInstance(int scenarioInstance_id){
        LinkedList<Integer> ids = new LinkedList<Integer>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances){
            if(nodeInstance instanceof ActivityInstance){
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }
        return ids;
    }

    public HashMap<Integer, String> getEnabledActivityLabelsForScenarioInstance(int scenarioInstance_id){
        HashMap<Integer,String> labels = new HashMap<Integer, String>();
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances){
            if(nodeInstance instanceof ActivityInstance){
                labels.put(((ActivityInstance) nodeInstance).controlNode_id, ((ActivityInstance) nodeInstance).label);
            }
        }
        return labels;
    }

    public void beginActivity(int scenarioInstance_id, int activity_id){
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances) {
            if (((ActivityInstance) nodeInstance).controlNode_id == activity_id) {
                ((ActivityInstance) nodeInstance).begin();
                return;
            }
        }
    }

    public void terminateActivity(int scenarioInstance_id, int activity_id){
        ScenarioInstance scenarioInstance = sortedScenarioInstances.get(scenarioInstance_id);
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances) {
            if (((ActivityInstance) nodeInstance).controlNode_id == activity_id) {
                ((ActivityInstance) nodeInstance).terminate();
                return;
            }
        }
    }

    public LinkedList<Integer> getAllDataObjectIDs(int scenarioInstance_id){
        LinkedList<Integer> dataObjectIDs = new LinkedList<Integer>();
        for(DataObjectInstance dataObject: sortedScenarioInstances.get(scenarioInstance_id).dataObjectInstances) dataObjectIDs.add(dataObject.dataObject_id);
        return dataObjectIDs;
    }

    public LinkedList<Integer> getAllDataObjectStateIDs(int scenarioInstance_id){
        LinkedList<Integer> dataObjectStateIDs = new LinkedList<Integer>();
        for(DataObjectInstance dataObject: sortedScenarioInstances.get(scenarioInstance_id).dataObjectInstances) dataObjectStateIDs.add(dataObject.state_id);
        return dataObjectStateIDs;
    }

    //Debug only
    public ExecutionService(ScenarioInstance scenarioInstance){
        this.scenarioInstance_debug=scenarioInstance;
    }
    //Debug only
    public LinkedList<Integer> getEnabledActivitiesIDs(){
        LinkedList<Integer> ids = new LinkedList<Integer>();
        for(ControlNodeInstance nodeInstance: scenarioInstance_debug.enabledControlNodeInstances){
            if(nodeInstance instanceof ActivityInstance){
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }

        return ids;
    }
    //Debug only
    public void startActivity(int id){
        for(ControlNodeInstance nodeInstance: scenarioInstance_debug.enabledControlNodeInstances) {
            if (((ActivityInstance) nodeInstance).controlNode_id == id) {
                ((ActivityInstance) nodeInstance).begin();
                ((ActivityInstance) nodeInstance).terminate();
                return;
            }
        }
    }


}
