package de.hpi.bpt.chimera.execution.controlnodes.activity;

import javax.persistence.Entity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;

/**
 * Handles the Behavior of a Human Activity.
 *
 */
@Entity
public class HumanTaskInstance extends AbstractActivityInstance {


	/**
	 * for JPA only
	 */
	public HumanTaskInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public HumanTaskInstance(HumanTask humanActivity, FragmentInstance fragmentInstance) {
		super(humanActivity, fragmentInstance);
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
