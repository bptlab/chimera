package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

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
