package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;


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

	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "name")
	private String name = "";
	@XmlElement(name = "bpmn:incoming")
	private List<String> incomingSequenceFlows = new ArrayList<>();
	@XmlElement(name = "bpmn:outgoing")
	private List<String> outgoingSequenceFlows = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public List<String> getIncomingSequenceFlows() {
		return incomingSequenceFlows;
	}

	public void setIncomingSequenceFlows(List<String> incomingSequenceFlows) {
		this.incomingSequenceFlows = incomingSequenceFlows;
	}

	public List<String> getOutgoingSequenceFlows() {
		return outgoingSequenceFlows;
	}

	public void setOutgoingSequenceFlows(List<String> outgoingSequenceFlows) {
		this.outgoingSequenceFlows = outgoingSequenceFlows;
	}
}


