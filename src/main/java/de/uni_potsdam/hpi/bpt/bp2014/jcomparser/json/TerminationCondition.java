package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbTerminationCondition;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import java.util.UUID;

/**
 * A Class representing a TerminationCondition,
 * consisting of multiple DataObjects in certain states.
 */
public class TerminationCondition {

	// auto increment in the database
	private int conditionSetId;
	private int stateId;
	private int dataObjectId;
	private int scenarioId;

	/**
	 * Initializes the Condition.
	 *
	 * @param stateId        This is the ID of the state of the DataNode.
	 * @param dataObjectId   This is the database ID of a DataObject.
	 * @param scenarioId     This is the database ID of a Scenario.
	 */
	public TerminationCondition(int stateId, int dataObjectId, int scenarioId) {
		this.stateId = stateId;
		this.dataObjectId = dataObjectId;
		this.scenarioId = scenarioId;
	}

	public void save() {
		Connector conn = new Connector();
		this.conditionSetId = conn.insertTerminationConditionIntoDatabase(stateId, dataObjectId, scenarioId);
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
