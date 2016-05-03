package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:receiveTask")
@XmlAccessorType(XmlAccessType.NONE)
public class ReceiveTask extends AbstractDataControlNode {
    /* "<bpmn:receiveTask id="ReceiveTask_12m516y" name="ReceiveTask" griffin:eventquery="SELECT * FROM Event">
            <bpmn:incoming>SequenceFlow_0vy2x8y</bpmn:incoming>
            <bpmn:outgoing>SequenceFlow_1uw5t49</bpmn:outgoing>
        </bpmn:receiveTask>" */

    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery;

    @Override
    public int save() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "ReceiveActivity", this.getFragmentId(), this.getId());

        connector.insertEventIntoDatabase("ReceiveActivity", this.eventQuery,
                this.fragmentId, this.getId(), this.databaseId);

        return this.databaseId;
    }

    public String getEventQuery() {
        return eventQuery;
    }

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

}
