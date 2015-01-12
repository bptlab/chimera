package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import java.util.LinkedList;

public class ExecutionService {
    ScenarioInstance scenarioInstance;

    public ExecutionService(ScenarioInstance scenarioInstance){
        this.scenarioInstance=scenarioInstance;
    }
    public LinkedList<Integer> getEnabledActivitiesIDs(){
        LinkedList<Integer> ids = new LinkedList<Integer>();
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances){
            if(nodeInstance instanceof ActivityInstance){
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }

        return ids;
    }

    public void startActivity(int id){
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances) {
            if (((ActivityInstance) nodeInstance).controlNode_id == id) {
                ((ActivityInstance) nodeInstance).begin();
                ((ActivityInstance) nodeInstance).terminate();
                return;
            }
        }
    }

    public LinkedList<Integer> getClosedActivitiesIDs() {
        LinkedList<Integer> ids = new LinkedList<Integer>();
        for(ControlNodeInstance nodeInstance: scenarioInstance.enabledControlNodeInstances){ // must be changed
            if(nodeInstance instanceof ActivityInstance){
                ids.add(((ActivityInstance) nodeInstance).controlNode_id);
            }
        }
        return ids;
    }
}
