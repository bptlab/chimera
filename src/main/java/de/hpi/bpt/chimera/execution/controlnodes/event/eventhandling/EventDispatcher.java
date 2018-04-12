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
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
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

	private static Logger log = Logger.getLogger(EventDispatcher.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("scenario/{scenarioId}/instance/{instanceId}/events/{requestKey}")
	public Response receiveEvent(@PathParam("scenarioId") String scenarioId, @PathParam("instanceId") String scenarioInstanceId, @PathParam("requestKey") String requestId, String eventJson) {
		try {
			CaseExecutioner caseExecutioner = ExecutionService.getCaseExecutioner(scenarioId, scenarioInstanceId);
			AbstractEventInstance eventInstance = caseExecutioner.getRegisteredEventFromRegistrationKey(requestId);
			MessageReceiveEventBehavior receiveBehavior = (MessageReceiveEventBehavior) eventInstance.getBehavior();
			if (eventJson.isEmpty() || "{}".equals(eventJson)) {
				receiveBehavior.setEventJson("");
			} else {
				receiveBehavior.setEventJson(eventJson);
			}
			eventInstance.terminate();
			SseNotifier.notifyRefresh();
		} catch (Exception e) {
			log.error("Error while processing a received event", e);
		}
		return Response.accepted("Event received.").build();
		// AbstractEventInstance eventInstance =
		// keyToRegisteredEvent.get(requestId);
		// MessageReceiveEventBehavior receiveBehavior =
		// (MessageReceiveEventBehavior) eventInstance.getBehavior();
		// if (eventJson.isEmpty() || "{}".equals(eventJson)) {
		// receiveBehavior.setEventJson("");
		// } else {
		// receiveBehavior.setEventJson(eventJson);
		// }
		// eventInstance.terminate();
		// SseNotifier.notifyRefresh();
		// /// unregisterEvent(event); >>>>>is now responsibility of
		// /// AbstractIntermediateCatchEvent
		// return Response.accepted("Event received.").build();
	}

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
				return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(buildException(message)).build();
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

	// Not supported anymore because we dont use Integers as Ids anymore.
	// public static AbstractEvent findEvent(String requestId, int scenarioId,
	// int instanceId) {
	// ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId,
	// instanceId);
	// return getEvent(scenarioInstance, requestId);
	// }

	// TODO Do we need this function? Doesnt works with the new Chimera
	// architecture because we dont uses Integers as ids any more.
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


	// Not needed anymore???
	// public static void unregisterEvent(AbstractIntermediateCatchEventInstance
	// event) {
	// if (isExclusiveEvent(event)) {
	// discardAllAlternatives(event);
	// } else {
	// unregisterEvent(event.getControlNodeId(), event.getFragmentInstanceId());
	// }
	// }

	// private static void discardAllAlternatives(AbstractEvent event) {
	// DbEventMapping mapping = new DbEventMapping();
	// int fragmentInstanceId = event.getFragmentInstanceId();
	// List<Integer> alternativeEventNodes =
	// mapping.getAlternativeEventsIds(event);
	// alternativeEventNodes.forEach(x -> unregisterEvent(x,
	// fragmentInstanceId));
	// }

	public static void registerCaseStartEvent(CaseModel cm, CaseStartTrigger caseStartTrigger) {
		final String requestId = caseStartTrigger.getId();
		String notificationPath = String.format(SELF_PATH_CASESTART, SELF_DEPLOY_URL, cm.getId(), requestId);
		String notificationRuleId = sendQueryToEventService(caseStartTrigger.getQueryExecutionPlan(), notificationPath);
		caseStartTrigger.setNotificationRuleId(notificationRuleId);
	}

	public static void registerTimerEvent(AbstractEventInstance event, TimerEventBehavior timerBehavior) {
		// String mappingKey = registerEvent(event, fragmentInstanceId,
		// scenarioInstanceId, scenarioId);
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

	// private static boolean isExclusiveEvent(AbstractEvent event) {
	// DbEventMapping mapping = new DbEventMapping();
	// return mapping.isAlternativeEvent(event);
	// }


	// Not supported anymore because we dont use Integers as Ids anymore.
	// private static AbstractEvent getEvent(ScenarioInstance instance, String
	// requestId) {
	// DbEventMapping eventMapping = new DbEventMapping();
	// EventFactory factory = new EventFactory(instance);
	//
	// int eventControlNodeId = eventMapping.getEventControlNodeId(requestId);
	// int fragmentInstanceId = eventMapping.getFragmentInstanceId(requestId);
	// AbstractEvent event =
	// factory.getEventForControlNodeId(eventControlNodeId, fragmentInstanceId);
	// new DbLogEntry().logEvent(event.getControlNodeInstanceId(),
	// instance.getId(), "received");
	// return event;
	// }

	/**
	 * Register an event instance in Unicorn. The notificationRuleId is set in
	 * {@link MessageReceiveEventBehavior} and the MessageReceiveEventBehavior
	 * will be stored in registered event instances in the case executioner of
	 * the event instance.
	 * 
	 * @param receiveBehavior
	 *            - MessageReceiveEventBehavior of the EventInstance to register
	 */
	public static void registerEvent(MessageReceiveEventBehavior receiveBehavior) {
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
		
		if (caseExecutioner.getRegisteredEventFromEventId(eventInstance.getId()) == null) {
			caseExecutioner.registerEvent(requestId, eventInstance);
		}
		log.info(String.format("Registered event with id %s and eventQuery %s at unicorn with the requestId %s, getting back %s as NotificationRule", eventInstance.getId(), query, requestId, notificationRuleId));
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
				// throw new RuntimeException("Query could not be registered");
				log.warn("Could not register Query \"" + query + "\". Response status: " + response.getStatus());
				return "-1";
			}
		} catch (ProcessingException e) {
			log.warn("Could not register Query \"" + query + "\"");
			return "-1";
		}
	}

	public static void unregisterEvent(AbstractEventInstance eventInstance, MessageReceiveEventBehavior receiveBehavior) {
		String notificationRuleId = receiveBehavior.getNotificationRule();

		unregisterNotificationRule(notificationRuleId);
		CaseExecutioner caseExecutioner = eventInstance.getCaseExecutioner();
		caseExecutioner.removeEvent(receiveBehavior.getRequestKey(), eventInstance);
		log.info(String.format("Event with id %s and notification Rule %s unregistered", eventInstance.getId(), notificationRuleId));
	}

	private static void unregisterNotificationRule(String notificationRuleId) {
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		try {
			WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH + "/" + notificationRuleId);
			Response response = target.request().delete();
			if (response.getStatus() != 200) {
				log.debug("Could not unregister Query");
			}
		} catch (ProcessingException e) {
			log.debug("Could not unregister Query");
		}
	}

	// TODO: unregister
	/*
	public static void unregisterCaseStartEvent(int scenarioId) {
		DbCaseStart caseStart = new DbCaseStart();
		List<String> requestKeys = caseStart.getRequestKeys(scenarioId);
		List<String> notificationRuleIds = requestKeys.stream().map(x -> caseStart.getNotificationRuleId(scenarioId, x)).collect(Collectors.toList());

		notificationRuleIds.forEach(EventDispatcher::unregisterNotificationRule);

		requestKeys.forEach(caseStart::deleteCaseMapping);
	}
	*/
}
