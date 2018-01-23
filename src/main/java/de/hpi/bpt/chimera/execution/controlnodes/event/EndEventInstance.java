package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;

@Entity
public class EndEventInstance extends AbstractEventInstance {


	/**
	 * for JPA only
	 */
	public EndEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public EndEventInstance(EndEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

	@Override
	public EndEvent getControlNode() {
		return (EndEvent) super.getControlNode();
	}
}
