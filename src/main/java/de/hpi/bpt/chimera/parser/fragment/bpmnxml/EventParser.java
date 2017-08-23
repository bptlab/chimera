package de.hpi.bpt.chimera.parser.fragment.bpmnxml;

import de.hpi.bpt.chimera.jcomparser.jaxb.FragmentXmlWrapper;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.EndEvent;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.StartEvent;

public class EventParser {

	public static StartEvent getStartEventFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		StartEvent start = new StartEvent();
		start.setId(fragXmlWrap.getStartEvent().getId());
		sfResolver.resolveOutgoingSequenceFlow(fragXmlWrap.getStartEvent().getOutgoing(), start);
		return start;
	}

	public static EndEvent getEndEventFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		EndEvent end = new EndEvent();
		end.setId(fragXmlWrap.getEndEvent().getId());
		sfResolver.resolveIncomingSequenceFlow(fragXmlWrap.getEndEvent().getIncoming(), end);
		return end;
	}

}
