package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateCatchEvent;

public class EventParser {

	private static final Logger log = Logger.getLogger(EventParser.class);

	private EventParser() {
	}

	/**
	 * Parse the Events for a BPMN-Fragment out of the FragmentXmlWrapper.
	 * 
	 * @param fragment
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 */
	public static void parseEvents(BpmnFragment fragment, FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		fragment.setStartEvent(getStartEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setEndEvent(getEndEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setTimerEvents(getTimerEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
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
		List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(fragXmlWrap.getStartEvent().getOutgoingDataNodeObjectReferences());
		DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
		startEvent.setPostCondition(postCondition);
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
		List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(fragXmlWrap.getEndEvent().getIncomingDataNodeObjectReferences());
		DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
		endEvent.setPreCondition(preCondition);
		return endEvent;
	}


	// TODO Make use of the inheritance hierarchy and pass all
	// IntermediateCatchEvents at once and then refine the to TimerEvents,
	// MessageCatchEvents and so on.
	/**
	 * Get the TimerEvents of a Fragment.
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 * @return List<TimerEvent>
	 */
	private static List<TimerEvent> getTimerEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<TimerEvent> timerEventList = new ArrayList<>();

		for (IntermediateCatchEvent xmlIntermediateCatchEvent : fragXmlWrap.getIntermediateCatchEvents()) {
			if (xmlIntermediateCatchEvent.getTimer() != null) {
				TimerEvent timerEvent = new TimerEvent();
				timerEvent.setId(xmlIntermediateCatchEvent.getId());
				timerEvent.setName(xmlIntermediateCatchEvent.getName());
				timerEvent.setTimerDuration(xmlIntermediateCatchEvent.getTimer().getTimerDuration());
				sfResolver.resolveIncomingSequenceFlow(xmlIntermediateCatchEvent.getIncomingSequenceFlows(), timerEvent);
				sfResolver.resolveOutgoingSequenceFlow(xmlIntermediateCatchEvent.getOutgoingSequenceFlows(), timerEvent);
				List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlIntermediateCatchEvent.getIncomingDataNodeObjectReferences());
				DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
				timerEvent.setPreCondition(preCondition);
				List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlIntermediateCatchEvent.getOutgoingDataNodeObjectReferences());
				DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
				timerEvent.setPostCondition(postCondition);
				timerEventList.add(timerEvent);
			}
		}
		return timerEventList;
	}

}
