package de.hpi.bpt.chimera.execution.controlnodes.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;

public class WebServiceTaskInstanceTest {
	private final String filepath = "src/test/resources/execution/WebServiceTaskInstanceTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;
	private AbstractActivityInstance webserviceTaskInstance;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();

		AbstractActivityInstance precedingTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "First Activity");
		precedingTaskInstance.begin();
		precedingTaskInstance.terminate();
		webserviceTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Get ReiseWarnung");
		testInitialization();
	}

	private void testInitialization() {
		assertNotNull(webserviceTaskInstance);
		assertTrue(webserviceTaskInstance instanceof WebServiceTaskInstance);
		assertTrue("WebServiceTaskInstance was not declared as automatic task", webserviceTaskInstance.hasAutomaticBegin());
		assertTrue("WebServiceTaskInstance has precondition", webserviceTaskInstance.getControlNode().getPreCondition().getConditionSets().isEmpty());
	}

	@Test
	public void testAutomaticExecution() {
		assertEquals("State of ActivityInstance is not TERMINATED", State.TERMINATED, webserviceTaskInstance.getState());
	}

	@Test
	public void testRestRequest() {
		DataObject dataObject = testDataObjectCreation();

		// an data attribute should be written by the webservice task instance
		DataAttributeInstance dataAttributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("wrong data attribute value or api has changed", "de", dataAttributeInstance.getValue());
	}

	private DataObject testDataObjectCreation() {
		List<DataObject> dataObjects = caseExecutioner.getDataManager().getDataObjects();
		assertEquals("Wrong amount of created data objects", 1, dataObjects.size());
		return dataObjects.get(0);
	}

	@Test
	public void testFollowingControlNodeInstantion() {
		AbstractActivityInstance followingTaskInstance = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Look at ReiseWarnung");
		assertNotNull(followingTaskInstance);
		assertEquals("State of ActivityInstance is not READY", State.READY, followingTaskInstance.getState());
	}
}
