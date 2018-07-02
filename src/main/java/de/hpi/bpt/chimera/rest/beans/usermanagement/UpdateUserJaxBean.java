package de.hpi.bpt.chimera.rest.beans.usermanagement;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class UpdateUserJaxBean {
	private String email;
	private String password;

	public UpdateUserJaxBean(String body) {
		try {
			JSONObject json = new JSONObject(body);
			setEmail(json.getString("email"));
			setPassword(json.getString("password"));
		} catch (Exception e) {
			throw e;
		}
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
