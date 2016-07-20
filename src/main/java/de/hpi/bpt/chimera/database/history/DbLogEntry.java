package de.hpi.bpt.chimera.database.history;

import de.hpi.bpt.chimera.database.DbObject;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNodeInstance;
import de.hpi.bpt.chimera.database.data.DbDataClass;
import de.hpi.bpt.chimera.database.data.DbDataObject;
import de.hpi.bpt.chimera.database.data.DbState;
import de.hpi.bpt.chimera.jhistory.LogEntry;
import de.hpi.bpt.chimera.database.ConnectionWrapper;
import de.hpi.bpt.chimera.database.controlnodes.DbActivityInstance;
import de.hpi.bpt.chimera.database.controlnodes.DbControlNode;
import de.hpi.bpt.chimera.database.data.DbDataAttributeInstance;

import java.sql.*;
import java.util.*;

/**
 * Data Access object of the {@link LogEntry}
 */
public class DbLogEntry extends DbObject {

    private final static String insertQuery =
            "INSERT INTO logentry (`scenarioinstance_id`, `logged_id`,"
            + "`new_value`, `cause`, `label`, `type`, `timestamp`) VALUES "
            + "(?, ?, ?, ?, ?, ?, ?);";

    /**
     * This method saves a log entry of a newly created ActivityInstance into the database.
     *
     * @param activityInstanceId the ID of the ActivityInstance that is created.
     */
    public void logActivity(int activityInstanceId, String state, int scenarioInstanceId) {
        String label = new DbActivityInstance().getLabel(activityInstanceId);
        this.insertLog(scenarioInstanceId, activityInstanceId, state,
                Optional.empty(), label, LogEntry.LogType.ACTIVITY);
    }

