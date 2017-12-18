package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.activity.HumanTaskInstance;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;

/**
 * Test the beginning behavior of HumanTasks. Therefore parse the CaseModel with
 * a Dataclass(name: 'dataclass', ObjectLifeycle: 'State 1' -> 'State 2') and a
 * Case with a StartEvent, an EndEvent and with four consecutive HumanTask
 * (StartEvent -> task 0 ->'task 1' -> 'task 2' -> 'task 3' -> EndEvent) and two
 * DataNodes ('task 1' -> 'dataclass[State 1]' -> 'task 2' & 'task 2' ->
 * 'dataclass[State 2]' -> 'task 3').
 */
public class SimpleBeginHumanTaskTest {
	private final String filepath = "src/test/resources/execution/SimpleCaseModelWithOneControlFlowAndDataObjects";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;

	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testBeginWithoutDataObjects() {
		AbstractActivityInstance task0 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 0");
		assertNotNull(task0);

		assertEquals("State of ActivityInstance is not READY", State.READY, task0.getState());
		caseExecutioner.beginActivityInstance(task0, new ArrayList<DataObject>());
		
		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task0.getState());
	}

	@Test
	public void testBeginWithDataObjects() {
		AbstractActivityInstance task0 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 0");
		AbstractControlNode task1ControlNode = task0.getControlNode().getOutgoingControlNodes().get(0);
		assertEquals("Following ControlNode is not a HumanTask", task1ControlNode.getClass(), HumanTask.class);

		HumanTaskInstance task1 = (HumanTaskInstance) ControlNodeInstanceFactory.createControlNodeInstance(task1ControlNode, task0.getFragmentInstance());
		assertNotNull(task1);

		Collection<FragmentInstance> fragmentInstances = caseExecutioner.getCase().getFragmentInstances().values();
		FragmentInstance fragmentInstance = new ArrayList<>(fragmentInstances).get(0);
		assertEquals(2, fragmentInstance.getControlNodeInstances().size());

		fragmentInstance.createFollowing(task1.getControlNode());
		assertEquals(3, fragmentInstance.getControlNodeInstances().size());

		AbstractActivityInstance task2 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 2");
		assertNotNull(task2);
		assertEquals("State of ActivityInstance is not CONTROLFLOW_ENABLED", State.CONTROLFLOW_ENABLED, task2.getState());

		AtomicDataStateCondition condition = CaseModelTestHelper.createDataStateCondition(cm, "dataclass", "State 1");
		assertNotNull(condition);
		DataObject dataObject = new DataObject(condition, caseExecutioner.getDataManager());
		// TODO: this shouldn't be necessary
		dataObject.unlock();

		caseExecutioner.getDataManager().getDataObjectIdToDataObject().put(dataObject.getId(), dataObject);
		assertEquals(1, caseExecutioner.getDataManager().getDataObjectIdToDataObject().size());
		fragmentInstance.updateDataFlow();
		assertEquals("State of ActivityInstance is not READY", State.READY, task2.getState());

		ArrayList<DataObject> dataObjects = new ArrayList<>(Arrays.asList(dataObject));
		caseExecutioner.beginActivityInstance(task2, dataObjects);

		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task2.getState());
		assertTrue("DataObject wasn't locked", dataObject.isLocked());
		assertEquals("DataObject wasn't registered as selected DataObject", dataObject, task2.getSelectedDataObjects().get(0));
	}
}
