package de.hpi.bpt.chimera.execution.gateway;

import de.hpi.bpt.chimera.execution.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.Gateway;

public abstract class AbstractGatewayInstance extends ControlNodeInstance {

	public AbstractGatewayInstance(Gateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
	}

	// GETTER & SETTER
	@Override
	public Gateway getControlNode() {
		return (Gateway) super.getControlNode();
	}
}
