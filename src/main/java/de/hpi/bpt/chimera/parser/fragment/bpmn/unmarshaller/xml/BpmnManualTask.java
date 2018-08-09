package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bpmn:manualTask")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmnManualTask extends AbstractDataControlNode {
	@XmlAttribute(name = "griffin:taskrole")
	private String role = "member";

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
