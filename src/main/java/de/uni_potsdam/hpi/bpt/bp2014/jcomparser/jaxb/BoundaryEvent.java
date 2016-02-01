package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import javax.xml.bind.annotation.*;
import java.util.Map;

/**
 * .
 */
@XmlRootElement(name = "bpmn:boundaryEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class BoundaryEvent extends AbstractControlNode {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;

    /**
     * Stores the Id of the activity (or subprocess) the Event is attached to.
     */
    @XmlAttribute(name = "attachedToRef")
    private String attachedToRef;

    @XmlElement(name = "bpmn:outgoing")
    private String outgoing;

    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery;


    public int save(Map<String, Integer> savedControlNodes) {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "BoundaryEvent", this.getFragmentId(), this.getId());

        int activityDatabaseId = savedControlNodes.get(this.getAttachedToRef());
        connector.insertBoundaryEventIntoDatabase("BoundaryEvent", this.getEventQuery(),
                this.getFragmentId(), this.getId(), this.getDatabaseId(), activityDatabaseId);

        return this.getDatabaseId();
    }

    public String getAttachedToRef() {
        return attachedToRef;
    }

    public void setAttachedToRef(String attachedToRef) {
        this.attachedToRef = attachedToRef;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int save() {
        throw new IllegalArgumentException("Boundary Events can not be saved without the "
                + "database id of the activity ");
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

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }

    public String getEventQuery() {
        return this.eventQuery;
    }

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }
}
