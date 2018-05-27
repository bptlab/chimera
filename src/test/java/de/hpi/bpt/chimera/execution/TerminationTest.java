package de.hpi.bpt.chimera.execution;

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
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.persistencemanager.DomainModelPersistenceManager;

public class TerminationTest {
	final String path = "execution/ExclusiveGatewayInstanceControlFlowTest";
	private final String filepath = "src/test/resources/execution/SimpleTerminationCondition";
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
	public void testTermination() {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();

		// Some controlflow to get the TerminationCondition fulfilled.
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "activity1");
		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "activity2");

		HumanTaskInstance activityInst = (HumanTaskInstance) CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity3");
		assertTrue("Activity3 should be enabled before terminating the case.", activityInst.getState() == State.READY);

		caseExecutioner.terminate();

		activityInst = (HumanTaskInstance) CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity3");
		assertTrue("Activity3 should be skipped after terminating the case.", activityInst.getState() == State.SKIPPED);
	}
}
