package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.hpi.bpt.chimera.execution.Case;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.TimerEventBehavior;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.MessageReceiveDefinition;
import de.hpi.bpt.chimera.util.PropertyLoader;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class EventDispatcher {
	// TODO: will we need something like EventLog?

	private static String url = PropertyLoader.getProperty("unicorn.url");
	private static final String REST_DEPLOY_PATH = PropertyLoader.getProperty("unicorn.path.deploy");
	private static final String REST_PATH = PropertyLoader.getProperty("unicorn.path.query.rest");

	private static final String SELF_DEPLOY_URL = PropertyLoader.getProperty("chimera.url") + PropertyLoader.getProperty("chimera.path.deploy");
	private static final String SELF_PATH_NODES = "%s/api/eventdispatcher/scenario/%s/instance/%s/events/%s";
	private static final String SELF_PATH_CASESTART = "%s/api/eventdispatcher/scenario/%s/casestart/%s";
	private static final String NOTIFICATION_ERROR_ID = "-1";

	private static Logger log = Logger.getLogger(EventDispatcher.class);

	private EventDispatcher() {
	}

	/**
	 * Register an {@link CaseStartTrigger} for an specific {@link CaseModel} by
	 * sending it to Unicorn. Set the returning notification rule id for
	 * de-registration in the case start trigger.
	 * 
	 * @param cm
	 *            - CaseModel the caseStartTrigger will instantiate
	 * @param caseStartTrigger
	 *            - CaseStartTrigger that will be registered
	 */
	public static void registerCaseStartEvent(CaseModel cm, CaseStartTrigger caseStartTrigger) {
		final String requestId = caseStartTrigger.getId();
		String notificationPath = String.format(SELF_PATH_CASESTART, SELF_DEPLOY_URL, cm.getId(), requestId);
		String notificationRuleId = sendQueryToEventService(caseStartTrigger.getQueryExecutionPlan(), notificationPath);
		caseStartTrigger.setNotificationRuleId(notificationRuleId);
	}

	/**
	 * Register an event instance in Unicorn by its {@link TimerEventBehavior}.
	 * Therefore create a new timer event job.
	 * 
	 * @param timerBehavior
	 *            - TimerEventBehavior of the EventInstance to be registered
	 */
	public static void registerTimerEvent(TimerEventBehavior timerBehavior) {
		AbstractEventInstance event = timerBehavior.getEventInstance();
		Date terminationDate = timerBehavior.getTerminationDate();
		assert (terminationDate.after(new Date())) : "Traveling back in time is not implemented yet, see feature request #CM-(-243)";
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			Scheduler sched = sf.getScheduler();
			JobDetail job = createJob(event, timerBehavior);
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().startAt(terminationDate).build();
			sched.start();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Creates a Quarz job for an {@link AbstractEventInstance} and register the
	 * jobKey at the {@link TimerEventBehavior} of the EventInstance.
	 * 
	 * @param event
	 *            - the EventInstance which should be registered as TimerEvent
	 * @param timerBehavior
	 *            - the {@link EventBehavior} of that EventInstance
	 * 
	 * @return a Quarz job
	 */
	private static JobDetail createJob(AbstractEventInstance event, TimerEventBehavior timerBehavior) {
		JobDetail timeEventJob = newJob(TimeEventJob.class).usingJobData("CaseModelId", event.getCaseExecutioner().getCaseModel().getId()).usingJobData("CaseId", event.getFragmentInstance().getCase().getId()).usingJobData("ControlNodeInstanceId", event.getId()).build();
		timerBehavior.setJobKey(timeEventJob.getKey());
		return timeEventJob;
	}

	/**
	 * Register an event instance in Unicorn by its
	 * {@link MessageReceiveEventBehavior}. The notificationRuleId is set in the
	 * message receive behavior and the message receive behavior will be stored
	 * in the case executioner of the event instance.
	 * 
	 * @param receiveBehavior
	 *            - MessageReceiveEventBehavior of the EventInstance to be
	 *            registered
	 */
	public static void registerReceiveEvent(MessageReceiveEventBehavior receiveBehavior) {
		log.info("trying to register an Event at unicorn");
		AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
		final String requestId = eventInstance.getId();
		String query = insertAttributesIntoQueryString(receiveBehavior);

		CaseExecutioner caseExecutioner = eventInstance.getCaseExecutioner();
		String cmId = caseExecutioner.getCaseModel().getId();
		String caseId = caseExecutioner.getCase().getId();
		String notificationPath = String.format(SELF_PATH_NODES, SELF_DEPLOY_URL, cmId, caseId, requestId);

		String notificationRuleId = sendQueryToEventService(query, notificationPath);
		receiveBehavior.setNotificationRule(notificationRuleId);
		
		if (!notificationRuleId.equals(NOTIFICATION_ERROR_ID)) {
			caseExecutioner.addRegisteredEventBehavior(receiveBehavior);
			log.info(String.format("Registered event with id %s and eventQuery %s at unicorn with the requestId %s, getting back %s as NotificationRule", eventInstance.getId(), query, requestId, notificationRuleId));
		}
	}

	/**
	 * Replace the variable expressions in the query of the
	 * {@link MessageReceiveDefinition}.
	 * 
	 * @param receiveBehavior
	 * @return String with replaced variable expressions.
	 */
	private static String insertAttributesIntoQueryString(MessageReceiveEventBehavior receiveBehavior) {
		MessageReceiveDefinition messageDefinition = receiveBehavior.getMessageDefinition();
		String queryString = messageDefinition.getEventQuerry();
		return receiveBehavior.getEventInstance().replaceVariableExpressions(queryString);
	}

	/**
	 * Register an event query in Unicorn by sending it to Unicorn. Unicorn will
	 * respond at the specified notificationPath if the query matches an Event.
	 * 
	 * @param rawQuery
	 *            - query with possible escaped symbols
	 * @param notificationPath
	 *            - path used for notification if the query matches an Event in
	 *            Unicorn
	 * @return id for notification. Equals the id the query is registered with
	 *         in Unicorn.
	 */
	private static String sendQueryToEventService(String rawQuery, String notificationPath) {
		// since some symbols (mainly < and >) are escaped in the fragment xml, we need to unescape them.
		// TODO: what should happen if a send request fails
		String query = StringEscapeUtils.unescapeHtml4(rawQuery);

		JsonObject queryRequest = new JsonObject();
		queryRequest.addProperty("queryString", query);
		queryRequest.addProperty("notificationPath", notificationPath);
		Gson gson = new Gson();
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		WebTarget target = client.target(getRestDeployUrl()).path(REST_PATH);

		log.info("The target URL is: " + target.getUri());
		log.info("The queryRequest is: \"" + queryRequest + "\"");
		try {
			Response response = target.request().post(Entity.json(gson.toJson(queryRequest)));
			if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
				// return the UUID of the Notification Rule
				// so that it can be removed later
				return response.readEntity(String.class);
			} else {
				log.warn("Could not register Query \"" + query + "\". Response status: " + response.getStatus());
				return NOTIFICATION_ERROR_ID;
			}
		} catch (ProcessingException e) {
			log.warn("Could not register Query \"" + query + "\"");
			return NOTIFICATION_ERROR_ID;
		}
	}

	/**
	 * De-register all events, which means {@link CaseStartTrigger} and
	 * {@link MessageReceiveEventBehavior}, for a given {@link CaseModel}.
	 * 
	 * @param cm
	 *            - case model which events should be de-registered
	 */
	public static void deregisterEvents(CaseModel cm) {
		// de-register case start trigger
		List<String> notificationRuleIds = cm.getStartCaseTrigger().stream().map(CaseStartTrigger::getNotificationRuleId).collect(Collectors.toList());
		notificationRuleIds.forEach(EventDispatcher::deregisterNotificationRule);

		// de-register receive events
		// TODO refactor and use deregisterReceiveEventsOfCase
		List<CaseExecutioner> caseExecutioners = ExecutionService.getAllCasesOfCaseModel(cm.getId());
		List<MessageReceiveEventBehavior> receiveBehaviors = caseExecutioners.stream().map(CaseExecutioner::getRegisteredEventBehaviors).flatMap(List::stream).collect(Collectors.toList());
		receiveBehaviors.forEach(EventDispatcher::deregisterReceiveEvent);
	}

	/**
	 * De-register all {@link MessageReceiveEventBehavior} for a given
	 * {@link Case}.
	 * 
	 * @param caze
	 *            - Case which events should be de-registered
	 */
	public static void deregisterReceiveEventsOfCase(Case caze) {
		// de-register receive events
		CaseExecutioner caseExecutioner = caze.getCaseExecutioner();
		List<MessageReceiveEventBehavior> receiveBehaviors = caseExecutioner.getRegisteredEventBehaviors();
		receiveBehaviors.forEach(EventDispatcher::deregisterReceiveEvent);
		log.info("Deregistered all MessageReceiveEvents of Case " + caze.getName());
	}

	/**
	 * De-register an event instance from Unicorn by its
	 * {@link MessageReceiveEventBehavior}.
	 * 
	 * @param receiveBehavior
	 *            - {@link MessageReceiveEventBehavior} of the
	 *            {@link AbstractEventInstance}.
	 */
	public static void deregisterReceiveEvent(MessageReceiveEventBehavior receiveBehavior) {
		String notificationRuleId = receiveBehavior.getNotificationRule();
		if (notificationRuleId.equals(NOTIFICATION_ERROR_ID)) {
			return;
		}

		if (deregisterNotificationRule(notificationRuleId)) {
			AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
			CaseExecutioner caseExecutioner = eventInstance.getCaseExecutioner();
			caseExecutioner.removeRegisteredEventBehavior(receiveBehavior);
			log.info(String.format("Event with id %s and notification Rule %s de-registered", eventInstance.getId(), notificationRuleId));
		}
	}

	/**
	 * De-register an notification rule from Unicorn.
	 * 
	 * @param notificationRuleId
	 *            - Id of notification to be de-registered
	 * @return true if the notification rule id was successfully de-registered
	 *         or the event was never registered. false if the notification rule
	 *         id was not successfully de-registered.
	 */
	private static boolean deregisterNotificationRule(String notificationRuleId) {
		if (notificationRuleId.equals(NOTIFICATION_ERROR_ID)) {
			return true;
		}
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		try {
			WebTarget target = client.target(getRestDeployUrl()).path(REST_PATH + "/" + notificationRuleId);
			Response response = target.request().delete();
			if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
				return true;
			}
			log.debug("Could not unregister Query");
		} catch (ProcessingException e) {
			log.warn("Could not unregister Query " + e.getMessage());
		}
		return false;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		EventDispatcher.url = url;
	}

	public static String getRestDeployUrl() {
		return url + REST_DEPLOY_PATH;
	}
}
