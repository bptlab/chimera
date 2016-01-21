package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:intermediateCatchEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class IntermediateEvent extends AbstractControlNode {
    /*"<bpmn:intermediateCatchEvent id=\"IntermediateCatchEvent_1le8d7a\"
                griffin:eventquery="querY" name="Fell asleep while drinking coffee">\n" +
            "      <bpmn:incoming>SequenceFlow_12g1b5j</bpmn:incoming>\n" +
            "      <bpmn:outgoing>SequenceFlow_08s25px</bpmn:outgoing>\n" +
            "      <bpmn:messageEventDefinition />\n" +
            "    </bpmn:intermediateCatchEvent>"
    */

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "name")
    private String name = "";

    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery;

    @XmlElement(name = "bpmn:outgoing")
    private String outgoing;

    @XmlElement(name = "bpmn:incoming")
    private String incoming;

    private int databaseId;

    @Override
    public int getFragmentId() {
        return fragmentId;
    }

    @Override
    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    private int fragmentId;

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "IntermediateEvent", this.fragmentId, this.id);

        connector.insertEventIntoDatabase("IntermediateEvent", this.eventQuery,
                this.fragmentId, this.id, this.databaseId);

        return this.databaseId;
    }


    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventQuery() {
        return eventQuery;
    }

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }
}
