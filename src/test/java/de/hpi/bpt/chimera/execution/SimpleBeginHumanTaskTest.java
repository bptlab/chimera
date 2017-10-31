package de.hpi.bpt.chimera.execution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.jcore.controlnodes.State;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

/**
 * Test the beginning behavior of HumanTasks.
 * Therefore parse the CaseModel with a Dataclass(name: 'dataclass',
 * ObjectLifeycle: 'State 1' -> 'State 2') and a Case with a StartEvent, an
 * EndEvent and with three consecutive HumanTask (StartEvent -> 'task 1' ->
 * 'task 2' -> 'task 3' -> EndEvent) and two DataNodes ('task 1' ->
 * 'dataclass[State 1]' -> 'task 2' & 'task 2' -> 'dataclass[State 2]' -> 'task
 * 3').
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
		AbstractActivityInstance task1 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 1");
		assertNotNull(task1);

		assertEquals("State of ActivityInstance is not READY", State.READY, task1.getState());
		caseExecutioner.beginActivityInstance(task1.getId(), new ArrayList<String>());
		
		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task1.getState());
	}

	@Test
	public void testBeginWithDataObjects() {
		AbstractActivityInstance task1 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 1");
		assertNotNull(task1);

		Collection<FragmentInstance> fragmentInstances = caseExecutioner.getCase().getFragmentInstances().values();
		FragmentInstance fragmentInstance = new ArrayList<>(fragmentInstances).get(0);
		assertEquals(2, fragmentInstance.getControlNodeInstances().size());

		fragmentInstance.createFollowing(task1.getControlNode());
		assertEquals(3, fragmentInstance.getControlNodeInstances().size());

		AbstractActivityInstance task2 = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task 2");
		assertNotNull(task2);
		assertEquals("State of ActivityInstance is not CONTROLFLOW_ENABLED", State.CONTROLFLOW_ENABLED, task2.getState());

		DataStateCondition condition = CaseModelTestHelper.createDataStateCondition(cm, "dataclass", "State 1");
		assertNotNull(condition);
		DataObject dataObject = new DataObject(condition, caseExecutioner.getDataManager());
		// TODO: this shouldn't be necessary
		dataObject.unlock();

		caseExecutioner.getDataManager().getDataObjectMap().put(dataObject.getId(), dataObject);
		assertEquals(1, caseExecutioner.getDataManager().getDataObjectMap().size());
		fragmentInstance.updateDataFlow();
		assertEquals("State of ActivityInstance is not READY", State.READY, task2.getState());

		ArrayList<String> dataObjectIds = new ArrayList<String>(Arrays.asList(dataObject.getId()));
		caseExecutioner.beginActivityInstance(task2.getId(), dataObjectIds);

		assertEquals("State of ActivityInstance is not RUNNING", State.RUNNING, task2.getState());
		assertTrue("DataObject wasn't locked", dataObject.isLocked());
		assertEquals("DataObject wasn't registered as selected DataObject", dataObject, task2.getSelectedDataObjectInstances().get(0));
	}
}
