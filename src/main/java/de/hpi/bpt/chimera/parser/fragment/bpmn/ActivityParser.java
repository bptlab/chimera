package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanActivity;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.Task;

public class ActivityParser {

	private ActivityParser() {
	}

	/**
	 * Parse the Activities for a for a BPMN-Fragment out of the
	 * FragmentXmlWrapper.
	 * 
	 * @param fragment
	 * @param fragXmlWrap
	 * @param sfResolver
	 * @param dfResolver
	 */
	public static void parseActivities(BpmnFragment fragment, FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<Activity> activities = new ArrayList<>();
		activities.addAll(getHumanActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.setTasks(activities);
	}

	public static List<HumanActivity> getHumanActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<HumanActivity> activityList = new ArrayList<>();

		for (Task xmlTask : fragXmlWrap.getTasks()) {
			HumanActivity activity = new HumanActivity();
			activity.setId(xmlTask.getId());
			activity.setName(xmlTask.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlTask.getIncomingSequenceFlows(), activity);
			sfResolver.resolveOutgoingSequenceFlow(xmlTask.getOutgoingSequenceFlows(), activity);
			dfResolver.resolveIncomingDataFlow(xmlTask.getIncomingDataNodeObjectReferences(), activity);
			dfResolver.resolveOutgoingDataFlow(xmlTask.getOutgoingDataNodeObjectReferences(), activity);
			activityList.add(activity);
		}
		return activityList;
	}


}
