package de.hpi.bpt.chimera;

import java.util.Collection;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;

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
		Collection<AbstractActivityInstance> activityInstances = caseExecutioner.getActivityInstances();
		for (AbstractActivityInstance activityInstance : activityInstances) {
			if (activityInstance.getControlNode().getName().equals(activityInstanceName)) {
				return activityInstance;
			}
		}
		return null;
	}
}
