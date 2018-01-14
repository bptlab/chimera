package de.hpi.bpt.chimera.execution.controlnodes.event;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;

public class StartEventInstance extends AbstractEventInstance {

	public StartEventInstance(StartEvent startEvent, FragmentInstance fragmentInstance) {
		super(startEvent, fragmentInstance);
	}

	@Override
	public StartEvent getControlNode() {
		return (StartEvent) super.getControlNode();
	}
}
