package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

/**
 * .
 */
public class BoundaryEvent {
    /**
     * Stores the Id of the activity (or subprocess) the Event is attached to.
     */
    private String attachedToRef;
    private String id;
    private String name;
    private String outgoing;

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
