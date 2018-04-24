package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class FragmentPreConditionExecutionTest {
	private final String filepath = "src/test/resources/execution/FragmentPreConditionExecutionTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	/**
	 * Activities that have automatic execution should not execute automatically
	 * if they are the first control node in the fragment and the fragment
	 * precondition is not fulfilled.
	 */
	@Test
	public void testAutoExecution() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "Fragment with Precondition");
		assertTrue("Fragment does not have a precondition", !fragmentInstance.getFragment().getFragmentPreCondition().isEmpty());

		// automatic task in one fragment
		AbstractActivityInstance automaticTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Automatic Task");
		assertNotNull(automaticTaskInstance);
		assertTrue(automaticTaskInstance.getControlNode().isAutomaticTask());
		assertTrue("is automatic task was not initialized correctly", automaticTaskInstance.hasAutomaticBegin());
		assertEquals("State of automatic task instance is not READY", State.READY, automaticTaskInstance.getState());
		
		// task also can not begin manually because of the unfulfilled
		// precondition
		automaticTaskInstance.begin();
		assertEquals("State of automatic task instance is not READY because task was able to begin although the precondition of the fragment was not fulfilled",
				State.READY, automaticTaskInstance.getState());
		
		// human task in other fragment that creates data object and enables
		// precondition of first fragment
		AbstractActivityInstance humanTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Create Dataobject");
		assertNotNull(humanTaskInstance);

		// *** TODO: put this in the test helper
		caseExecutioner.beginDataControlNodeInstance(humanTaskInstance, new ArrayList<>());
		DataStateCondition postCondition = humanTaskInstance.getControlNode().getPostCondition();
		Map<DataClass, ObjectLifecycleState> dataObjectToObjectLifecycleTransition = postCondition.getConditionSets().get(0).getDataClassToObjectLifecycleState();
		caseExecutioner.terminateDataControlNodeInstance(humanTaskInstance, dataObjectToObjectLifecycleTransition);
		assertEquals("State of human task instance is not TERMINATED", State.TERMINATED, humanTaskInstance.getState());
		// ***

		assertEquals("The data object was correctly created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		// automatic task should still be in state to ready to begin
		assertEquals("State of automatic task instance is not READY", State.READY, automaticTaskInstance.getState());
		assertTrue("is automatic task was not initialized correctly", automaticTaskInstance.hasAutomaticBegin());
		caseExecutioner.beginDataControlNodeInstance(automaticTaskInstance, new ArrayList<>());
		assertEquals("Automatic task instance was not successfully executed", State.TERMINATED, automaticTaskInstance.getState());
	}
}
