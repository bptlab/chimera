package de.hpi.bpt.chimera.rest.beans.exception;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DangerExceptionJaxBean {
	private String type = "danger";
	private String text;

	public DangerExceptionJaxBean(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
