package de.hpi.bpt.chimera.execution.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.execution.DataAttributeInstance;
import de.hpi.bpt.chimera.execution.event.AbstractEventInstance;
import de.hpi.bpt.chimera.execution.event.AbstractIntermediateCatchEventInstance;
import de.hpi.bpt.chimera.execution.event.TimerEventInstance;
import de.hpi.bpt.chimera.model.condition.CaseStartTrigger;
import de.hpi.bpt.chimera.model.fragment.bpmn.event.AbstractEvent;
import de.hpi.bpt.chimera.persistencemanager.CaseModelManager;
import de.hpi.bpt.chimera.util.PropertyLoader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("eventdispatcher/")
public final class EventDispatcher {

	private static final String REST_PATH = PropertyLoader.getProperty("unicorn.path.query.rest");
	private static final String REST_DEPLOY_URL = PropertyLoader.getProperty("unicorn.url") + PropertyLoader.getProperty("unicorn.path.deploy");
	private static final String SELF_DEPLOY_URL = PropertyLoader.getProperty("chimera.url") + PropertyLoader.getProperty("chimera.path.deploy");
	private static final String SELF_PATH_NODES = "%s/api/eventdispatcher/scenario/%d/instance/%d/events/%s";
	private static final String SELF_PATH_CASESTART = "%s/api/eventdispatcher/casestart/%s";

	private static Logger logger = Logger.getLogger(EventDispatcher.class);

