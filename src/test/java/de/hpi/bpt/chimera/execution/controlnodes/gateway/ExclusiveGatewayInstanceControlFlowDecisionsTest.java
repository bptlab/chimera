package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.model.CaseModel;

public class ExclusiveGatewayInstanceControlFlowDecisionsTest {
	private final String filepath = "src/test/resources/execution/ExclusiveGatewayInstanceControlFlowDecisionsTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void getJsonString() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	/**
	 * Checks whether the guards of the control flows of an ExclusiveGateway are
	 * evaluated correct and the corresponding activities are activated. This is
	 * done by automatically starting, controlling, and checking a test Case.
	 */
	@Test
	public void checkControlFlowGuards() {
		FragmentInstance fi = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fi, "activity1");

		Collection<String> activityInstances = new ArrayList<String>();
		activityInstances.addAll(fi.getActivityInstancesWithState(State.READY).stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 2 activities in state READY after activating activity1, but there are %d.", activityInstances.size()), activityInstances.size(), 2);
		assertTrue("Activity3 should be in State READY but isn't.", activityInstances.contains("activity3"));
		assertTrue("Activity4 should be in State READY but isn't.", activityInstances.contains("activity4"));
	}
}
