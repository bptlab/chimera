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


    public List<ExclusiveGateway> getXorGateways() {
        return this.bpmnProcessXml.getXorGateways();
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

}


