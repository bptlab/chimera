package de.hpi.bpt.chimera.parser.fragment.bpmnxml;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.jcomparser.jaxb.FragmentXmlWrapper;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.Activity;

public class ActivityParser {

	public static List<Activity> getActitityFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver) {
		List<Activity> activityList = new ArrayList();

		for (de.hpi.bpt.chimera.jcomparser.jaxb.Task xmlTask : fragXmlWrap.getTasks()) {
			Activity activity = new Activity();
			activity.setId(xmlTask.getId());
			sfResolver.resolveIncomingSequenceFlow(xmlTask.getIncoming(), activity);
			sfResolver.resolveOutgoingSequenceFlow(xmlTask.getOutgoing(), activity);
			activityList.add(activity);
		}
		return activityList;
	}

}
