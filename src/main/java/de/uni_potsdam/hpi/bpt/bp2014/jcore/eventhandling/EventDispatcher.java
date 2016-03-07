package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.*;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("eventdispatcher/")
public final class EventDispatcher {

    private static final String REST_PATH = "webapi/REST/EventQuery/REST";
    private static final String REST_URL = "http://172.16.64.105:8080/Unicorn-unicorn_BP15_dev/";
    private static final String SELF_URL = "http://172.16.64.113:8080/Chimera";

    private static Logger logger = Logger.getLogger(EventDispatcher.class);

    @Consumes(MediaType.APPLICATION_JSON)
    @Path("scenario/{scenarioID}/instance/{instanceID}/events/{requestKey}")
    public static Response receiveEvent(
            @PathParam("scenarioID") int scenarioId,
            @PathParam("instanceID") int scenarioInstanceId,
            @PathParam("requestKey") String requestId) {
        terminateEvent(requestId, scenarioId, scenarioInstanceId);
        return Response.accepted("Event received.").build();
    }

    public static void terminateEvent(String mappingKey, int scenarioId, int scenarioInstanceId) {
        ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        AbstractEvent event = getEvent(scenarioInstance, mappingKey);
        event.terminate();
        int fragmentInstanceId = event.getFragmentInstanceId();
        if (isExclusiveEvent(event)) {
            discardAllAlternatives(event);
        } else {
            unregisterEvent(event.getControlNodeId(), fragmentInstanceId);
        }
    }

    private static void discardAllAlternatives(AbstractEvent event) {
        DbEventMapping mapping = new DbEventMapping();
        int fragmentInstanceId = event.getFragmentInstanceId();
        List<Integer> alternativeEventNodes = mapping.getAlternativeEventsIds(event);
        alternativeEventNodes.forEach(x -> unregisterEvent(x, fragmentInstanceId));
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
            e.printStackTrace();
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
        DbControlNode controlNode = new DbControlNode();
        EventFactory factory = new EventFactory(instance);
        DbFragmentInstance fragmentInstance = new DbFragmentInstance();

        int eventControlNodeId = eventMapping.getEventControlNodeId(requestId);
        int fragmentInstanceId = eventMapping.getFragmentInstanceId(requestId);

        return factory.getEventForControlNodeId(eventControlNodeId,
                fragmentInstanceId);
    }

    public static String registerEvent(AbstractEvent event, int fragmentInstanceId, int scenarioInstanceId,
                              int scenarioId) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        String notificationRuleId = sendQueryToEventService(
                event.getQueryString(), requestId, scenarioInstanceId, scenarioId);
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveMappingToDatabase(fragmentInstanceId, requestId, event.getControlNodeId(), notificationRuleId);
        return requestId;
    }

    public static void registerExclusiveEvents(List<AbstractEvent> events) {
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveAlternativeEvents(events);
    }

    private static String sendQueryToEventService(String query, String requestId, int scenarioInstanceId,
                                         int scenarioId) {
        logger.debug("Sending EventQuery to Unicorn: " + query + " " + requestId);
        String notificationPath = String.format("%s/api/eventdispatcher/scenario/%d/instance/%d/events/%s",
                SELF_URL, scenarioId, scenarioInstanceId, requestId);

        JsonObject queryRequest = new JsonObject();
        queryRequest.addProperty("queryString", query);
        queryRequest.addProperty("notificationPath", notificationPath);
        Gson gson = new Gson();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(REST_URL).path(REST_PATH);
        Response response = target.request()
                .post(Entity.json(gson.toJson(queryRequest)));
        if (response.getStatus() != 200) {
            // throw new RuntimeException("Query could not be registered");
            logger.debug("Could not register Query");
            return null;
        } else {
            // return the UUID of the Notification Rule
            // so that it can be removed later
            return response.readEntity(String.class);
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
        WebTarget target = client.target(REST_URL).path(REST_PATH + "/" + notificationRuleId);
        Response response = target.request().delete();
        if(response.getStatus() != 200) {
            logger.debug("Could not unregister Query");
        }
    }
}
