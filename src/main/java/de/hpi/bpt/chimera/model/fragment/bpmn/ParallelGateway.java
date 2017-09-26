package de.hpi.bpt.chimera.model.fragment.bpmn;

import javax.persistence.Entity;

@Entity
public class ParallelGateway extends AbstractDataControlNode {

	private String name;


	// GETTER & SETTER
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
