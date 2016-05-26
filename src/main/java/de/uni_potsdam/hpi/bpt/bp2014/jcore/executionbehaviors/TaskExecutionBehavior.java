package de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;

import org.apache.log4j.Logger;

import java.util.Map;

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

	/**
	 * @param values a Map of Keys and Values.
	 * @return true if all values could be set.
	 */
	public boolean setDataAttributeValues(Map<Integer, String> values) {
		boolean allValuesValid = true;
		for (Map.Entry<Integer, String> attributeInstanceIdToValue : values.entrySet()) {
            Integer dataattributeInstanceId = attributeInstanceIdToValue.getKey();
            String value = attributeInstanceIdToValue.getValue();
            new DbLogEntry().logDataAttributeTransition(dataattributeInstanceId, value,
                    activityInstanceId, scenarioInstance.getScenarioInstanceId());
            DataAttributeInstance dataAttributeInstance = getScenarioInstance()
                    .getDataAttributeInstances().get(dataattributeInstanceId);
			if (dataAttributeInstance.isValueAllowed(value)) {
				dataAttributeInstance.setValue(value);
			} else {
				LOGGER.error("Attribute value could not be set "
						+ "because it has the wrong data type.");
				allValuesValid = false;
			}
        }
        this.setCanTerminate(true);
		return allValuesValid;
    }

	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	public AbstractControlNodeInstance getControlNodeInstance() {
		return controlNodeInstance;
	}
}
