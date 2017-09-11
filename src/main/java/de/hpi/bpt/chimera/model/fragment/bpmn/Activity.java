package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Activity extends AbstractDataControlNode {
	// TODO This class only exists to specify control nodes as Tasks.
	// Maybe leave it out?
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
