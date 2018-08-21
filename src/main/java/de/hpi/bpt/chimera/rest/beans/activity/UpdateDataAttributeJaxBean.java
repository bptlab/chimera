package de.hpi.bpt.chimera.rest.beans.activity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UpdateDataAttributeJaxBean {
	private String attribute;
	private String value;

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
