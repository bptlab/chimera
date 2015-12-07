package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 */
@XmlRootElement(name = "bpmn:process")
@XmlAccessorType(XmlAccessType.NONE)
public class Fragment {
    @XmlElement
    private List<SequenceFlow> associations;
    @XmlElement
    private List<ExclusiveGateway> xorGateways;
    @XmlElement
    private StartEvent startEvent;
    @XmlElement
    private List<BoundaryEvent> boundaryEvents;
    @XmlElement
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
