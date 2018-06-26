package de.hpi.bpt.chimera.execution.controlnodes.activity;

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
import de.hpi.bpt.chimera.model.CaseModel;

public class EmailActivityInstanceTest {
	private final String filepath = "src/test/resources/execution/EmailActivityInstanceTest.json";
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

	/**
	 * Only checks whether an EmailActivity is terminated automatically. Testing
	 * the mailing function has to be done manually.
	 */
	@Test
	public void testAutomaticEmailExecution() {
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fi, "activity1");
		
		Collection<AbstractActivityInstance> activityInstances = fi.getActivityInstancesWithState(State.READY);
		Collection<String> readyActivities = new ArrayList<String>();
		readyActivities.addAll(activityInstances.stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 1 activities in state READY after activating activity1, but there are %d.", readyActivities.size()), readyActivities.size(), 1);
		assertTrue("After the automatically terminated EmailActivty, activity3 should be in State READY but isn't.", readyActivities.contains("activity3"));
	}
}
