package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;

@Entity
public class EventBasedGatewayInstance extends AbstractGatewayInstance {

	private static final Logger logger = Logger.getLogger(EventBasedGatewayInstance.class);

	@OneToMany(cascade = CascadeType.ALL)
	private List<AbstractEventInstance> followingEventInstances = new ArrayList<>();


	/**
	 * for JPA only
	 */
	public EventBasedGatewayInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public EventBasedGatewayInstance(EventBasedGateway gateway, FragmentInstance fragmentInstance) {
		super(gateway, fragmentInstance);
	}

	@Override
	public void enableControlFlow() {
		logger.info("enabled ControlFlow of an EventBasedGatewayInstance.");
		this.begin();

	}

	@Override
	public void begin() {
		this.setState(State.EXECUTING);
		this.terminate();
	}

	@Override
	public void terminate() {
		followingEventInstances = this.getFragmentInstance().createFollowing(this.getControlNode()).stream().map(x -> (AbstractEventInstance) x).collect(Collectors.toList());
		followingEventInstances.stream().forEach(x -> x.setPreviousEventBasedGatewayInstance(this));
		this.setState(State.TERMINATED);
	}

	@Override
	public void skip() {
		this.setState(State.SKIPPED);
	}

	public void skipAlternativeGateways(AbstractEventInstance triggeredEvent) {
		logger.info("EventbasedGateway is skipping Events");
		for (AbstractEventInstance eventToSkip : followingEventInstances) {
			if (!eventToSkip.equals(triggeredEvent)) {
				eventToSkip.skip();
			}
		}
	}

}
