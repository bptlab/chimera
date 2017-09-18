package de.hpi.bpt.chimera.execution.activity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanActivity;

/**
 * Handles the Behavior of a Human Activity.
 *
 */
public class HumanActivityInstance extends AbstractActivityInstance {

	public HumanActivityInstance(HumanActivity humanActivity, FragmentInstance fragmentInstance) {
		super(humanActivity, fragmentInstance);
		setAutomaticTask(false);
	}
}
