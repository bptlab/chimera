package de.hpi.bpt.chimera.model.datamodel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.hpi.bpt.chimera.model.Listable;
import de.hpi.bpt.chimera.model.Nameable;

@Entity
public class DataAttribute implements Listable, Nameable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dbId;
	private String name;
	private String type;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
