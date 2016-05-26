package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;

/**
 * Handles the state for an activity instance.
 */
public class ActivityStateMachine extends AbstractStateMachine {
	/**
	 * Database Connection objects.
	 */
	private final DbActivityInstance dbActivityInstance = new DbActivityInstance();

	/**
	 * Creates and initialize a activity state machine for an activity.
	 * Reads the state from the database of the activity and
	 * add the activity to the correct lists in scenario instance.
	 *
	 * @param activityInstanceId This is the database id from the activity instance.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 */
	public ActivityStateMachine(int activityInstanceId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		this.setScenarioInstance(scenarioInstance);
		this.setControlNodeInstanceId(activityInstanceId);
		this.setControlNodeInstance(controlNodeInstance);
		switch (getDBState()) {
			case READY:
				getScenarioInstance().getControlFlowEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				getScenarioInstance().getDataEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				getScenarioInstance().getEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case DATAFLOW_ENABLED:
				getScenarioInstance().getDataEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case CONTROLFLOW_ENABLED:
				getScenarioInstance().getControlFlowEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case RUNNING:
				getScenarioInstance().getRunningControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case TERMINATED:
				getScenarioInstance().getTerminatedControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case SKIPPED:
				getScenarioInstance().getTerminatedControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			case REFERENTIAL_RUNNING:
				getScenarioInstance().getReferentialRunningControlNodeInstances()
						.add(getControlNodeInstance());
				break;
			default:
				break;
		}
		setState(getDBState());
		//adds the Activity Instance to the correct list
		// in Scenario Instance, decides on the state of the Activity
	}

	/**
	 * Returns the state from the database for the activity instance specified in attributes.
	 *
	 * @return the state from the database for the specific activity instance.
	 */
	private STATE getDBState() {
        return dbActivityInstance.getState(getControlNodeInstanceId());
    }

	/**
	 * Enables the control flow for the activity instance specified in attributes.
	 *
	 * @return true if the state update was successful, false if not.
	 */
	public boolean enableControlFlow() {
		if (STATE.INIT.equals(getState())) {
			this.setState(STATE.CONTROLFLOW_ENABLED);
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		} else {
			if (STATE.DATAFLOW_ENABLED.equals(getState())) {
				this.setState(STATE.READY);
				getScenarioInstance().getControlFlowEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				getScenarioInstance().getEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				return true;
			} else if (this.isReady(getState())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Enables the data flow for the activity instance specified in attributes.
	 *
	 * @return true if the state update was successful, false if not.
	 */
	public boolean enableData() {
		if (STATE.INIT.equals(getState())) {
			this.setState(STATE.DATAFLOW_ENABLED);
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		} else {
			if (STATE.CONTROLFLOW_ENABLED.equals(getState())) {
				this.setState(STATE.READY);
				getScenarioInstance().getDataEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				getScenarioInstance().getEnabledControlNodeInstances()
						.add(getControlNodeInstance());
				return true;
			} else if (this.isReady(getState())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Disables the data flow for the activity instance specified in attributes.
	 *
	 * @return true if the state could been updated. false if the state couldn't been updated.
	 */
	public boolean disableData() {
		if (STATE.DATAFLOW_ENABLED.equals(getState())) {
			this.setState(STATE.INIT);
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			return true;
		} else {
			if (STATE.READY.equals(getState())) {
				this.setState(STATE.CONTROLFLOW_ENABLED);
				getScenarioInstance().getDataEnabledControlNodeInstances()
						.remove(getControlNodeInstance());
				getScenarioInstance().getEnabledControlNodeInstances()
						.remove(getControlNodeInstance());
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the state from the activity instance specified in attributes to referential running.
	 *
	 * @return true if the state was set to referential running. (else false).
	 */
	public boolean referenceStarted() {
		if (STATE.READY.equals(getState())) {
			this.setState(STATE.REFERENTIAL_RUNNING);
			getScenarioInstance().getReferentialRunningControlNodeInstances()
					.add(getControlNodeInstance());
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			return true;
		} else {
			if (STATE.CONTROLFLOW_ENABLED.equals(getState())) {
				this.setState(STATE.REFERENTIAL_RUNNING);
				getScenarioInstance().getReferentialRunningControlNodeInstances()
						.add(getControlNodeInstance());
				getScenarioInstance().getControlFlowEnabledControlNodeInstances()
						.remove(getControlNodeInstance());
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the state from the activity instance specified in attributes
	 * from referential running to terminated.
	 *
	 * @return true if the state was set to referential running. (else false).
	 */
	public boolean referenceTerminated() {
		if (STATE.REFERENTIAL_RUNNING.equals(getState())) {
			this.setState(STATE.TERMINATED);
			getScenarioInstance().getReferentialRunningControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getTerminatedControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		}
		return false;
	}

	/**
	 * Sets the state from the activity instance specified in attributes from ready to running.
	 *
	 * @return true if the state was set to running. (else false).
	 */
	public boolean begin() {
		if (STATE.READY.equals(getState())) {
			this.setState(STATE.RUNNING);
			getScenarioInstance().getRunningControlNodeInstances()
					.add(getControlNodeInstance());
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			return true;
		}
		return false;
	}

	/**
	 * Sets the state from the activity instance specified in attributes
	 * from running to terminated.
	 *
	 * @return true if the state was set to terminated. (else false).
	 */
	@Override public boolean terminate() {
		if (STATE.RUNNING.equals(getState())) {
			this.setState(STATE.TERMINATED);
			getScenarioInstance().getRunningControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getTerminatedControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		}
		return false;
	}

    /**
     * Cancels the current activity.
     * @return Returns true if the activity was running and then could have been terminated correct.
     */
    public boolean cancel() {
        if (STATE.RUNNING.equals(getState())) {
            this.setState(STATE.CANCEL);
            getScenarioInstance().getRunningControlNodeInstances()
                    .remove(getControlNodeInstance());
            getScenarioInstance().getControlNodeInstances()
                    .remove(getControlNodeInstance());
            return true;
        }
        return false;
    }

	/**
	 * Sets the state from the activity instance specified in attributes to skipped.
	 *
	 * @return true if the state could been set. false if the state couldn't been set.
	 */
	@Override public boolean skip() {
		if (STATE.INIT.equals(getState()) || this.isReady(getState())) {
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			this.setState(STATE.SKIPPED);
			return true;
		}
		return false;
	}

	/**
	 * Checks if the state ist enabled.
	 *
	 * @return true if the state is ready, ready(ControlFlow) or ready(data).
	 */
	public boolean isEnabled() {
		return STATE.READY.equals(this.getState());
	}

	/**
	 * Checks if the state is enabled.
	 *
	 * @param state The state which should be checked.
	 * @return true if the state is ready, ready(ControlFlow) or ready(data).
	 */
	private boolean isReady(STATE state) {
		return STATE.READY.equals(state) || STATE.CONTROLFLOW_ENABLED.equals(state) ||
                STATE.DATAFLOW_ENABLED.equals(state);
	}

	/**
	 * Sets the state.
	 * Updates the state in database and in attributes
	 * also updates the scenario instance
	 *
	 * @param state This is the new state.
	 */
	public void setState(STATE state) {
		super.setState(state);
		dbActivityInstance.setState(getControlNodeInstanceId(), state);
	}
}
