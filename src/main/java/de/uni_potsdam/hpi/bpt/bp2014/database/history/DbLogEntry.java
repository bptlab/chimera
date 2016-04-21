package de.uni_potsdam.hpi.bpt.bp2014.database.history;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbActivityInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import de.uni_potsdam.hpi.bpt.bp2014.jhistory.LogEntry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Data Access object of the {@link LogEntry}
 */
public class DbLogEntry extends DbObject {
    /**
     * This method saves a log entry of a newly created ActivityInstance into the database.
     *
     * @param activityInstanceId the ID of the ActivityInstance that is created.
     * @return the generated key for the insert statement.
     */
    public int logActivity(int activityInstanceId, String state, int scenarioInstanceId) {
        String label = new DbActivityInstance().getLabel(activityInstanceId);
        String sql = "INSERT INTO logentry (`scenarioinstance_id`, `logged_id`,"
                + "`new_value`, `cause`, `label`, `type`) VALUES "
                + "(%d, %d, '%s', NULL, '%s', '%s');";
        sql = String.format(sql, scenarioInstanceId, activityInstanceId, state,
                label, LogEntry.LogType.ACTIVITY.name());

        return this.executeInsertStatement(sql);
    }

    /**
     * This method saves a log entry for a DataAttributeInstance value change into the database.
     *
     * @param dataAttributeInstanceId the ID of the DataAttributeInstance that is changed.
     * @param value                      the new value of the DataAttributeInstance.
     * @param causeInstanceId The Id of the control node which caused value change. Can be
     *                        an event or a activity
     * @return the generated key for the insert statement.
     */
    public int logDataAttributeTransition(int dataAttributeInstanceId, Object value,
                                          int causeInstanceId, int scenarioInstanceId) {

        DbDataAttributeInstance attributeDao = new DbDataAttributeInstance();
        int dataattributeId = attributeDao.getDataAttributeID(dataAttributeInstanceId);
        String label = attributeDao.getName(dataattributeId);
        String sql = "INSERT INTO logentry (`scenarioinstance_id`, `logged_id`,"
                + "`new_value`, `cause`, `label`, `type`) VALUES "
                + "(%d, %d, '%s', %d, '%s', '%s');";
        sql = String.format(sql, scenarioInstanceId, dataAttributeInstanceId, value,
                causeInstanceId, label, LogEntry.LogType.DATA_ATTRIBUTE.name());
        return this.executeInsertStatement(sql);
    }

    /**
     * This method saves a log entry with a DataAttributeInstance value change in the database.
     *
     * @param objectInstanceId the ID of the DataObjectInstance that is changed.
     * @param stateId           the new state of the DataObjectInstance.
     * @param activityInstanceId
     * @return the generated key for the insert statement.
     */
    public int logStateTransition(int objectInstanceId, int stateId, int activityInstanceId) {
        String sql =
                "INSERT INTO `logentry` (`scenarioinstance_id`, `logged_id`, " +
                        " `new_value`, `cause`, `label`) "
                        + "SELECT (SELECT `scenarioinstance_id` "
                        + "FROM `dataobjectinstance` "
                        + "WHERE `id` = " + objectInstanceId
                        + ") AS`scenarioinstance_id`, `id`,"
                        + "(SELECT `state_id` FROM `dataobjectinstance` "
                        + "WHERE `id` = " + objectInstanceId
                        + ") AS `oldstate_id`, "
                        + stateId + " AS `newstate_id`, " + activityInstanceId
                        + " FROM `dataobjectinstance` "
                        + "WHERE `id` = " + objectInstanceId;
        return this.executeInsertStatement(sql);
    }

    /**
     * This method returns the Activity log entries for a ScenarioInstance.
     *
     * @param scenarioInstanceId The database ID of a ScenarioInstance.
     * @return a Map with a Map of the log entries' attribute names as keys with their values.
     */
    public List<LogEntry> getLogEntriesForScenarioInstance(
            int scenarioInstanceId, LogEntry.LogType type) {
        String sql = "SELECT * FROM logentry AS WHERE scenarioinstance_id = %d AND type = %s;"
                        + "ORDER BY timestamp DESC;";
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
                entry.setLoggedId(rs.getInt("loggedId"));
                entry.setNewValue(rs.getString("new_value"));
                entry.setScenarioInstanceId(rs.getInt("scenarioinstance_id"));
                entry.setType(LogEntry.LogType.valueOf(rs.getString("type")));
                entry.setTimeStamp(rs.getDate("timestamp"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }
}
