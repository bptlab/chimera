package de.hpi.bpt.chimera.execution.controlnodes.gateway;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

public class SimpleExclusiveGatewayInstanceTest {
	private final String filepath = "src/test/resources/execution/SimpleExclusiveGatewayCaseModel.json";
	private CaseExecutioner caseExecutioner;

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
		FragmentInstance fi = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");
		AbstractActivityInstance activity1 = CaseExecutionerTestHelper.getActivityInstanceByName(fi, "activity1");
		AbstractActivityInstance activity2 = CaseExecutionerTestHelper.getActivityInstanceByName(fi, "activity2");
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
