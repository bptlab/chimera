package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbCaseStart;
import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.history.DbLogEntry;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.EventFactory;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.TimerEventInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.data.DataAttributeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.executionbehaviors.TimeEventJob;
import de.uni_potsdam.hpi.bpt.bp2014.settings.PropertyLoader;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("eventdispatcher/")
public final class EventDispatcher {

    private static final String REST_PATH = PropertyLoader.getProperty("unicorn.path.query.rest");
    private static final String REST_DEPLOY_URL = PropertyLoader.getProperty("unicorn.url")
            + PropertyLoader.getProperty("unicorn.path.deploy");
    private static final String SELF_DEPLOY_URL = PropertyLoader.getProperty("chimera.url")
            + PropertyLoader.getProperty("chimera.path.deploy");
    private static final String SELF_PATH_NODES = "%s/api/eventdispatcher/scenario/%d/instance/%d/eventnode/%s";
    private static final String SELF_PATH_CASESTART = "%s/api/eventdispatcher/scenario/%d/casestart/%s";

    private static Logger logger = Logger.getLogger(EventDispatcher.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    @Path("scenario/{scenarioId}/instance/{instanceId}/events/{requestKey}")
    public static Response receiveEvent(
            @PathParam("scenarioId") int scenarioId,
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("requestKey") String requestId,
            String eventJson) {
        AbstractEvent event = findEvent(requestId, scenarioId, scenarioInstanceId);
        if (eventJson.isEmpty()) {
            event.terminate();
        } else {
            event.terminate(eventJson);
        }
        unregisterEvent(event);
        return Response.accepted("Event received.").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("casestart/{requestKey}")
    public static Response startCase(@PathParam("requestKey") String requestKey, String eventJson) {
        int scenarioId = new DbCaseStart().getScenarioId(requestKey);
        int scenarioInstanceId = ExecutionService.startNewScenarioInstanceStatic(scenarioId);
        ScenarioInstance instance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        String queryId = new DbCaseStart().getQueryId(requestKey);
        CaseStarter caseStarter = new CaseStarter(scenarioId, queryId);
        try {
            caseStarter.startInstance(eventJson, instance);
        } catch (IllegalStateException e) {
            logger.error("Could not start case from query", e);
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(e.getMessage())
                    .build();
        }
        return Response.ok("Event received.").build();
    }

    public static AbstractEvent findEvent(String requestId, int scenarioId, int instanceId) {
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, instanceId);
        return getEvent(scenarioInstance, requestId);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("scenario/{scenarioId}/instance/{instanceId}/events")
    public static Response getRegisteredEvents(
            @PathParam("instanceId") int scenarioInstanceId,
            @PathParam("scenarioId") int scenarioId) {
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        List<Integer> fragmentIds = scenarioInstance.getFragmentInstances().stream()
                .map(FragmentInstance::getFragmentId)
                .collect(Collectors.toList());
        DbEventMapping eventMapping = new DbEventMapping();
        List<String> requestKeys = fragmentIds.stream()
                .map(eventMapping::getRequestKeysForFragment).flatMap(Collection::stream)
                .collect(Collectors.toList());
        JSONArray jsonArray = new JSONArray(requestKeys);
        return Response.ok().type(MediaType.APPLICATION_JSON).entity(jsonArray.toString()).build();
    }


    public static void unregisterEvent(AbstractEvent event) {
        if (isExclusiveEvent(event)) {
            discardAllAlternatives(event);
        } else {
            unregisterEvent(event.getControlNodeId(), event.getFragmentInstanceId());
        }
    }

    private static void discardAllAlternatives(AbstractEvent event) {
        DbEventMapping mapping = new DbEventMapping();
        int fragmentInstanceId = event.getFragmentInstanceId();
        List<Integer> alternativeEventNodes = mapping.getAlternativeEventsIds(event);
        alternativeEventNodes.forEach(x -> unregisterEvent(x, fragmentInstanceId));
    }

    public static void registerCaseStartEvent(String eventQuery, int scenarioId, String id) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        String notificationPath = String.format(
                SELF_PATH_CASESTART, SELF_DEPLOY_URL, scenarioId, requestId);
        String notificationRuleId = sendQueryToEventService(
                eventQuery, requestId, notificationPath);
        new DbCaseStart().insertCaseStartMapping(requestId, scenarioId, notificationRuleId, id);
    }

    public static void registerTimerEvent(TimerEventInstance event, int fragmentInstanceId,
                                          int scenarioInstanceId, int scenarioId) {
        String mappingKey = registerEvent(event, fragmentInstanceId, scenarioInstanceId, scenarioId);
        Date terminationDate = event.getTerminationDate();
        assert (terminationDate.after(new Date()));
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            Scheduler sched = sf.getScheduler();
            JobDetail job = createJob(mappingKey, scenarioInstanceId, scenarioId);
            SimpleTrigger trigger = (SimpleTrigger) newTrigger()
                    .startAt(terminationDate)
                    .build();
            sched.start();
            sched.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private static JobDetail createJob(String mappingKey, int scenarioInstanceId, int scenarioId) {
        JobDetail timeEventJob = newJob(TimeEventJob.class)
                .withIdentity("1")
                .build();
        timeEventJob.getJobDataMap().put("mappingKey", mappingKey);
        timeEventJob.getJobDataMap().put("scenarioInstanceId", scenarioInstanceId);
        timeEventJob.getJobDataMap().put("scenarioId", scenarioId);
        return timeEventJob;
    }

    private static boolean isExclusiveEvent(AbstractEvent event) {
        DbEventMapping mapping = new DbEventMapping();
        return mapping.isAlternativeEvent(event);
    }

    private static AbstractEvent getEvent(ScenarioInstance instance, String requestId) {
        DbEventMapping eventMapping = new DbEventMapping();
        EventFactory factory = new EventFactory(instance);

        int eventControlNodeId = eventMapping.getEventControlNodeId(requestId);
        int fragmentInstanceId = eventMapping.getFragmentInstanceId(requestId);
        AbstractEvent event = factory.getEventForControlNodeId(eventControlNodeId,
                fragmentInstanceId);
        new DbLogEntry().logEvent(event.getControlNodeInstanceId(),
                instance.getScenarioInstanceId(), "received");
        return event;
    }


    public static String registerEvent(
            AbstractEvent event, int fragmentInstanceId, int scenarioInstanceId, int scenarioId) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        String query = insertAttributesIntoQueryString(
                event.getQueryString(), scenarioInstanceId, scenarioId);
        String notificationRuleId = sendQueryToEventService(
                query, requestId, scenarioInstanceId, scenarioId);
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveMappingToDatabase(
                fragmentInstanceId, requestId, event.getControlNodeId(), notificationRuleId);
        new DbLogEntry().logEvent(event.getControlNodeInstanceId(), scenarioInstanceId, "registered");
        return requestId;
    }

    public static String insertAttributesIntoQueryString(
            String queryString, int scenarioInstanceId, int scenarioId) {
        if (queryString.contains("#")) {
            ScenarioInstance scenario = new ScenarioInstance(scenarioId, scenarioInstanceId);
            for (DataAttributeInstance attribute : scenario
                    .getDataManager().getDataAttributeInstances()) {
                String dataattributePath = String.format("#%s.%s",
                        attribute.getDataObject().getName(), attribute.getName());
                queryString = queryString.replace(dataattributePath,
                        attribute.getValue().toString());
            }
        }
        return queryString;
    }

    public static void registerExclusiveEvents(List<AbstractEvent> events) {
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveAlternativeEvents(events);
    }

    private static String sendQueryToEventService(String rawQuery, String requestId, int scenarioInstanceId,
                                         int scenarioId) {
        String notificationPath = String.format(SELF_PATH_NODES,
                SELF_DEPLOY_URL, scenarioId, scenarioInstanceId, requestId);
        return sendQueryToEventService(rawQuery, requestId, notificationPath);
    }

    private static String sendQueryToEventService(
            String rawQuery, String requestId, String notificationPath) {
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
            Response response = target.request()
                    .post(Entity.json(gson.toJson(queryRequest)));
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
            logger.warn("Could not register Query \"" + query+ "\"");
            return "-1";
        }
    }

    public static void unregisterEvent(int eventControlNodeId, int fragmentInstanceId) {
        DbEventMapping eventMapping = new DbEventMapping();
        String notificationRuleId = eventMapping.getNotificationRuleId(eventControlNodeId);

        unregisterNotificationRule(notificationRuleId);
        eventMapping.removeEventMapping(fragmentInstanceId, eventControlNodeId);
    }

    private static void unregisterNotificationRule(String notificationRuleId) {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT, 1000);
        try {
            WebTarget target = client.target(REST_DEPLOY_URL).path(REST_PATH + "/" + notificationRuleId);
            Response response = target.request().delete();
            if(response.getStatus() != 200) {
                logger.debug("Could not unregister Query");
            }
        } catch (ProcessingException e) {
            logger.debug("Could not unregister Query");
        }
    }
}
