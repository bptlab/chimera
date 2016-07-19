package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:intermediateCatchEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class IntermediateCatchEvent extends AbstractDataControlNode {
    /*"<bpmn:intermediateCatchEvent id=\"IntermediateCatchEvent_1le8d7a\"
                griffin:eventquery="querY" name="Fell asleep while drinking coffee">\n" +
            "      <bpmn:incoming>SequenceFlow_12g1b5j</bpmn:incoming>\n" +
            "      <bpmn:outgoing>SequenceFlow_08s25px</bpmn:outgoing>\n" +
            "      <bpmn:messageEventDefinition />\n" +
            "    </bpmn:intermediateCatchEvent>"
    */


    @XmlAttribute(name = "griffin:eventquery")
    private String eventQuery;

    @XmlElement(name = "bpmn:timerEventDefinition")
    private TimerDefinition timer;

    @Override
    public int save() {
        if (timer == null) {
            saveIntermediateWithoutTimer();
        } else {
            saveTimerIntermediate();
        }
        return this.databaseId;
    }

    private void saveIntermediateWithoutTimer() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "IntermediateCatchEvent", this.fragmentId, this.getId());

        connector.insertEventIntoDatabase("IntermediateCatchEvent", this.eventQuery,
                this.fragmentId, this.getId(), this.databaseId);
    }

    private void saveTimerIntermediate() {
        Connector connector = new Connector();
        this.databaseId = connector.insertControlNodeIntoDatabase(
                this.getName(), "TimerEvent", this.fragmentId, this.getId());
        connector.insertEventIntoDatabase("TimerEvent", this.eventQuery,
                this.fragmentId, this.getId(), this.databaseId);
        connector.saveTimerDefinition(timer.getTimerDuration(), this.fragmentId, this.databaseId);
    }

    public String getEventQuery() {
        return eventQuery;
    }

    public void setEventQuery(String eventQuery) {
        this.eventQuery = eventQuery;
    }

    public TimerDefinition getTimer() {
        return timer;
    }

}
