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
		fragment.addActivities(humanActivities);

		List<AbstractActivity> mailActivities = new ArrayList<>();
		mailActivities.addAll(getEmailActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addActivities(mailActivities);
		
		List<AbstractActivity> webServiceTasks = new ArrayList<>();
		webServiceTasks.addAll(getWebServiceTasksFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addActivities(webServiceTasks);
	}

	public static List<HumanTask> getHumanActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<HumanTask> activityList = new ArrayList<>();

		for (Task xmlTask : fragXmlWrap.getTasks()) {
			HumanTask activity = new HumanTask();
			ControlNodeParserHelper.parseDataControlNode(activity, xmlTask, sfResolver, dfResolver);
			activityList.add(activity);
		}
		return activityList;
	}

	public static List<EmailActivity> getEmailActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<EmailActivity> mailActivityList = new ArrayList<>();

		for (SendTask xmlSendTask : fragXmlWrap.getSendTasks()) {
			EmailActivity mailActivity = new EmailActivity();
			ControlNodeParserHelper.parseDataControlNode(mailActivity, xmlSendTask, sfResolver, dfResolver);
			mailActivityList.add(mailActivity);
		}
		return mailActivityList;
	}

	private static List<WebServiceTask> getWebServiceTasksFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<WebServiceTask> webServiceTasks = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.WebServiceTask xmlWebTask : fragXmlWrap.getWebServiceTasks()) {
			WebServiceTask webServiceTask = new WebServiceTask();
			ControlNodeParserHelper.parseDataControlNode(webServiceTask, xmlWebTask, sfResolver, dfResolver);

			webServiceTask.setWebServiceUrl(xmlWebTask.getWebServiceUrl());
			webServiceTask.setWebServiceMethod(xmlWebTask.getWebServiceMethod());
			webServiceTask.setWebServiceBody(xmlWebTask.getWebServiceBody());
			webServiceTask.setWebServiceHeader(xmlWebTask.getWebServiceHeader());
			webServiceTasks.add(webServiceTask);
		}
		return webServiceTasks;
	}
}
