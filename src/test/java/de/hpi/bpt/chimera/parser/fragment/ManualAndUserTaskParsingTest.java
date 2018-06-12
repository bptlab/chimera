package de.hpi.bpt.chimera.parser.fragment;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;

public class ManualAndUserTaskParsingTest {
	private final String filepath = "src/test/resources/parser/fragment/ManualAndUserTaskParsingTest.json";

	@Test
	public void testParsing() {
		try {
			CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);
			List<AbstractActivity> activities = cm.getFragments().get(0).getBpmnFragment().getActivities();
			// TODO: change this when Manual and User Tasks get implemented
			assertTrue(activities.get(0) instanceof HumanTask);
			assertTrue(activities.get(1) instanceof HumanTask);
		} catch (Exception e) {
			throw e;
		}
	}
}
