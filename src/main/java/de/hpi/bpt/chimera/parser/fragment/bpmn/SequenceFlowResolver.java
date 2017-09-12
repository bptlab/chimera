package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.bpmn.SequenceFlowAssociation;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.SequenceFlow;

public class SequenceFlowResolver {
	/**
	 * Map from Id of SequenceFlowAssociation to SequenceFlowAssociation.
	 */
	Map<String, SequenceFlowAssociation> associationMap = new HashMap<>();

	/**
	 * Creates a Resolver for the SequenceFlows of a certain Fragment.
	 * 
	 * @param fragXmlWrap
	 */
	public SequenceFlowResolver(FragmentXmlWrapper fragXmlWrap) {
		for (SequenceFlow sfWrap : fragXmlWrap.getSequenceFlow()) {
			SequenceFlowAssociation sfa = new SequenceFlowAssociation();
			sfa.setId(sfWrap.getId());
			sfa.setCondition(sfWrap.getCondition());
			associationMap.put(sfWrap.getId(), sfa);
		}
	}

	/**
	 * Resolves the incoming SequenceFlows for a certain ControlNode and sets
	 * them.
	 * 
	 * @param incomingSequenceFlows
	 * @param controlNode
	 */
	public void resolveIncomingSequenceFlow(List<String> incomingSequenceFlows, AbstractControlNode controlNode) {
		List<SequenceFlowAssociation> sequenceFlowAssociations = new ArrayList<>();
		for (String id : incomingSequenceFlows) {
			if (associationMap.containsKey(id)) {
				SequenceFlowAssociation sfa = associationMap.get(id);
				sfa.setTargetRef(controlNode);
				sequenceFlowAssociations.add(sfa);
			}
		}
		controlNode.setIncomingControlNodes(sequenceFlowAssociations);
	}

	/**
	 * Resolves the outgoing SequenceFlows for a certain ControlNode and sets
	 * them.
	 * 
	 * @param outgoingSequenceFlows
	 * @param controlNode
	 */
	public void resolveOutgoingSequenceFlow(List<String> outgoingSequenceFlows, AbstractControlNode controlNode) {
		List<SequenceFlowAssociation> sequenceFlowAssociations = new ArrayList<>();
		for (String id : outgoingSequenceFlows) {
			if (associationMap.containsKey(id)) {
				SequenceFlowAssociation sfa = associationMap.get(id);
				sfa.setSourceRef(controlNode);
				sequenceFlowAssociations.add(sfa);
			}
		}
		controlNode.setOutgoingControlNodes(sequenceFlowAssociations);
	}

	/**
	 * Get all SequenceFlowAssociations that were resolved.
	 * 
	 * @return List of SequenceFlowAssociation
	 * @throws UnresolvedSequenceFlowException
	 */
	public List<SequenceFlowAssociation> getResolvedSequenceFlowAssociations() throws UnresolvedSequenceFlowException {
		List<SequenceFlowAssociation> sfaList = new ArrayList<>();

		for (Map.Entry<String, SequenceFlowAssociation> entry : associationMap.entrySet()) {
			if (entry.getValue().getSourceRef() != null && entry.getValue().getTargetRef() != null)
				sfaList.add(entry.getValue());
			else
				throw new UnresolvedSequenceFlowException("there is at least one Sequence Flow Association which ins't attached to a Controlnode on both sides - id: " + entry.getKey());
		}

		return sfaList;
	}

}
