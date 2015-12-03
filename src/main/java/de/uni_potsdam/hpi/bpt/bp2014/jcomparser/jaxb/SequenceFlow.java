package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class used to store Sequence Flow elements from standard BPMN.
 * Example xml String
 * <bpmn:sequenceFlow id="SequenceFlow_17mh7cg"
 *      sourceRef="Task_0xjpfqa"
 *      targetRef="ExclusiveGateway_0ns9z26"
 * />
 */
@XmlRootElement
public class SequenceFlow {
    private int id;
    /**
     * Node from which edge starts. This can be a gateway, activity or event.
     */
    private String sourceRef;
    /**
     * Node to which edge points to. This can be a gateway, activity or event.
     */
    private String targetRef;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }
}
