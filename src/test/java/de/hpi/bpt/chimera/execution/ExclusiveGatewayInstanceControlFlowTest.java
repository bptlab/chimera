package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.jcore.controlnodes.State;

public class ExclusiveGatewayInstanceControlFlowTest {
	String jsonString = "";
	final String path = "execution/ExclusiveGatewayInstanceControlFlowTest";

	@Before
	public void getJsonString() {
		try {
			FileInputStream inputStream = new FileInputStream(this.getClass().getClassLoader().getResource(path).getFile());
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 1, e);
		}
	}

	@Test
	public void parseExclusiveGatewayTest() {
		CaseModel cm = CaseModelManager.parseCaseModel(jsonString);
		assertEquals("number of parsed ExclusiveGateways", 5, cm.getFragments().get(0).getBpmnFragment().getExclusiveGateways().size());

		// System.out.println(cm.getFragments().get(0).getBpmnFragment().getTasks().get(0).getName());
		ControlFlowTest(cm);
	}

	private void ControlFlowTest(CaseModel cm) {
		CaseExecutioner caseExecutioner = new CaseExecutioner(cm, "TestCase");
		Activity activity = caseExecutioner.getCaseModel().getFragments().get(0).getBpmnFragment().getTasks().get(0);
		HumanTaskInstance activityInst = (HumanTaskInstance) ControlNodeInstanceFactory.createControlNodeInstance(activity, caseExecutioner.getCase().getFragmentInstances().values().iterator().next());
		caseExecutioner.beginActivityInstance(activityInst.getId(), new ArrayList<String>());
		caseExecutioner.terminateActivityInstance(activityInst.getId(), new DataManagerBean(new JSONObject()));

		// System.out.println(caseExecutioner.getAllActivitiesWithState(State.CONTROLFLOW_ENABLED).size());
		Collection<AbstractActivityInstance> activityInstances = new ArrayList<>();


		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.READY));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.RUNNING));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.TERMINATED));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.DATAFLOW_ENABLED));
		activityInstances.addAll(caseExecutioner.getAllActivitiesWithState(State.CONTROLFLOW_ENABLED));

		for (AbstractActivityInstance actIns : activityInstances) {
			System.out.println(actIns.getControlNode().getName() + "is in state: " + actIns.getSelectedDataObjectInstances().toString());
		}
		// TODO get this Test working and add an appropriate assertion
	}
}
