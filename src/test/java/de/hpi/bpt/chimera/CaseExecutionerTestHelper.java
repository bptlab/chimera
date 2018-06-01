package de.hpi.bpt.chimera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.ControlNodeInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

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
	public static AbstractActivityInstance getActivityInstanceByName(CaseExecutioner caseExecutioner, String name) {
		List<AbstractActivityInstance> possibleActivityInstances = caseExecutioner.getCase().getFragmentInstances().values().stream()
																		.map(f -> getActivityInstanceByName(f, name))
																		.collect(Collectors.toList());
		assertEquals(String.format("The name %s is not unique in the case", name), possibleActivityInstances.size(), 1);
		return possibleActivityInstances.get(0);
	}

	public static AbstractActivityInstance getActivityInstanceByName(FragmentInstance fragmentInstance, String name) {
		List<AbstractActivityInstance> possibleActivityInstances = fragmentInstance.getActivActivityInstances().stream()
																		.filter(a -> a.getControlNode().getName().equals(name))
																		.collect(Collectors.toList());
		assertEquals(String.format("The name %s is not unique in the fragment", name), possibleActivityInstances.size(), 1);
		return possibleActivityInstances.get(0);
	}

	public static AbstractEventInstance getEventInstanceByName(FragmentInstance fragmentInstance, String eventInstanceName) {
		for (AbstractEventInstance eventInstance : getEventInstances(fragmentInstance)) {
			if (eventInstance.getControlNode().getName().equals(eventInstanceName)) {
				return eventInstance;
			}
		}
		return null;
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
	
	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, String name) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, name);
		assertNotNull(humanTaskInstance);

		DataStateCondition postCondition = humanTaskInstance.getControlNode().getPostCondition();
		Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = postCondition.getConditionSets().get(0).getDataClassToObjectLifecycleState();
		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, new ArrayList<>());
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance, dataObjectToObjectLifecycleTransition);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		return humanTaskInstance;
	}

	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, String name, ArrayList<DataObject> inputDataObjects) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, name);
		assertNotNull(humanTaskInstance);

		DataStateCondition postCondition = humanTaskInstance.getControlNode().getPostCondition();
		Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = postCondition.getConditionSets().get(0).getDataClassToObjectLifecycleState();
		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, inputDataObjects);
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance, dataObjectToObjectLifecycleTransition);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		return humanTaskInstance;
	}

	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, String name, List<DataObject> inputDataObjects, Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, name);
		assertNotNull(humanTaskInstance);

		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, inputDataObjects);
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance, dataObjectToObjectLifecycleTransition);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		return humanTaskInstance;
	}

	public static void triggerEvent(CaseExecutioner caseExecutioner, AbstractEventInstance eventInstance, WebTarget base, String body) {
		String cmId = caseExecutioner.getCaseModel().getId();
		String caseId = caseExecutioner.getCase().getId();
		String eventInstanceId = eventInstance.getId();
		String route = String.format("scenario/%s/instance/%s/events/%s", cmId, caseId, eventInstanceId);

		base.path(route).request().post(Entity.json(body));
	}
}
