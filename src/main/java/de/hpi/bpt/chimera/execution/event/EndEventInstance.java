package de.hpi.bpt.chimera.execution.event;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;

public class EndEventInstance extends AbstractEventInstance {

	public EndEventInstance(EndEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		setState(State.INIT);
	}

	@Override
	public EndEvent getControlNode() {
		return (EndEvent) super.getControlNode();
	}
}
