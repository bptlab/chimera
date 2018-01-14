package de.hpi.bpt.chimera;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;

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

	public static AbstractEventInstance getEventInstanceByName(FragmentInstance fragmentInstance, String eventInstanceName) {
		for (AbstractEventInstance eventInstance : getEventInstances(fragmentInstance)) {
			if (eventInstance.getControlNode().getName().equals(eventInstanceName)) {
				return eventInstance;
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
			for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstanceIdToInstance().values()) {
				if (nodeInstance instanceof AbstractActivityInstance) {
					activityInstances.add((AbstractActivityInstance) nodeInstance);
				}
			}
		}
		return activityInstances;
	}

	public static List<AbstractEventInstance> getEventInstances(FragmentInstance fragmentInstance) {
		List<AbstractEventInstance> eventInstances = new ArrayList<>();
		for (ControlNodeInstance nodeInstance : fragmentInstance.getControlNodeInstanceIdToInstance().values()) {
			if (nodeInstance instanceof AbstractEventInstance) {
				eventInstances.add((AbstractEventInstance) nodeInstance);
			}
		}
		return eventInstances;
	}

	public static FragmentInstance getFragmentInstanceByName(CaseExecutioner caseExecutioner, String name) {
		FragmentInstance searchedFragmentInstance = null;
		for (FragmentInstance fragmentInstance : caseExecutioner.getCase().getFragmentInstances().values()) {
			if (fragmentInstance.getFragment().getName().equals(name)) {
				if (searchedFragmentInstance != null) {
					throw new IllegalArgumentException(String.format("more than fragment exists with this name: %s", name));
				}
				searchedFragmentInstance = fragmentInstance;
			}
		}
		return searchedFragmentInstance;
	}
}
