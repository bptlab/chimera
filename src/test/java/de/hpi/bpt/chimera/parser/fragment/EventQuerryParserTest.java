package de.hpi.bpt.chimera.parser.fragment;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class EventQuerryParserTest {
	final String filepath = "src/test/resources/parser/fragment/ReceiveEventQuerryParserTest.json";
	CaseModel cm;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
	}

	@Test
	public void testModifyingJsonPath() {
		Fragment fragment = CaseModelTestHelper.getFragmentByName(cm, "Event Querry fits Event Class");
		AbstractEvent event = CaseModelTestHelper.getEventByName(fragment, "Receive Event");
		List<String> actualjsonPaths = getOutputJsonPathsForEvent(event);

		assertThat(actualjsonPaths, containsInAnyOrder("$.attribute1", "$.attribute2"));
	}

	@Test
	public void testNotModifyingJsonPathWithPredefienedMapping() {
		Fragment fragment = CaseModelTestHelper.getFragmentByName(cm, "Event Querry fits Event Class with Json Mapping");
		AbstractEvent event = CaseModelTestHelper.getEventByName(fragment, "Receive Event");
		List<String> actualjsonPaths = getOutputJsonPathsForEvent(event);

		assertThat(actualjsonPaths, hasSize(1));
		assertThat(actualjsonPaths, contains("$.attribute1"));
	}

	@Test
	public void testNotModifyingJsonPathWrongEventQuerryName() {
		Fragment fragment = CaseModelTestHelper.getFragmentByName(cm, "Event Querry does not fit Event Class Name");
		AbstractEvent event = CaseModelTestHelper.getEventByName(fragment, "Receive Event");
		List<String> actualjsonPaths = getOutputJsonPathsForEvent(event);

		assertTrue(actualjsonPaths.isEmpty());
	}

	@Test
	public void testNotModifyingJsonPathWithDataClass() {
		Fragment fragment = CaseModelTestHelper.getFragmentByName(cm, "Event Querry is for Data Class");
		AbstractEvent event = CaseModelTestHelper.getEventByName(fragment, "Receive Event");
		List<String> actualjsonPaths = getOutputJsonPathsForEvent(event);

		assertTrue(actualjsonPaths.isEmpty());
	}

	private List<String> getOutputJsonPathsForEvent(AbstractEvent event) {
		DataStateCondition postCondition = event.getPostCondition();
		ConditionSet conditionSet = postCondition.getConditionSets().get(0);

		DataNode dataNode = (DataNode) conditionSet.getConditions().get(0);
		List<String> actualjsonPaths = dataNode.getDataAttributeJsonPaths().stream().map(p -> p.getJsonPath()).collect(Collectors.toList());
		return actualjsonPaths;
	}
}
