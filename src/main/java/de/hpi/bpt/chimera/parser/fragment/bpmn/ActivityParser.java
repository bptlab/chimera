package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.Activity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.SendTask;
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
		List<Activity> humanActivities = new ArrayList<>();
		humanActivities.addAll(getHumanActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addTasks(humanActivities);

		List<Activity> mailActivities = new ArrayList<>();
		mailActivities.addAll(getEmailActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addTasks(mailActivities);
	}

	public static List<HumanTask> getHumanActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<HumanTask> activityList = new ArrayList<>();

		for (Task xmlTask : fragXmlWrap.getTasks()) {
			HumanTask activity = new HumanTask();
			activity.setId(xmlTask.getId());
			activity.setName(xmlTask.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlTask.getIncomingSequenceFlows(), activity);
			sfResolver.resolveOutgoingSequenceFlow(xmlTask.getOutgoingSequenceFlows(), activity);
			List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlTask.getIncomingDataNodeObjectReferences());
			DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
			activity.setPreCondition(preCondition);
			List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlTask.getOutgoingDataNodeObjectReferences());
			DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
			activity.setPostCondition(postCondition);
			activityList.add(activity);
		}
		return activityList;
	}

	public static List<EmailActivity> getEmailActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<EmailActivity> mailActivityList = new ArrayList<>();

		for (SendTask xmlSendTask : fragXmlWrap.getSendTasks()) {
			EmailActivity mailActivity = new EmailActivity();
			mailActivity.setId(xmlSendTask.getId());
			mailActivity.setName(xmlSendTask.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlSendTask.getIncomingSequenceFlows(), mailActivity);
			sfResolver.resolveOutgoingSequenceFlow(xmlSendTask.getOutgoingSequenceFlows(), mailActivity);
			dfResolver.resolveIncomingDataFlow(xmlSendTask.getIncomingDataNodeObjectReferences(), mailActivity);
			dfResolver.resolveOutgoingDataFlow(xmlSendTask.getOutgoingDataNodeObjectReferences(), mailActivity);
			mailActivityList.add(mailActivity);
		}
		return mailActivityList;
	}


}
