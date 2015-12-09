package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.DatabaseFragment;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Edge;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml.Node;

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
    @XmlElement(name = "bpmn:endEvent")
    private EndEvent endEvent;


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

    public void setEndEvent(EndEvent endEvent) {
        this.endEvent = endEvent;
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

    /**
     *
     * @return returns something
     */
    public DatabaseFragment createDbFragment() {
        DatabaseFragment frag = new DatabaseFragment();

        return frag;
    }

    /**
     *
     * @return Return something
     */
    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(getExclusiveGateways());
        nodes.addAll(getTaskNodes());
        nodes.addAll(getDataObjectNodes());
        nodes.add(getEndEventNode());
        nodes.add(getStartEventNode());
        return nodes;
    }

    /**
     *
     * @return returns something
     */
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        edges.addAll(getSequenceFlowEdges());
        return edges;
    }

    private Node getEndEventNode() {
        Node endEvent = new Node();
        endEvent.setId(this.endEvent.getId());
        endEvent.setType("EndEvent");
        endEvent.setText(this.endEvent.getName());
        endEvent.setGlobal(false);
        return endEvent;
    }

    private Node getStartEventNode() {
        Node startEvent = new Node();
        startEvent.setId(this.startEvent.getId());
        startEvent.setType("StartEvent");
        startEvent.setText(this.startEvent.getName());
        startEvent.setGlobal(false);
        return startEvent;
    }


    private List<Node> getExclusiveGateways() {
        List<Node> overallNodes = new ArrayList<>();
        for (ExclusiveGateway exclusiveGateway : this.xorGateways) {
            Node node = new Node();
            node.setType("ExclusiveGateway");
            node.setText(exclusiveGateway.getName());
            node.setId(exclusiveGateway.getId());
            node.setGlobal(false);
            overallNodes.add(node);
        }
        return overallNodes;
    }

    private List<Node> getDataObjectNodes() {
        List<Node> dataObjectNodes = new ArrayList<>();
        for (DataObjectReference dataObjectReference : dataObjectReferences) {
            Node node = new Node();
            node.setText(dataObjectReference.getName());
            node.setGlobal(false);
            node.setId(dataObjectReference.getId());
            node.setType("DataObject");
            dataObjectNodes.add(node);
        }
        return dataObjectNodes;
    }

    private List<Node> getTaskNodes() {
        List<Node> overallNodes = new ArrayList<>();
        for (Task task : tasks) {
            Node node = new Node();
            node.setType("Task");
            node.setText(task.getName());
            node.setId(task.getId());
            node.setGlobal(false);
            overallNodes.add(node);
        }
        return overallNodes;
    }

    private List<Edge> getSequenceFlowEdges() {
        List<Edge> sequenceFlowEdges = new ArrayList<>();
        for (SequenceFlow flow : this.associations) {
            Edge edge = new Edge();
            edge.setId(flow.getId());
            edge.setSourceNodeId(flow.getSourceRef());
            edge.setTargetNodeId(flow.getTargetRef());
            edge.setType("SequenceFlow");
            sequenceFlowEdges.add(edge);
            // edge setLabel
            // edge setDefault
        }
        return sequenceFlowEdges;
    }

    private List<Edge> getDataFlowAssociations() {
        List<Edge> overallEdges = new ArrayList<>();
        for (Task task : tasks) {
            for (DataInputAssociation association : task.getDataInputAssociations()) {
                Edge edge = new Edge();
                edge.setId(association.getId());
                edge.setIsDataInput(true);
                edge.setTargetNodeId(task.getId());
                edge.setSourceNodeId(association.getSourceRef());
                edge.setType("Association");
                overallEdges.add(edge);
            }

            for (DataOutputAssociation assoc : task.getDataOutputAssociations()) {
                Edge edge = new Edge();
                edge.setId(assoc.getId());
                edge.setIsDataInput(false);
                edge.setTargetNodeId(assoc.getTargetRef());
                edge.setSourceNodeId(task.getId());
                edge.setType("Association");
                overallEdges.add(edge);
            }
        }

        return overallEdges;
    }

}
