package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.BoundaryEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.IntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.StartEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;

public class MessageReceiveBehaviorTest {
	private final String filepath = "src/test/resources/execution/event/receiveEventBehaviorTest.json";
	private CaseModel cm;
	private CaseExecutioner caseExecutioner;
	private String eventJson = "{"
			+ "\"a\": \"1\","
			+ "\"b\": \"2\","
			+ "}";
	@Before
	public void setup() {
		cm = CaseModelTestHelper.parseCaseModel(filepath);
		caseExecutioner = new CaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testIntermediateReceiveBehavior() {
		FragmentInstance intermediateFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "IntermediateFragment");
		assertNotNull(intermediateFragment);

		// StartEvent and ReceiveEvent
		assertEquals("StartEvent and ReceiveEvent should be in the FragmentInstance", intermediateFragment.getControlNodeInstances().size(), 2);

		StartEventInstance startEventInstance = (StartEventInstance) CaseExecutionerTestHelper.getEventInstanceByName(intermediateFragment, "startEvent");
		assertEquals(startEventInstance.getState(), State.TERMINATED);

		assertEquals(caseExecutioner.getDataManager().getDataObjects().size(), 0);
		AbstractEventInstance receiveEvent = CaseExecutionerTestHelper.getEventInstanceByName(intermediateFragment, "intermediateReceiveEvent");
		assertTrue(IntermediateCatchEventInstance.class.isInstance(receiveEvent));
		assertTrue(MessageReceiveEventBehavior.class.isInstance(receiveEvent.getBehavior()));
		assertEquals("ReceiveEvent registered properly", receiveEvent.getState(), State.REGISTERED);

		String requestId = receiveEvent.getId();
		
		EventDispatcher.receiveEvent(caseExecutioner.getCaseModel().getId(), caseExecutioner.getCase().getId(), requestId, eventJson);
		assertEquals("ReceiveEvent terminated properly", receiveEvent.getState(), State.TERMINATED);
		assertEquals("StartEvent, ReceiveEvent and EndEvent should be in the FragmentInstance", intermediateFragment.getControlNodeInstances().size(), 3);

		assertEquals("One DataObject should be created", caseExecutioner.getDataManager().getDataObjects().size(), 1);
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", attributeInstance.getValue(), "1");
	}

	@Test
	public void testBoundaryReceiveBehavior() {
		FragmentInstance boundaryFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "BoundaryFragment");
		assertNotNull(boundaryFragment);

		assertEquals("StartEvent and Task should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 2);
		assertEquals(caseExecutioner.getDataManager().getDataObjects().size(), 0);

		AbstractActivityInstance task = CaseExecutionerTestHelper.getActivityInstanceByName(caseExecutioner, "task");
		assertEquals("BoundaryEvents", task.getControlNode().getAttachedBoundaryEvents().size(), 1);
		task.begin();
		assertEquals("task has not begun properly", task.getState(), State.RUNNING);
		assertEquals("StartEvent, Task and BoundaryEvent should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 3);

		AbstractEventInstance receiveEvent = CaseExecutionerTestHelper.getEventInstanceByName(boundaryFragment, "boundaryReceiveEvent");
		assertTrue(BoundaryEventInstance.class.isInstance(receiveEvent));
		assertTrue(MessageReceiveEventBehavior.class.isInstance(receiveEvent.getBehavior()));
		assertEquals("ReceiveEvent registered properly", receiveEvent.getState(), State.REGISTERED);

		String requestId = receiveEvent.getId();

		EventDispatcher.receiveEvent(caseExecutioner.getCaseModel().getId(), caseExecutioner.getCase().getId(), requestId, eventJson);
		assertEquals("ReceiveEvent terminated properly", receiveEvent.getState(), State.TERMINATED);
		assertEquals("StartEvent, Task, BoundaryEvent and Task after BoundaryEvent should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 4);
		assertEquals("Task was not canceled", task.getState(), State.CANCEL);

		assertEquals("One DataObject should be created", caseExecutioner.getDataManager().getDataObjects().size(), 1);
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", attributeInstance.getValue(), "2");
	}
}
