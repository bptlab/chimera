package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataAttributeJsonPath;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.DataNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.BoundaryEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.EndEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.StartEvent;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.MessageReceiveDefinition;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.SpecialBehavior;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.TimerDefinition;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
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
	 * @param parserHelper
	 */
	public static void parseEvents(BpmnFragment fragment, FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver, CaseModelParserHelper parserHelper) {
		fragment.setStartEvent(getStartEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setEndEvent(getEndEventFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setIntermediateCatchEvents(getIntermediateCatchEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver, parserHelper));
		fragment.setIntermediateThrowEvents(getIntermediateThrowEventsFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setBoundaryEvents(getBoundaryEventsFromXmlWrapper(fragXmlWrap, fragment.getActivities(), sfResolver, dfResolver));
		for (AbstractEvent event : fragment.getEvents()) {
			modifyMessageReceiveEventJsonMapping(event, parserHelper);
		}
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
		} else if (xmlEvent.hasMessageDefiniton() || event instanceof IntermediateThrowEvent) {
			if (event instanceof EndEvent || event instanceof IntermediateThrowEvent) {
				event.setSpecialBehavior(SpecialBehavior.MESSAGE_SEND);
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
	 * @param parserHelper
	 * @return List of IntermediaCatchEvents
	 */
	private static List<IntermediateCatchEvent> getIntermediateCatchEventsFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver, CaseModelParserHelper parserHelper) {
		List<IntermediateCatchEvent> intermediateCatchEventList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.IntermediateCatchEvent xmlIntermediateCatchEvent : fragXmlWrap.getIntermediateCatchEvents()) {
			IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
			parseEvent(intermediateCatchEvent, xmlIntermediateCatchEvent, sfResolver, dfResolver);

			modifyMessageReceiveEventJsonMapping(intermediateCatchEvent, parserHelper);
			intermediateCatchEventList.add(intermediateCatchEvent);
		}
		return intermediateCatchEventList;
	}

	/**
	 * If a {@link IntermediateCatchEvent} with an event query has a data object
	 * under some conditions all values of the received event will be copied to
	 * the data object. Therefore the mapping from {@link DataAttribute} to its
	 * JsonPath will be adjusted. The conditions are, the output control node
	 * needs to be an event class, the event class of the output DO needs to
	 * conform to the event type in the query (assume that the query is always
	 * of the form Select * from 'eventclass') and the attribute mapping for all
	 * attributes need to be empty (no JsonPath expression specified).
	 * 
	 * @param event
	 *            - which postconditon may be adjusted
	 * @param parserHelper
	 *            - that contains information about the casemodel
	 */
	private static void modifyMessageReceiveEventJsonMapping(AbstractEvent event, CaseModelParserHelper parserHelper) {
		if (event.hasPostCondition() && event.hasUniquePostCondition()
			&& event.getSpecialBehavior().equals(SpecialBehavior.MESSAGE_RECEIVE)) {

			MessageReceiveDefinition receiveDefinition = (MessageReceiveDefinition) event.getSpecialEventDefinition();
			Pattern p = Pattern.compile("Select \\* from (\\w+)\\b");
			Matcher m = p.matcher(receiveDefinition.getEventQuerry());
			
			if (!m.find()) {
				return;
			}

			int dataclassGroup = 1;
			String dataClassName = m.group(dataclassGroup);

			DataClass dataclass;
			try {
				dataclass = parserHelper.getDataClassByName(dataClassName);
			} catch (IllegalArgumentException e) {
				log.info(String.format("Dataclass %s in event querry of Catch Event %s does not exist.", dataClassName, event.getId()));
				return;
			}

			if (!dataclass.isEvent()) {
				return;
			}

			ConditionSet conditionSet = event.getPostCondition().getConditionSets().get(0);
			Optional<AtomicDataStateCondition> condition = conditionSet.getConditions().stream().filter(c -> c.getDataClassName().equals(dataClassName)).findFirst();

			if (!condition.isPresent()) {
				return;
			}

			DataNode dataNode = (DataNode) condition.get();
			String format = "$.%s";

			if (dataNode.getDataAttributeJsonPaths().isEmpty()) {
				List<DataAttributeJsonPath> modified = dataclass.getDataAttributes().stream()
																					.map(d -> new DataAttributeJsonPath(d, String.format(format, d.getName())))
																					.collect(Collectors.toList());

				dataNode.setDataAttributeJsonPaths(modified);
			}
		}
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
			IntermediateThrowEvent intermediateThrowEvent = new IntermediateThrowEvent();
			parseEvent(intermediateThrowEvent, xmlIntermediateThrowEvent, sfResolver, dfResolver);

			intermediateThrowEventList.add(intermediateThrowEvent);
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
