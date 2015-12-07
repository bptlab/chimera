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
    @XmlElement(name = "bpmn:sequenceFlow")
    private List<SequenceFlow> associations;
    @XmlElement(name = "bpmn:exclusiveGateway")
    private List<ExclusiveGateway> xorGateways;
    @XmlElement(name = "bpmn:startEvent")
    private StartEvent startEvent;
    @XmlElement(name = "bpmn:boundaryEvent")
    private List<BoundaryEvent> boundaryEvents;
    @XmlElement(name = "bpmn:task")
    private List<Task> tasks;
    @XmlElement(name = "bpmn:dataObjectReference")
    private List<DataObjectReference> dataObjectReferences;
    @XmlElement(name = "bpmn:dataObject")
    private List<DataObject> dataObjects;


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

    public List<DataObjectReference> getDataObjectReferences() {
        return dataObjectReferences;
    }

    public void setDataObjectReferences(List<DataObjectReference> dataObjectReferences) {
        this.dataObjectReferences = dataObjectReferences;
    }

    public List<DataObject> getDataObjects() {
        return dataObjects;
    }

    public void setDataObjects(List<DataObject> dataObjects) {
        this.dataObjects = dataObjects;
    }
}
