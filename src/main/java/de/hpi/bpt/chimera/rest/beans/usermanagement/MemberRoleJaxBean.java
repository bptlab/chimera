package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class MemberRoleJaxBean {
	private String name;

	public MemberRoleJaxBean(String body) {
		try {
			JSONObject json = new JSONObject(body);
			setName(json.getString("name"));
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
}
