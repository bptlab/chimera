package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.MessageReceiveDefinition;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.TimerDefinition;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateCatchEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.IntermediateThrowEvent;

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
		fragment.setIntermediateThrowEvents(getIntermediateThrowEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setBoundaryEvents(getBoundaryEventsFromXmlWrapper(fragXmlWrap, fragment.getActivities(), sfResolver, dfResolver));
	}

	/**
	 * Parse an Event and its behavior.
	 * 
	 * @param event
	 * @param xmlEvent
	 * @param sfResolver
	 * @param dfResolver
	 */
	private static void parseEvent(AbstractEvent event, de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.AbstractEvent xmlEvent, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		ControlNodeParserHelper.parseDataControlNode(event, xmlEvent, sfResolver, dfResolver);
		event.setSpecialBehavior(SpecialBehavior.NONE);

		if (xmlEvent.hasTimerDefinition()) {
			event.setSpecialBehavior(SpecialBehavior.TIMER);
			TimerDefinition timerDefiniton = new TimerDefinition();
			timerDefiniton.setTimerDuration(xmlEvent.getTimerDefinition().getTimerDuration());
			event.setSpecialEventDefinition(timerDefiniton);
		} else if (xmlEvent.hasMessageDefiniton()) {
			if (event instanceof EndEvent || event instanceof IntermediateThrowEvent) {
				event.setSpecialBehavior(SpecialBehavior.MESSAGE_SENT);
			} else if (event instanceof StartEvent || event instanceof IntermediateCatchEvent || event instanceof BoundaryEvent) {
				event.setSpecialBehavior(SpecialBehavior.MESSAGE_RECEIVE);
				MessageReceiveDefinition receiveDefinition = new MessageReceiveDefinition();
				receiveDefinition.setEventQuerry(xmlEvent.getEventQuery());
				event.setSpecialEventDefinition(receiveDefinition);
			}
		}
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
		parseEvent(startEvent, fragXmlWrap.getStartEvent(), sfResolver, dfResolver);
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
		parseEvent(endEvent, fragXmlWrap.getEndEvent(), sfResolver, dfResolver);
		return endEvent;
	}

	/**
	 * Get the IntermediateCatchEvents of a fragment.
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 * @return List of IntermediaCatchEvents
	 */
	private static List<IntermediateCatchEvent> getIntermediateCatchEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<IntermediateCatchEvent> intermediateCatchEventList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateCatchEvent xmlIntermediateCatchEvent : fragXmlWrap.getIntermediateCatchEvents()) {
			IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
			parseEvent(intermediateCatchEvent, xmlIntermediateCatchEvent, sfResolver, dfResolver);

			intermediateCatchEventList.add(intermediateCatchEvent);
		}
		return intermediateCatchEventList;
	}

	/**
	 * Get the IntermediateThrowEvents of a fragment.
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 * @return List of IntermediateThrowEvents
	 */
	private static List<IntermediateThrowEvent> getIntermediateThrowEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<IntermediateThrowEvent> intermediateThrowEventList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateThrowEvent xmlIntermediateThrowEvent : fragXmlWrap.getIntermediateThrowEvents()) {
			IntermediateThrowEvent intermediateCatchEvent = new IntermediateThrowEvent();
			parseEvent(intermediateCatchEvent, xmlIntermediateThrowEvent, sfResolver, dfResolver);

			intermediateThrowEventList.add(intermediateCatchEvent);
		}
		return intermediateThrowEventList;
	}

	/**
	 * Get the BoundaryEvents of a Fragment. Retrieve the attached activity by
	 * its id.
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
			parseEvent(boundaryEvent, xmlBoundaryEvent, sfResolver, dfResolver);

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
}
