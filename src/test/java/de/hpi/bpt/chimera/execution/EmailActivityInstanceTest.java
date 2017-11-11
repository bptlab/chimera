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
import de.hpi.bpt.chimera.execution.activity.EmailActivityInstance;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.parser.CaseModelParser;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

public class EmailActivityInstanceTest {
	String jsonString = "";
	final String path = "execution/EmailActivityInstanceTest";
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
	 * Only checks whether an EmailActivity is terminated automatically. Testing
	 * the mailing function has to be done manually.
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

		EmailActivityInstance emailActivityInst = (EmailActivityInstance) caseExecutioner.getAllActivitiesWithState(State.READY).stream().filter(activity -> activity.getControlNode().getName().equals("activity2")).toArray()[0];
		caseExecutioner.beginActivityInstance(emailActivityInst.getId(), new ArrayList<String>());

		Collection<AbstractActivityInstance> activityInstances = caseExecutioner.getAllActivitiesWithState(State.READY);
		Collection<String> readyActivities = new ArrayList<String>();
		readyActivities.addAll(activityInstances.stream().map(activity -> activity.getControlNode().getName()).collect(Collectors.toList()));

		assertEquals(String.format("There should be 1 activities in state READY after activating activity1, but there are %d.", readyActivities.size()), readyActivities.size(), 1);
		assertTrue("After the automatically terminated EmailActivty, activity3 should be in State READY but isn't.", readyActivities.contains("activity3"));
	}
}
