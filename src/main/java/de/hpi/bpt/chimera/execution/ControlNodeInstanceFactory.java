package de.hpi.bpt.chimera.execution;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.event.EndEventInstance;
import de.hpi.bpt.chimera.execution.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.gateway.ParallelGatewayInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;

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
		Class<? extends AbstractControlNode> clazz = controlNode.getClass();
		if (clazz.equals(StartEvent.class)) {
			return new StartEventInstance((StartEvent) controlNode, fragmentInstance);
		} else if (clazz.equals(EndEvent.class)) {
			return new EndEventInstance((EndEvent) controlNode, fragmentInstance);
		} else if (clazz.equals(HumanTask.class)) {
			return new HumanTaskInstance((HumanTask) controlNode, fragmentInstance);
		} else if (clazz.equals(ParallelGateway.class)) {
			return new ParallelGatewayInstance((ParallelGateway) controlNode, fragmentInstance);
		} else {
			log.error(String.format("Illegal type of ControlNode: %s", clazz.getName()));
			return null;
		}
	}
}
