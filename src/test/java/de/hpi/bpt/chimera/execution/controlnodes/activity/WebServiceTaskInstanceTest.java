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
	private final String filepath = "src/test/resources/execution/WebServiceTaskInstanceTest";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testAutoExecution() {
		AbstractActivityInstance task0 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Get ReiseWarnung");
		assertTrue("is automatic task was not parsed correctly", task0.isAutomaticTask());
		assertTrue("task has precondition", task0.getControlNode().getPreCondition().getConditionSets().isEmpty());
		assertNotNull(task0);

		assertEquals("State of ActivityInstance is not TERMINATED", State.TERMINATED, task0.getState());

		List<DataObject> dataObjects = caseExecutioner.getDataManager().getDataObjects();
		assertEquals("wrong creation of data objects", 1, dataObjects.size());

		DataObject dataObject = dataObjects.get(0);

		DataAttributeInstance dataAttributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("wrong data attribute value or api has changed", "de", dataAttributeInstance.getValue());

		AbstractActivityInstance task1 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "Look at Reisewarnung");
		assertEquals("State of ActivityInstance is not READY", State.READY, task1.getState());
	}
}
