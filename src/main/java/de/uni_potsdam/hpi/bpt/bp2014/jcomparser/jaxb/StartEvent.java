package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:startEvent")
@XmlAccessorType(XmlAccessType.NONE)
<<<<<<< HEAD
public class StartEvent extends AbstractDataControlNode {
=======
public class StartEvent extends AbstractControlNode {

    @XmlAttribute(name = "name")
    private String name = "";
>>>>>>> 831e057... Move incoming, outgoing and id fields from elements to abstractControlNode

    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery = "";

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "StartEvent", this.getFragmentId(), this.getId());

        connector.insertEventIntoDatabase("StartEvent", this.eventQuery,
            this.fragmentId, this.getId(), this.databaseId);

        return this.databaseId;
    }

    public String getEventQuery() {
        return this.eventQuery;
    }

<<<<<<< HEAD
=======
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

>>>>>>> 831e057... Move incoming, outgoing and id fields from elements to abstractControlNode
    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

}
