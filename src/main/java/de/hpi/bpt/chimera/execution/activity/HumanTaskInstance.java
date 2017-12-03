package de.hpi.bpt.chimera.execution.activity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;

/**
 * Handles the Behavior of a Human Activity.
 *
 */
public class HumanTaskInstance extends AbstractActivityInstance {

	public HumanTaskInstance(HumanTask humanActivity, FragmentInstance fragmentInstance) {
		super(humanActivity, fragmentInstance);
		setState(State.INIT);
		setAutomaticTask(false);
	}

	@Override
	public HumanTask getControlNode() {
		return (HumanTask) super.getControlNode();
	}

	@Override
	public void execute() {
		// no execution for HumanTasks
	}
}
