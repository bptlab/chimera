package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.condition.CaseStartTriggerConsequence;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.DataModel;
import de.hpi.bpt.chimera.model.datamodel.DataModelClass;
import de.hpi.bpt.chimera.model.datamodel.EventClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycle;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.model.fragment.Fragment;

public class CaseModelParserTest {
	String jsonString = "";
	final String fileName = "JsonString";

	@Before
	public void getJsonString() {
		try {
			// String file = getClass().getResource(fileName).getFile();
			String file = "src/test/resources/parser/JsonString";
			FileInputStream inputStream = new FileInputStream(file);
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 2, e);
		}
	}

	@Test
	public void parseCaseModel() {
		CaseModel caseModel;
		caseModel = CaseModelParser.parseCaseModel(jsonString);
		assertEquals("wrong CaseModel id", "591330db1ed1325048306e40", caseModel.getId());
		assertEquals("wrong CaseModel name", "testScenario123", caseModel.getName());
		assertEquals("wrong CaseModel version", 22, caseModel.getVersionNumber());

		DataModel dataModel = caseModel.getDataModel();
		assertEquals("wrong DataModel version", 21, dataModel.getVersionNumber());

		List<DataModelClass> dataModelClasses = dataModel.getDataModelClasses();
		testDataModelClasses(dataModelClasses);

		TerminationCondition terminationCondition = caseModel.getTerminationCondition();
		testTerminationCondition(terminationCondition, dataModelClasses);

		List<CaseStartTrigger> caseStartTriggers = caseModel.getStartCaseTrigger();
		testStartCondition(caseStartTriggers, dataModelClasses);

		Fragment fragment = caseModel.getFragments().get(0);
		assertEquals("wrong Fragment id", "591330db1ed1325048306e42", fragment.getId());
		assertEquals("wrong Fragment name", "First Fragment", fragment.getName());
		assertEquals("wrong Fragment version", 3, fragment.getVersionNumber());
		// TODO: implement testing for fragment elements

		caseModel.saveCaseModelToDB();
	}

	private void testDataModelClasses(List<DataModelClass> dataModelClasses) {
		assertTrue("wrong DataModelClass type: DataClass", DataClass.class.isInstance(dataModelClasses.get(0)));
		DataClass dataClass = (DataClass) dataModelClasses.get(0);
		assertEquals("wrong DataClass name", "dataclass 1", dataClass.getName());

		assertTrue("wrong DataModelClass type: EventClass", EventClass.class.isInstance(dataModelClasses.get(2)));
		EventClass eventClass = (EventClass) dataModelClasses.get(2);
		assertEquals("wrong EventClass name", "eventclass1", eventClass.getName());

		ObjectLifecycle objectLifecycle = dataClass.getObjectLifecycle();

		testObjectLifecycleStates(objectLifecycle.getObjectLifecycleStates());

		DataAttribute dataAttribute = dataClass.getDataAttributes().get(0);
		assertEquals("wrong DataAttribute name", "testString", dataAttribute.getName());
		assertEquals("wrong DataAttribute type", "String", dataAttribute.getType());
	}

	private void testObjectLifecycleStates(List<ObjectLifecycleState> objectLifecycleStates) {
		assertTrue("wrong ObjectLifecycleStates amount", objectLifecycleStates.size() == 3);

		// has to handle olcStates in specific behavior because List is unsorted
		// because of HashMap in ObjectLifecycleParser
		ObjectLifecycleState state1 = null, state2 = null, state3 = null;
		boolean state1_occured = false, state2_occured = false, state3_occured = false;

		for (ObjectLifecycleState state : objectLifecycleStates) {
			if (state.getName().equals("State 1")) {
				state1 = state;
				state1_occured = true;
			}
			else if (state.getName().equals("State 2")) {
				state2 = state;
				state2_occured = true;
			}
			else if (state.getName().equals("State 3")) {
				state3 = state;
				state3_occured = true;
			}
		}
		assertTrue("wrong ObjectLifecycleStates", state1_occured && state2_occured && state3_occured);

		assertTrue("wrong olcState predecessors", state1.getPredecessors().isEmpty());
		assertEquals("wrong olcState successors", "State 2", state1.getSuccessors().get(0).getName());

		assertEquals("wrong olcState successors", "State 1", state2.getPredecessors().get(0).getName());
		assertTrue("wrong olcState successors", state2.getSuccessors().isEmpty());

		assertTrue("wrong olcState predecessors", state3.getPredecessors().isEmpty());
		assertTrue("wrong olcState successors", state3.getSuccessors().isEmpty());
	}

	private void testTerminationCondition(TerminationCondition terminationCondition, List<DataModelClass> dataModelClasses) {
		List<ConditionSet> components = terminationCondition.getConditionSets();
		assertEquals("wrong TerminationConditionComponent amount", 2, components.size());

		ConditionSet component1 = components.get(0);
		ConditionSet component2 = components.get(1);

		assertEquals("wrong DataObjectStateCondition amount", 2, component1.getConditions().size());
		assertEquals("wrong DataObjectStateCondition amount", 1, component2.getConditions().size());

		AtomicDataStateCondition objectStateCondition1 = component1.getConditions().get(0);
		AtomicDataStateCondition objectStateCondition2 = component1.getConditions().get(1);
		AtomicDataStateCondition objectStateCondition3 = component2.getConditions().get(0);

		DataClass dataClass = (DataClass) dataModelClasses.get(0);
		DataClass dc = (DataClass) dataModelClasses.get(1);
		ObjectLifecycleState dc_state = dc.getObjectLifecycle().getObjectLifecycleStates().get(0);
		assertTrue("wrong Dataclass mapping", objectStateCondition1.getDataClass().equals(dataClass));
		assertTrue("wrong Dataclass mapping", objectStateCondition2.getDataClass().equals(dc));
		assertTrue("wrong Olc-State mapping", objectStateCondition2.getObjectLifecycleState().equals(dc_state));
		assertTrue("wrong Dataclass mapping", objectStateCondition3.getDataClass().equals(dataClass));
		
		assertTrue("wrong Dataclass matching", objectStateCondition1.getDataClass().equals(objectStateCondition3.getDataClass()));
	}

	private void testStartCondition(List<CaseStartTrigger> caseStartTriggers, List<DataModelClass> dataModelClasses) {
		assertEquals("wrong CaseStartTrigger amount", 1, caseStartTriggers.size());
		CaseStartTrigger trigger = caseStartTriggers.get(0);

		assertEquals("wrong CaseStart", "StartCondition", trigger.getQueryExecutionPlan());
		List<CaseStartTriggerConsequence> consequence = trigger.getTriggerConsequences();

		assertEquals("wrong CaseStartTriggerConsequence amount", 2, consequence.size());

		int dcPos = consequence.get(0).getDataObjectState().getDataClass().getName().equals("dc2") ? 0 : 1;

		AtomicDataStateCondition objectStateCondition1 = consequence.get(dcPos).getDataObjectState();

		DataClass dc = (DataClass) dataModelClasses.get(1);
		assertTrue("wrong DataClass mapping", objectStateCondition1.getDataClass().equals(dc));

		ObjectLifecycleState state = dc.getObjectLifecycle().getObjectLifecycleStates().get(0);
		assertTrue("wrong State mapping", objectStateCondition1.getObjectLifecycleState().equals(state));

		int dataclassPos = dcPos == 0 ? 1 : 0;

		DataAttribute attr = dataModelClasses.get(0).getDataAttributes().get(0);
		String jsonPathString = consequence.get(dataclassPos).getDataAttributeToJsonPath().get(attr);
		assertNotNull("wrong Attribute mapping", jsonPathString);
	}
}
