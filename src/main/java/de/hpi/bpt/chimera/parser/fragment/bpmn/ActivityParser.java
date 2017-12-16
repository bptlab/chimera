package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.AbstractActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.EmailActivity;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.HumanTask;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.WebServiceTask;
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
		List<AbstractActivity> humanActivities = new ArrayList<>();
		humanActivities.addAll(getHumanActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addTasks(humanActivities);

		List<AbstractActivity> mailActivities = new ArrayList<>();
		mailActivities.addAll(getEmailActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addTasks(mailActivities);
		
		List<AbstractActivity> webServiceTasks = new ArrayList<>();
		webServiceTasks.addAll(getWebServiceTasksFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addTasks(webServiceTasks);
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
			List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlSendTask.getIncomingDataNodeObjectReferences());
			DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
			mailActivity.setPreCondition(preCondition);
			List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlSendTask.getOutgoingDataNodeObjectReferences());
			DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
			mailActivity.setPostCondition(postCondition);

			mailActivityList.add(mailActivity);
		}
		return mailActivityList;
	}

	private static List<WebServiceTask> getWebServiceTasksFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<WebServiceTask> webServiceTasks = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.WebServiceTask xmlWebTask : fragXmlWrap.getWebServiceTasks()) {
			WebServiceTask webServiceTask = new WebServiceTask();
			webServiceTask.setId(xmlWebTask.getId());
			webServiceTask.setName(xmlWebTask.getName());
			sfResolver.resolveIncomingSequenceFlow(xmlWebTask.getIncomingSequenceFlows(), webServiceTask);
			sfResolver.resolveOutgoingSequenceFlow(xmlWebTask.getOutgoingSequenceFlows(), webServiceTask);
			List<AtomicDataStateCondition> availableInputConditions = dfResolver.resolveDataNodeReferences(xmlWebTask.getIncomingDataNodeObjectReferences());
			DataStateCondition preCondition = dfResolver.parseDataStateCondition(availableInputConditions);
			webServiceTask.setPreCondition(preCondition);
			List<AtomicDataStateCondition> availableOutputConditions = dfResolver.resolveDataNodeReferences(xmlWebTask.getOutgoingDataNodeObjectReferences());
			DataStateCondition postCondition = dfResolver.parseDataStateCondition(availableOutputConditions);
			webServiceTask.setPostCondition(postCondition);

			webServiceTask.setWebServiceUrl(xmlWebTask.getWebServiceUrl());
			webServiceTask.setWebServiceMethod(xmlWebTask.getWebServiceMethod());
			webServiceTask.setWebServiceBody(xmlWebTask.getWebServiceBody());
			webServiceTasks.add(webServiceTask);
		}
		return webServiceTasks;
	}
}
