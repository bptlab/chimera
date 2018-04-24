package de.hpi.bpt.chimera.execution.controlnodes.event.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.ExecutionService;
import de.hpi.bpt.chimera.execution.controlnodes.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.MessageReceiveEventBehavior;
import de.hpi.bpt.chimera.execution.controlnodes.event.behavior.TimerEventBehavior;
import de.hpi.bpt.chimera.execution.exception.IllegalCaseModelIdException;
import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.behavior.MessageReceiveDefinition;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.rest.AbstractRestService;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("eventdispatcher/")
public class EventDispatcher extends AbstractRestService {

	private static final String REST_PATH = PropertyLoader.getProperty("unicorn.path.query.rest");
	private static final String REST_DEPLOY_URL = PropertyLoader.getProperty("unicorn.url") + PropertyLoader.getProperty("unicorn.path.deploy");
	private static final String SELF_DEPLOY_URL = PropertyLoader.getProperty("chimera.url") + PropertyLoader.getProperty("chimera.path.deploy");
	private static final String SELF_PATH_NODES = "%s/api/eventdispatcher/scenario/%s/instance/%s/events/%s";
	private static final String SELF_PATH_CASESTART = "%s/api/eventdispatcher/scenario/%s/casestart/%s";
	private static final String NOTIFICATION_ERROR_ID = "-1";

	private static Logger log = Logger.getLogger(EventDispatcher.class);

	/**
	 * This method notifies that a certain event instance received Event was
	 * received. In addition the event instance will be terminated.
	 * 
	 * @param cmId
	 *            The id of a case model.
	 * @param caseId
	 *            - the id of case.
	 * @param requestId
	 *            - the id of the event instance receiving the event
	 * @param eventJson
	 *            - the json content for the event instance
	 * @return Response 202 because the Event was received by Chimera
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/events/{requestKey}")
	public Response receiveEvent(@PathParam("scenarioId") String cmId, @PathParam("instanceId") String caseId, @PathParam("requestKey") String requestId, String eventJson) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(cmId, caseId);
			MessageReceiveEventBehavior receiveBehavior = caseExecutioner.getRegisteredEventBehavior(requestId);
			if (eventJson.isEmpty() || "{}".equals(eventJson)) {
				receiveBehavior.setEventJson("");
			} else {
				receiveBehavior.setEventJson(eventJson);
			}
			AbstractEventInstance eventInstance = receiveBehavior.getEventInstance();
			eventInstance.terminate();
			SseNotifier.notifyRefresh();
		} catch (Exception e) {
			log.error("Error while processing a received event", e);
		}
		return Response.accepted("Event received.").build();
	}

	/**
	 * Start a new Case of a specific {@link CaseModel} by one of the case
	 * model's registered CaseStartTriggers.
	 * 
	 * @param cmId
	 *            The id of a case model.
	 * @param requestId
	 *            - the id of the {@link CaseStartTrigger} receiving the event
	 * @param eventJson
	 *            - the json content for writing at the first data objects of
	 *            the new case
	 * @return Response 200 because the Event was received by Chimera
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/casestart/{requestKey}")
	public Response startCase(@PathParam("scenarioId") String cmId, @PathParam("requestKey") String requestKey, String eventJson) {

		log.info("An Event started a Case via REST-Interface.");

		try {
			CaseModel cm = CaseModelManager.getCaseModel(cmId);
			Optional<CaseStartTrigger> caseStartTrigger = cm.getStartCaseTrigger().stream().filter(c -> c.getId().equals(requestKey)).findFirst();
			if (!caseStartTrigger.isPresent()) {
				String message = String.format("The case start trigger id: %s is not assigned", requestKey);
				log.error(message);
				return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildError(message)).build();
			}

			CaseExecutioner caseExecutioner = ExecutionService.createCaseExecutioner(cm, "Automatically Created Case");

			CaseStarter caseStarter = new CaseStarter(caseStartTrigger.get());
			caseStarter.startCase(eventJson, caseExecutioner);
			SseNotifier.notifyRefresh();
		} catch (IllegalCaseModelIdException e) {
			log.error("Could not start case from query", e);
			return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(e.getMessage()).build();
		}
		return Response.ok("Event received.").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/events")
	public Response getRegisteredEvents(@PathParam("instanceId") int scenarioInstanceId, @PathParam("scenarioId") int scenarioId) {
		log.error("This function doesn't work with Chimera 2.0");
		// ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId,
		// scenarioInstanceId);
		// List<Integer> fragmentIds =
		// scenarioInstance.getFragmentInstances().stream().map(FragmentInstance::getFragmentId).collect(Collectors.toList());
		// DbEventMapping eventMapping = new DbEventMapping();
		// List<String> requestKeys =
		// fragmentIds.stream().map(eventMapping::getRequestKeysForFragment).flatMap(Collection::stream).collect(Collectors.toList());
		// JSONArray jsonArray = new JSONArray(requestKeys);
		// Response.ok().type(MediaType.APPLICATION_JSON).entity(jsonArray.toString()).build();
		return Response.serverError().type("unimplemented in Chimera 2.0").build();
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

	private static JobDetail createJob(AbstractEventInstance event, TimerEventBehavior timerBehavior) {
		JobDetail timeEventJob = newJob(TimeEventJob.class).usingJobData("CaseModelId", event.getCaseExecutioner().getCaseModel().getId()).usingJobData("CaseId", event.getFragmentInstance().getCase().getId()).usingJobData("ControlNodeInstanceId", event.getId()).build();
		timerBehavior.setJobKey(timeEventJob.getKey());
		return timeEventJob;
	}

	// TODO: will we need something like EventLog?


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
		WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH);
		log.debug("The target URL is: " + target.getUri());
		log.debug("The queryRequest is: \"" + queryRequest + "\"");
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
		List<String> notificationRuleIds = cm.getStartCaseTrigger().stream().map(CaseStartTrigger::getNotificationRuleId).collect(Collectors.toList());
		notificationRuleIds.forEach(EventDispatcher::deregisterNotificationRule);

		List<CaseExecutioner> caseExecutioners = ExecutionService.getAllCasesOfCaseModel(cm.getId());
		List<MessageReceiveEventBehavior> receiveBehaviors = caseExecutioners.stream().map(CaseExecutioner::getRegisteredEventBehaviors).flatMap(List::stream).collect(Collectors.toList());
		receiveBehaviors.forEach(EventDispatcher::deregisterReceiveEvent);
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
			WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH + "/" + notificationRuleId);
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
}
