package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.EventBasedGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ExclusiveGateway;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class GatewayParser {
	// TODO Remove the code duplication in this Class. Use one type generic
	// Method instead of the 3 Event parsing functions which only differ by
	// their type.

	private GatewayParser() {
	}

	/**
	 * Parse the Gateways for a for a BPMN-Fragment out of the
	 * FragmentXmlWrapper.
	 * 
	 * @param fragment
	 * @param fragXmlWrap
	 * @param sfResolver
	 */
	public static void parseGateways(BpmnFragment fragment, FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		fragment.setParallelGateways(getParallelGatewayFromXmlWrapper(fragXmlWrap, sfResolver));
		fragment.setExclusiveGateways(getExclusiveGatewayFromXmlWrapper(fragXmlWrap, sfResolver));
		fragment.setEventBasedGateways(getEventBasedGatewayFromXmlWrapper(fragXmlWrap, sfResolver));
	}

	/**
	 * parses all ParallelGateways from a given XML-String and returns the as a
	 * ArrayList
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @return List<ParallelGateway> A List of all ParallelGateways of the
	 *         fragment given as XML.
	 */
	public static List<ParallelGateway> getParallelGatewayFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		List<ParallelGateway> parallelGatewayList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.ParallelGateway xmlParallelGateway : fragXmlWrap.getAndGateways()) {
			ParallelGateway parallelGateway = new ParallelGateway();
			parallelGateway.setId(xmlParallelGateway.getId());
			parallelGateway.setName(xmlParallelGateway.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlParallelGateway.getIncomingSequenceFlows(), parallelGateway);
			sfResolver.resolveOutgoingSequenceFlow(xmlParallelGateway.getOutgoingSequenceFlows(), parallelGateway);
			parallelGatewayList.add(parallelGateway);
		}
		return parallelGatewayList;
	}

	/**
	 * parses all ExclusiveGateways from a given XML-String and returns the as a
	 * ArrayList
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @return List<ExclusiveGateway> A List of all ExclusiveGateways of the
	 *         fragment given as XML.
	 */
	public static List<ExclusiveGateway> getExclusiveGatewayFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		List<ExclusiveGateway> exclusiveGatewayList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.ExclusiveGateway xmlExclusiveGateway : fragXmlWrap.getXorGateways()) {
			ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
			exclusiveGateway.setId(xmlExclusiveGateway.getId());
			exclusiveGateway.setName(xmlExclusiveGateway.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlExclusiveGateway.getIncomingSequenceFlows(), exclusiveGateway);
			sfResolver.resolveOutgoingSequenceFlow(xmlExclusiveGateway.getOutgoingSequenceFlows(), exclusiveGateway);
			exclusiveGatewayList.add(exclusiveGateway);
		}
		return exclusiveGatewayList;
	}

	/**
	 * parses all EventBasedGateways from a given XML-String and returns the as
	 * a ArrayList
	 * 
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @return List<EventBasedGateway> A List of all EventBasedGateways of the
	 *         fragment given as XML.
	 */
	public static List<EventBasedGateway> getEventBasedGatewayFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		List<EventBasedGateway> eventBasedGatewayList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.EventBasedGateway xmlEventBasedGateway : fragXmlWrap.getEventBasedGateways()) {
			EventBasedGateway eventBasedGateway = new EventBasedGateway();
			eventBasedGateway.setId(xmlEventBasedGateway.getId());
			sfResolver.resolveIncomingSequenceFlow(xmlEventBasedGateway.getIncomingSequenceFlows(), eventBasedGateway);
			sfResolver.resolveOutgoingSequenceFlow(xmlEventBasedGateway.getOutgoingSequenceFlows(), eventBasedGateway);
			eventBasedGatewayList.add(eventBasedGateway);
		}
		return eventBasedGatewayList;
	}
}
