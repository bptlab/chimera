package de.hpi.bpt.chimera.execution.event;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.State;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;

public class BoundaryEventInstance extends AbstractEventInstance {
	private AbstractActivityInstance attachedToActivity;

	public BoundaryEventInstance(BoundaryEvent event, FragmentInstance fragmentInstance) {
		super(event, fragmentInstance);
		setState(State.INIT);
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
