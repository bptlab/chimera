package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import java.util.List;

/**
 *
 */
public class Fragment {
    private List<SequenceFlow> associations;
    private List<ExclusiveGateway> xorGateways;
    private StartEvent startEvent;
    private List<BoundaryEvent> boundaryEvents;
    private List<Task> tasks;

    public List<SequenceFlow> getAssociations() {
        return associations;
    }

    public void setAssociations(List<SequenceFlow> associations) {
        this.associations = associations;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<BoundaryEvent> getBoundaryEvents() {
        return boundaryEvents;
    }

    public void setBoundaryEvents(List<BoundaryEvent> boundaryEvents) {
        this.boundaryEvents = boundaryEvents;
    }

    public List<ExclusiveGateway> getXorGateways() {
        return xorGateways;
    }

    public void setXorGateways(List<ExclusiveGateway> xorGateways) {
        this.xorGateways = xorGateways;
    }

    public StartEvent getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(StartEvent startEvent) {
        this.startEvent = startEvent;
    }
}
