package de.hpi.bpt.chimera.jcomparser.saving;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Possible control nodes are gateways, activities, webtasks and events. Each of them
 * get saved in the ControlNode table and can be part of control flow.
 */
@XmlTransient
public abstract class AbstractControlNode {

    protected int databaseId;

    protected int fragmentId;

    @XmlAttribute(name = "id")
    private String id;
    @XmlElement(name = "bpmn:incoming")
    private List<String> incoming = new ArrayList<>();
    @XmlElement(name = "bpmn:outgoing")
    private List<String> outgoing = new ArrayList<>();

    /**
     * Saves the control node to the database and returns it's database id.
     * @return database id which was created by autoincrement.
     */
    public abstract int save();

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<String> incoming) {
        this.incoming = incoming;
    }

    public List<String> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<String> outgoing) {
        this.outgoing = outgoing;
    }

}

