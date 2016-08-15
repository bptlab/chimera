package de.hpi.bpt.chimera.database.controlnodes;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.jhistory.HistoryService;
import de.hpi.bpt.chimera.jcore.controlnodes.State;


import java.util.List;

/**
 * Represents the activity instance of the database.
 * Methods are mostly used by the {@link HistoryService}.
 * Hence, you can get all terminated activities or start new ones.
 */

public class DbActivityInstance extends DbObject {
	/**
	 * This method creates and saves a new activity instance to the database
	 * into the context of a controlNode instance and saves a log entry into the database.
	 *
	 * @param controlNodeInstanceId This is the ID of a controlNode instance in the database.
	 * @param activityType           This is the type of an activity.
	 * @return the database ID of the newly created activity instance as an integer (Error: -1).
	 */
	public int createNewActivityInstance(int controlNodeInstanceId, String activityType) {
		// TODO remove unnessecary values in activity instance table
        String sql = "Insert INTO activityinstance (id, type, role_id, workitem_state) " +
                "VALUES (%d, '%s', 1, 'init')";
        sql = String.format(sql, controlNodeInstanceId, activityType);
		return this.executeInsertStatement(sql);
	}

	/**
	 * This method checks whether an activity instance exists for the given control
	 * node instance in the given scenario instance.
	 * @param controlNodeInstanceId Id of a control node instance.
	 * @param scenarioInstanceId Id of a scenario instance.
	 * @return True, if an activity instance exists for the specified Ids. False else.
     */
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
						+ "AND controlnodeinstance.state = 'terminated' "
						+ "AND fragmentinstance_id "
						+ "IN (SELECT id FROM fragmentinstance "
						+ "WHERE scenarioinstance_id ="
						+ scenarioInstanceId + ")";
		return this.executeStatementReturnsListInt(sql, "controlnode_id");
	}

	/**
	 * Retrieve whether an activity instance is automatic.
	 * @param id This is the database ID of an activity instance.
	 * @return a boolean with the Automatic Execution state
	 */
	public boolean getAutomaticExecution(int id) {
		String sql = "SELECT automaticexecution FROM activityinstance WHERE id = " + id;
		return this.executeStatementReturnsBoolean(sql, "automaticexecution");
	}

	/**
	 * Set the execution mode for an activity instance.
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
	 * Retrieves whether an activity instance can be terminated.
	 * @param id This is the database ID of an activity instance.
	 * @return a boolean indicating whether the instance can terminate
	 */
	public boolean getCanTerminate(int id) {
		String sql = "SELECT canTerminate FROM activityinstance WHERE id = " + id;
		return this.executeStatementReturnsBoolean(sql, "canTerminate");
	}

	/**
	 * Sets the termination permission for an activity instance.
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

	/**
	 * Retrieves the activity name for a given activity instance.
	 * @param activityInstanceId The Id of the activity instance.
	 * @return The name of the activity.
     */
    public String getLabel(int activityInstanceId) {
        String sql = "SELECT cn.label as label FROM controlnodeinstance as cni," +
                "controlnode as cn WHERE cni.controlnode_id = cn.id AND cni.id = %d; ";
        sql = String.format(sql, activityInstanceId);
        return this.executeStatementReturnsString(sql, "label");
    }
}
