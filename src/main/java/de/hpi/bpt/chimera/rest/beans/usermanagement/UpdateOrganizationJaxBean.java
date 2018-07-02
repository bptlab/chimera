package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class UpdateOrganizationJaxBean {
	private String name;
	private String description;

	public UpdateOrganizationJaxBean(String body) {
		try {
			JSONObject json = new JSONObject(body);
			setName(json.getString("name"));
			setDescription(json.getString("description"));
		} catch (Exception e) {
			throw e;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
