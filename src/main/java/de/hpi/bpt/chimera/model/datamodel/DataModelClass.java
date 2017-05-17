package de.hpi.bpt.chimera.model.datamodel;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import de.hpi.bpt.chimera.model.Nameable;

@Entity
public abstract class DataModelClass implements Nameable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	private String name;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<DataAttribute> attributes;

	@Override
	public String getName() {
		return name;
	}

	@Override
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
