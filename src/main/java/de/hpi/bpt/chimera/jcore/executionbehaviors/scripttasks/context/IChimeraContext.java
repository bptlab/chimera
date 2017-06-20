package de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context;

public interface IChimeraContext {
    Object getParam(String name);
    void setParam(String name, Object value);
    int getProcessId();
    int getActivityId();
}