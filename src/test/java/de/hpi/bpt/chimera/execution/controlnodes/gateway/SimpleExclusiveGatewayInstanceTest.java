package de.hpi.bpt.chimera.execution.controlnodes.gateway;

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
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

public class SimpleExclusiveGatewayInstanceTest {

	CaseExecutioner caseExecutioner;
	String filepath = "src/test/resources/execution/SimpleExclusiveGatewayCaseModel.json";

	@Before
	public void setup() {
		CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
	}

	@Test
	public void ControlFlowTest() {
		// start the Case
		caseExecutioner.startCase();
		// get the both activities
		AbstractActivityInstance activity1 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity1");
		AbstractActivityInstance activity2 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity2");
		// at the beginning both activities after the ExclusiveGateway should be
		// in state READY
		assertTrue("Activity1 should be in State READY but isn't.", activity1.getState() == State.READY);
		assertTrue("Activity2 should be in State READY but isn't.", activity2.getState() == State.READY);
		// executing activity1
		caseExecutioner.beginDataControlNodeInstance(activity1, new ArrayList<DataObject>());
		// now because of the ExclusiveGateway activity2 should be skipped
		assertTrue("Activity2 shouldn't be in State READY anymore but still is.", activity2.getState() == State.SKIPPED);
	}
}
