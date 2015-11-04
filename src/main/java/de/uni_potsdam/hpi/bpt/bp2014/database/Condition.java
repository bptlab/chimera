package de.uni_potsdam.hpi.bpt.bp2014.database;

/**
 * Represents an entry from the database entity termination condition.
 * This class is a data storage.
 */
public class Condition {

	private int conditionSetId;
	private int stateId;
	private int dataObjectId;
	private int scenarioId;

	/**
	 * Initializes the Condition.
	 *
	 * @param conditionSetId This is the database ID of a condition set.
	 * @param stateId        This is the database ID of a state.
	 * @param dataObjectId   This is the database ID of a data object.
	 * @param scenarioId     This is the database ID of a scenario.
	 */
	public Condition(int conditionSetId, int stateId, int dataObjectId, int scenarioId) {

		this.conditionSetId = conditionSetId;
		this.stateId = stateId;
		this.dataObjectId = dataObjectId;
		this.scenarioId = scenarioId;
	}


	public int getConditionSetId() {
		return conditionSetId;
	}

	public void setConditionSetId(int conditionSetId) {
		this.conditionSetId = conditionSetId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public int getDataObjectId() {
		return dataObjectId;
	}

	public void setDataObjectId(int dataObjectId) {
		this.dataObjectId = dataObjectId;
	}

	public int getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}

}