    /**
     * This method saves a log entry for a DataAttributeInstance value change into the database.
     *
     * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is changed.
     * @param value                      the new value of the DataAttributeInstance.
     * @param causeInstanceId The Id of the control node which caused value change. Can be
     *                        an event or a activity
     */
    public void logDataAttributeTransition(int dataAttributeInstanceId, Object value,
                                          int causeInstanceId, int scenarioInstanceId) {

        DbDataAttributeInstance attributeDao = new DbDataAttributeInstance();
        int dataAttributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataAttributeId);
        this.insertLog(scenarioInstanceId, dataAttributeInstanceId, value.toString(),
                Optional.of(causeInstanceId), label, LogEntry.LogType.DATA_ATTRIBUTE);
    }

    /**
     * Same as above, but this method is used for start query transitions, since
     * they do not have a causeInstanceId.
     */
    public void logDataAttributeTransition(int dataAttributeInstanceId, Object value,
                                           int scenarioInstanceId) {
        DbDataAttributeInstance attributeDao = new DbDataAttributeInstance();
        int dataAttributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataAttributeId);
        this.insertLog(scenarioInstanceId, dataAttributeInstanceId, value.toString(),
                Optional.empty(), label, LogEntry.LogType.DATA_ATTRIBUTE);
    }

    /**
     * This method saves a log entry with a DataAttributeInstance value change in the database.
     *
     * @param objectInstanceId the ID of the DataObject that is changed.
     * @param stateId           the new state of the DataObject.
     * @param activityInstanceId
     */
    public void logDataObjectTransition(int objectInstanceId, int stateId,
                                        int activityInstanceId, int scenarioInstanceId) {
        int dataObjectId = new DbDataObject().getDataClassId(objectInstanceId);
        String label = new DbDataClass().getName(dataObjectId);
        String state = new DbState().getStateName(stateId);
        this.insertLog(scenarioInstanceId, objectInstanceId,
                state, Optional.of(activityInstanceId), label, LogEntry.LogType.DATA_OBJECT);
    }

    /**
     *
     * @param objectInstanceId
     * @param stateId
     * @param scenarioInstanceId
     */
    public void logDataObjectCreation(int objectInstanceId, int stateId, int scenarioInstanceId) {
        int dataObjectId = new DbDataObject().getDataClassId(objectInstanceId);
        String label = new DbDataClass().getName(dataObjectId);
        String state = new DbState().getStateName(stateId);
        this.insertLog(scenarioInstanceId, objectInstanceId,
                state, Optional.empty(), label, LogEntry.LogType.DATA_OBJECT);
    }

    public void logDataAttributeCreation(
            int dataAttributeInstanceId, Object value, int scenarioInstanceId) {
        DbDataAttributeInstance attributeDao = new DbDataAttributeInstance();
        int dataAttributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataAttributeId);
        if (null == value) {
            value = "";
        }
        this.insertLog(scenarioInstanceId, dataAttributeInstanceId, value.toString(),
                Optional.empty(), label, LogEntry.LogType.DATA_ATTRIBUTE);
    }


    public void logEvent(int eventInstanceId, int scenarioInstanceId, String status) {
        int controlNodeId = new DbControlNodeInstance().getControlNodeID(eventInstanceId);
        String label = new DbControlNode().getLabel(controlNodeId);
        this.insertLog(scenarioInstanceId, eventInstanceId, status, Optional.empty(),
                label, LogEntry.LogType.EVENT);
    }

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId The database ID of a ScenarioInstance.
     * @return a Map with a Map of the log entries' attribute names as keys with their values.
     */
    public List<LogEntry> getLogEntriesForScenarioInstance(
            int scenarioInstanceId, LogEntry.LogType type) {
        String sql = "SELECT * FROM logentry WHERE scenarioinstance_id = %d AND type = '%s' "
                        + "ORDER BY timestamp ASC;";
        return retrieveLogEntries(String.format(sql, scenarioInstanceId, type.name()));
    }

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId The database ID of a ScenarioInstance.
     * @return a Map with a Map of the log entries' attribute names as keys with their values.
     */
    public List<LogEntry> getLogEntriesForScenarioInstance(int scenarioInstanceId) {
        String sql = "SELECT * FROM logentry WHERE scenarioinstance_id = %d "
                + "ORDER BY timestamp ASC;";
        return retrieveLogEntries(String.format(sql, scenarioInstanceId));
    }

    /**
     * This method receives for each logged object the first entry in which this
     * object appeared.
     * @param scenarioInstanceId
     * @return List of log entries, where each logged id only occurs one with the min timestamp.
     */
    public List<LogEntry> getCreationLogEntries(int scenarioInstanceId) {
        String sql = "SELECT l1.* FROM logentry l1 " +
        "INNER JOIN (SELECT logged_id, MIN(timestamp) as timestamp FROM logentry GROUP BY logged_id) l2 " +
        "ON l1.logged_id = l2.logged_id WHERE l1.timestamp = l2.timestamp " +
        "AND l1.scenarioinstance_id = %d;";

        sql = String.format(sql, scenarioInstanceId);
        return retrieveLogEntries(sql);
    }

    /**
     * Retrieve all log entries that log the creation of an object,
     * calculated by the use of the timestamp.
     * @param scenarioInstanceId Id of the scenario istance.
     * @param type The type of the object.
     * @return All creation log entries.
     */
    public List<LogEntry> getCreationLogEntries(int scenarioInstanceId, LogEntry.LogType type) {
        String sql = "SELECT l1.* FROM logentry l1 " +
                "INNER JOIN (SELECT logged_id, MIN(timestamp) as timestamp, type FROM logentry " +
                "GROUP BY logged_id, type) l2 " +
                "ON l1.logged_id = l2.logged_id WHERE l1.timestamp = l2.timestamp AND " +
                "l1.type = l2.type AND l1.scenarioinstance_id = %d AND l1.type = '%s';";

        sql = String.format(sql, scenarioInstanceId, type.name());
        return retrieveLogEntries(sql);
    }

    private List<LogEntry> retrieveLogEntries(String sql) {
        List<LogEntry> entries = new ArrayList<>();
        try (java.sql.Connection conn = ConnectionWrapper.getInstance().connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                LogEntry entry = new LogEntry();
                entry.setId(rs.getInt("id"));
                entry.setCause(rs.getInt("cause"));
                entry.setLoggedId(rs.getInt("logged_id"));
                entry.setNewValue(rs.getString("new_value"));
                entry.setLabel(rs.getString("label"));
                entry.setScenarioInstanceId(rs.getInt("scenarioinstance_id"));
                entry.setType(LogEntry.LogType.valueOf(rs.getString("type")));
                entry.setTimeStamp(rs.getTimestamp("timestamp"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    private void insertLog(int scenarioInstanceId, int loggedId, String newValue,
                           Optional<Integer> cause, String label, LogEntry.LogType type) {
        java.sql.Connection con = ConnectionWrapper.getInstance().connect();
        try (PreparedStatement stat = con.prepareStatement(insertQuery)) {
            stat.setInt(1, scenarioInstanceId);
            stat.setInt(2, loggedId);
            stat.setString(3, newValue);
            if (cause.isPresent()) {
                stat.setInt(4, cause.get());
            } else {
                stat.setNull(4, Types.INTEGER);
            }
            stat.setString(5, label);
            stat.setString(6, type.name());
            stat.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
            stat.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
