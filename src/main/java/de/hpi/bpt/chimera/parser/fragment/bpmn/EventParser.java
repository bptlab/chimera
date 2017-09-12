package de.hpi.bpt.chimera.parser.fragment.bpmn;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.StartEvent;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class EventParser {

	private EventParser() {
	}

	/**
	 * Parse the Events for a for a BPMN-Fragment out of the FragmentXmlWrapper.
	 * 
	 * @param fragment
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 */
	public static void parseEvents(BpmnFragment fragment, FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		fragment.setStartEvent(getStartEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setEndEvent(getEndEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
	}

	/**
	 * Get the StartEvent of a Fragment.
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 * @return StartEvent
	 */
	private static StartEvent getStartEventFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(fragXmlWrap.getStartEvent().getId());
		startEvent.setEventQuerry(fragXmlWrap.getStartEvent().getEventQuery());
		sfResolver.resolveOutgoingSequenceFlow(fragXmlWrap.getStartEvent().getOutgoingSequenceFlows(), startEvent);
		dfResolver.resolveOutgoingDataFlow(fragXmlWrap.getStartEvent().getOutgoingDataNodeObjectReferences(), startEvent);
		return startEvent;
	}

	/**
	 * Get the EndEvent of a Fragment.
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 * @return EndEvent
	 */
	private static EndEvent getEndEventFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(fragXmlWrap.getEndEvent().getId());
		sfResolver.resolveIncomingSequenceFlow(fragXmlWrap.getEndEvent().getIncomingSequenceFlows(), endEvent);
		dfResolver.resolveIncomingDataFlow(fragXmlWrap.getEndEvent().getIncomingDataNodeObjectReferences(), endEvent);
		return endEvent;
	}

}
