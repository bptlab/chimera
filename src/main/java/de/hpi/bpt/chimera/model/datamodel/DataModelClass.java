package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public abstract class DataModelClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	private String name;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<DataAttribute> attributes;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DataAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<DataAttribute> attributes) {
		this.attributes = attributes;
	}

}
