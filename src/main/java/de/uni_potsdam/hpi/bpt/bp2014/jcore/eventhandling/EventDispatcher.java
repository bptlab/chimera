package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbEventMapping;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragmentInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.AbstractEvent;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.EventFactory;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.ScenarioInstance;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

/**
 * The event dispatcher class is responsible for manage registrations from Events to RestQueries.
 */
@Path("eventdispatcher/")
public final class EventDispatcher {
    private final String restPath = "webapi/REST/EventQuery/REST";
    private final String restUrl = "http://172.16.64.105:8080/Unicorn-unicorn_BP15_dev/";

    private final String selfUrl = "http://172.16.64.113:8080/Chimera/";


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
        String reveicePath = "scenario/" + scenarioId + "/instance/" + scenarioInstanceId + "/events/";
        String notificationPath = selfUrl + reveicePath + requestId;

        JsonObject queryRequest = new JsonObject();
        queryRequest.addProperty("queryString", query);
        queryRequest.addProperty("notificationPath", notificationPath);
        Gson gson = new Gson();
        HttpPost post = new HttpPost(restUrl + restPath);
        try {
            StringEntity postString = new StringEntity(gson.toJson(queryRequest));
            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
            HttpClient httpClient1 = new DefaultHttpClient(httpParams);
            HttpResponse response = httpClient1.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Query could not be registered");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void unregisterEvent(int eventControlNodeId, int fragmentInstanceId) {
        DbEventMapping eventMapping = new DbEventMapping();
        eventMapping.removeEventMapping(fragmentInstanceId, eventControlNodeId);
    }
}
