package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jcore.executionbehaviors.AbstractStateMachine;

import java.util.List;
import java.util.Map;

/**
 * Represents the activity instance of the database.
 * Methods are mostly used by the HistoryService.
 * Hence, you can get all terminated activities or start new ones.
 */

public class DbActivityInstance extends DbObject {
	/**
	 * This method returns the actual state of an activity.
	 *
	 * @param id This is the database ID of an activity instance.
	 * @return the state of the activity as a String.
	 */
	public AbstractStateMachine.STATE getState(int id) {
		String sql = "SELECT activity_state FROM activityinstance WHERE id = " + id;
		String activityState = this.executeStatementReturnsString(sql, "activity_state");
	    return AbstractStateMachine.STATE.valueOf(activityState.toUpperCase());
    }

	/**
	 * This method sets the activity to a state and saves a log entry into the database.
	 *
	 * @param id    This is the database ID of an activity instance in the database.
	 * @param state This is the state in which an activity should be after executing setState.
	 */
	public void setState(int id, AbstractStateMachine.STATE state) {
        String sql = "UPDATE activityinstance "
				+ "SET activity_state = '" + state.name() + "' "
				+ "WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method creates and saves a new activity instance to the database
	 * into the context of a controlNode instance and saves a log entry into the database.
	 *
	 * @param controlNodeInstanceId This is the ID of a controlNode instance in the database.
	 * @param activityType           This is the type of an activity.
	 * @param activityState          This is the state of an activity.
	 * @return the database ID of the newly created activity instance as an integer (Error: -1).
	 */
	public int createNewActivityInstance(int controlNodeInstanceId, String activityType,
			String activityState) {
		String sql =
				"INSERT INTO activityinstance ("
						+ "id, type, role_id, "
						+ "activity_state, workitem_state) "
						+ "VALUES ("	+ controlNodeInstanceId + ", '"
						+ activityType + "', 1,'"
						+ activityState	+ "', 'init')";
		return this.executeInsertStatement(sql);
	}

    // TODO document
    public Boolean existsActivityInstance(int controlNodeInstanceId, int scenarioInstanceId) {
        String existsActivityInstance = "SELECT * FROM controlnodeinstance as cni, " +
                "fragmentinstance as fi WHERE cni.fragmentinstance_id = fi.id " +
                "AND fi.scenarioinstance_id = %d AND cni.id = %d;";
        existsActivityInstance = String.format(existsActivityInstance,
                scenarioInstanceId, controlNodeInstanceId);
        return this.executeExistStatement(existsActivityInstance);
    }

	/**
	 * This method returns all database ID's for all activities
	 * who have terminated in the context of a scenario instance.
	 *
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @return a list of activity ID's which belong to the scenario instance and are terminated.
	 */
	public List<Integer> getTerminatedActivitiesForScenarioInstance(
			int scenarioInstanceId) {
		String sql =
				"SELECT controlnode_id "
						+ "FROM activityinstance, controlnodeinstance "
						+ "WHERE "
						+ "activityinstance.id = controlnodeinstance.id "
						+ "AND controlnodeinstance.Type = 'Activity' "
						+ "AND activity_state = 'terminated' "
						+ "AND fragmentinstance_id "
						+ "IN (SELECT id FROM fragmentinstance "
						+ "WHERE scenarioinstance_id ="
						+ scenarioInstanceId + ")";
		return this.executeStatementReturnsListInt(sql, "controlnode_id");
	}

	/**
	 *
	 * @param id This is the database ID of an activity instance.
	 * @return a boolean with the Automatic Execution state
	 */
	public boolean getAutomaticExecution(int id) {
		String sql = "SELECT automaticexecution FROM activityinstance WHERE id = " + id;
		return this.executeStatementReturnsBoolean(sql, "automaticexecution");
	}

	/**
	 *
	 * @param id This is the database ID of an activity instance.
	 * @param automaticExecution This is a boolean Automatic Execution state.
	 */
	public void setAutomaticExecution(int id, boolean automaticExecution) {
		int automaticExecutionAsInt;
		if (automaticExecution) {
			automaticExecutionAsInt = 1;
		} else {
			automaticExecutionAsInt = 0;
		}
		String sql = "UPDATE activityinstance "
				+ "SET automaticexecution = " + automaticExecutionAsInt + " "
				+ "WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 *
	 * @param instanceId This is the database ID of an activity instance.
	 * @return a Map of all activity instances
	 */
	public Map<Integer, Map<String, Object>> getMapForAllActivityInstances(int instanceId) {
		String sql =
				"SELECT activityinstance.id AS id, activityinstance.type AS type, "
						+ "activity_state, controlnode.label AS label "
						+ "FROM activityinstance, "
						+ "controlnodeinstance, controlnode "
						+ "WHERE "
						+ "activityinstance.id = controlnodeinstance.id "
						+ "AND controlnodeinstance.Type = 'Activity' "
						+ "AND controlnodeinstance.controlnode_id = "
						+ 		"controlnode.id "
						+ "AND fragmentinstance_id "
						+ "IN (Select id FROM fragmentinstance "
						+ "WHERE scenarioinstance_id =" + instanceId + ")";
		return this.executeStatementReturnsMapWithMapWithKeys(
				sql, "id", "type", "activity_state", "label");
	}

	/**
	 *
	 * @param instanceID This is the database ID of an activity instance.
	 * @param filterString This is a filter string for labels.
	 * @param state This is a filter for activity states.
	 * @return a Map of all matching activity instances
	 */
	public Map<Integer, Map<String, Object>> getMapForActivityInstancesWithFilterAndState(
			int instanceID, String filterString, String state) {
		String sql =
				"SELECT activityinstance.id AS id, activityinstance.type AS type, "
						+ "activity_state, controlnode.label AS label "
						+ "FROM activityinstance, "
						+ "controlnodeinstance, controlnode "
						+ "WHERE "
						+ "activityinstance.id = controlnodeinstance.id "
						+ "AND controlnodeinstance.Type = 'Activity' "
						+ "AND controlnodeinstance.controlnode_id = "
						+ "controlnode.id AND controlnode.label "
						+ "LIKE '%" + filterString + "%' "
						+ "AND activity_state = '" + state + "'"
						+ "AND fragmentinstance_id IN (Select id "
						+ "FROM fragmentinstance "
						+ "WHERE scenarioinstance_id =" + instanceID + ")";
		return this.executeStatementReturnsMapWithMapWithKeys(
				sql, "id", "type", "activity_state", "label");
	}

	/**
	 *
	 * @param instanceID This is the database ID of an activity instance.
	 * @param state This is a filter for activity states.
	 * @return a Map of all matching activity instances
	 */
	public Map<Integer, Map<String, Object>> getMapForActivityInstancesWithState(
			int instanceID,	String state) {
		String sql =
				"SELECT activityinstance.id AS id, activityinstance.type AS type, "
						+ "activity_state, controlnode.label AS label "
						+ "FROM activityinstance, "
						+ "controlnodeinstance, controlnode "
						+ "WHERE "
						+ "activityinstance.id = controlnodeinstance.id "
						+ "AND controlnodeinstance.Type = 'Activity' "
						+ "AND controlnodeinstance.controlnode_id = "
						+ "controlnode.id "
						+ "AND activity_state = '" + state + "'"
						+ "AND fragmentinstance_id IN (Select id "
						+ "FROM fragmentinstance "
						+ "WHERE scenarioinstance_id =" + instanceID + ")";
		return this.executeStatementReturnsMapWithMapWithKeys(
				sql, "id", "type", "activity_state", "label");
	}

	/**
	 *
	 * @param instanceID This is the database ID of an activity instance.
	 * @param filterString This is a filter string for labels.
	 * @return a Map of all matching activity instances
	 */
	public Map<Integer, Map<String, Object>> getMapForActivityInstancesWithFilter(
			int instanceID,	String filterString) {
		String sql =
				"SELECT activityinstance.id AS id, activityinstance.type AS type, "
						+ "activity_state, controlnode.label AS label "
						+ "FROM activityinstance, "
						+ "controlnodeinstance, controlnode "
						+ "WHERE "
						+ "activityinstance.id = controlnodeinstance.id "
						+ "AND controlnodeinstance.Type = 'Activity' "
						+ "AND controlnodeinstance.controlnode_id = "
						+ "controlnode.id AND controlnode.label "
						+ "LIKE '%" + filterString + "%'" + " "
						+ "AND fragmentinstance_id IN (Select id "
						+ "FROM fragmentinstance "
						+ "WHERE scenarioinstance_id =" + instanceID + ")";
		return this.executeStatementReturnsMapWithMapWithKeys(
				sql, "id", "type", "activity_state", "label");
	}

	/**
	 *
	 * @param id This is the database ID of an activity instance.
	 * @return a boolean indicating whether the instance can terminate
	 */
	public boolean getCanTerminate(int id) {
		String sql = "SELECT canTerminate FROM activityinstance WHERE id = " + id;
		return this.executeStatementReturnsBoolean(sql, "canTerminate");
	}

	/**
	 *
	 * @param id This is the database ID of an activity instance.
	 * @param canTerminate This is a boolean indicating whether the instance can terminate
	 */
	public void setCanTerminate(int id, boolean canTerminate) {
		int canTerminateAsInt;
		if (canTerminate) {
			canTerminateAsInt = 1;
		} else {
			canTerminateAsInt = 0;
		}
		String sql =
				"UPDATE activityinstance "
						+ "SET canTerminate = " + canTerminateAsInt
						+ " WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

    public String getLabel(int activityInstanceId) {
        String sql = "SELECT cn.label as label FROM controlnodeinstance as cni," +
                "controlnode as cn WHERE cni.controlnode_id = cn.id AND cni.id = %d; ";
        sql = String.format(sql, activityInstanceId);
        return this.executeStatementReturnsString(sql, "label");
    }
}
