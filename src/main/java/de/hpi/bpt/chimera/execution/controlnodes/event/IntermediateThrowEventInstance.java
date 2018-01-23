package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;

@Entity
public class IntermediateThrowEventInstance extends AbstractEventInstance {


	/**
	 * for JPA only
	 */
	public IntermediateThrowEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public IntermediateThrowEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

	@Override
	public IntermediateThrowEvent getControlNode() {
		return (IntermediateThrowEvent) super.getControlNode();
	}
}
