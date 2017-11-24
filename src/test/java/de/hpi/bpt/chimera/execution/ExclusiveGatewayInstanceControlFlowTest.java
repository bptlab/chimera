package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.parser.CaseModelParser;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

public class ExclusiveGatewayInstanceControlFlowTest {
	String jsonString = "";
	final String path = "execution/ExclusiveGatewayInstanceControlFlowTest";
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
	 * Checks whether the controlflow of an ExclusiveGateway works correct and
	 * whether the correct corresponding activities are activated, and skipped
	 * when one of these activated activities is started. This is done by
	 * automatically starting, controlling and checking a test case.
	 */
	@Test
	public void parseExclusiveGatewayTest() {

		ControlFlowTest(cm);
	}

	private void ControlFlowTest(CaseModel cm) {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, "TestCase");
		caseExecutioner.startCase();

		HumanTaskInstance activityInst = (HumanTaskInstance) caseExecutioner.getAllActivitiesWithState(State.READY).stream().filter(activity -> activity.getControlNode().getName().equals("activity1")).toArray()[0];
		caseExecutioner.beginActivityInstance(activityInst.getId(), new ArrayList<String>());
		caseExecutioner.terminateActivityInstance(activityInst.getId(), new DataManagerBean(new JSONObject()));

		Collection<AbstractActivityInstance> activityInstances = caseExecutioner.getAllActivitiesWithState(State.READY);
		Collection<String> readyActivities = new ArrayList<String>();
		readyActivities.addAll(activityInstances.stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 3 activities in state READY after activating activity1, but there are %d.", readyActivities.size()), readyActivities.size(), 3);
		assertTrue("Activity2 should be in State READY but isn't.", readyActivities.contains("activity2"));
		assertTrue("Activity3 should be in State READY but isn't.", readyActivities.contains("activity3"));
		assertTrue("Activity4 should be in State READY but isn't.", readyActivities.contains("activity4"));

		HumanTaskInstance activityInst2 = (HumanTaskInstance) activityInstances.stream().filter(activity -> activity.getControlNode().getName().equals("activity2")).toArray()[0];
		caseExecutioner.beginActivityInstance(activityInst2.getId(), new ArrayList<String>());
		assertEquals("Activities aren't skipped properly.", caseExecutioner.getAllActivitiesWithState(State.READY).size(), 0);
	}
}
