package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

public class FragmentConcurrencyTest extends Unicorn {
	private final String filepath = "src/test/resources/execution/FragmentConcurrencyTest.json";

	private CaseExecutioner caseExecutioner;
	private int fragmentAmount;

	@Before
	public void setUpTest() {
		super.setUpTest();
		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		fragmentAmount = cm.getFragments().size();
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	/**
	 * Test fundamental concurrency behavior of fragment instances with a
	 * non-automatic Activity.
	 */
	@Test
	public void testActivity() {
		String fragmentName = "Fragment with Activity";
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, fragmentName);
		AbstractActivityInstance activityInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, "Task");
		assertEquals(FragmentState.ENABLED, fragmentInstance.getState());

		caseExecutioner.beginDataControlNodeInstance(activityInstance, new ArrayList<DataObject>());
		assertEquals("Fragment Instance does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		
		List<FragmentInstance> fragmentInstances = CaseExecutionerTestHelper.getFragmentInstancesByName(caseExecutioner, fragmentName);
		assertEquals(2, fragmentInstances.size());
		
		FragmentInstance newFragmentInstance = fragmentInstances.stream().filter(f -> !f.getId().equals(fragmentInstance.getId())).findFirst().get();
		assertEquals("New Fragment Instance is not enabled.", FragmentState.ENABLED, newFragmentInstance.getState());
	}

	@Test
	public void testAutomaticActivity() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with automatic Activity");
		AbstractActivityInstance activityInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, "Automatic Task");
		assertEquals(FragmentState.ENABLED, fragmentInstance.getState());

		caseExecutioner.beginDataControlNodeInstance(activityInstance, new ArrayList<DataObject>());
		assertEquals("Fragment does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		assertEquals("Exactly one Fragment Instance has to be created.", fragmentAmount + 1, caseExecutioner.getCase().getFragmentInstances().size());
	}

	@Test
	public void testReceiveEvent() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with Catch Event");
		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(fragmentInstance, "Catch Event");
		assertEquals(FragmentState.ENABLED, fragmentInstance.getState());

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), "");
		assertEquals("Fragment does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		assertEquals("Exactly one Fragment Instance has to be created.", fragmentAmount + 1, caseExecutioner.getCase().getFragmentInstances().size());
	}

	@Test
	public void testThrowEvent() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with Throw Event");
		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(fragmentInstance, "Throw Event");
		assertEquals(FragmentState.ENABLED, fragmentInstance.getState());
		
		caseExecutioner.beginDataControlNodeInstance(eventInstance, new ArrayList<DataObject>());
		assertEquals("Fragment does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		assertEquals("Exactly one Fragment Instance has to be created.", fragmentAmount + 1, caseExecutioner.getCase().getFragmentInstances().size());
	}

	@Test
	public void testTestFiniteFragmentInstanceAmount() {
//		for (int i = 0; i < 500; i++) {
//			Optional<MessageReceiveEventBehavior> receiveBehavior = caseExecutioner.getRegisteredEventBehaviors().stream().findFirst();
//			if (receiveBehavior.isPresent()) {
//				AbstractEventInstance eventInstance = receiveBehavior.get().getEventInstance();
//				CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), "");
//			}
//		}
//
//		List<FragmentInstance> fragmentInstance = caseExecutioner.getCase().getFragmentInstances().values()
//													.stream().filter(f -> f.getFragment().getName().equals("Fragment with Catch Event"))
//													.collect(Collectors.toList());
//		assertEquals("Not the expcted amount of Fragment Instances were created", 100, fragmentInstance.size());
	}
}
