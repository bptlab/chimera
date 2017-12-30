package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.TimerEvent;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;

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
		fragment.setIntermediateCatchEvents(getIntermediateCatchEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setTimerEvents(getTimerEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setBoundaryEvents(getBoundaryEventsFromXmlWrapper(fragXmlWrap, fragment.getActivities(), sfResolver, dfResolver));
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
		de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.StartEvent xmlStartEvent = fragXmlWrap.getStartEvent();
		ControlNodeParserHelper.parseDataControlNode(startEvent, xmlStartEvent, sfResolver, dfResolver);
		startEvent.setEventQuerry(xmlStartEvent.getEventQuery());
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
		ControlNodeParserHelper.parseDataControlNode(endEvent, fragXmlWrap.getEndEvent(), sfResolver, dfResolver);
		return endEvent;
	}

	/**
	 * Get the BoundaryEvents of a Fragment.
	 * 
	 * @param fragXmlWrap
	 * @param activities
	 * @param sfResolver
	 * @param dfResolver
	 * @return List of BoundaryEvents
	 */
	private static List<BoundaryEvent> getBoundaryEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, List<AbstractActivity> activities, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<BoundaryEvent> boundaryEventList = new ArrayList<>();

		Map<String, AbstractActivity> activityIdToActivity = new HashMap<>();
		for (AbstractActivity activity : activities) {
			activityIdToActivity.put(activity.getId(), activity);
		}

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.BoundaryEvent xmlBoundaryEvent : fragXmlWrap.getBoundaryEvents()) {
			BoundaryEvent boundaryEvent = new BoundaryEvent();
			ControlNodeParserHelper.parseDataControlNode(boundaryEvent, xmlBoundaryEvent, sfResolver, dfResolver);
			boundaryEvent.setEventQuerry(xmlBoundaryEvent.getEventQuery());

			if (xmlBoundaryEvent.hasAttachedToRef()) {
				String attachedActivityId = xmlBoundaryEvent.getAttachedToRef();
				if (activityIdToActivity.containsKey(attachedActivityId)) {
					AbstractActivity attachedToActivity = activityIdToActivity.get(attachedActivityId);
					attachedToActivity.addAttachedBoundaryEvent(boundaryEvent);
					boundaryEvent.setAttachedToActivity(attachedToActivity);
				} else {
					throw new IllegalArgumentException(String.format("Referred activityId at BoundaryEvent: %s does not exist.", attachedActivityId));
				}
			}

			boundaryEventList.add(boundaryEvent);
		}

		return boundaryEventList;
	}

	// TODO Make use of the inheritance hierarchy and pass all
	// IntermediateCatchEvents at once and then refine the to TimerEvents,
	// MessageCatchEvents and so on.
	/**
	 * Get the TimerEvents of a Fragment.
	 * 
	 * @param fragXmlWrap
	 * @param activities
	 * @param sfResolver
	 * @param dfResolver
	 * @return List of TimerEvents
	 */
	private static List<TimerEvent> getTimerEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<TimerEvent> timerEventList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateCatchEvent xmlIntermediateCatchEvent : fragXmlWrap.getIntermediateCatchEvents()) {
			if (xmlIntermediateCatchEvent.getTimer() != null) {
				log.info("parsing TimerEvent");
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

	private static List<IntermediateCatchEvent> getIntermediateCatchEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<IntermediateCatchEvent> intermediateCatchEventList = new ArrayList<>();
		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateCatchEvent xmlIntermediateCatchEvent : fragXmlWrap.getIntermediateCatchEvents()) {
			log.info("parsing intermediateCatchEvents");
			if (xmlIntermediateCatchEvent.getTimer() == null) {
				IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
				intermediateCatchEvent.setId(xmlIntermediateCatchEvent.getId());
				intermediateCatchEvent.setName(xmlIntermediateCatchEvent.getName());
				log.info("Xml-EventQuery" + xmlIntermediateCatchEvent.getEventQuery());
				intermediateCatchEvent.setEventQuery(xmlIntermediateCatchEvent.getEventQuery());
				log.info("Eventquery parsed:" + intermediateCatchEvent.getEventQuery());
				sfResolver.resolveIncomingSequenceFlow(xmlIntermediateCatchEvent.getIncomingSequenceFlows(), intermediateCatchEvent);
				sfResolver.resolveOutgoingSequenceFlow(xmlIntermediateCatchEvent.getOutgoingSequenceFlows(), intermediateCatchEvent);
				List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlIntermediateCatchEvent.getIncomingDataNodeObjectReferences());
				DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
				intermediateCatchEvent.setPreCondition(preCondition);
				List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlIntermediateCatchEvent.getOutgoingDataNodeObjectReferences());
				DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
				intermediateCatchEvent.setPostCondition(postCondition);
				intermediateCatchEventList.add(intermediateCatchEvent);
			}
		}
		return intermediateCatchEventList;
	}
}
