package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;
import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.LogEntry;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
        int dataattributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataattributeId);
        this.insertLog(scenarioInstanceId, dataAttributeInstanceId, value.toString(),
                Optional.of(causeInstanceId), label, LogEntry.LogType.DATA_ATTRIBUTE);
    }

    /**
     * This method saves a log entry with a DataAttributeInstance value change in the database.
     *
     * @param objectInstanceId the ID of the DataObjectInstance that is changed.
     * @param stateId           the new state of the DataObjectInstance.
     * @param activityInstanceId
     */
    public void logDataobjectTransition(int objectInstanceId, int stateId,
                                       int activityInstanceId, int scenarioInstanceId) {
        int dataObjectId = new DbDataObjectInstance().getDataObjectID(objectInstanceId);
        String label = new DbDataObject().getName(dataObjectId);
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
    public void logDataobjectCreation(int objectInstanceId, int stateId, int scenarioInstanceId) {
        int dataObjectId = new DbDataObjectInstance().getDataObjectID(objectInstanceId);
        String label = new DbDataObject().getName(dataObjectId);
        String state = new DbState().getStateName(stateId);
        this.insertLog(scenarioInstanceId, objectInstanceId,
                state, Optional.empty(), label, LogEntry.LogType.DATA_OBJECT);
    }

    public void logDataattributeCreation(
            int dataAttributeInstanceId, Object value, int scenarioInstanceId) {
        DbDataAttributeInstance attributeDao = new DbDataAttributeInstance();
        int dataattributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataattributeId);
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
        return receiveLogEntries(String.format(sql, scenarioInstanceId, type.name()));
    }

    private List<LogEntry> receiveLogEntries(String sql) {
        List<LogEntry> entries = new ArrayList<>();
        try (java.sql.Connection conn = Connection.getInstance().connect();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                LogEntry entry = new LogEntry();
                entry.setId(rs.getInt("id"));
                entry.setCause(rs.getInt("cause"));
                entry.setLoggedId(rs.getInt("logged_id"));
                entry.setNewValue(rs.getString("new_value"));
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
        java.sql.Connection con = Connection.getInstance().connect();
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
