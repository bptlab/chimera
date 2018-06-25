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
import de.hpi.bpt.chimera.execution.Unicorn;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling.EventDispatcher;
import de.hpi.bpt.chimera.execution.data.DataManager;
import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.EventRestService;

public class CatchEventPreconditionTest extends Unicorn {
	private final String filepath = "src/test/resources/execution/event/CatchEventPreconditionTest.json";

	private CaseExecutioner caseExecutioner;
	private final String body = "{" + "\"attribute\": \"1\"," + "}";

	@Before
	public void setUpTest() {
		super.setUpTest();
		String json = CaseModelTestHelper.getJsonString(filepath);
		CaseModel cm = CaseModelManager.parseCaseModel(json);
		caseExecutioner = ExecutionService.createCaseExecutioner(cm, cm.getName());
		caseExecutioner.startCase();
	}

	@Test
	public void testCatchEventWithUniquePrecondition() {
		FragmentInstance fragmentInstance = CaseExecutionerTestHelper.getFragmentInstanceByName(caseExecutioner, "First Fragment");

		CaseExecutionerTestHelper.executeHumanTaskInstance(caseExecutioner, fragmentInstance, "Create Dataobject");

		DataManager dataManager = caseExecutioner.getDataManager();
		DataObject DO = dataManager.getDataObjects().get(0);

		AbstractEventInstance eventInstance = CaseExecutionerTestHelper.getEventInstanceByName(fragmentInstance, "CatchEvent");

		CaseExecutionerTestHelper.triggerEvent(caseExecutioner, eventInstance, getBase(), body);
		assertEquals("Event instance was not correctly terminated", State.TERMINATED, eventInstance.getState());
		assertEquals("There is not exactly one data object", 1, dataManager.getDataObjects().size());
		assertEquals("eventclass", DO.getDataClass().getName());
		assertEquals("The data object did not changed its state", "State 2", DO.getObjectLifecycleState().getName());
		assertEquals("1", DO.getDataAttributeInstances().get(0).getValue());
	}
}
