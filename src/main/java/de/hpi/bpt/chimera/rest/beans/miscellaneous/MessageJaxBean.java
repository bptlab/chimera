package de.hpi.bpt.chimera.rest.beans.miscellaneous;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageJaxBean {
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
