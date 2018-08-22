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
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;

public class ExclusiveGatewayInstanceControlFlowTest {
	private final String filepath = "src/test/resources/execution/ExclusiveGatewayInstanceControlFlowTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	/**
	 * Checks whether the controlflow of an ExclusiveGateway works correct and
	 * whether the correct corresponding activities are activated, and skipped
	 * when one of these activated activities is started. This is done by
	 * automatically starting, controlling and checking a test case.
	 */
	@Test
	public void checkExclusiveGatewayControlFlow() {
		caseExecutioner.startCase();
		FragmentInstance fi = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fi, "activity1");

		Collection<AbstractActivityInstance> activityInstances = fi.getActivityInstancesWithState(State.READY);
		Collection<String> readyActivities = new ArrayList<String>();
		readyActivities.addAll(activityInstances.stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 3 activities in state READY after activating activity1, but there are %d.", readyActivities.size()), readyActivities.size(), 3);
		assertTrue("Activity2 should be in State READY but isn't.", readyActivities.contains("activity2"));
		assertTrue("Activity3 should be in State READY but isn't.", readyActivities.contains("activity3"));
		assertTrue("Activity4 should be in State READY but isn't.", readyActivities.contains("activity4"));

		AbstractActivityInstance activityInst2 = CaseExecutionerTestHelper.getActivityInstanceByName(fi, "activity2");
		caseExecutioner.beginDataControlNodeInstance(activityInst2, new ArrayList<DataObject>());
		assertEquals("Activities aren't skipped properly.", fi.getActivityInstancesWithState(State.READY).size(), 0);
	}
}
