package de.hpi.bpt.chimera.persistencemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class SimpleCasePersistenceTest {
	final String path = "execution/ExclusiveGatewayInstanceControlFlowTest";
	private final String filepath = "src/test/resources/execution/ExclusiveGatewayInstanceControlFlowTest";
	private CaseModel cm;
	// private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
	}

	/*
	 * Checks whether the Controlflow of an ExclusiveGateway works correct as in
	 * the ExclusiveGatewayInstanceControlFlowTest. But now the Case is
	 * persisted and loaded in between.
	 */
	@Test
	public void SimpleCasePersistenceTest() {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, cm.getName());
		doSomeControlFlow(caseExecutioner);
		caseExecutioner = DomainModelPersistenceManager.saveCase(caseExecutioner.getCase()).getCaseExecutioner();
		caseExecutioner = DomainModelPersistenceManager.loadCase(caseExecutioner.getCase().getId()).getCaseExecutioner();
		doRestOfControlflow(caseExecutioner);
	}

	private void doSomeControlFlow(CaseExecutioner caseExecutioner) {
		// CaseExecutioner caseExecutioner = new CaseExecutioner(cm,
		// "TestCase");
		caseExecutioner.startCase();

		HumanTaskInstance activityInst = (HumanTaskInstance) CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity1");
		caseExecutioner.beginDataControlNodeInstance(activityInst, new ArrayList<DataObject>());
		caseExecutioner.terminateDataControlNodeInstance(activityInst, new HashMap<DataClass, ObjectLifecycleState>());
	}

	private void doRestOfControlflow(CaseExecutioner caseExecutioner) {
		Collection<AbstractActivityInstance> activityInstances = caseExecutioner.getActivitiesWithState(State.READY);
		Collection<String> readyActivities = new ArrayList<String>();
		readyActivities.addAll(activityInstances.stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 3 activities in state READY after activating activity1, but there are %d.", readyActivities.size()), readyActivities.size(), 3);
		assertTrue("Activity2 should be in State READY but isn't.", readyActivities.contains("activity2"));
		assertTrue("Activity3 should be in State READY but isn't.", readyActivities.contains("activity3"));
		assertTrue("Activity4 should be in State READY but isn't.", readyActivities.contains("activity4"));

		HumanTaskInstance activityInst2 = (HumanTaskInstance) activityInstances.stream().filter(activity -> activity.getControlNode().getName().equals("activity2")).toArray()[0];
		caseExecutioner.beginDataControlNodeInstance(activityInst2, new ArrayList<DataObject>());
		assertEquals("Activities aren't skipped properly.", caseExecutioner.getActivitiesWithState(State.READY).size(), 0);
	}
}
