package de.hpi.bpt.chimera.parser.fragment.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.model.fragment.bpmn.activity.*;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.BpmnManualTask;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.BpmnUserTask;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.SendTask;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.Task;
import de.hpi.bpt.chimera.validation.NameValidation;

public class ActivityParser {
	private static final Logger log = Logger.getLogger(ActivityParser.class);

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

		List<AbstractActivity> userTasks = new ArrayList<>();
		userTasks.addAll(getUserTasksFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addActivities(userTasks);

		List<AbstractActivity> manualTasks = new ArrayList<>();
		manualTasks.addAll(getManualTasksFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addActivities(manualTasks);

		List<AbstractActivity> emptyActivities = new ArrayList<>();
		emptyActivities.addAll(getEmptyActivitiesFromXmlWrapper(fragXmlWrap, sfResolver, dfResolver));
		fragment.addActivities(emptyActivities);
	}

	public static List<HumanTask> getHumanActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<HumanTask> activityList = new ArrayList<>();

		for (Task xmlTask : fragXmlWrap.getTasks()) {
			HumanTask activity = new HumanTask();
			ControlNodeParserHelper.parseDataControlNode(activity, xmlTask, sfResolver, dfResolver);
			NameValidation.validateName(activity.getName());
			
			activity.setRole(xmlTask.getRole());
			activityList.add(activity);
		}
		return activityList;
	}

	public static List<EmailActivity> getEmailActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<EmailActivity> mailActivityList = new ArrayList<>();

		for (SendTask xmlSendTask : fragXmlWrap.getSendTasks()) {
			EmailActivity mailActivity = new EmailActivity();
			ControlNodeParserHelper.parseDataControlNode(mailActivity, xmlSendTask, sfResolver, dfResolver);
			NameValidation.validateName(mailActivity.getName());
			
			mailActivityList.add(mailActivity);
		}
		return mailActivityList;
	}

	private static List<WebServiceTask> getWebServiceTasksFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<WebServiceTask> webServiceTasks = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.WebServiceTask xmlWebTask : fragXmlWrap.getWebServiceTasks()) {
			WebServiceTask webServiceTask = new WebServiceTask();
			ControlNodeParserHelper.parseDataControlNode(webServiceTask, xmlWebTask, sfResolver, dfResolver);
			NameValidation.validateName(webServiceTask.getName());
			
			webServiceTask.setWebServiceUrl(xmlWebTask.getWebServiceUrl());
			webServiceTask.setWebServiceMethod(xmlWebTask.getWebServiceMethod());
			webServiceTask.setWebServiceBody(xmlWebTask.getWebServiceBody());
			webServiceTask.setWebServiceHeader(xmlWebTask.getWebServiceHeader());
			webServiceTask.setContentType(xmlWebTask.getContentType());
			webServiceTasks.add(webServiceTask);
		}
		return webServiceTasks;
	}

	private static List<UserTask> getUserTasksFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<UserTask> activityList = new ArrayList<>();

		for (BpmnUserTask xmlTask : fragXmlWrap.getUserTasks()) {
			UserTask userTask = new UserTask();
			ControlNodeParserHelper.parseDataControlNode(userTask, xmlTask, sfResolver, dfResolver);
			NameValidation.validateName(userTask.getName());
			userTask.setRole(xmlTask.getRole());
			activityList.add(userTask);
		}
		return activityList;
	}

	private static List<ManualTask> getManualTasksFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<ManualTask> activityList = new ArrayList<>();

		for (BpmnManualTask xmlTask : fragXmlWrap.getManualTasks()) {
			ManualTask manualTask = new ManualTask();
			ControlNodeParserHelper.parseDataControlNode(manualTask, xmlTask, sfResolver, dfResolver);
			NameValidation.validateName(manualTask.getName());
			manualTask.setRole(xmlTask.getRole());
			activityList.add(manualTask);
		}
		return activityList;
	}

	private static List<EmptyActivity> getEmptyActivitiesFromXmlWrapper(FragmentXmlWrapper fragXmlWrap, SequenceFlowResolver sfResolver, DataFlowResolver dfResolver) {
		List<EmptyActivity> activityList = new ArrayList<>();

		for (de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.EmptyActivity xmlTask : fragXmlWrap.getEmptyActivities()) {
			EmptyActivity emptyActivity = new EmptyActivity();
			ControlNodeParserHelper.parseDataControlNode(emptyActivity, xmlTask, sfResolver, dfResolver);
			activityList.add(emptyActivity);
		}
		return activityList;
	}
}
