package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;

public class FragmentPreConditionExecutionTest {
	private final String filepath = "src/test/resources/execution/FragmentPreConditionExecutionTest.json";

	private CaseExecutioner caseExecutioner;
	private FragmentInstance fragmentInstance;
	private AbstractActivityInstance automaticTaskInstance;

	@Before
	public void setup() {
		CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
		testInitialization();
	}

	private void testInitialization() {
		fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with Precondition");
		assertNotNull(fragmentInstance);
		assertTrue("Fragment does not have a precondition", !fragmentInstance.getFragment().getFragmentPreCondition().isEmpty());
		assertEquals("Fragment with disabled precondition is in wrong state", FragmentState.CREATED, fragmentInstance.getState());
		assertTrue("Control nodes were instantiated", fragmentInstance.getControlNodeInstanceIdToInstance().isEmpty());
	}

	/**
	 * Activities that have automatic execution should not execute automatically
	 * if they are the first control node in the fragment and the fragment
	 * precondition is not fulfilled.
	 */
	@Test
	public void testAutoExecution() {
		// human task in other fragment that creates data object and enables
		// precondition of first fragment
		List<DataObject> dataObjects = CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "Create Dataobject").getOutputDataObjects();
		assertEquals("Incorrect number of created dataobjects", 1, dataObjects.size());
		DataObject firstDataObject = dataObjects.get(0);
		testEnabling();
		// task also can not begin manually because of the unfulfilled
		// precondition
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "Change Dataobject State", new ArrayList<>(Arrays.asList(firstDataObject)));
		testDisabling();

		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "Create new Dataobject");
		testReenabling();

		testExecution();
	}

	private void testEnabling() {
		assertEquals("Fragment with enabled precondition is in wrong state", FragmentState.ENABLED, fragmentInstance.getState());

		// automatic task in one fragment
		automaticTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Automatic Task");
		assertNotNull(automaticTaskInstance);
		assertTrue(automaticTaskInstance.getControlNode().isAutomaticTask());
		assertTrue("is automatic task was not initialized correctly", automaticTaskInstance.hasAutomaticBegin());
		assertEquals("State of automatic task instance is wrong", State.READY, automaticTaskInstance.getState());
	}

	private void testDisabling() {
		assertEquals("Fragment with disabled precondition is in wrong state", FragmentState.DISABLED, fragmentInstance.getState());
		assertEquals("State of automatic task instance is wrong", State.CONTROLFLOW_ENABLED, automaticTaskInstance.getState());

		automaticTaskInstance.begin();
		assertEquals("Automatic task should not begun", State.CONTROLFLOW_ENABLED, automaticTaskInstance.getState());
	}

	private void testReenabling() {
		testEnabling();
	}

	private void testExecution() {
		// automatic task should still be in state to ready to begin
		caseExecutioner.beginDataControlNodeInstance(automaticTaskInstance, new ArrayList<>());
		assertEquals("Automatic task instance was not successfully executed", State.TERMINATED, automaticTaskInstance.getState());
		assertEquals("Fragment instance is in wrong state", FragmentState.STARTED, fragmentInstance.getState());
	}
}
