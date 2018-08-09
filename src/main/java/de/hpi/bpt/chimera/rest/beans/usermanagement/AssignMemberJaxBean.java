package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class AssignMemberJaxBean {
	private String id;

	public AssignMemberJaxBean(String body) {
		try {
			setId(new JSONObject(body).getString("id"));
		} catch (Exception e) {
			throw e;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
