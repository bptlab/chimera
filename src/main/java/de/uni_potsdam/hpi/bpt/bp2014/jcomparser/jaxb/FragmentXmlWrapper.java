package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlRootElement(name = "bpmn:definitions")
@XmlAccessorType(XmlAccessType.NONE)
public class FragmentXmlWrapper {

    @XmlElement(name = "bpmn:process")
    private BpmnProcessXml bpmnProcessXml;

    public List<SequenceFlow> getSequenceFlow() {
        return this.bpmnProcessXml.getSequenceFlowAssociations();
    }


    public List<Task> getTasks() {
        return this.bpmnProcessXml.getTasks();
    }


    public List<BoundaryEvent> getBoundaryEvents() {
        return this.bpmnProcessXml.getBoundaryEvents();
    }

    public List<EventBasedGateway> getEventBasedGateways() {
        return this.bpmnProcessXml.getEventBasedGateways();
    }


    public List<ExclusiveGateway> getXorGateways() {
        return this.bpmnProcessXml.getXorGateways();
    }

    public List<ParallelGateway> getAndGateways() {
        return this.bpmnProcessXml.getAndGateways();
    }


    public EndEvent getEndEvent() {
        return this.bpmnProcessXml.getEndEvent();
    }

    public StartEvent getStartEvent() {
        return this.bpmnProcessXml.getStartEvent();
    }

    public List<DataNode> getDataNodes() {
        return this.bpmnProcessXml.getDataNodes();
    }

    public String getId() {
        return this.bpmnProcessXml.getId();
    }

    public List<IntermediateEvent> getIntermediateEvents() {
        return this.bpmnProcessXml.getIntermediateEvents();
    }
    public List<IntermediateThrowEvent> getIntermediateThrowEvents() {
        return this.bpmnProcessXml.getIntermediateThrowEvents();
    }

    public List<WebServiceTask> getWebServiceTasks() {
        return this.bpmnProcessXml.getWebServiceTasks();
    }

    public List<ReceiveTask> getReceiveTasks() {
        return this.bpmnProcessXml.getReceiveTasks();
    }

    public List<SendTask> getSendTasks() {
        return this.bpmnProcessXml.getSendTasks();
    }

    public List<AbstractDataControlNode> getAllActivities() {
        List< ? super AbstractDataControlNode> allActivities = new ArrayList<>();
        allActivities.addAll(this.bpmnProcessXml.getTasks());
        allActivities.addAll(this.bpmnProcessXml.getWebServiceTasks());
        return (List<AbstractDataControlNode>) allActivities;
    }

    public List<AbstractDataControlNode> getAllEvents() {
        List< ? super AbstractDataControlNode> allEvents = new ArrayList<>();
        allEvents.add(this.bpmnProcessXml.getStartEvent());
        allEvents.add(this.bpmnProcessXml.getEndEvent());
        allEvents.addAll(this.bpmnProcessXml.getIntermediateEvents());
        allEvents.addAll(this.bpmnProcessXml.getBoundaryEvents());
        allEvents.addAll(this.bpmnProcessXml.getReceiveTasks());
        allEvents.addAll(this.bpmnProcessXml.getSendTasks());
        allEvents.addAll(this.bpmnProcessXml.getIntermediateThrowEvents());
        return (List<AbstractDataControlNode>) allEvents;
    }

    public List<AbstractDataControlNode> getAllDataControlNodes() {
        List<AbstractDataControlNode> allNodes = new ArrayList<>();
        allNodes.addAll(getAllActivities());
        allNodes.addAll(getAllEvents());
        return allNodes;
    }
}


