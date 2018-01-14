package de.hpi.bpt.chimera.parser.fragment;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hpi.bpt.chimera.CaseModelTestHelper;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.fragment.Fragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;

public class EventParserTest {
	final String filepath = "src/test/resources/parser/fragment/allPossibleEvents.json";

	@Test
	public void parseAllPossibleEvents() {
		CaseModel cm = CaseModelTestHelper.parseCaseModel(filepath);

		assertEquals("Not exactly one fragment", cm.getFragments().size(), 1);

		Fragment fragment = cm.getFragments().get(0);

		AbstractEvent startEvent = CaseModelTestHelper.getEventByName(fragment, "startEvent");
		assertTrue(StartEvent.class.isInstance(startEvent));
		assertEquals(startEvent.getSpecialBehavior(), SpecialBehavior.NONE);

		AbstractEvent intermediateReceiveEvent = CaseModelTestHelper.getEventByName(fragment, "intermediateReceiveEvent");
		assertTrue(IntermediateCatchEvent.class.isInstance(intermediateReceiveEvent));
		assertEquals(intermediateReceiveEvent.getSpecialBehavior(), SpecialBehavior.MESSAGE_RECEIVE);

		AbstractEvent intermediateSendEvent = CaseModelTestHelper.getEventByName(fragment, "intermediateSendEvent");
		assertTrue(IntermediateThrowEvent.class.isInstance(intermediateSendEvent));
		assertEquals(intermediateSendEvent.getSpecialBehavior(), SpecialBehavior.MESSAGE_SEND);

		AbstractEvent intermediateTimerEvent = CaseModelTestHelper.getEventByName(fragment, "intermediateTimerEvent");
		assertTrue(IntermediateCatchEvent.class.isInstance(intermediateTimerEvent));
		assertEquals(intermediateTimerEvent.getSpecialBehavior(), SpecialBehavior.TIMER);

		AbstractEvent boundaryReceiveEvent = CaseModelTestHelper.getEventByName(fragment, "boundaryReceiveEvent");
		assertTrue(BoundaryEvent.class.isInstance(boundaryReceiveEvent));
		assertEquals(boundaryReceiveEvent.getSpecialBehavior(), SpecialBehavior.MESSAGE_RECEIVE);
		assertEquals(((BoundaryEvent) boundaryReceiveEvent).getAttachedToActivity().getName(), "bounded1");

		AbstractEvent boundaryTimerEvent = CaseModelTestHelper.getEventByName(fragment, "boundaryTimerEvent");
		assertTrue(BoundaryEvent.class.isInstance(boundaryTimerEvent));
		assertEquals(boundaryTimerEvent.getSpecialBehavior(), SpecialBehavior.TIMER);
		assertEquals(((BoundaryEvent) boundaryTimerEvent).getAttachedToActivity().getName(), "bounded2");
	}
}
