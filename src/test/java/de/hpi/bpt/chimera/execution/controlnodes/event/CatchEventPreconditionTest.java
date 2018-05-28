package de.hpi.bpt.chimera.execution.controlnodes.event;

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
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.EventRestService;

public class CatchEventPreconditionTest extends JerseyTest {
	private final String filepath = "src/test/resources/execution/event/CatchEventPreconditionTest.json";

	private CaseExecutioner caseExecutioner;

	private final String body = "{" + "\"attribute\": \"1\"," + "}";
	private WebTarget base;
	private MockServerClient unicorn;

	@Override
	protected Application configure() {
		return new ResourceConfig(EventRestService.class);
	}

	@Rule
	public MockServerRule mockServerRule = new MockServerRule(this, 8081);

	@SuppressWarnings("resource")
	@Before
	public void setup() {
		base = target("eventdispatcher");
		String host = "localhost";
		int port = 8081;
		// WireMock.configureFor(host, port);

		unicorn = new MockServerClient(host, port).reset();
		
		String unicornPathDeploy = "/Unicorn-unicorn_BP15_dev/webapi/REST/EventQuery/REST";
		EventDispatcher.setUrl(String.format("http://%s:%d", host, port));
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
	public void testCatchEventWithUniquePrecondition() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");

		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, "Create Dataobject");

		DataManager dataManager = caseExecutioner.getDataManager();
		DataObject DO = dataManager.getDataObjects().get(0);

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(fragmentInstance, "CatchEvent");

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, base, body);
		assertEquals("Event instance was not correctly terminated", State.TERMINATED, eventInstance.getState());
		assertEquals("There is not exactly one data object", 1, dataManager.getDataObjects().size());
		assertEquals("eventclass", DO.getDataClass().getName());
		assertEquals("The data object did not changed its state", "State 2", DO.getObjectLifecycleState().getName());
		assertEquals("1", DO.getDataAttributeInstances().get(0).getValue());
	}
}
