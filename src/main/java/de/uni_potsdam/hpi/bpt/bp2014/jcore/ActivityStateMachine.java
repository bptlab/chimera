package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;


/**
 * Handles the state for an activity instance.
 */
public class ActivityStateMachine extends StateMachine {
    /**
     * Database Connection objects.
     */
    private final DbActivityInstance dbActivityInstance = new DbActivityInstance();

    /**
     * Creates and initialize a activity state machine for an activity.
     * Reads the state from the database of the activity and add the activity to the correct lists in scenario instance.
     *
     * @param activityInstance_id This is the database id from the activity instance.
     * @param scenarioInstance    This is an instance from the class ScenarioInstance.
     * @param controlNodeInstance This is an instance from the class ControlNodeInstance.
     */
    public ActivityStateMachine(int activityInstance_id, ScenarioInstance scenarioInstance, ControlNodeInstance controlNodeInstance) {
        this.scenarioInstance = scenarioInstance;
        this.controlNodeInstance_id = activityInstance_id;
        this.controlNodeInstance = controlNodeInstance;
        state = getDBState();
        //adds the Activity Instance to the correct list in Scenario Instance, decides on the state of the Activity
        switch (state) {
            case "ready":
                scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
                scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
                scenarioInstance.getEnabledControlNodeInstances().add(controlNodeInstance);
                break;
            case "ready(Data)":
                scenarioInstance.getDataEnabledControlNodeInstances().add(controlNodeInstance);
                break;
            case "ready(ControlFlow)":
                scenarioInstance.getControlFlowEnabledControlNodeInstances().add(controlNodeInstance);
                break;
            case "running":
                scenarioInstance.getRunningControlNodeInstances().add(controlNodeInstance);
                break;
            case "terminated":
                scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
                break;
            case "skipped":
                scenarioInstance.getTerminatedControlNodeInstances().add(controlNodeInstance);
                break;
            case "referentialRunning":
                scenarioInstance.getReferentialRunningControlNodeInstances().add(controlNodeInstance);
                break;
        }
    }

    /**
     * Returns the state from the database for the activity instance specified in attributes.
     *
     * @return the state from the database for the specific activity instance.
     */
    private String getDBState() {
        return dbActivityInstance.getState(controlNodeInstance_id);
    }

    /**
     * Enables the control flow for the activity instance specified in attributes.
     *
     * @return true if the state could been updated. false if the state couldn't been updated.
     */
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

    /**
     * Enables the data flow for the activity instance specified in attributes.
     *
     * @return true if the state could been updated. false if the state couldn't been updated.
     */
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

    /**
     * Disables the data flow for the activity instance specified in attributes.
     *
     * @return true if the state could been updated. false if the state couldn't been updated.
     */
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

    /**
     * Sets the state from the activity instance specified in attributes to referential running.
     *
     * @return true if the state could been set to referential running. false if the state couldn't been set.
     */
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

    /**
     * Sets the state from the activity instance specified in attributes from referential running to terminated.
     *
     * @return true if the state could been set to referential running. false if the state couldn't been set.
     */
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

    /**
     * Sets the state from the activity instance specified in attributes from ready to running.
     *
     * @return true if the state could been set to running. false if the state couldn't been set.
     */
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

    /**
     * Sets the state from the activity instance specified in attributes from running to terminated.
     *
     * @return true if the state could been set to terminated. false if the state couldn't been set.
     */
    @Override
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

    /**
     * Sets the state from the activity instance specified in attributes to skipped.
     *
     * @return true if the state could been set. false if the state couldn't been set.
     */
    @Override
    public boolean skip() {
        //String state = this.getState();
        if (state.equals("init") || this.isReady(state)) {
            scenarioInstance.getControlFlowEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getDataEnabledControlNodeInstances().remove(controlNodeInstance);
            scenarioInstance.getEnabledControlNodeInstances().remove(controlNodeInstance);
            this.setState("skipped");
            return true;
        }
        return false;
    }

    /**
     * Checks if the state ist enabled
     *
     * @return true if the state is ready, ready(ControlFlow) or ready(data).
     */
    public boolean isEnabled() {
        return this.state.equals("ready");
    }

    /**
     * Checks if the state is enabled.
     *
     * @param state The state which should be checked.
     * @return true if the state is ready, ready(ControlFlow) or ready(data).
     */
    private boolean isReady(String state) {
        return state.equals("ready") || state.equals("ready(ControlFlow)") || state.equals("ready(Data)");
    }

    /**
     * Sets the state.
     * Updates the state in database and in attributes
     *
     * @param state This is the new state.
     */
    private void setState(String state) {
        this.state = state;
        dbActivityInstance.setState(controlNodeInstance_id, state);
    }
}
