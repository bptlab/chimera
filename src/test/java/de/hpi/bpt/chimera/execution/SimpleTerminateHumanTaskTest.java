package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.model.CaseModel;

public class SimpleTerminateHumanTaskTest {
	private final String filepath = "src/test/resources/execution/SimpleCaseModelWithOneControlFlowAndDataObjects";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testTerminateWithoutDataObjects() {
		AbstractActivityInstance task0 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 0");
		assertNotNull(task0);

		task0.setState(State.RUNNING);
		// caseExecutioner.terminateActivityInstance(task0.getId(), dataManagerBean);
		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task0.getState());
	}
}
