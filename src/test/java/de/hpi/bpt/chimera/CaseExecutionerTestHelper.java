package de.hpi.bpt.chimera;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class CaseExecutionerTestHelper {
	public static AbstractActivityInstance getActivityInstanceByName(FragmentInstance fragmentInstance, String name) {
		List<AbstractActivityInstance> possibleActivityInstances = fragmentInstance.getActivityInstances().stream()
																		.filter(a -> a.getControlNode().getName().equals(name))
																		.collect(Collectors.toList());
		if (possibleActivityInstances.size() == 0) {
			throw new IllegalArgumentException(String.format("The name %s does not exist in the fragment", name));
		}
		// TODO: think about how to handle concurrency
		if (possibleActivityInstances.size() == 1) {
			return possibleActivityInstances.get(0);
		}
		throw new IllegalArgumentException(String.format("The name %s is not unique in the fragment", name));
	}

	public static AbstractEventInstance getEventInstanceByName(FragmentInstance fragmentInstance, String name) {
		List<AbstractEventInstance> possibleEventInstances = fragmentInstance.getControlNodeInstances().stream()
																.filter(c -> c instanceof AbstractEventInstance && c.getControlNode().getName().equals(name))
																.map(AbstractEventInstance.class::cast)
																.collect(Collectors.toList());

		if (possibleEventInstances.size() == 1) {
			return possibleEventInstances.get(0);
		}
		throw new IllegalArgumentException(String.format("The name %s is not unique in the fragment", name));
	}


	public static List<FragmentInstance> getFragmentInstancesByName(CaseExecutioner caseExecutioner, String name) {
		return caseExecutioner.getCase().getFragmentInstances().values().stream()
				.filter(f -> f.getFragment().getName().equals(name))
				.collect(Collectors.toList());
	}

	public static FragmentInstance getFragmentInstanceByName(CaseExecutioner caseExecutioner, String name) {
		List<FragmentInstance> possibleFragmentInstances = getFragmentInstancesByName(caseExecutioner, name);
		if (possibleFragmentInstances.size() == 1) {
			return possibleFragmentInstances.get(0);
		}
		throw new IllegalArgumentException(String.format("The name %s is not a unique fragment name in the case", name));
	}
	
	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, FragmentInstance fragmentInstance, String name) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, name);
		assertNotNull(humanTaskInstance);

		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, new ArrayList<>());
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		return humanTaskInstance;
	}

	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, FragmentInstance fragmentInstance, String name, ArrayList<DataObject> inputDataObjects) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, name);
		assertNotNull(humanTaskInstance);

		DataStateCondition postCondition = humanTaskInstance.getControlNode().getPostCondition();
		Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = postCondition.getConditionSets().get(0).getDataClassToObjectLifecycleState();
		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, inputDataObjects);
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance, dataObjectToObjectLifecycleTransition);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		return humanTaskInstance;
	}

	public static AbstractActivityInstance executeHumanTaskInstance(CaseExecutioner caseExecutioner, FragmentInstance fragmentInstance, String name, List<DataObject> inputDataObjects, Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition) {
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, name);
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

		Response response = base.path(route).request().post(Entity.json(body));
		if (response.getStatus() != 200) {
			throw new IllegalArgumentException(String.format("%s : %s", response.getStatus(), response.readEntity(String.class)));
		}
	}
}
