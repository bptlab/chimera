package de.hpi.bpt.chimera.execution.event;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.EndEvent;

public class EndEventInstance extends AbstractEventInstance {

	public EndEventInstance(EndEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

}
