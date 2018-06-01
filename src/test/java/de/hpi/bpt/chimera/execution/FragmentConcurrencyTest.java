package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
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
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with Activity");
		AbstractActivityInstance activityInstance = CaseExecutionerTestHelper.getActivityInstanceByName(fragmentInstance, "Task");
		assertEquals(FragmentState.ENABLED, fragmentInstance.getState());

		caseExecutioner.beginDataControlNodeInstance(activityInstance, new ArrayList<DataObject>());
		assertEquals("Fragment does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		assertEquals("Exactly one Fragment Instance has to be created.", fragmentAmount + 1, caseExecutioner.getCase().getFragmentInstances().size());
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
		assertEquals(State.TERMINATED, eventInstance.getState());
		assertEquals("Fragment does not changed in State Active.", FragmentState.ACTIVE, fragmentInstance.getState());
		assertEquals("Exactly one Fragment Instance has to be created.", fragmentAmount + 1, caseExecutioner.getCase().getFragmentInstances().size());
	}

	@Test
	public void testSendEvent() {

	}
}
