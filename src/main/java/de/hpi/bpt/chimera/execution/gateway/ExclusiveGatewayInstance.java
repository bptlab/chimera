package de.hpi.bpt.chimera.execution.gateway;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;

public class ExclusiveGatewayInstance extends AbstractGatewayInstance {

	public ExclusiveGatewayInstance(ExclusiveGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
		setState(State.INIT);
	}

	@Override
	public void enableControlFlow() {
		begin();
	}

	@Override
	public void begin() {
		setState(State.EXECUTING);
		getFragmentInstance().createFollowing(getControlNode());
	}

	@Override
	public void terminate() {
		if (!getState().equals(State.EXECUTING))
			return;
		// Check exclusive behaviour
		setState(State.TERMINATED);
	}

	@Override
	public void skip() {
		// TODO Auto-generated method stub

	}

	@Override
	public ExclusiveGateway getControlNode() {
		return (ExclusiveGateway) super.getControlNode();
	}
}
