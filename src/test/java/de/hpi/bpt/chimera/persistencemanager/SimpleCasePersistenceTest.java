package de.hpi.bpt.chimera.persistencemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.gateway.ExclusiveGatewayInstanceControlFlowTest;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class SimpleCasePersistenceTest {
	private final String filepath = "src/test/resources/execution/ExclusiveGatewayInstanceControlFlowTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;
	private FragmentInstance fi;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
		fi = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
	}

	/**
	 * Checks whether the Controlflow of an ExclusiveGateway works correct as in
	 * the ExclusiveGatewayInstanceControlFlowTest. But now the Case is
	 * persisted and loaded in between.
	 * 
	 * @see ExclusiveGatewayInstanceControlFlowTest#checkExclusiveGatewayControlFlow()
	 */
	@Test
	public void testCasePersistence() {
		doSomeControlFlow();
		caseExecutioner = DomainModelPersistenceManager.saveCase(caseExecutioner.getCase()).getCaseExecutioner();
		caseExecutioner = DomainModelPersistenceManager.loadCase(caseExecutioner.getCase().getId()).getCaseExecutioner();
		doRestOfControlflow();
	}

	private void doSomeControlFlow() {
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fi, "activity1");
	}

	private void doRestOfControlflow() {
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
