package de.hpi.bpt.chimera.execution.gateway;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;

public class ParallelGatewayInstance extends AbstractGatewayInstance {
	private ParallelGateway parallelGateway;

	/**
	 * Create a new ParallelGatewayInstance
	 * 
	 * @param parallelGateway
	 * @param fragmentInstance
	 */
	public ParallelGatewayInstance(ParallelGateway parallelGateway, FragmentInstance fragmentInstance) {
		super(parallelGateway, fragmentInstance);
		this.setParallelGateway(parallelGateway);
		this.setState(State.INIT);
	}


	@Override
	public void enableControlFlow() {
		if (getFragmentInstance().areInstantiated(parallelGateway.getIncomingControlNodes())) {
			setState(State.READY);
			begin();
		}
	}


	@Override
	public void begin() {
		setState(State.EXECUTING);
		terminate();
	}


	@Override
	public void terminate() {
		getFragmentInstance().createFollowing(parallelGateway);
		setState(State.TERMINATED);
	}


	@Override
	public void skip() {
		setState(State.SKIPPED);
	}


	// GETTER & SETTER
	public ParallelGateway getParallelGateway() {
		return parallelGateway;
	}

	public void setParallelGateway(ParallelGateway paralelGateway) {
		this.parallelGateway = paralelGateway;
	}
}
