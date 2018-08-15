package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;

public class DataFlowParserTest {
	final String filepath = "src/test/resources/parser/JsonStringExtendedDataFlow.json";

	private List<String> expectedPrecondition = new ArrayList<>(Arrays.asList("A1 B1 C1", "A1 B1 C1", "A1 B2 C1", "A2 B1 C1", "A2 B1 C1", "A2 B2 C1"));
	private List<String> expectedPostcondition = new ArrayList<>(Arrays.asList("A3 B3 C2", "A3 B4 C2", "A3 B5 C2", "A4 B3 C2", "A4 B4 C2", "A4 B5 C2"));
	private AbstractActivity task;

	@Before
	public void setup() {
		CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);
		task = cm.getFragments().get(0).getBpmnFragment().getActivities().get(0);
		assertEquals(12, cm.getFragments().get(0).getBpmnFragment().getConditions().size());
	}

	private String conditionSetToString(ConditionSet cs) {
		return cs.getConditions().stream().map(c -> String.format("%s%s ", c.getDataClassName(), c.getStateName())).sorted().collect(Collectors.joining()).trim();
	}

	private List<String> conditionToString(DataStateCondition condition) {
		return condition.getConditionSets().stream().map(this::conditionSetToString).sorted().collect(Collectors.toList());
	}

	@Test
	public void testPreConditionParsing() {
		assertThat(expectedPrecondition, is(conditionToString(task.getPreCondition())));
	}

	@Test
	public void testPostConditionParsing() {
		assertThat(expectedPostcondition, is(conditionToString(task.getPostCondition())));
	}
}
