package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.*;

/**
 *
 */
@XmlRootElement(name = "bpmn:dataInputAssociation")
@XmlAccessorType(XmlAccessType.NONE)
public class DataInputAssociation extends Edge {
	@XmlAttribute
	private String id;
	@XmlElement(name = "bpmn:sourceRef")

	private String sourceDataObjectRef;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceDataObjectRef() {
		return sourceDataObjectRef;
	}

	public void setSourceDataObjectRef(String sourceDataObjectRef) {
		this.sourceDataObjectRef = sourceDataObjectRef;
	}

}
