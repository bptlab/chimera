package de.hpi.bpt.chimera.execution.controlnodes;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.EmailActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.WebServiceTaskInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.EndEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateThrowEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.EventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageSendEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.TimerEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.AbstractGatewayInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.ExclusiveGatewayInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.ParallelGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.WebServiceTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.AbstractGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;

public class ControlNodeInstanceFactory {
	private static Logger log = Logger.getLogger(ControlNodeInstanceFactory.class);

	private ControlNodeInstanceFactory() {
	}

	/**
	 * Create a ControlNodeInstance of specific ControlNode. Therefore check
	 * Class of ControlNode.
	 * 
	 * @param controlNode
	 * @param caseExecutioner
	 * @param fragmentInstance
	 * @return ControlNodeInstance
	 */
	public static ControlNodeInstance createControlNodeInstance(AbstractControlNode controlNode, FragmentInstance fragmentInstance) {
		if (controlNode instanceof AbstractActivity) {
			return createActivityInstance((AbstractActivity) controlNode, fragmentInstance);
		} else if (controlNode instanceof AbstractEvent) {
			AbstractEventInstance eventInstance = createEventInstance((AbstractEvent) controlNode, fragmentInstance);
			setEventBehavior(eventInstance);
			return eventInstance;
		} else if (controlNode instanceof AbstractGateway) {
			return createGatewayInstance((AbstractGateway) controlNode, fragmentInstance);
		} else {
			throw new IllegalArgumentException(String.format("Illegal type of ControlNode: %s", controlNode.getClass().getName()));
		}
	}

	/**
	 * Create a specific ActivityInstance.
	 * 
	 * @param activity
	 * @param fragmentInstance
	 * @return AbstractActivityInstance
	 */
	private static AbstractActivityInstance createActivityInstance(AbstractActivity activity, FragmentInstance fragmentInstance) {
		if (activity instanceof HumanTask) {
			return new HumanTaskInstance((HumanTask) activity, fragmentInstance);
		} else if (activity instanceof EmailActivity) {
			return new EmailActivityInstance((EmailActivity) activity, fragmentInstance);
		} else if (activity instanceof WebServiceTask) {
			return new WebServiceTaskInstance((WebServiceTask) activity, fragmentInstance);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported type of Activity: %s", activity.getClass().getName()));
		}
	}

	/**
	 * Create a specific EventInstance.
	 * 
	 * @param event
	 * @param fragmentInstance
	 * @return AbstractEventInstance
	 */
	private static AbstractEventInstance createEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		if (event instanceof StartEvent) {
			return new StartEventInstance((StartEvent) event, fragmentInstance);
		} else if (event instanceof EndEvent) {
			return new EndEventInstance((EndEvent) event, fragmentInstance);
		} else if (event instanceof BoundaryEvent) {
			return new BoundaryEventInstance((BoundaryEvent) event, fragmentInstance);
		} else if (event instanceof IntermediateCatchEvent) {
			return new IntermediateCatchEventInstance((IntermediateCatchEvent) event, fragmentInstance);
		} else if (event instanceof IntermediateThrowEvent) {
			return new IntermediateThrowEventInstance((IntermediateThrowEvent) event, fragmentInstance);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported type of Event: %s", event.getClass().getName()));
		}

	}

	private static void setEventBehavior(AbstractEventInstance eventInstance) {

		switch (eventInstance.getControlNode().getSpecialBehavior()) {
		case MESSAGE_SEND:
			eventInstance.setBehavior(new MessageSendEventBehavior(eventInstance));
			break;
		case MESSAGE_RECEIVE:
			eventInstance.setBehavior(new MessageReceiveEventBehavior(eventInstance));
			break;
		case TIMER:
			eventInstance.setBehavior(new TimerEventBehavior(eventInstance));
			break;
		case NONE:
			eventInstance.setBehavior(new EventBehavior(eventInstance));
			break;
		default:
			throw new IllegalArgumentException(String.format("Unsupported type of SpecialEventBehavior: %s", eventInstance.getControlNode().getSpecialBehavior().toString()));
		}
	}

	/**
	 * Create a specific GatewayInstance.
	 * 
	 * @param gateway
	 * @param fragmentInstance
	 * @return AbstractGatewayInstance
	 */
	private static AbstractGatewayInstance createGatewayInstance(AbstractGateway gateway, FragmentInstance fragmentInstance) {
		if (gateway instanceof ParallelGateway) {
			return new ParallelGatewayInstance((ParallelGateway) gateway, fragmentInstance);
		} else if (gateway instanceof ExclusiveGateway) {
			return new ExclusiveGatewayInstance((ExclusiveGateway) gateway, fragmentInstance);
		} else if (gateway instanceof EventBasedGateway) {
			return new EventBasedGatewayInstance((EventBasedGateway) gateway, fragmentInstance);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported type of Gateway: %s", gateway.getClass().getName()));
		}
	}


}
