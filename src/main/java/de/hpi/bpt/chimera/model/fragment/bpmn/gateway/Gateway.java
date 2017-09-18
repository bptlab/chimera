package de.hpi.bpt.chimera.model.fragment.bpmn.gateway;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

public class Gateway extends AbstractControlNode {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
