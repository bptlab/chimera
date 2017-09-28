package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.gateway.ParallelGateway;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class GatewayParser {
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
}
