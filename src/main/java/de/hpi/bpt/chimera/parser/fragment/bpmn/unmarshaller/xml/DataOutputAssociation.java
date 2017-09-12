package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataOutputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataOutputAssociation extends Edge {
	@XmlAttribute
	private String id;
	@XmlElement(name = "bpmn:targetRef")
	private String targetDataObjectRef;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetDataObjectRef() {
		return targetDataObjectRef;
	}

	public void setTargetDataObjectRef(String targetDataObjectRef) {
		this.targetDataObjectRef = targetDataObjectRef;
	}

}
