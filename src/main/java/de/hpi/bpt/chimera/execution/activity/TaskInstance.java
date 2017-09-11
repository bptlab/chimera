package de.hpi.bpt.chimera.execution.activity;

import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.model.fragment.bpmn.Activity;

public class TaskInstance extends AbstractActivityInstance {

	public TaskInstance(Activity activity, FragmentInstance fragmentInstance) {
		super(activity, fragmentInstance);
	}

}
