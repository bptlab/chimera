package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class DataFlowParserTest {
	final String filepath = "src/test/resources/parser/JsonStringExtendedDataFlow";

	// TODO: make this independent from positions in list
	@Test
	public void parseCaseModel() {

		// Parse a CaseModel from jsonString and give it to the CaseModel
		// manager
		CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);

		AbstractActivity task = cm.getFragments().get(0).getBpmnFragment().getActivities().get(0);
		assertEquals(12, cm.getFragments().get(0).getBpmnFragment().getConditions().size());
		List<ConditionSet> preCondition = task.getPreCondition().getConditionSets();
		List<ConditionSet> postCondition = task.getPostCondition().getConditionSets();

		// PreCondition
		assertEquals("A1", preCondition.get(0).getConditions().get(0).getDataClassName() + preCondition.get(0).getConditions().get(0).getStateName());
		assertEquals("B1", preCondition.get(0).getConditions().get(1).getDataClassName() + preCondition.get(0).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(0).getConditions().get(2).getDataClassName() + preCondition.get(0).getConditions().get(2).getStateName());

		assertEquals("A1", preCondition.get(1).getConditions().get(0).getDataClassName() + preCondition.get(1).getConditions().get(0).getStateName());
		assertEquals("B2", preCondition.get(1).getConditions().get(1).getDataClassName() + preCondition.get(1).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(1).getConditions().get(2).getDataClassName() + preCondition.get(1).getConditions().get(2).getStateName());

		assertEquals("A1", preCondition.get(2).getConditions().get(0).getDataClassName() + preCondition.get(2).getConditions().get(0).getStateName());
		assertEquals("B1", preCondition.get(2).getConditions().get(1).getDataClassName() + preCondition.get(2).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(2).getConditions().get(2).getDataClassName() + preCondition.get(2).getConditions().get(2).getStateName());

		assertEquals("A2", preCondition.get(3).getConditions().get(0).getDataClassName() + preCondition.get(3).getConditions().get(0).getStateName());
		assertEquals("B1", preCondition.get(3).getConditions().get(1).getDataClassName() + preCondition.get(3).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(3).getConditions().get(2).getDataClassName() + preCondition.get(3).getConditions().get(2).getStateName());

		assertEquals("A2", preCondition.get(4).getConditions().get(0).getDataClassName() + preCondition.get(4).getConditions().get(0).getStateName());
		assertEquals("B2", preCondition.get(4).getConditions().get(1).getDataClassName() + preCondition.get(4).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(4).getConditions().get(2).getDataClassName() + preCondition.get(4).getConditions().get(2).getStateName());

		assertEquals("A2", preCondition.get(5).getConditions().get(0).getDataClassName() + preCondition.get(5).getConditions().get(0).getStateName());
		assertEquals("B1", preCondition.get(5).getConditions().get(1).getDataClassName() + preCondition.get(5).getConditions().get(1).getStateName());
		assertEquals("C1", preCondition.get(5).getConditions().get(2).getDataClassName() + preCondition.get(5).getConditions().get(2).getStateName());

		// PostCondition
		assertEquals(6, postCondition.size());
		assertEquals("A3", postCondition.get(0).getConditions().get(0).getDataClassName() + postCondition.get(0).getConditions().get(0).getStateName());
		assertEquals("B3", postCondition.get(0).getConditions().get(1).getDataClassName() + postCondition.get(0).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(0).getConditions().get(2).getDataClassName() + postCondition.get(0).getConditions().get(2).getStateName());

		assertEquals("A3", postCondition.get(1).getConditions().get(0).getDataClassName() + postCondition.get(1).getConditions().get(0).getStateName());
		assertEquals("B4", postCondition.get(1).getConditions().get(1).getDataClassName() + postCondition.get(1).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(1).getConditions().get(2).getDataClassName() + postCondition.get(1).getConditions().get(2).getStateName());

		assertEquals("A3", postCondition.get(2).getConditions().get(0).getDataClassName() + postCondition.get(2).getConditions().get(0).getStateName());
		assertEquals("B5", postCondition.get(2).getConditions().get(1).getDataClassName() + postCondition.get(2).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(2).getConditions().get(2).getDataClassName() + postCondition.get(2).getConditions().get(2).getStateName());

		assertEquals("A4", postCondition.get(3).getConditions().get(0).getDataClassName() + postCondition.get(3).getConditions().get(0).getStateName());
		assertEquals("B3", postCondition.get(3).getConditions().get(1).getDataClassName() + postCondition.get(3).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(3).getConditions().get(2).getDataClassName() + postCondition.get(3).getConditions().get(2).getStateName());

		assertEquals("A4", postCondition.get(4).getConditions().get(0).getDataClassName() + postCondition.get(4).getConditions().get(0).getStateName());
		assertEquals("B4", postCondition.get(4).getConditions().get(1).getDataClassName() + postCondition.get(4).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(4).getConditions().get(2).getDataClassName() + postCondition.get(4).getConditions().get(2).getStateName());

		assertEquals("A4", postCondition.get(5).getConditions().get(0).getDataClassName() + postCondition.get(5).getConditions().get(0).getStateName());
		assertEquals("B5", postCondition.get(5).getConditions().get(1).getDataClassName() + postCondition.get(5).getConditions().get(1).getStateName());
		assertEquals("C2", postCondition.get(5).getConditions().get(2).getDataClassName() + postCondition.get(5).getConditions().get(2).getStateName());
	}
}
