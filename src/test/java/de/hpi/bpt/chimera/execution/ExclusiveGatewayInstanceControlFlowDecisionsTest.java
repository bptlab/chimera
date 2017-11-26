package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.parser.CaseModelParser;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

public class ExclusiveGatewayInstanceControlFlowDecisionsTest {
	String jsonString = "";
	final String path = "execution/ExclusiveGatewayInstanceControlFlowDecisionsTest";
	CaseModel cm;

	@Before
	public void getJsonString() {
		try {
			FileInputStream inputStream = new FileInputStream(this.getClass().getClassLoader().getResource(path).getFile());
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cm = CaseModelParser.parseCaseModel(jsonString);
	}

	/*
	 * Checks whether the guards of the controlflows of an ExclusiveGateway are
	 * eveluated correct and the corresponding activities are activated. This is
	 * done by automatically starting, controlling, and checking a test Case.
	 */
	@Test
	public void parseExclusiveGatewayTest() {

		ControlFlowTest(cm);
	}


	private void ControlFlowTest(CaseModel cm) {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, "TestCase");
		caseExecutioner.startCase();

		HumanTaskInstance activityInst = (HumanTaskInstance) CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "activity1");
		caseExecutioner.beginActivityInstance(activityInst, new ArrayList<DataObject>());
		caseExecutioner.prepareForActivityInstanceTermination(activityInst, new HashMap<DataClass, ObjectLifecycleState>());

		Collection<String> activityInstances = new ArrayList<String>();
		activityInstances.addAll(caseExecutioner.getActivitiesWithState(State.READY).stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 2 activities in state READY after activating activity1, but there are %d.", activityInstances.size()), activityInstances.size(), 2);
		assertTrue("Activity3 should be in State READY but isn't.", activityInstances.contains("activity3"));
		assertTrue("Activity4 should be in State READY but isn't.", activityInstances.contains("activity4"));
	}
}
