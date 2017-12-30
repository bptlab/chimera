package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.List;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public class ControlNodeParserHelper {
	private ControlNodeParserHelper() {
	}

	/**
	 * Parse id, name and sequence flow for a given xml control node.
	 * 
	 * @param controlNode
	 * @param xmlControlNode
	 * @param sfResolver
	 */
	public static void parseControlNode(AbstractControlNode controlNode, de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.AbstractControlNode xmlControlNode, SequenceFlowResolver sfResolver) {
		controlNode.setId(xmlControlNode.getId());
		controlNode.setName(xmlControlNode.getName());
		sfResolver.resolveIncomingSequenceFlow(xmlControlNode.getIncomingSequenceFlows(), controlNode);
		sfResolver.resolveOutgoingSequenceFlow(xmlControlNode.getOutgoingSequenceFlows(), controlNode);
	}

	/**
	 * Parse id, name, sequence flow and data flow for a given xml data control
	 * node.
	 * 
	 * @param dataControlNode
	 * @param xmlDataControlNode
	 * @param sfResolver
	 * @param dfResolver
	 */
	public static void parseDataControlNode(AbstractDataControlNode dataControlNode, de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.AbstractDataControlNode xmlDataControlNode, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		parseControlNode(dataControlNode, xmlDataControlNode, sfResolver);
		List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlDataControlNode.getIncomingDataNodeObjectReferences());
		DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
		dataControlNode.setPreCondition(preCondition);
		List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlDataControlNode.getOutgoingDataNodeObjectReferences());
		DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
		dataControlNode.setPostCondition(postCondition);
	}
}
