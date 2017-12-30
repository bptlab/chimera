package de.hpi.bpt.chimera.execution;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.activity.EmailActivityInstance;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.activity.WebServiceTaskInstance;
import de.hpi.bpt.chimera.execution.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.event.AbstractIntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.event.EndEventInstance;
import de.hpi.bpt.chimera.execution.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.event.TimerEventInstance;
import de.hpi.bpt.chimera.execution.gateway.AbstractGatewayInstance;
import de.hpi.bpt.chimera.execution.gateway.EventBasedGatewayInstance;
import de.hpi.bpt.chimera.execution.gateway.ExclusiveGatewayInstance;
import de.hpi.bpt.chimera.execution.gateway.ParallelGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.WebServiceTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
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
			return createEventInstance((AbstractEvent) controlNode, fragmentInstance);
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
		} else if (event instanceof TimerEvent) {
			return new TimerEventInstance((TimerEvent) event, fragmentInstance);
		} else if (event instanceof IntermediateCatchEvent) {
			return new AbstractIntermediateCatchEventInstance((IntermediateCatchEvent) event, fragmentInstance);
		} else {
			throw new IllegalArgumentException(String.format("Unsupported type of Event: %s", event.getClass().getName()));
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
