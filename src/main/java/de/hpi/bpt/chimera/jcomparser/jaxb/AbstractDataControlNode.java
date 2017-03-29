package de.hpi.bpt.chimera.jcomparser.jaxb;

import de.hpi.bpt.chimera.jcomparser.saving.AbstractControlNode;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractDataControlNode extends AbstractControlNode {
	@XmlAttribute(name = "name")
	protected String name = "";
	@XmlElement(name = "bpmn:dataOutputAssociation")
	protected List<DataOutputAssociation> dataOutputAssociations = new ArrayList<>();
	@XmlElement(name = "bpmn:dataInputAssociation")
	protected List<DataInputAssociation> dataInputAssociations = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DataOutputAssociation> getDataOutputAssociations() {
		return dataOutputAssociations;
	}

	public List<DataInputAssociation> getDataInputAssociations() {
		return dataInputAssociations;
	}


	public void setIncoming(String incoming) {
		List<String> incomingList = new ArrayList<>();
		incomingList.add(incoming);
		this.setIncoming(incomingList);
	}

	public void setOutgoing(String outgoing) {
		List<String> outgoingList = new ArrayList<>();
		outgoingList.add(outgoing);
		this.setOutgoing(outgoingList);
	}
}
