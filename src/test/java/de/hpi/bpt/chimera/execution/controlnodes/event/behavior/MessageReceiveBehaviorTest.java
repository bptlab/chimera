package de.hpi.bpt.chimera.execution.controlnodes.event.behavior;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.hpi.bpt.chimera.CaseExecutionerTestHelper;
import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.FragmentInstance;
import de.hpi.bpt.chimera.execution.controlnodes.State;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
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
	public void testReceiveBehavior() {
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

		MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) receiveEvent.getBehavior();
		String requestId = receiveBehavior.getUnicornKey();
		
		EventDispatcher.receiveEvent(0, 0, requestId, eventJson);
		assertEquals("ReceiveEvent terminated properly", receiveEvent.getState(), State.TERMINATED);
		assertEquals("StartEvent, ReceiveEvent and EndEvent should be in the FragmentInstance", intermediateFragment.getControlNodeInstances().size(), 3);

		assertEquals("One DataObject should be created", caseExecutioner.getDataManager().getDataObjects().size(), 1);
		DataObject dataObject = caseExecutioner.getDataManager().getDataObjects().get(0);
		DataAttributeInstance attributeInstance = dataObject.getDataAttributeInstances().get(0);
		assertNotNull(attributeInstance);
		assertEquals("DataObject was not properly written", attributeInstance.getValue(), "1");
	}
}
