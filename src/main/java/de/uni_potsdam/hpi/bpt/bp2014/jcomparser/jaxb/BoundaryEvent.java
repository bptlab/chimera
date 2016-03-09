package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

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


    /**
     *
     * @param savedControlNodes Map from the Id of the control nodes to the database Id, which
     *                          is needed to reference the activity the event is attached to.
     * @return
     */
    public void saveConnectionToActivity(Map<String, Integer> savedControlNodes) {
        Connector connector = new Connector();
        int activityDatabaseId = savedControlNodes.get(this.getAttachedToRef());
        connector.insertBoundaryEventIntoDatabase("BoundaryEvent", this.getEventQuery(),
                this.getFragmentId(), this.getId(), this.getDatabaseId(), activityDatabaseId);
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
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "BoundaryEvent", this.getFragmentId(), this.getId());
        return this.getDatabaseId();
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
