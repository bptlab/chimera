package de.hpi.bpt.chimera.parser;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.model.CaseModel;
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
			String file = getClass().getResource(fileName).getFile();
			FileInputStream inputStream = new FileInputStream(file);
			jsonString = IOUtils.toString(inputStream);
			inputStream.close();
		} catch (Exception e) {
			assertEquals("Error", 1, e);
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

		assertTrue("wrong DataModelClass type: DataClass", DataClass.class.isInstance(dataModelClasses.get(0)));
		DataClass dataClass = (DataClass) dataModelClasses.get(0);
		assertEquals("wrong DataClass name", "dataclass 1", dataClass.getName());

		assertTrue("wrong DataModelClass type: EventClass", EventClass.class.isInstance(dataModelClasses.get(1)));
		EventClass eventClass = (EventClass) dataModelClasses.get(1);
		assertEquals("wrong EventClass name", "eventclass1", eventClass.getName());

		ObjectLifecycle objectLifecycle = dataClass.getObjectLifecycle();

		testObjectLifecycleStates(objectLifecycle.getObjectLifecycleStates());

		DataAttribute dataAttribute = dataClass.getAttributes().get(0);
		assertEquals("wrong DataAttribute name", "testString", dataAttribute.getName());
		assertEquals("wrong DataAttribute type", "String", dataAttribute.getType());

		Fragment fragment = caseModel.getFragments().get(0);
		assertEquals("wrong Fragment id", "591330db1ed1325048306e42", fragment.getId());
		assertEquals("wrong Fragment name", "First Fragment", fragment.getName());
		assertEquals("wrong Fragment version", 3, fragment.getVersionNumber());
		// TODO: implement testing for fragment elements

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
}
