package de.hpi.bpt.chimera.jcore.executionbehaviors;

import de.hpi.bpt.chimera.jcore.ScenarioInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.AbstractControlNodeInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;

import org.apache.log4j.Logger;

/**
 * Performs the execution for one activity.
 */
public class TaskExecutionBehavior {
	private static final Logger LOGGER = Logger.getLogger(TaskExecutionBehavior.class);

	private final ScenarioInstance scenarioInstance;
	private final int activityInstanceId;
	private final AbstractControlNodeInstance controlNodeInstance;

	/**
	 * Initializes.
	 *
	 * @param activityInstanceId Id from the activity instance in the database.
	 * @param scenarioInstance    This is an instance from the class ScenarioInstance.
	 * @param controlNodeInstance This is an AbstractControlNodeInstance.
	 */
	public TaskExecutionBehavior(int activityInstanceId, ScenarioInstance scenarioInstance,
			AbstractControlNodeInstance controlNodeInstance) {
		this.activityInstanceId = activityInstanceId;
		this.scenarioInstance = scenarioInstance;
		this.controlNodeInstance = controlNodeInstance;
	}

	/**
	 * Executes the behavior of the activity.
	 */
	public void execute() {
		//handled by implementers
	}

	/**
	 * @param canTerminate a boolean stating whether it can terminate.
	 */
	protected void setCanTerminate(boolean canTerminate) {
		((ActivityInstance) controlNodeInstance).setCanTerminate(canTerminate);
	}

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public AbstractControlNodeInstance getControlNodeInstance() {
		return controlNodeInstance;
	}
}
