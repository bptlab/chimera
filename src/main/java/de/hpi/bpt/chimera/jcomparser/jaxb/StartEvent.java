package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:startEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class StartEvent extends AbstractDataControlNode {

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

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

}
