package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

/**
 * Class to store exclusive Gateway. An exclusive gateway can have an arbitrary number
 * of incoming control flows and one outgoing control flow.
 * Example String:
 * <bpmn:exclusiveGateway id="ExclusiveGateway_0pjea83">
 *      <bpmn:incoming>SequenceFlow_0306jnu</bpmn:incoming>
 *      <bpmn:outgoing>SequenceFlow_0ct04t8</bpmn:outgoing>
 *      <bpmn:outgoing>SequenceFlow_1pj6qq6</bpmn:outgoing>
 *   </bpmn:exclusiveGateway>
 */
public class ExclusiveGateway {
    private String id;
    private String incoming;
    private String outgoing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }
}
