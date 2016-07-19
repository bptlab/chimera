package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.ConnectionWrapper;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import org.json.JSONObject;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents the change of a value in the database
 * e.g. activity instance 1 changed from ready to running at time
 * These logs abstract from {@link LogEntry} and not stored in the database,
 */
@XmlRootElement
public class StateTransitionLog {
    private Object oldValue;
    private Object newValue;
    private int loggedId;
    private Date timeStamp;
    private int cause;


    String label;

    public static List<StateTransitionLog> getStateTransitions(
            int scenarioInstanceId, LogEntry.LogType type) {
        String sql = "SELECT b.new_value as new_value, a.label as label, a.new_value as old_value, b.timestamp as timestamp, "
                + "       a.logged_id as logged_id, a.label as label, b.cause as cause "
                + "FROM logentry a JOIN logentry b "
                + "ON a.logged_id = b.logged_id "
                + "AND a.type = b.type "
                + "AND b.timestamp = (SELECT MIN(timestamp) FROM logentry WHERE "
                + "    timestamp >= a.timestamp AND a.logged_id = logged_id AND id <> a.id) "
                + "WHERE a.scenarioinstance_id = %d AND a.type = '%s';";
        sql = String.format(sql, scenarioInstanceId, type.name());
        List<StateTransitionLog> transitionLogs = parseStateTransitions(sql);
        addInitialTransitions(transitionLogs, scenarioInstanceId, type);
        return transitionLogs;
    }

    private static void addInitialTransitions(
            List<StateTransitionLog> transitions, int scenarioInstanceId, LogEntry.LogType type) {
        DbLogEntry logEntryDao = new DbLogEntry();
        List<LogEntry> logEntries = logEntryDao.getCreationLogEntries(scenarioInstanceId, type);
        for (LogEntry logEntry : logEntries) {
            StateTransitionLog stateTransition = new StateTransitionLog();
            stateTransition.setCause(logEntry.getCause());
            stateTransition.setLoggedId(logEntry.getLoggedId());
            stateTransition.setLabel(logEntry.getLabel());
            stateTransition.setNewValue(logEntry.getNewValue());
            stateTransition.setOldValue(JSONObject.NULL);
            stateTransition.setTimeStamp(logEntry.getTimeStamp());
            transitions.add(stateTransition);
        }
        transitions.sort((l1, l2) -> l1.getTimeStamp().compareTo(l2.getTimeStamp()));
    }

    public static List<StateTransitionLog> getStateTransitions(int scenarioInstanceId) {
        String sql =
                "SELECT b.new_value as new_value, a.label as label, a.new_value as old_value, b.timestamp as timestamp, " +
                        "a.logged_id as logged_id, a.label as label, b.cause as cause FROM logentry a JOIN logentry b" +
                        " ON a.logged_id = b.logged_id  AND a.type = b.type " +
                        " AND b.timestamp = (SELECT MIN(timestamp) FROM logentry WHERE" +
                        " timestamp >= a.timestamp AND a.logged_id = logged_id AND " +
                        "id <> a.id) " +
                        " WHERE a.scenarioinstance_id = %d;";
        sql = String.format(sql, scenarioInstanceId);
        List<StateTransitionLog> transitionLogs = parseStateTransitions(sql);
        addInitialTransitions(transitionLogs, scenarioInstanceId);
        return transitionLogs;
    }

    /**
     * This method is used to add an created transition to each logged object.
     * e.g. When having an activity which has a transition init -> running logged.
     * a transition from null -> init would be added to represent creation.
     *
     * @param transitions Transitions created from database join
     */
    private static void addInitialTransitions(
            List<StateTransitionLog> transitions, int scenarioInstanceId) {
        DbLogEntry logEntryDao = new DbLogEntry();
        List<LogEntry> logEntries = logEntryDao.getCreationLogEntries(scenarioInstanceId);
        for (LogEntry logEntry : logEntries) {
            StateTransitionLog stateTransition = new StateTransitionLog();
            stateTransition.setCause(logEntry.getCause());
            stateTransition.setLoggedId(logEntry.getLoggedId());
            stateTransition.setLabel(logEntry.getLabel());
            stateTransition.setNewValue(logEntry.getNewValue());
            stateTransition.setOldValue(JSONObject.NULL);
            stateTransition.setTimeStamp(logEntry.getTimeStamp());
            transitions.add(stateTransition);
        }
        transitions.sort((l1, l2) -> l1.getTimeStamp().compareTo(l2.getTimeStamp()));
    }

    private static List<StateTransitionLog> parseStateTransitions(String sql) {
        java.sql.Connection con = ConnectionWrapper.getInstance().connect();
        List<StateTransitionLog> transitionLogs = new ArrayList<>();
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                StateTransitionLog transitionLog = new StateTransitionLog();
                transitionLog.setCause(rs.getInt("cause"));
                transitionLog.setLoggedId(rs.getInt("logged_id"));
                transitionLog.setNewValue(rs.getString("new_value"));
                transitionLog.setOldValue(rs.getString("old_value"));
                transitionLog.setTimeStamp(rs.getTimestamp("timestamp"));
                transitionLog.setLabel(rs.getString("label"));
                transitionLogs.add(transitionLog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transitionLogs;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public int getLoggedId() {
        return loggedId;
    }

    public void setLoggedId(int loggedId) {
        this.loggedId = loggedId;
    }

    public Object getNewValue() {
        return newValue;
    }
    public int getCause() {
        return cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
