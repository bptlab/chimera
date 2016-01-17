package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import javax.xml.bind.annotation.*;

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

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "Boundaryevent", this.getFragmentId(), this.id);
        return this.databaseId;
    }

    public String getAttachedToRef() {
        return attachedToRef;
    }

    public void setAttachedToRef(String attachedToRef) {
        this.attachedToRef = attachedToRef;
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

    public String getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(String outgoing) {
        this.outgoing = outgoing;
    }
}
