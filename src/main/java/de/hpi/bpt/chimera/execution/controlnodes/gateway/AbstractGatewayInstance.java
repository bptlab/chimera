package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

@Entity
public abstract class AbstractGatewayInstance extends ControlNodeInstance {


	/**
	 * for JPA only
	 */
	public AbstractGatewayInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public AbstractGatewayInstance(AbstractGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
	}

	// GETTER & SETTER
	@Override
	public AbstractGateway getControlNode() {
		return (AbstractGateway) super.getControlNode();
	}
}
