package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json.Scenario;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.EventFactory;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.log4j.Logger;

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
import java.util.UUID;

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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("scenario/{scenarioID}/instance/{instanceID}/events/{requestKey}")
    public static Response receiveEvent(
            @PathParam("scenarioID") int scenarioId,
            @PathParam("instanceID") int scenarioInstanceId,
            @PathParam("requestKey") String requestId) {

        ScenarioInstance scenarioInstance = new ScenarioInstance(scenarioId, scenarioInstanceId);
        AbstractEvent event = getEvent(scenarioInstance, requestId);
        event.terminate();
        unregisterEvent(event.getControlNodeId(), event.getFragmentInstanceId());

        return Response.status(Response.Status.ACCEPTED).build();
    }

    private static AbstractEvent getEvent(ScenarioInstance instance, String requestId) {
        DbEventMapping eventMapping = new DbEventMapping();
        DbControlNode controlNode = new DbControlNode();
        EventFactory factory = new EventFactory(instance);
        DbFragmentInstance fragmentInstance = new DbFragmentInstance();

        int eventControlNodeId = eventMapping.getEventControlNodeId(requestId);
        int fragmentId = controlNode.getFragmentId(eventControlNodeId);
        int fragmentInstanceId =
                fragmentInstance.getFragmentInstanceID(fragmentId, instance.getScenarioId());
        return factory.getEventForControlNodeId(eventControlNodeId,
                fragmentInstanceId);
    }

    public static void registerEvent(AbstractEvent event, int fragmentInstanceId, int scenarioInstanceId,
                              int scenarioId) {
        final String requestId = UUID.randomUUID().toString().replaceAll("\\-", "");
        sendQueryToEventService(event.getQueryString(), requestId, scenarioInstanceId, scenarioId);
        DbEventMapping mapping = new DbEventMapping();
        mapping.saveMappingToDatabase(fragmentInstanceId, requestId, event.getControlNodeId());
    }

    private static void sendQueryToEventService(String query, String requestId, int scenarioInstanceId,
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
        String x = gson.toJson(queryRequest);
        Response response = target.request()
                .post(Entity.json(gson.toJson(queryRequest)));
        if (response.getStatus() != 200) {
            // throw new RuntimeException("Query could not be registered");
            logger.debug("Could not register Query");
        }
    }

    public static void unregisterEvent(int eventControlNodeId, int fragmentInstanceId) {
        DbEventMapping eventMapping = new DbEventMapping();
        eventMapping.removeEventMapping(fragmentInstanceId, eventControlNodeId);
    }
}
