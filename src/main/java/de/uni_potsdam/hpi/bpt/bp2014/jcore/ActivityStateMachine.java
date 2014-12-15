package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;

public class ActivityStateMachine extends StateMachine {
    private DbActivityInstance dbActivityInstance = new DbActivityInstance();


    public ActivityStateMachine(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance){
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance_id = activityInstance_id;
        this.controlNodeInstance = controlNodeInstance;
        state = getDBState();
        if (state.equals("ready")){
            scenarioInstance.controlFlowEnabledControlNodeInstances.add(controlNodeInstance);
            scenarioInstance.dataEnabledControlNodeInstances.add(controlNodeInstance);
            scenarioInstance.enabledControlNodeInstances.add(controlNodeInstance);
        }else if(state.equals("ready(Data)")){
            scenarioInstance.dataEnabledControlNodeInstances.add(controlNodeInstance);
        }else if(state.equals("ready(ControlFlow)")){
            scenarioInstance.controlFlowEnabledControlNodeInstances.add(controlNodeInstance);
        }else if(state.equals("running")){
            scenarioInstance.runningControlNodeInstances.add(controlNodeInstance);
        }else if(state.equals("terminated")){
            scenarioInstance.terminatedControlNodeInstances.add(controlNodeInstance);
        }else if(state.equals("skipped")){
            scenarioInstance.terminatedControlNodeInstances.add(controlNodeInstance);
        }
    }
    private String getDBState(){
        return dbActivityInstance.getState(controlNodeInstance_id);
    }

    public Boolean enableControlFlow(){
        //String state = this.getState();
        if(state.equals("init")) {
            this.setState("ready(ControlFlow)");
            scenarioInstance.controlFlowEnabledControlNodeInstances.add(controlNodeInstance);
            return true;
        }else if(state.equals("ready(Data)")){
            this.setState("ready");
            scenarioInstance.controlFlowEnabledControlNodeInstances.add(controlNodeInstance);
            scenarioInstance.enabledControlNodeInstances.add(controlNodeInstance);
            return true;
        }else if(this.isReady(state)){
            return true;
        }
        return false;
    }

    public Boolean enableData(){
        //String state = this.getState();
        if(state.equals("init")) {
            this.setState("ready(Data)");
            scenarioInstance.dataEnabledControlNodeInstances.add(controlNodeInstance);
            return true;
        }else if(state.equals("ready(ControlFlow)")){
            this.setState("ready");
            scenarioInstance.dataEnabledControlNodeInstances.add(controlNodeInstance);
            scenarioInstance.enabledControlNodeInstances.add(controlNodeInstance);
            return true;
        }else if(this.isReady(state)){
            return true;
        }
        return false;
    }

    public Boolean begin(){
        //String state = this.getState();
        if(state.equals("ready")){
            this.setState("running");
            scenarioInstance.runningControlNodeInstances.add(controlNodeInstance);
            scenarioInstance.controlFlowEnabledControlNodeInstances.remove(controlNodeInstance);
            scenarioInstance.dataEnabledControlNodeInstances.remove(controlNodeInstance);
            scenarioInstance.enabledControlNodeInstances.remove(controlNodeInstance);
            return true;
        }
        return false;
    }

    public Boolean terminate(){
        //String state = this.getState();
        if(state.equals("running")){
            this.setState("terminated");
            scenarioInstance.runningControlNodeInstances.remove(controlNodeInstance);
            scenarioInstance.controlNodeInstances.remove(controlNodeInstance);
            scenarioInstance.terminatedControlNodeInstances.add(controlNodeInstance);
            return true;
        }
        return false;
    }

    public Boolean skip(){
        //String state = this.getState();
        if(state.equals("init") || this.isReady(state)){
            this.setState("skipped");
            return true;
        }
        return false;
    }

    public Boolean isEnabled(){
        if (this.state.equals("ready")) return true;
        return false;
    }

    private Boolean isReady(String state){
        if(state.equals("ready") || state.equals("ready(ControlFlow)") || state.equals("ready(Data)")){
            return true;
        }
        return false;
    }

    private void setState(String state){
        this.state = state;
        dbActivityInstance.setState(controlNodeInstance_id, state);
    }
}
