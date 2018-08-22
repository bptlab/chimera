package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.Unicorn;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.data.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;

public class MessageReceiveBehaviorTest extends Unicorn {
	private final String filepath = "src/test/resources/execution/event/CatchEventBehaviorTest.json";

	private CaseExecutioner caseExecutioner;
	private String eventJson = "{" +
								"\"a\": \"1\"," +
								"\"b\": \"2\"," +
								"\"c\": \"3\"" +
								"}";

	@Before
	public void setUpTest() {
		super.setUpTest();
		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testIntermediateReceiveBehavior() {
		FragmentInstance intermediateFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "IntermediateFragment");
		assertNotNull(intermediateFragment);

		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, intermediateFragment, "RegisterEvent");
		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(intermediateFragment, "IntermediateReceiveEvent");
		assertTrue(MessageReceiveEventBehavior.class.isInstance(eventInstance.getBehavior()));

		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();

		assertNotNull("Notification Rule Id was not set", receiveBehavior.getNotificationRule());
		assertFalse("Notification Rule Id was not set properly", receiveBehavior.getNotificationRule().isEmpty());
		assertNotEquals("Notification Rule Id was not set properly", "-1", receiveBehavior.getNotificationRule());

		assertEquals("ReceiveEvent in incorrect state", State.REGISTERED, eventInstance.getState());

		assertFalse("No registered events", caseExecutioner.getRegisteredEventBehaviors().isEmpty());

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), eventJson);
		
		assertEquals("ReceiveEvent terminated properly", State.TERMINATED, eventInstance.getState());

		assertEquals("One DataObject should be created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", "1", attributeInstance.getValue());
	}

	@Test
	public void testBoundaryReceiveBehavior() {
		FragmentInstance boundaryFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "BoundaryFragment");
		assertNotNull(boundaryFragment);

		AbstractActivityInstance task = CaseExecutionerTestHelper.getActivityInstanceByName(boundaryFragment, "Task");
		assertEquals("BoundaryEvents", task.getControlNode().getAttachedBoundaryEvents().size(), 1);
		task.begin();
		assertEquals("task has not begun properly", task.getState(), State.RUNNING);
		assertEquals("StartEvent, Task and BoundaryEvent should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 3);

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(boundaryFragment, "BoundaryReceiveEvent");
		assertTrue(MessageReceiveEventBehavior.class.isInstance(eventInstance.getBehavior()));
		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();

		assertNotNull("Notification Rule Id was not set", receiveBehavior.getNotificationRule());
		assertFalse("Notification Rule Id was not set properly", receiveBehavior.getNotificationRule().isEmpty());
		assertNotEquals("Notification Rule Id was not set properly", "-1", receiveBehavior.getNotificationRule());

		assertEquals("ReceiveEvent registered properly", eventInstance.getState(), State.REGISTERED);

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), eventJson);

		assertEquals("ReceiveEvent terminated properly", eventInstance.getState(), State.TERMINATED);
		assertEquals("StartEvent, Task, BoundaryEvent and Task after BoundaryEvent should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 4);
		assertEquals("Task was not canceled", task.getState(), State.CANCEL);

		assertEquals("One DataObject should be created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", "2", attributeInstance.getValue());
	}

	@Test
	public void testMessageReceiveStartEvent() {
		FragmentInstance boundaryFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "StartEventFragment");

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(boundaryFragment, "StartEvent");
		assertTrue(MessageReceiveEventBehavior.class.isInstance(eventInstance.getBehavior()));
		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();

		assertNotNull("Notification Rule Id was not set", receiveBehavior.getNotificationRule());
		assertFalse("Notification Rule Id was not set properly", receiveBehavior.getNotificationRule().isEmpty());
		assertNotEquals("Notification Rule Id was not set properly", "-1", receiveBehavior.getNotificationRule());

		assertEquals("ReceiveEvent registered properly", eventInstance.getState(), State.REGISTERED);

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), eventJson);

		assertEquals("ReceiveEvent terminated properly", eventInstance.getState(), State.TERMINATED);
		CaseExecutionerTestHelper.getActivityInstanceByName(boundaryFragment, "CheckStartEvent");

		assertEquals("One DataObject should be created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", "3", attributeInstance.getValue());
	}
}
