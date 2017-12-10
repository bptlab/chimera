package de.hpi.bpt.chimera.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JsonPath {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;

	private String jsonPathString;

	private JsonPath() {
	}

	public JsonPath(String jsonPathString) {
		this.jsonPathString = jsonPathString;
	}

	public String getJsonPathString() {
		return jsonPathString;
	}

	public void setJsonPathString(String jsonPathString) {
		this.jsonPathString = jsonPathString;
	}
}
