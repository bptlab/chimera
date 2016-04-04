package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlRootElement(name = "bpmn:process")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmnProcessXml {
    @XmlAttribute
    private String id;

    @XmlElement(name = "bpmn:sequenceFlow")
    private List<SequenceFlow> sequenceFlowAssociations = new ArrayList<>();

    @XmlElement(name = "bpmn:exclusiveGateway")
    private List<ExclusiveGateway> xorGateways = new ArrayList<>();

    @XmlElement(name = "bpmn:startEvent")
    private StartEvent startEvent;

    @XmlElement(name = "bpmn:boundaryEvent")
    private List<BoundaryEvent> boundaryEvents = new ArrayList<>();

    @XmlElement(name = "bpmn:task")
    private List<Task> tasks = new ArrayList<>();

    @XmlElement(name = "bpmn:serviceTask")
    private List<ServiceTask> serviceTasks = new ArrayList<>();

    @XmlElement(name = "bpmn:dataObjectReference")
    private List<DataNode> dataNodes = new ArrayList<>();

    @XmlElement(name = "bpmn:intermediateCatchEvent")
    private List<IntermediateEvent> intermediateEvents = new ArrayList<>();

    @XmlElement(name = "bpmn:eventBasedGateway")
    private List<EventBasedGateway> eventBasedGateways = new ArrayList<>();

    @XmlElement(name = "bpmn:endEvent")
    private EndEvent endEvent;

    public EndEvent getEndEvent() {
        return endEvent;
    }

    public void setEndEvent(EndEvent endEvent) {
        this.endEvent = endEvent;
    }

    public List<SequenceFlow> getSequenceFlowAssociations() {
        return sequenceFlowAssociations;
    }

    public void setSequenceFlowAssociations(List<SequenceFlow> sequenceFlowAssociations) {
        this.sequenceFlowAssociations = sequenceFlowAssociations;
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

    public List<BoundaryEvent> getBoundaryEvents() {
        return boundaryEvents;
    }

    public void setBoundaryEvents(List<BoundaryEvent> boundaryEvents) {
        this.boundaryEvents = boundaryEvents;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<DataNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(List<DataNode> dataNodes) {
        this.dataNodes = dataNodes;
    }

    public List<IntermediateEvent> getIntermediateEvents() {
        return intermediateEvents;
    }

    public void setIntermediateEvents(List<IntermediateEvent> intermediateEvents) {
        this.intermediateEvents = intermediateEvents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<EventBasedGateway> getEventBasedGateways() {
        return this.eventBasedGateways;
    }

    public List<ServiceTask> getServiceTasks() {
        return serviceTasks;
    }
}