	// TODO move these maps in the EventMapper and add persistence or move them
	// into the Case, so that each Case is responsible for its own events.
	private static Map<String, AbstractIntermediateCatchEventInstance> idToRegisteredEvent = new HashMap<>();
	private static Map<String, AbstractIntermediateCatchEventInstance> keyToRegisteredEvent = new HashMap<>();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)

	@Path("scenario/{scenarioId}/instance/{instanceId}/events/{requestKey}")
	public static Response receiveEvent(@PathParam("scenarioId") int scenarioId, @PathParam("instanceId") int scenarioInstanceId, @PathParam("requestKey") String requestId, String eventJson) {
		AbstractIntermediateCatchEventInstance event = keyToRegisteredEvent.get(requestId);
		if (eventJson.isEmpty() || "{}".equals(eventJson)) {
			event.terminate("");
		} else {
			event.terminate(eventJson);
		}
		/// unregisterEvent(event); >>>>>is now responsibility of
		/// AbstractIntermediateCatchEvent
		return Response.accepted("Event received.").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("casestart/{requestKey}")
	public static Response startCase(@PathParam("requestKey") String requestKey, String eventJson) {
		logger.info("CaseSartEvent received via REST-Interface.");
		CaseStartTrigger caseStartTrigger = CaseModelManager.getCaseStartTrigger(requestKey);
		// int scenarioInstanceId =
		// ExecutionService.startNewScenarioInstanceStatic(scenarioId);
		// ScenarioInstance instance = new ScenarioInstance(scenarioId,
		// scenarioInstanceId);
		// String queryId = new DbCaseStart().getQueryId(requestKey);
		// TODO: setDataAttributes
		// TODO: Maybe directly give the CaseModel via getParenCaseModel() to
		// ExecutionService
		CaseExecutioner caseExecutioner = de.hpi.bpt.chimera.execution.ExecutionService.createCaseExecutioner(caseStartTrigger.getParentCaseModel().getId(), "AutomaticallyCreatedInstance");
		logger.info("An Event started a Case via REST-Interface.");
		CaseStarter caseStarter = new CaseStarter(caseStartTrigger);
		try {
			caseStarter.startCase(eventJson, caseExecutioner);
			SseNotifier.notifyRefresh();
		} catch (IllegalStateException e) {
			logger.error("Could not start case from query", e);
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
	public static Response getRegisteredEvents(@PathParam("instanceId") int scenarioInstanceId, @PathParam("scenarioId") int scenarioId) {
		logger.error("This function doesn't works with Chimera 2.0");
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

	public static void registerCaseStartEvent(CaseStartTrigger caseStartTrigger) {
		final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
		String notificationPath = String.format(SELF_PATH_CASESTART, SELF_DEPLOY_URL, requestId);
		String notificationRuleId = sendQueryToEventService(caseStartTrigger.getQueryExecutionPlan(), requestId, notificationPath);
		caseStartTrigger.setEventKeyId(requestId);
		caseStartTrigger.setNotificationRuleId(notificationRuleId);
		CaseModelManager.getEventMapper().registerCaseStartTrigger(requestId, caseStartTrigger);
	}

	public static void registerTimerEvent(TimerEventInstance event) {
		// String mappingKey = registerEvent(event, fragmentInstanceId,
		// scenarioInstanceId, scenarioId);
		Date terminationDate = event.getTerminationDate();
		assert (terminationDate.after(new Date())) : "Traveling back in time is not implemented yet, see feature request #CM-(-243)";
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			Scheduler sched = sf.getScheduler();
			JobDetail job = createJob(event);
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().startAt(terminationDate).build();
			sched.start();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}

	}

	private static JobDetail createJob(TimerEventInstance event) {
		JobDetail timeEventJob = newJob(TimeEventJob.class).usingJobData("CaseModelId", event.getCaseExecutioner().getCaseModel().getId()).usingJobData("CaseId", event.getFragmentInstance().getCase().getId()).usingJobData("ControlNodeInstanceId", event.getId()).build();
		event.setJobKey(timeEventJob.getKey());
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


	public static void registerEvent(AbstractIntermediateCatchEventInstance event) {
		logger.info("trying to register an Event at unicorn");
		final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
		String query = insertAttributesIntoQueryString(event);
		logger.info("Created query to register an Event at unicorn");
		String notificationRuleId = sendQueryToEventService(query, requestId, 0 , 0);//last to Parameters 0 because we dont Integer Ids anymore but have to fit the unicorn Interface
		event.setNotificationRule(notificationRuleId);
		event.setUnicornKey(requestId);
		
		if (!idToRegisteredEvent.containsKey(event.getId())) {
			idToRegisteredEvent.put(event.getId(), event);
			keyToRegisteredEvent.put(requestId, event);
		}
		
		logger.info(String.format("Registered event with id %s and eventQuery %s at unicorn, getting back %s as NotificationRule", event.getId(), query, notificationRuleId));
		
	}

	public static String insertAttributesIntoQueryString(AbstractIntermediateCatchEventInstance event) {
		String queryString = event.getControlNode().getEventQuery();
		if (queryString.contains("#")) {
			// ScenarioInstance scenario = new ScenarioInstance(scenarioId,
			// scenarioInstanceId);
			for (DataAttributeInstance attribute : event.getDataManager().getDataAttributeInstances()) {
				String dataattributePath = String.format("#%s.%s", attribute.getDataObject().getDataClass().getName(), attribute.getDataAttribute().getName());
				queryString = queryString.replace(dataattributePath, attribute.getValue().toString());
			}
		}
		return queryString;
	}

	/**
	 * Saves that events are alternative to each other, so that they can be skipped when
	 * one of the is triggered.
	 * The events have to be registered
	 *
	 * @param events the events that are alternative to each other (e.g. behind a event
	 *               based gateway)
	 */
	// not needed anymore
	// public static void registerExclusiveEvents(List<AbstractEvent> events) {
	// DbEventMapping mapping = new DbEventMapping();
	// mapping.saveAlternativeEvents(events);
	// events.forEach(AbstractEvent::enableControlFlow);
	// }

	private static String sendQueryToEventService(String rawQuery, String requestId, int scenarioInstanceId, int scenarioId) {
		String notificationPath = String.format(SELF_PATH_NODES, SELF_DEPLOY_URL, scenarioId, scenarioInstanceId, requestId);
		return sendQueryToEventService(rawQuery, requestId, notificationPath);
	}

	private static String sendQueryToEventService(String rawQuery, String requestId, String notificationPath) {
		// since some symbols (mainly < and >) are escaped in the fragment xml, we need to unescape them.
		String query = StringEscapeUtils.unescapeHtml4(rawQuery);
		logger.debug("Sending EventQuery to Unicorn: " + query + " " + requestId);

		JsonObject queryRequest = new JsonObject();
		queryRequest.addProperty("queryString", query);
		queryRequest.addProperty("notificationPath", notificationPath);
		Gson gson = new Gson();
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH);
		try {
			Response response = target.request().post(Entity.json(gson.toJson(queryRequest)));
			if (response.getStatus() != 200) {
				// throw new RuntimeException("Query could not be registered");
				logger.warn("Could not register Query \"" + query + "\"");
				return "-1";
			} else {
				// return the UUID of the Notification Rule
				// so that it can be removed later
				return response.readEntity(String.class);
			}
		} catch (ProcessingException e) {
			logger.warn("Could not register Query \"" + query + "\"");
			return "-1";
		}
	}

	public static void unregisterEvent(AbstractIntermediateCatchEventInstance event) {
		String notificationRuleId = event.getNotificationRule();

		unregisterNotificationRule(notificationRuleId);
		keyToRegisteredEvent.remove(event.getUnicornKey());
		idToRegisteredEvent.remove(event.getId());
		logger.info(String.format("Event with id %s and notification Rule %s unregistered", event.getId(), notificationRuleId));
	}

	private static void unregisterNotificationRule(String notificationRuleId) {
		Client client = ClientBuilder.newClient();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
		client.property(ClientProperties.READ_TIMEOUT, 1000);
		try {
			WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH + "/" + notificationRuleId);
			Response response = target.request().delete();
			if (response.getStatus() != 200) {
				logger.debug("Could not unregister Query");
			}
		} catch (ProcessingException e) {
			logger.debug("Could not unregister Query");
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
