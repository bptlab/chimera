package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import static org.junit.Assert.*;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
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
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.EventRestService;

public class MessageReceiveBehaviorTest extends JerseyTest {
	private final String filepath = "src/test/resources/execution/event/receiveEventBehaviorTest.json";

	private CaseExecutioner caseExecutioner;
	private String eventJson = "{" + "\"a\": \"1\"," + "\"b\": \"2\"," + "}";

	private WebTarget base;
	private final int port = 8081;
	@Rule
	public MockServerRule mockServerRule = new MockServerRule(this, port);

	private MockServerClient unicorn;

	@Override
	protected Application configure() {
		return new ResourceConfig(EventRestService.class);
	}

	@SuppressWarnings("resource")
	@Before
	public void setup() {
		base = target("eventdispatcher");
		String host = "localhost";

		unicorn = new MockServerClient(host, port).reset();
		EventDispatcher.setUrl(String.format("http://%s:%d", host, port));

		String unicornPathDeploy = "/Unicorn-unicorn_BP15_dev/webapi/REST/EventQuery/REST";
		unicorn.when(
				HttpRequest.request()
					.withMethod("POST")
					.withPath(unicornPathDeploy))
			.respond(
				HttpResponse.response()
					.withStatusCode(201)
					.withBody("1"));


		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		unicorn.close();
	}

	@Test
	public void testIntermediateReceiveBehavior() {
		FragmentInstance intermediateFragment = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "IntermediateFragment");
		assertNotNull(intermediateFragment);

		// StartEvent and ReceiveEvent
		assertEquals("StartEvent and ReceiveEvent should be in the FragmentInstance", intermediateFragment.getControlNodeInstances().size(), 2);

		StartEventInstance startEventInstance = (StartEventInstance) CaseExecutionerTestHelper.getEventInstanceByName(intermediateFragment, "startEvent");
		assertEquals(State.TERMINATED, startEventInstance.getState());

		assertEquals(0, caseExecutioner.getDataManager().getDataObjects().size());
		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(intermediateFragment, "intermediateReceiveEvent");
		assertTrue(IntermediateCatchEventInstance.class.isInstance(eventInstance));
		assertTrue(MessageReceiveEventBehavior.class.isInstance(eventInstance.getBehavior()));

		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();
		assertEquals("Notification Rule Id was not set", "1", receiveBehavior.getNotificationRule());
		assertEquals("ReceiveEvent in incorrect state", State.REGISTERED, eventInstance.getState());

		assertFalse("No registered events", caseExecutioner.getRegisteredEventBehaviors().isEmpty());

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, base, eventJson);
		
		assertEquals("ReceiveEvent terminated properly", State.TERMINATED, eventInstance.getState());
		assertEquals("StartEvent, ReceiveEvent and EndEvent should be in the FragmentInstance", 3, intermediateFragment.getControlNodeInstances().size());

		assertEquals("One DataObject should be created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", "1", attributeInstance.getValue());
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

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(boundaryFragment, "boundaryReceiveEvent");
		assertTrue(BoundaryEventInstance.class.isInstance(eventInstance));
		assertTrue(MessageReceiveEventBehavior.class.isInstance(eventInstance.getBehavior()));
		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();
		assertEquals("Notification Rule Id was not set", "1", receiveBehavior.getNotificationRule());
		assertEquals("ReceiveEvent registered properly", eventInstance.getState(), State.REGISTERED);

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, base, eventJson);

		assertEquals("ReceiveEvent terminated properly", eventInstance.getState(), State.TERMINATED);
		assertEquals("StartEvent, Task, BoundaryEvent and Task after BoundaryEvent should be in the FragmentInstance", boundaryFragment.getControlNodeInstances().size(), 4);
		assertEquals("Task was not canceled", task.getState(), State.CANCEL);

		assertEquals("One DataObject should be created", 1, caseExecutioner.getDataManager().getDataObjects().size());
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertEquals("DataObject was not properly written", "2", attributeInstance.getValue());
	}
}
