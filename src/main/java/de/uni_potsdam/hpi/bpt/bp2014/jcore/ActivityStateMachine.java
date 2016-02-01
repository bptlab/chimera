package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;

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
		setState(getDBState());
		//adds the Activity Instance to the correct list
		// in Scenario Instance, decides on the state of the Activity
		switch (getState()) {
		case "ready":
			scenarioInstance.getControlFlowEnabledControlNodeInstances()
					.add(controlNodeInstance);
			scenarioInstance.getDataEnabledControlNodeInstances()
					.add(controlNodeInstance);
			scenarioInstance.getEnabledControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "ready(Data)":
			scenarioInstance.getDataEnabledControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "ready(ControlFlow)":
			scenarioInstance.getControlFlowEnabledControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "running":
			scenarioInstance.getRunningControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "terminated":
			scenarioInstance.getTerminatedControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "skipped":
			scenarioInstance.getTerminatedControlNodeInstances()
					.add(controlNodeInstance);
			break;
		case "referentialRunning":
			scenarioInstance.getReferentialRunningControlNodeInstances()
					.add(controlNodeInstance);
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the state from the database for the activity instance specified in attributes.
	 *
	 * @return the state from the database for the specific activity instance.
	 */
	private String getDBState() {
		return dbActivityInstance.getState(getControlNodeInstanceId());
	}

	/**
	 * Enables the control flow for the activity instance specified in attributes.
	 *
	 * @return true if the state could been updated. false if the state couldn't been updated.
	 */
	public boolean enableControlFlow() {
		//String state = this.getState();
		if ("init".equals(getState())) {
			this.setState("ready(ControlFlow)");
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		} else {
			if ("ready(Data)".equals(getState())) {
				this.setState("ready");
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
	 * @return true if the state could been updated. false if the state couldn't been updated.
	 */
	public boolean enableData() {
		//String state = this.getState();
		if ("init".equals(getState())) {
			this.setState("ready(Data)");
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.add(getControlNodeInstance());
			return true;
		} else {
			if ("ready(ControlFlow)".equals(getState())) {
				this.setState("ready");
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
		//String state = this.getState();
		if ("ready(Data)".equals(getState())) {
			this.setState("init");
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			return true;
		} else {
			if ("ready".equals(getState())) {
				this.setState("ready(ControlFlow)");
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
		if ("ready".equals(getState())) {
			this.setState("referentialRunning");
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
			if ("ready(ControlFlow)".equals(getState())) {
				this.setState("referentialRunning");
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
		if ("referentialRunning".equals(getState())) {
			this.setState("terminated");
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
		//String state = this.getState();
		if ("ready".equals(getState())) {
			this.setState("running");
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
		//String state = this.getState();
		if ("running".equals(getState())) {
			this.setState("terminated");
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
        if ("running".equals(getState())) {
            this.setState("cancel");
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
		//String state = this.getState();
		if ("init".equals(getState()) || this.isReady(getState())) {
			getScenarioInstance().getControlFlowEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getDataEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			getScenarioInstance().getEnabledControlNodeInstances()
					.remove(getControlNodeInstance());
			this.setState("skipped");
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
		return "ready".equals(this.getState());
	}

	/**
	 * Checks if the state is enabled.
	 *
	 * @param state The state which should be checked.
	 * @return true if the state is ready, ready(ControlFlow) or ready(data).
	 */
	private boolean isReady(String state) {
		return "ready".equals(state) || "ready(ControlFlow)".equals(state) || "ready(Data)"
				.equals(state);
	}

	/**
	 * Sets the state.
	 * Updates the state in database and in attributes
	 *
	 * @param state This is the new state.
	 */
	public void setState(String state) {
		super.setState(state);
		dbActivityInstance.setState(getControlNodeInstanceId(), state);
	}
}
