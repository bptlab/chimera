package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;

@Entity
public class StartEventInstance extends AbstractEventInstance {


	/**
	 * for JPA only
	 */
	public StartEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public StartEventInstance(StartEvent startEvent, FragmentInstance fragmentInstance) {
		super(startEvent, fragmentInstance);
	}

	@Override
	public StartEvent getControlNode() {
		return (StartEvent) super.getControlNode();
	}
}
