package de.uni_potsdam.hpi.bpt.bp2014.jhistory;


import java.util.Date;

/**
 *
 */
public class LogEntry {
    private int id;
    private Date timeStamp;
    private LogType type;
    private String newValue;


    private String label;
    private int scenarioInstanceId;

    /**
     * This id is the controlnode id in case of activities and events
     * or the id of a data attribute or data object.
     */
    private int loggedId;

    /**
     * Cause indicates that this log entry is the result of an activity or event
     * The value will be null for activity and event and only be set in data objects and data
     * attributes
     */
    private int cause;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public int getScenarioInstanceId() {
        return scenarioInstanceId;
    }

    public void setScenarioInstanceId(int scenarioInstanceId) {
        this.scenarioInstanceId = scenarioInstanceId;
    }

    public int getLoggedId() {
        return loggedId;
    }

    public void setLoggedId(int loggedId) {
        this.loggedId = loggedId;
    }

    public int getCause() {
        return cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public enum LogType {
        DATA_ATTRIBUTE,
        DATA_OBJECT,
        ACTIVITY,
        EVENT
    }
}
