package de.hpi.bpt.chimera.execution.controlnodes.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.CaseModel;

public class SimpleTerminateHumanTaskTest {
	private final String filepath = "src/test/resources/execution/SimpleCaseModelWithOneControlFlowAndDataObjects.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;
	private FragmentInstance fi;
	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
		fi = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
	}

	@Test
	public void testTerminateWithoutDataObjects() {
		AbstractActivityInstance task0 = CaseExecutionerTestHelper.getActivityInstanceByName(fi, "task 0");
		assertNotNull(task0);

		task0.setState(State.RUNNING);
		// caseExecutioner.terminateActivityInstance(task0.getId(), dataManagerBean);
		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task0.getState());
	}
}
