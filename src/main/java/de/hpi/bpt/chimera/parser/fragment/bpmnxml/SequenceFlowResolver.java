package de.hpi.bpt.chimera.parser.fragment.bpmnxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.bpt.chimera.jcomparser.jaxb.FragmentXmlWrapper;
import de.hpi.bpt.chimera.jcomparser.jaxb.SequenceFlow;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.AbstractControlNode;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.SequenceFlowAssociation;

public class SequenceFlowResolver {
	Map<String, SequenceFlowAssociation> associationMap = new HashMap();

	public SequenceFlowResolver(FragmentXmlWrapper fragXmlWrap) {
		for (SequenceFlow sfWrap : fragXmlWrap.getSequenceFlow()) {
			SequenceFlowAssociation sfa = new SequenceFlowAssociation();
			sfa.setId(sfWrap.getId());
			sfa.setCondition(sfWrap.getCondition());
			associationMap.put(sfWrap.getId(), sfa);
		}
	}

	public void resolveIncomingSequenceFlow(List<String> IncomingSequenceFlows, AbstractControlNode controlNode) {
		List<SequenceFlowAssociation> sfaList = new ArrayList<>();
		for (String id : IncomingSequenceFlows) {
			SequenceFlowAssociation sfa = associationMap.get(id);
			sfa.setTargetRef(controlNode);
			sfaList.add(sfa);
		}
		controlNode.setIncoming(sfaList);
	}

	public void resolveOutgoingSequenceFlow(List<String> OutgoingSequenceFlows, AbstractControlNode controlNode) {
		List<SequenceFlowAssociation> sfaList = new ArrayList<>();
		for (String id : OutgoingSequenceFlows) {
			SequenceFlowAssociation sfa = associationMap.get(id);
			sfa.setSourceRef(controlNode);
			sfaList.add(sfa);
		}
		controlNode.setOutgoing(sfaList);
	}

	public List<SequenceFlowAssociation> getResolvedSequenceFlowAssociations() throws UnresolvedSequenceFlowException {
		List<SequenceFlowAssociation> sfaList = new ArrayList<>();

		for (Map.Entry<String, SequenceFlowAssociation> entry : associationMap.entrySet()) {
			if (entry.getValue().getSourceRef() != null && entry.getValue().getTargetRef() != null)
				sfaList.add(entry.getValue());
			else
				throw new UnresolvedSequenceFlowException("there is at least one Sequence Flow Association which ins't attached to a Controlnode on both sides.");
		}

		return sfaList;
	}

}
