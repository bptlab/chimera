package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EndEvent extends AbstractControlNode {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "bpmn:incoming")
    private String incoming;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncoming() {
        return incoming;
    }

    public void setIncoming(String incoming) {
        this.incoming = incoming;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "Endevent", this.getFragmentId(), this.id);
        return this.databaseId;
    }
}
