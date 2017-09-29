package de.hpi.bpt.chimera.model.fragment.bpmn.gateway;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

@Entity
public class Gateway extends AbstractControlNode {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
