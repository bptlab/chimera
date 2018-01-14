package de.hpi.bpt.chimera;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;

public class CaseExecutionerTestHelper {
	/**
	 * Get the AbstractActivityInstance that is currently hold in a
	 * CaseExecutioner by its name. If the name does not occur return null.
	 * 
	 * @param caseExecutioner
	 * @param activityInstanceName
	 * @return AbstractActivityInstance or {@code null} if the name does not
	 *         occur.
	 */
	public static AbstractActivityInstance getActivityInstanceByName(CaseExecutioner caseExecutioner, String activityInstanceName) {
		List<AbstractActivityInstance> activityInstances = getActivityInstances(caseExecutioner);
		for (AbstractActivityInstance activityInstance : activityInstances) {
			if (activityInstance.getControlNode().getName().equals(activityInstanceName)) {
				return activityInstance;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return all AbstractActivityInstances in all FragmentInstances.
	 */
	public static List<AbstractActivityInstance> getActivityInstances(CaseExecutioner caseExecutioner) {
		List<AbstractActivityInstance> activityInstances = new ArrayList<>();
		for (FragmentInstance fragmentInstance : caseExecutioner.getCase().getFragmentInstances().values()) {
			for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstances().values()) {
				if (nodeInstance instanceof AbstractActivityInstance) {
					activityInstances.add((AbstractActivityInstance) nodeInstance);
				}
			}
		}
		return activityInstances;
	}
}
