package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:startEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class StartEvent extends AbstractControlNode {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "name")
    private String name = "";

    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery = "";

    @XmlElement(name = "bpmn:outgoing")
    private String outgoing;

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "StartEvent", this.getFragmentId(), this.id);

        connector.insertEventIntoDatabase("StartEvent", this.eventQuery,
            this.fragmentId, this.id, this.databaseId);

        return this.databaseId;
    }

    public String getEventQuery() {
        return this.eventQuery;
    }


    @Override
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

}
