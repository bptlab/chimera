package de.uni_potsdam.hpi.bpt.bp2014.database.data;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;

import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataObject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


/**
 * This class is the representation of a dataObject in the database.
 * It provides the functionality to check for existing data objects or create new ones.
 */
public class DbDataObject extends DbObject {
    private static Logger log = Logger.getLogger(DbDataObject.class);
	/**
	 * This method sets the state of a dataObject. Note that securing if the data object
	 * is unlocked has to be done by the layer above.
	 * @param id    This is the database ID of a dataObject instance.
	 * @param state This is the desirable state of a dataObject instance.
	 */
	public void setState(int id, int state) {
		String sql = "UPDATE dataobject SET state_id = " + state
				+ " WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method creates a data object. Newly created data objects are always unlocked.
	 * @param scenarioInstanceId This is the database ID of a scenario instance.
	 * @param stateId            This is the initial state of a dataObject instance.
	 * @param dataClassId       This is the database ID of a dataObject.
	 * @return the database ID of the newly created dataObject instance (Error: -1).
	 */
	public int createDataObject(int scenarioId, int scenarioInstanceId, int stateId, int dataClassId) {
		String sql =
				"INSERT INTO dataobject ("
						+ "scenario_id, scenarioinstance_id, state_id, dataclass_id, locked) "
						+ "VALUES (%d, %d, %d, %d, %b);";
        sql = String.format(sql, scenarioId, scenarioInstanceId, stateId, dataClassId, false);
		return this.executeInsertStatement(sql);
	}

    public List<Integer> getDataObjectIds(int scenarioInstanceId) {
        String sql = "SELECT id FROM dataobject WHERE scenarioinstance_id = %d;";
        sql = String.format(sql, scenarioInstanceId);
        return this.executeStatementReturnsListInt(sql, "id");
    }

	/**
	 * This method returns the state ID of a given data object.
	 *
	 * @param dataObjectId This is the database ID of a dataObject instance.
	 * @return the database ID of the state of the dataObject instance (Error: -1).
	 */
	public int getStateId(int dataObjectId) {
		String sql = "SELECT state_id FROM dataobject "
				+ "WHERE id = " + dataObjectId;
		return this.executeStatementReturnsInt(sql, "state_id");
	}

	/**
	 * This method sets the dataObject to a modified state
	 * so that no other activity can work with it.
	 * @param id       This is the database ID of a dataObject instance.
	 * @param locked This is the flag set to indicate if
	 *                    the dataObject instance is being modified or not.
	 */
	public void setLocked(int id, Boolean locked) {
		int lockedInt = (locked) ? 1 : 0;
		String sql =
				"UPDATE dataobject SET locked = " + lockedInt
						+ " WHERE id = " + id;
		this.executeUpdateStatement(sql);
	}

	/**
	 * This method returns the dataobject_id of a dataObject.
	 *
	 * @param dataObjectId Id of the dataObject.
	 * @return Dataobject_id.
	 */
	public int getDataClassId(int dataObjectId) {
		String sql = "SELECT dataclass_id FROM dataobject "
						+ "WHERE id = " + dataObjectId;
		return this.executeStatementReturnsInt(sql, "dataclass_id");
	}

    /**
     * This method returns the name of a dataObject.
     *
     * @param dataObjectId Id of the dataObject.
     * @return Dataobject_id.
     */
    public String getName(int dataObjectId) {
        String sql = "SELECT name FROM dataobject, dataclass " +
                "WHERE id = %d AND dataobject.dataclass_id = dataclass.id;";
        sql = String.format(sql, dataObjectId);
        return this.executeStatementReturnsString(sql, "name");
    }

    public boolean isLocked(int dataObjectId) {
        String sql = "SELECT locked FROM dataobject "
                + "WHERE id = " + dataObjectId;
        return this.executeStatementReturnsBoolean(sql, "locked");
    }

    /**
     * Looks up the values in the database which are stored under the
     * id of the passed data object and sets data class id, state and locked.
     * @param dataObject data object which should be initialized, but has id already set
     */
    public void loadFromDb(DataObject dataObject) {
        String sql = "SELECT * FROM dataobject WHERE id = %d;";
        sql = String.format(sql, dataObject.getId());
        java.sql.Connection con =  Connection.getInstance().connect();
        try (Statement stat = con.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            if (rs.next()) {
                int dataclassId = rs.getInt("dataclass_id");
                dataObject.setDataClassId(dataclassId);
                boolean locked = rs.getBoolean("locked");
                if (locked) {
                    dataObject.lock();
                }
                int state = rs.getInt("state_id");
                dataObject.setState(state);
            } else {
                log.error("Could not initialize data object from database.");
            }
        } catch (SQLException e) {
            log.error("Could not initialize data object from database.", e);
        }
    }
}

