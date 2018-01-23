package de.hpi.bpt.chimera.execution.controlnodes.event;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

@Entity
public class BoundaryEventInstance extends AbstractEventInstance {
	@OneToOne
	private AbstractActivityInstance attachedToActivity;

	/**
	 * for JPA only
	 */
	public BoundaryEventInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public BoundaryEventInstance(BoundaryEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
	}

	@Override
	public void terminate() {
		attachedToActivity.cancel();
		super.terminate();
	}

	public AbstractActivityInstance getAttachedToActivity() {
		return attachedToActivity;
	}

	public void setAttachedToActivity(AbstractActivityInstance attachedToActivity) {
		this.attachedToActivity = attachedToActivity;
	}

	@Override
	public BoundaryEvent getControlNode() {
		return (BoundaryEvent) super.getControlNode();
	}
}
