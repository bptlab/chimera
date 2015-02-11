package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;


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


public class ActivityStateMachine extends StateMachine {
    //Database Connection objects
    private DbActivityInstance dbActivityInstance = new DbActivityInstance();


    public ActivityStateMachine(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance_id = activityInstance_id;
        this.controlNodeInstance = controlNodeInstance;
        state = getDBState();
        //adds the Activity Instance to the correct list in Scenario Instance, decides on the state of the Activity
        if (state.equals("ready")) {
            scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("ready(Data)")) {
            scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("ready(ControlFlow)")) {
            scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("running")) {
            scenarioInstance.getRunningControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("terminated")) {
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("skipped")) {
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
        } else if (state.equals("referentialRunning")) {
            scenarioInstance.getReferentialRunningControlNodeInstances().add(controlNodeInstance);
        }
    }

    private String getDBState() {
        return dbActivityInstance.getState(controlNodeInstance_id);
    }

    public boolean enableControlFlow() {
        //String state = this.getState();
        if (state.equals("init")) {
            this.setState("ready(ControlFlow)");
            scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
            return true;
        } else if (state.equals("ready(Data)")) {
            this.setState("ready");
            scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().add(controlNodeInstance);
            return true;
        } else if (this.isReady(state)) {
            return true;
        }
        return false;
    }

    public boolean enableData() {
        //String state = this.getState();
        if (state.equals("init")) {
            this.setState("ready(Data)");
            scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
            return true;
        } else if (state.equals("ready(ControlFlow)")) {
            this.setState("ready");
            scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().add(controlNodeInstance);
            return true;
        } else if (this.isReady(state)) {
            return true;
        }
        return false;
    }

    public boolean disableData() {
        //String state = this.getState();
        if (state.equals("ready(Data)")) {
            this.setState("init");
            scenarioInstance.getDataEnabledControlNodeInstances().remove(controlNodeInstance);
            return true;
        } else if (state.equals("ready")) {
            this.setState("ready(ControlFlow)");
            scenarioInstance.getDataEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().remove(controlNodeInstance);
            return true;
        }
        return false;
    }

    public boolean referenceStarted() {
        if (state.equals("ready")) {
            this.setState("referentialRunning");
            scenarioInstance.getReferentialRunningControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getControlFlowEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getDataEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().remove(controlNodeInstance);
            return true;
        } else if (state.equals("ready(ControlFlow)")) {
            this.setState("referentialRunning");
            scenarioInstance.getReferentialRunningControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getControlFlowEnabledControlNodeInstances().remove(controlNodeInstance);
            return true;
        }
        return false;
    }

    public boolean referenceTerminated() {
        if (state.equals("referentialRunning")) {
            this.setState("terminated");
            scenarioInstance.getReferentialRunningControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
            return true;
        }
        return false;
    }

    public boolean begin() {
        //String state = this.getState();
        if (state.equals("ready")) {
            this.setState("running");
            scenarioInstance.getRunningControlNodeInstances().add(controlNodeInstance);
            scenarioInstance.getControlFlowEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getDataEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().remove(controlNodeInstance);
            return true;
        }
        return false;
    }

    public boolean terminate() {
        //String state = this.getState();
        if (state.equals("running")) {
            this.setState("terminated");
            scenarioInstance.getRunningControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
            return true;
        }
        return false;
    }

    public boolean skip() {
        //String state = this.getState();
        if (state.equals("init") || this.isReady(state)) {
            this.setState("skipped");
            return true;
        }
        return false;
    }

    public boolean isEnabled() {
        if (this.state.equals("ready")) return true;
        return false;
    }

    private boolean isReady(String state) {
        if (state.equals("ready") || state.equals("ready(ControlFlow)") || state.equals("ready(Data)")) {
            return true;
        }
        return false;
    }

    private void setState(String state) {
        this.state = state;
        dbActivityInstance.setState(controlNodeInstance_id, state);
    }
}
