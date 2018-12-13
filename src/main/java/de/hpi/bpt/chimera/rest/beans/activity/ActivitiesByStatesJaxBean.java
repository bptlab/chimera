package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;

import java.util.List;
import java.util.stream.Collectors;

public class ActivitiesByStatesJaxBean {
    private List<ActivityJaxBean> ready;
    private List<ActivityJaxBean> running;

    public ActivitiesByStatesJaxBean(List<AbstractActivityInstance> activitiesInstances) {
        setReady(filterByState(activitiesInstances, State.READY));
        setRunning(filterByState(activitiesInstances, State.RUNNING));
    }

    private List<ActivityJaxBean> filterByState(List<AbstractActivityInstance> activitiesInstances, State state) {
        return activitiesInstances.stream()
                .filter(a -> a.getState().equals(state))
                .map(ActivityJaxBean::new)
                .collect(Collectors.toList());
    }
    public List<ActivityJaxBean> getReady() {
        return ready;
    }

    public void setReady(List<ActivityJaxBean> ready) {
        this.ready = ready;
    }

    public List<ActivityJaxBean> getRunning() {
        return running;
    }

    public void setRunning(List<ActivityJaxBean> running) {
        this.running = running;
    }
}
