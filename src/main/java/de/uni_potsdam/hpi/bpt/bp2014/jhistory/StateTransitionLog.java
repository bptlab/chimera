package de.uni_potsdam.hpi.bpt.bp2014.jhistory;

import de.uni_potsdam.hpi.bpt.bp2014.database.Connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
    private String oldValue;
    private String newValue;
    private int loggedId;
    private Date timeStamp;
    private int cause;


    String label;

    public static List<StateTransitionLog> getStateTransitons(
            int scenarioInstanceId, LogEntry.LogType type) {
        String sql =
                "SELECT b.new_value as new_value, a.label as label, a.new_value as old_value, b.timestamp as timestamp, " +
                "a.logged_id as logged_id, a.label as label, b.cause as cause FROM logentry a JOIN logentry b" +
                " ON a.logged_id = b.logged_id AND b.timestamp = (SELECT MIN(timestamp) FROM logentry WHERE" +
                " timestamp >= a.timestamp AND a.logged_id = logged_id AND id <> a.id) " +
                " WHERE a.scenarioinstance_id = %d AND a.type = '%s';";
        return parseStateTransitions(String.format(sql, scenarioInstanceId, type.name()));
    }

    public static List<StateTransitionLog> getStateTransitons(int scenarioInstanceId) {
        String sql =
                "SELECT b.new_value as new_value, a.label as label, a.new_value as old_value, b.timestamp as timestamp, " +
                        "a.logged_id as logged_id, a.label as label, b.cause as cause FROM logentry a JOIN logentry b" +
                        " ON a.logged_id = b.logged_id AND b.timestamp = (SELECT MIN(timestamp) FROM logentry WHERE" +
                        " timestamp >= a.timestamp AND a.logged_id = logged_id AND id <> a.id) " +
                        " WHERE a.scenarioinstance_id = %d;";
        return parseStateTransitions(String.format(sql, scenarioInstanceId));
    }


    private static List<StateTransitionLog> parseStateTransitions(String sql) {
        java.sql.Connection con = Connection.getInstance().connect();
        List<StateTransitionLog> transitionLogs = new ArrayList<>();
        try (Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                StateTransitionLog transitionLog = new StateTransitionLog();
                transitionLog.setCause(rs.getInt("cause"));
                transitionLog.setLoggedId(rs.getInt("logged_id"));
                transitionLog.setNewValue(rs.getString("new_value"));
                transitionLog.setOldValue(rs.getString("old_value"));
                transitionLog.setTimeStamp(rs.getTime("timestamp"));
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public int getLoggedId() {
        return loggedId;
    }

    public void setLoggedId(int loggedId) {
        this.loggedId = loggedId;
    }

    public String getNewValue() {
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
