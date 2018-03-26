package de.hpi.bpt.chimera.model.condition;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

@Entity
public class DataAttributeJsonPath {
	@Id
	@GeneratedValue
	private int dbId;

	@OneToOne(cascade = CascadeType.ALL)
	private DataAttribute dataAttribute;
	private String jsonPath;

	public DataAttributeJsonPath() {
		// used by jpa
	}

	public DataAttributeJsonPath(DataAttribute dataAttribute, String jsonPath) {
		this.dataAttribute = dataAttribute;
		this.jsonPath = jsonPath;
	}

	public DataAttribute getDataAttribute() {
		return dataAttribute;
	}

	public void setDataAttribute(DataAttribute dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
}
