package de.hpi.bpt.chimera.execution.gateway;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;

public class ParallelGatewayInstance extends AbstractGatewayInstance {

	public ParallelGatewayInstance(ParallelGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
	}

	@Override
	public void enableControlFlow() {
		if (getState().equals(State.INIT))
			setState(State.CONTROLFLOW_ENABLED);
		begin();
	}

	/**
	 * Check that all incoming Instances of ControlNodes are terminated. Then
	 * terminate this Parallel Gateway.
	 */
	@Override
	public void begin() {
		for (AbstractControlNode incommingControlNode : getControlNode().getIncomingControlNodes()) {
			if (!getFragmentInstance().isInstantiated(incommingControlNode))
				return;
		}
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void skip() {
		// TODO Auto-generated method stub

	}
}
