package de.hpi.bpt.chimera.execution.event;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

public class BoundaryEventInstance extends AbstractEventInstance {
	private AbstractActivityInstance attachedToActivity;

	public BoundaryEventInstance(AbstractEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		setState(State.INIT);
	}

	@Override
	public BoundaryEvent getControlNode() {
		return (BoundaryEvent) super.getControlNode();
	}

	public AbstractActivityInstance getAttachedToActivity() {
		return attachedToActivity;
	}

	public void setAttachedToActivity(AbstractActivityInstance attachedToActivity) {
		this.attachedToActivity = attachedToActivity;
	}
}
